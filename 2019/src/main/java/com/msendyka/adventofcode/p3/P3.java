package com.msendyka.adventofcode.p3;

import com.msendyka.adventofcode.Functions;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class P3 {

    public static void main(String[] args) {
        List<List<String>> inputs = Functions
                .readInput("p3/input.txt")
                .map(line -> line.split(","))
                .map(arr -> List.ofAll(Arrays.asList(arr)));
        Set<Point> firstSet = new HashSet<>();
        Set<Point> secondSet = new HashSet<>();

        List<Set<Point>> sets = inputs.map(list ->
                list.map(P3::parse)
                        .map(P3::getLines)
                        .map(P3::fillSet))
                .flatMap(l -> l.toStream());


//        inputs2._1.flatMap(a -> a.stream())
//        inputs2._1.retainAll(inputs2._2);
//
//        Set<Point> intersections = inputs2._1;
//
//        Stream<Integer> resultFirst = Stream.ofAll(intersections)
//                .map(point -> Math.abs(point.x) + Math.abs(point.y)).sorted();
//
//        Stream<Integer> resultSecond = Stream.ofAll(firstSet)
//                .map(point -> point.distance + secondSet
//                        .stream()
//                        .filter(p -> p.equals(point))
//                        .findFirst()
//                        .get().distance)
//                .sorted();
//        System.out.println(resultFirst);
//        System.out.println(resultSecond);
        int i = 0 ;
    }

    private static Tuple2<Integer, Integer> getLines(Tuple2<String, Integer> tuple1) {
        String s1 = tuple1._1;
        switch (s1) {
            case "R":
                return new Tuple2<>(tuple1._2, 0);
            case "D":
                return new Tuple2<>(0, -tuple1._2);
            case "U":
                return new Tuple2<>(0, tuple1._2);
            case "L":
                return new Tuple2<>(-tuple1._2, 0);
        }
        throw new IllegalArgumentException();
    }


    private static Tuple2<String, Integer> parse(String string) {
        return new Tuple2<>(string.substring(0, 1), Integer.valueOf(string.substring(1)));
    }

    private static Set<Point> fillSet(Tuple2<Integer, Integer> instruction) {
        Set<Point> set = new HashSet<>();
        int distance = 0;
        int x = 0, y = 0;

        for (int i = 0; i < Math.abs(instruction._1) + Math.abs(instruction._2); i++) {
            distance += 1;
            if (instruction._1 > 0) {
                x++;
            }
            if (instruction._1 < 0) {
                x--;
            }
            if (instruction._2 > 0) {
                y++;
            }
            if (instruction._2 < 0) {
                y--;
            }
            set.add(new Point(x, y, distance));

        }
        return set;
    }

    public static class Point {
        int x, y;
        int distance;


        Point(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
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

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
