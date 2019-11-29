package com.msendyka.adventofcode.p3;

import com.msendyka.adventofcode.Functions;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import static io.vavr.API.*;

public class P3 {
    // https://adventofcode.com/2015/day/3
    public static void main(String[] args) {
        List<String> input = Functions.readInputOneLine("p3/input.txt");

        System.out.println(partOne(input).size());
        System.out.println(partTwo(input).size());
    }

    private static List<Point> partTwo(List<String> input) {
        Tuple2<List<Tuple2<String, Integer>>, List<Tuple2<String, Integer>>> partitioned =
                input.zipWithIndex()
                        .partition(a -> a._2 % 2 == 0);
        List<Point> firstPart = partOne(partitioned._1.map(Tuple2::_1));
        List<Point> secondPart = partOne(partitioned._2.map(Tuple2::_1));
        return firstPart.appendAll(secondPart).distinct();
    }

    private static List<Point> partOne(List<String> input) {
        return input.map(P3::moveInstruction)
                .foldLeft(List.of(new Point(0, 0)), P3::appendNextInstruction)
                .distinct();
    }

    private static List<Point> appendNextInstruction(List<Point> visitedPoints, Function1<Point, Point> nextInstruction) {
        return visitedPoints.append(nextInstruction.apply(visitedPoints.last()));
    }

    private static Function1<Point, Point> moveInstruction(String moveInstruction) {
        return Match(moveInstruction).of(
                Case($(">"), newPoint(1, 0)),
                Case($("^"), newPoint(0, 1)),
                Case($("v"), newPoint(0, -1)),
                Case($("<"), newPoint(-1, 0)),
                Case($(), o -> {
                    throw new IllegalArgumentException();
                })
        );
    }

    private static Function1<Point, Point> newPoint(int xChange, int yChange) {
        return point -> new Point(point.x + xChange, point.y + yChange);
    }

}
