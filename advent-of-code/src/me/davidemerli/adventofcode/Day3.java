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

    /** --- Day 3: Crossed Wires ---

     The gravity assist was successful, and you're well on your way to the Venus refuelling station. During the rush back on Earth, the fuel management system wasn't completely installed, so that's next on the priority list.

     Opening the front panel reveals a jumble of wires. Specifically, two wires are connected to a central port and extend outward on a grid. You trace the path each wire takes as it leaves the central port, one wire per line of text (your puzzle input).

     The wires twist and turn, but the two wires occasionally cross paths. To fix the circuit, you need to find the intersection point closest to the central port. Because the wires are on a grid, use the Manhattan distance for this measurement. While the wires do technically cross right at the central port where they both start, this point does not count, nor does a wire count as crossing with itself.

     For example, if the first wire's path is R8,U5,L5,D3, then starting from the central port (o), it goes right 8, up 5, left 5, and finally down 3:

     ...........
     ...........
     ...........
     ....+----+.
     ....|....|.
     ....|....|.
     ....|....|.
     .........|.
     .o-------+.
     ...........

     Then, if the second wire's path is U7,R6,D4,L4, it goes up 7, right 6, down 4, and left 4:

     ...........
     .+-----+...
     .|.....|...
     .|..+--X-+.
     .|..|..|.|.
     .|.-X--+.|.
     .|..|....|.
     .|.......|.
     .o-------+.
     ...........

     These wires cross at two locations (marked X), but the lower-left one is closer to the central port: its distance is 3 + 3 = 6.

     Here are a few more examples:

     R75,D30,R83,U83,L12,D49,R71,U7,L72
     U62,R66,U55,R34,D71,R55,D58,R83 = distance 159
     R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
     U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = distance 135

     What is the Manhattan distance from the central port to the closest intersection?

     Your puzzle answer was 865. */
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
    
    /** --- Part Two ---

     It turns out that this circuit is very timing-sensitive; you actually need to minimize the signal delay.

     To do this, calculate the number of steps each wire takes to reach each intersection; choose the intersection where the sum of both wires' steps is lowest. If a wire visits a position on the grid multiple times, use the steps value from the first time it visits that position when calculating the total value of a specific intersection.

     The number of steps a wire takes is the total number of grid squares the wire has entered to get to that location, including the intersection being considered. Again consider the example from above:

     ...........
     .+-----+...
     .|.....|...
     .|..+--X-+.
     .|..|..|.|.
     .|.-X--+.|.
     .|..|....|.
     .|.......|.
     .o-------+.
     ...........

     In the above example, the intersection closest to the central port is reached after 8+5+5+2 = 20 steps by the first wire and 7+6+4+3 = 20 steps by the second wire for a total of 20+20 = 40 steps.

     However, the top-right intersection is better: the first wire takes only 8+5+2 = 15 and the second wire takes only 7+6+2 = 15, a total of 15+15 = 30 steps.

     Here are the best steps for the extra examples from above:

     R75,D30,R83,U83,L12,D49,R71,U7,L72
     U62,R66,U55,R34,D71,R55,D58,R83 = 610 steps
     R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
     U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = 410 steps

     What is the fewest combined steps the wires must take to reach an intersection?

     Your puzzle answer was 35038. */
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