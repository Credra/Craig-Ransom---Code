import numpy as np
from PIL import Image
from datetime import datetime

# global variables
key = 'asd'
bbs=0
bbsN=0

def initRandom():
    global bbs, bbsN
    p = 1223
    q = 1187
    bbsN = p * q
    s = datetime.now().time().microsecond
    while s % p == 0 or s % q == 0:
        s = (s + 1) % bbsN
    bbs = s ** 2 % bbsN

def getRandomBit():
    global bbs, bbsN
    bbs = bbs**2%bbsN
    return str(bbs%2)

def rightRotate(x, r):
    temp = len(x)-r
    return x[temp:] + x[:temp]

def leftRotate(x,l):
    temp = len(x)
    return x[l: temp] + x[0:l]

def rightShift(x, r):
    temp=''
    for i in range(r):
        temp += '0'
    temp += x[0:len(x)-r]
    return temp

def leftShift(x,l):
    temp=x[l:len(x)]
    for i in range(l):
        temp+='0'
    return temp

def xor(a,b):
    temp = bin(int(a,2)^int(b,2))[2:]
    while len(temp) < len(a):
        temp = '0' + temp
    return temp

def binAnd(a,b):
    temp = bin(int(a, 2) & int(b, 2))[2:]
    while len(temp) < len(a):
        temp = '0' + temp
    return temp

def binInv(a):
    temp = ''
    for i in a:
        if i =='0':
            temp+='1'
        else:
            temp+='0'
    return temp

def binPlus(a,b, c='0', d='0', e='0'):
    temp = int(a,2)+int(b,2)+int(c,2)+int(d,2)+int(e,2)
    temp=temp%2**64
    temp=bin(temp)[2:]
    while len(temp)<64:
        temp = '0' + temp
    return temp

def hash(message):#message in hex
    h0 = '0110101000001001111001100110011111110011101111001100100100001000'
    h1 = '1011101101100111101011101000010110000100110010101010011100111011'
    h2 = '0011110001101110111100110111001011111110100101001111100000101011'
    h3 = '1010010101001111111101010011101001011111000111010011011011110001'
    h4 = '0101000100001110010100100111111110101101111001101000001011010001'
    h5 = '1001101100000101011010001000110000101011001111100110110000011111'
    h6 = '0001111110000011110110011010101111111011010000011011110101101011'
    h7 = '0101101111100000110011010001100100010011011111100010000101111001'
    k = ['0100001010001010001011111001100011010111001010001010111000100010',
         '0111000100110111010001001001000100100011111011110110010111001101',
         '1011010111000000111110111100111111101100010011010011101100101111',
         '1110100110110101110110111010010110000001100010011101101110111100',
         '0011100101010110110000100101101111110011010010001011010100111000',
         '0101100111110001000100011111000110110110000001011101000000011001',
         '1001001000111111100000101010010010101111000110010100111110011011',
         '1010101100011100010111101101010111011010011011011000000100011000',
         '1101100000000111101010101001100010100011000000110000001001000010',
         '0001001010000011010110110000000101000101011100000110111110111110',
         '0010010000110001100001011011111001001110111001001011001010001100',
         '0101010100001100011111011100001111010101111111111011010011100010',
         '0111001010111110010111010111010011110010011110111000100101101111',
         '1000000011011110101100011111111000111011000101101001011010110001',
         '1001101111011100000001101010011100100101110001110001001000110101',
         '1100000110011011111100010111010011001111011010010010011010010100',
         '1110010010011011011010011100000110011110111100010100101011010010',
         '1110111110111110010001111000011000111000010011110010010111100011',
         '0000111111000001100111011100011010001011100011001101010110110101',
         '0010010000001100101000011100110001110111101011001001110001100101',
         '0010110111101001001011000110111101011001001010110000001001110101',
         '0100101001110100100001001010101001101110101001101110010010000011',
         '0101110010110000101010011101110010111101010000011111101111010100',
         '0111011011111001100010001101101010000011000100010101001110110101',
         '1001100000111110010100010101001011101110011001101101111110101011',
         '1010100000110001110001100110110100101101101101000011001000010000',
         '1011000000000011001001111100100010011000111110110010000100111111',
         '1011111101011001011111111100011110111110111011110000111011100100',
         '1100011011100000000010111111001100111101101010001000111111000010',
         '1101010110100111100100010100011110010011000010101010011100100101',
         '0000011011001010011000110101000111100000000000111000001001101111',
         '0001010000101001001010010110011100001010000011100110111001110000',
         '0010011110110111000010101000010101000110110100100010111111111100',
         '0010111000011011001000010011100001011100001001101100100100100110',
         '0100110100101100011011011111110001011010110001000010101011101101',
         '0101001100111000000011010001001110011101100101011011001111011111',
         '0110010100001010011100110101010010001011101011110110001111011110',
         '0111011001101010000010101011101100111100011101111011001010101000',
         '1000000111000010110010010010111001000111111011011010111011100110',
         '1001001001110010001011001000010100010100100000100011010100111011',
         '1010001010111111111010001010000101001100111100010000001101100100',
         '1010100000011010011001100100101110111100010000100011000000000001',
         '1100001001001011100010110111000011010000111110001001011110010001',
         '1100011101101100010100011010001100000110010101001011111000110000',
         '1101000110010010111010000001100111010110111011110101001000011000',
         '1101011010011001000001100010010001010101011001011010100100010000',
         '1111010000001110001101011000010101010111011100010010000000101010',
         '0001000001101010101000000111000000110010101110111101000110111000',
         '0001100110100100110000010001011010111000110100101101000011001000',
         '0001111000110111011011000000100001010001010000011010101101010011',
         '0010011101001000011101110100110011011111100011101110101110011001',
         '0011010010110000101111001011010111100001100110110100100010101000',
         '0011100100011100000011001011001111000101110010010101101001100011',
         '0100111011011000101010100100101011100011010000011000101011001011',
         '0101101110011100110010100100111101110111011000111110001101110011',
         '0110100000101110011011111111001111010110101100101011100010100011',
         '0111010010001111100000101110111001011101111011111011001011111100',
         '0111100010100101011000110110111101000011000101110010111101100000',
         '1000010011001000011110000001010010100001111100001010101101110010',
         '1000110011000111000000100000100000011010011001000011100111101100',
         '1001000010111110111111111111101000100011011000110001111000101000',
         '1010010001010000011011001110101111011110100000101011110111101001',
         '1011111011111001101000111111011110110010110001100111100100010101',
         '1100011001110001011110001111001011100011011100100101001100101011',
         '1100101000100111001111101100111011101010001001100110000110011100',
         '1101000110000110101110001100011100100001110000001100001000000111',
         '1110101011011010011111011101011011001101111000001110101100011110',
         '1111010101111101010011110111111111101110011011101101000101111000',
         '0000011011110000011001111010101001110010000101110110111110111010',
         '0000101001100011011111011100010110100010110010001001100010100110',
         '0001000100111111100110000000010010111110111110010000110110101110',
         '0001101101110001000010110011010100010011000111000100011100011011',
         '0010100011011011011101111111010100100011000001000111110110000100',
         '0011001011001010101010110111101101000000110001110010010010010011',
         '0011110010011110101111100000101000010101110010011011111010111100',
         '0100001100011101011001111100010010011100000100000000110101001100',
         '0100110011000101110101001011111011001011001111100100001010110110',
         '0101100101111111001010011001110011111100011001010111111000101010',
         '0101111111001011011011111010101100111010110101101111101011101100',
         '0110110001000100000110011000110001001010010001110101100000010111'
       ]

    bits=""
    for i in message:
        temp=bin(int(i,16))[2:]
        while len(temp)<4:
            temp="0"+temp
        bits +=temp
    message=padding(bits)

    for chunk in message:
        w=[]
        for i in range(80):
            w.append('')

        for i in range(16):
            w[i]=chunk[i*64:(i+1)*64]

        for i in range(16, 80):
            s0= xor(xor(rightRotate(w[i-15],1), rightRotate(w[i-15],8)), rightShift(w[i-15],7))
            s1= xor(xor(rightRotate(w[i-2],19), rightRotate(w[i-2],61)), rightShift(w[i-2],6))
            w[i] = binPlus(w[i-16], s0, w[i-7], s1)

        a = h0
        b = h1
        c = h2
        d = h3
        e = h4
        f = h5
        g = h6
        h = h7

        for i in range(80):
            S0 = xor(xor(rightRotate(a,28),rightRotate(a,34)),rightRotate(a,39))
            S1 = xor(xor(rightRotate(e,14),rightRotate(e,18)),rightRotate(e,41))
            ch = xor(binAnd(e,f),binAnd(binInv(e),g))
            temp1 = binPlus(h, S1, ch, k[i], w[i])
            maj = xor(xor((binAnd(a,b)),(binAnd(a,c))),(binAnd(b,c)))
            temp2 = binPlus(S0, maj)

            h = g
            g = f
            f = e
            e = binPlus(d, temp1)
            d = c
            c = b
            b = a
            a = binPlus(temp1, temp2)

        h0 = binPlus(a,h0)
        h1 = binPlus(b,h1)
        h2 = binPlus(c,h2)
        h3 = binPlus(d,h3)
        h4 = binPlus(e,h4)
        h5 = binPlus(f,h5)
        h6 = binPlus(g,h6)
        h7 = binPlus(h,h7)

    return hex(int(h0 + h1 + h2 + h3 + h4 + h5 + h6 + h7,2)).upper()[2:]

def padding(message):#message is in bits
    og=len(message)%2**128
    message+="1"
    while((len(message)+128)%1024!=0):
        message+='0'
    temp=bin(og)[2:]
    while len(temp) < 128:
        temp = '0' + temp
    message=message + temp
    temp=[]
    for i in range(int(len(message)/1024)):
        temp.append(message[i*1024:(i+1)*1024])
    return temp

def getDigest(filename):
    message=""
    if filename.find(".png")!=-1:
        print("Message is an image")
        imageFile = np.asarray(Image.open(filename).convert('RGB'))

        x=bin((len(imageFile)))[2:]
        y=bin(len(imageFile[0]))[2:]
        while len(x)<10:
            x='0'+x
        while len(y)<10:
            y='0'+y
        message=hex(int(x+y,2))[2:]

        while len(message)<5:
            message='0'+message

        message='FFF' + message

        for i in imageFile:
            for j in i:
                for k in j:
                    rgb=hex(k)[2:]
                    while len(rgb)<2:
                        rgb = '0' + rgb
                    message += rgb
    else:
        if filename.find(".txt") != -1:
            file = open(filename, "r")
            temp=file.read()
            print("Message is a file")
        else:
            temp=filename
            print("Message is text")
        message=''
        for i in temp:
            x=hex(ord(i))[2:]
            if len(x)<2:
                x='0'+x
            message+=x
        print("The text to be transmitted is:\t'", end='')
        print(temp,end="'\n")
    h=hash(message)
    print("Transmitter plaintext hash:\n",h)
    return message + h

def rebuildImage(cipherH):#cipherH in hex
    cipher=''
    for i in cipherH:
        temp=bin(int(i,16))[2:]
        while len(temp)<4:
            temp = '0' + temp
        cipher+=temp

    x=int(cipher[0:10],2)
    y=int(cipher[10:20],2)
    image=[]
    for i in range(x):
        temp=[]
        for j in range(y):
            rgb=[]
            for k in range(3):
                val=''
                for l in range(8):
                    val+=cipher[20 + l + k*8 + j*8*3 + i*8*3*y]
                rgb.append(int(val,2))
            temp.append(rgb)
        image.append(temp)
    encryptedImage = Image.fromarray(np.uint8(image))
    encryptedImage.show()

def RC4_Encrypt(plaintext, key):
    k = []
    s = []
    t = []
    p = []
    sTable = []
    outK = []

    for char in key:
        k.append(char)
    for char in plaintext:
        p.append(char)

    for i in range(0, 256):
        s.append(i)
        t.append(k[i % len(k)])

    j = 0
    for i in range(0, 256):
        j = (j + s[i] + ord(t[i])) % 256
        temp = s[i]
        s[i] = s[j]
        s[j] = temp
        # print(temp, s[i], s[j])

    i = 0
    j = 0
    for i in range(0, len(plaintext)):
        i = (i + 1) % 256
        j = (j + s[i]) % 256
        # swap
        temp = s[i]
        s[i] = s[j]
        s[j] = temp
        #print(toHex(np.asarray(s)))
        sTable.append(toHex(np.asarray(s)))
        #sTable.append(np.asarray(s))
        tint = (s[i] + s[j]) % 256
        outK.append(tint)

    finStr = ""
    for i in range(0,len(p),2):
        a=p[i]
        if i+1<len(p):
            a+=p[i+1]
        finStr += chr(outK[i] ^ int(a,16))
    return(finStr)

def RC4_Decrypt(ciphertext, key):
    cypher=""
    for i in ciphertext:
        temp=hex(ord(i))[2:]
        while len(temp)<2:
            temp='0'+temp
        cypher+=temp
    return RC4_Encrypt(cypher, key)

def toHex(sIn):
    hexArr = []
    for i in range(len(sIn)):
        hexArr.append("{:02x}".format(sIn[i]))
    return np.asarray(hexArr)

def gcd(a,h):
    while True:
        temp=a%h
        if temp==0:
            return h
        a=h
        h=temp

def keyGen(p,q):
    n = q * p
    e = 2
    phi = (p - 1) * (q - 1)
    while e < phi:
        if gcd(e, phi) == 1:
            d = int((1 + (2 * phi)) / e)
            if 2 == (pow(pow(2, e, n), d, n)):
                break
        e += 1
    print("RECEIVER The value of n is:\t",n)
    print('RECEIVER Phi:\t', phi)
    print('RECEIVER Public Key:\t', e)
    print('RECEIVER Private Key:\t', d)
    return n,e,d

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
    global key
    initRandom()
    print("Phase 1")
    print("*********************************************************************")
    p = input("Enter the RECEIVER'S p value or press enter: ")
    q = input("Enter the RECEIVER'S q value or press enter: ")
    if p=='':
        if q=='4026300193':
            p='1433129909'
        else:
            p='4026300193'
        print("The value of p set to:\t",p)
    if q=='':
        if p=='4026300193':
            q='1433129909'
        else:
            q='4026300193'
        print("The value of q set to:\t", q)
    p=int(p)
    q=int(q)

    #n, pub, priv
    n,e,d = keyGen(p,q)
    keyT = input("TRANSMITTER Enter the RC4 key in hex or press enter: ")
    if keyT=='':
        for i in range(256):
            byte=""
            for j in range(8):
                byte+=getRandomBit()
            byte=hex(int(byte,2))[2:]
            keyT+=byte
        print(len(keyT))
        print("TRANSMITTER Generated RC4 key:\t", keyT)
    #else:
    #    key=''
    #    for i in keyT:
    #        key+=hex(ord(i))[2:]
    key = RSA(keyT,e,n)
    print("TRANSMITTER RSA encrypted key: \t",key)
    key = RSAde(key,d,n)
    print("RECEIVER RSA decrypted key: \t",key)

    phase2()

def phase2():
    print()
    #file=input("Enter file name or message: ")
    file="Image0.png"
    print("\nPhase 2")
    print("*********************************************************************")

    text = getDigest(file)

    enc = RC4_Encrypt(text, key)
    ran=['1','1','1','1']

    for i in range(4):
        ran[i]=getRandomBit()

    if ran[0]=='1':
        if ran[1]=='1':
            if ran[2]=='1':
                if ran[3]=='1':
                    print("A bit will be flipped to ensure hash is not authenticated")
                    byte=bin(ord(enc[3]))[2:]
                    flipped=''
                    if byte[0]=='0':
                        flipped ='1'
                    else:
                        flipped = '0'
                    flipped+=byte[1:]
                    enc=enc[0]+enc[1]+enc[2]+chr(int(flipped,2))+enc[4:]

    else:
        print("No bit flip")


    a=RC4_Decrypt(enc, key)
    plainHex=''
    for i in a:
        x=hex(ord(i))[2:]
        if len(x)<2:
            x='0'+x
        plainHex+=x
    plainHex=plainHex.upper()

    print("RC4 Encrypted Cyphertext:\n",plainHex)

    hash0 = plainHex[len(plainHex) - 128:]
    hash1 = hash(plainHex[:len(plainHex) - 128])

    print("\nPhase 3")
    print("*********************************************************************")

    print("Expected Hash:\n", hash0)
    print("Calculated Hash:\n", hash1)
    if hash1 != hash0:
        print("Authentication Failed")
    else:
        print("Message Authenticated")

        if plainHex[:3]=='FFF':
            plainHex=plainHex[3:]
            b = bin(int(plainHex[0:5], 16))[2:]
            while len(b) < 20:
                b = '0' + b
            x = int(b[:10], 2)
            y = int(b[10:], 2)
            rebuildImage(plainHex[0:len(plainHex)-128])
            print("Image Recieved with dimensions:\t",x,"by",y)
        else:
            plain=""
            for i in range(0,len(plainHex)-128,2):
                plain+=chr(int(plainHex[i:i+2],16))
            print("Decrypted message:\t'", end='')
            print(plain,end="'\n")


start()