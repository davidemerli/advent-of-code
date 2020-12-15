import sys
sys.path.insert(0, '../utils')

from bootcode import BootcodeMachine
import aocd

DAY, YEAR = 8, 2020


def data():
    return aocd.get_data(day=DAY, year=YEAR).splitlines()


def first():
    executor = BootcodeMachine(data())
    exit_code = executor.execute(detect_loop=True)

    assert exit_code == 2

    return executor.accumulator


def second():
    program = data()

    for index, inst in enumerate(program):
        fixed_program = program[:]

        if 'nop' in inst:
            fixed_program[index] = inst.replace('nop', 'jmp')
        elif 'jmp' in inst:
            fixed_program[index] = inst.replace('jmp', 'nop')

        executor = BootcodeMachine(fixed_program)
        exit_code = executor.execute(detect_loop=True)

        if exit_code == 0:
            return executor.accumulator


aocd.submit(first(), part='a', day=DAY, year=YEAR)
aocd.submit(second(), part='b', day=DAY, year=YEAR)
