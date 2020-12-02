package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.List;

import static me.davidemerli.utils.SettingsReader.*;

public class Day01 {

    public static void main(String[] args) throws IOException {
        firstPart();
        secondPart();
    }

    private static void firstPart() throws IOException {
        List<String> inputLines = getLinesFromFile(getFileFromWorkingDir("input/day1.txt"));

        int result = inputLines.stream()
                .map(Integer::parseInt)//maps the elements of the list as Integers
                .reduce(0, Day01::partialSum); //sums into partialSum and retrieves the sum value

        System.out.println("result1: " + result);
    }

    private static void secondPart() throws IOException {
        List<String> inputLines = getLinesFromFile(getFileFromWorkingDir("input/day1.txt"));

        int result = inputLines.stream()
                .map(Integer::parseInt) //maps the elements of the list as Integers
                .reduce(0, Day01::recursivePartialSum);

        System.out.println("result2: " + result);
    }

    private static int partialSum(int partialSum, int element) {
        return partialSum + Math.floorDiv(element, 3) - 2;
    }

    private static int recursivePartialSum(int partialSum, int element) {
        do {
            element = Math.floorDiv(element, 3) - 2;
            if (element > 0) partialSum += element;
        } while (element > 0);

        return partialSum;
    }
}