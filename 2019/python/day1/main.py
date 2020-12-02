import os
from functools import reduce

INPUT = [int(l) for l in open('data').readlines()]


def first_star():
    pass


def second():
    def single(f):
        return f // 3 - 2

    def cost(f):
        total = 0

        while f > 0:
            cost = single(f)
            if cost > 0:
                total += cost
            
            f = cost
        return total	

    result = sum([cost(f) for f in INPUT])
    return result


if __name__ == "__main__":
    print(second())
