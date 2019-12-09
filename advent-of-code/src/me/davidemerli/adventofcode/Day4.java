package me.davidemerli.adventofcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.davidemerli.utils.SettingsReader.*;

public class Day4 {

    private static int FROM, TO;

    public static void main(String[] args) throws IOException {
        String code = getLinesFromFile(getFileFromWorkingDir("input/day4.txt")).get(0);
        FROM = Integer.parseInt(code.split("-")[0]);
        TO = Integer.parseInt(code.split("-")[1]);

        firstPart();
        secondPart();
    }

    /**
     * --- Day 4: Secure Container ---
     * <p>
     * You arrive at the Venus fuel depot only to discover it's protected by a password. The Elves had written the password on a sticky note, but someone threw it out.
     * <p>
     * However, they do remember a few key facts about the password:
     * <p>
     * It is a six-digit number.
     * The value is within the range given in your puzzle input.
     * Two adjacent digits are the same (like 22 in 122345).
     * Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
     * <p>
     * Other than the range rule, the following are true:
     * <p>
     * 111111 meets these criteria (double 11, never decreases).
     * 223450 does not meet these criteria (decreasing pair of digits 50).
     * 123789 does not meet these criteria (no double).
     * <p>
     * How many different passwords within the range given in your puzzle input meet these criteria?
     * <p>
     * Your puzzle answer was 979.
     */
    private static void firstPart() {
        System.out.println("result1: " + IntStream.range(FROM, TO).filter(psw -> checkNumber(psw, (byte) 0)).count());
    }

    /**
     * --- Part Two ---
     * <p>
     * An Elf just remembered one more important detail: the two adjacent matching digits are not part of a larger group of matching digits.
     * <p>
     * Given this additional criterion, but still ignoring the range rule, the following are now true:
     * <p>
     * 112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
     * 123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).
     * 111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
     * <p>
     * How many different passwords within the range given in your puzzle input meet all of the criteria?
     * <p>
     * Your puzzle answer was 635.
     */
    private static void secondPart() {
        System.out.println("result2: " + IntStream.range(FROM, TO).filter(psw -> checkNumber(psw, (byte) 1)).count());
    }

    private static List<Integer> getDigits(int value) {
        String toString = "" + value;
        return Arrays.stream(toString.split("")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static boolean checkNumber(int password, byte CHECK_TYPE) {
        List<Integer> OK_DIGITS = new ArrayList<>();

        for (Integer digit : getDigits(password)) {
            if (OK_DIGITS.size() == 0 || OK_DIGITS.get(OK_DIGITS.size() - 1) <= digit) {
                OK_DIGITS.add(digit);
            } else {
                return false;
            }
        }

        Predicate<Integer> doubleCheck = (digit) -> {
            int index = OK_DIGITS.indexOf(digit);
            return OK_DIGITS.get(index).equals(OK_DIGITS.get(index + 1));
        };

        Predicate<Integer> rangeCheck = (digit) -> {
            int firstIndex = OK_DIGITS.indexOf(digit);
            boolean sizeCheck = OK_DIGITS.size() > firstIndex + 2;

            return !(sizeCheck && OK_DIGITS.get(firstIndex + 2).equals(OK_DIGITS.get(firstIndex)));
        };

        if (CHECK_TYPE == 0) {
            return OK_DIGITS.stream()
                    .distinct()
                    .filter(digit -> OK_DIGITS.indexOf(digit) < OK_DIGITS.size() - 1)
                    .anyMatch(doubleCheck);
        } else {
            return OK_DIGITS.stream()
                    .distinct()
                    .filter(digit -> OK_DIGITS.indexOf(digit) < OK_DIGITS.size() - 1)
                    .filter(doubleCheck)
                    .anyMatch(rangeCheck);
        }
    }
}
