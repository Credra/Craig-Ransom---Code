class node (object) :   #Node class used to implement the RPS tree
    def __init__ (self , object , children = [ ], visted=False) :
        self.object = object #Value of the node, either "R","P" or "S"
        self.children = children #The children of the node
        self.visted = visted #A boolean variable used in the DFS and BFS

def populateNode(child): #adds 3 children to the node
    child.children=[node("R"),node("P"),node("S")]

def populateTree(tree, depth):#recursivly populates the tree, giving each node 3 children except for the leaf nodes
    if depth>0:
        depth-=1
        populateNode(tree)
        for i in tree.children:
            populateTree(i,depth)

def dfsrec(root):#Recursive Depth First Search that returns the path of the nodes to the current node
    sequence=""
    if root.children:#if the node is not a leaf node
        for i in root.children:#for each child the node has
            if i.visted==False:#if the node leads to unvisited leaf nodes
                sequence+=i.object#adds the value of the current node to sequence
                sequence+=dfsrec(i)#recursively calls this function on the children of node

                if i.object=="S":#checks if the entire tree that leads on from this node has been traversed
                    done = True
                    for j in i.children:
                        if done:
                            done = j.visted
                    if done:
                        root.visted=True
                return sequence
        root.visted = True
        return sequence
    else:#the node is a leaf node
        root.visted=True
        return sequence

def beatPrev(prev):
    if prev == "R":
        output = "P"
    elif prev == "P":
        output = "S"
    else:
        output = "R"
    return output

def bfsrec(root, prefix,sequence):
    for i in root.children: #for each child of the node
        if i.visted==False: #if the child's has not been added to sequence
            sequence.append(prefix+i.object)#adds path to sequence
            i.visted=True
        else:
            bfsrec(i, prefix + i.object,sequence)#repeats process on one layer lower

bfs_dfs = 1 #choose between bfs and dfs search
if input == "": #initialize variables
    output = "R"
    current = []
    fullSeq = []
    seqIndex = 0

    tree = node("temp")
    depth = 5
    populateTree(tree, depth) #initialize tree
    found = False
    prev = ""


else: #normal playing

    if not found: #check that we are in a normal state and not the found state
        if input==prev:
            #add check for length for bfs
            found = True #temp found variable during initial break
            output = beatPrev(input)
            seqIndex = 0
        else:

            if len(current) == 0:
                temp=""
                if (bfs_dfs == 0):
                    temp = bfsrec(tree, "", [])
                else:
                    temp = dfsrec(tree)
                current=list(temp)
                prevSeq = list(fullSeq)
                fullSeq = list(temp)   # store the full current sequence so we can pop current and still have full seq
                seqIndex = 0
            output=current.pop(0)
            prev = input

    else:
        if input == prev: #while bot is broken, simply beat previous move
            output = beatPrev(input)
            seqIndex = 0

        else: #bot is no longer
            if seqIndex == len(fullSeq):
                seqIndex = 0
                output = beatPrev(input)
                if(fullSeq == prevSeq):
                    found = False
                else:
                    fullSeq = list(prevSeq)
            else:
                output = fullSeq[seqIndex]
                seqIndex += 1
            prev = input