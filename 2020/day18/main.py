import aocd


DAY, YEAR = 18, 2020


def parse_to_list(s, i=0):
    result = []
    while i < len(s):
        if s[i] == '(':
            i, r = parse_to_list(s, i+1)
            result.append(r)
        elif s[i] == ')':
            return i+1, result
        else:
            result.append(s[i])
            i += 1
    return i, result


def data():
    return aocd.get_data(day=DAY, year=YEAR).splitlines()


def solve(exp):
    if len(exp) == 1:
        return exp

    if all(type(el) != list for el in exp):
        res = exp.pop(0)

        while len(exp) != 0:
            res = eval(f'{res} {exp.pop(0)} {exp.pop(0)}')

        return res

    res = solve(exp.pop(0))

    while len(exp) != 0:
        res = eval(f'{res} {exp.pop(0)} {solve(exp.pop(0))}')

    return res


class swap():
    def __init__(self, val):
        self.val = val

    def __add__(self, o):
        return swap(self.val * o.val)

    def __mul__(self, o):
        return swap(self.val + o.val)


def first():
    expressions = [parse_to_list(l.replace(' ', ''))[1] for l in data()]

    return sum(solve(e) for e in expressions)


def second():
    def fix(exp):
        return ''.join(['+' if c == '*' else '*' if c == '+' else f'swap({c})' if c.isnumeric() else c for c in exp])

    return sum(eval(fix(e)).val for e in data())


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
