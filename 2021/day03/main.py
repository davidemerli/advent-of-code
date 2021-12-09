data = open("input.txt", "r").readlines()
data = [[int(digit) for digit in row.strip()] for row in data]


def most_common(index, data):
    occurences = sum(row[index] for row in data)

    return 1 if occurences >= len(data) / 2 else 0


def invert(num):
    return [0 if digit == 1 else 1 for digit in num]


def multiply(a, b):
    a = int("".join(str(digit) for digit in a), 2)
    b = int("".join(str(digit) for digit in b), 2)
    print(a)
    return a * b


def part1(data):
    epsilon = [most_common(col, data) for col in range(len(data[0]))]
    gamma = invert(epsilon)
    print(epsilon)
    return multiply(epsilon, gamma)


def part2(data):
    def find(data, most=True):
        copy = data[::]

        for col in range(len(copy[0])):
            most_common_digit = int(most) if most_common(col, copy) else int(not most)

            copy = [row for row in copy if row[col] == most_common_digit]

            if len(copy) == 1:
                break

        return copy[0]

    oxigen = find(data, most=True)
    scrubber = find(data, most=False)

    return multiply(oxigen, scrubber)


print(part1(data))
print(part2(data))
