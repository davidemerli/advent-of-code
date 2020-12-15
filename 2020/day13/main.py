import aocd


DAY, YEAR = 13, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    return int(data[0]), data[1].split(',')


def first():
    time, buses = data()
    buses = [int(b) for b in buses if b.isnumeric()]
    buses = [(b, b - (time - time//b*b)) for b in buses]

    best = sorted(buses, key=lambda x: x[1])[0]
    return best[0] * best[1]


def second():
    time, buses = data()
    time = 0
    prod = 1

    for i, bus in enumerate(buses):
        if bus != 'x':
            while((time + i) % int(bus) != 0):
                time += prod
            prod *= int(bus)
    return time


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
