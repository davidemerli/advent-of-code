data = [int(x) for x in open("input.txt").readlines()]

part1 = sum(1 for x in range(0, len(data) - 1) if data[x] < data[x + 1])

part2 = sum(
    1
    for x in range(0, len(data) - 1)
    if sum(data[x : x + 3]) < sum(data[x + 1 : x + 4])
)

print(part1)
print(part2)
