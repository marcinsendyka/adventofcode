package com.msendyka.adventofcode.p1;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

public class P1 {

    public static void main(String[] args) {
        List<String> input = Functions.readInput("p1/input.txt");

        Number one = partOne(input);
        Number two = partTwo(input);
        System.out.println(one);
        System.out.println(two);
    }

    private static Number partTwo(List<String> input) {
        return input
                .map(Integer::valueOf)
                .map(a -> calculateFuelRequiredWithFuelIncluded(a))
                .sum();
    }

    private static int calculateFuelRequiredWithFuelIncluded(int mass) {
        int fuelRequired = calculateFuelRequired(mass);
        if(fuelRequired <= 0) {
            return 0;
        }
        return fuelRequired + calculateFuelRequiredWithFuelIncluded(fuelRequired);
    }

    private static Number partOne(List<String> input) {
        return input
                .map(Integer::valueOf)
                .map(P1::calculateFuelRequired)
                .sum();
    }

    private static int calculateFuelRequired(int mass) {
        return mass / 3 - 2;
    }
}
