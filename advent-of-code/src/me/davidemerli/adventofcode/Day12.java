package me.davidemerli.adventofcode;

import me.davidemerli.utils.MathUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static me.davidemerli.utils.SettingsReader.getFileFromWorkingDir;
import static me.davidemerli.utils.SettingsReader.getLinesFromFile;

public class Day12 {

    private static List<Moon> moons = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        List<String> inputList = getLinesFromFile(getFileFromWorkingDir("input/day12.txt"));

        inputList.stream()
                .map(s -> s.substring(1, s.length() - 1))
                .map(s -> s.split(", "))
                .forEach(strings -> {
                    int x = Integer.parseInt(strings[0].split("x=")[1]);
                    int y = Integer.parseInt(strings[1].split("y=")[1]);
                    int z = Integer.parseInt(strings[2].split("z=")[1]);

                    moons.add(new Moon(x, y, z));
                });

        firstPart();
        secondPart();
    }

    private static void firstPart() {
        IntStream.range(0, 1000).forEach(i -> step());
        int result = moons.stream().map(Moon::energy).reduce((sum, el) -> sum += el).orElse(0);

        System.out.println("result1: " + result);
    }

    private static void secondPart() {
        String[] initialConfig = getMoonsConfiguration();

        int xTimes = 0;
        int yTimes = 0;
        int zTimes = 0;

        for (int i = 1; true; i++) {
            step();
            String[] currentConfig = getMoonsConfiguration();

            if (xTimes == 0 && currentConfig[0].equals(initialConfig[0])) {
                xTimes = i;
            }

            if (yTimes == 0 && currentConfig[1].equals(initialConfig[1])) {
                yTimes = i;
            }

            if (zTimes == 0 && currentConfig[2].equals(initialConfig[2])) {
                zTimes = i;
            }

            if (xTimes != 0 && yTimes != 0 && zTimes != 0) break;
        }

        long result = MathUtils.leastCommonMultiplier(MathUtils.leastCommonMultiplier(xTimes, yTimes), zTimes);

        System.out.println("result2: " + result);
    }

    private static String[] getMoonsConfiguration() {
        String confX = moons.stream().map(moon -> moon.x + " " + moon.vX).reduce((s, s2) -> s += " " + s2).orElse("");
        String confY = moons.stream().map(moon -> moon.y + " " + moon.vY).reduce((s, s2) -> s += " " + s2).orElse("");
        String confZ = moons.stream().map(moon -> moon.z + " " + moon.vZ).reduce((s, s2) -> s += " " + s2).orElse("");
        return new String[]{confX, confY, confZ};
    }

    private static void step() {
        for (int i = 0; i < moons.size(); i++) {
            for (int j = i + 1; j < moons.size(); j++) {
                Moon moon1 = moons.get(i);
                Moon moon2 = moons.get(j);

                moon1.vX += Integer.compare(moon2.x, moon1.x);
                moon1.vY += Integer.compare(moon2.y, moon1.y);
                moon1.vZ += Integer.compare(moon2.z, moon1.z);
            }
        }

        moons.forEach(moon -> {
            moon.x += moon.vX;
            moon.y += moon.vY;
            moon.z += moon.vZ;
        });
    }

    private static class Moon {
        int x, y, z;
        int vX, vY, vZ;

        Moon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int energy() {
            int potentialEnergy = Math.abs(x) + Math.abs(y) + Math.abs(z);
            int kineticEnergy = Math.abs(vX) + Math.abs(vY) + Math.abs(vZ);
            return potentialEnergy * kineticEnergy;
        }
    }
}