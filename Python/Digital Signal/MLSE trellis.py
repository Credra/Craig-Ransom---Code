import cmath
import matplotlib.pyplot as plt
import numpy as np
import math
import copy
import random

class Node:
    # the delta of the path to get to this node
    delta=-1
    # the column that the node is in
    col=0
    # the row this node is in
    state=0
    # the shortest path to get to that node
    prevState=-1


# declare global variables here, You may use global variables in functions by using the global key work
uniform_seed1 = 1
uniform_seed2 = 2
uniform_seed3 = 3

# uniform on [0,1)
u = 0
u1 = 0
u2 = 0
u3 = 0
# uniform on (-1,1)
v1 = 0
v2 = 0

# returns all the other possible states that this state can lead to
def nextState(state, m):
    t=[]
    if m==2:
        if state==0 or state==1:
            t = [0,2]
        elif state==2 or state==3:
            t = [1,3]
    elif m==4:
        if state<4:
            t = [0,4,8,12]
        elif state<8:
            t = [1,5,9,13]
        elif state < 12:
            t = [2, 6, 10, 14]
        else:
            t = [3, 7, 11, 15]
    elif m==8:
        if state < 8:
            t = [0, 8, 16, 24, 32, 40, 48]
        elif state < 16:
            t = [1, 9, 17, 25, 33, 41, 49, 57]
        elif state < 24:
            t = [2, 10, 18, 26, 34, 42, 50, 58]
        elif state < 32:
            t = [3, 11, 19, 27, 35, 43, 51, 59]
        elif state < 40:
            t = [4, 12, 20, 28, 36, 44, 52, 60]
        elif state < 48:
            t = [5, 13, 21, 29, 37, 45, 53, 61]
        elif state < 54:
            t = [6, 14, 22, 30, 38, 46, 54, 62]
        else:
            t = [7, 15, 23, 31, 39, 47, 55, 63]
    temp=[]
    length=len(t)
    for i in range(length):
        temp.append(t[length-i-1])
    return temp

# returns the previous values for each state
def getValues(state,m):
    if m==2:
        if state == 3:
            return [1,1]
        if state == 2:
            return [1,-1]
        if state == 1:
            return [-1,1]
        if state == 0:
            return [-1,-1]
    elif m==4:
        r=1/math.sqrt(2)
        t=[complex(r,r),complex(-r,r),complex(-r,-r),complex(r,-r)]
        if state == 15:
            return [t[0], t[0]]
        if state == 14:
            return [t[0], t[1]]
        if state == 13:
            return [t[0], t[2]]
        if state == 12:
            return [t[0], t[3]]
        if state == 11:
            return [t[1], t[0]]
        if state == 10:
            return [t[1], t[1]]
        if state == 9:
            return [t[1], t[2]]
        if state == 8:
            return [t[1], t[3]]
        if state == 7:
            return [t[2], t[0]]
        if state == 6:
            return [t[2], t[1]]
        if state == 5:
            return [t[2], t[2]]
        if state == 4:
            return [t[2], t[3]]
        if state == 3:
            return [t[3], t[0]]
        if state == 2:
            return [t[3], t[1]]
        if state == 1:
            return [t[3], t[2]]
        if state == 0:
            return [t[3], t[3]]

    elif m==8:
        r=1/math.sqrt(2)
        t = [1,complex(r, r), complex(0, 1),complex(-r, r), -1, complex(-r, -r), complex(0, -1),complex(r, -r)]

        if state == 63:
            return [t[0], t[0]]
        if state == 62:
            return [t[0], t[1]]
        if state == 61:
            return [t[0], t[2]]
        if state == 60:
            return [t[0], t[3]]
        if state == 59:
            return [t[0], t[4]]
        if state == 58:
            return [t[0], t[5]]
        if state == 57:
            return [t[0], t[6]]
        if state == 56:
            return [t[0], t[7]]
        if state == 55:
            return [t[1], t[0]]
        if state == 54:
            return [t[1], t[1]]
        if state == 53:
            return [t[1], t[2]]
        if state == 52:
            return [t[1], t[3]]
        if state == 51:
            return [t[1], t[4]]
        if state == 50:
            return [t[1], t[5]]
        if state == 49:
            return [t[1], t[6]]
        if state == 48:
            return [t[1], t[7]]
        if state == 47:
            return [t[2], t[0]]
        if state == 46:
            return [t[2], t[1]]
        if state == 45:
            return [t[2], t[2]]
        if state == 44:
            return [t[2], t[3]]
        if state == 43:
            return [t[2], t[4]]
        if state == 42:
            return [t[2], t[5]]
        if state == 41:
            return [t[2], t[6]]
        if state == 40:
            return [t[2], t[7]]
        if state == 39:
            return [t[3], t[0]]
        if state == 38:
            return [t[3], t[1]]
        if state == 37:
            return [t[3], t[2]]
        if state == 36:
            return [t[3], t[3]]
        if state == 35:
            return [t[3], t[4]]
        if state == 34:
            return [t[3], t[5]]
        if state == 33:
            return [t[3], t[6]]
        if state == 32:
            return [t[3], t[7]]
        if state == 31:
            return [t[4], t[0]]
        if state == 30:
            return [t[4], t[1]]
        if state == 29:
            return [t[4], t[2]]
        if state == 28:
            return [t[4], t[3]]
        if state == 27:
            return [t[4], t[4]]
        if state == 26:
            return [t[4], t[5]]
        if state == 25:
            return [t[4], t[6]]
        if state == 24:
            return [t[4], t[7]]
        if state == 23:
            return [t[5], t[0]]
        if state == 22:
            return [t[5], t[1]]
        if state == 21:
            return [t[5], t[2]]
        if state == 20:
            return [t[5], t[3]]
        if state == 19:
            return [t[5], t[4]]
        if state == 18:
            return [t[5], t[5]]
        if state == 17:
            return [t[5], t[6]]
        if state == 16:
            return [t[5], t[7]]
        if state == 15:
            return [t[6], t[0]]
        if state == 14:
            return [t[6], t[1]]
        if state == 13:
            return [t[6], t[2]]
        if state == 12:
            return [t[6], t[3]]
        if state == 11:
            return [t[6], t[4]]
        if state == 10:
            return [t[6], t[5]]
        if state == 9:
            return [t[6], t[6]]
        if state == 8:
            return [t[6], t[7]]
        if state == 7:
            return [t[7], t[0]]
        if state == 6:
            return [t[7], t[1]]
        if state == 5:
            return [t[7], t[2]]
        if state == 4:
            return [t[7], t[3]]
        if state == 3:
            return [t[7], t[4]]
        if state == 2:
            return [t[7], t[5]]
        if state == 1:
            return [t[7], t[6]]
        if state == 0:
            return [t[7], t[7]]

# assigns the path to the next nodes, if the delta is smaller than the existing one or if there is not an existing path
# then  the delta and path are overwritten
def getPath(values,nodes,col,state,m,c):
    next = nextState(state,m)

    if col > 199:
        i=next[0]
        st = getValues(state, m)
        st0 = getValues(i, m)[0]
        if col == 203:
            return
        delta = abs(values[col] - (c[0] * st0 + c[1] * st[0] + st[1] * c[2])) ** 2 + nodes[state][col].delta

        if nodes[i][col + 1].delta == -1 or nodes[i][col + 1].delta > delta:
            nodes[i][col + 1].delta = delta
            nodes[i][col + 1].prevState = state
    else:
        for i in next:
            st=getValues(state,m)
            st0=getValues(i,m)[0]

            delta=abs(values[col]-(c[0]*st0+c[1]*st[0]+st[1]*c[2]))**2 + nodes[state][col].delta
            if nodes[i][col+1].delta==-1 or nodes[i][col+1].delta>delta:
                nodes[i][col + 1].delta = delta
                nodes[i][col + 1].prevState = state


def MLSE(values,c, m):
    L=3
    N=200
    vert = m**(L-1)
    hor = 203
    nodes = [[Node() for j in range(hor+1)] for i in range(vert)]

    for i in range(vert):
        for j in range(hor):
            nodes[i][j].state=i
            nodes[i][j].col=j

    state=vert-1
    col=0
    nodes[state][col].delta=0
    nodes[state][col].prevState = state
    for i in range(hor):#col
        for j in range(vert):#state
            if nodes[vert-1-j][i].delta!=-1:
                getPath(values, nodes,i,vert-1-j,m,c)



    temp=[]
    state=vert-1
    for i in range(200):
        state=nodes[state][201-i].prevState
        temp.append(getValues(state,m)[0])


    temp.reverse()
    return temp

def getCIR3(staticC: bool):
    global u, u1, u2, u3, v1, v2
    if staticC is True:
        return [complex(0.89, 0.92), complex(0.42, -0.37), complex(0.19, 0.12)]
    else:
        kv = [0] * 6
        for i in range(6):
            kv[i] = gauss()
            u = np.random.random()
            u1 = np.random.random()
            u2 = np.random.random()
            u3 = np.random.random()
            v1 = np.random.random() * 2 - 1
            v2 = np.random.random() * 2 - 1
        return [complex(kv[0]/np.sqrt(2.3), kv[1]/np.sqrt(2.3)), complex(kv[2]/np.sqrt(2.3), kv[3]/np.sqrt(2.3)), complex(kv[4]/np.sqrt(2.3), kv[5]/np.sqrt(2.3))]

def add_noise(symbols, snr, f_bits) -> tuple:
    sigmavar = sigma(snr, f_bits)
    r_k=[]
    for i in symbols:
        r_k.append(i + 1*sigmavar * complex(random.gauss(0,1)/math.sqrt(2), random.gauss(0,1)/math.sqrt(2)) )
    return r_k

def sigma(snr, f_bits):
    return 1/math.sqrt(10**(snr/10)*2*f_bits)

def calculate_BER(transmitted: tuple, received: tuple) -> float:
    errorsBER = 0
    for i in range(len(transmitted)):  # for each transmitted bit
        if (not transmitted[i] == (
                received[i])):  # check if the transmitted bit is the same as the corresponding recieved bit
            errorsBER += 1  # if it is not identical, add 1 to the error count
    return (errorsBER / len(transmitted)*1.0)  # return the error count over the total transmitted bits
    pass

def gauss() -> float:
    global u, u1, u2, u3, v1, v2
    if u <= 0.8638:
        return 2 * (u1 + u2 + u3 - 1.5)
    elif u <= 0.8638 + 0.1107:
        return 1.5 * (u1 + u2 - 1)
    elif u <= 0.8638 + 0.1107 + 0.0228002039:
        x = 6 * u1 - 3
        y = 0.358 * u2
        if abs(x) < 1:
            temp = 17.49731196 * math.exp(-0.5 * x * x) - 4.73570326 * (3 - x ** 2)
        elif abs(x) < 1.5:
            temp = 17.49731196 * math.exp(-0.5 * x * x) - 2.36785163 * ((3 - abs(x)) ** 2) - 2.15787544 * (1.5 - abs(x))
        elif abs(x) < 3:
            temp = 17.49731196 * math.exp(-0.5 * x * x) - 2.36785163 * ((3 - abs(x)) ** 2)
        else:
            temp = 0.0
        while y >= temp:
            u1 = np.random.random()
            u2 = np.random.random()
            x = 6 * u1 - 3
            y = 0.358 * u2
            if abs(x) < 1:
                temp = 17.49731196 * math.exp(-0.5 * x * x) - 4.73570326 * (3 - x ** 2)
            elif abs(x) < 1.5:
                temp = 17.49731196 * math.exp(-0.5 * x * x) - 2.36785163 * ((3 - abs(x)) ** 2) - 2.15787544 * (
                        1.5 - abs(x))
            elif abs(x) < 3:
                temp = 17.49731196 * math.exp(-0.5 * x * x) - 2.36785163 * ((3 - abs(x)) ** 2)
            else:
                temp = 0.0
        return x
    x = v1 * (((9 - 2 * math.log(v1 ** 2 + v2 ** 2)) / (v1 ** 2 + v2 ** 2)) ** 0.5)
    y = v2 * (((9 - 2 * math.log(v1 ** 2 + v2 ** 2)) / (v1 ** 2 + v2 ** 2)) ** 0.5)
    while x <= 3 or y <= 3:
        v1 = np.random.random() * 2 - 1
        v2 = np.random.random() * 2 - 1
        x = v1 * (((9 - 2 * math.log(v1 ** 2 + v2 ** 2)) / (v1 ** 2 + v2 ** 2)) ** 0.5)
        y = v2 * (((9 - 2 * math.log(v1 ** 2 + v2 ** 2)) / (v1 ** 2 + v2 ** 2)) ** 0.5)
    if y > 3:
        return y
    return x
    pass

def bit_detection(symbols: tuple, constellation='bpsk') -> tuple:
    # All encoding assumes clear (non-noisy) symbol values, and therefore needs to be run through the symbol detection algorithm to return defined symbols before running this algorithm

    bits = []
    if constellation == 'bpsk':
        for i in range(len(symbols)):
            if symbols[i] == 1:  # if the detected symbol is a 1, return a 1 bit. else return a 0 bit
                bits.append(1)
            else:
                bits.append(0)

    if constellation == 'qpsk':
        for i in range(len(
                symbols)):  # append the bits corresponding to the detected symbol, according to the bit allocation in the notes.
            if symbols[i] == 1:
                bits.append(0)
                bits.append(0)
            elif symbols[i] == -1:
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(0, 1):
                bits.append(0)
                bits.append(1)
            elif symbols[i] == complex(0, -1):
                bits.append(1)
                bits.append(0)

    if constellation == '4qam':
        for i in range(len(
                symbols)):  # append the bits corresponding to the detected symbol, according to the bit allocation in the notes.
            if symbols[i] == complex(1 / cmath.sqrt(2), 1 / cmath.sqrt(2)):
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(-1 / cmath.sqrt(2), 1 / cmath.sqrt(2)):
                bits.append(0)
                bits.append(1)
            elif symbols[i] == complex(-1 / cmath.sqrt(2), -1 / cmath.sqrt(2)):
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(1 / cmath.sqrt(2), -1 / cmath.sqrt(2)):
                bits.append(1)
                bits.append(0)

    if constellation == '8psk':
        for i in range(len(
                symbols)):  # append the bits corresponding to the detected symbol, according to the bit allocation in the notes.
            if symbols[i] == 1:
                bits.append(1)
                bits.append(1)
                bits.append(1)
            elif symbols[i] == -1:
                bits.append(0)
                bits.append(0)
                bits.append(1)
            elif symbols[i] == complex(0, 1):
                bits.append(0)
                bits.append(1)
                bits.append(0)
            elif symbols[i] == complex(0, -1):
                bits.append(1)
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(1 / cmath.sqrt(2), 1 / cmath.sqrt(2)):
                bits.append(1)
                bits.append(1)
                bits.append(0)
            elif symbols[i] == complex(-1 / cmath.sqrt(2), 1 / cmath.sqrt(2)):
                bits.append(0)
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(-1 / cmath.sqrt(2), -1 / cmath.sqrt(2)):
                bits.append(0)
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(1 / cmath.sqrt(2), -1 / cmath.sqrt(2)):
                bits.append(1)
                bits.append(0)
                bits.append(1)

    if constellation == '16qam':
        for i in range(len(
                symbols)):  # append the bits corresponding to the detected symbol, according to the bit allocation in the notes.
            if symbols[i] == complex(1, 1):
                bits.append(1)
                bits.append(0)
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(-1, 1):
                bits.append(0)
                bits.append(0)
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(-1, -1):
                bits.append(0)
                bits.append(0)
                bits.append(1)
                bits.append(0)
            elif symbols[i] == complex(1, -1):
                bits.append(1)
                bits.append(0)
                bits.append(1)
                bits.append(0)
            elif symbols[i] == complex(1 / 3, 1):
                bits.append(1)
                bits.append(1)
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(-1 / 3, 1):
                bits.append(0)
                bits.append(1)
                bits.append(0)
                bits.append(0)
            elif symbols[i] == complex(-1 / 3, -1):
                bits.append(0)
                bits.append(1)
                bits.append(1)
                bits.append(0)
            elif symbols[i] == complex(1 / 3, -1):
                bits.append(1)
                bits.append(1)
                bits.append(1)
                bits.append(0)
            elif symbols[i] == complex(1, 1 / 3):
                bits.append(1)
                bits.append(0)
                bits.append(0)
                bits.append(1)
            elif symbols[i] == complex(1, -1 / 3):
                bits.append(1)
                bits.append(0)
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(-1, 1 / 3):
                bits.append(0)
                bits.append(0)
                bits.append(0)
                bits.append(1)
            elif symbols[i] == complex(-1, -1 / 3):
                bits.append(0)
                bits.append(0)
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(1 / 3, 1 / 3):
                bits.append(1)
                bits.append(1)
                bits.append(0)
                bits.append(1)
            elif symbols[i] == complex(1 / 3, -1 / 3):
                bits.append(1)
                bits.append(1)
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(-1 / 3, -1 / 3):
                bits.append(0)
                bits.append(1)
                bits.append(1)
                bits.append(1)
            elif symbols[i] == complex(-1 / 3, 1 / 3):
                bits.append(0)
                bits.append(1)
                bits.append(0)
                bits.append(1)

    return tuple(bits)
    pass

static=True
m=4

BER = []
x = []
for i in range(16):
    BER.append(0)
    x.append(i)

for k in range(100):
    print(k)
    c=getCIR3(static)
    values=[]
    const=""
    N=math.log(m,2)
    if m==2:
        options=[1,-1]
        const = "bpsk"
    elif m==4:
        const = "4qam"
        options = [complex(1/math.sqrt(2),1/math.sqrt(2)),complex(-1/math.sqrt(2),-1/math.sqrt(2)),complex(-1/math.sqrt(2),1/math.sqrt(2)),complex(1/math.sqrt(2),-1/math.sqrt(2))]
    else:
        const="8psk"
        options = [1,complex(1/math.sqrt(2),1/math.sqrt(2)),complex(-1/math.sqrt(2),-1/math.sqrt(2)),complex(-1/math.sqrt(2),1/math.sqrt(2)),complex(1/math.sqrt(2),-1/math.sqrt(2)),-1,1j,-1j]
    for i in range(200):
        values.append(random.choice(options))

    values.append(options[0])
    values.append(options[0])
    values.append(options[0])
    noisy=[]
    prev1=options[0]
    prev2=options[0]
    for i in range(203):
        noisy.append(values[i]*c[0]+prev1*c[1]+prev2*c[2])
        prev2=prev1
        prev1=values[i]


    #print(values)
    for i in range(16):
        noise=add_noise(copy.copy(noisy),i,N)
    #    print(noise)
        t=MLSE(noise, c, m)
    #    print(t)
        BER[i]+=calculate_BER(bit_detection(t,const), bit_detection(values,const))

for i in range(16):
    BER[i]/=100

ct=""
if static:
    ct="Static"
else:
    ct="Dynamic"
title=const+" BER for SNR of [0,15] dB, CIR "+ct+", MLSE"
plt.title(title)
plt.xlabel("SNR (db)")
plt.ylabel("Error Rate (%/100)")
plt.semilogy(x, BER, label="BER")
plt.legend()
plt.show()