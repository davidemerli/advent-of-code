package me.davidemerli.adventofcode;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.davidemerli.utils.SettingsReader.*;

public class Day3 {

    private static List<Point> wire1List = new ArrayList<>();
    private static List<Point> wire2List = new ArrayList<>();
    private static final Point CENTER = new Point(0, 0);

    public static void main(String[] args) throws IOException {
        firstPart();
        secondPart();
    }

    private static void firstPart() throws IOException {
        List<String> inputList = getLinesFromFile(getFileFromWorkingDir("input/day3.txt"));

        List<String> wire1Steps = Arrays.asList(inputList.get(0).split(","));
        List<String> wire2Steps = Arrays.asList(inputList.get(1).split(","));

        fillWireList(wire1Steps, wire1List);
        fillWireList(wire2Steps, wire2List);

        int min = Integer.MAX_VALUE;

        for (Point point : wire1List.stream().filter(wire2List::contains).collect(Collectors.toList())) {
            if (!point.equals(CENTER)) {
                int mDist = manhattanDistance(point, CENTER);

                if (mDist < min) {
                    min = mDist;
                }
            }
        }

        System.out.println("result1: " + min);
    }

    private static void secondPart() throws IOException {
        int min = Integer.MAX_VALUE;

        for (Point point : wire1List.stream().filter(wire2List::contains).collect(Collectors.toList())) {
            if (!point.equals(CENTER)) {
                int mDist = stepDistance(point, wire1List, wire2List);
                if (mDist < min) {
                    min = mDist;
                }
            }
        }

        System.out.println("result2: " + min);
    }

    private static int stepDistance(Point p, List<Point> w1List, List<Point> w2List) {
        return w1List.indexOf(p) + w2List.indexOf(p);
    }

    private static int manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    private static void fillWireList(List<String> stepList, List<Point> wireList) {
        int xCounter = 0, yCounter = 0;

        for (String s : stepList) {
            char direction = s.charAt(0);
            int quantity = Integer.parseInt(s.substring(1));

            Vector vDir = getDirection(direction);

            for (int i = 0; i < quantity; i++) {
                assert vDir != null;

                Point p = new Point(xCounter, yCounter);

                xCounter += vDir.x;
                yCounter += vDir.y;

                wireList.add(p);
            }
        }
    }

    private static Vector getDirection(char c) {
        if (c == 'U') return new Vector(0, -1);
        if (c == 'D') return new Vector(0, 1);
        if (c == 'L') return new Vector(-1, 0);
        if (c == 'R') return new Vector(1, 0);
        return null;
    }

    private static class Vector {
        int x, y;

        Vector(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}