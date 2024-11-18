import numpy as np

m8 = np.load('measure8.npy')
m5 = np.load('measure5.npy')
m2 = np.load('measure2.npy')
t8 = np.load('true8.npy')
t5 = np.load('true5.npy')
t2 = np.load('true2.npy')




g=[t2,t5,t8]
G=[m2,m5,m8]

for k in range(3):
    x = 0
    y = 0
    z = 0
    for i in range(4):
        for j in range(4):
            x+=(G[k][i][j][0]-g[k][i][j][0])**2
            y+=(G[k][i][j][1]-g[k][i][j][1])**2
            z+=(G[k][i][j][2]-g[k][i][j][2])**2

    x = np.round(np.sqrt(x/16),2)
    y = np.round(np.sqrt(y/16),2)
    z = np.round(np.sqrt(z/16),2)
    print(x,end=' ')
    print(y,end=' ')
    print(z)

    print(z+y+x)
"""
#RMSE each distance along
#x y z
48.23 64.13 53.07 #2m =165.43
12.08 15.25 17.47 #5m =44.8
4.04 4.51 8.13 #8m = 16.86

#total RMSE
28.8 38.15 32.59
=99.54



g=[t2,t5,t8]
G=[m2,m5,m8]
for k in range(3):
    x=0
    y=0
    z=0
    for i in range(4):
        for j in range(4):
            x+=(G[k][i][j][0]-g[k][i][j][0])**2
            y+=(G[k][i][j][1]-g[k][i][j][1])**2
            z+=(G[k][i][j][2]-g[k][i][j][2])**2

    x = np.round(np.sqrt(x/16),2)
    y = np.round(np.sqrt(y/16),2)
    z = np.round(np.sqrt(z/16),2)
    print(x,end=' ')
    print(y,end=' ')
    print(z)



g=[t2,t5,t8]
G=[m2,m5,m8]
for k in range(3):
    for i in range(4):
        for j in range(4):
            print(r"\multicolumn{1}{|l|}{",g[k][i][j][0],r"} & \multicolumn{1}{l|}{",g[k][i][j][1],r"} & ",g[k][i][j][2],r" & \multicolumn{1}{l|}{",G[k][i][j][0],r"} & \multicolumn{1}{l|}{",G[k][i][j][1],r"} & ",G[k][i][j][2],r" \\ \hline")


for i in range(3):
    rmse = np.sqrt(np.mean(np.square(m8-t8),i))
    print(rmse)
    print()
    
t=[
[[x0,y0,z0], [x1,y1,z1], [x2,y2,z2]]
[[x0,y0,z0], [x1,y1,z1], [x2,y2,z2]]
]



g=[t2,t5,t8]
G=[m2,m5,m8]
for k in range(3):
    for i in range(4):
        for j in range(4):
            print(r"\multicolumn{1}{|l|}{",g[k][i][j][0],r"} & \multicolumn{1}{l|}{",g[k][i][j][1],r"} & ",g[k][i][j][2],r" & \multicolumn{1}{l|}{",G[k][i][j][0],r"} & \multicolumn{1}{l|}{",G[k][i][j][1],r"} & ",G[k][i][j][2],r" \\ \hline")



g=[t2,t5,t8]
G=[m2,m5,m8]
for k in range(3):
    x=0
    y=0
    z=0
    for i in range(4):
        for j in range(4):
            x+=(G[k][i][j][0]-g[k][i][j][0])**2
            y+=(G[k][i][j][1]-g[k][i][j][1])**2
            z+=(G[k][i][j][2]-g[k][i][j][2])**2

    x = np.round(np.sqrt(x/16),2)
    y = np.round(np.sqrt(y/16),2)
    z = np.round(np.sqrt(z/16),2)
    print(x,end=' ')
    print(y,end=' ')
    print(z)


g=[t2,t5,t8]
G=[m2,m5,m8]
k=2
for i in range(4):
    for j in range(4):
        print(r"\multicolumn{1}{|l|}{",g[k][i][j][0],r"} & \multicolumn{1}{l|}{",g[k][i][j][1],r"} & ",g[k][i][j][2],r" & \multicolumn{1}{l|}{",G[k][i][j][0],r"} & \multicolumn{1}{l|}{",G[k][i][j][1],r"} & ",G[k][i][j][2],r" \\ \hline")


m8=np.round(m8,2)
m5=np.round(m5,2)
m2=np.round(m2,2)
t8=np.round(t8,2)
t5=np.round(t5,2)
t2=np.round(t2,2)
np.save('measure8.npy',m8)
np.save('measure5.npy',m5)
np.save('measure2.npy',m2)
np.save('true8.npy',t8)
np.save('true5.npy',t5)
np.save('true2.npy',t2)
"""