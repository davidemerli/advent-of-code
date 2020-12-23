import aocd

DAY, YEAR = 22, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).split('\n\n')
    # data = open('in').read().split('\n\n')
    return [list(map(int, d.splitlines()[1:])) for d in data]


def first():
    p1, p2 = data()

    counter = 0
    while len(p1) != 0 and len(p2) != 0:
        counter += 1
        print(counter)
        c1, c2 = p1.pop(0), p2.pop(0)

        if c1 > c2:
            p1 += [c1, c2]
        else:
            p2 += [c2, c1]

        print(p1, p2)

    winner = p2 if len(p1) == 0 else p1

    return sum((i + 1) * val for i, val in enumerate(winner[::-1]))


aocd.submit(first(), part='a', day=DAY, year=YEAR)
# aocd.submit(second(), part='b', day=DAY, year=YEAR)
