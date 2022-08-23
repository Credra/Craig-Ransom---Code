import math
import csv
import random
import matplotlib.pyplot as plt

#D inputs =4
D=4
#number of H hidden neutrons
H=1
#C outputs =1
C=1

#N - number of data samples
N=10**6

bias=1

#n=learning rate
n=0.5

#epochs - number of times it must be repeated to train the NN
epochs=15
#errorConvergence
errorConvergence=1e-7

def task1(input, target, inputWeights, outputWeights, hiddenWeights = None):
    Etotal=[0]*epochs
    for i in range(epochs):
        output = [0] * N
        layers=1

        #if there are 1 or 2 hidden layers
        if hiddenWeights:
            layers = 2
        h=[[0]*H]*layers
        for m in range(N):
            #passing inputs through ANN
            for k in range(H):#each hidden neuron
                for l in range(D):#each link
                    h[0][k]+=input[m][l]*inputWeights[k][l]
                h[0][k]+=bias*inputWeights[k][D]
                h[0][k]=math.tanh(h[0][k])

            #if there are 2 hidden layers
            if hiddenWeights:
                # passing inputs through ANN
                for k in range(H):  # each hidden neuron
                    for l in range(H):  # each link
                        h[1][k] += h[0][l] * hiddenWeights[k][l]
                    h[1][k] += bias * hiddenWeights[k][H]
                    h[1][k] = math.tanh(h[1][k])

                    for k in range(H):
                        output[m] += outputWeights[k] * h[1][k]
                    output[m] += outputWeights[H] * bias
                    output[m]=math.tanh(output[m])

                    #back-propergation
                    deltaK = (target[m] - output[m]) * (1 - output[m]**2)

                    for k in range(H):
                        outputWeights[k] += n * deltaK * h[1][k]
                    outputWeights[H] += n * deltaK * bias

                    deltaJ = [0] * H

                    for k in range(H):
                        deltaJ[k] = (1 - h[1][k]**2) * deltaK * outputWeights[k]

                    for k in range(H):
                        for l in range(H):
                            hiddenWeights[k][l] += n * deltaJ[l] * h[0][k]
                        hiddenWeights[k][H] += n * deltaJ[l] * bias

                    deltaI = [0] * H
                    for k in range(H):
                        for l in range(H):
                            deltaI[k]+=deltaJ[l] * hiddenWeights[k][l]
                        deltaI[k] *= (1 - h[0][k] ** 2)

                    # (D + 1) × H
                    for l in range(H):
                        for k in range(D):
                            inputWeights[l][k] += n * deltaI[l] * input[m][k]
                        inputWeights[l][D] += n * deltaI[l]*bias

            # only 1 hidden layer
            else:
                # passing inputs through ANN
                for k in range(H):
                    output[m]+=outputWeights[k]*h[0][k]
                output[m] += outputWeights[H] * bias
                output[m] = math.tanh(output[m])

                # back-propergation
                deltaK=(target[m]-output[m])*(1-output[m]**2)

                for k in range(H):
                    outputWeights[k]+=n*deltaK*h[0][k]
                outputWeights[H]+=n*deltaK*bias

                deltaJ=[0]*H
                for k in range(H):
                    deltaJ[k]= (1-h[0][k]**2)*deltaK*outputWeights[k]

                for k in range(H):  # each hidden neuron
                    for l in range(D):  # each link
                        inputWeights[k][l] += n * deltaJ[k] * input[m][l]

            #Finding average of the error per epoch
            Etotal[i]+=(target[m]-output[m]) ** 2
        Etotal[i]/=2
        Etotal[i] /= N

        if i>0 and abs(Etotal[i]-Etotal[i-1])<errorConvergence:
            return Etotal
    return Etotal

#normalises input data
def convert(char):
    if char=="S":
        return 0.9
    elif char=="R":
        return -0.9
    return 0

#input - 4 × N array
inputs=[[0]*4]*N

#target - The target values as an array of N length
target=[0]*N

#inputWeights - weights from input layer to hidden layer, a (D+1) × H array.
inputWeights=[[0]*(D+1)]*H

# outputWeights - array of length (H+1)
outputWeights=[0]*(H+1)


hiddenWeights=[[0]*(H+1)]*H

#reads in data from .csv
with open("data1.csv", newline='') as f:
    reader = csv.reader(f)
    response = list(reader)

#randomises weights
for i in range(H):
    for j in range(D+1):
        inputWeights[i][j]=random.uniform(-1,1)

for i in range(H):
    for j in range(H+1):
        hiddenWeights[i][j]=random.uniform(-1,1)

for i in range(H+1):
    outputWeights[i]=random.uniform(-1,1)

pos=0
#normalises data from .csv
for i in response:
    temp=list(i)
    for j in range(4):
        inputs[pos][j]=convert(temp[0][j])
    target[pos]=convert(temp[1][0])
    pos+=1

#remove the comment from the line below to make it only have one hidden layer
#hiddenWeights=None

Etot=task1(inputs, target, inputWeights, outputWeights, hiddenWeights)
pos=0

for i in Etot:
    if i==0:
        Etot[pos]=Etot[pos-1]
    pos+=1
xAxis = list(range(0, epochs))

plt.plot(xAxis, Etot, label="")
plt.xlabel('Epochs')
plt.ylabel('L2')
plt.show()