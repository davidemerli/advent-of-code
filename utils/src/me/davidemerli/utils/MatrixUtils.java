package me.davidemerli.utils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class MatrixUtils {

    public static Optional<java.util.List<Point>> bfs(char[][] matrix, Point source, Point dest, java.util.List<Character> toAvoid) {
        java.util.Queue<Point> queue = new ArrayDeque<>();

        boolean[][] flag = new boolean[matrix.length][matrix[0].length];
        Point[][] prev = new Point[matrix.length][matrix[0].length];

        flag[source.x][source.y] = true;

        queue.add(source);

        while (!queue.isEmpty()) {
            Point v = queue.poll();

            if (v.equals(dest)) break;

            for (Point w : getNeighbors(matrix, v, toAvoid, false)) {
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

    public static List<Point> getNeighbors(char[][] matrix, Point p, List<Character> toAvoid, boolean diagonalCheck) {
        List<Point> list = new ArrayList<>();

        for (int x = Math.max(0, p.x - 1); x <= Math.min(p.x + 1, matrix.length - 1); x++) {
            for (int y = Math.max(0, p.y - 1); y <= Math.min(p.y + 1, matrix[0].length - 1); y++) {
                if (x == p.x && y == p.y) continue;
                if (!diagonalCheck && Math.abs(p.x - x) == Math.abs(p.y - y)) continue;

                if (toAvoid.contains(matrix[x][y])) continue;

                list.add(new Point(x, y));
            }
        }

        return list;
    }

    public static void printMatrix(char[][] matrix) {
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static List<Point> getFromMatrix(char[][] matrix, Predicate<Character> isValid) {
        List<Point> list = new ArrayList<>();

        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                if (isValid.test(matrix[i][j])) {
                    list.add(new Point(i, j));
                }
            }
        }

        return list;
    }
}
