from functools import reduce
import aocd

DAY, YEAR = 5, 2020
ids = []


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [l for l in data]

    return data


def bin(begin, end, mapping, string, stop_at):
    mid = (begin + end) // 2

    if len(string) == 1:
        return begin if string[0] == mapping[0] else end
    elif string[0] == mapping[0]:
        return bin(begin, mid, mapping, string[1:], stop_at)
    elif string[0] == mapping[1]:
        return bin(mid + 1, end, mapping, string[1:], stop_at)


def first():
    global ids

    def id(r, c):
        return r * 8 + c

    for d in data():
        r = bin(0, 127, ('F', 'B'), d[:-3], 4)
        c = bin(0, 7, ('L', 'R'), d[-3:], 1)
        ids.append(id(r, c))

    return max(ids), ids


def second():
    global ids
    return set(i for i in range(min(ids), max(ids) + 1)).difference(set(ids)).pop()


aocd.submit(first()[0], part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
