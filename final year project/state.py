import numpy as np
import time as TIME
import cv2
import threading
import filter
import matplotlib.pyplot as plt


STATE = 'T'

SHOWALL = False


HSVL0 = ( 172 , 114 , 131 )
HSVH0 = ( 179 , 255 , 255 )

HSVL1 = ( 159 , 136 , 126 )
HSVH1 = ( 178 , 255 , 255 )


xBOUNDS=[0,1]
yBOUNDS=[0,0.72]
zBOUNDS=[8,8.02]

P1=[
[44.87191743812341, 1563.2657748024249, 918.1066359728537, 25390.755503555585],
[-1738.8241861218605, 58.37764482418471, 203.31986398154106, 104385.71573567492],
[-0.09265223866288609, -0.2020137696927567, 0.97499025611812, 338.64920954681145]]

P2=[
[25.0970667121036, 1069.8745352542664, 469.2746225470331, 207845.03386099398],
[-990.468477566376, 103.62276596295887, 274.68952732734715, 82084.71718401814],
[-0.04715675874273151, 0.1695268656017781, 0.9843967096369818, 178.54320307057185]]

P1 = np.array(P1)
P2 = np.array(P2)


xNoise = 1
yNoise = 1
zNoise = 1
bounceCoef = 0.3
systemNoise = 1e-1


EPSILON=1e-3

DBMIN = 16
DBEPS = 2.9


FRAMEHEIGHT = 720
FRAMEWIDTH = 1280

greyT = np.array([[0.114], [0.587], [0.2989]])

#for laptop
video0 = cv2.VideoCapture(1, cv2.CAP_DSHOW)
video1 = cv2.VideoCapture(2, cv2.CAP_DSHOW)

#for odroid
#video0 = cv2.VideoCapture(0)
#video1 = cv2.VideoCapture(1)


from sklearn.cluster import DBSCAN
video0.set(3, 1280)
video0.set(4, 720)
video0.set(5, 30)

video1.set(3, 1280)
video1.set(4, 720)
video1.set(5, 30)

COLOURS = []
for i in range(10):
    r = (50 + i * 50) % 255
    g = (150 + i * 80) % 255
    b = (200 + i * 5) % 255
    COLOURS.append([r, g, b])

def bgr2grey(frame):
    global greyT
    #greyscale conversion and making array 2D
    grey = (frame @ greyT)[:, :, 0]
    #rounds off the values and then converts to 8 bit int
    grey = np.round(grey).astype(np.uint8)
    return grey

def bSub(OG, frame,mask):#change
    mask[:] = (np.abs(OG-bgr2grey(frame))>20)[:]
    return

def HSV0(frame, HSVH, HSVL):
#    return cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), HSVL, HSVH) > 0
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


def HSV(frame, mask, HSVH, HSVL):
    mask[:] = (cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), HSVL, HSVH) > 0)[:]
    return
    mask=[]

    #f=frame/255.0
    mx = np.argmax(frame,2)
    mn = np.argmin(frame,2)
    df = frame[mx] - frame[mn]
    mask0 = f[mx] <= HSVL[2]/100
    mask1 = f[mx] >= HSVL[2]/100
    maskV= mask0 & mask1

    mask0 = not f[mx]==0
    s = (df / mx)
    mask1= f[mx] <= HSVL[2] / 100
    mask2 = f[mx] >= HSVL[2] / 100
    maskS = mask0 & mask1 & mask2

    mask0 = not mx==mn
    maskR = mx==2
    h=np.zeros_like(mx)
    h = (60 * ((g - b) / df) + 360) % 360

    maskG = mx==1
    h = (60 * ((b - r) / df) + 120) % 360

    maskB= mx==0
    h = (60 * ((r - g) / df) + 240) % 360

    #if h<HSVL[0] or h>HSVH[0]:

def DLTB(point1, point2):
    global P1, P2

    A = [point1[1] * P1[2, :] - P1[1, :],
         P1[0, :] - point1[0] * P1[2, :],
         point2[1] * P2[2, :] - P2[1, :],
         P2[0, :] - point2[0] * P2[2, :]
         ]
    A = np.array(A).reshape((4, 4))

    B = A.transpose() @ A
    U, s, Vh = np.linalg.svd(B, full_matrices=False)

    X = Vh[3,0:3]/Vh[3,3]

    #scale real world co-ord here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!---***********************
    x=float(X[1])/54
    y=float(X[0])/45+1.4
    z=float(X[2])/54
    return [x,y,z,1]
def DLT(c1, c2):
    global P1, P2

    A =np.array([
        [c1[0] * P1[2][0] - P1[0][0], c1[0] * P1[2][1] - P1[0][1], c1[0] * P1[2][2] - P1[0][2], c1[0] * P1[2][3] - P1[0][3]],
        [c1[1] * P1[2][0] - P1[1][0], c1[1] * P1[2][1] - P1[1][1], c1[1] * P1[2][2] - P1[1][2], c1[1] * P1[2][3] - P1[1][3]],
        [c2[0] * P2[2][0] - P2[0][0], c2[0] * P2[2][1] - P2[0][1], c2[0] * P2[2][2] - P2[0][2], c2[0] * P2[2][3] - P2[0][3]],
        [c2[1] * P2[2][0] - P2[1][0], c2[1] * P2[2][1] - P2[1][1], c2[1] * P2[2][2] - P2[1][2], c2[1] * P2[2][3] - P2[1][3]]
    ])
    B = np.array([[c1[0] * P1[2, 3] - P1[0, 3]],
         [c1[1] * P1[2, 3] - P1[1, 3]],
         [c2[0] * P2[2, 3] - P2[0, 3]],
         [c2[1] * P2[2, 3] - P2[1, 3]]])*-1.0
    return solveSVD(A,B)

def solveSVD(A, B):
    U, s, Vh = np.linalg.svd(A)
    # exlude numerical zeros
    m = (abs(s) / np.max(abs(s))) > 1e-8
    U, s, Vh = U[:, m], s[m], Vh[m, :]
    X = Vh.conj().T @ np.diag(1 / s) @ U.T @ B
    return X

def IterativeLinearLSTriangulation(u, u1):
    global P1, P2, EPSILON
    wi = 1
    wi1 = 1
    X = np.zeros([4,1])
    X_ = DLT(u, u1)
    for i in range(10):
        X[0][0] = X_[0][0]
        X[1][0] = X_[1][0]
        X[2][0] = X_[2][0]
        X[3][0] = 1.0
        p2x = float((P1[2]@X)[0])
        p2x1 = float((P2[2]@X)[0])
        if abs(wi - p2x) <= EPSILON and abs(wi1 - p2x1) <= EPSILON:
            return X[0], X[1], X[2]
        wi = p2x;
        wi1 = p2x1;
        A = [(u[1] * P2[2, :] - P2[1, :])/wi,
            (P2[0, :] - u[0] * P2[2, :])/wi,
            (u1[1] * P2[2, :] - P2[1, :])/wi1,
            (P2[0, :] - u1[0] * P2[2, :]/wi1)]
        A = np.array(A).reshape((4, 4))
        B = A.transpose() @ A
        X_ = solveSVD(A, B)
        X[0][0] = X_[0][0]
        X[1][0] = X_[1][0]
        X[2][0] = X_[2][0]
        X[3][0] = 1.0
    return X[0][0], X[1][0], X[2][0], X[3][0]


def dbscan(D):
    global DBMIN, DBEPS
    labels = [0] * len(D)
    C = 0
    numLabs=[0]
    for P in range(0, len(D)):
        print(P/len(D)*100)
        #checks if P has been processed
        if not (labels[P] == 0):
            continue
        #finds the neighbours of P
        NeighborPts = findNeighbours(D, P)
        #checks if P is a core pixel
        if len(NeighborPts) < DBMIN:
            labels[P] = -1
        else:
            C += 1
            numLabs.append(0)
            #if P is a core point, all its neighbours are in its cluster
            extendCluster(D, labels, P, NeighborPts, C,numLabs)
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
    global DBEPS
    #finds all the neighbours of P
    neighbors = []
    for Pn in range(0, len(D)):
        if np.sqrt(np.sum(np.square(D[P]-D[Pn]))) < DBEPS:
            neighbors.append(Pn)
    return neighbors

def plot3D(xArr, yArr, zArr, tArr, futu):
    global xNoise, yNoise, zNoise, systemNoise, bounceCoef
    KF = filter.KF(xArr[0], yArr[0], zArr[0], 0, 0, 0, xNoise, yNoise, zNoise, systemNoise, bounceCoef)
    
    xE=[]
    xE.append(xArr[0])
    yE=[]
    yE.append(yArr[0])
    zE=[]
    zE.append(zArr[0])
    
    for i in range(1, len(xArr),1):
        KF.predict(tArr[i])
        KF.update(xArr[i], yArr[i], zArr[i])
        xE.append(KF.x[0][0])
        yE.append(KF.x[1][0])
        zE.append(KF.x[2][0])
    plt.figure('3D position')
    ax = plt.axes(projection='3d')
    ax.plot3D(xArr, yArr, zArr)
    ax.plot3D(xE, yE, zE)

    if futu:
        LBW=False
        KFarr = []
        num = 0
        KFarr.append(KF.x)
        while(KF.x[2]<=8 or num<5):
            num+=1
            KF.predict(1e-3)
            KFarr.append(KF.x)
            if xBOUNDS[0] <= KF.x[0][0] <= xBOUNDS[1] and yBOUNDS[0] <= KF.x[1][0] <= yBOUNDS[1] and zBOUNDS[0] <= KF.x[2][0] <= zBOUNDS[1]:
                LBW = True

        print('LBW:',LBW)
        KFarr = np.transpose(KFarr)

        ax.plot3D(KFarr[0][0], KFarr[0][1], KFarr[0][2], color='blue')
        ax.legend(['Measured','Estimated', 'Predicted'])
    else:
        ax.legend(['Measured','Estimated'])

    plt.show()


def plotDrop3D(xArr, yArr, zArr, tArr, true):
    global xNoise, yNoise, zNoise, systemNoise, bounceCoef
    KF = filter.KF(xArr[0], yArr[0], zArr[0], 0, 0, 5, xNoise, yNoise, zNoise, systemNoise, bounceCoef)

    xE = []
    xE.append(xArr[0])
    yE = []
    yE.append(yArr[0])
    zE = []
    zE.append(zArr[0])

    for i in range(1, len(xArr), 1):
        KF.predict(tArr[i])
        KF.update(xArr[i], yArr[i], zArr[i])
        xE.append(KF.x[0][0])
        yE.append(KF.x[1][0])
        zE.append(KF.x[2][0])

    plt.figure('3D position')
    ax = plt.axes(projection='3d')
    ax.plot3D(xArr, yArr, zArr)
    ax.plot3D(xE, yE, zE)
    xt=np.zeros_like(true)+xArr[0]
    zt=np.zeros_like(true)+zArr[0]
    ax.plot3D(xt, true, xt)

    ax.legend(['Measured', 'Estimated', 'True'])

    plt.show()


def plotDrop(yArr, tArr):
    print('plotDrop')
    KF = filter.KF(0, yArr[0], 0, 0, 0, 0, xNoise, yNoise, zNoise, systemNoise, bounceCoef)
    totalTime=0
    curTime=[]
    truePos=2
    trueVel=0
    G=-9.81
    tPos=[]
    tVel=[]
    tPos.append(truePos)
    tVel.append(trueVel)
    curTime.append(totalTime)
    ye=[]
    ye.append(yArr[0])
    yv=[]
    yv.append(0)
    velPercent=[]
    velPercent.append(0)
    for i in range(1,len(tArr),1):
        KF.predict(tArr[i])
        KF.update(0,yArr[i],0)
        ye.append(KF.x[1][0])
        yv.append(KF.x[4][0])
        
        truePos+= trueVel*tArr[i] + 0.5*G*tArr[i]**2
        trueVel+= G*tArr[i]
        tPos.append(truePos)
        tVel.append(trueVel)
        totalTime+=tArr[i]
        curTime.append(totalTime)

        if trueVel!=0:
            velPercent.append(abs((trueVel-KF.x[4][0])/trueVel)*100.0)
        else:
            velPercent.append(KF.x[4][0]*100.0)

    plt.plot(curTime, yArr)
    plt.plot(curTime, ye)
    plt.plot(curTime, tPos)
    plt.xlabel("time (s)")
    plt.ylabel("Y position (m)")
    plt.legend(["Measured", "Estimated", "True"])
    plt.show()

    plt.plot(curTime, yv)
    plt.plot(curTime, tVel)
    plt.xlabel("time (s)")
    plt.ylabel("Y velocity (m/s)")
    plt.legend(["Estimated", "True"])
    plt.show()

    yArr = np.array(yArr)
    ye = np.array(ye)
    tPos = np.array(tPos)

    RMSEm = np.abs(yArr-tPos)
    RMSEe = np.abs(ye-tPos)

    plt.plot(curTime, RMSEm)
    plt.plot(curTime, RMSEe)
    plt.xlabel("time (s)")
    plt.ylabel("|Error| (m)")
    plt.legend(["Measured", "Estimated"])
    plt.show()


    posErr = abs((RMSEm - RMSEe) / RMSEm)*100.0
    plt.plot(curTime, posErr)
    plt.xlabel("time (s)")
    plt.ylabel("Velocity error (%)")
    plt.legend(["Measured", "Estimated"])
    plt.show()

    plt.plot(curTime, velPercent)
    plt.xlabel("time (s)")
    plt.ylabel("Velocity error (%)")
    plt.legend(["Measured", "Estimated"])
    plt.show()


POS0 = [0, 0]
POS1 = [0, 0]
def findBall(mask, hOut1, p0):
    global COLOURS, SHOWALL, POS0, POS1
    #get the x and y co-ordinates
    x, y = np.where(mask)
    #ensure the minimum points are met
    if len(x) > DBMIN:
        arr = np.stack((x, y), 1)
        #label - the idenities the clusters within arr
        #num - the number of pixels in each cluster
        labels = DBSCAN(eps=DBEPS, min_samples=DBMIN).fit(arr).labels_
        if SHOWALL:
            pos=0
            for i in arr:
                label = labels[pos]
                pos+=1
                if label == -1:
                    hOut1[i[0]][i[1]] = [255,255,255]
                else:
                    hOut1[i[0],i[1]] = COLOURS[label]
        
        temp = np.where(labels>-1)
        if len(labels[temp]) < DBMIN:
            if p0:
                POS0[0]=False
            else:
                POS1[0] = False
            return
    
        mask0 = np.where(labels == np.argmax(np.bincount(labels[temp])))
        
        y, x = np.mean(arr[mask0],0)

        if not (np.isnan(x) or np.isnan(y)):
            if p0:
                POS0[0] = x
                POS0[1] = y
            else:
                POS1[0] = x
                POS1[1] = y
            return
    if p0:
        POS0[0] = False
    else:
        POS1[0] = False
    return


def findBallSlow(mask, hOut1, p0):
    global COLOURS, SHOWALL, POS0, POS1
    #get the x and y co-ordinates
    x, y = np.where(mask)
    #ensure the minimum points are met
    if len(x) > DBMIN:
        arr = np.stack((x, y), 1)
        #label - the idenities the clusters within arr
        #num - the number of pixels in each cluster
        labels, num = dbscan(arr)
        if SHOWALL:
            pos=0
            for i in arr:
                label = labels[pos]
                pos+=1
                if label == -1:
                    hOut1[i[0]][i[1]] = [255,255,255]
                else:
                    hOut1[i[0],i[1]] = COLOURS[label]

        max=np.max(num)
        if max < DBMIN:
            if p0:
                POS0[0]=False
            else:
                POS1[0] = False
            return
        pos=0
        while(num[pos]!=max):pos+=1
        labels=np.array(labels)
        mask0 = np.where(labels == pos)
        y, x = np.mean(arr[mask0], 0)
        if not (np.isnan(x) or np.isnan(y)):
            if p0:
                POS0[0] = x
                POS0[1] = y
            else:
                POS1[0] = x
                POS1[1] = y
            return
    if p0:
        POS0[0] = False
    else:
        POS1[0] = False
    return

#STATES:
#Base       - B
#Record     - R
#Show Drop  - D
#Show throw - T
#Live       - L

#record
cv2.namedWindow("Keys")
#esc = 27
#space = 32

while True:

    if STATE == 'R':
        print('record')
        frames0=[]
        frames1=[]
        times=[]
        FILENAME = str(input("Enter Filename (save):"))

        print("recording")
        ret0, frame0 = video0.read()
        ret1, frame1 = video1.read()
        if ret0 == ret1 == True:
            frames0.append(frame0)
            frames1.append(frame1)
        prev = TIME.time()
        while True:
            ret0, frame0 = video0.read()
            ret1, frame1 = video1.read()
            if ret0 == ret1 == True:
                frames0.append(frame0)
                frames1.append(frame1)
                cur = TIME.time()
                times.append((cur-prev))
                prev=cur
            if cv2.waitKey(1) == 32 or cv2.waitKey(1) == 27:
                frames0 = np.array(frames0, dtype=np.uint8)
                frames1 = np.array(frames1, dtype=np.uint8)
                print("Stopped Recording")
                np.save(FILENAME+"Frame0.npy", frames0)
                np.save(FILENAME+"Frame1.npy", frames1)
                np.save(FILENAME+"Time.npy", times)
                print("FPS of recording:")
                print(np.round(1/np.mean(times)))
                print("saved ", FILENAME)
                STATE = 'B'
                break
    elif STATE == 'D':
        print('Drop')
        #KF = filter.KF(0, 0, 0, 0, 0, 0, xNoise, yNoise, zNoise, systemNoise, bounceCoef)
        delta = 0

        filename = str(input("Enter Filename (load)"))
        FRAMES0 = np.load(filename + "Frame0.npy")
        FRAMES1 = np.load(filename + "Frame1.npy")
        TIMES = np.load(filename + "Time.npy")
        
        xArr=[]
        yArr=[]
        zArr=[]
        tArr=[]

        refFrame0 = bgr2grey(FRAMES0[0])
        refFrame1 = bgr2grey(FRAMES1[0])

        LOOP = True
        i = 0
        frame0 = FRAMES0[i + 1]
        frame1 = FRAMES1[i + 1]

        prev = TIME.time()

        mask0 = []
        mask1 = []
        t1 = threading.Thread(target=bSub, args=(refFrame0, frame0, mask0,))
        t2 = threading.Thread(target=bSub, args=(refFrame1, frame1, mask1,))
        t1.start()
        t2.start()
        t1.join()
        t2.join()
        # mask0 = bSub(refFrame0, frame0)
        # mask1 = bSub(refFrame1, frame1)

        if SHOWALL:
            outB0 = np.zeros_like(frame0, np.uint8)
            outB1 = np.zeros_like(frame1, np.uint8)
            outB0[mask0] = frame0[mask0]
            outB1[mask1] = frame1[mask1]

        t1 = threading.Thread(target=HSV, args=(frame0, mask0, HSVH0, HSVL0,))
        t2 = threading.Thread(target=HSV, args=(frame1, mask1, HSVH1, HSVL1,))
        t1.start()
        t2.start()
        t1.join()
        t2.join()
        # mask0 = HSV(frame0, mask0, HSVH0, HSVL0)
        # mask1 = HSV(frame1, mask1, HSVH1, HSVL1)

        if SHOWALL:
            hOut0 = np.zeros_like(frame0, np.uint8)
            hOut0[mask0] = outB0[mask0]

            hOut1 = np.zeros_like(frame1, np.uint8)
            hOut1[mask1] = outB1[mask1]

        db0 = np.zeros_like(frame1)
        db1 = np.zeros_like(frame1)

        t1 = threading.Thread(target=findBall, args=(mask0, db0, True,))
        t2 = threading.Thread(target=findBall, args=(mask1, db1, False,))
        t1.start()
        t2.start()
        t1.join()
        t2.join()

        # pos0, db0 = findBall(mask0)
        # pos1, db1 = findBall(mask1)

        if POS0[0] != False:
            x0 = int(POS0[0])
            y0 = int(POS0[1])
            cv2.line(frame0, (x0, 0), (x0, FRAMEHEIGHT), (0, 0, 0))
            cv2.line(frame0, (0, y0), (FRAMEWIDTH, y0), (0, 0, 0))
        if POS1[0] != False:
            x1 = int(POS1[0])
            y1 = int(POS1[1])
            cv2.line(frame1, (x1, 0), (x1, FRAMEHEIGHT), (0, 0, 0))
            cv2.line(frame1, (0, y1), (FRAMEWIDTH, y1), (0, 0, 0))

        if POS0[0] != False and POS1[0] != False:
            x, y, z, t = DLTB([POS0[0], POS0[1]], [POS1[0], POS1[1]])
            text = str('X:' + str(np.round(x, 3)) + ' Y:' + str(np.round(y, 3)) + ' Z:' + str(np.round(z, 3)))
            cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
            tArr.append(0)
            xArr.append(x)
            yArr.append(y)
            zArr.append(z)

        if SHOWALL:
            cv2.imshow('Bsub1', outB1)
            cv2.imshow('Bsub0', outB0)
            cv2.imshow('HSVFrame1', hOut1)
            cv2.imshow('HSVFrame0', hOut0)
            cv2.imshow('DB1', db1)
            cv2.imshow('DB0', db0)

        cur = TIME.time()
        text = "FPS" + str(np.round((1 / (cur - prev)), 3))
        cv2.putText(frame1, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
        prev = cur
        cv2.imshow('Frame1', frame1)
        cv2.imshow('Frame0', frame0)
        i += 1
        while LOOP:
            key = cv2.waitKey(0)
            if key & 0xFF == ord('s'):
                SHOWALL = not SHOWALL
            elif key & 0xFF == ord('b'):
                LOOP = False
                STATE = 'B'
                cv2.destroyAllWindows()
                cv2.namedWindow("Keys")
            elif key & 0xFF == ord('n'):
                if i < (len(FRAMES0) - 1):
                    frame0 = FRAMES0[i + 1]
                    frame1 = FRAMES1[i + 1]
                    cv2.imshow('Frame1', frame1)
                    cv2.imshow('Frame0', frame0)
                else:
                    LOOP = False
                    STATE = 'B'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                
                key = cv2.waitKey(0)
                if key & 0xFF == ord('p'):
                    #PLOT
                    plotDrop(yArr, tArr)
                    #plotDrop3D(xArr, yArr, zArr, tArr, False)
                    LOOP = False
                    STATE = 'B'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                elif key & 0xFF == ord('s'):
                    print('skip')
                    i+=1
                else:

                    if i < (len(FRAMES0) - 1):
                        prev = TIME.time()
                        delta += TIMES[i - 1] / 750 #change
                        mask0 = []
                        mask1 = []
                        t1 = threading.Thread(target=bSub, args=(refFrame0, frame0, mask0,))
                        t2 = threading.Thread(target=bSub, args=(refFrame1, frame1, mask1,))
                        t1.start()
                        t2.start()
                        t1.join()
                        t2.join()
                        # mask0 = bSub(refFrame0, frame0)
                        # mask1 = bSub(refFrame1, frame1)

                        if SHOWALL:
                            outB0 = np.zeros_like(frame0, np.uint8)
                            outB1 = np.zeros_like(frame1, np.uint8)
                            outB0[mask0] = frame0[mask0]
                            outB1[mask1] = frame1[mask1]

                        t1 = threading.Thread(target=HSV, args=(frame0, mask0, HSVH0, HSVL0,))
                        t2 = threading.Thread(target=HSV, args=(frame1, mask1, HSVH1, HSVL1,))
                        t1.start()
                        t2.start()
                        t1.join()
                        t2.join()
                        # mask0 = HSV(frame0, mask0, HSVH0, HSVL0)
                        # mask1 = HSV(frame1, mask1, HSVH1, HSVL1)

                        if SHOWALL:
                            hOut0 = np.zeros_like(frame0, np.uint8)
                            hOut0[mask0] = outB0[mask0]

                            hOut1 = np.zeros_like(frame1, np.uint8)
                            hOut1[mask1] = outB1[mask1]

                        db0 = np.zeros_like(frame1)
                        db1 = np.zeros_like(frame1)

                        t1 = threading.Thread(target=findBall, args=(mask0, db0, True,))
                        t2 = threading.Thread(target=findBall, args=(mask1, db1, False,))
                        t1.start()
                        t2.start()
                        t1.join()
                        t2.join()
                        # pos0, db0 = findBall(mask0)
                        # pos1, db1 = findBall(mask1)

                        if POS0[0] != False:
                            x0 = int(POS0[0])
                            y0 = int(POS0[1])
                            cv2.line(frame0, (x0, 0), (x0, FRAMEHEIGHT), (0, 0, 0))
                            cv2.line(frame0, (0, y0), (FRAMEWIDTH, y0), (0, 0, 0))
                        if POS1[0] != False:
                            x1 = int(POS1[0])
                            y1 = int(POS1[1])
                            cv2.line(frame1, (x1, 0), (x1, FRAMEHEIGHT), (0, 0, 0))
                            cv2.line(frame1, (0, y1), (FRAMEWIDTH, y1), (0, 0, 0))

                        if POS0[0] != False and POS1[0] != False:
                            x, y, z, t = DLTB([POS0[0], POS0[1]], [POS1[0], POS1[1]])
                            text = str(
                                'X:' + str(np.round(x, 3)) + ' Y:' + str(np.round(y, 3)) + ' Z:' + str(np.round(z, 3)))
                            cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
                            tArr.append(delta)
                            xArr.append(x)
                            yArr.append(y)
                            zArr.append(z)
                            delta = 0

                        if SHOWALL:
                            cv2.imshow('Bsub1', outB1)
                            cv2.imshow('Bsub0', outB0)
                            cv2.imshow('HSVFrame1', hOut1)
                            cv2.imshow('HSVFrame0', hOut0)
                            cv2.imshow('DB1', db1)
                            cv2.imshow('DB0', db0)

                        cur = TIME.time()
                        text = "FPS: " + str(np.round((1 / (cur - prev)), 3))
                        cv2.putText(frame1, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
                        prev = cur
                        cv2.imshow('Frame1', frame1)
                        cv2.imshow('Frame0', frame0)
                    else:
                        LOOP = False
                        STATE = 'B'
                        cv2.destroyAllWindows()
                        cv2.namedWindow("Keys")
                    i += 1

    elif STATE == 'T':
        print('throw')
        delta=0
        xArr=[]
        yArr=[]
        zArr=[]
        tArr=[]
        
        filename = str(input("Enter Filename (load)"))
        FRAMES0 = np.load(filename+"Frame0.npy")
        FRAMES1 = np.load(filename+"Frame1.npy")
        TIMES = np.load(filename+"Time.npy")

        refFrame0 = bgr2grey(FRAMES0[0])
        refFrame1 = bgr2grey(FRAMES1[0])
        cv2.imshow('Frame1', refFrame0)
        cv2.imshow('Frame0', refFrame1)
        prev = TIME.time()
        LOOP = True
        i=0
        frame0 = FRAMES0[i + 1]
        frame1 = FRAMES1[i + 1]

        mask0 = []
        mask1= []
        t1 = threading.Thread(target=bSub, args=(refFrame0, frame0,mask0,))
        t2 = threading.Thread(target=bSub, args=(refFrame1, frame1,mask1,))
        t1.start()
        t2.start()
        t1.join()
        t2.join()
        #mask0 = bSub(refFrame0, frame0)
        #mask1 = bSub(refFrame1, frame1)

        if SHOWALL:
            outB0 = np.zeros_like(frame0, np.uint8)
            outB1 = np.zeros_like(frame1, np.uint8)
            outB0[mask0] = frame0[mask0]
            outB1[mask1] = frame1[mask1]



        t1 = threading.Thread(target=HSV, args=(frame0, mask0, HSVH0, HSVL0,))
        t2 = threading.Thread(target=HSV, args=(frame1, mask1, HSVH1, HSVL1,))
        t1.start()
        t2.start()
        t1.join()
        t2.join()
        #mask0 = HSV(frame0, mask0, HSVH0, HSVL0)
        #mask1 = HSV(frame1, mask1, HSVH1, HSVL1)


        if SHOWALL:
            hOut0 = np.zeros_like(frame0, np.uint8)
            hOut0[mask0] = outB0[mask0]

            hOut1 = np.zeros_like(frame1, np.uint8)
            hOut1[mask1] = outB1[mask1]

        db0 = np.zeros_like(frame1)
        db1 = np.zeros_like(frame1)

        t1 = threading.Thread(target=findBall, args=(mask0, db0, True,))
        t2 = threading.Thread(target=findBall, args=(mask1, db1, False,))
        t1.start()
        t2.start()
        t1.join()
        t2.join()

        #pos0, db0 = findBall(mask0)
        #pos1, db1 = findBall(mask1)

        if POS0[0] != False:
            x0 = int(POS0[0])
            y0 = int(POS0[1])
            cv2.line(frame0, (x0, 0), (x0, FRAMEHEIGHT), (0, 0, 0))
            cv2.line(frame0, (0, y0), (FRAMEWIDTH, y0), (0, 0, 0))
        if POS1[0] != False:
            x1 = int(POS1[0])
            y1 = int(POS1[1])
            cv2.line(frame1, (x1, 0), (x1, FRAMEHEIGHT), (0, 0, 0))
            cv2.line(frame1, (0, y1), (FRAMEWIDTH, y1), (0, 0, 0))

        if POS0[0] != False and POS1[0] != False:
            x, y, z, t = DLTB([POS0[0], POS0[1]], [POS1[0], POS1[1]])
            text = str('X:' + str(np.round(x, 3)) + ' Y:' + str(np.round(y, 3)) + ' Z:' + str(np.round(z, 3)))
            cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
            xArr.append(x)
            yArr.append(y)
            zArr.append(z)
            tArr.append(0)
            

        cur = TIME.time()
        text = "FPS: " + str(np.round((1 / (cur - prev)), 3))
        cv2.putText(frame1, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
        prev=cur
        if SHOWALL:
            cv2.imshow('Bsub1', outB1)
            cv2.imshow('Bsub0', outB0)
            cv2.imshow('HSVFrame1', hOut1)
            cv2.imshow('HSVFrame0', hOut0)
            cv2.imshow('DB1', db1)
            cv2.imshow('DB0', db0)

        cv2.imshow('Frame1', frame1)
        cv2.imshow('Frame0', frame0)
        i+=1
        while LOOP:
            key = cv2.waitKey(0)
            if key & 0xFF == ord('s'):
                SHOWALL = not SHOWALL
            elif key & 0xFF == ord('b'):
                LOOP = False
                STATE = 'B'
                cv2.destroyAllWindows()
                cv2.namedWindow("Keys")
            elif key & 0xFF == ord('n'):
                if i < (len(FRAMES0) - 1):
                    frame0 = FRAMES0[i + 1]
                    frame1 = FRAMES1[i + 1]
                    cv2.imshow('Frame1', frame1)
                    cv2.imshow('Frame0', frame0)
                else:
                    LOOP = False
                    STATE = 'B'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")

                key = cv2.waitKey(0)
                         
                if key & 0xFF == ord('p'):
                    #plot
                    plot3D(xArr, yArr, zArr, tArr, True)
                    LOOP = False
                    STATE = 'B'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                elif key & 0xFF == ord('s'):
                    print('skip')
                    i+=1
                else:
                    if i <(len(FRAMES0)-1):
                        prev = TIME.time()
                        delta += TIMES[i - 1] / 1000
                        mask0 = []
                        mask1 = []
                        t1 = threading.Thread(target=bSub, args=(refFrame0, frame0, mask0,))
                        t2 = threading.Thread(target=bSub, args=(refFrame1, frame1, mask1,))
                        t1.start()
                        t2.start()
                        t1.join()
                        t2.join()
                        # mask0 = bSub(refFrame0, frame0)
                        # mask1 = bSub(refFrame1, frame1)

                        if SHOWALL:
                            outB0 = np.zeros_like(frame0, np.uint8)
                            outB1 = np.zeros_like(frame1, np.uint8)
                            outB0[mask0] = frame0[mask0]
                            outB1[mask1] = frame1[mask1]

                        t1 = threading.Thread(target=HSV, args=(frame0, mask0, HSVH0, HSVL0,))
                        t2 = threading.Thread(target=HSV, args=(frame1, mask1, HSVH1, HSVL1,))
                        t1.start()
                        t2.start()
                        t1.join()
                        t2.join()
                        # mask0 = HSV(frame0, mask0, HSVH0, HSVL0)
                        # mask1 = HSV(frame1, mask1, HSVH1, HSVL1)

                        if SHOWALL:
                            hOut0 = np.zeros_like(frame0, np.uint8)
                            hOut0[mask0] = outB0[mask0]

                            hOut1 = np.zeros_like(frame1, np.uint8)
                            hOut1[mask1] = outB1[mask1]

                        db0 = np.zeros_like(frame1)
                        db1 = np.zeros_like(frame1)

                        t1 = threading.Thread(target=findBall, args=(mask0, db0, True,))
                        t2 = threading.Thread(target=findBall, args=(mask1, db1, False,))
                        t1.start()
                        t2.start()
                        t1.join()
                        t2.join()
                        # pos0, db0 = findBall(mask0)
                        # pos1, db1 = findBall(mask1)

                        if POS0[0] != False:
                            x0 = int(POS0[0])
                            y0 = int(POS0[1])
                            cv2.line(frame0, (x0, 0), (x0, FRAMEHEIGHT), (0, 0, 0))
                            cv2.line(frame0, (0, y0), (FRAMEWIDTH, y0), (0, 0, 0))
                        if POS1[0] != False:
                            x1 = int(POS1[0])
                            y1 = int(POS1[1])
                            cv2.line(frame1, (x1, 0), (x1, FRAMEHEIGHT), (0, 0, 0))
                            cv2.line(frame1, (0, y1), (FRAMEWIDTH, y1), (0, 0, 0))

                        if POS0[0] != False and POS1[0] != False:
                            x, y, z, t = DLTB([POS0[0], POS0[1]], [POS1[0], POS1[1]])
                            text = str(
                                'X:' + str(np.round(x, 3)) + ' Y:' + str(np.round(y, 3)) + ' Z:' + str(np.round(z, 3)))
                            cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
                            xArr.append(x)
                            yArr.append(y)
                            zArr.append(z)
                            tArr.append(delta)
                            delta=0

                        cur = TIME.time()
                        text = "FPS: " + str(np.round((1 / (cur - prev)), 3))
                        cv2.putText(frame1, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
                        prev = cur

                        if SHOWALL:
                            cv2.imshow('Bsub1', outB1)
                            cv2.imshow('Bsub0', outB0)
                            cv2.imshow('HSVFrame1', hOut1)
                            cv2.imshow('HSVFrame0', hOut0)
                            cv2.imshow('DB1', db1)
                            cv2.imshow('DB0', db0)

                        cv2.imshow('Frame1', frame1)
                        cv2.imshow('Frame0', frame0)
                    else:
                        LOOP = False
                        STATE = 'B'
                        cv2.destroyAllWindows()
                        cv2.namedWindow("Keys")
                    i+=1
        #n to go to next frame

    else:#base state
        print("base")
        end=False
        ret0, frame0 = video0.read()
        ret1, frame1 = video1.read()
        refFrame0 = bgr2grey(frame0)
        refFrame1 = bgr2grey(frame1)
        prev = TIME.time()
        while True:
            ret0, frame0 = video0.read()
            ret1, frame1 = video1.read()
            if ret0 and ret1:
                cur = TIME.time()
                text = str(np.round((1/(cur-prev)),3))
                cv2.putText(frame1, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_4)
                prev=cur
                cv2.imshow('Frame1', frame1)
                cv2.imshow('Frame0', frame0)

                key = cv2.waitKey(1)
                if key & 0xFF == ord('d'):
                    STATE = 'D'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                    break
                elif key & 0xFF == ord('s'):
                    SHOWALL = not SHOWALL
                elif key & 0xFF == ord('t'):
                    STATE = 'T'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                    break
                elif key & 0xFF == ord('r'):
                    STATE = 'R'
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                    break
                elif key == 27:
                    end = True
                    cv2.destroyAllWindows()
                    cv2.namedWindow("Keys")
                    break


            else:
                end = True
                cv2.destroyAllWindows()
                break

        if end:
            break

video0.release()
video1.release()