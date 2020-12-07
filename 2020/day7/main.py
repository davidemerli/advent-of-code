import re
import aocd

DAY, YEAR = 7, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()

    def bag_name(line):
        return ' '.join(line.split(' ')[:2])

    def inside_bags(line):
        return [re.match(r'(\d) ([\w ]+) bag[s]{0,1}', r).groups() for r in re.findall(r'\d [\w ]+ bag[s]{0,1}', line)]

    data = [(bag_name(line), inside_bags(line)) for line in data]
    data = {d[0]: {dd[1]: int(dd[0]) for dd in d[1]} for d in data}

    return data


def first():
    bags = data()

    def contains_gold(bag):
        return True if 'shiny gold' in bags[bag] else any(contains_gold(b) for b in bags[bag].keys())

    return len([bag for bag in bags.keys() if contains_gold(bag)])


def second():
    bags = data()

    def count(bag):
        return sum(bags[bag].values()) + sum([count(k)*v for k, v in bags[bag].items()])

    return count('shiny gold')


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
