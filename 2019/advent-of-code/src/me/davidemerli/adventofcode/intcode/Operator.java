package me.davidemerli.adventofcode.intcode;

import java.util.List;

abstract class Operator {
    final int opcode;
    final int arity;

    Operator(int opcode, int arity) {
        this.opcode = opcode;
        this.arity = arity;
    }

    abstract int apply(List<Long> program, String code, int programCounter, long... args);
}