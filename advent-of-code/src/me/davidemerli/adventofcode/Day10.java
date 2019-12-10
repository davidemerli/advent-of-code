package me.davidemerli.adventofcode;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day10 {

    private static Point center = null;
    private static Map<Point, Double> pointsMap = new HashMap<>();
    private static Type[][] matrix;

    public static void main(String[] args) throws IOException {
        List<String> inputLines = getLinesFromFile(getFileFromWorkingDir("input/day10.txt"));
        matrix = fillMatrix(inputLines);
        printMatrix(matrix);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        int[][] distances = getDistances(matrix, getAsteroids(matrix));
        System.out.println();
        printDistances(distances);
        System.out.println();
        System.out.println("result1: " + getMax(distances));
    }

    private static void secondPart() {
        getAsteroids(matrix).forEach(point -> pointsMap.put(point, getPointAngle(point, center)));
        List<Double> angles = getOrderedAngles();

        int count = 0;
        Point last = null;

        while (count < 200) {
            for (double angle : angles) {
                Point min = getPointWithSameAngle(pointsMap, angle).stream()
                        .filter(point -> !point.equals(center))
                        .min(Comparator.comparingDouble(point -> point.distance(center)))
                        .orElse(null);

                if (min != null) {
                    pointsMap.remove(min);
                    last = min;
                    count++;

                    if (count == 200) break;
                }
            }
        }

        System.out.println("result2: " + (last.x * 100 + last.y));
    }

    private static List<Double> getOrderedAngles() {
        return pointsMap.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getValue)
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<Point> getPointWithSameAngle(Map<Point, Double> map, double angle) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(angle))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static int getMax(int[][] matrix) {
        int max = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int value = matrix[i][j];
                if (value > max) {
                    max = value;
                    center = new Point(j, i);
                }
            }
        }

        return max;
    }

    private static int countReachable(Point p, List<Point> asteroids) {
        Map<Point, Double> pointDoubleMap = new HashMap<>();
        asteroids.stream()
                .filter(point -> !p.equals(point))
                .forEach(point -> pointDoubleMap.put(point, getPointAngle(point, p)));

        int count = 0;

        for (Point asteroid : asteroids) {
            if (asteroid.equals(p)) continue;

            double angle = pointDoubleMap.get(asteroid);

            Point min = getPointWithSameAngle(pointDoubleMap, angle).stream()
                    .min(Comparator.comparingDouble(point -> point.distance(p)))
                    .orElse(null);

            assert min != null;
            if (min.equals(asteroid)) {
                count++;
            }
        }

        return count;
    }

    private static List<Point> getAsteroids(Type[][] matrix) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == Type.ASTEROID) {
                    points.add(new Point(j, i));
                }
            }
        }
        return points;
    }

    private static double getPointAngle(Point p, Point center) {
        int diffX = p.x - center.x;
        int diffY = -(p.y - center.y);

        double angle = Math.toDegrees(Math.atan2(diffX, diffY));
        return diffX >= 0 ? angle : 360 + angle;
    }

    private static int[][] getDistances(Type[][] matrix, List<Point> points) {
        int[][] distances = new int[matrix.length][matrix[0].length];

        points.forEach(point -> distances[point.y][point.x] = countReachable(point, points));

        return distances;
    }

    private static void printDistances(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int value = matrix[i][j];
                System.out.print(value == 0 ? ".\t" : value + "\t");
            }
            System.out.println();
        }
    }

    private static void printMatrix(Type[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j].c + "\t");
            }
            System.out.println();
        }
    }

    private static Type[][] fillMatrix(List<String> inputLines) {
        Type[][] matrix = new Type[inputLines.get(0).length()][inputLines.size()];

        for (int i = 0; i < inputLines.size(); i++) {
            String inputLine = inputLines.get(i);

            for (int j = 0; j < inputLine.toCharArray().length; j++) {
                char c = inputLine.charAt(j);

                if (c == '#') {
                    matrix[i][j] = Type.ASTEROID;
                } else if (c == '.') {
                    matrix[i][j] = Type.NOTHING;
                }
            }
        }

        return matrix;
    }

    private enum Type {
        ASTEROID('#'), NOTHING('.');

        public char c;

        Type(char c) {
            this.c = c;
        }
    }
}