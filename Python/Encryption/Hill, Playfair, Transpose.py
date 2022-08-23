import numpy as np

soa=[]
playfairMatrix=[]
hillMatrix=[]
playfairSize=0
imageFiller=177
ALPHABET=['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']
modVal=26
def PlayFairFormat(string):
    temp = ""
    for i in string:
        asciiNum = ord(i)
        # replacing J or j with i
        if asciiNum == 74 or asciiNum == 106:
            temp += "i"
        # converting to lowercase and removing no alphabet characters
        if asciiNum >= 65:
            if asciiNum <= 90:
                temp += chr(asciiNum + 32)
            elif asciiNum >= 97 and asciiNum <= 122:
                temp += chr(asciiNum)
    return temp

def PlayFairPairS(string):
    i=0
    pairs=[]
    while i < len(string):
        temp=[string[i]]
        i+=1
        if i >= len(string):
            temp.append("x")
        elif temp[0]==string[i]:
            temp.append("x")
        else:
            temp.append(string[i])
            i += 1
        pairs.append(temp)
    return pairs

#Nx2 array
def PlayFairEncryptPairs(array):
    global playfairMatrix
    pos=[]
    for k in array:
        for i in k:
            j=0
            while j < playfairSize:
                if i in playfairMatrix[j]:
                    pos.append([j, playfairMatrix[j].index(i)])
                    j=playfairSize
                else:
                    j+=1
    temp=[]
    for i in range(0, len(pos), 2):
        if pos[i][0] == pos[i+1][0]:#same row
            temp.append(playfairMatrix[pos[i][0]][(pos[i][1]+1)%playfairSize])
            temp.append(playfairMatrix[pos[i+1][0]][(pos[i+1][1]+1)%playfairSize])
        elif pos[i][1] == pos[i+1][1]:#same column
            temp.append(playfairMatrix[(pos[i][0] + 1) % playfairSize][pos[i][1]])
            temp.append(playfairMatrix[(pos[i + 1][0] + 1) % playfairSize][pos[i + 1][1]])
        else:
            temp.append(playfairMatrix[pos[i][0]][pos[i + 1][1]])
            temp.append(playfairMatrix[pos[i + 1][0]][pos[i][1]])
    return temp

def PlayFairEncryptImagePairs(array):
    global playfairMatrix, imageFiller
    print("A")
    pairs = []
    for j in array:
        i = 0
        row = []
        while i < len(j):
            if j[i] == imageFiller:
                row.append(imageFiller-1)
            else:
                row.append(j[i])
            i += 1
            if i >= len(j):
                row.append(imageFiller)
            elif j[i] == row[len(row)-1]:
                row.append(imageFiller)
            else:
                if j[i] == imageFiller:
                    row.append(imageFiller-1)
                else:
                    row.append(j[i])
                i += 1
        if len(row)%2!=0:
            row.append(imageFiller)
        pairs.append(row)

    pos=[]
    for k in pairs:
        row = []
        for i in k:
            j=0
            while j < playfairSize:
                if i in playfairMatrix[j]:
                    row.append([j, playfairMatrix[j].index(i)])
                    j=playfairSize
                else:
                    j+=1
        pos.append(row)


    temp=[]
    for j in pos:
        size=len(j)
        row=[]
        for i in range(0, size, 2):
            if j[i][0] == j[i+1][0]:#same row
                row.append(playfairMatrix[j[i][0]][(j[i][1]+1)%playfairSize])
                row.append(playfairMatrix[j[i+1][0]][(j[i+1][1]+1)%playfairSize])
            elif j[i][1] == j[i+1][1]:#same column
                row.append(playfairMatrix[(j[i][0] + 1) % playfairSize][j[i][1]])
                row.append(playfairMatrix[(j[i + 1][0] + 1) % playfairSize][j[i + 1][1]])
            else:
                row.append(playfairMatrix[j[i][0]][j[i + 1][1]])
                row.append(playfairMatrix[j[i + 1][0]][j[i][1]])
        temp.append(row)

    return temp

def Playfair_Encrypt(key, plaintext):
    global playfairMatrix, playfairSize, imageFiller
    if type(plaintext) == str:#text
        plaintext=PlayFairFormat(plaintext)
        key=PlayFairFormat(key)
        pairs=PlayFairPairS(plaintext)
        playfairSize=5
        temp=[]
        for i in key:
            if i not in temp:
                temp.append(i)
        key="abcdefghiklmnopqrstuvwxyz"
        for i in key:
            if i not in temp:
                temp.append(i)
        playfairMatrix = []
        for i in range(playfairSize):
            row=[]
            for j in range(playfairSize):
                row.append(temp[i*5+j])
            playfairMatrix.append(row)

        return np.array(PlayFairEncryptPairs(pairs))

    else:#image
        playfairSize = 16
        temp = []
        key=PlayFairFormat(key)
        for i in key:
            if ord(i)-65-32 not in temp:
                temp.append(ord(i)-65-32)# A=0 ... a=32
        for i in range(256):
            if i not in temp:
                temp.append(i)
        playfairMatrix = []

        for i in range(playfairSize):
            row=[]
            for j in range(playfairSize):
                row.append(temp[i*playfairSize+j])
            playfairMatrix.append(row)
        rt=[]
        gt=[]
        bt=[]
        for i in plaintext:
            rTemp=[]
            gTemp=[]
            bTemp=[]
            for j in i:
                rTemp.append(j[0])
                gTemp.append(j[1])
                bTemp.append(j[2])
            rt.append(rTemp)
            gt.append(gTemp)
            bt.append(bTemp)

        temp=[]

        r = PlayFairEncryptImagePairs(rt)
        g = PlayFairEncryptImagePairs(gt)
        b = PlayFairEncryptImagePairs(bt)

        length=0
        for i in range(len(r)):
            if len(r[i])>length:
                length=len(r[i])

            if len(g[i])>length:
                length=len(g[i])

            if len(b[i])>length:
                length=len(b[i])

        for i in range(len(r)):
            while len(r[i])<length:
                r[i].append(imageFiller)
            while len(g[i])<length:
                g[i].append(imageFiller)
            while len(b[i])<length:
                b[i].append(imageFiller)


        for i in range(len(r)):
            row=[]
            for j in range(length):
                row.append([r[i][j], g[i][j], b[i][j]])
            temp.append(row)

        return np.array(temp)

def PlayFairDecryptPairs(array):
    global playfairMatrix

    pos=[]
    for k in array:
        for i in k:
            j=0
            while j < playfairSize:
                if i in playfairMatrix[j]:
                    pos.append([j, playfairMatrix[j].index(i)])
                    j=playfairSize
                else:
                    j+=1
    temp=[]
    for i in range(0, len(pos), 2):
        if pos[i][0] == pos[i+1][0]:#same row
            temp.append(playfairMatrix[pos[i][0]][(pos[i][1]-1)%playfairSize])
            temp.append(playfairMatrix[pos[i+1][0]][(pos[i+1][1]-1)%playfairSize])
        elif pos[i][1] == pos[i+1][1]:#same column
            temp.append(playfairMatrix[(pos[i][0] - 1) % playfairSize][pos[i][1]])
            temp.append(playfairMatrix[(pos[i + 1][0] - 1) % playfairSize][pos[i + 1][1]])
        else:
            temp.append(playfairMatrix[pos[i][0]][pos[i + 1][1]])
            temp.append(playfairMatrix[pos[i + 1][0]][pos[i][1]])
    return temp

def PlayFairDecryptImage(array):
    global playfairMatrix

    pos=[]
    for i in array:
        j=0
        while j < playfairSize:
            if i in playfairMatrix[j]:
                pos.append([j, playfairMatrix[j].index(i)])
                j=playfairSize
            else:
                j+=1
    temp=[]
    for i in range(0, len(pos), 2):
        if i+1==len(pos):
            temp.append(playfairMatrix[pos[i][0]][(pos[i][1])])
        if pos[i][0] == pos[i + 1][0]:  # same row
            if pos[i][1] != pos[i + 1][1]:
                temp.append(playfairMatrix[pos[i][0]][(pos[i][1] - 1) % playfairSize])
                temp.append(playfairMatrix[pos[i + 1][0]][(pos[i + 1][1] - 1) % playfairSize])
            elif playfairMatrix[pos[i][0]][pos[i][1]]!=imageFiller:
                temp.append(playfairMatrix[pos[i][0]][pos[i][1]])
                temp.append(playfairMatrix[pos[i][0]][pos[i][1]])
        elif pos[i][1] == pos[i + 1][1]:  # same column
            temp.append(playfairMatrix[(pos[i][0] - 1) % playfairSize][pos[i][1]])
            temp.append(playfairMatrix[(pos[i + 1][0] - 1) % playfairSize][pos[i + 1][1]])
        else:
            temp.append(playfairMatrix[pos[i][0]][pos[i + 1][1]])
            temp.append(playfairMatrix[pos[i + 1][0]][pos[i][1]])
    return temp


def Playfair_Decrypt(key, ciphertext):
    global playfairMatrix, playfairSize, imageFiller

    ciphertext=np.array(ciphertext)

    if type(ciphertext[0]) == np.str_:#text
        for i in range(playfairSize):
            row = []
            for j in range(playfairSize):
                row.append(temp[i * 5 + j])
            playfairMatrix.append(row)


        temp=PlayFairDecryptPairs(ciphertext)
        text=""
        for i in temp:
            if i!='x':
                text+=i

        return text
    else:#image
        r = []
        g = []
        b = []
        for i in ciphertext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                rTemp.append(j[0])
                gTemp.append(j[1])
                bTemp.append(j[2])
            r.append(rTemp)
            g.append(gTemp)
            b.append(bTemp)

    dR = []
    dG = []
    dB = []
    for i in r:
        temp=PlayFairDecryptImage(i)
        row=[]
        for j in temp:
            if j!=imageFiller:
                row.append(j)
        dR.append(row)

    for i in g:
        temp = PlayFairDecryptImage(i)
        row = []
        for j in temp:
            if j != imageFiller:
                row.append(j)
        dG.append(row)

    for i in b:
        temp = PlayFairDecryptImage(i)
        row = []
        for j in temp:
            if j != imageFiller:
                row.append(j)
        dB.append(row)

    length=0
    for i in range(len(dR)):
        if len(dR[i])>length:
            length=len(dR[i])

        if len(dG[i])>length:
            length= len(dG[i])

        if len(dB[i])>length:
            length=len(dB[i])

    for i in range(len(dR)):
        j=len(dR[i])
        while j<length:
            dR[i].append(imageFiller)
            j+=1
        j=len(dG[i])
        while j<length:
            dG[i].append(imageFiller)
            j+=1

        j=len(dB[i])
        while j<length:
            dB[i].append(imageFiller)
            j+=1


    temp = []
    for i in range(len(dR)):
        row = []
        for j in range(length):
            row.append([dR[i][j], dG[i][j], dB[i][j]])
        temp.append(row)


    return np.array(temp)

def Get_Playfair_Encryption_Matrix():
    return playfairMatrix

def getInverse():
    global hillMatrix, modVal

    if 2 == len(hillMatrix):
        inverse = [[hillMatrix[1][1], -hillMatrix[0][1]], [-hillMatrix[1][0], hillMatrix[0][0]]]
    else:
        inverse=[[0,0,0],[0,0,0],[0,0,0]]
        for i in range(len(hillMatrix)):
            for j in range(len(hillMatrix)):
                inverse[i][j] = getMinor(i, j)

    return (np.array(transpose(inverse))*modInverse(getDet()))%modVal

def transpose(mat):
    if len(mat)==2:
        trans = [[0,0], [0, 0]]
        for i in range(2):
            for j in range(2):
                trans[j][i] = mat[i][j]
    else:
        trans = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
        for i in range(3):
            for j in range(3):
                trans[j][i]=mat[i][j]
    return trans
def getMinor(k,l):
    global hillMatrix
    minor = []

    for i in range(len(hillMatrix)):
        if i!=k:
            temp=[]
            for j in range(len(hillMatrix)):
                if j!=l:
                    temp.append(hillMatrix[i][j])
            minor.append(temp)

    temp=minor[0][0]*minor[1][1]-minor[0][1]*minor[1][0]
    if k%2!=l%2:
        temp*=-1
    return temp


def modInverse(num):#https://www.geeksforgeeks.org/multiplicative-inverse-under-modulo-m/#:~:text=The%20modular%20multiplicative%20inverse%20is%20an%20integer%20'x'%20such%20that.&text=The%20multiplicative%20inverse%20of%20%E2%80%9Ca,of%203(under%2011).
    global modVal
    for i in range(1, modVal):
        if ((num % modVal) * (i % modVal)) % modVal == 1:
            return i
    return -1

def getDet():
    if len(hillMatrix)==3:
        det=hillMatrix[0][0]*getMinor(0,0)
        det+=hillMatrix[0][1]*getMinor(0,1)
        det+=hillMatrix[0][2]*getMinor(0,2)
    else:
        det=hillMatrix[0][0]*hillMatrix[1][1]-hillMatrix[1][0]*hillMatrix[0][1]
    return det

def Hill_Encrypt(key:str, plaintext):
    global hillMatrix, ALPHABET,imageFiller, modVal
    hillMatrix=[]
    key=PlayFairFormat(key)

    if len(key)==4:
        for i in range(2):
            temp = []
            for j in range(2):
                temp.append(ord(key[i * 2 + j])-97)
            hillMatrix.append(temp)
    elif len(key)==9:
        for i in range(3):
            temp=[]
            for j in range(3):
                temp.append(ord(key[i*3+j])-97)
            hillMatrix.append(temp)
    else:
        return "Key Error"

    if getDet()==0:
        return "Key Error"

    size = len(hillMatrix)

    if type(plaintext) == str:  # text
        modVal=26
        plaintext=PlayFairFormat(plaintext)
        while len(plaintext)%size!=0:
            plaintext+='x'

        encoded=""
        if size==2:
            for i in range(0, len(plaintext), 2):
                p=[ALPHABET.index(plaintext[i]),ALPHABET.index(plaintext[i+1])]
                c=matmul(hillMatrix,p)
                encoded+=ALPHABET[c[0]%modVal]
                encoded+=ALPHABET[c[1]%modVal]
        else:
            for i in range(0, len(plaintext), 3):
                p=[ALPHABET.index(plaintext[i]),ALPHABET.index(plaintext[i+1]),ALPHABET.index(plaintext[i+2])]
                c=matmul(p,hillMatrix)
                encoded+=ALPHABET[c[0]%modVal]
                encoded+=ALPHABET[c[1]%modVal]
                encoded+=ALPHABET[c[2]%modVal]
        return encoded
    else: # image
        modVal=256
        r = []
        g = []
        b = []
        for i in plaintext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                if j[0]==imageFiller:
                    rTemp.append(imageFiller-1)
                else:
                    rTemp.append(j[0])

                if j[1]==imageFiller:
                    gTemp.append(imageFiller-1)
                else:
                    gTemp.append(j[1])

                if j[2]==imageFiller:
                    bTemp.append(imageFiller-1)
                else:
                    bTemp.append(j[2])

            while len(rTemp)%size!=0:
                rTemp.append(imageFiller)
            r.append(rTemp)

            while len(gTemp)%size!=0:
                gTemp.append(imageFiller)
            g.append(gTemp)

            while len(bTemp)%size!=0:
                bTemp.append(imageFiller)
            b.append(bTemp)

        length = 0
        for i in range(len(r)):
            if len(r[i]) > length:
                length = len(r[i])

            if len(g[i]) > length:
                length = len(g[i])

            if len(b[i]) > length:
                length = len(b[i])

        for i in range(len(r)):
            while len(r[i]) < length:
                r[i].append(imageFiller)
            while len(g[i]) < length:
                g[i].append(imageFiller)
            while len(b[i]) < length:
                b[i].append(imageFiller)

        er = []
        eg = []
        eb = []

        if size == 2:
            for k in r:
                row=[]
                for i in range(0, len(k), 2):
                    p = [k[i], k[i + 1]]
                    c = matmul(p, hillMatrix)
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                er.append(row)

            for k in g:
                row = []
                for i in range(0, len(k), 2):
                    p = [k[i], k[i + 1]]
                    c = matmul(p, hillMatrix)
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                eg.append(row)

            for k in b:
                row = []
                for i in range(0, len(k), 2):
                    p = [k[i], k[i + 1]]
                    c = matmul(p, hillMatrix)
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                eb.append(row)

        else:
            for k in r:
                row = []
                for i in range(0, len(k), 3):
                    p = [k[i], k[i + 1], k[i+2]]
                    c = matmul(p, hillMatrix)
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                    row.append(c[2] % modVal)
                er.append(row)

            for k in g:
                row = []
                for i in range(0, len(k), 3):
                    p = [k[i], k[i + 1], k[i+2]]
                    c = matmul(p, hillMatrix)
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                    row.append(c[2] % modVal)
                eg.append(row)

            for k in b:
                row = []
                for i in range(0, len(k), 3):
                    p = [k[i], k[i + 1], k[i+2]]
                    c = matmul(p, hillMatrix)
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                    row.append(c[2] % modVal)
                eb.append(row)

        temp=[]
        for i in range(len(er)):
            row=[]
            for j in range(len(er[i])):
                row.append([er[i][j],eg[i][j],eb[i][j]])
            temp.append(row)
        return np.array(temp)


def Hill_Decrypt(key, ciphertext):
    global hillMatrix, modVal

    inverse = getInverse()
    size = len(hillMatrix)
    if type(ciphertext) == str:  # text
        decoded = ""
        if size == 2:
            for i in range(0, len(ciphertext), 2):
                p = [ALPHABET.index(ciphertext[i]), ALPHABET.index(ciphertext[i + 1])]
                c = np.round(matmul(p, inverse))
                decoded += ALPHABET[c[0] % modVal]
                decoded += ALPHABET[c[1] % modVal]
        else:
            for i in range(0, len(ciphertext), 3):
                p = [ALPHABET.index(ciphertext[i]), ALPHABET.index(ciphertext[i + 1]), ALPHABET.index(ciphertext[i + 2])]
                c = np.round(matmul(p,inverse))
                decoded += ALPHABET[int(c[0] % modVal)]
                decoded += ALPHABET[int(c[1] % modVal)]
                decoded += ALPHABET[int(c[2] % modVal)]
        temp=""
        for i in decoded:
            if i != "x":
                temp+=i
        return temp

    else:  # image
        r = []
        g = []
        b = []
        for i in ciphertext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                rTemp.append(j[0])
                gTemp.append(j[1])
                bTemp.append(j[2])

            r.append(rTemp)
            g.append(gTemp)
            b.append(bTemp)
        dr = []
        dg = []
        db = []
        if size == 2:
            for k in r:
                row=[]
                for i in range(0, len(k), 2):
                    p = [k[i], k[i + 1]]
                    c = np.round(matmul(inverse,p))
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                dr.append(row)

            for k in g:
                row=[]
                for i in range(0, len(k), 2):
                    p = [k[i], k[i + 1]]
                    c = np.round(matmul(inverse,p))
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                dg.append(row)

            for k in b:
                row=[]
                for i in range(0, len(k), 2):
                    p = [k[i], k[i + 1]]
                    c = np.round(matmul(inverse,p))
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                db.append(row)
        else:
            for k in r:
                row = []
                for i in range(0, len(k), 3):
                    p = [k[i], k[i + 1], k[i + 2]]
                    c = np.round(matmul(p, inverse))
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                    row.append(c[2] % modVal)
                dr.append(row)

            for k in g:
                row = []
                for i in range(0, len(k), 3):
                    p = [k[i], k[i + 1], k[i + 2]]
                    c = np.round(matmul(p, inverse))
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                    row.append(c[2] % modVal)
                dg.append(row)

            for k in b:
                row = []
                for i in range(0, len(k), 3):
                    p = [k[i], k[i + 1], k[i + 2]]
                    c = np.round(matmul(p, inverse))
                    row.append(c[0] % modVal)
                    row.append(c[1] % modVal)
                    row.append(c[2] % modVal)
                db.append(row)
    r=[]
    g=[]
    b=[]
    for i in dr:
        row=[]
        for j in i:
            if j!=imageFiller:
                row.append(j)
        r.append(row)
    for i in dg:
        row=[]
        for j in i:
            if j!=imageFiller:
                row.append(j)
        g.append(row)
    for i in db:
        row=[]
        for j in i:
            if j!=imageFiller:
                row.append(j)
        b.append(row)
    temp = []
    for i in range(len(r)):
        row = []
        for j in range(len(r[i])):
            row.append([r[i][j], g[i][j], b[i][j]])
        temp.append(row)
    return np.array(temp)

def Get_Hill_Encryption_Matrix():
    global hillMatrix
    return hillMatrix

def Transpose_Encrypt(key, stage, plaintext):
    key=PlayFairFormat(key)
    cols=[]
    for i in key:
        if (ord(i) - 97) not in cols:
            cols.append(ord(i)-97)
    for i in range(26):
        if i not in cols:
            cols.append(i)

    while(len(plaintext)%26!=0):
        plaintext+="x"

    for k in range(stage):
        text=[]
        for i in range(26):
            text.append("")

        for i in range(len(plaintext)):
            text[i%26]+=plaintext[i]
        plaintext=""
        for i in cols:
            plaintext+=text[int(i)]
    return plaintext

def Transpose_Decrypt (key, stage, ciphertext):
    key = PlayFairFormat(key)
    cols = []

    for i in key:
        if (ord(i) - 97) not in cols:
            cols.append(ord(i) - 97)
    for i in range(26):
        if i not in cols:
            cols.append(i)
    rows=len(ciphertext)//26

    for k in range(stage):
        text = []
        for i in range(26):
            text.append("")
        pos=0
        for i in range(0,len(ciphertext),rows):
            for j in range(rows):
                text[pos] += ciphertext[i+j]
            pos+=1
        ciphertext = ""

        for k in range(rows):
            for i in range(26):
                ciphertext+= text[cols.index(i)][k]
    temp=""
    for i in ciphertext:
        if i !='x':
            temp+=i
    return np.array(temp)

def matmul(a,b):
    global soa
    at= str(np.shape(a))
    at+=" "
    at+= str(np.shape(b))
    if at not in soa:
        soa.append(at)
    return np.matmul(a,b)

def getSoa():
    global soa
    return soa