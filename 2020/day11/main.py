import aocd
import numpy as np


DAY, YEAR = 11, 2020
dirs = [(-1, 0), (-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1)]


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    return np.array([list(d.strip()) for d in data])


def normal_neighs(p, matrix):
    def clamp(x, low, high):
        return max(low, min(x, high))

    width, height = matrix.shape

    neighs = set((clamp(p[0] + v[0], 0, width - 1), clamp(p[1] + v[1], 0, height - 1)) for v in dirs)
    neighs.discard(p)

    return neighs


def count_seats(neighs, threshold):
    matrix = data()

    while True:
        status = matrix.copy()

        with np.nditer(matrix, flags=['multi_index']) as it:
            for c in it:
                x, y = it.multi_index

                if c == 'L':
                    if [matrix[p] for p in neighs((x, y), matrix)].count('#') == 0:
                        c = '#'
                elif c == '#':
                    if [matrix[p] for p in neighs((x, y), matrix)].count('#') >= threshold:
                        c = 'L'
                status[x, y] = c

        if (status == matrix).all():
            return len(np.where(matrix == '#')[0])

        matrix = status


def see_first_neighs(p, matrix):
    neighs = set()

    for d in dirs:
        curr = p

        while True:
            curr = curr[0] + d[0], curr[1] + d[1]

            if all(0 <= c < limit for c, limit in zip(curr, matrix.shape)):
                if matrix[curr] != '.':
                    neighs.add(curr)
                    break
            else:
                break

    return neighs


def first():
    return count_seats(normal_neighs, 4)


def second():
    return count_seats(see_first_neighs, 5)


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
