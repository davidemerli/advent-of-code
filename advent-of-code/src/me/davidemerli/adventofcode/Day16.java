package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day16 {
    private static List<Integer> digits, copy;

    public static void main(String[] args) throws IOException {
        String signal = getLinesFromFile(getFileFromWorkingDir("input/day16.txt")).get(0);
        digits = Arrays.stream(signal.split("")).map(Integer::parseInt).collect(Collectors.toList());

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        copy = new ArrayList<>(digits);
        List<Integer> newDigits = new ArrayList<>();

        for (int phase = 0; phase < 100; phase++) {
            newDigits.clear();

            for (int j = 0; j < copy.size(); j++) {
                int sum = 0;

                for (int d = 0; d < copy.size(); d++) {
                    int digit = copy.get(d);

                    int value = digit * getMultiplierFromPattern(d, j);
                    sum += value;
                }

                newDigits.add(Math.abs(sum % 10));
            }
            copy = new ArrayList<>(newDigits);
        }

        System.out.print("result1: ");
        copy.stream().limit(8).forEach(System.out::print);
        System.out.println();
    }

    private static void secondPart() {
        copy = new ArrayList<>(digits);
        IntStream.range(1, 10000).forEach(i -> copy.addAll(digits));

        int offset = 0;

        for (int i = 0; i < 7; i++) {
            offset += copy.get(i) * Math.pow(10, (6 - i));
        }

        copy = copy.subList(offset, copy.size());

        for (int i = 0; i < 100; i++) {
            int partialSum = 0;
            for (int j = copy.size() - 1; j >= 0; j--) {//526153
                partialSum += copy.get(j);
                partialSum = Math.abs(partialSum % 10);
                copy.set(j, partialSum);
            }
        }

        System.out.print("result2: ");
        IntStream.range(0, 8).forEach(i -> System.out.print(copy.get(i)));
        System.out.println();
    }

    private static int getMultiplierFromPattern(int index, int repetition) {
        int[] pattern = {0, 1, 0, -1};

        int i = (index + 1) % ((repetition + 1) * pattern.length);
        int j = i / (repetition + 1);

        return pattern[j];
    }
}
