# converts the character representing rock, paper or scissors into the corresponding digit
def convert(t):
    if t == "R":
        return 0
    if t == "P":
        return 1
    return 2

# returns what should be played in order to beat in input
def beat(t):
    if t=="R":
        return "P"
    if t=="P":
        return "S"
    return "R"

if input == "":
    # total number of rounds
    # starts with a value of 9 to ensure each of the conditional probabilities are not 0
    n = 9

    # 2d array representing the probabilities of the opponent's move 1 turn ago
    # i1[input][output] where input is what was played and output is what would have won
    i1 = [[1,1,1],
          [1,1,1],
          [1,1,1]]

    # 2d array representing probabilities of the this agent's move 1 turn ago
    # i2[input][output] where input is what was played and output is what would have won
    i2 = [[1,1,1],
          [1,1,1],
          [1,1,1]]

    # 2d array representing probabilities of the opponent's move 2 turns ago
    # i3[input][output] where input is what was played and output is what would have won
    i3 = [[1,1,1],
          [1,1,1],
          [1,1,1]]

    # 2d array representing probabilities of this agent's move 1 turn ago
    # i4[input][output] where input is what was played and output is what would have won
    i4 = [[1,1,1],
          [1,1,1],
          [1,1,1]]

    # the number of times Rock would have won
    totalRock=3

    # the number of times Paper would have won
    totalPaper=3

    # the number of times Scissors would have won
    totalScissors=3


    # histories assumed to be rock at the start of game
    # history of them 1 move ago
    y1 = "R"

    # history of  us 1 move ago
    x1 = "R"

    # history of  them 2 moves ago
    y2 = "R"

    # history of  us 2 moves ago
    x2 = "R"

    # converts histories into indexes and stores in an array
    evidence = [0, 0, 0, 0]
    evidence[0] = convert(y1)
    evidence[1] = convert(x1)
    evidence[2] = convert(y2)
    evidence[3] = convert(x2)

else:
    # updating histories
    y2 = y1
    x2 = x1
    y1 = input
    x1 = last


    #updates probabillites according to previous rounds results
    win = beat(y1)
    index = convert(win)
    if win == "R":
        totalRock += 1
    elif win == "P":
        totalPaper += 1
    else:
        totalScissors += 1

    n += 1

    i1[evidence[0]][index] += 1
    i2[evidence[1]][index] += 1
    i3[evidence[2]][index] += 1
    i4[evidence[3]][index] += 1

    # converts histories into indexes and stores in an array
    evidence[0] = convert(y1)
    evidence[1] = convert(x1)
    evidence[2] = convert(y2)
    evidence[3] = convert(x2)

# determines the probability that rock will win given the history
# probability of rock
pRock = totalRock / n
# probability of rock given histories
pRock *= i1[evidence[0]][0] / (i1[0][0] + i1[1][0] + i1[2][0])
pRock *= i2[evidence[1]][0] / (i2[0][0] + i2[1][0] + i2[2][0])
pRock *= i3[evidence[2]][0] / (i3[0][0] + i3[1][0] + i3[2][0])
pRock *= i4[evidence[3]][0] / (i4[0][0] + i4[1][0] + i4[2][0])

# determines the probability that paper will win given the history
# probability of paper
pPaper = totalPaper / n
# probability of paper given histories
pPaper *= i1[evidence[0]][1] / (i1[0][1] + i1[1][1] + i1[2][1])
pPaper *= i2[evidence[1]][1] / (i2[0][1] + i2[1][1] + i2[2][1])
pPaper *= i3[evidence[2]][1] / (i3[0][1] + i3[1][1] + i3[2][1])
pPaper *= i4[evidence[3]][1] / (i4[0][1] + i4[1][1] + i4[2][1])

# determines the probability that scissors will win given the history
# probability of scissors
pScissors = totalScissors / n
# probability of scissors given histories
pScissors *= i1[evidence[0]][2] / (i1[0][2] + i1[1][2] + i1[2][2])
pScissors *= i2[evidence[1]][2] / (i2[0][2] + i2[1][2] + i2[2][2])
pScissors *= i3[evidence[2]][2] / (i3[0][2] + i3[1][2] + i3[2][2])
pScissors *= i4[evidence[3]][2] / (i4[0][2] + i4[1][2] + i4[2][2])

# assigns output based on highest probability
if pRock > pPaper:
    if pRock > pScissors:
        output = "R"
    else:
        output = "S"
else:
    if pPaper > pScissors:
        output = "P"
    else:
        output = "S"

last = output