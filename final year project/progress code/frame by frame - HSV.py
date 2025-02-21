import numpy as np
import cv2
from sklearn.cluster import DBSCAN as DBSCANLib

#joshie_toss
#68
#joshie_throw - no
#78
#joshie_hopefully
#40
#joshie_gray
#29
#joshie_gooi
#36
#joshie_bowl
#112
#joshie_4gray
#32
#joshie_3gray
#38
#joshie_2gray
#29
#joshie_drop
#
#joshie_move
#

start= 16

HSVTesting = True
#cap = cv2.VideoCapture(1, cv2.CAP_DSHOW)
cap = cv2.VideoCapture('shade_joshie0.avi')

if (cap.isOpened() == False):
    print("Error opening video file")

ret, frame = cap.read()
intFrame=np.array(frame)

def nothing(x):
   pass

DBMIN = 16
DBEPS = 2.9
def DBSCAN(mask):
    global DBMIN, DBEPS
    x, y = np.where(mask == True)
    if len(x) > DBMIN:
        arr = np.stack((x, y), 1)
        clustering = DBSCANLib(eps=DBEPS, min_samples=DBMIN).fit(arr)
        temp = np.where(clustering.labels_ > -1)
        if len(clustering.labels_[temp])<DBMIN:
            return [False]
        temp = np.argmax(np.bincount(clustering.labels_[temp]))
        mask0 = np.where(clustering.labels_ == temp)
        y, x = np.mean(arr[mask0], 0)
        if not (np.isnan(x) or np.isnan(y)):
            return np.array([x,y])
        else:
            print(temp)
    return [False]


cv2.namedWindow('OG')
#joshie0
#HSVL = ( 167 ,  41 ,  95 )
#HSVH = ( 178 ,  255 ,  255 )
#HSVL = ( 143 , 122 , 137 )
#HSVH = ( 182 , 255 , 255 )


#garden
#HSVL = ( 159 , 139 , 189 )#0
#HSVH = ( 177 , 255 , 255 )

#garden 7 -0
#HSVL = ( 166 , 161 , 90 )
#HSVH = ( 174 , 255 , 255 )
#shade
#HSVL = (158 ,  114 ,  97)
#HSVH = (178 ,  255 ,  255)

#shade_gray
#HSVL1 = (158 ,  114 ,  97)
#HSVH1 = (178 ,  255 ,  255)
#HSVL0 = ( 144 , 88 , 161 )
#HSVH0 = ( 182 , 255 , 255 )

#shade joshie 0
HSVL = ( 159 , 52 , 83 )
HSVH = ( 172 , 255 , 255 )
#caity
#HSVL = (166 ,  97 ,  113)
#HSVH = (178 ,  255 ,  255)
cv2.createTrackbar('H','OG',HSVH[0],255,nothing)
cv2.createTrackbar('HL','OG',HSVL[0],255,nothing)

cv2.createTrackbar('S','OG',HSVH[1],255,nothing)
cv2.createTrackbar('SL','OG',HSVL[1],255,nothing)

cv2.createTrackbar('V','OG',HSVH[2],255,nothing)
cv2.createTrackbar('VL','OG',HSVL[2],255,nothing)

backSub0 = cv2.createBackgroundSubtractorKNN()
i=1
while i < start:
    ret, frame = cap.read()
    i += 1;
ret, frame = cap.read()
print(np.shape(frame))
if __name__ =="__main__":
    while (cap.isOpened()):
        key=cv2.waitKey(150)
        if key & 0xFF == ord('n'):
            ret, frame = cap.read()
            out0 = cv2.resize(frame, (1280, 720), interpolation=cv2.INTER_LINEAR)
        elif key & 0xFF == ord('l'):
            while cv2.waitKey(150) != 27:
                H = cv2.getTrackbarPos('H', 'OG')
                S = cv2.getTrackbarPos('S', 'OG')
                V = cv2.getTrackbarPos('V', 'OG')
                HL = cv2.getTrackbarPos('HL', 'OG')
                SL = cv2.getTrackbarPos('SL', 'OG')
                VL = cv2.getTrackbarPos('VL', 'OG')

                hsv = cv2.cvtColor(out0, cv2.COLOR_BGR2HSV)
                mask = cv2.inRange(hsv, (HL, SL, VL), (H, S, V))>0

                if HSVTesting:
                    out = np.zeros_like(frame, np.uint8)
                    out[mask] = out0[mask]
                else:
                    x, y = np.where(mask == True)
                    if len(x) > DBMIN:
                        arr = np.stack((x, y), 1)
                        clustering = DBSCANLib(eps=DBEPS, min_samples=DBMIN).fit(arr)
                        temp=np.where(clustering.labels_>-1)
                        temp = np.argmax(np.bincount(clustering.labels_[temp]))
                        print()
                        print(clustering.labels_)
                        print(temp)
                        if temp != -1:
                            mask0 = np.where(clustering.labels_ == temp)
                            y, x = np.mean(arr[mask0], 0)
                            if not (np.isnan(x) or np.isnan(y)):
                                x0 = int(pos0[0])
                                y0 = int(pos0[1])
                                cv2.line(frame, [x0, 0], [x0, 720], [0, 0, 0])
                                cv2.line(frame, [0, y0], [1080, y0], [0, 0, 0])

                cv2.imshow('OG', out)

        elif key == 27:
            H = cv2.getTrackbarPos('H', 'OG')
            S = cv2.getTrackbarPos('S', 'OG')
            V = cv2.getTrackbarPos('V', 'OG')
            HL = cv2.getTrackbarPos('HL', 'OG')
            SL = cv2.getTrackbarPos('SL', 'OG')
            VL = cv2.getTrackbarPos('VL', 'OG')
            print('HSVL = (',HL,',',SL,',',VL,')')
            print('HSVH = (',H,',',S,',',V,')')
            print()
        if ret != True:
            break
        out0 = cv2.resize(frame, (1280, 720), interpolation=cv2.INTER_LINEAR)

        H = cv2.getTrackbarPos('H', 'OG')
        S = cv2.getTrackbarPos('S', 'OG')
        V = cv2.getTrackbarPos('V', 'OG')
        HL = cv2.getTrackbarPos('HL', 'OG')
        SL = cv2.getTrackbarPos('SL', 'OG')
        VL = cv2.getTrackbarPos('VL', 'OG')

        hsv = cv2.cvtColor(out0, cv2.COLOR_BGR2HSV)
        mask = cv2.inRange(hsv, (HL, SL, VL), (H, S, V))>0

        pos0 = DBSCAN(mask)
        if pos0[0] != False:
            x0 = int(pos0[0])
            y0 = int(pos0[1])
            cv2.line(out0, [x0, 0], [x0, 720], [0, 0, 0])
            cv2.line(out0, [0, y0], [1280, y0], [0, 0, 0])

        jmask = mask > 0
        out = np.zeros_like(frame, np.uint8)
        out[jmask] = out0[jmask]


        cv2.imshow('OG', out)
        cv2.imshow('Frame', out0)

    # When everything done, release
    # the video capture object
    cap.release()

    # Closes all the frames
    cv2.destroyAllWindows()

    # shade_throw
    # 90
    # shade_gray
    # 34
    # shade_bowl - good
    # 57
    # shade_drop
    # 127
    # 189