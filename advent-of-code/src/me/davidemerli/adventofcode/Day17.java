package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day17 {

    private static List<Long> PROGRAM = new ArrayList<>();

    private static char[][] matrix = new char[35][61];

    private static Direction currentDirection;
    private static Point currentPos;

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day17.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        int x = 0;
        int y = 0;

        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.output.isPresent()) {
                char output = (char) computer.output.get().intValue();

                if (output == '\n') {
                    y++;
                    x = 0;
                } else {
                    matrix[x][y] = output;
                    x++;
                }

                computer.output = Optional.empty();
            }
        }

        List<Point> intersections = getIntersections(matrix);
        int result = intersections.stream().map(point -> point.x * point.y).reduce((sum, el) -> sum += el).orElse(0);

        System.out.println("result1: " + result);
    }

    private static void secondPart() {
        setStartingSettings(matrix);

//        printPath(getIntersections(matrix));

        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);
        computer.overrideMemory(0, 2);

        String main = "A,B,B,A,C,A,C,A,C,B\n";

        String A = "R,6,R,6,R,8,L,10,L,4\n";
        String B = "R,6,L,10,R,8\n";
        String C = "L,4,L,12,R,6,L,10\n";

        String mode = "n\n";

        Queue<Long> input = new LinkedList<>();

        Arrays.stream(stringToLongArray(main)).forEach(input::add);
        Arrays.stream(stringToLongArray(A)).forEach(input::add);
        Arrays.stream(stringToLongArray(B)).forEach(input::add);
        Arrays.stream(stringToLongArray(C)).forEach(input::add);
        Arrays.stream(stringToLongArray(mode)).forEach(input::add);

        long lastOutput = 0;

        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.status == Status.PAUSED) {
                computer.input = Optional.ofNullable(input.poll());
                computer.status = Status.RUNNING;
            }

            if (computer.output.isPresent()) {
                lastOutput = computer.output.get();
//                char output = (char) computer.output.get().intValue();
//                System.out.print(output);
                computer.output = Optional.empty();
            }
        }

        System.out.println("result2: " + lastOutput);
    }

    private static void printPath(List<Point> intersections) {
        List<Point> path = new ArrayList<>();

        int count = 0;
        while (true) {
            List<Point> neighbours = getNeighbors(matrix, currentPos);
            neighbours = neighbours.stream()
                    .filter(p -> matrix[p.x][p.y] == '#')
                    .filter(p -> !path.contains(p) || intersections.contains(p))
                    .collect(Collectors.toList());

            Point next = new Point(currentPos.x + currentDirection.vector[0], currentPos.y + currentDirection.vector[1]);

            if (neighbours.size() == 0) break;

            if (neighbours.contains(next)) {
                path.add(next);
                currentPos = next;
                count++;
            } else {
                next = neighbours.get(0);
                String direction = setTurningDirection(currentPos, next);
                if (count != 0) {
                    System.out.print(count + ",");
                    count = 0;
                }

                System.out.print(direction + ",");
            }
        }
        System.out.println(count);
    }

    private enum Direction {
        LEFT('<', new int[]{-1, 0}),
        UP('^', new int[]{0, -1}),
        RIGHT('>', new int[]{1, 0}),
        DOWN('v', new int[]{0, 1});

        char s;
        int[] vector;

        Direction(char s, int[] vector) {
            this.s = s;
            this.vector = vector;
        }
    }

    private static long[] stringToLongArray(String s) {
        long[] array = new long[s.length()];

        for (int i = 0; i < s.toCharArray().length; i++) {
            array[i] = s.charAt(i);
        }

        return array;
    }

    private static void setStartingSettings(char[][] matrix) {
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                char value = matrix[i][j];
                Optional<Direction> dir = Arrays.stream(Direction.values()).filter(direction -> direction.s == value).findFirst();
                if (dir.isPresent()) {
                    currentDirection = dir.get();
                    currentPos = new Point(i, j);
                    break;
                }
            }
        }
    }

    private static String setTurningDirection(Point current, Point next) {
        if (current.distance(next) > 1) return null;

        if (currentDirection == Direction.LEFT) {
            if (next.y > current.y) {
                currentDirection = Direction.DOWN;
                return "L";
            } else {
                currentDirection = Direction.UP;
                return "R";
            }
        } else if (currentDirection == Direction.UP) {
            if (next.x > current.x) {
                currentDirection = Direction.RIGHT;
                return "R";
            } else {
                currentDirection = Direction.LEFT;
                return "L";
            }
        } else if (currentDirection == Direction.RIGHT) {
            if (next.y > current.y) {
                currentDirection = Direction.DOWN;
                return "R";
            } else {
                currentDirection = Direction.UP;
                return "L";
            }
        } else if (currentDirection == Direction.DOWN) {
            if (next.x > current.x) {
                currentDirection = Direction.LEFT;
                return "L";
            } else {
                currentDirection = Direction.RIGHT;
                return "R";
            }
        }

        return null;
    }

    private static void printMatrix(char[][] matrix, List<Point> intersections, List<Point> path) {
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                Point p = new Point(i, j);
                System.out.print(intersections.contains(p) ? 'O' : path.contains(p) ? 'A' : matrix[i][j]);
            }
            System.out.println();
        }
    }

    private static List<Point> getIntersections(char[][] matrix) {
        List<Point> list = new ArrayList<>();

        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][j] == '#') {
                    Point current = new Point(i, j);

                    if (getNeighbors(matrix, current).stream().allMatch(point -> matrix[point.x][point.y] == '#')) {
                        list.add(current);
                    }
                }
            }
        }

        return list;
    }

    private static List<Point> getNeighbors(char[][] matrix, Point p) {
        List<Point> list = new ArrayList<>();

        for (int x = Math.max(0, p.x - 1); x <= Math.min(p.x + 1, matrix.length - 1); x++) {
            for (int y = Math.max(0, p.y - 1); y <= Math.min(p.y + 1, matrix[0].length - 1); y++) {
                if (x == p.x && y == p.y) continue;
                if (Math.abs(p.x - x) == Math.abs(p.y - y)) continue;

                list.add(new Point(x, y));
            }
        }

        return list;
    }
}