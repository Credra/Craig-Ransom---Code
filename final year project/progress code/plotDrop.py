import numpy as np
import matplotlib.pyplot as plt
import filter

bounceCoef = 0.38337013261879244

RMSEm = np.zeros((7))
RMSEp = np.zeros((7))
RMSEv = np.zeros((7))
velPercent = np.zeros((7))
TY=[]
TT=[]
x=[]
t=[]

x.append([2.048651271283271, 1.979366498914406, 1.899510165780182, 1.7099324814667716, 1.2513092001518449, 0.686823667184904, 0.04970391899693949])
t.append([114.051, 95.33800000000001, 94.134, 95.16, 116.54100000000001, 121.03899999999999, 68.498])

x.append([2.03777933613620386, 2.0098420836039497, 1.9163240478168316, 1.8913978989009155, 1.5043267324277991, 0.9968660984775971, 0.038209540739996845])
t.append([93.78999999999999, 97.878, 111.735, 91.709, 99.9, 95.263, 113.16])

x.append([2.0123008904524316, 1.9168395365881056, 1.8222481208174939, 1.4928546205694153, 1.0500867367380256, 0.5025683842053272, 0.023336718020679065])
t.append([99.16600000000001, 113.542, 95.816, 93.013, 93.804, 115.22, 94.068])

x.append([2.047088878479889, 1.8362980684320287, 1.5078679228103944, 1.0696201328340267, 0.5115882884404925, 0.029962953563600347, 0.02094056189075062])
t.append([115.414, 93.731, 95.653, 94.40700000000001, 120.693, 88.343, 97.74])

x.append([2.0176405234596766, 1.904001572083672, 1.8097873798410575, 1.5224089319920147, 1.0186755799014997, 0.4902272212012359, 0.009500884175255894])
t.append([93.44014423511656, 101.21149686766579, 92.77797618277056, 97.83874605462434, 95.75734771772566, 92.39682878145722, 88.8954487540974])

x.append([1.996972855834046, 1.8979356229641287, 1.8082119700387675, 1.5028808714232176, 1.0290854701392596, 0.5152207545029399, 0.002433500329856568])
t.append([92.18111580747603, 102.3210789389451, 86.53192397207054, 89.28736173551317, 95.1332775010158, 87.07222032237642, 103.1249440317574])

x.append([2.0133158969823626, 1.9100102298212278, 1.8448223721947283, 1.493845252087026, 1.009392031049527, 0.47437712782094826, 0.07658969447502235])
t.append([98.33316423772177, 108.90899096886005, 86.62050846499505, 103.81254302737372, 96.60170644210356, 96.17856163659475, 91.28671328352343])

x.append([2.0261447438176146, 1.9345774479543802, 1.7703133991837423, 1.5907901849595043, 0.9418253730160494, 0.5018303406920579, 0.007487354001033344])
t.append([98.50627770979519, 98.71565273968129, 106.04140248120137,  88.11537281810507, 96.73449881621063, 103.05406547162397, 92.75073376245436])

x.append([2.076638960717923, 1.9734679384950142, 1.8077473712848457, 1.5189081259801087, 0.9556107126184944, 0.4009601765888037, 0.017395607466307632])
t.append([95.16961642760157, 94.6730964741898, 100.26748331600868, 97.9669318346459, 87.267907482624, 105.38450094815703, 96.70438583702854])

x.append([2.0093809381462386, 1.973817440843663, 1.8721427909270647, 1.476760390955523, 0.9818618349654799, 0.43708682209597444, 0.08520107623073851])
t.append([91.39331959178892, 101.17297912192633, 91.09693651921165, 104.73206341156182, 83.52284374913697, 87.44679900563588, 88.60425921885164])

x = np.round(x,3)
t = np.round(t,3)


for i in range(10):
    time=t[i]
    TY=x[i]

    totalTime=0
    timeAx=[]

    KF = filter.KF(0, TY[0],0, 0, 0, 0, 36e-2, 36e-2, 36e-2, 1, bounceCoef)
    KF.P = np.ones_like(KF.P)*36e-2

    timeAx.append(0)
    KFy =[]
    KFy.append(TY[0])

    yPos = []
    yPos.append(TY[0])
    length = 7

    yv=[]
    yv.append(float(KF.x[4]))

    yvT=[]
    trueYv = 0
    yvT.append(trueYv)
    truePos = 2
    trueY=[]
    trueY.append(truePos)
    G = -9.81

    for i in range(1, length, 1):
        delta=time[i]/1000
        totalTime+=delta
        timeAx.append(totalTime)
        KF.predict(delta)
        KF.update(0, TY[i], 0)
        KFy.append(float(KF.x[1]))
        yv.append(float(KF.x[4]))

        yPos.append(TY[i])

        truePos+= trueYv*delta + 0.5*G*delta**2

        trueYv += G*delta

        if trueYv<0 and truePos<0:
            trueYv*=-bounceCoef
            truePos*=-bounceCoef

        trueY.append(truePos)
        yvT.append(trueYv)

        if trueYv!=0:
            velPercent[i]+=abs((trueYv-KF.x[4])/trueYv)
        else:
            velPercent[i] += KF.x[4]
            print('asasas')

    trueY = np.array(trueY)
    yvT = np.array(yvT)
    yPos = np.array(yPos)
    KFy = np.array(KFy)
    yv = np.array(yv)


    RMSEm += np.square(trueY - TY)
    RMSEp += np.square(trueY - KFy)
    RMSEv += np.square(yvT - yv)


velPercent = np.array(velPercent)/10*100
RMSEm = np.sqrt(RMSEm/10)
RMSEp = np.sqrt(RMSEp/10)
RMSEv = np.sqrt(RMSEv/10)

font = {'family' : 'DejaVu Sans',
    'weight' : 'normal',
    'size'   : 12}

plt.rc('font', **font)

plt.plot(timeAx, RMSEm)
plt.plot(timeAx, RMSEp)
plt.xlabel('time (s)')
plt.ylabel('RMSE (m)')
plt.legend(['Measured', 'Predicted'])
plt.xlim(0,0.6)
plt.show()

temp=[]
for i in range(len(RMSEm)):
    if RMSEp[i]==0:
        if RMSEm[i]==0:
            temp.append(0)
        else:
            temp.append(100)
    else:
        temp.append(abs((RMSEp[i]-RMSEm[i])/RMSEp[i])*100)

plt.plot(timeAx, temp)
plt.xlabel('time (s)')
plt.ylabel('RMSE decrease %')
plt.show()

plt.plot(timeAx, RMSEv)
plt.xlabel('time (s)')
plt.ylabel('RMSE (m/s)')
plt.show()

plt.plot(timeAx, velPercent)
plt.xlabel('time (s)')
plt.ylabel('Percentage Difference (%)')

plt.show()

""""

x=[]
t=[]

x.append([2.048651271283271, 1.979366498914406, 1.899510165780182, 1.7099324814667716, 1.2513092001518449, 0.686823667184904, 0.04970391899693949])
t.append([114.051, 95.33800000000001, 94.134, 95.16, 116.54100000000001, 121.03899999999999, 68.498])

x.append([2.03777933613620386, 2.0098420836039497, 1.9163240478168316, 1.8913978989009155, 1.5043267324277991, 0.9968660984775971, 0.038209540739996845])
t.append([93.78999999999999, 97.878, 111.735, 91.709, 99.9, 95.263, 113.16])

x.append([2.0123008904524316, 1.9168395365881056, 1.8222481208174939, 1.4928546205694153, 1.0500867367380256, 0.5025683842053272, 0.023336718020679065])
t.append([99.16600000000001, 113.542, 95.816, 93.013, 93.804, 115.22, 94.068])

x.append([2.047088878479889, 1.8362980684320287, 1.5078679228103944, 1.0696201328340267, 0.5115882884404925, 0.029962953563600347, 0.02094056189075062])
t.append([115.414, 93.731, 95.653, 94.40700000000001, 120.693, 88.343, 97.74])

x.append([2.0176405234596766, 1.904001572083672, 1.8097873798410575, 1.5224089319920147, 1.0186755799014997, 0.4902272212012359, 0.009500884175255894])
t.append([93.44014423511656, 101.21149686766579, 92.77797618277056, 97.83874605462434, 95.75734771772566, 92.39682878145722, 88.8954487540974])

x.append([1.996972855834046, 1.8979356229641287, 1.8082119700387675, 1.5028808714232176, 1.0290854701392596, 0.5152207545029399, 0.002433500329856568])
t.append([92.18111580747603, 102.3210789389451, 86.53192397207054, 89.28736173551317, 95.1332775010158, 87.07222032237642, 103.1249440317574])

x.append([2.0133158969823626, 1.9100102298212278, 1.8448223721947283, 1.493845252087026, 1.009392031049527, 0.47437712782094826, 0.07658969447502235])
t.append([98.33316423772177, 108.90899096886005, 86.62050846499505, 103.81254302737372, 96.60170644210356, 96.17856163659475, 91.28671328352343])

x.append([2.0261447438176146, 1.9345774479543802, 1.7703133991837423, 1.5907901849595043, 0.9418253730160494, 0.5018303406920579, 0.007487354001033344])
t.append([98.50627770979519, 98.71565273968129, 106.04140248120137,  88.11537281810507, 96.73449881621063, 103.05406547162397, 92.75073376245436])

x.append([2.076638960717923, 1.9734679384950142, 1.8077473712848457, 1.5189081259801087, 0.9556107126184944, 0.4009601765888037, 0.017395607466307632])
t.append([95.16961642760157, 94.6730964741898, 100.26748331600868, 97.9669318346459, 87.267907482624, 105.38450094815703, 96.70438583702854])

x.append([2.0093809381462386, 1.973817440843663, 1.8721427909270647, 1.476760390955523, 0.9818618349654799, 0.43708682209597444, 0.08520107623073851])
t.append([91.39331959178892, 101.17297912192633, 91.09693651921165, 104.73206341156182, 83.52284374913697, 87.44679900563588, 88.60425921885164])

x = np.round(x,3)
t = np.round(t,3)

for i in range(10):
    print(r"\multicolumn{1}{|l|}{", i,r"} & \multicolumn{1}{l|}{", x[i][0],r"} & \multicolumn{1}{l|}{", x[i][1],r"} & \multicolumn{1}{l|}{", x[i][2],r"} & \multicolumn{1}{l|}{", x[i][3],r"} & \multicolumn{1}{l|}{", x[i][4],r"} & \multicolumn{1}{l|}{", x[i][5],r"} & ", x[i][6],r" & \multicolumn{1}{l|}{", t[i][0],r"} & \multicolumn{1}{l|}{", t[i][1],r"} & \multicolumn{1}{l|}{", t[i][2],r"} & \multicolumn{1}{l|}{", t[i][3],r"} & \multicolumn{1}{l|}{", t[i][4],r"} & \multicolumn{1}{l|}{", t[i][5],r"} & ", t[i][6],r" \\ \hline")










start=[254,340,452,534]
RMSEm = np.zeros((7))
RMSEp = np.zeros((7))
RMSEv = np.zeros((7))
velPercent = np.zeros((7))
TY=[]
TT=[]

for timeStamp in start:
    arr= np.load('caity_drop_' + str(timeStamp) +'.npy')
    arr = np.transpose(arr[0:7])
    time=arr[3]
    TY.append(arr[1])
    TT.append(time)
    sampleToPredict = 1
    totalTime=0
    timeAx=[]

    KF = filter.KF(arr[0][0], arr[1][0], arr[2][0], 0, 0, 0, 36e-2, 36e-2, 36e-2, 1e-1, bounceCoef)
    KF.P = np.ones_like(KF.P)*36e-3

    timeAx.append(0)
    KFy =[]
    KFy.append(arr[1][0])

    yPos = []
    yPos.append(arr[1][0])
    length = len(arr[0])

    yv=[]
    yv.append(float(KF.x[4]))

    yvT=[]
    trueYv = 0
    yvT.append(trueYv)
    truePos = 2
    trueY=[]
    trueY.append(truePos)
    G = -9.81

    for i in range(1, length, 1):
        delta=time[i]/1000
        totalTime+=delta
        timeAx.append(totalTime)
        KF.predict(delta)
        KF.update(arr[0][i], arr[1][i], arr[2][i])
        KFy.append(float(KF.x[1]))
        yv.append(float(KF.x[4]))

        yPos.append(arr[1][i])

        truePos+= trueYv*delta + 0.5*G*delta**2

        trueYv += G*delta

        if trueYv<0 and truePos<0:
            trueYv*=-bounceCoef
            truePos*=-bounceCoef

        trueY.append(truePos)
        yvT.append(trueYv)

        if trueYv!=0:
            velPercent[i]+=abs((trueYv-KF.x[4])/trueYv)
        else:
            velPercent[i] += KF.x[4]
            print('asasas')

    trueY = np.array(trueY)
    yvT = np.array(yvT)
    yPos = np.array(yPos)
    KFy = np.array(KFy)
    yv = np.array(yv)


    RMSEm += np.square(trueY - arr[1])
    RMSEp += np.square(trueY - KFy)
    RMSEv += np.square(yvT - yv)


for i in range(4):
    print('[',end='')
    for j in range(6):
        print(TY[i][j],end=', ')
    print(TY[i][6],end=']\n')
    print('[', end='')
    for j in range(6):
        print(TT[i][j], end=', ')
    print(TT[i][6], end=']\n\n')

velPercent = np.array(velPercent)/4*100
RMSEm = np.sqrt(RMSEm/4)
RMSEp = np.sqrt(RMSEp/4)
RMSEv = np.sqrt(RMSEv/4)

font = {'family' : 'DejaVu Sans',
        'weight' : 'normal',
        'size'   : 12}

plt.rc('font', **font)

plt.plot(timeAx, RMSEm)
plt.plot(timeAx, RMSEp)
plt.xlabel('time (s)')
plt.ylabel('RMSE (m)')
plt.legend(['Measured', 'Predicted'])
plt.xlim(0,0.6)
plt.show()

temp=[]
for i in range(len(RMSEm)):
    if RMSEp[i]==0:
        if RMSEm[i]==0:
            temp.append(0)
        else:
            temp.append(100)
    else:
        temp.append(abs((RMSEp[i]-RMSEm[i])/RMSEp[i])*100)

plt.plot(timeAx, temp)
plt.xlabel('time (s)')
plt.ylabel('RMSE decrease %')
plt.xlim(0,0.6)
#plt.ylim(0,2.2)
plt.show()

plt.plot(timeAx, RMSEv)
plt.xlabel('time (s)')
plt.ylabel('RMSE (m/s)')
plt.xlim(0,0.6)
#plt.ylim(0,4.3)
plt.show()

plt.plot(timeAx, velPercent)
plt.xlabel('time (s)')
plt.ylabel('Percentage Difference (%)')
plt.xlim(0,0.6)
#plt.ylim(0,0.41)

plt.show()
"""
