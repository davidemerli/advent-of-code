package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day21 {

    private static List<Long> PROGRAM = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day21.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        String program = "OR A T\n" +
                "AND B T\n" +
                "AND C T\n" +
                "NOT T J\n" +
                "AND D J\n" +
                "WALK\n";

        System.out.println("result1:" + runComputer(program));
    }

    private static void secondPart() {
        String program = "OR A T\n" +
                "AND B T\n" +
                "AND C T\n" +
                "NOT T J\n" +
                "AND D J\n" +
                "OR E T\n" +
                "OR H T\n" +
                "AND T J\n" +
                "RUN\n";

        System.out.println("result2:" + runComputer(program));
    }

    private static long runComputer(String boolProgram) {
        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Queue<Integer> q = boolProgram.chars().boxed().collect(Collectors.toCollection(LinkedList::new));

        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.status == Status.PAUSED && !q.isEmpty()) {
                long val = (long) q.poll();
                computer.input = Optional.of(val);
                computer.status = Status.RUNNING;
            }

            if (computer.output.isPresent()) {
                if (computer.output.get() > 200) {
                    return computer.output.get();
                }

                computer.output = Optional.empty();
            }
        }

        return -1;
    }
}