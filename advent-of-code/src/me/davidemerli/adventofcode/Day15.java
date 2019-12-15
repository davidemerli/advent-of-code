package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day15 {

    private static List<Long> PROGRAM = new ArrayList<>();

    private static char[][] matrix = new char[50][50];

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day15.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        for (char[] chars : matrix) Arrays.fill(chars, '?');
    }

    private static void firstPart() {
        Point start = new Point(25, 25), current = new Point(start), prev = new Point(start);

        IntCodeComputer computer = new IntCodeComputer(PROGRAM);
        computer.addOperators(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Optional<Point> toCheck;

        Point oxygenSystem = null;

        while (computer.status != Status.STOPPED) {
            computer.processInput();

            if (computer.status == Status.PAUSED) {
                toCheck = toCheck(matrix, current);
                if (!toCheck.isPresent()) break;

                List<Point> path = bfs(matrix, current, toCheck.get()).orElse(Collections.singletonList(current));

                Point next = path.get(1);

                computer.input = Optional.of((long) getDirection(current, next));
                computer.status = Status.RUNNING;

                prev = current;
                current = next;
            }

            if (computer.output.isPresent()) {
                long out = computer.output.get();
                computer.output = Optional.empty();

                if (out == 0) {
                    matrix[current.x][current.y] = '#';
                    current = prev;
                } else if (out == 1) {
                    matrix[current.x][current.y] = '.';
                } else if (out == 2) {
                    matrix[current.x][current.y] = 'O';
                    oxygenSystem = (current);
                }
            }
        }

        printGrid(matrix);

        Optional<List<Point>> path = bfs(matrix, start, oxygenSystem);

        path.ifPresent(points -> System.out.println("result1: " + (points.size() - 1)));
    }

    private void secondPart() {
        int count;
        for (count = 0; needsOxygen(matrix); count++) {
            fillOnce(matrix);
        }

        System.out.println("result2: " + count);
    }

    private static Optional<Point> toCheck(char[][] matrix, Point source) {
        List<Point> toCheck = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                char value = matrix[i][j];

                if (value == '?') {
                    Point p = new Point(i, j);
                    toCheck.add(p);
                }
            }
        }

        toCheck.sort(Comparator.comparingDouble(point -> point.distance(source)));

        for (Point point : toCheck) {
            Optional<List<Point>> path = bfs(matrix, source, point);

            if (path.isPresent()) {
                return Optional.of(point);
            }
        }

        return Optional.empty();
    }

    private static int getDirection(Point from, Point to) {
        if (from.distance(to) != 1) return -1;

        if (from.x == to.x) {
            return from.y < to.y ? 2 : 1;
        }

        if (from.y == to.y) {
            return from.x < to.x ? 4 : 3;
        }

        return -1;
    }

    private static Optional<List<Point>> bfs(char[][] matrix, Point source, Point dest) {
        Queue<Point> queue = new ArrayDeque<>();

        boolean[][] flag = new boolean[matrix.length][matrix[0].length];
        Point[][] prev = new Point[matrix.length][matrix[0].length];

        flag[source.x][source.y] = true;

        queue.add(source);

        while (!queue.isEmpty()) {
            Point v = queue.poll();

            if (v.equals(dest)) break;

            for (Point w : getNeighbors(matrix, v)) {
                if (!flag[w.x][w.y]) {
                    flag[w.x][w.y] = true;

                    prev[w.x][w.y] = v;
                    queue.add(w);
                }
            }
        }

        List<Point> shortestPath = new ArrayList<>();
        Point current = dest;

        while (current != null) {
            shortestPath.add(current);

            current = prev[current.x][current.y];
        }

        Collections.reverse(shortestPath);

        return shortestPath.size() == 1 ? Optional.empty() : Optional.of(shortestPath);
    }

    private static boolean needsOxygen(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                char value = matrix[i][j];
                if (value == '.') return true;
            }
        }

        return false;
    }

    private static void fillOnce(char[][] matrix) {
        List<Point> toExpand = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                char value = matrix[i][j];

                if (value == 'O') {
                    toExpand.add(new Point(i, j));
                }
            }
        }

        for (Point point : toExpand) {
            for (Point neighbor : getNeighbors(matrix, point)) {
                if (matrix[neighbor.x][neighbor.y] == '.') {
                    matrix[neighbor.x][neighbor.y] = 'O';
                }
            }
        }
    }

    private static List<Point> getNeighbors(char[][] matrix, Point p) {
        List<Point> list = new ArrayList<>();

        for (int x = Math.max(0, p.x - 1); x <= Math.min(p.x + 1, matrix.length - 1); x++) {
            for (int y = Math.max(0, p.y - 1); y <= Math.min(p.y + 1, matrix[0].length - 1); y++) {
                if (x == p.x && y == p.y) continue;
                if (Math.abs(p.x - x) == Math.abs(p.y - y)) continue;

                if (matrix[x][y] == '#') continue;

                list.add(new Point(x, y));
            }
        }

        return list;
    }

    private static void printGrid(char[][] grid) {
        System.out.println();
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}