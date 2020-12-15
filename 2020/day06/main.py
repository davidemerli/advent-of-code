from functools import reduce
import aocd

DAY, YEAR = 6, 2020


def data():
    return aocd.get_data(day=DAY, year=YEAR).split('\n\n')


def first():
    return sum(len(s) for s in [set(''.join([p.strip() for p in group])) for group in data()])


def second():
    # make every person answers into a set of letters (results in list of lists of sets)
    to_sets = [[set(p) for p in group.splitlines()] for group in data()]
    # reduce every group to a single set doing intersections, length of that set is the number of common questions answered yes
    return sum(len(s) for s in [reduce(lambda x, y: x.intersection(y), group) for group in to_sets])


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
