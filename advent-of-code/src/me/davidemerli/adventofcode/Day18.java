package me.davidemerli.adventofcode;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;
import static me.davidemerli.utils.MatrixUtils.*;

public class Day18 {

    private static final Predicate<Character> IS_KEY = (c) -> c.toString().matches("[a-z]");

    public static void main(String[] args) throws IOException {
        List<String> inputLines = getLinesFromFile(getFileFromWorkingDir("input/day18.txt"));

        char[][] matrix = new char[inputLines.get(0).length()][inputLines.size()];

        for (int i = 0; i < inputLines.size(); i++) {
            for (int j = 0; j < inputLines.get(i).length(); j++) {
                matrix[j][i] = inputLines.get(i).charAt(j);
            }
        }

        firstPart(matrix);

        splitIntoVaults(matrix);

        secondPart(matrix);
    }

    private static void firstPart(char[][] matrix) {
        final Predicate<Character> IS_DOOR = (c) -> c.toString().matches("[A-Z]");

        Vault vault = new Vault(matrix);
        System.out.println("result1: " + vault.getMinimumPath(
                getFromMatrix(matrix, IS_DOOR),
                getFromMatrix(matrix, IS_KEY),
                vault.current));
    }

    private static void secondPart(char[][] matrix) {
        List<char[][]> matrices = getVaultsMatrices(matrix);
        List<Vault> vaults = new ArrayList<>();

        for (char[][] chars : matrices) {
            vaults.add(new Vault(chars));
        }

        int count = 0;

        final BiPredicate<Character, char[][]> IS_DOOR = (c, m) -> {
            for (Point p : getFromMatrix(m, IS_KEY)) {
                if(("" + m[p.x][p.y]).toUpperCase().equals(c + "")) {
                    return true;
                }
            }
            return false;
        };

        for (Vault vault : vaults) {
            int result = vault.getMinimumPath(
                    getFromMatrix(vault.matrix, c -> IS_DOOR.test(c, vault.matrix)),
                    getFromMatrix(vault.matrix, IS_KEY),
                    vault.current);

            if (result != 0) {
                count += result;
            }
        }

        System.out.println("result2: " + count);
    }

    private static void splitIntoVaults(char[][] matrix) {
        Point current = getFromMatrix(matrix, (c) -> c == '@').get(0);

        List<Point> toChange = getNeighbors(matrix, current, Collections.emptyList(), true);
        toChange.add(current);
        for (Point point : toChange) {
            if(point.distance(current) > 1) {
                matrix[point.x][point.y] = '@';
            } else {
                matrix[point.x][point.y] = '#';
            }
        }
    }

    private static List<char[][]> getVaultsMatrices(char[][] matrix) {
        char[][] matrix1 = new char[matrix.length / 2 + 1][matrix[0].length / 2 + 1];
        char[][] matrix2 = new char[matrix.length / 2 + 1][matrix[0].length / 2 + 1];
        char[][] matrix3 = new char[matrix.length / 2 + 1][matrix[0].length / 2 + 1];
        char[][] matrix4 = new char[matrix.length / 2 + 1][matrix[0].length / 2 + 1];

        for (int i = 0; i < matrix.length / 2 + 1; i++) {
            System.arraycopy(matrix[i], 0, matrix1[i], 0, matrix[0].length / 2 + 1);
        }

        for (int i = 0; i < matrix.length / 2 + 1; i++) {
            System.arraycopy(matrix[i + matrix.length / 2], 0, matrix2[i], 0, matrix[0].length / 2 + 1);
        }

        for (int i = 0; i < matrix.length / 2 + 1; i++) {
            System.arraycopy(matrix[i], matrix[0].length / 2, matrix3[i], 0, matrix[0].length / 2 + 1);
        }

        for (int i = 0; i < matrix.length / 2 + 1; i++) {
            System.arraycopy(matrix[i + matrix.length / 2], matrix[0].length / 2, matrix4[i], 0, matrix[0].length / 2 + 1);
        }

        return Arrays.asList(matrix1, matrix2, matrix3, matrix4);
    }

    private static Map<List<Point>, Integer> cache = new HashMap<>();

    private static List<Point> hash(List<Point> doors, List<Point> keys, Point current) {
        List<Point> list = new ArrayList<>(doors);
        list.addAll(keys);
        list.add(current);
        return list;
    }

    private static class Vault {
        char[][] matrix;
        Point current;

        Vault(char[][] matrix) {
            this.matrix = matrix;
            Predicate<Character> isRobot = (c) -> c == '@';
            current = getFromMatrix(matrix, isRobot).get(0);
        }

        private int getMinimumPath(List<Point> doors, List<Point> keys, Point current) {
            List<Point> hash = hash(doors, keys, current);

            if (cache.containsKey(hash)) {
                return cache.get(hash);
            }

            List<Character> toAvoid = doors.stream().map(point -> matrix[point.x][point.y]).collect(Collectors.toList());
            toAvoid.add('#');

            Map<Point, Optional<List<Point>>> keyMap = new LinkedHashMap<>();
            keys.forEach(point -> keyMap.put(point, bfs(matrix, current, point, toAvoid)));

            Map<Point, List<Point>> reachableKeys = keyMap.entrySet().stream()
                    .filter(entry -> entry.getValue().isPresent())
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));

            if (reachableKeys.size() == 0) {
                return 0;
            }

            int minCount = Integer.MAX_VALUE;

            for (Map.Entry<Point, List<Point>> entry : reachableKeys.entrySet()) {
                List<Point> modifiedDoors = new ArrayList<>(doors);
                modifiedDoors.removeAll(getFromMatrix(matrix,
                        (c) -> {
                            String value = "" + matrix[entry.getKey().x][entry.getKey().y];
                            return c.toString().equals(value.toUpperCase());
                        }));

                List<Point> modifiedKeys = new ArrayList<>(keys);
                modifiedKeys.remove(entry.getKey());

                Point next = entry.getValue().get(entry.getValue().size() - 1);

                int count = entry.getValue().size() - 1 + getMinimumPath(modifiedDoors, modifiedKeys, next);

                minCount = Integer.min(count, minCount);
            }

            cache.put(hash, minCount);
            return minCount;
        }
    }
}