package com.msendyka.adventofcode.p24;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;
import io.vavr.collection.List;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class P24 {

    private static final int MINUTES = 200;
    private static final int LEVELS_COUNT = 200;
    private static final int GRID_SIZE = 5;

    public static void main(String[] args) {

        partOne();
        List<String> strings = Functions.readInput("p24/input.txt");
        Map<Integer, Set<Point>> levels = new HashMap<>();
        Set<Point> points = new HashSet<>();
        int y = 0;
        int x = 0;
        for (String line : strings) {
            java.util.List<String> lineItems = line.chars().mapToObj(c -> (char) c).map(String::valueOf).collect(Collectors.toList());
            for (String item : lineItems) {
                if (item.equals("#")) {
                    points.add(new Point(x, y));
                }
                x++;
            }
            y++;
            x = 0;
        }
        levels.put(0, points);
        for (int i = 1; i <= LEVELS_COUNT; i++) {
            levels.put(i, new HashSet<>());
        }
        for (int i = -1; i >= -LEVELS_COUNT; i--) {
            levels.put(i, new HashSet<>());
        }
        for (int i = 0; i < MINUTES; i++) {
            Map<Integer, Set<Point>> iterationResults = new HashMap<>();
            for (int j = LEVELS_COUNT; j > -LEVELS_COUNT; j--) {
                Set<Point> pointsAbove = levels.get(j + 1);
                Set<Point> pointsBelow = levels.get(j - 1);
                if (pointsAbove == null) {
                    pointsAbove = new HashSet<>();
                }
                if (pointsBelow == null) {
                    pointsBelow = new HashSet<>();
                }
                Set<Point> itResult = iterationPart2(levels.get(j), pointsAbove, pointsBelow);
                iterationResults.put(j, itResult);
            }
            for (int j = LEVELS_COUNT; j > -LEVELS_COUNT; j--) {
                levels.put(j, iterationResults.get(j));
            }
        }
        long pointCount = levels.values().stream().mapToLong(Set::size).sum();
        System.out.println(pointCount);

    }

    private static Set<Point> iterationPart2(Set<Point> points, Set<Point> pointsAbove, Set<Point> pointsBelow) {
        Set<Point> toDie = new HashSet<>();
        Set<Point> toInfest = new HashSet<>();
        findToDiePart2(points, toDie, pointsAbove, pointsBelow);
        adjacent2(points, toInfest, pointsAbove, pointsBelow);
        if (toInfest.contains(new Point(2, 2))) {
            throw new IllegalStateException();
        }
        Set<Point> result = new HashSet<>(points);
        result.removeAll(toDie);
        result.addAll(toInfest);
        return result;
    }

    private static void adjacent2(Set<Point> points, Set<Point> toInfest, Set<Point> pointsAbove, Set<Point> pointsBelow) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Point point = new Point(i, j);
                if (!points.contains(point)) {
                    int adjacent = countAdjacent2(points, point, pointsAbove, pointsBelow);
                    if ((adjacent == 1 || adjacent == 2) && (point.x != 2 || point.y != 2)) {
                        toInfest.add(point);
                    }

                }
            }
        }
    }

    private static void findToDiePart2(Set<Point> points, Set<Point> toDie, Set<Point> pointsAbove, Set<Point> pointsBelow) {
        for (Point point : points) {
            int adjacent = countAdjacent2(points, point, pointsAbove, pointsBelow);
            if (adjacent != 1) {
                toDie.add(point);
            }
        }
    }

    private static int countAdjacent2(Set<Point> points, Point point, Set<Point> pointsAbove, Set<Point> pointsBelow) {
        int adjacent = 0;

        Point right = new Point(point.x + 1, point.y);
        Point left = new Point(point.x - 1, point.y);
        Point down = new Point(point.x, point.y - 1);
        Point up = new Point(point.x, point.y + 1);
        Point recursiveCenter = new Point(2, 2);

        if (points.contains(right)) {
            adjacent++;
        }
        if (points.contains(left)) {
            adjacent++;
        }
        if (points.contains(down)) {
            adjacent++;
        }
        if (points.contains(up)) {
            adjacent++;
        }
        if (right.equals(recursiveCenter)) {
            Set<Point> recursivePoints = pointsBelow.stream().filter(p -> p.x == 0).collect(Collectors.toSet());
            adjacent += recursivePoints.size();
        }
        if (left.equals(recursiveCenter)) {
            Set<Point> recursivePoints = pointsBelow.stream().filter(p -> p.x == 4).collect(Collectors.toSet());
            adjacent += recursivePoints.size();
        }
        if (down.equals(recursiveCenter)) {
            Set<Point> recursivePoints = pointsBelow.stream().filter(p -> p.y == 4).collect(Collectors.toSet());
            adjacent += recursivePoints.size();
        }
        if (up.equals(recursiveCenter)) {
            Set<Point> recursivePoints = pointsBelow.stream().filter(p -> p.y == 0).collect(Collectors.toSet());
            adjacent += recursivePoints.size();
        }
        if (left.x == -1) {
            if (pointsAbove.contains(new Point(1, 2))) {
                adjacent++;
            }
        }
        if (right.x == GRID_SIZE) {
            if (pointsAbove.contains(new Point(3, 2))) {
                adjacent++;
            }
        }
        if (up.y == GRID_SIZE) {
            if (pointsAbove.contains(new Point(2, 3))) {
                adjacent++;
            }
        }
        if (down.y == -1) {
            if (pointsAbove.contains(new Point(2, 1))) {
                adjacent++;
            }
        }

        return adjacent;
    }

    private static void adjacent(Set<Point> points, Set<Point> toInfest) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Point point = new Point(i, j);
                if (!points.contains(point)) {
                    int adjacent = countAdjacent(points, point);
                    if (adjacent == 1 || adjacent == 2) {
                        toInfest.add(point);
                    }

                }
            }
        }
    }

    private static void partOne() {
        List<String> strings = Functions.readInput("p24/input.txt");
        Set<Point> points = new HashSet<>();
        int y = 0;
        int x = 0;
        for (String line : strings) {
            java.util.List<String> lineItems = line.chars().mapToObj(c -> (char) c).map(String::valueOf).collect(Collectors.toList());
            for (String item : lineItems) {
                if (item.equals("#")) {
                    points.add(new Point(x, y));
                }
                x++;
            }
            y++;
            x = 0;
        }
        System.out.println(points);
        visualiseInitialInput(points);
        Set<Set<Point>> results = new HashSet<>();
        while (true) {
            Set<Point> iterationResult = iteration(points);
            if (results.contains(iterationResult)) {
                points = iterationResult;
                break;
            } else {
                results.add(iterationResult);
                points = iterationResult;
            }

        }
        System.out.println();
        visualiseInitialInput(points);
        long result = 0;
        for (Point point : points) {
            int ratingPower = point.x + point.y * GRID_SIZE;
            double pow = Math.pow(2, ratingPower);
            result += pow;
        }
        System.out.println(result);
    }

    private static Set<Point> iteration(Set<Point> points) {
        Set<Point> toDie = new HashSet<>();
        Set<Point> toInfest = new HashSet<>();
        findToDie(points, toDie);
        adjacent(points, toInfest);
        Set<Point> result = new HashSet<>(points);
        result.removeAll(toDie);
        result.addAll(toInfest);
        return result;
    }

    private static void findToDie(Set<Point> points, Set<Point> toDie) {
        for (Point point : points) {
            int adjacent = countAdjacent(points, point);
            if (adjacent != 1) {
                toDie.add(point);
            }
        }
    }

    private static int countAdjacent(Set<Point> points, Point point) {
        int adjacent = 0;
        if (points.contains(new Point(point.x + 1, point.y))) {
            adjacent++;
        }
        if (points.contains(new Point(point.x - 1, point.y))) {
            adjacent++;
        }
        if (points.contains(new Point(point.x, point.y - 1))) {
            adjacent++;
        }
        if (points.contains(new Point(point.x, point.y + 1))) {
            adjacent++;
        }
        return adjacent;
    }

    private static void visualiseInitialInput(Set<Point> bugs) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (bugs.contains(new Point(j, i))) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
