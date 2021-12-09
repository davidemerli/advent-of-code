import re

data = open("input.txt").read().split("\n\n")
extractions = [int(x) for x in data[0].split(",")]


boards = [
    [[int(el) for el in re.findall(r"\d+", row)] for row in board.strip().split("\n")]
    for board in data[1:]
]


def board_win(board, marked):
    for row in board:
        row_win = True

        for el in row:
            if el not in marked:
                row_win = False

        if row_win:
            return True

    for i in range(len(board[0])):
        column_win = True

        for row in board:
            if row[i] not in marked:
                column_win = False

        if column_win:
            return True

    return False


def board_score(board, marked):
    unmarked = sum(sum(el for el in row if el not in marked) for row in board)

    return unmarked * marked[-1]


def part1(extractions, boards):
    for i in range(len(extractions)):
        for board in boards:
            if board_win(board, extractions[:i]):
                return board_score(board, extractions[:i])


def part2(extractions, boards):
    for i in range(len(extractions)):
        for board in boards:
            if board_win(board, extractions[:i]):
                if len(boards) == 1:
                    return board_score(board, extractions[:i])
                else:
                    boards.remove(board)


print(part1(extractions, boards))
print(part2(extractions, boards))
