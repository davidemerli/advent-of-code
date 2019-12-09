package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.*;

public class Day2 {

    public static void main(String[] args) throws IOException {
        firstPart();
        secondPart();
    }

    private static void firstPart() throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day2.txt")).get(0);
        List<Integer> codes = Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList());

        codes.set(1, 12);
        codes.set(2, 2);

        processInputInstructions(codes);

        System.out.println("result1: " + codes.get(0));
    }

    private static void secondPart() throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day2.txt")).get(0);

        final int givenResultToAchieve = 19690720;

        List<Integer> codes;

        for (int noun = 0; noun < 99; noun++) {
            for (int verb = 0; verb < 99; verb++) {
                codes = Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList());

                codes.set(1, noun);
                codes.set(2, verb);

                processInputInstructions(codes);

                if (codes.get(0) == givenResultToAchieve) {
                    int result = noun * 100 + verb;

                    System.out.println("result2: " + result);
                    return;
                }
            }
        }
    }

    private static void processInputInstructions(List<Integer> codes) {
        for (int i = 0; i < codes.size(); i++) {
            int opcode = codes.get(i);

            if (opcode == 99) break;

            int value1 = codes.get(i + 1);
            int value2 = codes.get(i + 2);
            int where = codes.get(i + 3);

            codes.set(where, opcode == 1 ? codes.get(value1) + codes.get(value2) : codes.get(value1) * codes.get(value2));
            i += 3;
        }
    }
}
