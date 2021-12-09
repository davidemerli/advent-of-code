data = [[int(x) for x in line.strip()] for line in open("input.txt").readlines()]

dirs = [(1, 0), (0, 1), (-1, 0), (0, -1)]


def neighbours(x, y):

    return [
        (x + dx, y + dy)
        for dx, dy in dirs
        if 0 <= x + dx < len(data) and 0 <= y + dy < len(data[0])
    ]


def is_lowpoint(x, y):
    return all(data[x][y] < data[n[0]][n[1]] for n in neighbours(x, y))


def part1():
    return sum(
        data[x][y] + 1
        for x in range(len(data))
        for y in range(len(data[0]))
        if is_lowpoint(x, y)
    )


def part2():
    low_points = [
        (x, y)
        for x in range(len(data))
        for y in range(len(data[0]))
        if is_lowpoint(x, y)
    ]

    print(len(low_points))


# print(part1())
print(part2())
