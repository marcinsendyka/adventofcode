package com.msendyka.adventofcode.p10;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;
import io.vavr.collection.List;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.abs;

public class P10 {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        List<List<String>> map = Functions.readInput("p10/input.txt")
                .map(line -> line.chars().mapToObj(c -> (char) c).map(String::valueOf))
                .map(stream -> List.ofAll(stream));

        java.util.List<Point> asteroids = new ArrayList<>();
        int width = map.head().size();
        int height = map.size();
        List<String> strings = map.flatMap(List::ofAll);
        visualiseInitialInput(asteroids, width, height, strings);

        Point base = partOne(List.ofAll(asteroids), width, height);
        System.out.println(base);
        partTwo(map, List.ofAll(asteroids), width, strings, base);
    }

    private static void visualiseInitialInput(java.util.List<Point> asteroids, int width, int height, List<String> strings) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String s = strings.get(i * width + j);
                soutSameLine(s + " ");
                if (s.equals("#")) {
                    asteroids.add(new Point(j, i));

                }
            }
            sout("");
        }
    }

    private static Point partOne(List<Point> points, int width, int height) {
        int max = 0;
        Point bestPoint = null;
        for (Point a : points) {
            SkyMap skyMap = new SkyMap(width, height);
            List<Point> check = skyMap.findVisible(a, List.ofAll(points));
            List<Point> visible = List.ofAll(points).remove(a).filter(check::contains);

            if (visible.size() > max) {
                max = visible.size();
                bestPoint = a;
                sout(visible);
            }
        }
        System.out.println(max);
        return bestPoint;
    }

    private static void partTwo(List<List<String>> map, List<Point> asteroids, int width, List<String> strings, Point base) {
        List<Point> toDestroy = List.ofAll(asteroids);
        int i = 1;
        while (toDestroy.size() > 1) {
            List<Point> killed = fire(map, toDestroy, width, strings, base);
            for (Point p : killed) {
                if (i == 200) {
                    System.out.println(i + " " + p);
                }
                sout(i++ + " " + p);
            }
            toDestroy = toDestroy.removeAll(killed);
        }
    }

    private static List<Point> fire(List<List<String>> map, List<Point> asteroids, int size, List<String> strings, Point base) {
        SkyMap skyMap = new SkyMap(map.head().size(), map.size());
        List<Point> visibleFromBase = skyMap.findVisible(base, List.ofAll(asteroids));
        List<Point> toKill = List.ofAll(asteroids).remove(base).filter(visibleFromBase::contains);
        List<Point> toKillOrdered = toKill.filter(p -> p.x >= base.x && p.y < base.y).sorted(new Comp(base));
        toKillOrdered = toKillOrdered.appendAll(toKill.filter(p -> p.x >= base.x && p.y >= base.y).sorted(new Comp(base)));
        toKillOrdered = toKillOrdered.appendAll(toKill.filter(p -> p.x < base.x && p.y >= base.y).sorted(new Comp(base)));
        toKillOrdered = toKillOrdered.appendAll(toKill.filter(p -> p.x < base.x && p.y < base.y).sorted(new Comp(base)));
        sout(toKillOrdered);
        visualiseLaserShot(map, size, strings, base, toKill, toKillOrdered);
        return toKillOrdered;
    }

    private static void visualiseLaserShot(List<List<String>> map, int size, List<String> strings, Point base, List<Point> toKill, List<Point> toKillOrdered) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < size; j++) {
                String s = strings.get(i * size + j);
                if (base.y == i && base.x == j) {
                    soutSameLine("X" + " ");
                } else if (toKill.contains(new Point(j, i))) {
                    int order = toKillOrdered.indexOf(new Point(j, i));
                    if (order == -1) order = 0;
                    soutSameLine(order + " ");
                } else {
                    soutSameLine(s + " ");
                }

            }
            sout("");
        }
    }

    private static void sout(Object toPrint) {
        if (DEBUG) {
            System.out.println(toPrint);
        }
    }

    private static void soutSameLine(Object toPrint) {
        if (DEBUG) {
            System.out.print(toPrint);
        }
    }
    private static class Comp implements Comparator<Point> {


        private Point basePoint;

        public Comp(Point basePoint) {
            this.basePoint = basePoint;
        }

        @Override
        public int compare(Point o1, Point o2) {
            Double o1Slope = slope(o1, basePoint);
            Double o2Slope = slope(o2, basePoint);
            return o1Slope.compareTo(o2Slope);
        }
        private Double slope(Point o1, Point o2) {
            double divider = o2.x - o1.x;
            if (divider == 0) {
                return -100000.;
            }
            if (o2.y - o1.y == 0) {
                return Double.MIN_VALUE;
            }
            return (o2.y - o1.y) / divider;
        }

    }

    private static class SkyMap {
        List<Point> coordinates;
        int width;
        int height;

        public SkyMap(int width, int height) {
            this.width = width;
            this.height = height;
            java.util.List<Point> points = new ArrayList<>();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    points.add(new Point(i, j));
                }
            }
            coordinates = List.ofAll(points);
        }


        List<Point> findVisible(Point asteroid, List<Point> others) {
            List<Point> result = List.ofAll(coordinates);

            for (Point b : others) {
                if (asteroid == b) {
                    continue;
                }
                int dxc = b.x - asteroid.x;
                int dyc = b.y - asteroid.y;
                for (Point p : coordinates) {
                    if (p.equals(b)) {
                        continue;
                    }
                    int dxl = p.x - asteroid.x;
                    int dyl = p.y - asteroid.y;
                    int cross = dxc * dyl - dyc * dxl;
                    if (cross == 0 && between(dxl, dyl, asteroid, b, p)) {
                        result = result.remove(p);
                    }
                }

            }
            return result;

        }
    }

    static boolean between(int dxl, int dyl, Point point1, Point currPoint, Point point2) {
        if (abs(dxl) >= abs(dyl)) {
            return dxl > 0 ?
                    point1.x <= currPoint.x && currPoint.x <= point2.x :
                    point2.x <= currPoint.x && currPoint.x <= point1.x;
        } else {
            return dyl > 0 ?
                    point1.y <= currPoint.y && currPoint.y <= point2.y :
                    point2.y <= currPoint.y && currPoint.y <= point1.y;
        }
    }

}
