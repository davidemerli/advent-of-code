package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day11 {

    private static List<Long> PROGRAM = new ArrayList<>();

    private static List<Point> points = new ArrayList<>();

    private static IntCodeComputer computer;

    private static final Integer[] LEFT = new Integer[]{-1, 0};
    private static final Integer[] UP = new Integer[]{0, -1};
    private static final Integer[] RIGHT = new Integer[]{1, 0};
    private static final Integer[] DOWN = new Integer[]{0, 1};

    private static final List<Integer[]> directions = Arrays.asList(LEFT, UP, RIGHT, DOWN);

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day11.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        firstPart();

        computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        secondPart();
    }

    private static void firstPart() {
        System.out.print("result1: ");
        startRobot(0, true);
    }

    private static void secondPart() {
        System.out.println("result2: ");
        startRobot(1, false);
    }

    private static void startRobot(int firstColor, boolean printEdits) {
        points.add(new Point(50, 50));
        Point position = points.get(0);

        Integer[] direction = UP;
        int outputCount = 0;

        int[][] matrix = new int[100][100];

        List<Point> edits = new ArrayList<>();

        matrix[position.x][position.y] = firstColor;

        while (computer.status != Status.STOPPED) {
            if (computer.status == Status.PAUSED) {
                computer.input = Optional.of((long) matrix[position.x][position.y]);
            }

            computer.processInput();

            if (computer.output.isPresent()) {
                long output = computer.output.get();

                if (outputCount % 2 == 0) {
                    matrix[position.x][position.y] = (int) output;

                    edits.add(position);
                } else {
                    direction = turn(direction, (int) output);

                    position = new Point(position.x + direction[0], position.y + direction[1]);
                }

                computer.output = Optional.empty();
                outputCount++;
            }
        }

        if (printEdits) {
            System.out.println(edits.stream().distinct().count());
        } else {
            printMatrix(matrix);
        }
    }

    private static Integer[] turn(Integer[] dir, int where) {
        int newIndex = directions.indexOf(dir) + (where == 0 ? -1 : 1);

        return directions.get(newIndex < 0 ? directions.size() - 1 : newIndex == directions.size() ? 0 : newIndex);
    }

    private static void printMatrix(int[][] matrix) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[j][i] == 1) {
                    if (j < minX) minX = j;
                    if (i < minY) minY = i;
                    if (j > maxX) maxX = j;
                    if (i > maxY) maxY = i;
                }
            }
        }

        for (int j = minY; j <= maxY; j++) {
            for (int i = minX; i <= maxX; i++) {
                System.out.print(matrix[i][j] == 1 ? "█" : " ");
            }
            System.out.println();
        }
    }
}
