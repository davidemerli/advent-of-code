data = [
    [list(map(set, part.split())) for part in line.strip().split("|")]
    for line in open("input.txt").readlines()
]


def part1(data):
    return sum(
        sum(1 for y in x if len(y) in [2, 3, 4, 7]) for x in [x[1] for x in data]
    )


def part2(data):
    def find_value(line):
        considered, output = line

        one, seven, four, eight = [
            x for x in sorted(considered, key=len) if len(x) in [2, 3, 4, 7]
        ]

        considered = [x for x in considered if x not in [one, seven, four, eight]]

        def find_with_len(l, length):
            return [i for i, x in enumerate(l) if len(x) == length][0]

        def subtract(l, b):
            return [x - b for x in l]

        three = considered[find_with_len(subtract(considered, one), 3)]
        considered.remove(three)

        six = considered[find_with_len(subtract(considered, seven), 4)]
        considered.remove(six)

        five = considered[find_with_len(subtract(considered, six), 0)]
        considered.remove(five)

        nine = considered[find_with_len(subtract(considered, five), 1)]
        considered.remove(nine)

        two = considered[find_with_len(subtract(considered, three), 1)]
        considered.remove(two)

        zero = considered[0]

        numbers = [zero, one, two, three, four, five, six, seven, eight, nine]

        return int("".join(str(numbers.index(d)) for d in output))

    return sum(find_value(line) for line in data)


print(part1(data))
print(part2(data))
