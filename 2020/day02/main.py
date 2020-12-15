from collections import Counter
import aocd
import re


DAY, YEAR = 2, 2020


def data():
    # line example: '1-3 a: abcde'

    def parse(l):
        g = re.match(r'(\d+)-(\d+) (\w): (\w+)', l).groups()
        return int(g[0]), int(g[1]), g[2], g[3]

    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [parse(l) for l in data]

    return data


def first():
    return len([pwd for min, max, c, pwd in data() if min <= Counter(pwd)[c] <= max])


def second():
    return len([pwd for min, max, c, pwd in data() if bool(pwd[min - 1] == c) ^ bool(pwd[max - 1] == c)])


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
