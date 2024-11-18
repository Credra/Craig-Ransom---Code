import cv2
import numpy as np
from sklearn.cluster import DBSCAN as DBSCANLib


arr=[]

FILENAME = 'shade_bowl'
start=57

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


#shade_
#P1=[
#[-6.581485088076711, 1806.8293357679772, 2100.2890797416917, 72316.99672176925],
#[-2569.6605069905436, -24.396601153283115, 95.49160559699268, 106578.80626977581],
#[-0.1209592120039773, -0.6409430391907733, 0.7579979482454235, 429.0657642419051]]

#P2=[
#[69.68334906215955, 1160.220867826899, 307.51289834949034, 199085.59026251244],
#[-1066.5587519122653, 286.60012279388735, -62.14292061195317, 100971.8037570991],
#[-0.33365856919422576, 0.3124859329855524, 0.8893955817797901, 197.71739156031114]]

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


video0 = cv2.VideoCapture(FILENAME+'0.avi')
video1 = cv2.VideoCapture(FILENAME+'1.avi')

if (video0.isOpened() == False):
    print("Error reading video file")

def nothing(temp):
    return
i=1
cv2.namedWindow('OG')
cv2.createTrackbar('x', 'OG', 0, 1280, nothing)
cv2.createTrackbar('y', 'OG', 0, 720, nothing)

repeat = True
ret0, FRAME0 = video0.read()
ret1, FRAME1 = video1.read()

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
    elif key == ord('a'):
        x -= 1
        cv2.setTrackbarPos('x', 'OG', x)
    elif key == ord('d'):
        x += 1
        cv2.setTrackbarPos('x', 'OG', x)
    elif key == ord('w'):
        y -= 1
        cv2.setTrackbarPos('y', 'OG', y)
    elif key == ord('s'):
        y += 1
        cv2.setTrackbarPos('y', 'OG', y)
    else:
        if i>start:repeat = False

    frame0 = cv2.resize(FRAME0, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
    frame1 = cv2.resize(FRAME1, (FRAMEWIDTH, FRAMEHEIGHT), interpolation=cv2.INTER_LINEAR)
    if ret0 == ret1 == True:
        x = cv2.getTrackbarPos('x', 'OG')
        y = cv2.getTrackbarPos('y', 'OG')
        cv2.line(frame0, [0, y], [1280, y], [0, 0, 0], 1)
        cv2.line(frame0, [x, 0], [x, 720], [0, 0, 0], 1)

        cv2.imshow('OG', frame0)
        cv2.imshow('Frame0', frame1)

# When everything done, release
# the video capture and video
# write objects
video0.release()
video1.release()

# Closes all the frames
cv2.destroyAllWindows()
