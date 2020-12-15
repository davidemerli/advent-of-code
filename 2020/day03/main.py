from functools import reduce
import aocd

DAY, YEAR = 3, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [l for l in data]

    return data


def count_trees(slope):
    current = (0, 0)
    counter = 0
    map = data()

    while current[1] != len(data()) - 1:
        current = (current[0] + slope[0], current[1] + slope[1])

        if map[current[1]][current[0] % 31] == '#':
            counter += 1

    return counter


def first():
    return count_trees((3, 1))


def second():
    slopes = [(1, 1), (3, 1), (5, 1), (7, 1), (1, 2)]

    return reduce(lambda a, b: a * b, map(lambda x: count_trees(x), slopes))


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
