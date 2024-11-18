import numpy as np
import cv2
import time



prev = time.time()

cap = cv2.VideoCapture('a.jp')

if (cap.isOpened() == False):
    print("Error opening video file")

ret, frame = cap.read()
intFrame=np.array(frame)

def nothing(x):
   pass

cv2.namedWindow('OG')
cv2.createTrackbar('x','OG',0,1280+100,nothing)
cv2.createTrackbar('y','OG',0,720+100,nothing)
ret, frame = cap.read()
if __name__ =="__main__":
    while (cap.isOpened()):
        if cv2.waitKey(1) & 0xFF == ord('n'):
            ret, frame = cap.read()
            if ret != True:
                break
        line = cv2.resize(frame, (1280, 720), interpolation = cv2.INTER_LINEAR)

        x = cv2.getTrackbarPos('x', 'OG')
        y = cv2.getTrackbarPos('y', 'OG')
        cv2.line(line, [0, y], [1280, y], [0, 0, 0], 1)
        cv2.line(line, [x, 0], [x, 720], [0, 0, 0], 1)
        cv2.circle(line, [x,y],3,[250,250,250])

        cv2.imshow('OG', line)

        if cv2.waitKey(1) & 0xFF == ord('p'):
            hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
            print(hsv[x,y])
    # When everything done, release
    # the video capture object
    cap.release()

    # Closes all the frames
    cv2.destroyAllWindows()
