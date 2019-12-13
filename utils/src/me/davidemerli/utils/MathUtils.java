package me.davidemerli.utils;

public class MathUtils {
    public static long greatestCommonDivisor(long a, long b) {
        if (a == 0) return b;

        return greatestCommonDivisor(b % a, a);
    }

    public static long leastCommonMultiplier(long a, long b) {
        return (a * b) / greatestCommonDivisor(a, b);
    }
}
