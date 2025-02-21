import numpy as np
import matplotlib.pyplot as plt
import filter
#(1.886m  0m     8m)
#(2.114m  0.72m  8.04m)$
#hit but not detected

r=7
w=6

n=r+w
rp=0.9
wp=0.1
temp = (w-wp*n)**2/(n*wp)
temp += (w-rp*n)**2/(n*rp)
print(temp)

"""

shade_joshie_15=[[  1.21],
 [0.68],
 [7.29],
 [2.27],
 [0.97],
 [16.86]]
temp=shade_joshie_15
z=float(temp[2][0])
zv=float(temp[2][0])
t=(8-z)/zv
print(float(temp[0][0])+float(temp[3][0])*t)
print(float(temp[1][0])+float(temp[4][0])*t)
print()
#False

shade_joshie_15=[[0.86],
 [0.45],
 [7.33],
 [2.27],
 [-3.65],
 [10.48]]
temp=shade_joshie_15
z=float(temp[2][0])
zv=float(temp[2][0])
t=(8-z)/zv
print(float(temp[0][0])+float(temp[3][0])*t)
print(float(temp[1][0])+float(temp[4][0])*t)
print()
#False
shade_joshie_15=[[0.46],
 [1.18],
 [7.63],
 [6.13],
 [-1.42],
 [15.43]]
temp=shade_joshie_15
z=float(temp[2][0])
zv=float(temp[2][0])
t=(8-z)/zv
print(float(temp[0][0])+float(temp[3][0])*t)
print(float(temp[1][0])+float(temp[4][0])*t)
print()

temp = 1
temp= np.transpose(np.load('shade_gray_' + str(temp) +'.npy'))
time=temp[3]
totalTime=0
timeAx=[]
temp/=100
arr=[]
arr.append(temp[2])
arr.append(temp[1])
arr.append(temp[0])
KF = filter.KF(arr[0][0], arr[1][0], arr[2][0], 0, 0, 10, 3.6e-2, 3.6e-2, 3.6e-2, 1, bounceCoef)
timeAx.append(0)
KFarr = [[arr[0][0],arr[1][0],arr[2][0]]]
length = len(arr[0])

xv=[]
yv=[]
zv=[]
xv.append(float(KF.x[3]))
yv.append(float(KF.x[4]))
zv.append(float(KF.x[5]))

for i in range(1, length, 1):
    delta=time[i]/1000
    totalTime+=delta
    timeAx.append(totalTime)
    KF.predict(delta)
    KF.update(arr[0][i], arr[1][i], arr[2][i])
    KFarr.append([float(KF.x[0]), float(KF.x[1]), float(KF.x[2])])

    xv.append(float(KF.x[3]))
    yv.append(float(KF.x[4]))
    zv.append(float(KF.x[5]))

KFarr = np.transpose(KFarr)


fx=[]
fy=[]
fz=[]

time0=[]
delta=1e-5
fx.append(float(KF.x[0]))
fy.append(float(KF.x[1]))
fz.append(float(KF.x[2]))
time0.append(totalTime)
while float(KF.x[2])<=6.2:
    KF.predict(delta)
    fx.append(float(KF.x[0]))
    fy.append(float(KF.x[1]))
    fz.append(float(KF.x[2]))
    totalTime+=delta
    time0.append(totalTime)

print('#',float(KF.x[0]), float(KF.x[1]),float(KF.x[2]))

plt.figure('2D position')
plt.subplot(1, 3, 1)
plt.xlabel('time (s)')
plt.ylabel('x pos (m)')
plt.plot(timeAx, arr[0])
plt.plot(timeAx, KFarr[0])
plt.plot(time0, fx)
plt.legend(['Measured', 'Predicted', 'Future'])


plt.subplot(1, 3, 2)
plt.plot(timeAx, arr[1])
plt.plot(timeAx, KFarr[1])
plt.xlabel('time (s)')
plt.ylabel('y pos (m)')
plt.plot(time0, fy)
plt.legend(['Measured', 'Predicted', 'Future'])


plt.subplot(1, 3, 3)
plt.plot(timeAx, arr[2])
plt.plot(timeAx, KFarr[2])
plt.xlabel('time (s)')
plt.ylabel('z pos (m)')
plt.plot(time0, fz)
plt.legend(['Measured', 'Predicted', 'Future'])



plt.show()
plt.figure('Velocity')
plt.subplot(1, 3, 1)
plt.xlabel('time (s)')
plt.ylabel('x vel (m/s)')
plt.plot(timeAx, xv)
plt.legend(['Measured', 'Predicted'])

plt.subplot(1, 3, 2)
plt.plot(timeAx, yv)
plt.xlabel('time (s)')
plt.ylabel('y vel (m/s)')
plt.legend(['Measured', 'Predicted'])

plt.subplot(1, 3, 3)
plt.plot(timeAx, zv)
plt.xlabel('time (s)')
plt.ylabel('z vel (m/s)')
plt.legend(['Measured', 'Predicted'])

plt.figure('3D position')
ax = plt.axes(projection='3d')
ax.plot3D(arr[0], arr[1], arr[2], color='red')
ax.scatter3D(KFarr[0], KFarr[1], KFarr[2], color='blue')
ax.legend(['Measured', 'Predicted'])




















bounceCoef = 0.38337013261879244



xB=[2.010,2.92]
yB=[0,0.7]
zB=[6.35,6.4]

def collide(name):
    ped=True
    prevState=[]
    temp = np.transpose(np.load(name + '.npy'))
    time = temp[3]
    temp /= 100
    arr = []
    arr.append(temp[2])
    arr.append(temp[1])
    arr.append(temp[0])
    KF = filter.KF(arr[0][0], arr[1][0], arr[2][0], 0, 0, 10, 3.6e-2, 3.6e-2, 3.6e-2, 1, bounceCoef)
    length = len(arr[0])

    for i in range(1, length, 1):
        delta = time[i] / 1000
        KF.predict(delta)
        KF.update(arr[0][i], arr[1][i], arr[2][i])
        if ped:
            if float(KF.x[2]) >= 6.4:
                print(prevState)
                ped=False
            else:
                prevState=np.round(KF.x,2)
    delta = 1e-5
    if ped:
        print(prevState)
    while float(KF.x[2]) <= 6.4:
        KF.predict(delta)
        if xB[0]<=KF.x[0] <= xB[1]:
            if yB[0] <= KF.x[1] <= yB[1]:
                if zB[0] <= KF.x[2] <= zB[1]:
                    return True
    return False

names=['shade_gray_1','shade_bowl_1','shade_josh_1','shade_throw_1','shade_joshie_15']
for i in names:
    print(i)
    print(collide(i))
    print()

"""
