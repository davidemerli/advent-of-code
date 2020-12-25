from collections import defaultdict
from itertools import product
import aocd
import copy
import numpy as np

DAY, YEAR = 17, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    return [list(l) for l in data]


def neighbours(dims):
    neighs = set(product((-1, 0, 1), repeat=dims))
    neighs.discard(tuple([0] * dims))
    return neighs


def first():
    starting = data()

    start_size = len(starting)
    size = start_size + 12

    space = np.full(shape=(size, size, size), fill_value='.')

    for x in range(start_size):
        for y in range(start_size):
            space[size//2 - start_size//2 + x, size//2 - start_size//2 + y, size//2] = starting[x][y]

    def valid(c, p):
        return all(0 <= coord < size for coord in c) and c != p

    for _ in range(6):
        new_space = np.copy(space)

        with np.nditer(space, flags=['multi_index']) as it:
            for val in it:
                x, y, z = it.multi_index

                to_check = [tuple(i + j for i, j in zip(it.multi_index, dir)) for dir in neighbours(3)]

                if val == '#' and len([c for c in to_check if valid(c, it.multi_index) and space[c] == '#']) not in [2, 3]:
                    new_space[x, y, z] = '.'
                elif val == '.' and len([c for c in to_check if valid(c, it.multi_index) and space[c] == '#']) == 3:
                    new_space[x, y, z] = '#'

        space = new_space
        
    return len(np.where(space == '#')[0])
    

def second():
    starting = data()

    start_size = len(starting)
    size = start_size + 12

    space = np.full(shape=(size, size, size, size), fill_value='.')

    for x in range(start_size):
        for y in range(start_size):
            space[size//2 - start_size//2 + x, size//2 - start_size//2 + y, size//2, size//2] = starting[x][y]

    def valid(c, p):
        return all(0 <= coord < size for coord in c) and c != p

    for _ in range(6):
        new_space = np.copy(space)

        with np.nditer(space, flags=['multi_index']) as it:
            for val in it:

                to_check = [tuple(i + j for i, j in zip(it.multi_index, dir)) for dir in neighbours(4)]

                if val == '#' and len([c for c in to_check if valid(c, it.multi_index) and space[c] == '#']) not in [2, 3]:
                    new_space[it.multi_index] = '.'
                elif val == '.' and len([c for c in to_check if valid(c, it.multi_index) and space[c] == '#']) == 3:
                    new_space[it.multi_index] = '#'

        space = new_space
        
    return len(np.where(space == '#')[0])
    
        
aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)   
