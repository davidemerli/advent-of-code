import aocd

DAY, YEAR = 19, 2020


def data():
    rules, messages = map(str.splitlines, aocd.get_data(day=DAY, year=YEAR).split('\n\n'))

    rules = {int(k): [rule.replace('"', '') for rule in v.split(' | ')] for k, v in [r.split(': ') for r in rules]}
    rules = {k: [[int(vvv) if vvv.isnumeric() else vvv for vvv in vv.split(' ')] for vv in v] for k, v in rules.items()}

    return rules, messages


def match(message, r_list, rule_dict):
    if message == '':
        return len(r_list) == 0

    if not r_list:
        return False

    rule = rule_dict[r_list.pop(0)]

    if len(rule) == 1 and rule[0] in [['a'], ['b']]:
        return message[0] == rule[0][0] and match(message[1:], r_list, rule_dict)

    return any(match(message, sub_rules + r_list, rule_dict) for sub_rules in rule)


def first():
    rules, messages = data()
    return len([m for m in messages if match(m, [0], rules)])


def second():
    rules, messages = data()

    rules[8] = [[42], [42, 8]]
    rules[11] = [[42, 31], [42, 11, 31]]

    return len([m for m in messages if match(m, [0], rules)])


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
