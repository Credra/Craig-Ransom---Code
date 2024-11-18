import numpy as np
#totoal, yes, miss, out, false, wrong
cam0=[
[ 17,10,0,7,0,0],
[ 14,7,1,6,0,0],
[ 21,14,0,6,1,0],
[ 27,7,0,17,2,1],
[ 18,8,1,8,1,0],
[ 21,12,0,8,1,0],
[ 20,12,1,7,0,0],
[ 22,13,1,8,0,0],
[ 15,9,1,5,0,0],
[ 15,9,0,6,0,0],
[ 13,7,0,6,0,0],
[ 10,3,1,6,0,0]
]
cam1=[
[ 17,10,0,7,0,0],
[ 14,9,1,4,0,0],
[ 21,16,0,5,0,0],
[ 27,9,0,16,1,1],
[ 18,10,1,6,1,0],
[ 21,13,0,8,0,0],
[ 20,12,1,7,0,0],
[ 22,14,1,6,1,0],
[ 15,10,1,4,0,0],
[ 15,9,0,6,0,0],
[ 13,7,0,6,0,0],
[ 10,4,1,5,0,0]
]

TOTAL0 = []
YES0 = []
MISS0 = []
OUT0 = []
FALSE0 = []
WRONG0 = []

TOTAL1 = []
YES1 = []
MISS1 = []
OUT1 = []
FALSE1 = []
WRONG1 = []

for i in range(len(cam0)):
    TOTAL0.append(cam0[i][0])
    YES0.append(cam0[i][1])
    MISS0.append(cam0[i][2])
    OUT0.append(cam0[i][3])
    FALSE0.append(cam0[i][4])
    WRONG0.append(cam0[i][5])

    TOTAL1.append(cam1[i][0])
    YES1.append(cam1[i][1])
    MISS1.append(cam1[i][2])
    OUT1.append(cam1[i][3])
    FALSE1.append(cam1[i][4])
    WRONG1.append(cam1  [i][5])

#totoal, yes, miss, out, false
#for i in range(10):
#    TOTAL0.append(extras0[i][0])
#    YES0.append(extras0[i][1])
#    MISS0.append(extras0[i][2])
#    OUT0.append(extras0[i][3])
#    FALSE0.append(extras0[i][4])

#    TOTAL1.append(extras1[i][0])
#    YES1.append(extras1[i][1])
#    MISS1.append(extras1[i][2])
#    OUT1.append(extras1[i][3])
#    FALSE1.append(extras1[i][4])



TOTAL = np.sum(TOTAL0) + np.sum(TOTAL1)
FALSE = np.sum(FALSE0) + np.sum(FALSE1)
MISS = np.sum(MISS0) + np.sum(MISS1)
YES = np.sum(YES0) + np.sum(YES1)
WRONG = np.sum(WRONG0) + np.sum(WRONG1)
OUT = np.sum(OUT0) + np.sum(OUT1)

print('Total')
print(TOTAL)

print('FALSE')
print(FALSE)

print('MISS')
print(MISS)

print('YES')
print(YES)

print('WRONG')
print(WRONG)

print('OUT')
print(OUT   )

print('false positive')
print((WRONG+FALSE)/TOTAL*100)

print('false positive | in frame')
print((WRONG)/(WRONG+YES+MISS)*100)

print('false positive | not in frame')
print((FALSE)/(FALSE+OUT)*100)

print('ball not detected | in frame')
print((MISS+WRONG)/(MISS+YES+WRONG)*100)

print()
print()
temp = (MISS + WRONG - 0.1*(MISS+WRONG+YES))**2/(0.1*(MISS+WRONG+YES))
temp += (YES - 0.9*(MISS+WRONG+YES))**2/(0.9*(MISS+WRONG+YES))
print(temp)

print()
print()
temp = (FALSE + WRONG - 0.1*(TOTAL))**2/(0.1*(TOTAL))
temp += ((TOTAL - FALSE - WRONG) - 0.9*(TOTAL))**2/(0.9*(TOTAL))
print(temp)

if False:
    for i in range(len(YES0)):
        temp=r"\multicolumn{1}{|l|}{"+str(TOTAL0[i])+'} & '
        temp+=r"\multicolumn{1}{l|}{"+str(YES0[i])+'} & '
        temp+=r"\multicolumn{1}{l|}{"+str(MISS0[i])+'} & '
        temp+=r"\multicolumn{1}{l|}{"+str(WRONG0[i])+'} & '
        temp+=r"\multicolumn{1}{l|}{"+str(OUT0[i])+'} & '
        temp+= str(FALSE0[i])+' & '

        temp += r"\multicolumn{1}{l|}{" + str(TOTAL1[i]) + '} & '
        temp += r"\multicolumn{1}{l|}{" + str(YES1[i]) + '} & '
        temp += r"\multicolumn{1}{l|}{" + str(MISS1[i]) + '} & '
        temp += r"\multicolumn{1}{l|}{" + str(WRONG1[i]) + '} & '
        temp += r"\multicolumn{1}{l|}{" + str(OUT1[i]) + '} & '
        temp += str(FALSE1[i]) + r' \\ \hline'

        print(temp)

"""

    
    
    
extras0 = [
    [20, 12,0,7,1,0],
    [19, 11,2,6,0,0],
    [26, 13,0,11,2,0],
    [18, 9,1,8,0,0],
    [23, 14,0,8,1,0],
    [24, 13,1,8,2,0],
    [21, 14,0,7,0,0],
    [25, 15,0,9,1,0],
    [19, 10,0,9,0,0],
    [24, 14,2,8,0,0],
]

extras1 = [
    [20, 14,1,5,0,0],
    [19, 11,0,7,1,0],
    [26, 16,1,6,2,0],
    [18, 9,2,7,0,0],
    [23, 14,1,7,1,0],
    [24, 15,2,6,1,0],
    [21, 14,1,6,0,0],
    [25, 17,0,8,0,0],
    [19, 11,1,7,0,0],
    [24, 18,0,6,0,0],
]
"""
