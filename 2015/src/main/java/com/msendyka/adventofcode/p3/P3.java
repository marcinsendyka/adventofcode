package com.msendyka.adventofcode.p3;

import com.msendyka.adventofcode.Functions;
import io.vavr.Function1;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

import static io.vavr.API.*;


import java.util.Objects;

public class P3 {
    // https://adventofcode.com/2015/day/3
    public static void main(String[] args) {
        List<String> inputLines = List.ofAll(Functions.readInput("p3/input.txt")
                .head()
                .chars()
                .mapToObj(c -> (char) c))
                .map(String::valueOf);
        partOne(inputLines);
    }

    private static void partOne(List<String> input) {
        Set<Point> points = HashSet.empty();
        Point currentLocation = new Point(0, 0);
        points = points.add(currentLocation);
        List<Function1<Point, Point>> instructions = input
                .map(P3::moveInstruction).toList();
        for (int i = 0; i < instructions.size(); i++) {
            currentLocation = instructions.get(i).apply(currentLocation);
            points = points.add(currentLocation);
        }
        System.out.println(points.size());
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

    private static class Point {
        private int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
