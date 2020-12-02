import aocd

DAY, YEAR = 1, 2020


def data():
	data = aocd.get_data(day=DAY, year=YEAR).splitlines()
	data = [int(l) for l in data]

	return data


def first():
	for a in data():
		for b in data():
			if a + b == 2020:
				return a * b


def second():
	for a in data():
		for b in data():
			for c in data():
				if a + b + c == 2020:
					return a * b * c

aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
