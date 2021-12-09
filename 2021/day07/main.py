from functools import cache

data = [int(x) for x in open("input.txt").read().split(",")]

min_pos, max_pos = min(data), max(data)


def part1():
    @cache
    def get_total_cost(x):
        return sum(abs(d - x) for d in data)

    res = min(range(min_pos, max_pos + 1), key=get_total_cost)

    return get_total_cost(res)


def part2():
    @cache
    def sum_until(n):
        return sum(range(n + 1))

    @cache
    def get_total_cost(x):
        return sum(sum_until(abs(d - x)) for d in data)

    res = min(range(min_pos, max_pos + 1), key=get_total_cost)

    return get_total_cost(res)


print(part1())
print(part2())
