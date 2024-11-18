import cv2
from datetime import datetime

import numpy as np

#import os
#os.environ["OPENCV_VIDEOIO_MSMF_ENABLE_HW_TRANSFORMS"] = "0"
FILENAME = r"garden7_"
# Create an object to read
# from camera


video0 = cv2.VideoCapture(1, cv2.CAP_DSHOW)
video1 = cv2.VideoCapture(2, cv2.CAP_DSHOW)

# We need to check if camera
# is opened previously or not

video0.set(3, 1280)
video0.set(4, 720)
video0.set(5, 30)

video1.set(3, 1280)
video1.set(4, 720)
video1.set(5, 30)

print("fps:", video1.get(5))
print("1280 =:", video1.get(4))
print("720  =:", video1.get(3))
print("fps:", video0.get(5))
print("1280 =:", video0.get(4))
print("720  =:", video0.get(3))
# We need to set resolutions.
# so, convert them from float to integer.
frame_width = 1280
frame_height = 720

size = (frame_width, frame_height)

# Below VideoWriter object will create
# a frame of above defined The output
# is stored in 'filename.avi' file.
result0 = cv2.VideoWriter(FILENAME+'0.avi',
                         cv2.VideoWriter_fourcc(*'MJPG'),
                         30, size)
result1 = cv2.VideoWriter(FILENAME+'1.avi',
                         cv2.VideoWriter_fourcc(*'MJPG'),
                         30, size)
prevTime = datetime.now()

time = [0]
while (True):
    ret0, frame0 = video0.read()
    ret1, frame1 = video1.read()

    if ret0 == ret1 == True:

        # Write the frame into the
        # file 'filename.avi'
        result0.write(frame0)
        result1.write(frame1)

        # Display the frame
        # saved in the file
        cv2.imshow('Frame0', frame0)
        cv2.imshow('Frame1', frame1)

        # Press S on keyboard
        # to stop the process
        if cv2.waitKey(1) & 0xFF == ord('s'):
            break

    # Break the loop
    else:
        break

    curTime = datetime.now()
    time.append((curTime - prevTime).total_seconds() * 10 ** 3)
    prevTime = curTime


video0.release()
video1.release()
result0.release()
result1.release()

# Closes all the frames
cv2.destroyAllWindows()


np.savetxt(FILENAME+'.txt',time)
print("The video was successfully saved")

""""
P0=[
[968.3771243256969, 0.0, 637.6172705802193, 0.0, ]
[0.0, 969.9507598597629, 355.1913215829512, 0.0, ]
[0.0, 0.0, 1.0, 0.0, ]

P1=[
[1009.3630742015191, -77.1314183586685, 625.9704820381947, 45236.19615649376, ]
[30.387286545886735, 964.4011288196897, 442.5369439420354, -2413.9571814624187, ]
[0.017955540649002292, -0.0845550526531666, 0.9962570158502384, 8.902078500343734, ]
"""