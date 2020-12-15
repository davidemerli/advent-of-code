import aocd
from collections import OrderedDict

# TODO: refactor with complex numbers

DAY, YEAR = 12, 2020
dirs = OrderedDict(E=(1, 0), N=(0, 1), W=(-1, 0), S=(0, -1))


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [(l[0], int(l[1:])) for l in data]
    return data


def first():
    facing = dirs['E']
    curr = (0, 0)

    def rotate(where, units):
        units = units//90 % 4
        vectors = list(dirs.values())
        index = vectors.index(facing)
        new_index = index + units * (1 if where == 'L' else -1)
        return vectors[new_index % 4]

    for inst in data():
        label, units = inst

        if label in ['R', 'L']:
            facing = rotate(label, units)
            continue

        if label in dirs:
            vect = dirs[label]
        elif label == 'F':
            vect = facing

        curr = tuple(a + b * units for a, b in zip(curr, vect))

    return sum(abs(v) for v in curr)


def second():
    waypoint = (10, 1)
    curr = (0, 0)

    def rotate(where, degrees):
        units = degrees//90 % 4
        units = 4 - units if where == 'L' else units
        new_waypoint = waypoint

        for _ in range(units):
            new_waypoint = new_waypoint[1], -new_waypoint[0]

        return new_waypoint

    for inst in data():
        label, units = inst

        if label in ['R', 'L']:
            waypoint = rotate(label, units)
            continue
        if label == 'F':
            curr = tuple(a + b * units for a, b in zip(curr, waypoint))
        elif label in dirs:
            vect = dirs[label]
            waypoint = tuple(a + b * units for a, b in zip(waypoint, vect))

    return sum(abs(v) for v in curr)


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
