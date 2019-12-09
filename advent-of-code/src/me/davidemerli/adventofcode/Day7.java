package me.davidemerli.adventofcode;

import me.davidemerli.adventofcode.intcode.IntCodeComputer;
import me.davidemerli.adventofcode.intcode.Status;
import me.davidemerli.utils.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.davidemerli.utils.SettingsReader.*;

public class Day7 {

    private static final List<Long> PROGRAM = new ArrayList<>();

    private static List<IntCodeComputer> amplifiers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String s = getLinesFromFile(getFileFromWorkingDir("input/day7.txt")).get(0);
        Arrays.stream(s.split(",")).map(Long::parseLong).forEach(PROGRAM::add);

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        long max = 0;

        for (String s : StringUtils.permutationFinder("01234")) {
            List<Integer> digits = Arrays.stream(s.split("")).map(c -> Integer.parseInt(c + "")).collect(Collectors.toList());

            initAmplifiers();

            digits.forEach(d -> amplifiers.get(digits.indexOf(d)).input = Optional.of((long) d));

            while (amplifiers.stream().anyMatch(a -> a.status != Status.STOPPED)) {
                amplifiers.stream().filter(a -> a.status == Status.RUNNING).forEach(IntCodeComputer::processInput);

                for (int i = 0; i < amplifiers.size(); i++) {
                    IntCodeComputer a = amplifiers.get(i);

                    if (a.status == Status.PAUSED) {
                        if (i == 0) {
                            a.input = Optional.of((long) 0);
                            a.status = Status.RUNNING;
                        } else {
                            Optional<Long> previousOutput = amplifiers.get(i - 1).output;
                            if (previousOutput.isPresent()) {
                                a.input = previousOutput;
                                a.status = Status.RUNNING;
                            }
                        }
                    }
                }
            }

            long lastOutput = amplifiers.get(amplifiers.size() - 1).output.orElse((long) 0);

            if (lastOutput > max) {
                max = lastOutput;
            }
        }

        System.out.println("result1: " + max);
    }

    private static void secondPart() {
        long max = 0;

        for (String s : StringUtils.permutationFinder("56789")) {
            List<Integer> digits = Arrays.stream(s.split("")).map(c -> Integer.parseInt(c + "")).collect(Collectors.toList());

            initAmplifiers();

            digits.forEach(d -> amplifiers.get(digits.indexOf(d)).input = Optional.of((long) d));

            boolean first = false;

            while (amplifiers.stream().anyMatch(a -> a.status != Status.STOPPED)) {
                amplifiers.stream().filter(a -> a.status == Status.RUNNING).forEach(IntCodeComputer::processInput);

                for (int i = 0; i < amplifiers.size(); i++) {
                    IntCodeComputer a = amplifiers.get(i);


                    if (a.status == Status.PAUSED) {
                        if(!first && i == 0) {
                            a.input = Optional.of((long) 0);
                            a.status = Status.RUNNING;

                            first = true;
                        }

                        Optional<Long> previousOutput = amplifiers.get(i == 0 ? amplifiers.size() - 1 : i - 1).output;
                        if (previousOutput.isPresent()) {
                            a.input = previousOutput;
                            amplifiers.get(i == 0 ? amplifiers.size() - 1 : i - 1).output = Optional.empty();

                            a.status = Status.RUNNING;
                        }
                    }
                }
            }

            long lastOutput = amplifiers.get(amplifiers.size() - 1).output.orElse((long) 0);

            if (lastOutput > max) {
                max = lastOutput;
            }
        }

        System.out.println("result2: " + max);
    }

    private static void initAmplifiers() {
        amplifiers.clear();
        IntStream.range(0, 5).forEach(i -> amplifiers.add(new IntCodeComputer(PROGRAM)));
        amplifiers.forEach(intCodeComputer -> intCodeComputer.addOperators(1, 2, 3, 4, 5, 6, 7, 8));
    }
}
