import cv2
import numpy as np
from sklearn.cluster import DBSCAN as DBSCANLib


arr=[]

SHOWALL = False

#caity
#HSVL0 = (166,  65, 130)
#HSVH0 = (178,  255, 255)
#HSVL1 = (166 ,  97 ,  113)
#HSVH1 = (178 ,  255 ,  255)

#shade
HSVL0 = (158, 114, 97)
HSVH0 = (178, 255, 255)
HSVL1 = (158, 114, 97)
HSVH1 = (178, 255, 255)



#caity_move
#100
#125
#180
#210
#235
#255

#335
#390
#425
#460
#508
#534
#550
#578
#652

#caity_drop
#254
#340
#452
#534
#647
#700
#789
#830
#871

#caity_bowl
#75
#304
#460
#545?


#caity_throw
#134
#244
#367
#499
#660

#shade_throw
#90
#shade_gray
#34
#shade_bowl - good
#57

FILENAME = 'shade_bowl'
start= 6
"""""
total, yes, miss, out, false, wrong
Frame 0
=[
[ 15,9,0,6,0,0],
[ 13,7,1,5,0,0],
[ 10,3,1,6,0,0]
]
Frame 1
=[
[ 15,9,0,6,0,0],
[ 13,6,2,5,0,0],
[ 10,4,0,6,0,0],
]
"""
start-=5
time = np.loadtxt(FILENAME+'.txt')

DBMIN = 16
DBEPS = 2.9

FRAMEHEIGHT = 720
FRAMEWIDTH = 1280
HSVL =[]
HSVH = []



toSave = []
xSave = 0
ySave = 0
zSave = 0

#P1=[
#[914.7180712457515, 0.0, 627.0206888285073, 0.0],
#[0.0, 918.612201962699, 344.3188099961452, 0.0],
#[0.0, 0.0, 1.0, 0.0]]

#P2=[
#[959.0799732708952, 40.543082785875086, -550.5616312934567, 195809.49889293086],
#[308.6121465410575, 889.3122828831362, 225.28504163350732, 22649.917411898132],
#[0.9045899541137842, -0.05132484367110563, 0.42318172850273006, 90.64961938685026]]

P1=[
[-63.45149058596992, -1246.1656110494248, 458.7562084622791, 334072.3221846878],
[1138.8399126029187, -36.74856797794209, 415.6999105566118, 74448.93454281519],
[-0.12640046692726092, -0.11952853073182265, 0.9847516703726185, 276.12215956855425]]

P2=[
[6.254864329765105, -812.8068203889829, 864.3649871232805, 271505.9654784311],
[1003.8390011898896, 36.723303738739936, 273.8109118310181, 187927.21183245143],
[0.04973250480781609, 0.22695047440621588, 0.9726356769789674, 437.6728247259547]]
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

    return Vh[3,0:3]/Vh[3,3] #cm to m


def bSub(OG, frame):
    return np.where((OG-(cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)))>10)
    #return OG.apply(frame)>0

def backsub(greyReference, greyFrame, threshold):
    return np.where(np.square(greyReference - greyFrame)>threshold)

def HSV(frame, HSVH=(175, 255, 255), HSVL = (167,144,177)):
    return cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), HSVL, HSVH) > 0

def DBSCAN(mask):
    global DBMIN, DBEPS
    x, y = np.where(mask == True)
    if len(x) > DBMIN:
        arr = np.stack((x, y), 1)
        clustering = DBSCANLib(eps=DBEPS, min_samples=DBMIN).fit(arr)
        temp = np.where(clustering.labels_ > -1)
        if len(clustering.labels_[temp]) < DBMIN:
            return [False]
        temp = np.argmax(np.bincount(clustering.labels_[temp]))
        mask0 = np.where(clustering.labels_ == temp)
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

i=1

TOTAL = 0
YES = 0
MISS = 0
OUT = 0
FALSE = 0
WRONG = 0

repeat = True
#backSub0 = cv2.createBackgroundSubtractorKNN()
#backSub1 = cv2.createBackgroundSubtractorKNN()
ret0, FRAME0 = video0.read()
ret1, FRAME1 = video1.read()

backSub0 = cv2.cvtColor(FRAME0, cv2.COLOR_BGR2GRAY)
backSub1 = cv2.cvtColor(FRAME1, cv2.COLOR_BGR2GRAY)
while i < start:
    ret0, FRAME0 = video0.read()
    ret1, FRAME1 = video1.read()
    i += 1;

while (True):
    # to stop the process
    key = cv2.waitKey(100)
    if key & 0xFF == ord('n'):
        ret0, FRAME0 = video0.read()
        ret1, FRAME1 = video1.read()
        repeat = True
        i += 1
        #print(i)
    #in frame, detected
    elif key & 0xFF == ord('y'):
        TOTAL += 1
        YES += 1
        ret0, FRAME0 = video0.read()
        ret1, FRAME1 = video1.read()
        repeat = True
        i += 1
    #in frame, not detected
    elif key & 0xFF == ord('m'):
        TOTAL += 1
        MISS += 1
        ret0, FRAME0 = video0.read()
        ret1, FRAME1 = video1.read()
        repeat = True
        i += 1
    #false detected
    elif key & 0xFF == ord('f'):
        TOTAL += 1
        FALSE += 1
        ret0, FRAME0 = video0.read()
        ret1, FRAME1 = video1.read()
        repeat = True
        i += 1
    #out of frame, not detected
    elif key & 0xFF == ord('o'):
        TOTAL += 1
        OUT += 1
        ret0, FRAME0 = video0.read()
        ret1, FRAME1 = video1.read()
        repeat = True
        i += 1
    elif key & 0xFF == ord('w'):
        TOTAL += 1
        WRONG += 1
        ret0, FRAME0 = video0.read()
        ret1, FRAME1 = video1.read()
        repeat = True
        i += 1
    elif key & 0xFF == 27:
        print('[', TOTAL,end=',')
        print(YES,end=',')
        print(MISS,end=',')
        print(OUT,end=',')
        print(FALSE,end=',')
        print(WRONG,end='],\n')
    else:
        if i>start:repeat = False
    if repeat:
        repeat = False
        frame0 = cv2.resize(FRAME0, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
        frame1 = cv2.resize(FRAME1, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
        if ret0 == ret1 == True:

            out0 = np.zeros_like(frame0, np.uint8)
            out1 = np.zeros_like(frame1, np.uint8)

            mask0 = bSub(backSub0, frame0)
            mask1 = bSub(backSub1, frame1)

            out0[mask0] = frame0[mask0]
            out1[mask1] = frame1[mask1]


            mask0 = HSV(out0, HSVH0, HSVL0)
            mask1 = HSV(out1,HSVH1,HSVL1)
            if SHOWALL:
                Hout0 = np.zeros_like(frame0, np.uint8)
                Hout1 = np.zeros_like(frame0, np.uint8)

                Hout0[mask0] = out0[mask0]
                Hout1[mask1] = out1[mask1]

            pos0 = DBSCAN(mask0)
            pos1 = DBSCAN(mask1)
            if pos0[0] != False:
                x0 = int(pos0[0])
                y0 = int(pos0[1])
                cv2.line(frame0, [x0, 0], [x0, FRAMEHEIGHT], [0, 0, 0])
                cv2.line(frame0, [0, y0], [FRAMEWIDTH, y0], [0, 0, 0])
            if pos1[0] != False:
                x1 = int(pos1[0])
                y1 = int(pos1[1])
                cv2.line(frame1, [x1, 0], [x1, FRAMEHEIGHT], [0, 0, 0])
                cv2.line(frame1, [0, y1], [FRAMEWIDTH, y1], [0, 0, 0])

                if pos0[0] != False:
                    tx, ty, tz = DLT(pos0,pos1)

                    x=ty/100
                    y=-tx/100+0.36
                    z=tz/100
                    xSave = x
                    ySave = y
                    zSave = z
                    text = str('X:' + str(np.round(x, 2)) + ' Y:' + str(np.round(y, 2)) + ' Z:' + str(np.round(z, 2)))
                    cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 255), 2, cv2.LINE_4)
                    #print('[',x,',',y,',',z,'],')
            cv2.imshow('Frame1', frame1)
            cv2.imshow('Frame0', frame0)

            greyT = np.array([[0.114],[0.587],[0.2989]])
            grey = (frame1 @ greyT)[:,:,0]
            grey = np.round(grey).astype(np.uint8)
            gray_image = cv2.cvtColor(frame1, cv2.COLOR_BGR2GRAY)

            cv2.imshow('Grayscale', grey)
            cv2.imshow('Grey0', gray_image)

            if SHOWALL:
                cv2.imshow('B0', out0)
                cv2.imshow('B1', out1)

                cv2.imshow('H0', Hout0)
                cv2.imshow('H1', Hout1)
        else:
            break

# When everything done, release
# the video capture and video
# write objects
video0.release()
video1.release()

# Closes all the frames
cv2.destroyAllWindows()
