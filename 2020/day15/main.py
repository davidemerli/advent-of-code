from collections import defaultdict
import aocd

DAY, YEAR = 15, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR)
    return list(map(int, data.split(',')))


def get_last_number(limit):
    input = data()
    numbers = defaultdict(int, {n: i + 1 for i, n in enumerate(input)})

    c = len(numbers.keys())
    last = input[-1]

    while c < limit:
        new = 0 if last not in numbers else c - numbers[last]
        numbers[last] = c
        last = new
        c += 1

    return last


def first():
    return get_last_number(2020)


def second():
    return get_last_number(30000000)


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
