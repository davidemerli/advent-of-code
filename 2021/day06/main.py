from tqdm import tqdm
from collections import defaultdict

data = open("input.txt").read().split(",")
data = [int(x) for x in data]


def get_after_n_days(data, n):
    copy = data[::]

    for _ in tqdm(range(n)):
        new_fish = 0

        for i, val in enumerate(copy):
            if val == 0:
                copy[i] = 6
                new_fish += 1
            else:
                copy[i] = val - 1

        copy += [8] * new_fish

    return len(copy)


def get_after_n_days_optimized(data, n):
    fish = defaultdict(int, {val: data.count(val) for val in data})

    for _ in tqdm(range(n)):
        new_day = defaultdict(int)

        for k, v in fish.items():
            if k == 0:
                new_day[6] += v
                new_day[8] += v
            else:
                new_day[k - 1] += v

        fish = new_day

    return sum(fish.values())


print(get_after_n_days(data, 80))
print(get_after_n_days_optimized(data, 256))
