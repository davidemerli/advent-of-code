import aocd

DAY, YEAR = 10, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = list(map(int, data))

    data = [0] + sorted(data)
    data.append(data[-1] + 3)
    return data


def first():
    adapters = data()
    diffs = [b - a for a, b in zip(adapters, adapters[1:])]

    return diffs.count(1) * diffs.count(3)


def second():
    adapters = data()
    combinations = dict()

    last = adapters.pop()
    combinations[last] = 1

    for a in reversed(adapters):
        total = 0

        for i in range(1, 4):
            if a + i in combinations:
                total += combinations[a + i]

        combinations[a] = total

    return combinations[0]


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
