package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.davidemerli.utils.SettingsReader.*;

public class Day5 {

    private static final List<Operator> operators = new ArrayList<>();
    private static final List<Integer> PROGRAM = new ArrayList<>();

    private static int SYSTEM_ID;

    private abstract static class Operator {
        final int opcode;

        Operator(int opcode) {
            this.opcode = opcode;

            operators.add(this);
        }

        abstract int apply(List<Integer> program, int programCounter, int... args);
    }

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day5.txt")).get(0);
        Arrays.stream(s.split(",")).map(Integer::parseInt).forEach(PROGRAM::add);

        firstPart();

        PROGRAM.clear();
        Arrays.stream(s.split(",")).map(Integer::parseInt).forEach(PROGRAM::add);

        secondPart();
    }

    private static void firstPart() throws IOException {
        SYSTEM_ID = 1;

        new Operator(1) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                program.set(args[5], args[0] + args[1]);

                return programCounter + 4;
            }
        };

        new Operator(2) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                program.set(args[5], args[0] * args[1]);

                return programCounter + 4;
            }
        };

        new Operator(3) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                program.set(args[3], SYSTEM_ID);

                return programCounter + 2;
            }
        };

        new Operator(4) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                System.out.print(args[0]);
                return programCounter + 2;
            }
        };

        System.out.print("result1: ");
        processInput(PROGRAM);
        System.out.println();
    }

    private static void secondPart() {
        SYSTEM_ID = 5;

        new Operator(5) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                if (args[0] != 0) return args[1];

                return programCounter + 3;
            }
        };

        new Operator(6) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                if (args[0] == 0) return args[1];

                return programCounter + 3;
            }
        };

        new Operator(7) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                program.set(args[5], args[0] < args[1] ? 1 : 0);

                return programCounter + 4;
            }
        };

        new Operator(8) {
            @Override
            int apply(List<Integer> program, int programCounter, int... args) {
                program.set(args[5], args[0] == args[1] ? 1 : 0);

                return programCounter + 4;
            }
        };

        System.out.print("result2: ");
        processInput(PROGRAM);
        System.out.println();
    }

    private static Operator getOperatorFromCode(int opcode) {
        return operators.stream()
                .filter(operator -> operator.opcode == opcode)
                .findFirst()
                .orElse(null);
    }

    private static void processInput(List<Integer> program) {
        int programCounter = 0;

        while (true) {
            StringBuilder sb = new StringBuilder("" + program.get(programCounter));
            for (int j = 0; j < 5 - ("" + program.get(programCounter)).length(); j++) {
                sb.insert(0, "0");
            }

            String code = sb.toString();
            int opcode = Integer.parseInt(code.substring(3));

            if (opcode == 99) break;

            int a1 = programCounter < program.size() ? program.get(programCounter + 1) : 0;
            int a2 = programCounter < program.size() ? program.get(programCounter + 2) : 0;
            int a3 = programCounter < program.size() ? program.get(programCounter + 3) : 0;

            int v1 = code.charAt(2) == '0' ? (a1 < program.size() ? program.get(a1) : 0) : a1;
            int v2 = code.charAt(1) == '0' ? (a2 < program.size() ? program.get(a2) : 0) : a2;
            int v3 = code.charAt(0) == '0' ? (a3 < program.size() ? program.get(a3) : 0) : a3;

            Operator o = getOperatorFromCode(opcode);

            if (o != null) {
                programCounter = o.apply(program, programCounter, v1, v2, v3, a1, a2, a3);
            }
        }
    }
}
