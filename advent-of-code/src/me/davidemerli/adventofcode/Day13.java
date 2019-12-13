package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day13 {

    private static List<Long> PROGRAM = new ArrayList<>();

    private static int[][] grid = new int[38][21];
    private static int[] codes = new int[3];
    private static int outputCount = 0;

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day13.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        firstPart(computer);

        computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        secondPart(computer);
    }

    private static void firstPart(IntCodeComputer computer) {
        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.output.isPresent()) {
                long out = computer.output.get();
                computer.output = Optional.empty();

                codes[outputCount % 3] = (int) out;

                if (outputCount % 3 == 2) {
                    grid[codes[0]][codes[1]] = codes[2];
                }

                outputCount++;
            }
        }

        printGrid(grid);

        System.out.println("result1: " + countBlocks(grid));
    }

    private static void secondPart(IntCodeComputer computer) {
        long score = 0;

        Point ball = null;
        Point paddle = null;

        computer.overrideMemory(0, 2);

        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.status == Status.PAUSED) {
                assert ball != null && paddle != null;
                computer.input = Optional.of((long) (Long.compare(ball.x, paddle.x)));
            }

            if (computer.output.isPresent()) {
                long out = computer.output.get();
                computer.output = Optional.empty();

                codes[outputCount % 3] = (int) out;

                if (outputCount % 3 == 2) {
                    if (codes[0] == -1 && codes[1] == 0) {
                        score = codes[2];
                    } else {
                        grid[codes[0]][codes[1]] = codes[2];

                        if (codes[2] == 3) {
                            paddle = new Point(codes[0], codes[1]);
                        } else if (codes[2] == 4) {
                            ball = new Point(codes[0], codes[1]);
                        }
                    }
                }

                outputCount++;
            }
        }

        System.out.println("result2: " + score);
    }

    private static void printGrid(int[][] grid) {
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                int value = grid[i][j];
                String symbol = value == 1 ? "\u2B1B" : value == 0 ? "\u3000" : value == 4 ? "\u200A\u26AC" : value == 3 ? "\u2B1B" : "\u2B1C";
                System.out.print(symbol);
            }
            System.out.println();
        }
    }

    private static int countBlocks(int[][] grid) {
        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 2) {
                    count++;
                }
            }
        }

        return count;
    }
}