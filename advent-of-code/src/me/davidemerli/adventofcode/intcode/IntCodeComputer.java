package me.davidemerli.adventofcode.intcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntCodeComputer {

    private List<Operator> operators = new ArrayList<>();
    private List<Operator> availableOperators = new ArrayList<>();
    private List<Long> program;

    public Optional<Long> input, output;

    private int programCounter = 0;

    public Status status;

    private int relativeBaseOffset = 0;

    public IntCodeComputer(List<Long> program) {
        this.input = Optional.empty();
        this.output = Optional.empty();

        this.program = new ArrayList<>(program);

        for (long i = 0; i < 100000; i++) {
            this.program.add((long) 0);
        }

        status = Status.RUNNING;
    }

    private long resolveArg(String modes, long[] args, int index, boolean isWrite) {
        char mode = index == 0 ? modes.charAt(2) : index == 1 ? modes.charAt(1) : modes.charAt(0);
        long arg = args[index];

        switch (mode) {
            case '0':
                return isWrite ? arg : program.get((int) arg);
            case '1':
                return arg;
            case '2':
                return isWrite ? arg + relativeBaseOffset : program.get((int) (arg + relativeBaseOffset));
            default:
                return 0;
        }
    }

    public void overrideMemory(int where, long arg) {
        this.program.set(where, arg);
    }

    public void processInput() {
        StringBuilder sb = new StringBuilder("" + program.get(programCounter));
        for (int j = 0; j < 5 - ("" + program.get(programCounter)).length(); j++) {
            sb.insert(0, "0");
        }

        String code = sb.toString();
        int opcode = Integer.parseInt(code.substring(3));

        if (opcode == 99) {
            status = Status.STOPPED;
            return;
        }

        Operator o = getOperatorFromCode(opcode);

        long a1 = program.get(programCounter + 1);
        long a2 = program.get(programCounter + 2);
        long a3 = program.get(programCounter + 3);

        if (opcode == 3 && !input.isPresent()) {
            this.status = Status.PAUSED;
            return;
        }

        programCounter = o.apply(program, code, programCounter, a1, a2, a3);
    }

    private Operator getOperatorFromCode(int opcode) {
        return availableOperators.stream()
                .filter(operator -> operator.opcode == opcode)
                .findFirst()
                .orElse(null);
    }

    public void addOperators(int... opcodes) {
        operators.add(new Operator(1, 3) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                program.set((int) resolveArg(code, args, 2, true),
                        resolveArg(code, args, 0, false) + resolveArg(code, args, 1, false));
                return programCounter + 4;
            }
        });

        operators.add(new Operator(2, 3) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                program.set((int) resolveArg(code, args, 2, true),
                        resolveArg(code, args, 0, false) * resolveArg(code, args, 1, false));
                return programCounter + 4;
            }
        });

        operators.add(new Operator(3, 1) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                program.set((int) resolveArg(code, args, 0, true), input.orElse(null));
                input = Optional.empty();

                return programCounter + 2;
            }
        });

        operators.add(new Operator(4, 1) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                output = Optional.of(resolveArg(code, args, 0, false));
                return programCounter + 2;
            }
        });

        operators.add(new Operator(5, 2) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                if (resolveArg(code, args, 0, false) != 0)
                    return (int) resolveArg(code, args, 1, false);
                return programCounter + 3;
            }
        });

        operators.add(new Operator(6, 2) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                if (resolveArg(code, args, 0, false) == 0)
                    return (int) resolveArg(code, args, 1, false);
                return programCounter + 3;
            }
        });

        operators.add(new Operator(7, 3) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                program.set((int) resolveArg(code, args, 2, true),
                        (long) (resolveArg(code, args, 0, false) < resolveArg(code, args, 1, false) ? 1 : 0));
                return programCounter + 4;
            }
        });

        operators.add(new Operator(8, 3) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                program.set((int) resolveArg(code, args, 2, true),
                        (long) (resolveArg(code, args, 0, false) == resolveArg(code, args, 1, false) ? 1 : 0));
                return programCounter + 4;
            }
        });

        operators.add(new Operator(9, 1) {
            @Override
            int apply(List<Long> program, String code, int programCounter, long... args) {
                relativeBaseOffset += (int) resolveArg(code, args, 0, false);//TODO: CHECK
                return programCounter + 2;
            }
        });

        for (int opcode : opcodes) {
            availableOperators.add(operators.stream().filter(operator -> operator.opcode == opcode).findFirst().orElse(null));
        }
    }
}
