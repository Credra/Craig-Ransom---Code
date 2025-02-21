import cv2
import numpy as np
from datetime import datetime
from sklearn.cluster import DBSCAN as DBSCANLib

SHOWALL = False
FILENAME = 'newBowl'

DBMIN = 10
DBEPS = 3
FRAMEHEIGHT = 720
FRAMEWIDTH = 1280
HSVL =[]
HSVH = []
P0 = [
    [959.8988088552329,0.0,637.4274955938772,0.0],
    [0.0,959.7811548094121,346.7334052012085,0.0],
    [0.0,0.0,1.0,0.0],
]

P1 = [
    [947.7850357406393,-97.70559240180245,639.1801542655036,46506.4763023842],
    [14.884396689720854,905.9932191147809,451.211733071673,-4947.0997509346],
    [-0.010017589424166955,-0.12314988350524375,0.9923375202494228,2.1709617049620857],
]
P1 = np.array(P1)
P2 = np.array(P2)
def DLT(point1, point2):
    global P1, P2
    A = [point1[1]*P1[2,:] - P1[1,:],
         P1[0,:] - point1[0]*P1[2,:],
         point2[1]*P2[2,:] - P2[1,:],
         P2[0,:] - point2[0]*P2[2,:]
        ]
    A = np.array(A).reshape((4,4))

    B = A.transpose() @ A
    U, s, Vh = np.linalg.svd(B, full_matrices = False)

    return Vh[3,0:3]/Vh[3,3]


def bSub(OG, frame):
    return OG.apply(frame)>0

def HSV(frame):
    return cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), (167, 144, 177), (175, 255, 255)) > 0

def DBSCAN(mask):
    global DBMIN, DBEPS
    x, y = np.where(mask == True)
    if len(x) > DBMIN:
        arr = np.stack((x, y), 1)
        clustering = DBSCANLib(eps=DBEPS, min_samples=DBMIN).fit(arr)
        mask0 = np.where(clustering.labels_ == np.argmax(np.bincount(clustering.labels_ + 1)) - 1)
        y, x = np.mean(arr[mask0], 0)
        if not (np.isnan(x) or np.isnan(y)):
            return np.array([x,y])
    return [False]


# Create an object to read
# from camera
video0 = cv2.VideoCapture(FILENAME+'0.avi')
video1 = cv2.VideoCapture(FILENAME+'1.avi')
time = np.loadtxt(FILENAME+'.txt')

# We need to check if camera
# is opened previously or not
if (video0.isOpened() == False):
    print("Error reading video file")

i=0

prevTime = datetime.now()
start=0
backSub0 = cv2.createBackgroundSubtractorKNN()
backSub1 = cv2.createBackgroundSubtractorKNN()
while (True):

    ret0, frame0 = video0.read()
    ret1, frame1 = video1.read()
    #frame0 = cv2.resize(frame0, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
    #frame1 = cv2.resize(frame1, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
    if ret0 == ret1 == True:
        while ((datetime.now() - prevTime).total_seconds() * 10 ** 3)<time[i]:{}
        prevTime = datetime.now()

        out0 = np.zeros_like(frame0, np.uint8)
        out1 = np.zeros_like(frame1, np.uint8)

        mask0 = bSub(backSub0, frame0)
        mask1 = bSub(backSub1, frame1)

        out0[mask0] = frame0[mask0]
        out1[mask1] = frame1[mask1]


        mask0 = HSV(out0)
        mask1 = HSV(out1)
        if SHOWALL:
            Hout0 = np.zeros_like(frame0, np.uint8)
            Hout1 = np.zeros_like(frame0, np.uint8)

            Hout0[mask0] = out0[mask0]
            Hout1[mask1] = out1[mask1]

        pos0 = DBSCAN(mask0)
        if pos0[0] != False:
            x0 = int(pos0[0])
            y0 = int(pos0[1])
            cv2.line(frame0, [x0, 0], [x0, FRAMEHEIGHT], [0, 0, 0])
            cv2.line(frame0, [0, y0], [FRAMEWIDTH, y0], [0, 0, 0])
            pos1 = DBSCAN(mask1)
            if pos1[0] != False:
                x1 = int(pos1[0])
                y1 = int(pos1[1])

                cv2.line(frame1, [x1, 0], [x1, FRAMEHEIGHT], [0, 0, 0])
                cv2.line(frame1, [0, y1], [FRAMEWIDTH, y1], [0, 0, 0])
                x, y, z = DLT(pos0,pos1)
                text = str('X:' + str(np.round(x, 2)) + ' Y:' + str(np.round(y, 2)) + ' Z:' + str(np.round(z, 2)))
                cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 255), 2, cv2.LINE_4)

        cv2.imshow('Frame1', frame1)
        cv2.imshow('Frame0', frame0)

        if SHOWALL:
            cv2.imshow('B0', out0)
            cv2.imshow('B1', out1)

            cv2.imshow('H0', Hout0)
            cv2.imshow('H1', Hout1)

        # Press S on keyboard
        # to stop the process
        if cv2.waitKey(1) & 0xFF == ord('s'):
            break
        i+=1;

    # Break the loop
    else:
        break

# When everything done, release
# the video capture and video
# write objects
video0.release()
video1.release()

# Closes all the frames
cv2.destroyAllWindows()
