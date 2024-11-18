import cv2
import numpy as np
from sklearn.cluster import DBSCAN as DBSCANLib


arr=[]


SHOWALL = False

#shade_gray
HSVL0 = ( 144 , 88 , 161 )
HSVH0 = ( 182 , 255 , 255 )
HSVL1 = ( 130 , 119 , 60 )
HSVH1 = ( 178 , 255 , 255 )

#shade_bowl
#HSVL0 = ( 168 , 74 , 69 )
#HSVH0 = ( 172 , 255 , 255 )
#shade_joshie
#HSVL0 = ( 159 , 52 , 83 )
#HSVH0 = ( 172 , 255 , 255 )
#shae
#HSVL0 = (158, 114, 97)
#HSVH0 = (178, 255, 255)
#HSVL1 = (158, 114, 97)
#HSVH1 = (178, 255, 255)

#CAITY
#HSVL0 = (166,  65, 130)
#HSVH0 = (178,  255, 255)
#HSVL1 = (166 ,  97 ,  113)
#HSVH1 = (178 ,  255 ,  255)

#joshie
#HSVL0 = ( 167 ,  41 ,  95 )
#HSVH0 = ( 178 ,  255 ,  255 )
#HSVL1 = ( 143 , 122 , 137 )
#HSVH1 = ( 182 , 255 , 255 )

#garden
#HSVL0 = ( 159 , 139 , 189 )
#HSVH0 = ( 177 , 255 , 255 )
#HSVL1 = ( 159 , 139 , 189 )
#HSVH1 = ( 177 , 255 , 255 )
#garden 7
#HSVL0 = ( 166 , 161 , 90 )
#HSVH0 = ( 174 , 255 , 255 )
#HSVL1 = ( 166 , 161 , 90 )
#HSVH1 = ( 174 , 255 , 255 )

#joshie_move
#233
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
#shade_gray
#shade_bowl




#shade_drop



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

#garden3_
#182
#700
#800

#miss
#shade_throw
#shade_joshie

#hit
#shade_josh
#shade_gray
#shade_bowl

#shade_toss
FILENAME = 'shade_gray'
start=30

time = np.loadtxt(FILENAME+'.txt')

DBMIN = 16
DBEPS = 2.9

FRAMEHEIGHT = 720
FRAMEWIDTH = 1280
HSVL =[]
HSVH = []



toSave0 = []
toSave1 = []
saveTimes=[]
xSave = 0
ySave = 0
zSave = 0


P1=[
[122.2309785166587, 104.41700795491317, 1164.3600362992165, 148858.23882848167],
[924.9205757143097, 492.9997856339263, 51.54441356285844, 22849.293700673672],
[-0.10937235796802147, 0.8702341401456921, 0.4803438649940301, 145.9816183262997]]

P2=[
[63.31786705601067, -1076.3510151423866, 316.4433576495695, 566390.6775373373],
[960.5040553626036, -67.53792537948023, 263.8352164741761, 112412.59728433345],
[0.07988903223900826, -0.2949120604722212, 0.9521788797888472, 419.9324614797241]]

#caity_
#P1=[
#[-63.45149058596992, -1246.1656110494248, 458.7562084622791, 334072.3221846878],
#[1138.8399126029187, -36.74856797794209, 415.6999105566118, 74448.93454281519],
#[-0.12640046692726092, -0.11952853073182265, 0.9847516703726185, 276.12215956855425]]
#P2=[
#[6.254864329765105, -812.8068203889829, 864.3649871232805, 271505.9654784311],
#[1003.8390011898896, 36.723303738739936, 273.8109118310181, 187927.21183245143],
#[0.04973250480781609, 0.22695047440621588, 0.9726356769789674, 437.6728247259547]]

#shade_
#P1=[
#[-6.581485088076711, 1806.8293357679772, 2100.2890797416917, 72316.99672176925],
#[-2569.6605069905436, -24.396601153283115, 95.49160559699268, 106578.80626977581],
#[-0.1209592120039773, -0.6409430391907733, 0.7579979482454235, 429.0657642419051]]

#P2=[
#[69.68334906215955, 1160.220867826899, 307.51289834949034, 199085.59026251244],
#[-1066.5587519122653, 286.60012279388735, -62.14292061195317, 100971.8037570991],
#[-0.33365856919422576, 0.3124859329855524, 0.8893955817797901, 197.71739156031114]]

#sun
#P1=[
#[369.2270299270152, 2692.4794417554344, -319.9607376188078, 703587.6851511409],
#[-2378.9520781221577, 481.9299102612971, 749.9663806842482, 223191.49008321637],
#[0.18889889679039618, 0.25918386953921313, 0.9471752364594692, 820.2148593659587]]

#P2=[
#[170.87788731459796, 1214.6865195618561, -17.94309540931897, 1007683.725357775],
#[-998.6032910453004, 348.1749467088014, 409.97218515797977, 975974.9341168939],
#[0.12674453649243267, 0.5004455810915696, 0.8564403323263331, 2975.727637313998]]

#P2=[
#[301.71604918441363, 1188.5597034905954, 35.74639498130915, 564720.6482211279],
#[-950.6423837962562, 434.13430070865803, 440.81513821982827, 440943.5278556728],
#[0.1890444647449283, 0.44238369500724606, 0.8766748865691543, 1485.0827236463529]]

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
    return OG.apply(frame)>0

def HSV(frame, HSVH=(175, 255, 255), HSVL = (167,144,177)):
    return cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV), HSVL, HSVH) > 0

def DBSCAN(mask):
    global DBMIN, DBEPS
    x, y = np.where(mask == True)
    print(len(x),'A')
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

repeat = True
backSub0 = cv2.createBackgroundSubtractorKNN()
backSub1 = cv2.createBackgroundSubtractorKNN()
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
        #FRAME0[:,920:1280]=[0,0,0]
        #FRAME1[:,640:1280]=[0,0,0]
        repeat = True
        i += 1
        #print(i)
    elif key & 0xFF == ord('s'):
        #n=FILENAME+'_'+str(start)
        np.save('odroidFrame0', toSave0)
        np.save('odroidFrame1', toSave1)
        np.save(('odroidTime'), saveTimes)
        print('saved - ')#,n)
    elif key & 0xFF == ord('p'):
        #temp=[xSave, ySave, zSave, time[i]]
        #print(temp)
        toSave0.append(frame0)
        toSave1.append(frame1)
        saveTimes.append(time)
        print('p')
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
            print(np.shape(mask0))
            out0[mask0] = frame0[mask0]
            out1[mask1] = frame1[mask1]


            mask0 = HSV(out0, HSVH0, HSVL0)
            mask1 = HSV(out1,HSVH1,HSVL1)
            print(np.shape(mask0))

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

                    #caity
                    y=-tx/100+0.36
                    x=ty/100+1.56
                    z=tz/100
                    y*=100
                    x*=100
                    z*=100
                    #shade
                    #x=ty#/54
                    #y=tx#/54+1.31
                    #z=tz#/54
                    #sun
                    #x = ty/5.4
                    #y=tx/5.4
                    #z=tz/-5.4
                    xSave = x
                    ySave = y
                    zSave = z
                    text = str('X:' + str(np.round(x, 3)) + ' Y:' + str(np.round(y, 3)) + ' Z:' + str(np.round(z, 3)))
                    cv2.putText(frame0, text, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 255), 2, cv2.LINE_4)
                    #print('[',x,',',y,',',z,'],')
            cv2.imshow('Frame1', frame1)
            cv2.imshow('Frame0', frame0)

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

#joshie_toss
#68
#joshie_throw
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


"""
garden3
********************************************
1
[278.4105255306527, -78.09639832198036, 210.67212358070753]

[270.1849266562573, -10.252402053955933, 211.6059269264964]

[279.15047840379776, 61.97710276138366, 217.22132069721042]

[273.0085406146, 130.98638778243478, 212.7259531732024]
2
[417.02951415880153, -77.35751841038515, 195.8861172907625]
[415.8176420499867, -76.88761060291124, 194.7044112420442]

[418.31240869150827, -25.003476302869252, 196.56658680372948]
[418.20127675455046, -2.7409828294163887, 199.45303556608184]

[417.8383739470451, 66.39317822347545, 208.07628697523285]
[419.9933713324075, 72.02300034965099, 203.2549921478713]
[421.65719955103214, 71.559585291408, 203.14719599044778]

[425.52937749984494, 139.36924184681337, 203.14868466422104, 95.694]
[424.518450456402, 140.6872023067619, 203.9281756137981, 94.903]
[425.96481772316287, 140.20142175792424, 203.76160264456934, 97.251]

3
[562.5877667084198, -69.29527513892715, 150.21766343408794, 94.702]
[562.6297745153054, -69.51245919946845, 152.07575463502977, 116.08]
[561.4776320360053, -70.04184794189445, 151.7803888703555, 99.97200000000001]
[560.5478874217148, -69.11015384076204, 153.40190559518464, 113.724]
[559.0126647730772, -71.45157577133966, 154.4947643738664, 116.117]
[560.1857333695276, -72.36898977101738, 153.37647739586365, 98.775]

[566.3744931917234, -0.6978027766831707, 152.18614448294818, 89.14099999999999]
[566.7239634850154, 2.7972294932315123, 153.5072808431694, 100.61]

[562.2765992640398, 76.91066614699142, 154.7867309411006, 97.521]
[562.4017638933686, 76.8660226810156, 155.7775476752348, 118.31099999999999]
[561.2280074166101, 72.35084018960056, 155.68468679619636, 98.922]


[560.3488264018551, 148.01027867581382, 157.93947050351096, 98.342]
[554.4973342990106, 146.2349098174115, 167.53860828111843, 101.44200000000001]

4
[675.5726577339935, -59.63405187933412, 94.88763479520661, 92.866]

[671.1947955867746, 6.197341152610791, 98.29145735359118, 97.055]
[672.0512892974701, 7.485735432232615, 99.62309327974496, 91.69]
[668.2152175837881, 9.20781278672586, 98.41276546217357, 96.10000000000001]

[662.9039624148921, 77.27944799935491, 109.73890955618934, 94.461]

[658.6471143454341, 147.33966274045733, 115.84354119448574, 107.027]
[658.1346394983681, 150.1982010165325, 116.82796731650625, 91.879]
[657.0873873308924, 149.78615304314195, 120.5168454221474, 91.81400000000001]
*****************************
garden 4
1
[376.6956404926372, -94.18588348071, 375.1617305334488, 101.68]

[379.7039217062843, -45.97696339175613, 374.4814925153544, 93.47800000000001]
[379.6286282605332, -36.00672877336969, 374.85658340853246, 112.731]
[380.34350940006146, -41.06590312631333, 373.6782859607548, 93.222]

[383.4524080665343, 21.143710931923472, 376.9630544839145, 79.46000000000001]
[384.72647030748556, 26.064857959407433, 376.9873926825071, 118.062]

[388.9415587674222, 84.8352118807081, 378.4692552747044, 105.124]
[389.8975903697754, 85.50632281491362, 379.80813879370857, 109.829]
[389.8961476130589, 84.67244113395851, 378.423708895994, 100.035]
2
[490.11300778639634, 96.42200027004937, 357.1270324592726, 100.979]
[490.11300778639634, 96.42200027004937, 357.1270324592726, 100.979]
[499.54282609677216, 93.34809448800112, 347.5998494974893, 116.661]
[495.2443069813022, 91.47440678175765, 349.8462790011608, 88.06700000000001]
[495.7516592739833, 93.86056994099114, 350.8314817776567, 90.746]

[491.0361330876367, 32.218969059749234, 351.2099531581998, 95.776]
[491.8814629196673, 26.737870162279233, 352.5116195383136, 91.10799999999999]

[488.977336905999, -37.52561082183543, 346.69432308447307, 94.706]

[489.8489951488627, -94.16713572135599, 353.4299869695005, 119.11]
[489.9165767825464, -94.98420848645307, 352.9186081405095, 92.519]
3
[605.6966290325763, -88.1205692153831, 312.6401510833075, 98.236]
[604.4744751259776, -88.762415805159, 313.49850067393413, 90.594]

[606.7484802101907, -32.550184334693434, 313.6681614234624, 93.28500000000001]
[609.1714155640698, -30.07489135155318, 313.7894536889211, 104.172]

[612.2027112023394, 35.514365003876115, 311.0721098376546, 107.774]

[611.564618044556, 95.56650732547163, 314.56587105188396, 97.168]
[611.4675292600335, 101.94286621082804, 314.85338874135266, 96.452]
4
[706.044266707881, 92.66711031261516, 263.75085226947573, 96.613]

[711.3398219915746, 36.1600714280312, 257.4935638967148, 92.872]
[710.64058121479, 34.6292984820206, 258.4713676190186, 120.367]

[711.8282336271369, -31.479442478475804, 254.60903064191336, 113.71199999999999]
[713.3723725891521, -31.449463477234975, 254.67757511677235, 106.5]
[712.3392680384827, -30.640882061474816, 255.97264775680603, 99.653]

[708.1927904655903, -83.94019723289888, 257.777743700249, 90.828]
[708.333770434199, -81.77298948245168, 257.5734434837515, 107.681]
[709.645790865355, -84.49256495510879, 257.0542920172347, 96.949]
**********************************
garden 6
1
[414.3464431176148, -103.46724216192325, 437.4240859982508, 116.91799999999999]
[414.45453205637256, -103.32176224194068, 437.42482530503605, 100.26100000000001]

[408.4354692585759, -50.223956525031085, 437.7962711788486, 111.155]
[405.2134252865152, -45.679226555038035, 438.8523804995627, 111.24499999999999]

[407.31531136741063, 8.39099724045213, 438.84714411409834, 109.513]
[406.05220209373556, 8.996463593794951, 438.16842431365484, 103.72099999999999]

[395.0503153622355, 52.65855187682917, 438.37917391976384, 98.08600000000001]
[390.86660071630274, 67.20754366425203, 439.91732826790076, 100.274]

2
[520.7562776615008, -45.43560771198803, 413.63558629730386, 97.667]
[515.5697427044231, -45.30260126551362, 413.96166694252264, 96.136]
[519.0850138448437, -102.1332869445318, 416.4719278275579, 107.192]

[507.7814298963901, 13.309640844191279, 417.3172721620979, 96.064]

[509.17966068173035, 34.04376247803456, 419.1750237212827, 92.984]

[511.7527440575633, 70.24772117276576, 422.2006797313289, 99.43299999999999]

3

[622.6851972597625, -98.98137851971967, 379.55975020347705, 102.115]

[612.8075873167013, -48.038562350028236, 381.1092118868891, 107.70200000000001]

[608.6085704560762, 11.055669226366636, 383.11292222645625, 94.002]
[611.876126384854, 11.889498812729512, 382.58893722905736, 102.702]

[606.2515374316279, 71.0546679580125, 378.8563130230371, 98.704]
[608.9169835743793, 72.87729124812847, 379.91016597767805, 107.929]

4
none

[711.4293543382233, -12.657057693570145, 334.7748926726142, 94.22500000000001]
[716.6084359990647, -47.36734508435359, 331.9219375980003, 98.476]
[712.3882735385541, -43.401807325146905, 339.21346518503447, 94.883]

[701.8420768891671, 21.67350926616787, 341.26319561747044, 96.337]
[702.2857181468455, 11.379450039466125, 340.4610932222639, 106.392]
[702.84934713105, 14.775254094216313, 340.87096952722754, 104.931]

[699.9161177858405, 68.04630058912467, 339.4924076142749, 102.78099999999999]

garden 7
1
[446.59026989839197, -106.49442592348142, 487.7560146740936, 101.133]
[447.17249627295564, -107.5153519628639, 488.13351301650584, 87.312]
[449.5946103489678, -108.32912443528127, 489.5287096557223, 99.333]

[438.8610837901794, -59.1720799289575, 492.44616437878267, 87.179]

[432.3669990003026, -6.889361579215297, 490.69055682649366, 108.414]
[432.4571042707108, -4.3867077471202, 490.59862516696586, 91.934]
[432.1610367637554, -3.8907899213622645, 491.4741490503582, 98.54599999999999]

[421.96672605575844, 45.11048353346509, 489.5248245200131, 99.481]
[421.52620549226503, 47.93755213974883, 491.07737112282734, 115.523]

2
[543.800526584992, -106.74412626766063, 472.14107913785926, 91.245]
[542.7379530096335, -107.8195291354298, 474.42905892153107, 94.156]
[544.745256656815, -109.02633372403403, 474.06768006381566, 99.33500000000001]
[645.9946329004314, -109.65565209861094, 442.57179745676234, 98.61699999999999]

[537.185344787699, -55.97691671011545, 476.5342709456918, 99.915]

[536.4072393059065, -3.131835729083549, 474.7809004415169, 113.39699999999999]
[535.440108789715, -1.9222145514909472, 476.647764962005, 89.283]
[535.7354123080128, -1.4695716075465048, 475.1340908597168, 96.916]

[539.6566966170501, 52.394222768068, 473.2382852477334, 110.966]
[542.1537974139229, 51.42012463520891, 472.41794692502526, 100.395]
[533.4459769474139, 46.06103943928366, 475.49549852155286, 88.67999999999999]

"""