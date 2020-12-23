from functools import reduce
import aocd
import re

DAY, YEAR = 21, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [re.match(r'(.+) (\(.+\))', l).groups() for l in data]
    data = [(set(i.split(' ')), a[10:-1].split(', ')) for i, a in data]

    return data


def first():
    allergens = {}

    ingredients = []

    for i, a in data():
        ingredients += i

        for aa in a:
            if aa not in allergens:
                allergens[aa] = set(i)
            else:
                allergens[aa] &= i

    with_allergens = reduce(set.union, allergens.values())

    return sum(1 for i in ingredients if i not in with_allergens), allergens


def second():
    _, allergens = first()

    def get_assigned():
        return {k: list(v)[0] for k, v in allergens.items() if len(v) == 1}

    while True:
        assigned = get_assigned()

        if len(allergens) == len(assigned):
            break

        for k in [a for a in allergens.keys() if a not in assigned]:
            allergens[k] = allergens[k].difference(assigned.values())

    return ','.join([list(v)[0] for _, v in sorted(allergens.items())])


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
