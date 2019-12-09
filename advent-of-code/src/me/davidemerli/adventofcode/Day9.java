package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day9 {

    private static List<Long> PROGRAM = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day9.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        System.out.print("result1: ");
        runWithStatus(1);
    }

    private static void secondPart() {
        System.out.print("result2: ");
        runWithStatus(2);
    }

    private static void runWithStatus(int status) {
        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        computer.input = Optional.of((long) status);

        while(computer.status != Status.STOPPED) {
            computer.processInput();

            if(computer.output.isPresent()) {
                System.out.println(computer.output.get());
                computer.output = Optional.empty();
            }
        }
    }
}