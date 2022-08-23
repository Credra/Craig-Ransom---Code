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
def seed_uniform(seed1: int, seed2: int, seed3: int) -> None:
    """Advanced update of seed values for URNG
    :param seed1: the first seed value for WH Algorithm
    :param seed2: the second seed value for WH Algorithm
    :param seed3: the third seed value for WH Algorithm
    """
    global uniform_seed1, uniform_seed2, uniform_seed3
    uniform_seed1 = seed1
    uniform_seed2 = seed2
    uniform_seed3 = seed3
    if uniform_seed1 < 1:
        uniform_seed1 += 30000
    elif uniform_seed1 > 30000:
        uniform_seed1 -= 30000
    if uniform_seed2 < 1:
        uniform_seed2 += 30000
    elif uniform_seed2 > 30000:
        uniform_seed2 -= 30000
    if uniform_seed3 < 1:
        uniform_seed3 += 30000
    elif uniform_seed3 > 30000:
        uniform_seed3 -= 30000

    pass


def uniform() -> float:
    """Wichmann-Hill algorithm
    Use global variables
    """
    global uniform_seed1, uniform_seed2, uniform_seed3
    # First generator
    x = 171 * (uniform_seed1 % 177) - 2 * (uniform_seed1 / 177)
    if x < 0:
        x += 30269
    # Second generator
    y = 172 * (uniform_seed2 % 176) - 35 * (uniform_seed2 / 176)
    if y < 0:
        y += 30307
    # Third generator
    z = 170 * (uniform_seed3 % 178) - 63 * (uniform_seed3 / 178)
    if z < 0:
        z += 30323

    temp = x / 30269.0 + y / 30307.0 + z / 30323.0
    temp %= 1
    return temp
    pass


def calculate_uniform_stats(N: int) -> tuple:
    """calculate the statistical parameters for the random sequence give
    :param N: the number of values to generate
    :return: tuple containing (mu, sigma, sigma_2) in that order
    """
    values = [uniform() for x in range(N)]

    # seeding test values:
    # values = [0]*N
    # for i in range(N):
    #  values[i]=uniform()
    #  seed_uniform(uniform_seed1+1, uniform_seed2+1, uniform_seed3+1)

    # mean
    mu = sum(values) / len(values)

    # variance
    sigma_2 = 0
    for i in values:
        sigma_2 += abs(i - mu) ** 2
    sigma_2 /= len(values)

    # standard deviation
    sigma = sigma_2 ** 0.5

    return (mu, sigma, sigma_2)

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


def calculate_gauss_stats(n: int) -> tuple:
    """calculate the statistical parameters for the random sequence give
    :param N: the number of values to generate
    :return: tuple containing (mu, sigma, sigma_2) in that order
    """
    global u, u1, u2, u3, v1, v2

    # values = [gauss() for x in range(N)]
    values = [0] * n
    for i in range(n):
        values[i] = gauss()
        u = np.random.random()
        u1 = np.random.random()
        u2 = np.random.random()
        u3 = np.random.random()
        v1 = np.random.random() * 2 - 1
        v2 = np.random.random() * 2 - 1

    # mean
    mu = sum(values) / len(values)

    # variance
    sigma_2 = 0
    for i in values:
        sigma_2 += abs(i - mu) ** 2
    sigma_2 /= len(values)

    # standard deviation
    sigma = sigma_2 ** 0.5

    return (mu, sigma, sigma_2)


def generate_N_bits(N) -> tuple:
    # Use your uniform random number generator to generate random bits. A value larger
    # than 0.5 is a 1 bit; a value less than or equal to 0.5 is a 0 bit. This will ensure a 50/50 0/1 bit ratio.
    values = [0] * N
    for i in range(N):
        rnd1 = np.round(np.random.random(1) * 100)
        rnd2 = np.round(np.random.random(1) * 100)
        rnd3 = np.round(np.random.random(1) * 100)
        # print(rnd1)
        if i % 2 == 0:
            seed_uniform(uniform_seed1 + rnd1, uniform_seed2 + rnd2, uniform_seed3 + rnd3)
        else:
            seed_uniform(uniform_seed1 - rnd1, uniform_seed2 - rnd2, uniform_seed3 - rnd3)
        values[i] = uniform()[0]
    values = np.round(list(values))
    newValues = []
    for i in values:
        if i==0:
            newValues.append(0)
        else:
            newValues.append(1)
    # print(values)
    # print(newValues)
    return newValues


def bits_to_symbols(bits: tuple, constellations='bpsk') -> tuple:
    # Map the bits to symbols using the respective modulation constellations.
    # If we generate an even number of bits, how would we go about encoding something like 8psk which requires 3 bit groupings? What should we do with the remainders?
    # Answer:  you just ignore them or add a few extra. Just keep in mind the total number of bits when you calculate the BER.
    symbols = []
    if constellations == 'bpsk':
        for bit in bits:
            if bit == 0:
                symbols.append(1)
            else:
                symbols.append(-1)

    if constellations == '4qam':
        for i in range(0, len(bits) , 2):
            if bits[i] == 0:
                if bits[i + 1] == 0:
                    symbols.append(complex(1, 1) / cmath.sqrt(2))
                else:
                    symbols.append(complex(-1, 1)  / cmath.sqrt(2))
            else:
                if bits[i + 1] == 1:
                    symbols.append(complex(-1, -1) / cmath.sqrt(2))
                else:
                    symbols.append(complex(1, -1) / cmath.sqrt(2))

    if constellations == '8psk':
        for i in range(0, len(bits), 3):
            if bits[i] == 0:
                if bits[i + 1] == 0:
                    if bits[i + 2] == 0:
                        # 000
                        symbols.append(complex(-1, -1) / cmath.sqrt(2))
                    else:
                        # 001
                        symbols.append(-1)
                else:
                    if bits[i + 2] == 0:
                        # 010
                        symbols.append(complex(0, 1))
                    else:
                        # 011
                        symbols.append(complex(-1, 1) / cmath.sqrt(2))
            else:
                if bits[i + 1] == 0:
                    if bits[i + 2] == 0:
                        # 100
                        symbols.append(complex(0, -1))
                    else:
                        # 101
                        symbols.append(complex(1, -1) / cmath.sqrt(2))
                else:
                    if bits[i + 2] == 0:
                        # 110
                        symbols.append(complex(1, 1) / cmath.sqrt(2))
                    else:
                        # 111
                        symbols.append(1)


    return tuple(symbols)


def get_fbits(constellation='bpsk') -> int:
    M = 0
    if constellation == 'bpsk':
        M = 2
    elif constellation == 'qpsk':
        M = 4
    elif constellation == '4qam':
        M = 4
    elif constellation == '8psk':
        M = 8
    else:
        M = 16
    fbit = math.log(M, 2)
    return fbit


def sigma(snr: float, fbits: int):
    return 1 / cmath.sqrt((10 ** (snr/ 10)) * 2 * fbits * 3)

def calculate_BER(transmitted: tuple, received: tuple) -> float:
    errorsBER = 0
    for i in range(len(transmitted)):  # for each transmitted bit
        if (not transmitted[i] == (
                received[i])):  # check if the transmitted bit is the same as the corresponding recieved bit
            errorsBER += 1  # if it is not identical, add 1 to the error count
    return (errorsBER / len(transmitted)*1.0)  # return the error count over the total transmitted bits
    pass

def add_noise(symbols: tuple, snr: float, f_bits: int) -> tuple:
    # generate random values
    global u, u1, u2, u3, v1, v2
    N = len(symbols) * 2
    values = [0] * N
    for i in range(N):
        values[i] = gauss()
        u = np.random.random()
        u1 = np.random.random()
        u2 = np.random.random()
        u3 = np.random.random()
        v1 = np.random.random() * 2 - 1
        v2 = np.random.random() * 2 - 1
    # generate narray which will be the complex numbers n_k
    n = []
    for i in range(0, len(values) - 1, 2):
        n.append(complex(values[i], values[i + 1]) / cmath.sqrt(2))
    # add noise
    r_k = [0] * len(symbols)
    sigmavar = sigma(snr, f_bits)
    for i in range(len(symbols)):
        r_k[i] = symbols[i] + sigmavar * n[i]
    return r_k
# returns all the other possible states that this state can lead to



#!!!!!!!!!!!!!!!!!!!!!!new code!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
def nextState(state):
    t=[]
    if state==0:
        t = [0,2]
    elif state==1:
        t = [0,2]
    elif state==2:
        t = [1,3]
    elif state==3:
        t = [1,3]
    return t

# returns the previous values for each state
def getValues(state):
    if state == 3 or state == 2:
        return 1
    if state == 1 or state == 0:
        return 0

def getCode(cur,prev):
    if cur == 0:
        if prev == 0:
            return [-1, -1, -1]
        elif prev == 1:
            return [-1, -1, 1]
    elif cur==1:
        if prev==2:
            return [-1,1,-1]
        elif prev==3:
            return [-1,1,1]
    elif cur==2:
        if prev==1:
            return [1,1,-1]
        elif prev==0:
            return [1,1,1]
    elif cur==3:
        if prev==3:
            return [1,-1,-1]
        elif prev==2:
            return [1,-1,1]


# assigns the path to the next nodes, if the delta is smaller than the existing one or if there is not an existing path
# then  the delta and path are overwritten
def getPath(ct,nodes,col,state):
    next = nextState(state)
    if col == 101:
        return
    else:
        for i in next:
            codes=getCode(i,state)
            delta=abs(ct[col*3]-codes[0])**2
            delta+= abs(ct[col*3+1]-codes[1])**2
            delta+=abs(ct[col*3+2]-codes[2])**2
            delta+=nodes[state][col].delta
            if nodes[i][col+1].delta==-1 or nodes[i][col+1].delta>delta:
                nodes[i][col + 1].delta = delta
                nodes[i][col + 1].prevState = state

def encode(bits):
    temp=[]
    prev0=0
    prev1=0
    for i in bits:
        temp.append(i)
        temp.append(XOR(i,prev0))
        temp.append(XOR(i, prev1))
        prev1=prev0
        prev0=i
    return temp

def XOR(a,b):
    if a==0:
        if b==0:
            return 0
    else:
        if b == 1:
            return 0
    return 1


def decode(values):
    N=102
    vert = 4
    hor = N
    nodes = [[Node() for j in range(hor)] for i in range(vert)]

    for i in range(vert):
        for j in range(hor):
            nodes[i][j].state=i
            nodes[i][j].col=j

    state=0
    col=0
    nodes[state][col].delta=0
    nodes[state][col].prevState = state
    for i in range(hor):#col
        for j in range(vert):#state
            if nodes[j][i].delta!=-1:
                getPath(values, nodes,i,j)
    temp=[]
    state=0
    for i in range(3):
        if nodes[state][N-1].delta >nodes[i+1][N-1].delta:
            state=i+1

    for i in range(N):
        state=nodes[state][N-1-i].prevState
        temp.append(getValues(state))

    answer=[]
    temp.reverse()
    for i in range(100):
        answer.append(temp[i+2])
    return answer

def euclidian(a,b):
    return abs(a - b)**2

def getSmallest(options, sym):
    min=1000
    pos=-1
    for i in range(len(options)):
        temp=math.pow(abs(options[i] - sym),2)
        if temp<min:
            min=temp
            pos=i

    return pos

def symToBits(sym, m):
    dist = []
    if m==2:
        options = [1, -1]
    elif m==4:
        options = [complex(1 , 1)/cmath.sqrt(2),complex(-1 , 1)/cmath.sqrt(2),complex(1 , -1)/cmath.sqrt(2),complex(-1 , -1)/cmath.sqrt(2)]

    elif m == 8:
        options = [complex(-1 , -1)/cmath.sqrt(2), -1, complex(0, 1), complex(-1 , 1)/cmath.sqrt(2), complex(0, -1), complex(1 , -1)/cmath.sqrt(2), complex(1 , 1)/cmath.sqrt(2), 1]
    bits = []
    for i in sym:
        temp = getSmallest(options, i)
        if m == 2:
            if temp==0:
                bits.append(0)
            else:
                bits.append(1)
        elif m == 4:
            if temp == 0:
                bits.append(0)
                bits.append(0)
            elif temp == 1:
                bits.append(0)
                bits.append(1)
            elif temp == 2:
                bits.append(1)
                bits.append(0)
            elif temp == 3:
                bits.append(1)
                bits.append(1)

        elif m == 8:
            if temp == 0:
                bits.append(0)
                bits.append(0)
                bits.append(0)
            elif temp == 1:
                bits.append(0)
                bits.append(0)
                bits.append(1)
            elif temp == 2:
                bits.append(0)
                bits.append(1)
                bits.append(0)
            elif temp == 3:
                bits.append(0)
                bits.append(1)
                bits.append(1)
            elif temp == 4:
                bits.append(1)
                bits.append(0)
                bits.append(0)
            elif temp == 5:
                bits.append(1)
                bits.append(0)
                bits.append(1)
            elif temp == 6:
                bits.append(1)
                bits.append(1)
                bits.append(0)
            elif temp == 7:
                bits.append(1)
                bits.append(1)
                bits.append(1)

    return bits

#100+2 bits ---> 306 encoded bits
m=2
N=math.log(m,2)
BERNC = []
BER = []
x = []
for i in range(16):
    BER.append(0)
    BERNC.append(0)
    x.append(i)

bits=list(generate_N_bits(100))
oBits=copy.copy(bits)
bits.append(0)
bits.append(0)
bits.append(0)
bits.append(0)

if m == 2:
    const = "bpsk"
elif m == 4:
    const = "4qam"
else:
    const = "8psk"
    bits.append(0)


encoded = encode(bits)

symbols=list(bits_to_symbols(encoded,const))
ncSymbols=list(bits_to_symbols(bits,const))
temp=[]

run=250
for j in range(run):
    print(j)
    for i in range(16):
        noiseS = add_noise(copy.deepcopy(symbols), i, N)
        noiseSnc = add_noise(copy.deepcopy(ncSymbols), i, N)
        temp = symToBits(noiseS,m)
        ncBits = symToBits(noiseSnc,m)
        t=[]
        tnc=[]
        for k in range(306):
            t.append(temp[k])
        for k in range(100):
            tnc.append(ncBits[k])
        t = decode(t)
        BER[i] += calculate_BER(oBits, t)
        BERNC[i] += calculate_BER(oBits, tnc)


for i in range(16):
    BER[i] /= run
    BERNC[i] /= run

title=const+" BER for SNR of [0,15] dB"
plt.title(title)
plt.xlabel("SNR (db)")
plt.ylabel("Error Rate")
plt.semilogy(x, BER, label="Convolution")
plt.semilogy(x, BERNC, label="No Convolution")
plt.legend()
plt.show()