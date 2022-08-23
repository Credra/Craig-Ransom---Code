import math
import matplotlib.pyplot as plt

# global variables
uniform_seed1 = 1
uniform_seed2 = 1
uniform_seed3 = 1

def seed_uniform(seed1:int, seed2:int, seed3:int)->None:
    # Sets the global seeds to the passed values
    global uniform_seed1, uniform_seed2, uniform_seed3
    uniform_seed1 = seed1
    uniform_seed2 = seed2
    uniform_seed3 = seed3

    # ensures that the seeds are with the correct range, seeds should be between 1 and 30 000
    if uniform_seed1<1:
        uniform_seed1+=30000
    elif uniform_seed1>30000:
        uniform_seed1-=30000
        uniform_seed2+=1
    if uniform_seed2<1:
        uniform_seed2+=30000
    elif uniform_seed2>30000:
        uniform_seed2-=30000
        uniform_seed3+=1
    if uniform_seed3<1:
        uniform_seed3+=30000
    elif uniform_seed3>30000:
        uniform_seed3-=30000


def uniform():
    # Wichmann-Hill algorithm for uniform random number generation between 0 and 1
    global uniform_seed1, uniform_seed2, uniform_seed3
    # First generator
    x = 171 * (uniform_seed1 % 171) - 2 * (uniform_seed1 / 171)
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
    # linearly combining the generators
    temp = x / 30269.0 + y / 30307.0 + z / 30323.0
    temp %= 1
    return temp

def go():
    # number of data points
    n=5000
    values=[]
    for i in range(n):
        values.append(uniform())
        seed_uniform(uniform_seed1+1, uniform_seed2+1, uniform_seed3+1)

    # mean
    mu = sum(values)/len(values)

    # variance
    sigma_2=0
    for i in values:
        sigma_2 += abs(i - mu)**2
    sigma_2 /= len(values)

    # standard deviation
    sigma = sigma_2**0.5


    print("meanGenerated: ", mu)
    print("stdG: ", sigma)
    print("varG: ", sigma_2)

    #PDF

    numberOfRectangles=20

    #Theoretical
    y=[1]*numberOfRectangles
    x=[0]*numberOfRectangles
    x[numberOfRectangles-1]=1
    plt.plot(x,y)

    #Generated
    delta=1/numberOfRectangles #the change between each histogram
    x=[0]*numberOfRectangles
    for i in range(numberOfRectangles):
        x[i] = (i+0.5)*delta #mid-point of the current histogram
        for j in values:
            if i*delta <= j and j < (i+1)*delta:
                y[i]+=1
        y[i]/=(n*delta)

    plt.xlim((0,1))
    plt.ylim((0,1.5))
    plt.plot(x,y)

    plt.legend(["Theoretical", "Generated"])
    plt.show()