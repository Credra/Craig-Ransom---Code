import random
import matplotlib.pyplot as plt

#the "ideal" used in the fitness function from data set 1
data1=['R', 'P', 'R', 'R', 'S', 'R', 'R', 'R', 'R', 'R', 'P', 'R', 'R', 'P', 'R', 'R', 'P', 'R', 'R', 'P', 'S', 'R', 'S', 'S', 'R', 'R', 'S', 'R', 'R', 'P', 'R', 'R', 'S', 'R', 'R', 'R', 'R', 'P', 'P', 'R', 'P', 'S', 'R', 'P', 'R', 'R', 'R', 'S', 'R', 'R', 'S', 'R', 'R', 'S', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'P', 'P', 'R', 'S', 'P', 'R', 'R', 'P', 'R', 'P', 'R', 'S', 'S', 'R', 'S', 'R', 'R', 'S']
#the "ideal" used in the fitness function from data set 2
data2=['R', 'P', 'R', 'R', 'S', 'S', 'R', 'R', 'R', 'S', 'P', 'R', 'P', 'P', 'R', 'R', 'P', 'P', 'S', 'P', 'S', 'S', 'S', 'S', 'P', 'R', 'S', 'R', 'R', 'P', 'R', 'R', 'S', 'R', 'R', 'R', 'S', 'P', 'P', 'R', 'P', 'S', 'S', 'P', 'R', 'S', 'S', 'S', 'P', 'S', 'S', 'S', 'S', 'S', 'R', 'S', 'S', 'R', 'P', 'P', 'R', 'R', 'S', 'P', 'P', 'P', 'S', 'P', 'P', 'R', 'P', 'P', 'P', 'S', 'S', 'S', 'R', 'S', 'R', 'P', 'S']
ideal=data2

#percentage of population to be culled
cull = 20
#percentage chance of an individual to be mutated
mutation = 20
#maximum number of iterations
maxIt = 200
#size of the population
populationSize = 100
#simulation runs n times
n = 50

#creates an individual with random genes
def createIndividual(individual):
    for i in range(81):
        individual[i] = random.choice(["R", "P", "S"])

#returns fitness score of individual
#1 point for each gene the same as the "ideal"
def fitnessIndividual(individual):
    score=0
    for i in range(81):
        if individual[i]==ideal[i]:
            score+=1
    return score

#returns fitness score of the population
def fitnessPopulation(population):
    fit=[]
    for i in population:
        fit.append(fitnessIndividual(i))
    return fit

#returns average fitness of population
def avgFitness(fitness):
    return sum(fitness)/len(fitness)

#returns fitness of most fit individual
def maxFitness(fitness):
    return max(fitness)

#culls the population by the culling rate then repopulates
#overwrites all the individuals that are to be culled with the new generation
#population is the entire current population
def cullPopulation(population):
    fitness = fitnessPopulation(population)
    orderPopulation(population,fitness)
    kill= round(cull/100*populationSize)
    pos=kill
    while pos<populationSize:
        population[pos]=newGenerationindividual(population,fitness,kill)
        pos+=1

#orders population in desecending order of fitness
#population is the entire current population
#fitnesss is an arrray of the fitness values of the population
#rearranges both fitness and population in descending order in terms of fitness
def orderPopulation(population,fitness):
    for i in range(populationSize):
        k=i
        for j in range(i+1,populationSize):
            if fitness[j]>fitness[k]:
                k=j
        fitness[i],fitness[k]=fitness[k],fitness[i]
        population[i], population[k] = population[k], population[i]

#selects two parents from the current population and return their child
#parents with a higher fitness are more likely to be selected
#population is the entire current population
#fitnesss is an arrray of the fitness values of the population
#pos is the position in which the previous population was culled at, all individuals at and after pos are to be ignored until they are overwritten
def newGenerationindividual(population, fitness, pos):
    total=0
    for i in range(pos):
        total+=fitness[i]
    rand=random.randint(0,total)
    i=-1
    while rand>0:
        i+=1
        rand-=fitness[i]

    rand = random.randint(0, total-fitness[i])
    j=-1
    while rand>0:
        j+=1
        if j==i:
            j+=1
        rand-=fitness[j]
    return crossover(population[i],fitness[i],population[j],fitness[j])

#creates a individual of the next generation from the genes of the parents
#par1 is parent 1, fit1 is the fitness of parent 1
#par2 is parent 2, fit2 is the fitness of parent 2
#the child recieves genes from parent 1 and 2 equal to the ratio of the parents fitness
#if the parents have the same fitness they alternativly give genes to the child
def crossover(par1, fit1, par2, fit2):
    temp=[""]*81
    if fit1!=fit2:
        ratio = round(fit2 / (fit1+fit2) * 81)
        for i in range(81):
            if i < ratio:
                temp[i] = par2[i]
            else:
                temp[i] = par1[i]
    else:
        for i in range(81):
            if i%2==0:
                temp[i]=par1[i]
            else:
                temp[i]=par2[i]
    rand = random.randint(0, 100)
    if rand < mutation:
        return mutate(temp)
    return temp

#mutates one random gene of an individual
def mutate(individual):
    rand = random.randint(0, 80)

    if individual[rand] == "R":
        individual[rand] = random.choice(["P", "S"])
    elif individual[rand] == "P":
        individual[rand] = random.choice(["R", "S"])
    else:
        individual[rand] = random.choice(["R", "P"])
    return individual

#plots the average number of iterations (run n times) it takes for an indidivual to have a fitness of 81 (max fitness) for changing mutation rates (0 to 99)
def mutationGraph():
    global mutation
    x=100
    avgIt = [0] * x
    for q in range(x):
        print(q)
        mutation = q
        for j in range(n):

            # populates the 1st generation
            population = []
            for i in range(populationSize):
                temp = [""] * 81
                createIndividual(temp)
                population.append(temp)

            # runs GA maxIt number of times or until an ideal individual is found
            iteration = 0
            maxS = 0
            while iteration < maxIt and maxS < 81:
                if iteration != 0:
                    cullPopulation(population)
                fitness = fitnessPopulation(population)
                maxS = maxFitness(fitness)
                iteration += 1
            avgIt[q] += iteration
        avgIt[q]/=n

    xAxis = list(range(0, x))

    plt.plot(xAxis, avgIt, label="Changing Mutation Rate")
    plt.xlabel('Mutation %')
    plt.ylabel('Number of Iterations to ideal')
    plt.legend()
    plt.show()

#plots the average number of iterations (run n times) it takes for an indidivual to have a fitness of 81 (max fitness) for changing cull rates (5 to 95)
def cullRateGraph():
    global cull
    x=90
    avgIt = [0] * x
    for q in range(x):
        print(q+5)
        cull = q+5
        for j in range(n):

            # populates the 1st generation
            population = []
            for i in range(populationSize):
                temp = [""] * 81
                createIndividual(temp)
                population.append(temp)

            # runs GA maxIt number of times or until an ideal individual is found
            iteration = 0
            maxS = 0
            while iteration < maxIt and maxS < 81:
                if iteration != 0:
                    cullPopulation(population)
                fitness = fitnessPopulation(population)
                maxS = maxFitness(fitness)
                iteration += 1
            avgIt[q] += iteration
        avgIt[q]/=n

    xAxis = list(range(5, 95))

    plt.plot(xAxis, avgIt, label="Changing Cull Rate")
    plt.xlabel('Cull %')
    plt.ylabel('Number of Iterations to ideal')
    plt.legend()
    plt.show()

##plots the average number of iterations (run n times) it takes for an indidivual to have a fitness of 81 (max fitness) for population size (10 to 1000)
def populationSizeGraph():
    global populationSize
    x=50
    avgIt = [0] * x
    xAxis = []
    for q in range(0,50):
        print(q)
        populationSize = (q+1)*10
        xAxis.append(populationSize)
        for j in range(n):

            # populates the 1st generation
            population = []
            for i in range(populationSize):
                temp = [""] * 81
                createIndividual(temp)
                population.append(temp)

            # runs GA maxIt number of times or until an ideal individual is found
            iteration = 0
            maxS = 0
            while iteration < maxIt and maxS < 81:
                if iteration != 0:
                    cullPopulation(population)
                fitness = fitnessPopulation(population)
                maxS = maxFitness(fitness)
                iteration += 1
            avgIt[q] += iteration
        avgIt[q]/=n

    plt.plot(xAxis, avgIt, label="Changing Population Size")
    plt.xlabel('Population Size')
    plt.ylabel('Number of Iterations to ideal')
    plt.legend()
    plt.show()

#plots the average of n runs of the max fitness and average fitness per generation
def plotAverageOfN ():
    totalAvg=[0]*maxIt
    totalMax=[0]*maxIt
    xAxis = list(range(0, maxIt))
    avgIt=0

    for j in range(n):
        #populates the 1st generation
        population = []
        for i in range(populationSize):
            temp = [""] * 81
            createIndividual(temp)
            population.append(temp)

        # runs GA maxIt number of times
        iteration=0
        maxS = 0
        while iteration<maxIt:
           if iteration!=0:
               cullPopulation(population)
           fitness=fitnessPopulation(population)
           maxS=maxFitness(fitness)

           totalMax[iteration]+=maxS
           totalAvg[iteration]+=avgFitness(fitness)
           iteration+=1
    #finds average of all the runs
    for i in range(maxIt):
        totalAvg[i]/=n
        totalMax[i]/=n
    #displays results
    plt.plot(xAxis, totalMax, label="Maximum Fitness")
    plt.plot(xAxis, totalAvg, label="Average Fitness")
    plt.xlabel('Generation')
    plt.ylabel('Fitness')
    plt.legend()
    plt.show()

populationSizeGraph()
#The following was in another .py file but is how we obtained the "ideal" for the fitness function
#
#import numpy as np
#import csv

##gets data from .csv files

#sequence_filename = "data1.csv"
#with open(sequence_filename, newline='') as f:
#    reader = csv.reader(f)
#    response = list(reader)

##creates an array to store the historic best sequence, uses a base 3 number system as indices ie: RRRR=0, RRRP=1, RRRS=2, RRPR=3...
#ideal= [""]*81

##populates all the historic values into the ideal array
#for i in response:
#    path = 0
#    pos=3
#    temp=list(i)
#    for j in temp[0]:
#        if j=="P":
#            path+= (3**pos)
#        elif j=="S":
#            path+= (3**pos)*2
#        pos-=1
#    ideal[path]+=temp[1]

##determines the best historic value (most frequent value, if none or tie then R>P>S)
#for i in range(81):
#    l=ideal[i]
#    if not l:
#        l="R"
#    else:
#        r = l.count("R")
#        p = l.count("P")
#        s = l.count("S")
#        if s>p and s>r:
#            l="S"
#        elif p>s and p>r:
#            l="P"
#        else:
#            l="R"
#    ideal[i] = l
#print(ideal)