import numpy as np



P0=np.array([
[-6.581485088076711, 1806.8293357679772, 2100.2890797416917, 72316.99672176925],
[-2569.6605069905436, -24.396601153283115, 95.49160559699268, 106578.80626977581],
[-0.1209592120039773, -0.6409430391907733, 0.7579979482454235, 429.0657642419051]]
)
P1=np.array([
[69.68334906215955, 1160.220867826899, 307.51289834949034, 199085.59026251244],
[-1066.5587519122653, 286.60012279388735, -62.14292061195317, 100971.8037570991],
[-0.33365856919422576, 0.3124859329855524, 0.8893955817797901, 197.71739156031114]]
)
EPSILON = 0.01

def DLT(point1, point2):
    global P0, P1
    A = [point1[1]*P0[2,:] - P0[1,:],
         P0[0,:] - point1[0]*P0[2,:],
         point2[1]*P1[2,:] - P1[1,:],
         P1[0,:] - point2[0]*P1[2,:]
        ]

    A = np.array(A).reshape((4,4))

    B = A.transpose() @ A
    U, s, Vh = np.linalg.svd(B, full_matrices = False)

    return Vh[3,0:3]/Vh[3,3]


def IterativeLinearLSTriangulation(u, u1):
    #https://www.morethantechnical.com/blog/2012/01/04/simple-triangulation-with-opencv-from-harley-zisserman-w-code/
    #page 129
    global P0, P1, EPSILON
    wi = 1
    wi1 = 1
    X = np.zeros([4,1])
    for i in range(10):
        X_ = DLT(u,u1)
        X[0] = X_[0]
        X[1] = X_[1]
        X[2] = X_[2]
        X[3] = 1.0

        p2x = float((P0[2]@X)[0])
        p2x1 = float((P1[2]@X)[0])
        if abs(wi - p2x) <= EPSILON and abs(wi1 - p2x1) <= EPSILON:
            return X[0], X[1], X[2]
        wi = p2x;
        wi1 = p2x1;

        A = [
            (u[1] * P0[2, :] - P0[1, :])/wi,
            (P0[0, :] - u[0] * P0[2, :])/wi,
            (u1[1] * P1[2, :] - P1[1, :])/wi1,
            (P1[0, :] - u1[0] * P1[2, :]/wi1)
             ]
        A = np.array(A).reshape((4, 4))

        B = A.transpose() @ A
        U, s, Vh = np.linalg.svd(B, full_matrices=False)

        X_ = Vh[3, 0:3] / Vh[3, 3]
        X[0] = X_[0]
        X[1] = X_[1]
        X[2] = X_[2]
        X[3] = 1.0
    return X[0], X[1], X[2]


p0=[
    [652,344],
    [674,344],
    [649,411],
    [671,412]
]

p1=[
    [239,213],
    [264,215],
    [236,287],
    [261,290]
]

X=[]
Y=[]
Z=[]

for i in range(4):
    x,y,z = IterativeLinearLSTriangulation(p0[i], p1[i])

    X.append(y)
    Y.append(x)
    Z.append(z)
print(np.min(X), np.max(X))
print(np.min(Y), np.max(Y))
print(np.min(Z), np.max(Z))

"""
def collison(state):
    Xsmall = 0
    Xbig = 2
    Ysmall = 0
    Ybig = 2
    Zsmall = 0
    Zbig = 2


    tx0 = (Xsmall - state[0][0]) / state[3][0]
    tx1 = (Xbig - state[0][0]) / state[3][0]
    if tx0 < 0 and tx1 < 0:
        return False
    if tx0 > tx1:
        temp = tx0
        tx0 = tx1
        tx1=temp

    ty0 = (Ysmall - state[1][0]) / state[4][0]
    ty1 = (Ybig - state[1][0]) / state[4][0]
    if ty0 < 0 and ty1 < 0:
        return False
    if ty0 > ty1:
        temp = ty0
        ty0 = ty1
        ty1=temp

    tz0 = (Zsmall - state[2][0])/state[5][0]
    tz1 = (Zbig - state[2][0])/state[5][0]
    if tz0<0 and tz1<0:
        return False
    if tz0 > tz1:
        temp = tz0
        tz0 = tz1
        tz1=temp

    tmin = np.max([tx0, ty0, tz0])
    tmax = np.min([tx1, ty1, tz1])
    if tmin>tmax:
        return False
    return True


state=[
    [1],
    [0],
    [0],
    [0.00000000000001],
    [1],
    [1]
]
print(collison(state))

ty0=0
tz0=0
ty1=0
tz1=0

def collison(state, bounds):
    #if the ball is stationery on that plane is it already within the bounds
    if state[3][0] == 0:
        if bounds[0][0]<=state[0][0]<=bounds[0][1]:
            tx0 = 0
            tx1 = 1000
        else:
            return False
    else:
        #calculate the range of time for the ball to be within the bounds
        tx0 = (bounds[0][0] - state[0][0]) / state[3][0]
        tx1 = (bounds[0][1] - state[0][0]) / state[3][0]
        #if both times are negative then the ball will never be within the bounds
        if tx0 < 0 and tx1 < 0:
            return False
        #ensures that tx1 contains the bigger time value between tx1 and tx0
        if tx0 > tx1:
            temp = tx0
            tx0 = tx1
            tx1=temp
    #repeat the above for y and z
    #find the maximum of the smaller time intervals
    tmin = np.max([tx0, ty0, tz0])
    #find the minimum of the larger time intervals
    tmax = np.min([tx1, ty1, tz1])
    #if the maximum of the smaller time intervals is greater than the
    #minimum of the larger time intervals, the ball will not hit the wickets
    if tmin>tmax:
        return False
    return True







dropCoef = [
    [2.38651271, 0.36295948],
    [2.37779336, 0.31742948],
    [2.04708888, 0.20940562],
    [1.1772782, 0.20323063],
    [1.27865121,  0.1900368],
    [1.22228432, 0.2185388],
    [1.0490233, 0.15513264]
]
e=[]
for i in dropCoef:
   e.append(np.sqrt(i[1]/i[0]))
print(e)
print(np.mean(e))
"""