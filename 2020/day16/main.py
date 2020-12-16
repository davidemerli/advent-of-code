from functools import reduce
import aocd
import re

DAY, YEAR = 16, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).split('\n\n')

    fields = {t[0]: [list(map(int, t[1:3])), list(map(int, t[3:5]))] for t in re.findall(r'([\w ]+): (\d+)-(\d+) or (\d+)-(\d+)', data[0])}
    my_ticket = list(map(int, re.findall(r'\d+', data[1])))
    nearby_tickets = [list(map(int, re.findall(r'\d+', d))) for d in data[2].splitlines()]

    return fields, my_ticket, nearby_tickets


def valid(n, fields):
    return any(any([a <= n <= b for a, b in t]) for t in fields.values())


def first():
    fields, _, nearby_tickets = data()

    return sum(sum([n for n in ticket if not valid(n, fields)]) for ticket in nearby_tickets)


def second():
    fields, my_ticket, nearby_tickets = data()

    valid_tickets = [t for t in nearby_tickets if t and all(valid(n, fields) for n in t)]

    order = [[] for _ in range(len(my_ticket))]
    for i, ids in enumerate(zip(*valid_tickets)):
        for field in fields.items():
            name, ranges = field

            if (all(any(a <= id <= b for a, b in ranges) for id in ids)):
                order[i].append(name)

    while not all(len(o) == 1 for o in order):
        found = [o for o in order if len(o) == 1]

        for o in [o for o in order if len(o) != 1]:
            for oo in found:
                if oo[0] in o:
                    o.remove(oo[0])

    order = [o[0] for o in order]
    res = [my_ticket[i] for i, name in enumerate(order) if 'departure' in name]

    return reduce(lambda x, y: x*y, res)


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
