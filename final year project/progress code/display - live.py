import cv2
import numpy as np
from datetime import datetime
from sklearn.cluster import DBSCAN as DBSCANLib

SHOWALL = False

DBMIN = 10
DBEPS = 3
FRAMEHEIGHT = 720
FRAMEWIDTH = 1280
HSVL =[166,255,255]
HSVH = [181,255,113]

EPSILON = 0.01

P0=[
[1461.270739062491, 0.0, 568.1065274827602, 0.0]
[0.0, 1462.8458143901078, 309.44006786922523, 0.0]
[0.0, 0.0, 1.0, 0.0]
]
P1=[
[899.6981940814015, 77.22014982723218, 754.5555286533175, -141229.9577276383]
[-33.49299203520616, 1011.8717473685108, 231.48341525704353, 138667.38892397884]
[-0.11232419312291886, 0.10748594979245606, 0.9878411037391078, 123.1362397898827]
]
P2 = np.array(P1)
P1 = np.array(P0)

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

def DLTB(point1, point2):
    global P1, P2

    A = [point1[1] * P1[2, :] - P1[0, :],
         P1[2, :] - point1[0] * P1[1, :],
         point2[1] * P2[2, :] - P2[0, :],
         P2[2, :] - point2[0] * P2[1, :]
         ]
    A = np.array(A).reshape((4,4))

    B = A.transpose() @ A
    U, s, Vh = np.linalg.svd(B, full_matrices = False)

    return Vh[3,0:3]/Vh[3,3]


def IterativeLinearLSTriangulation(u, u1):
    #https://www.morethantechnical.com/blog/2012/01/04/simple-triangulation-with-opencv-from-harley-zisserman-w-code/
    #page 129
    global P0, P1, EPSILON
    wi = 1
    wi1 = 1
    X = np.zeros([4,1])
    for i in range(10):
        X_ = DLT(u,u1)
        X[0] = X_[0]
        X[1] = X_[1]
        X[2] = X_[2]
        X[3] = 1.0

        p2x = float((P0[2]@X)[0])
        p2x1 = float((P1[2]@X)[0])
        if abs(wi - p2x) <= EPSILON and abs(wi1 - p2x1) <= EPSILON:
            return X[0], X[1], X[2]
        wi = p2x;
        wi1 = p2x1;

        A = [
            (u[1] * P1[2, :] - P1[1, :])/wi,
            (P1[0, :] - u[0] * P1[2, :])/wi,
            (u1[1] * P2[2, :] - P2[1, :])/wi1,
            (P2[0, :] - u1[0] * P2[2, :]/wi1)
             ]
        A = np.array(A).reshape((4, 4))

        B = A.transpose() @ A
        U, s, Vh = np.linalg.svd(B, full_matrices=False)

        X_ = Vh[3, 0:3] / Vh[3, 3]
        X[0] = X_[0]
        X[1] = X_[1]
        X[2] = X_[2]
        X[3] = 1.0
    return X[0], X[1], X[2]




def bSub(OG, frame):
    return OG.apply(frame)>0

def HSV(frame):
    return cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), (HSVL[0],HSVL[1],HSVL[2]), (HSVH[0],HSVH[1],HSVH[2])) > 0

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
video0 = cv2.VideoCapture(1, cv2.CAP_DSHOW)
video1 = cv2.VideoCapture(2, cv2.CAP_DSHOW)

def nothing(x):
   pass

cv2.namedWindow('OG')

cv2.createTrackbar('H','OG',181,255,nothing)
cv2.createTrackbar('HL','OG',166,255,nothing)

cv2.createTrackbar('S','OG',255,255,nothing)
cv2.createTrackbar('SL','OG',113,255,nothing)

cv2.createTrackbar('V','OG',255,255,nothing)
cv2.createTrackbar('VL','OG',113,255,nothing)

# We need to check if camera
# is opened previously or not
if (video0.isOpened() == False):
    print("Error reading video file")

i=0

start=0
backSub0 = cv2.createBackgroundSubtractorKNN()
backSub1 = cv2.createBackgroundSubtractorKNN()
while (True):

    ret0, frame0 = video0.read()
    ret1, frame1 = video1.read()
    #frame0 = cv2.resize(frame0, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
    #frame1 = cv2.resize(frame1, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
    if ret0 == ret1 == True:
        out0 = np.zeros_like(frame0, np.uint8)
        out1 = np.zeros_like(frame1, np.uint8)

        mask0 = bSub(backSub0, frame0)
        mask1 = bSub(backSub1, frame1)

        out0[mask0] = frame0[mask0]
        out1[mask1] = frame1[mask1]

        H = cv2.getTrackbarPos('H', 'OG')
        S = cv2.getTrackbarPos('S', 'OG')
        V = cv2.getTrackbarPos('V', 'OG')
        HL = cv2.getTrackbarPos('HL', 'OG')
        SL = cv2.getTrackbarPos('SL', 'OG')
        VL = cv2.getTrackbarPos('VL', 'OG')
        HSVH = [H,S,V]
        HSVL = [HL,SL,VL]
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
                cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)

                x, y, z = DLTB(pos0, pos1)
                text = str('X:' + str(np.round(x, 2)) + ' Y:' + str(np.round(y, 2)) + ' Z:' + str(np.round(z, 2)))
                cv2.putText(frame1, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_4)


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


