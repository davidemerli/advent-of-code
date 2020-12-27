import aocd
import re

DAY, YEAR = 5, 2020


def data():
    global ids
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    ids = [int(re.sub(r'B|R', '1', re.sub(r'F|L', '0', d)), 2) for d in data]
    return ids


def first():
    return max(data())


def second():
    return set(i for i in range(min(ids), max(ids) + 1)).difference(set(ids)).pop()


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
