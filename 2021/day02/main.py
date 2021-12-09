import re

data = open("input.txt", "r").readlines()
data = [re.match("(\w+)\s(\d+)", line).groups() for line in data]


def part1(data):
    # (horizontal, depth)
    horiz, depth = 0, 0
    directions = dict(forward=(1, 0), up=(0, -1), down=(0, 1))

    data = [list(map(lambda x: x * int(d[1]), directions[d[0]])) for d in data]

    for d in data:
        horiz += d[0]
        depth += d[1]

    return horiz * depth


def part2(data):
    # (horizontal, depth, aim)
    horiz, depth, aim = 0, 0, 0

    zero = lambda x: 0
    plus = lambda x: x + 1
    minus = lambda x: x - 1
    mult_aim = lambda x: x * aim

    directions = dict(
        forward=(plus, mult_aim, zero),
        up=(zero, zero, minus),
        down=(zero, zero, plus),
    )

    for d in data:
        val = int(d[1])
        horiz += directions[d[0]][0](val)
        depth += directions[d[0]][1](val)
        aim += directions[d[0]][2](val)

    return horiz * depth


print(part1(data))
print(part2(data))
