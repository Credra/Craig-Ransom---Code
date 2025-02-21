import numpy as np
import time as TIME
import cv2

DBMIN = 16
DBEPS = 2.9
greyT = np.array([[0.114], [0.587], [0.2989]])

HSVL0 = np.array([ 144 , 88 , 161 ])
HSVH0 = np.array([ 182 , 255 , 255 ])
HSVL1 = np.array([ 130 , 119 , 60 ])
HSVH1 = np.array([ 178 , 255 , 255 ])
def bgr2grey(frame):
    global greyT
    #greyscale conversion and making array 2D
    grey = (frame @ greyT)[:, :, 0]
    #rounds off the values and then converts to 8 bit int
    grey = np.round(grey).astype(np.uint8)
    return grey

def bSub(OG, frame):#change
    return (np.abs(OG-bgr2grey(frame))>20)


def HSV(frame, HSVH, HSVL):
    return cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), HSVL, HSVH) > 0
    mask=[]
    for i in frame:
        temp=[]
        for j in i:
            r=j[2]
            g=j[1]
            b=j[0]
            r, g, b = r / 255.0, g / 255.0, b / 255.0
            mx = max(r, g, b)
            mn = min(r, g, b)
            df = mx - mn
            v = mx * 100
            if v < HSVL[2] or v > HSVH[2]:
                temp.append(False)
            else:
                if mx == 0:
                    s = 0
                else:
                    s = (df / mx) * 100

                if s < HSVL[1] or s > HSVH[1]:
                    temp.append(False)
                else:
                    if mx == mn:
                        h = 0
                    elif mx == r:
                        h = (60 * ((g - b) / df) + 360) % 360
                    elif mx == g:
                        h = (60 * ((b - r) / df) + 120) % 360
                    elif mx == b:
                        h = (60 * ((r - g) / df) + 240) % 360
                    if h<HSVL[0] or h>HSVH[0]:
                        temp.append(False)
                    else:
                        temp.append(True)
        mask.append(temp)
    return np.array(mask)




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

def DLT(c1, c2):
    global P1, P2
    A =np.array([c1[0] * P1[2, 0] - P1[0, 0], c1[0] * P1[2, 1] - P1[0, 1], c1[0] * P1[2, 2] - P1[0, 2],
      c1[1] * P1[2, 0] - P1[1, 0], c1[1] * P1[2, 1] - P1[1, 1], c1[1] * P1[2, 2] - P1[1, 2],
      c2[0] * P2[2, 0] - P2[0, 0], c2[0] * P2[2, 1] - P2[0, 1], c2[0] * P2[2, 2] - P2[0, 2],
      c2[1] * P2[2, 0] - P2[1, 0], c2[1] * P2[2, 1] - P2[1, 1], c2[1] * P2[2, 2] - P2[1, 2]
      ])
    B = np.array([[c1[0] * P1[2, 3] - P1[0, 3]],
         [c1[1] * P1[2, 3] - P1[1, 3]],
         [c2[0] * P2[2, 3] - P2[0, 3]],
         [c2[1] * P2[2, 3] - P2[1, 3]]])*-1.0
    solveSVD(A,B)
    return X

def solveSVD(A, B):
    U, s, Vh = np.linalg.svd(A)
    # exlude numerical zeros
    m = (abs(s) / np.max(abs(s))) > 1e-8
    U, s, Vh = U[:, m], s[m], Vh[m, :]
    X = Vh.conj().T @ np.diag(1 / s) @ U.T @ B
    return X

def IterativeLinearLSTriangulation(u, u1):
    global P0, P1, EPSILON
    wi = 1
    wi1 = 1
    X = np.zeros([4,1])
    X_ = DLT(u, u1)
    for i in range(10):
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
        A = [(u[1] * P1[2, :] - P1[1, :])/wi,
            (P1[0, :] - u[0] * P1[2, :])/wi,
            (u1[1] * P2[2, :] - P2[1, :])/wi1,
            (P2[0, :] - u1[0] * P2[2, :]/wi1)]
        A = np.array(A).reshape((4, 4))
        B = A.transpose() @ A
        X_ = solveSVD(A, B)
        X[0] = X_[0]
        X[1] = X_[1]
        X[2] = X_[2]
        X[3] = 1.0
    return X[0], X[1], X[2]


def dbscan(D):
    global DBMIN, DBEPS
    labels = [0] * len(D)
    C = 0
    numLabs=[0]
    for P in range(0, len(D)):
        #checks if P has been processed
        if not (labels[P] == 0):
            continue
        #finds the neighbours of P
        NeighborPts = findNeighbours(D, P, DBEPS)
        #checks if P is a core pixel
        if len(NeighborPts) < DBMIN:
            labels[P] = -1
        else:
            C += 1
            numLabs.append(0)
            #if P is a core point, all its neighbours are in its cluster
            extendCluster(D, labels, P, NeighborPts, C, DBEPS, DBMIN,numLabs)
    return labels, numLabs


def extendCluster(D, labels, P, NeighborPts, C,numLabs):
    global DBMIN
    labels[P] = C
    i = 0
    #for each neighbour, continues
    #when a new neighbour is added
    while i < len(NeighborPts):
        Pn = NeighborPts[i]
        #if it was a noise point
        if labels[Pn] == -1:
            labels[Pn] = C
            numLabs[C]+=1
        #if not yet processed
        elif labels[Pn] == 0:
            labels[Pn] = C
            numLabs[C] += 1
            #checks if D is a core pixel
            PnNeighborPts = findNeighbours(D, Pn)
            if len(PnNeighborPts) >= DBMIN:
                NeighborPts = NeighborPts + PnNeighborPts
        i += 1


def findNeighbours(D, P):
    global EPS
    #finds all the neighbours of P
    neighbors = []
    for Pn in range(0, len(D)):
        if np.linalg.norm(D[P] - D[Pn]) < EPS:
            neighbors.append(Pn)
    return neighbors




def findBall(mask):
    #get the x and y co-ordinates
    x, y = np.where(mask==True)
    #ensure the minimum points are met
    if len(x) > DBMIN:
        arr = np.stack((x, y), 1)
        #label - the idenities the clusters within arr
        #num - the number of pixels in each cluster
        labels, num = dbscan(arr)
        max=np.max(num)
        if max < DBMIN:
            return [False]
        pos=0
        while(num[pos]!=max):pos+=1
        
        mask0 = np.where(labels == pos)
        y, x = np.mean(arr[mask0], 0)
        if not (np.isnan(x) or np.isnan(y)):
            return np.array([x,y])
    return [False]



FRAMES0 = np.load('odroidFrame0.npy')
FRAMES1 = np.load('odroidFrame1.npy')
TIMES = np.load('odroidTime.npy')

refFrame0 = bgr2grey(FRAMES0[0])
refFrame1 = bgr2grey(FRAMES1[0])


for i in range(len(FRAMES0)-1):
    time=TIMES[i]
    frame0 = FRAMES0[i+1]
    frame1 = FRAMES1[i+1]
    out0 = np.zeros_like(frame0, np.uint8)
    out1 = np.zeros_like(frame1, np.uint8)

    mask0 = bSub(refFrame0, frame0)
    mask1 = bSub(refFrame1, frame1)
    out0[mask0] = frame0[mask0]
    out1[mask1] = frame1[mask1]



    hOut0 = np.zeros_like(frame0, np.uint8)
    mask0 = HSV(out0, HSVH0, HSVL0)
    hOut0[mask0] = out0[mask0]

    prev = TIME.time()
    hOut1 = np.zeros_like(frame1, np.uint8)
    mask1 = HSV(out1, HSVH1, HSVL1)
    hOut1[mask1] = out1[mask1]
    cur=TIME.time()
    pos0 = DBSCAN(mask0)
    pos1 = DBSCAN(mask1)
    if pos0[0] != False and pos1[0]!=False:
        x, y, z = DLT(pos0, pos1)
