import numpy as np
import matplotlib.pyplot as plt
import filter


bounceCoef = 0.38337013261879244

#caity_bowl_
#304
#460


#caity_drop_
#254
#340
#452
#534
#647
#700
#789
#830
#871

#caity_throw_
#134
#246
#367
#501
#660


temp = 246
arr= np.transpose(np.load('caity_throw_' + str(temp) +'.npy'))
time=arr[3]
sampleToPredict = 1
totalTime=0
timeAx=[]
timeAx0=[]
timeAx1=[]

KF = filter.KF(arr[0][0], arr[1][0], arr[2][0], 0, 0, 10, 3.6e-3, 3.6e-3, 3.6e-3, 1, bounceCoef)
timeAx.append(0)
timeAx1.append(0)
KFarr = [[arr[0][0],arr[1][0],arr[2][0]]]
length = len(arr[0])

for i in range(1, length-sampleToPredict, 1):
    delta=time[i]/1000
    totalTime+=delta
    timeAx.append(totalTime)
    timeAx1.append(totalTime)
    KF.predict(delta)
    KF.update(arr[0][i], arr[1][i], arr[2][i])
    KFarr.append([float(KF.x[0]), float(KF.x[1]), float(KF.x[2])])

temp = totalTime
for i in range(sampleToPredict):
    temp += time[length-sampleToPredict+i] / 1000
    timeAx1.append(temp)

delta = (temp - totalTime)/10
pX=[]
pY=[]
pZ=[]
temp=0
for i in range(10):
    totalTime+=delta
    timeAx0.append(totalTime)
    KF.predict(delta)
    pX.append(float(KF.x[0]))
    pY.append(float(KF.x[1]))
    pZ.append(float(KF.x[2]))

KFarr = np.transpose(KFarr)
plt.figure('3D position')
ax = plt.axes(projection='3d')
ax.plot3D(arr[0], arr[1], arr[2], color='red')
ax.scatter3D(KFarr[0], KFarr[1], KFarr[2], color='blue')
ax.scatter3D(pX, pY, pZ, color='orange')
ax.legend(['Measured', 'Predicted'])


plt.figure('2D position')
plt.subplot(1, 3, 1)
plt.xlabel('time (s)')
plt.ylabel('x pos (m)')
plt.plot(timeAx1, arr[0])
plt.plot(timeAx, KFarr[0])
plt.plot(timeAx0, pX)
plt.legend(['Measured', 'Predicted','Future'])

plt.subplot(1, 3, 2)
plt.plot(timeAx1, arr[1])
plt.plot(timeAx, KFarr[1])
plt.plot(timeAx0, pY)
plt.xlabel('time (s)')
plt.ylabel('y pos (m)')
plt.legend(['Measured', 'Predicted','Future'])


plt.subplot(1, 3, 3)
plt.plot(timeAx1, arr[2])
plt.plot(timeAx, KFarr[2])
plt.xlabel('time (s)')
plt.ylabel('z pos (m)')
plt.plot(timeAx0, pZ)
plt.legend(['Measured', 'Predicted','Future'])


plt.show()

