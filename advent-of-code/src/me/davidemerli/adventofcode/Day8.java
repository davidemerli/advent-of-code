package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day8 {

    private static final int WIDTH = 25, HEIGHT = 6;
    private static final List<Layer> LAYERS = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        firstPart();
        secondPart();
    }

    private static void firstPart() throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day8.txt")).get(0);
        List<Integer> codes = Arrays.stream(s.split("")).map(Integer::parseInt).collect(Collectors.toList());


        for (int l = 0; l < codes.size(); l += WIDTH * HEIGHT) {
            Layer layer = new Layer();

            for (int p = l, y = 0; p < l + WIDTH * HEIGHT; p += WIDTH, y++) {
                for (int x = 0; x < WIDTH; x++) {
                    layer.matrix[x][y] = codes.get(p + x);
                }
            }

            LAYERS.add(layer);
        }

        Layer min = LAYERS.stream().min(Comparator.comparingInt(layer -> layer.digitRepeated(0))).orElse(null);

        assert min != null;
        System.out.println("result1: " + (min.digitRepeated(1) * min.digitRepeated(2)));
    }

    private static void secondPart() {
        int[][] matrix = new int[WIDTH][HEIGHT];

        for (Layer layer : LAYERS) {
            for (int i = 0; i < layer.matrix.length; i++) {
                for (int j = 0; j < layer.matrix[i].length; j++) {
                    int digit = layer.matrix[i][j];

                    if (digit != 2) matrix[i][j] = digit;
                }
            }
        }

        Layer image = new Layer();
        image.matrix = matrix;
        System.out.println("result2: ");
        image.print();
    }

    private static class Layer {
        int[][] matrix = new int[WIDTH][HEIGHT];

        void print() {
            for (int j = 0; j < matrix[0].length; j++) {
                for (int[] col : matrix) {
                    System.out.print(col[j] == 1 ? "\u2B1B" : "\u2B1C");
                }
                System.out.println();
            }
        }

        int digitRepeated(int digit) {
            int sum = 0;
            for (int[] cols : matrix) {
                for (int row : cols) {
                    if (digit == row) sum++;
                }
            }
            return sum;
        }
    }
}
