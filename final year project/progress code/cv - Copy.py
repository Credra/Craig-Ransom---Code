import numpy as np
import cv2
import time
from sklearn.cluster import DBSCAN


cap0 = cv2.VideoCapture('newBowl0.avi')
cap1 = cv2.VideoCapture('newBowl1.avi')

if (cap0.isOpened() == cap1.isOpened() == False):
    print("Error opening video file")

while (cap0.isOpened() and cap1.isOpened()):
    # while (time.time()-prev)<30e-3:{}
    # prev=time.time()

    # Capture frame-by-frame
    ret0, frame0 = cap0.read()
    ret1, frame1 = cap1.read()
    if ret0 == ret1 == True:
        hsv0 = cv2.cvtColor(frame0, cv2.COLOR_BGR2HSV)
        hsv1 = cv2.cvtColor(frame1, cv2.COLOR_BGR2HSV)
        mask0 = cv2.inRange(hsv0, (167, 144, 177), (175, 255, 255))
        mask1 = cv2.inRange(hsv1, (167, 144, 177), (175, 255, 255))

        imask = mask0 > 0
        x, y = np.where(imask == True)
        out = np.zeros_like(frame0, np.uint8)
        out[imask] = frame0[imask]

        if len(x) > 5:
            arr = np.stack((x, y), 1)
            clustering = DBSCAN(eps=1, min_samples=5).fit(arr)
            mask = np.where(clustering.labels_ == 0)
            y, x = np.mean(arr[mask], 0)
            if not (np.isnan(x) or np.isnan(y)):
                x = int(x)
                y = int(y)
                cv2.line(frame0, [x, 0], [x, 540], [0, 0, 0])
                cv2.line(frame0, [0, y], [780, y], [0, 0, 0])

        imask = mask1 > 0
        x, y = np.where(imask == True)
        out = np.zeros_like(frame1, np.uint8)
        out[imask] = frame1[imask]

        if len(x) > 5:
            arr = np.stack((x, y), 1)
            clustering = DBSCAN(eps=1, min_samples=5).fit(arr)
            mask = np.where(clustering.labels_ == 0)
            y, x = np.mean(arr[mask], 0)
            if not (np.isnan(x) or np.isnan(y)):
                x = int(x)
                y = int(y)
                cv2.line(frame1, [x, 0], [x, 540], [0, 0, 0])
                cv2.line(frame1, [0, y], [780, y], [0, 0, 0])

        cv2.imshow("Camera 0", frame0)
        cv2.imshow("Camera 1", frame1)

        # Press Q on keyboard to exit
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Break the loop
    else:
        break

cv2.destroyAllWindows()
