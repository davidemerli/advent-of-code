from collections import defaultdict
from copy import deepcopy
from tqdm import tqdm
import aocd
import re


DAY, YEAR = 24, 2020


dirs = dict(w=(-1, 1, 0), nw=(0, 1, -1), ne=(1, 0, -1), e=(1, -1, 0), se=(0, -1, 1), sw=(-1, 0, 1))


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = [re.findall(r'e|se|sw|w|nw|ne', l) for l in data]
    return data


def first():
    board = defaultdict(bool)

    for line in data():
        curr = (0, 0, 0)

        for move in line:
            curr = tuple(p + v for p, v in zip(curr, dirs[move]))

        board[curr] = not board[curr]

    return sum(1 for flipped in board.values() if flipped), board


def second():
    _, board = first()

    for _ in tqdm(range(100), ncols=60):
        board_copy = deepcopy(board)

        for p in board.keys():
            neighs = [tuple(c1 + c2 for c1, c2 in zip(p, d)) for d in dirs.values()]

            for n in neighs:
                board_copy[n] = board_copy[n]

        board = board_copy
        board_copy = deepcopy(board)
        new_board = deepcopy(board)

        for p, flipped in board.items():
            neighs = [tuple(c1 + c2 for c1, c2 in zip(p, d)) for d in dirs.values()]
            neighs = [board_copy[coords] for coords in neighs]

            num_flipped = sum(1 for n in neighs if n)

            new_board[p] = flipped

            if flipped:
                if num_flipped == 0 or num_flipped > 2:
                    new_board[p] = False
            else:
                if num_flipped == 2:
                    new_board[p] = True

        board = new_board

    return sum(1 for flipped in board.values() if flipped)


aocd.submit(first()[0], part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
