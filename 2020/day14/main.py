from collections import defaultdict
from itertools import product
import aocd
import re

DAY, YEAR = 14, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [l[7:] if 'mask' in l else l for l in data]
    data = [l if len(l) == 36 else list(
        map(int, re.findall(r'\d+', l))) for l in data]
    return data


def first():
    lines = data()
    space = defaultdict(int)

    last_mask = None

    for line in lines:
        if len(line) == 36:
            last_mask = line
            continue

        addr, value = line

        new = [y if y in ['0', '1'] else x for x,
               y in zip('{0:036b}'.format(value), last_mask)]
        new = int(''.join(new), 2)
        space[addr] = new

    return sum(space.values())


def second():
    lines = data()
    space = defaultdict(int)

    last_mask = None

    for line in lines:
        if len(line) == 36:
            last_mask = line
            continue

        addr, value = line

        new = [x if y == '0' else y for x, y in zip('{0:036b}'.format(addr), last_mask)]
        combinations = map(list, product(['0', '1'], repeat=new.count('X')))

        all_addr = [[comb.pop() if c == 'X' else c for c in new] for comb in combinations]

        for a in all_addr:
            space[int(''.join(a), 2)] = value

    return sum(space.values())


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
