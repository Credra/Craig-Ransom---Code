import numpy


def dbscan(D, eps, MinPts):
    labels = [0] * len(D)
    C = 0
    numLabs=[0]

    for P in range(0, len(D)):
        if not (labels[P] == 0):
            continue

        NeighborPts = region_query(D, P, eps)

        if len(NeighborPts) < MinPts:
            labels[P] = -1
        else:
            C += 1
            numLabs.append(0)
            grow_cluster(D, labels, P, NeighborPts, C, eps, MinPts,numLabs)

    return labels, numLabs

#make neighbour points a thing that cAN ONLY HAVE UNIQUE DATA

def grow_cluster(D, labels, P, NeighborPts, C, eps, MinPts,numLabs):
    labels[P] = C
    i = 0
    while i < len(NeighborPts):

        Pn = NeighborPts[i]

        if labels[Pn] == -1:
            labels[Pn] = C
            numLabs[C]+=1

        elif labels[Pn] == 0:
            labels[Pn] = C
            numLabs[C] += 1

            PnNeighborPts = region_query(D, Pn, eps)

            if len(PnNeighborPts) >= MinPts:
                NeighborPts = NeighborPts + PnNeighborPts
        i += 1

def region_query(D, P, eps):
    neighbors = []
    for Pn in range(0, len(D)):
        if numpy.linalg.norm(D[P] - D[Pn]) < eps:
            neighbors.append(Pn)

    return neighbors


def dbscan(D, eps, MinPts):
    labels = [0] * len(D)
    C = 0
    numLabs=[0]

    for P in range(0, len(D)):
        if not (labels[P] == 0):
            continue

        NeighborPts = region_query(D, P, eps)

        if len(NeighborPts) < MinPts:
            labels[P] = -1
        else:
            C += 1
            numLabs.append(0)
            grow_cluster(D, labels, P, NeighborPts, C, eps, MinPts,numLabs)

    return labels, numLabs

#make neighbour points a thing that cAN ONLY HAVE UNIQUE DATA

def grow_cluster(D, labels, P, NeighborPts, C, eps, MinPts,numLabs):
    labels[P] = C
    i = 0
    while i < len(NeighborPts):

        Pn = NeighborPts[i]

        if labels[Pn] == -1:
            labels[Pn] = C
            numLabs[C]+=1

        elif labels[Pn] == 0:
            labels[Pn] = C
            numLabs[C] += 1

            PnNeighborPts = region_query(D, Pn, eps)

            if len(PnNeighborPts) >= MinPts:
                NeighborPts = NeighborPts + PnNeighborPts
        i += 1

def region_query(D, P, eps):
    neighbors = []
    for Pn in range(0, len(D)):
        if numpy.linalg.norm(D[P] - D[Pn]) < eps:
            neighbors.append(Pn)

    return neighbors




"""
import numpy as np

class DBSCAN:
    def __init__(self, DB, eps, minPts):
        self.eps = eps
        self.minPts = minPts
        self.DB = DB
        self.label = np.zeros(len(DB))

    def predict(self, DB):
        self.DB = DB
        length = len(DB)
        self.label = np.zeros(length)
        pos=0
        C = 1
        for Q in DB:
            if self.label[pos]==0:
                N = self.rangeQuery(Q)

                if len(N) < self.minPts:
                    self.label[pos]=-1
                else:
                    C = C+1
                    self.label[pos] = C

                    SeedSet = N.remove([Q,pos])
                    for Qa in SeedSet:
                        if self.label[Qa[1]] == -1:
                            self.label[Qa[1]] = C

                        elif self.label[Qa[1]] != 0:
                            self.label[Qa[1]] = C
                            N = self.rangeQuery(Qa)
                            np.unio
            pos+=1

    def rangeQuery(self, Q):
        N = []
        pos=0
        for P in self.DB:
            if ((Q[0]-P[0])**2+(Q[1]-P[1])**2)**0.5 <= self.eps:
                N.append([Q,pos])
            pos+=1
        return N
"""