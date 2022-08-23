from PIL import Image
import numpy as np
import random as rand

imageConst=69

AES_sbox = np.load("AES_Sbox_lookup.npy")
AES_Inverse_sbox = np.load("AES_Inverse_Sbox_lookup.npy")

def xorHexByte(a, b):
    xor = hex(int(a,16)^int(b,16))[2:]
    if len(xor) < 2:
        xor = '0' + xor
    return xor

RC = [0x00000000, 0x01000000, 0x02000000,
        0x04000000, 0x08000000, 0x10000000,
        0x20000000, 0x40000000, 0x80000000,
       0x1b000000, 0x36000000, 0x6c, 0x1c3, 0x29d, 0x421]

def keyExpansion(keyAscii):
    key = []
    for i in keyAscii:
        key.append(format(ord(i), "x"))
    x=60
    w = [()] * x

    for i in range(4):
        w[i] = (key[4 * i], key[4 * i + 1], key[4 * i + 2], key[4 * i + 3])

    for i in range(4, x):
        prev = w[i - 1]
        word = w[i - 4]

        if i % 4 == 0:
            x = leftRotate(prev)
            y = SubWord(x)
            prev = xorHex(y, hex(RC[int(i/4)])[2:])

        word = ''.join(word)
        prev = ''.join(prev)

        temp = xorHex(word, prev)
        w[i] = (temp[:2], temp[2:4], temp[4:6], temp[6:8])

    return w
def SubWord(word):
    sub = ()

    for i in range(4):
        row = int(word[i][0],16)
        col = int(word[i][1],16)

        piece = hex(int(AES_sbox[row][col],16))[2:]

        if len(piece) != 2:
            piece = '0' + piece
        sub = (*sub, piece)

    return ''.join(sub)

def xorHex(a, b):
    xor = hex(int(a,16)^int(b,16))[2:]
    while len(xor) != 8:
        xor = '0' + xor
    return xor


def addRoundKey(state, key, round, inv=False):
    if inv:
        round=14-round
    rk = []
    for i in range(4):
        temp = []
        for j in range(4):
            temp.append(xorHexByte(state[i][j], key[round * 4 + i][j]))
        rk.append(temp)

    return rk

def leftRotate(val):
    return val[1:] + val[:1]

def rightRotate(val):
    temp=[]
    i=len(val)-1
    temp.append(val[i])
    for j in range(i):
        temp.append(val[j])
    return temp

def subBytes(state, sbox):
    sub=[]
    for i in state:
        row=[]
        for j in i:
            if len(j)<2:
                j='0'+j
            r = int(j[0], 16)
            c = int(j[1], 16)
            temp = hex(int(sbox[r][c], 16))[2:]
            if len(temp) != 2:
                temp= '0' + temp
            row.append(temp)
        sub.append(row)
    return sub

def shiftRows(state, inv=False):
    if inv:
        for i in range(4):
            for j in range(i):
                state[i]=rightRotate(state[i])
    else:
        for i in range(4):
            for j in range(i):
                state[i] = leftRotate(state[i])

    return state

def sXorC(state, cbc):
    temp=[]
    for i in range(4):
        row=[]
        for j in range(4):
            row.append(xorHex(state[i][j],cbc[i*4+j]))
        temp.append(row)
    return temp

mult2=['00', '02', '04', '06', '08', '0a', '0c', '0e', '10', '12', '14', '16', '18', '1a', '1c', '1e', '20', '22', '24', '26', '28', '2a', '2c', '2e', '30', '32', '34', '36', '38', '3a', '3c', '3e', '40', '42', '44', '46', '48', '4a', '4c', '4e', '50', '52', '54', '56', '58', '5a', '5c', '5e', '60', '62', '64', '66', '68', '6a', '6c', '6e', '70', '72', '74', '76', '78', '7a', '7c', '7e', '80', '82', '84', '86', '88', '8a', '8c', '8e', '90', '92', '94', '96', '98', '9a', '9c', '9e', 'a0', 'a2', 'a4', 'a6', 'a8', 'aa', 'ac', 'ae', 'b0', 'b2', 'b4', 'b6', 'b8', 'ba', 'bc', 'be', 'c0', 'c2', 'c4', 'c6', 'c8', 'ca', 'cc', 'ce', 'd0', 'd2', 'd4', 'd6', 'd8', 'da', 'dc', 'de', 'e0', 'e2', 'e4', 'e6', 'e8', 'ea', 'ec', 'ee', 'f0', 'f2', 'f4', 'f6', 'f8', 'fa', 'fc', 'fe', '1b', '19', '1f', '1d', '13', '11', '17', '15', '0b', '09', '0f', '0d', '03', '01', '07', '05', '3b', '39', '3f', '3d', '33', '31', '37', '35', '2b', '29', '2f', '2d', '23', '21', '27', '25', '5b', '59', '5f', '5d', '53', '51', '57', '55', '4b', '49', '4f', '4d', '43', '41', '47', '45', '7b', '79', '7f', '7d', '73', '71', '77', '75', '6b', '69', '6f', '6d', '63', '61', '67', '65', '9b', '99', '9f', '9d', '93', '91', '97', '95', '8b', '89', '8f', '8d', '83', '81', '87', '85', 'bb', 'b9', 'bf', 'bd', 'b3', 'b1', 'b7', 'b5', 'ab', 'a9', 'af', 'ad', 'a3', 'a1', 'a7', 'a5', 'db', 'd9', 'df', 'dd', 'd3', 'd1', 'd7', 'd5', 'cb', 'c9', 'cf', 'cd', 'c3', 'c1', 'c7', 'c5', 'fb', 'f9', 'ff', 'fd', 'f3', 'f1', 'f7', 'f5', 'eb', 'e9', 'ef', 'ed', 'e3', 'e1', 'e7', 'e5']
mult3=['00', '03', '06', '05', '0c', '0f', '0a', '09', '18', '1b', '1e', '1d', '14', '17', '12', '11', '30', '33', '36', '35', '3c', '3f', '3a', '39', '28', '2b', '2e', '2d', '24', '27', '22', '21', '60', '63', '66', '65', '6c', '6f', '6a', '69', '78', '7b', '7e', '7d', '74', '77', '72', '71', '50', '53', '56', '55', '5c', '5f', '5a', '59', '48', '4b', '4e', '4d', '44', '47', '42', '41', 'c0', 'c3', 'c6', 'c5', 'cc', 'cf', 'ca', 'c9', 'd8', 'db', 'de', 'dd', 'd4', 'd7', 'd2', 'd1', 'f0', 'f3', 'f6', 'f5', 'fc', 'ff', 'fa', 'f9', 'e8', 'eb', 'ee', 'ed', 'e4', 'e7', 'e2', 'e1', 'a0', 'a3', 'a6', 'a5', 'ac', 'af', 'aa', 'a9', 'b8', 'bb', 'be', 'bd', 'b4', 'b7', 'b2', 'b1', '90', '93', '96', '95', '9c', '9f', '9a', '99', '88', '8b', '8e', '8d', '84', '87', '82', '81', '9b', '98', '9d', '9e', '97', '94', '91', '92', '83', '80', '85', '86', '8f', '8c', '89', '8a', 'ab', 'a8', 'ad', 'ae', 'a7', 'a4', 'a1', 'a2', 'b3', 'b0', 'b5', 'b6', 'bf', 'bc', 'b9', 'ba', 'fb', 'f8', 'fd', 'fe', 'f7', 'f4', 'f1', 'f2', 'e3', 'e0', 'e5', 'e6', 'ef', 'ec', 'e9', 'ea', 'cb', 'c8', 'cd', 'ce', 'c7', 'c4', 'c1', 'c2', 'd3', 'd0', 'd5', 'd6', 'df', 'dc', 'd9', 'da', '5b', '58', '5d', '5e', '57', '54', '51', '52', '43', '40', '45', '46', '4f', '4c', '49', '4a', '6b', '68', '6d', '6e', '67', '64', '61', '62', '73', '70', '75', '76', '7f', '7c', '79', '7a', '3b', '38', '3d', '3e', '37', '34', '31', '32', '23', '20', '25', '26', '2f', '2c', '29', '2a', '0b', '08', '0d', '0e', '07', '04', '01', '02', '13', '10', '15', '16', '1f', '1c', '19', '1a']


def mixColumns(state, inv=False):
    mixC = []
    if inv:
        for j in range(4):
            temp = []
            temp.append(state[1][j])
            temp.append(state[3][j])
            temp.append(state[0][j])
            temp.append(state[2][j])
            mixC.append(temp)
    else:
        for j in range(4):
            temp = []
            temp.append(state[2][j])
            temp.append(state[0][j])
            temp.append(state[3][j])
            temp.append(state[1][j])
            mixC.append(temp)

    mixC = np.transpose(mixC)
    return mixC

def AES_Encrypt(inspect_mode, plaintext, iv, key, sbox_array):
    cbc=[]
    for i in iv:
        cbc.append(format(ord(i), "x"))
    key=keyExpansion(key)
    if type(plaintext) == np.ndarray or type(plaintext) == list:
        r = []
        g = []
        b = []
        for i in plaintext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                x = j[0]
                if x == imageConst:
                    x += 1
                rTemp.append(hex(x)[2:])

                x = j[1]
                if x == imageConst:
                    x += 1
                gTemp.append(hex(x)[2:])

                x = j[2]
                if x == imageConst:
                    x += 1
                bTemp.append(hex(x)[2:])
            r.append(rTemp)
            g.append(gTemp)
            b.append(bTemp)
        for i in range(len(r)):
            while len(r[i]) % 16 != 0:
                r[i].append(hex(imageConst)[2:])
                g[i].append(hex(imageConst)[2:])
                b[i].append(hex(imageConst)[2:])

        rC = []
        gC = []
        bC = []
        # 111111111111111111111111111111111111
        for q in range(len(r)):
            plaintext = []
            hextext = r[q]
            for i in range(int(len(hextext) / 16)):
                temp = ""
                for j in range(16):
                    t = hextext[i * 16 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                temp = temp.upper()
                plaintext.append(temp)
            cipher = []
            for text in plaintext:
                state = []
                for i in range(4):
                    temp = []
                    for j in range(4):
                        temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                    state.append(temp)

                state = addRoundKey(state, key, 0)

                for i in range(1, 14, 1):
                    state = subBytes(state, sbox_array)
                    state = shiftRows(state)
                    state = mixColumns(state)
                    state = addRoundKey(state, key, i)

                state = subBytes(state, sbox_array)
                state = shiftRows(state)
                state = addRoundKey(state, key, 14)
                for i in state:
                    for j in i:
                        cipher.append(int(j, 16))
            rC.append(cipher)


        # 111111111111111111111111111111111111
        for q in range(len(g)):
            plaintext = []
            hextext = g[q]
            for i in range(int(len(hextext) / 16)):
                temp = ""
                for j in range(16):
                    t = hextext[i * 16 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                plaintext.append(temp)
            cipher = []
            for text in plaintext:
                state = []
                for i in range(4):
                    temp = []
                    for j in range(4):
                        temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                    state.append(temp)

                state = addRoundKey(state, key, 0)

                for i in range(1, 14, 1):
                    state = subBytes(state, sbox_array)
                    state = shiftRows(state)
                    state = mixColumns(state)
                    state = addRoundKey(state, key, i)

                state = subBytes(state, sbox_array)
                state = shiftRows(state)
                state = addRoundKey(state, key, 14)
                for i in state:
                    for j in i:
                        cipher.append(int(j, 16))
            gC.append(cipher)
        # 111111111111111111111111111111111111
        for q in range(len(b)):
            plaintext = []
            hextext = b[q]
            for i in range(int(len(hextext) / 16)):
                temp = ""
                for j in range(16):
                    t = hextext[i * 16 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                plaintext.append(temp)
            cipher = []
            for text in plaintext:
                state = []
                for i in range(4):
                    temp = []
                    for j in range(4):
                        temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                    state.append(temp)

                state = addRoundKey(state, key, 0)

                for i in range(1, 14, 1):
                    state = subBytes(state, sbox_array)
                    state = shiftRows(state)
                    state = mixColumns(state)
                    state = addRoundKey(state, key, i)

                state = subBytes(state, sbox_array)
                state = shiftRows(state)
                state = addRoundKey(state, key, 14)
                for i in state:
                    for j in i:
                        cipher.append(int(j, 16))
            bC.append(cipher)

        cipher = []
        for i in range(len(rC)):
            temp = []
            for j in range(len(rC[i])):
                temp.append([rC[i][j], gC[i][j], bC[i][j]])
            cipher.append(temp)
        return np.array(cipher)

    else:
        hextext = []
        states=[]
        while len(plaintext) % 16 != 0:
            plaintext += '~'
        for i in plaintext:
            hextext.append(format(ord(i), "x"))
        plaintext = []
        for i in range(int(len(hextext) / 16)):
            temp = ""
            for j in range(16):
                t = hextext[i * 16 + j]
                if len(t) != 2:
                    t = '0' + t
                temp += t
                temp = temp.upper()
            plaintext.append(temp)
        cipher=""
        for text in plaintext:
            state=[]
            for i in range(4):
                temp=[]
                for j in range(4):
                    temp.append(text[(i*4+j)*2]+text[(i*4+j)*2+1])
                state.append(temp)

            state = addRoundKey(state, key, 0)

            for i in range(1,14,1):
                state=subBytes(state,sbox_array)
                state=shiftRows(state)
                state=mixColumns(state)
                state = addRoundKey(state, key, i)

            state = subBytes(state, sbox_array)
            state = shiftRows(state)
            state = addRoundKey(state, key, 14)
            states.append(state)
            for i in state:
                for j in i:
                    cipher+=chr(int(j,16))
        if inspect_mode:
            return {
                "States":states,
                "Ciphertext":cipher
            }
        return cipher

def AES_Decrypt(inspect_mode, ciphertext, iv, key, inv_sbox_array):
    key = keyExpansion(key)
    cbc=[]
    for i in iv:
        cbc.append(format(ord(i), "x"))
    if type(ciphertext) == np.ndarray or type(ciphertext) == list:
        r = []
        g = []
        b = []
        for i in ciphertext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                x = j[0]
                if x == imageConst:
                    x += 1
                rTemp.append(hex(int(x))[2:])

                x = j[1]
                if x == imageConst:
                    x += 1
                gTemp.append(hex(int(x))[2:])

                x = j[2]
                if x == imageConst:
                    x += 1
                bTemp.append(hex(int(x))[2:])
            r.append(rTemp)
            g.append(gTemp)
            b.append(bTemp)

        rC = []
        gC = []
        bC = []

        # 1111111111111111111111111111111111111111111
        for q in range(len(r)):
            ciphertext = []
            hextext = r[q]
            for i in range(int(len(hextext) / 16)):
                temp = ""
                for j in range(16):
                    t = hextext[i * 16 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                ciphertext.append(temp)
            row=[]
            for text in ciphertext:
                state = []
                for i in range(4):
                    temp = []
                    for j in range(4):
                        temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                    state.append(temp)

                state = addRoundKey(state, key, 0, True)
                for i in range(1, 14, 1):
                    state = shiftRows(state, True)
                    state = subBytes(state, inv_sbox_array)
                    state = addRoundKey(state, key, i, True)
                    state = mixColumns(state, True)

                state = shiftRows(state, True)
                state = subBytes(state, inv_sbox_array)
                state = addRoundKey(state, key, 14, True)
                for i in state:
                    for j in i:
                        a = int(j, 16)
                        if a != imageConst:
                            row.append(a)
            rC.append(row)
        # 22222222222222222222222222222222222222222222
        for q in range(len(g)):
            ciphertext = []
            hextext = g[q]
            for i in range(int(len(hextext) / 16)):
                temp = ""
                for j in range(16):
                    t = hextext[i * 16 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                ciphertext.append(temp)
            row = []
            for text in ciphertext:
                state = []
                for i in range(4):
                    temp = []
                    for j in range(4):
                        temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                    state.append(temp)

                state = addRoundKey(state, key, 0, True)
                for i in range(1, 14, 1):
                    state = shiftRows(state, True)
                    state = subBytes(state, inv_sbox_array)
                    state = addRoundKey(state, key, i, True)
                    state = mixColumns(state, True)

                state = shiftRows(state, True)
                state = subBytes(state, inv_sbox_array)
                state = addRoundKey(state, key, 14, True)
                for i in state:
                    for j in i:
                        a = int(j, 16)
                        if a != imageConst:
                            row.append(a)
            gC.append(row)
        # 33333333333333333333333333333333333333333333
        for q in range(len(b)):
            ciphertext = []
            hextext = b[q]
            for i in range(int(len(hextext) / 16)):
                temp = ""
                for j in range(16):
                    t = hextext[i * 16 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                ciphertext.append(temp)
            row = []
            for text in ciphertext:
                state = []
                for i in range(4):
                    temp = []
                    for j in range(4):
                        temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                    state.append(temp)

                state = addRoundKey(state, key, 0, True)
                for i in range(1, 14, 1):
                    state = shiftRows(state, True)
                    state = subBytes(state, inv_sbox_array)
                    state = addRoundKey(state, key, i, True)
                    state = mixColumns(state, True)

                state = shiftRows(state, True)
                state = subBytes(state, inv_sbox_array)
                state = addRoundKey(state, key, 14, True)
                for i in state:
                    for j in i:
                        a = int(j, 16)
                        if a != imageConst:
                            row.append(a)
            bC.append(row)
        # 44444444444444444444444444444444444444444444
        cipher = []
        for i in range(len(rC)):
            temp = []
            for j in range(len(rC[i])):
                if rC[i][j] != imageConst:
                    temp.append([rC[i][j], gC[i][j], bC[i][j]])
            cipher.append(temp)
        return np.array(cipher)

    else:
        hextext = []
        for i in ciphertext:
            hextext.append(format(ord(i), "x"))

        ciphertext = []
        for i in range(int(len(hextext) / 16)):
            temp = ""
            for j in range(16):
                t = hextext[i * 16 + j]
                if len(t) != 2:
                    t = '0' + t
                temp += t
                temp = temp.upper()
            ciphertext.append(temp)

        plain = ""
        rounds=[]
        for text in ciphertext:
            state = []
            for i in range(4):
                temp = []
                for j in range(4):
                    temp.append(text[(i * 4 + j) * 2] + text[(i * 4 + j) * 2 + 1])
                state.append(temp)

            state = addRoundKey(state, key, 0, True)
            for i in range(1, 14, 1):
                state = shiftRows(state,True)
                state = subBytes(state, inv_sbox_array)
                state = addRoundKey(state, key, i, True)
                state = mixColumns(state, True)

            state = shiftRows(state, True)
            state = subBytes(state, inv_sbox_array)
            state = addRoundKey(state, key, 14, True)
            rounds.append(state)
            for i in state:
                for j in i:
                    a = chr(int(j, 16))
                    if a!='~':
                        plain +=a
        if inspect_mode:
            return {
                "States":rounds,
                "Plaintext":plain
            }

        return plain

key = np.load("AESKeys.npy")[0]
imageToEncrypt = Image.open("Images/Hi.png").convert("RGB").__array__()

img = Image.fromarray(np.array(np.uint8(imageToEncrypt)))
img.show()
enc=AES_Encrypt(False, imageToEncrypt, "", key, AES_sbox)
img = Image.fromarray(np.array(np.uint8(enc)))
img.show()
dec=AES_Decrypt(False, enc, "", key, AES_Inverse_sbox)
img = Image.fromarray(np.array(np.uint8(dec)))
img.show()
