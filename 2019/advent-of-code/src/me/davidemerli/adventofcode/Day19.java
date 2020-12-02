package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.io.IOException;
import java.util.*;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day19 {

    private static List<Long> PROGRAM = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day19.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        int count = 0;

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (isBeam(i, j)) count++;
            }
        }

        System.out.println("result1: " + count);
    }

    private static void secondPart() {
        int currentX = 99;
        int currentY = 0;
        int result;

        /* checks opposite corners
         *
         *  ###O
         *  ####
         *  ####
         *  0###
         */

        while (true) {
            if (isBeam(currentX, currentY)) {
                if (isBeam(currentX - 99, currentY + 99)) {
                    result = ((currentX - 99) * 10000 + currentY);
                    break;
                } else {
                    currentX++;
                }
            } else {
                currentY++;
            }
        }

        System.out.println("result2: " + result);
    }

    private static boolean isBeam(long x, long y) {
        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Queue<Long> q = new LinkedList<>(Arrays.asList(x, y));

        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.status == Status.PAUSED && !q.isEmpty()) {
                computer.input = Optional.of(q.poll());
            }

            if (computer.output.isPresent()) {
                return computer.output.get().intValue() == 1;
            }
        }

        return false;
    }
}