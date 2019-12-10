package me.davidemerli.adventofcode;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day10 {

    private enum Type {
        ASTEROID('#'), NOTHING('.');

        public char c;

        Type(char c) {
            this.c = c;
        }
    }

    private static void printMatrix(Type[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j].c);
            }
            System.out.println();
        }

    }

    public static void main(String[] args) throws IOException {
        List<String> inputLines = getLinesFromFile(getFileFromWorkingDir("input/day10.txt"));

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

        printMatrix(matrix);

        int[][] matrixValues = new int[inputLines.get(0).length()][inputLines.size()];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int sum = 0;
                if (matrix[i][j] == Type.NOTHING) {
//                    System.out.print(".");
                    continue;
                }

                for (int i1 = 0; i1 < matrix.length; i1++) {
                    for (int j1 = 0; j1 < matrix[i1].length; j1++) {
                        if (matrix[i1][j1] == Type.NOTHING) continue;

                        if (canSeeTile(matrix, new Point(i, j), new Point(i1, j1))) {
                            sum++;
                        }
                    }
                }
//                System.out.print(sum -1);
                matrixValues[i][j] = sum - 1;
            }
//            System.out.println();
        }

        int max = 0;
        Point p = null;

        for (int i = 0; i < matrixValues.length; i++) {
            for (int j = 0; j < matrixValues[i].length; j++) {
                int value = matrixValues[i][j];
                if (value > max) {
                    p = new Point(i, j);
                    max = value;
                }
            }
        }

        System.out.println(p + " " + max);

        fillPointDirections(matrix, p);

        pointDirections.values().stream().distinct().forEach(doubles -> System.out.println(doubles[0] + " " + doubles[1]));

        System.out.println(pointDirections);

        Point last = null;
//        int count = 0;
//        while (count < 200) {
//            for (double i = 0; i < 2 * Math.PI; i += 0.0000001) {
//                double[] vector = {Math.cos(i), Math.sin(i)};
//
//                Point toLaser = getFirst(matrix, p, vector);
//
//                if (toLaser != null) {
//                    count++;
//                    matrix[toLaser.x][toLaser.y] = Type.NOTHING;
//                    last = toLaser;
//
//                    System.out.println(last);
//
////                    printMatrix(matrix);
//                }
//            }
//            count++;
//        }

        System.out.println(last);
    }

    private static Map<Point, double[]> pointDirections = new HashMap<>();

    private static void fillPointDirections(Type[][] matrix, Point center) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Point pos = new Point(i, j);

                if(pos.equals(center)) continue;

                pointDirections.put(pos, normVect(pos, center));
            }
        }
    }

    private static Point getFirst(Type[][] matrix, Point p1, double[] vector) {
        Point p = null;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Point pos = new Point(i, j);

                if (p1.equals(pos) | matrix[pos.x][pos.y] == Type.NOTHING) continue;
                double[] vectorPos = normVect(p1, pos);

                if (Math.abs(vector[0] - vectorPos[0]) < 0.0000001 && Math.abs(vector[1] - vectorPos[1]) < 0.0000001) {
                    if (p == null || p1.distance(pos) < p1.distance(p)) {
                        p = pos;
                    }
                }
            }
        }

        return p;
    }

    private static boolean canSeeTile(Type[][] matrix, Point p1, Point p2) {

        boolean canSee = true;

        double[] vector = normVect(p1, p2);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Point pos = new Point(i, j);

                if (p1.equals(pos) || p2.equals(pos) || matrix[pos.x][pos.y] == Type.NOTHING) continue;
                double[] vectorPos = normVect(p1, pos);

//                System.out.println(vectorPos);


                if (Math.abs(vector[0] - vectorPos[0]) < 0.0000001 && Math.abs(vector[1] - vectorPos[1]) < 0.0000001) {
                    if (p1.distance(pos) < p1.distance(p2))
                        canSee = false;
                }

            }
        }

        return canSee;
    }

    private static double[] normVect(Point p1, Point p2) {
        int diffX = p1.x - p2.x;
        int diffY = p1.y - p2.y;
        double distance = p1.distance(p2);

        return new double[]{diffX / distance, diffY / distance};
    }

    public static ArrayList<Point> discretizeRay(Point p1, Point p2) {
        ArrayList<Point> rayList = new ArrayList<>();
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        int mx = x2 - x1;
        int my = y2 - y1;
        int dirX = mx > 0 ? 1 : -1;
        int dirY = my > 0 ? 1 : -1;
        int currentX = x1;
        int currentY = y1;
        float nextX = x1 + 0.1f * dirX;
        float nextY = y1 + 0.1f * dirY;
        //Initialize the Matrix
        rayList.add(new Point(currentX, currentY));
        while (currentX != x2 && currentY != y2) {
            float tx = (nextX - x1) / mx;
            float ty = (nextY - y1) / my;
            rayList.add(new Point(currentX, currentY));
            if (tx < ty) {
                currentX += dirX;
                nextX = currentX + 0.1f * dirX;
            } else if (tx > ty) {
                currentY += dirY;
                nextY = currentY + 0.1f * dirY;
            } else {
                currentX += dirX;
                currentY += dirY;
                nextX = currentX + 0.1f * dirX;
                nextY = currentY + 0.1f * dirY;
            }
        }
        rayList.add(new Point(currentX, currentY));
        if (currentX == x2)
            for (int i = currentY; (i <= y2 && dirY > 0) || (i >= y2 && dirY < 0); i += dirY)
                rayList.add(new Point(currentX, i));
        else
            for (int i = currentX; (i <= x2 && dirX > 0) || (i >= x2 && dirX < 0); i += dirX)
                rayList.add(new Point(i, currentY));

        rayList.remove(rayList.size() - 1);
        rayList.remove(0);
        return rayList;
    }
//    public static boolean canRayReachTile(Point p1, Point p2) {
//        return discretizeRay(p1, p2).stream().allMatch(i -> func.canCross(map, i));
//    }
}