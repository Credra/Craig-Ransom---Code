import numpy as np
import cv2
import time
from sklearn.cluster import DBSCAN
import threading

A=np.array([[1.19428128e+03, 0.00000000e+00, 5.96762146e+02],
 [0.00000000e+00, 1.85457444e+03, 5.01302537e+02],
 [0.00000000e+00, 0.00000000e+00, 1.00000000e+00]])
Rt=np.array([
    [1,0,0,0],
    [0,1,0,0],
    [0,0,1,0]
])
cam1Mat = A@Rt

A=np.array(
[[7.26392206e+02, 0.00000000e+00, 9.09392672e+02],
 [0.00000000e+00, 1.04221291e+03, 2.35853057e+00],
 [0.00000000e+00, 0.00000000e+00, 1.00000000e+00]]
)

Rt=np.array([
[[ 0.55082638, -0.01817002,  0.83442204, -2.45089743],
 [-0.04237055,  0.99786509,  0.04969913, 0.5435101],
 [-0.83354366, -0.06273052,  0.54888054, 0.39246079]]
])

cam2Mat= A@Rt
camMat=np.append(cam1Mat,cam2Mat)
class camThread(threading.Thread):
    def __init__(self, previewName, camID):
        threading.Thread.__init__(self)
        self.previewName = previewName
        self.camID = camID
    def run(self):
        print ("Starting " + self.previewName)
        camPreview(self.previewName, self.camID)

position = [-1,-1]
ready = False

def camPreview(previewName, camID):
    global position, ready

    cap = cv2.VideoCapture(camID, cv2.CAP_DSHOW)
    if (cap.isOpened() == False):
        print("Error opening video file")
    ret, back = cap.read()
    backSub = cv2.createBackgroundSubtractorKNN()
    while (cap.isOpened()):
        # while (time.time()-prev)<30e-3:{}
        # prev=time.time()

        # Capture frame-by-frame
        ret, frame = cap.read()
        if ret == True:
            frame = cv2.resize(frame, (780, 540), interpolation=cv2.INTER_LINEAR)
            fgMask = backSub.apply(frame)
            imask = fgMask > 0
            out = np.zeros_like(frame, np.uint8)
            out[imask] = frame[imask]
            hsv = cv2.cvtColor(out, cv2.COLOR_BGR2HSV)
            #mask = cv2.inRange(hsv, (167, 144, 177), (175, 255, 255))
            mask = cv2.inRange(hsv, (157, 167, 15), (179, 231, 167))

            imask = mask > 0
            x, y = np.where(imask == True)
            out = np.zeros_like(frame, np.uint8)
            out[imask] = frame[imask]

            if len(x) > 5:
                arr = np.stack((x, y), 1)
                clustering = DBSCAN(eps=1, min_samples=5).fit(arr)
                mask = np.where(clustering.labels_ == 0)
                y, x = np.mean(arr[mask], 0)
                if not (np.isnan(x) or np.isnan(y)):
                    x = int(x)
                    y = int(y)
                    cv2.line(frame, [x, 0], [x, 540], [0, 0, 0])
                    cv2.line(frame, [0, y], [780, y], [0, 0, 0])
                    if camID == 1:
                        position=[x,y]
                        ready = True
                    else:
                        if ready:
                            points=[0,0,0]
                            print(np.array([[position[0],position[1]],[x,y]]))
                            cv2.triangulatePoints(np.array([[position[0],position[1]],[x,y]]), camMat, points)
                            text=str('x:' + str(points[0]) + '\n'+'y:' + str(points[1]) + '\n'+'z:' + str(points[2]))
                            cv2.putText(frame,
                                        text,
                                        (50, 50),
                                        font, 3,
                                        (0, 255, 255),
                                        2,
                                        cv2.LINE_4)
                        ready = False
            cv2.imshow(previewName, frame)
            # Press Q on keyboard to exit
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        # Break the loop
        else:
            break
    cv2.destroyWindow(previewName)

# Create two threads as follows
thread1 = camThread("Camera 1",1)
thread2 = camThread("Camera 2", 2)

thread1.start()
thread2.start()

cv2.destroyAllWindows()
