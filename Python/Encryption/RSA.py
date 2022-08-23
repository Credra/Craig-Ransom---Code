import numpy as np
def gcd(a,h):
    while True:
        temp=a%h
        if temp==0:
            return h
        a=h
        h=temp

def RSA(k,e,n):
    x=int(np.floor(np.log2(n)/4))

    while len(k)%(x)!=0:
        k='0'+k

    key=''
    for i in range(int(len(k)/x)):
        str=''
        for j in range(x):
            str+=k[i*x+j]
        temp=int(str,16)

        temp=hex(pow(temp,e,n))[2:]
        while len(temp)<x+1:
            temp='0'+temp
        key+=temp
    #key=hex(int(key,16))[2:]
    return key

def RSAde(k,e,n):
    x=int(np.floor(np.log2(n)/4))+1
    key=''
    for i in range(int(len(k)/x)):
        str=''
        for j in range(x):
            str+=k[i*x+j]
        temp=int(str,16)

        temp=hex(pow(temp,e,n))[2:]
        while len(temp)<x-1:
            temp='0'+temp
        key+=temp
    key=hex(int(key,16))[2:]
    return key

def start():
    p=1433129909
    q=4026300193
    #q=31
    #p=17

    n=q*p

    print(n)

    e=2
    phi = (p-1)*(q-1)
    while e<phi:
        if gcd(e,phi)==1:
            d = int((1 + (2*phi))/e)
            if 2 ==(pow(pow(2, e, n), d, n)):
                break
        e+=1

    print('e',e)
    print('d',d)
    print('phi',phi)

n=5770211229200772437
pub=29941
pr=385438777845853
k="93365aaa9554aaad556aab52d29696b4b4b5a5ad2d2969694b4a5a5a52d2d696b4b4b5a5a52d2d69694b4b4a5a5ad2d69696b4b4a5a52dffff803fffc01ffff8003fffc001fffe007fffc01ffff00ffff8003ffff00ffff807fffb24db24db249b649b649b649b649364936c9b6c936c936c936d936d926d926d926d926db26db24db24db24c3ef83e0f07c1f7c3e0f83ef7c1f07c3ef83e0f07c1f7c3e0f83e1f7c1f0783ef83e1f07c1fccc9999999b333333366666666ccccccc99999999333333366666666cccccccd9999999333333326666666ccccb5a52d694b5ad2d6b4a5a5296b4a5ad296b4a5ad296b4a5ad296b"

#k='F013e88a837b6d5'

print(k)
s=RSA(k,pub,n)
print(RSAde(s,pr,n))
