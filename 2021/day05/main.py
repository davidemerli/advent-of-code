import numpy as np
import re

data = open("input.txt").readlines()
data = [[int(x) for x in re.findall(r"\d+", line)] for line in data]

max_x = max(max(d[0], d[2]) for d in data)
max_y = max(max(d[1], d[3]) for d in data)


def point_in_line(x1, y1, x2, y2, count_diagonal=False):
    if x1 == x2:
        y1, y2 = sorted([y1, y2])
        return [(y, x1) for y in range(y1, y2 + 1)]
    if y1 == y2:
        x1, x2 = sorted([x1, x2])
        return [(y1, x) for x in range(x1, x2 + 1)]

    if not count_diagonal:
        return []

    x2 = x2 + 1 if x2 > x1 else x2 - 1
    y2 = y2 + 1 if y2 > y1 else y2 - 1

    return [
        (y, x)
        for y, x in zip(
            range(y1, y2, -1 if y1 > y2 else 1),
            range(x1, x2, -1 if x1 > x2 else 1),
        )
    ]


def solve(count_diagonal):
    matrix = np.zeros((max_y + 1, max_x + 1))

    for x1, y1, x2, y2 in data:
        points = point_in_line(x1, y1, x2, y2, count_diagonal=count_diagonal)

        for p in points:
            matrix[p] += 1

    return np.sum(matrix > 1)


print(solve(False))
print(solve(True))
