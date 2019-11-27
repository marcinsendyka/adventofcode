package com.msendyka.adventofcode.p2;

import com.msendyka.adventofcode.Functions;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

public class P2 {

    // https://adventofcode.com/2015/day/2
    public static void main(String[] args) {
        List<String> inputLines = Functions.readInput("p2/input.txt");
        partOne(inputLines);
        partTwo(inputLines);
    }

    private static void partTwo(List<String> input) {
        Number result = input.map(line -> line.split("x"))
                .map(P2::toTuple)
                .map(sideDimensions -> sideDimensions.apply(P2::multiply) + getCubicVolume(sideDimensions))
                .sum();

        System.out.println(result);

    }

    private static Integer multiply(Integer a, Integer b, Integer c) {
        return a * b * c;
    }

    private static Integer getCubicVolume(Tuple3<Integer, Integer, Integer> sideDimensions) {
        return toSeq(sideDimensions)
                .sorted()
                .subSequence(0, 2)
                .map(a -> a * 2)
                .sum()
                .intValue();
    }

    private static void partOne(List<String> inputLines) {
        Number result = inputLines.map(line -> line.split("x"))
                .map(P2::toTuple)
                .map(P2::toSideDimensions)
                .map(P2::calculateSizeOfRequiredFabricForEachPresent)
                .sum();

        System.out.println(result);

    }

    private static Integer smallestSide(Tuple3<Integer, Integer, Integer> surfaceDimensions) {
        return toSeq(surfaceDimensions).min().getOrElseThrow(() -> new IllegalArgumentException());
    }

    private static Seq<Integer> toSeq(Tuple3<Integer, Integer, Integer> surfaceDimensions) {
        return (Seq<Integer>) surfaceDimensions.toSeq();
    }

    private static Integer findPresentSidesSizes(Tuple3<Integer, Integer, Integer> surfaceDimensions) {
        return 2 * surfaceDimensions._1 + 2 * surfaceDimensions._2 + 2 * surfaceDimensions._3;
    }

    private static Tuple3<Integer, Integer, Integer> toTuple(String[] array) {
        return new Tuple3<>(Integer.valueOf(array[0]), Integer.valueOf(array[1]), Integer.valueOf(array[2]));
    }

    private static Tuple3<Integer, Integer, Integer> toSideDimensions(Tuple3<Integer, Integer, Integer> dimensions) {
        return new Tuple3<>(
                dimensions._1 * dimensions._2,
                dimensions._2 * dimensions._3,
                dimensions._1 * dimensions._3);
    }

    private static Integer calculateSizeOfRequiredFabricForEachPresent(Tuple3<Integer, Integer, Integer> surfaceDimensions) {
        return findPresentSidesSizes(surfaceDimensions) + smallestSide(surfaceDimensions);
    }
}
