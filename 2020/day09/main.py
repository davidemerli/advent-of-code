import aocd

DAY, YEAR = 9, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = list(map(int, data))
    return data


def first():
    nums, w = data(), 25
    return [n for i, n in enumerate(nums) if i >= w and n not in [j+k for j in nums[i-w:i] for k in nums[i-w:i]]][0]


def second():
    nums, invalid = data(), first()

    sum, start = 0, 0

    for i, a in enumerate(nums):
        sum += a

        if sum == invalid:
            return max(nums[start:i+1]) + min(nums[start:i+1])

        while sum > invalid:
            sum -= nums[start]
            start += 1


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
