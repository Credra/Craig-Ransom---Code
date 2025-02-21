import numpy as np

class KF:
    def __init__(self, xInit, yInit, zInit, vxInit, vyInit, vzInit, xNoise, yNoise, zNoise, systemNoise , bounceCoef):
        self.x = np.array([[xInit], [yInit], [zInit], [vxInit], [vyInit], [vzInit]])
        self.P = np.diag([1,1,1,1,1,1])*1e6
        self.R = np.diag([xNoise, yNoise, zNoise])
        self.systemNoise = systemNoise
        self.H = np.array([
            [1, 0, 0, 0, 0, 0],
            [0, 1, 0, 0, 0, 0],
            [0, 0, 1, 0, 0, 0]
        ])
        self.HT = np.transpose(self.H)
        self.bounceCoef = bounceCoef
        self.bounced=False

    # delta is the change in time from the last measurement
    def predict(self, delta):
        #used indicate if a bounce was predicted
        self.bounced = False
        #covariance of the process noise which is depended on delta
        Q = np.diag([(self.systemNoise * (delta ** 2))*0.5,(self.systemNoise * (delta ** 2))*0.5,
    (self.systemNoise*(delta**2))*0.5,(self.systemNoise*delta),(self.systemNoise*delta),(self.systemNoise*delta)])
        F = np.array([
            [1, 0, 0, delta, 0, 0],
            [0, 1, 0, 0, delta, 0],
            [0, 0, 1, 0, 0, delta],
            [0, 0, 0, 1, 0, 0],
            [0, 0, 0, 0, 1, 0],
            [0, 0, 0, 0, 0, 1]
        ])
        self.x = F @ self.x + [[0],
                          [0.5*delta**2*-9.81],
                          [0],
                          [0],
                          [-9.81*delta],
                          [0]]
        #determine if a bounce has occured
        #y position less than 0, y velocity negative
        if self.x[1]<0 and self.x[4]<0:
            self.x[1]*=-self.bounceCoef
            self.x[4]*=-self.bounceCoef
            #account for the uncertainty introduced by the bounce
            self.P = F @ self.P @ F.T + Q+np.diag([1,1,1,1,1,1])*1000
            self.bounced = True
        else:
            self.P = F @ self.P @ F.T + Q

    def update(self, x, y, z):
        z = np.array([[x], [y], [z]])

        y = z - self.H @ self.x

        S = self.H @ self.P @ self.HT + self.R
        K = (self.P @ self.HT) @ np.linalg.inv(S)

        self.x = self.x + (K @ y)
        self.P = ((np.identity(6) - K @ self.H) @ self.P)
