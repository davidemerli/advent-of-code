import aocd
import re

DAY, YEAR = 4, 2020


def data():
    data = aocd.get_data(day=DAY, year=YEAR).split('\n\n')
    data = [d.replace('\n', ' ') for d in data]
    data = [{k:int(v) if 'y' in k else v for k, v in [s.split(':') for s in d.split(' ')]} for d in data]
    return data


def is_valid(dict):
    fields = ['byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid']
    return all([f in dict for f in fields])


def first():
    return len([d for d in data() if is_valid(d)])


def second():
    def valid_years(p):
        return 1920 <= p['byr'] <= 2002 and 2010 <= p['iyr'] <= 2020 and 2020 <= p['eyr'] <= 2030

    def valid_hgt(p):
        return 150 <= int(p['hgt'][:-2]) <= 193 if 'cm' in p['hgt'] else 59 <= int(p['hgt'][:-2]) <= 76 if 'in' in p['hgt'] else False

    def valid_hcl(p):
        return re.match(r'#[0-9a-f]{6}', p['hcl'])

    def valid_ecl(p):
        return p['ecl'] in ['amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth']

    def valid_pid(p):
        return re.fullmatch(r'\d{9}', p['pid'])

    return len([p for p in data() if is_valid(p) and valid_years(p) and valid_hgt(p) and valid_hcl(p) and valid_ecl(p) and valid_pid(p)])


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
