from PIL import Image
import numpy as np
import random as rand

imageConst=69

TDEA_ip = np.load("DES_Initial_Permutation.npy")
TDEA_invIp = np.load("DES_Inverse_Initial_Permutation.npy")


# Expansion D-box Table
expansion_table = [31, 0, 1, 2, 3, 4, 3,  4,  5,  6,  7,  8, 7, 8, 9,10, 11, 12, 11, 12, 13, 14, 15, 16, 15, 16, 17, 18, 19, 20, 19, 20, 21, 22, 23, 24, 23, 24, 25, 26, 27, 28, 27, 28, 29, 30, 31, 0]

# Straight Permutation Table
per = [15, 6, 19, 20, 28, 11, 27, 16, 0, 14, 22, 25, 4, 17, 30, 9, 1, 7, 23, 13, 31, 26, 2, 8, 18, 12, 29, 5, 21, 10, 3, 24]

# S-box Table
sbox = [
        [[14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7],
         [0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8],
         [4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0],
         [15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13]],
        [[15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10],
         [3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5],
         [0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15],
         [13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9]],
        [[10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8],
         [13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1],
         [13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7],
         [1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12]],
        [[7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15],
         [13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9],
         [10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4],
         [3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14]],
        [[2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9],
         [14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6],
         [4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14],
         [11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3]],
        [[12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11],
         [10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8],
         [9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6],
         [4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13]],
        [[4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1],
         [13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6],
         [1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2],
         [6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12]],
        [[13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7],
         [1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2],
         [7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8],
         [2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11]]
]

def hex2bin(s):
    mp = {'0': "0000",
          '1': "0001",
          '2': "0010",
          '3': "0011",
          '4': "0100",
          '5': "0101",
          '6': "0110",
          '7': "0111",
          '8': "1000",
          '9': "1001",
          'A': "1010",
          'B': "1011",
          'C': "1100",
          'D': "1101",
          'E': "1110",
          'F': "1111"}
    bin = ""
    for i in range(len(s)):
        bin = bin + mp[s[i]]
    return bin

def bin2hex(s):
    mp = {"0000": '0',
          "0001": '1',
          "0010": '2',
          "0011": '3',
          "0100": '4',
          "0101": '5',
          "0110": '6',
          "0111": '7',
          "1000": '8',
          "1001": '9',
          "1010": 'A',
          "1011": 'B',
          "1100": 'C',
          "1101": 'D',
          "1110": 'E',
          "1111": 'F'}
    hex = ""
    for i in range(0, len(s), 4):
        ch = ""
        ch = ch + s[i]
        ch = ch + s[i + 1]
        ch = ch + s[i + 2]
        ch = ch + s[i + 3]
        hex = hex + mp[ch]

    return hex

def bin2dec(binary):
    binary1 = binary
    decimal, i, n = 0, 0, 0
    while (binary != 0):
        dec = binary % 10
        decimal = decimal + dec * pow(2, i)
        binary = binary // 10
        i += 1
    return decimal

def dec2bin(num):
    res = bin(num).replace("0b", "")
    if (len(res) % 4 != 0):
        div = len(res) / 4
        div = int(div)
        counter = (4 * (div + 1)) - len(res)
        for i in range(0, counter):
            res = '0' + res
    return res

def permute(k, arr, n):
    permutation = ""
    for i in range(0, n):
        permutation = permutation + k[arr[i]]
    return permutation

def permuteM(k, arr, n):
    permutation = ""
    for i in range(0, n):
        permutation = permutation + k[arr[i]-1]
    return permutation

def shift_left(k, nth_shifts):
    s = ""
    for i in range(nth_shifts):
        for j in range(1, len(k)):
            s = s + k[j]
        s = s + k[0]
        k = s
        s = ""
    return k


def xor(a, b):
    ans = ""
    for i in range(len(a)):
        if a[i] == b[i]:
            ans = ans + "0"
        else:
            ans = ans + "1"
    return ans


def hexKey(key):
    k = ""
    for i in key:
        a = hex(ord(i))[2:]
        if len(a) < 2:
            a = '0' + a
        k += a
    k=str.upper(k)
    return k

def truncKey(key):
    temp = []
    for i in range(1, 65):
        if i % 8 != 0:
            temp.append(key[i])
    return key

def compPermutation(key):
    comTable = [14, 17, 11, 24, 1, 5,
                3, 28, 15, 6, 21, 10,
                23, 19, 12, 4, 26, 8,
                16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55,
                30, 40, 51, 45, 33, 48,
                44, 49, 39, 56, 34, 53,
                46, 42, 50, 36, 29, 32]
    temp=[]
    for i in comTable:
        temp.append(key[i-1])
    return temp

def TDEA_Encrypt(inspect_mode, plaintext, key1, key2, key3, ip):
    if inspect_mode:
        hextext = []
        while len(plaintext) % 8 != 0:
            plaintext += '~'
        for i in plaintext:
            hextext.append(format(ord(i), "x"))
        plaintext = []
        for i in range(int(len(hextext) / 8)):
            temp = ""
            for j in range(8):
                t = hextext[i * 8 + j]
                if len(t) != 2:
                    t = '0' + t
                temp += t
                temp = temp.upper()
            plaintext.append(temp)

        key = [hex2bin(hexKey(key1)), hex2bin(hexKey(key2)), hex2bin(hexKey(key3))]
        cipher = ""
        rounds=[]
        for text in plaintext:
            for k in key:
                k = truncKey(k)
                # Splitting
                left = k[0:28]
                right = k[28:56]

                rkb = []
                for i in range(0, 16):
                    if i in [0, 1, 8, 15]:
                        t = 1
                    else:
                        t = 2
                    left = shift_left(left, t)
                    right = shift_left(right, t)

                    combine_str = left + right

                    round_key = compPermutation(combine_str)

                    rkb.append(round_key)

                rounds.append(DesEncrypt(text, rkb, ip, True, True))
        return {
            "DES1_Outputs": rounds[0],
            "DES2_Outputs": rounds[1],
            "DES3_Outputs": rounds[2],
            "Ciphertext": rounds[2][len(rounds[2])-1]
        }
    if type(plaintext)==np.ndarray:
        r = []
        g = []
        b = []
        for i in plaintext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                x=j[0]
                if x==imageConst:
                    x+=1
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
            while len(r[i])%16!=0:
                r[i].append(hex(imageConst)[2:])
                g[i].append(hex(imageConst)[2:])
                b[i].append(hex(imageConst)[2:])

        rC=[]
        gC=[]
        bC=[]
        #111111111111111111111111111111111111
        for q in range(len(r)):
            plaintext = []
            hextext=r[q]
            for i in range(int(len(hextext) / 8)):
                temp = ""
                for j in range(8):
                    t = hextext[i * 8 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp+=t
                    temp=temp.upper()
                plaintext.append(temp)

            key=[hex2bin(hexKey(key1)), hex2bin(hexKey(key2)), hex2bin(hexKey(key3))]
            cipher=[]
            for text in plaintext:
                for k in key:
                    k = truncKey(k)
                    # Splitting
                    left = k[0:28]
                    right = k[28:56]

                    rkb = []
                    for i in range(0, 16):
                        if i in [0, 1, 8, 15]:
                            t = 1
                        else:
                            t = 2
                        left = shift_left(left, t)
                        right = shift_left(right, t)

                        combine_str = left + right

                        round_key = compPermutation(combine_str)

                        rkb.append(round_key)

                    text = bin2hex(DesEncrypt(text, rkb, ip, True))  # rk for RoundKeys in hexadecimal # rkb for RoundKeys in binary
                cipher.append(text)
            temp=[]
            for i in cipher:
                for j in range(int(len(i)/2)):
                   temp.append(int((i[j*2]+i[j*2+1]),16))
            rC.append(temp)
        #222222222222222222222222222222222222222222222
        for q in range(len(g)):
            plaintext = []
            hextext=g[q]
            for i in range(int(len(hextext) / 8)):
                temp = ""
                for j in range(8):
                    t = hextext[i * 8 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp+=t
                    temp=temp.upper()
                plaintext.append(temp)

            key=[hex2bin(hexKey(key1)), hex2bin(hexKey(key2)), hex2bin(hexKey(key3))]
            cipher=[]
            for text in plaintext:
                for k in key:
                    k = truncKey(k)
                    # Splitting
                    left = k[0:28]
                    right = k[28:56]

                    rkb = []
                    for i in range(0, 16):
                        # Shifting the bits by nth shifts according to shift table
                        if i in [0, 1, 8, 15]:
                            t = 1
                        else:
                            t = 2
                        left = shift_left(left, t)
                        right = shift_left(right, t)

                        # Combination of left and right string
                        combine_str = left + right

                        # Compression of key from 56 to 48 bits
                        round_key = compPermutation(combine_str)

                        rkb.append(round_key)

                    text = bin2hex(DesEncrypt(text, rkb, ip, True))  # rk for RoundKeys in hexadecimal # rkb for RoundKeys in binary
                cipher.append(text)
            temp=[]
            for i in cipher:
                for j in range(int(len(i)/2)):
                   temp.append(int((i[j*2]+i[j*2+1]),16))
            gC.append(temp)
        #333333333333333333333333333333333333333333333
        for q in range(len(b)):
            plaintext = []
            hextext=b[q]
            for i in range(int(len(hextext) / 8)):
                temp = ""
                for j in range(8):
                    t = hextext[i * 8 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp+=t
                    temp=temp.upper()
                plaintext.append(temp)

            key=[hex2bin(hexKey(key1)), hex2bin(hexKey(key2)), hex2bin(hexKey(key3))]
            cipher=[]
            for text in plaintext:
                for k in key:
                    k = truncKey(k)
                    # Splitting
                    left = k[0:28]
                    right = k[28:56]

                    rkb = []
                    for i in range(0, 16):
                        # Shifting the bits by nth shifts according to shift table
                        if i in [0, 1, 8, 15]:
                            t = 1
                        else:
                            t = 2
                        left = shift_left(left, t)
                        right = shift_left(right, t)

                        # Combination of left and right string
                        combine_str = left + right

                        # Compression of key from 56 to 48 bits
                        round_key = compPermutation(combine_str)

                        rkb.append(round_key)

                    text = bin2hex(DesEncrypt(text, rkb, ip, True))  # rk for RoundKeys in hexadecimal # rkb for RoundKeys in binary
                cipher.append(text)
            temp=[]
            for i in cipher:
                for j in range(int(len(i)/2)):
                   temp.append(int((i[j*2]+i[j*2+1]),16))
            bC.append(temp)
        #444444444444444444444444444444444444444444444
        cipher=[]
        for i in range(len(rC)):
            temp=[]
            for j in range(len(rC[i])):
                temp.append([rC[i][j],bC[i][j],gC[i][j]])
            cipher.append(temp)
        return np.array(cipher)

    else:
        hextext = []
        while len(plaintext)%8!=0:
            plaintext+='~'
        for i in plaintext:
            hextext.append(format(ord(i), "x"))
        plaintext = []
        for i in range(int(len(hextext) / 8)):
            temp = ""
            for j in range(8):
                t = hextext[i * 8 + j]
                if len(t) != 2:
                    t = '0' + t
                temp+=t
                temp=temp.upper()
            plaintext.append(temp)

        key=[hex2bin(hexKey(key1)), hex2bin(hexKey(key2)), hex2bin(hexKey(key3))]
        cipher=""
        for text in plaintext:
            for k in key:
                k = truncKey(k)
                # Splitting
                left = k[0:28]
                right = k[28:56]

                rkb = []
                for i in range(0, 16):
                    if i in [0, 1, 8, 15]:
                        t = 1
                    else:
                        t = 2
                    left = shift_left(left, t)
                    right = shift_left(right, t)

                    combine_str = left + right

                    round_key = compPermutation(combine_str)

                    rkb.append(round_key)

                text = bin2hex(DesEncrypt(text, rkb, ip, True))  # rk for RoundKeys in hexadecimal # rkb for RoundKeys in binary
            cipher+=text
        return cipher



def TDEA_Decrypt(inspect_mode, ciphertext, key1, key2, key3, inv_ip):
    if inspect_mode:
        plaintext = []
        for i in range(int(len(ciphertext) / 16)):
            temp = ""
            for j in range(16):
                temp += ciphertext[i * 16 + j]
            plaintext.append(temp)

        key = [hex2bin(hexKey(key3)), hex2bin(hexKey(key2)), hex2bin(hexKey(key1))]
        cipher = ""
        rounds=[]
        for text in plaintext:
            for k in key:
                k = truncKey(k)
                # Splitting
                left = k[0:28]
                right = k[28:56]

                rkb = []
                for i in range(0, 16):
                    # Shifting the bits by nth shifts according to shift table
                    if i in [0, 1, 8, 15]:
                        t = 1
                    else:
                        t = 2
                    left = shift_left(left, t)
                    right = shift_left(right, t)

                    # Combination of left and right string
                    combine_str = left + right

                    # Compression of key from 56 to 48 bits
                    round_key = compPermutation(combine_str)

                    rkb.append(round_key)

                rounds.append(DesEncrypt(text, rkb[::-1], inv_ip, False, True))

        return {
            "DES1_Outputs": rounds[0],
            "DES2_Outputs": rounds[1],
            "DES3_Outputs": rounds[2],
            "Ciphertext": rounds[2][15]
        }

    if type(ciphertext) == np.ndarray or type(ciphertext) == list:
        r = []
        g = []
        b = []
        for i in ciphertext:
            rTemp = []
            gTemp = []
            bTemp = []
            for j in i:
                rTemp.append(hex(j[0])[2:])
                gTemp.append(hex(j[1])[2:])
                bTemp.append(hex(j[2])[2:])

            r.append(rTemp)
            g.append(gTemp)
            b.append(bTemp)

        rC = []
        gC = []
        bC = []
        # 111111111111111111111111111111111111
        for q in range(len(r)):
            plaintext = []
            hextext = r[q]
            for i in range(int(len(hextext) / 8)):
                temp = ""
                for j in range(8):
                    t = hextext[i * 8 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                plaintext.append(temp)

            key = [hex2bin(hexKey(key3)), hex2bin(hexKey(key2)), hex2bin(hexKey(key1))]
            cipher = []
            for text in plaintext:
                for k in key:
                    k = truncKey(k)
                    left = k[0:28]
                    right = k[28:56]

                    rkb = []
                    for i in range(0, 16):
                        if i in [0, 1, 8, 15]:
                            t = 1
                        else:
                            t = 2
                        left = shift_left(left, t)
                        right = shift_left(right, t)

                        combine_str = left + right

                        round_key = compPermutation(combine_str)

                        rkb.append(round_key)

                    text = bin2hex(DesEncrypt(text, rkb[::-1], inv_ip,
                                              False))
                cipher.append(text)
            temp = []
            for i in cipher:
                for j in range(int(len(i) / 2)):
                    temp.append(int((i[j * 2] + i[j * 2 + 1]), 16))
            rC.append(temp)
        # 222222222222222222222222222222222222222222222
        for q in range(len(g)):
            plaintext = []
            hextext = g[q]
            for i in range(int(len(hextext) / 8)):
                temp = ""
                for j in range(8):
                    t = hextext[i * 8 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                plaintext.append(temp)

            key = [hex2bin(hexKey(key3)), hex2bin(hexKey(key2)), hex2bin(hexKey(key1))]
            cipher = []
            for text in plaintext:
                for k in key:
                    k = truncKey(k)
                    # Splitting
                    left = k[0:28]
                    right = k[28:56]

                    rkb = []
                    for i in range(0, 16):
                        if i in [0, 1, 8, 15]:
                            t = 1
                        else:
                            t = 2
                        left = shift_left(left, t)
                        right = shift_left(right, t)

                        combine_str = left + right

                        round_key = compPermutation(combine_str)

                        rkb.append(round_key)

                    text = bin2hex(DesEncrypt(text, rkb[::-1], inv_ip, False))
                cipher.append(text)
            temp = []
            for i in cipher:
                for j in range(int(len(i) / 2)):
                    temp.append(int((i[j * 2] + i[j * 2 + 1]), 16))
            gC.append(temp)
        # 333333333333333333333333333333333333333333333
        for q in range(len(b)):
            plaintext = []
            hextext = b[q]
            for i in range(int(len(hextext) / 8)):
                temp = ""
                for j in range(8):
                    t = hextext[i * 8 + j]
                    if len(t) != 2:
                        t = '0' + t
                    temp += t
                    temp = temp.upper()
                plaintext.append(temp)

            key = [hex2bin(hexKey(key2)), hex2bin(hexKey(key2)), hex2bin(hexKey(key1))]
            cipher = []
            for text in plaintext:
                for k in key:
                    k = truncKey(k)
                    # Splitting
                    left = k[0:28]
                    right = k[28:56]

                    rkb = []
                    for i in range(0, 16):
                        if i in [0, 1, 8, 15]:
                            t = 1
                        else:
                            t = 2
                        left = shift_left(left, t)
                        right = shift_left(right, t)

                        combine_str = left + right

                        round_key = compPermutation(combine_str)

                        rkb.append(round_key)

                    text = bin2hex(DesEncrypt(text, rkb[::-1], inv_ip, False))
                cipher.append(text)
            temp = []
            for i in cipher:
                for j in range(int(len(i) / 2)):
                    temp.append(int((i[j * 2] + i[j * 2 + 1]), 16))
            bC.append(temp)
        # 444444444444444444444444444444444444444444444

        cipher = []
        for i in range(len(rC)):
            temp = []
            for j in range(len(rC[i])):
                if rC[i][j]!=imageConst:
                    temp.append([rC[i][j], gC[i][j], bC[i][j]])
            cipher.append(temp)
        return np.array(cipher)

    else:
        plaintext=[]
        for i in range(int(len(ciphertext) / 16)):
            temp = ""
            for j in range(16):
                temp += ciphertext[i*16+j]
            plaintext.append(temp)

        key = [hex2bin(hexKey(key3)), hex2bin(hexKey(key2)), hex2bin(hexKey(key1))]
        cipher = ""
        for text in plaintext:
            for k in key:
                k = truncKey(k)
                # Splitting
                left = k[0:28]
                right = k[28:56]

                rkb = []
                for i in range(0, 16):
                    if i in [0, 1, 8, 15]:
                        t = 1
                    else:
                        t = 2
                    left = shift_left(left, t)
                    right = shift_left(right, t)

                    combine = left + right

                    round_key = compPermutation(combine)

                    rkb.append(round_key)

                text = bin2hex(DesEncrypt(text, rkb[::-1], inv_ip, False))
            cipher += text

        out=""
        for i in range(int(len(cipher)/2)):
            a=(cipher[i*2]+cipher[i*2+1])
            out+=chr(int(a,16))
        plaintext=""
        for i in out:
            if i!='~':
                plaintext+=i
        return plaintext

def DesEncrypt(plaintext, rkb, ip, en, inspect=False):
    plaintext = hex2bin(plaintext)

    if en:
        plaintext = permuteM(plaintext, ip, 64)
    rounds=[]
    left = plaintext[0:32]
    right = plaintext[32:64]
    for i in range(0, 16):
        right_expanded = permute(right, expansion_table, 48)

        xor_x = xor(right_expanded, rkb[i])

        sbox_str = ""
        for j in range(0, 8):
            row = bin2dec(int(xor_x[j * 6] + xor_x[j * 6 + 5]))
            col = bin2dec(int(xor_x[j * 6 + 1] + xor_x[j * 6 + 2] + xor_x[j * 6 + 3] + xor_x[j * 6 + 4]))
            val = sbox[j][row][col]
            sbox_str = sbox_str + dec2bin(val)

        sbox_str = permute(sbox_str, per, 32)

        result = xor(left, sbox_str)
        left = result

        if (i != 15):
            left, right = right, left
        if inspect:
            rounds.append(hex(int(left+right,2))[2:])
    combine = left + right
    if en:
        if inspect:
            rounds.append(hex(int(left+right,2))[2:])
            return rounds
        return combine
    else:
        if inspect:
            rounds.append(hex(int(permuteM(combine, ip, 64),2))[2:])
            return rounds
        return permuteM(combine, ip, 64)

keys = np.load("AESKeys.npy")
imageToEncrypt = Image.open("Images/Image0.png").convert("RGB").__array__()
key1=keys[0]
key2=keys[1]
key3=keys[2]
img = Image.fromarray(np.array(np.uint8(imageToEncrypt)))
img.show()
enc=TDEA_Encrypt(False, imageToEncrypt, key1, key2, key3, TDEA_ip)
img = Image.fromarray(np.array(np.uint8(enc)))
img.show()
dec=TDEA_Decrypt(False, enc, key1, key2, key3, TDEA_invIp)
img = Image.fromarray(np.array(np.uint8(dec)))
img.show()






