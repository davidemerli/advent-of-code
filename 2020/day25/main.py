import aocd


DAY, YEAR = 25, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).splitlines()
    data = list(map(int, data))
    return data


def get_loop_size(public_key):
    subj, value = 7, 1
    counter = 0

    while value != public_key:
        value *= subj
        value %= 20201227

        counter += 1

    return counter


def transform(subj, loop_size):
    value = 1
    for _ in range(loop_size):
        value *= subj
        value %= 20201227
    return value


def first():
    card_key, door_key = data()

    card_loop = get_loop_size(card_key)
    door_loop = get_loop_size(door_key)

    en1 = transform(card_key, door_loop)
    en2 = transform(door_key, card_loop)

    assert en1 == en2

    return en1


aocd.submit(first(), part='a', day=DAY, year=YEAR)
