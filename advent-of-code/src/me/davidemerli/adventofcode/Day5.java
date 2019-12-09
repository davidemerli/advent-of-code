package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.davidemerli.utils.SettingsReader.*;

public class Day5 {

    private static final List<Long> PROGRAM = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day5.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        System.out.print("result1: ");
        runComputer(1, 1, 2, 3, 4);
    }

    private static void secondPart() {
        System.out.print("result2: ");
        runComputer(5, 1, 2, 3, 4, 5, 6, 7, 8);
    }

    private static void runComputer(int systemID, int... operators) {
        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(operators);

        computer.input = Optional.of((long) systemID);

        while (computer.status != Status.STOPPED) {
            if (computer.output.isPresent()) {
                long output = computer.output.get();
                if (output != 0) System.out.println(output);

                computer.output = Optional.empty();
            }

            computer.processInput();
        }
    }
}
