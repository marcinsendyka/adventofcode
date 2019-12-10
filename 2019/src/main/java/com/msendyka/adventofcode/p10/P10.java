package com.msendyka.adventofcode.p10;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import com.msendyka.adventofcode.Functions;

import io.vavr.collection.List;

public class P10 {

    public static void main(String[] args) {
        List<List<String>> map = Functions.readInput("p10/test.txt")
                .map(line -> line.chars().mapToObj(c -> (char) c).map(String::valueOf))
                .map(stream -> List.ofAll(stream));

        java.util.List<Point> asteroids = new ArrayList<>();
        int size = map.head().size();
        List<String> strings = map.flatMap(List::ofAll);
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < size; j++) {
                String s = strings.get(i * size + j);
                System.out.print(s + " ");
                if (s.equals("#")) {
                    asteroids.add(new Point(j, i));

                }
            }
            System.out.println("");
        }

        Point base = partOne(map, asteroids);
        System.out.println(base);
//        SkyMap skyMap = new SkyMap(map.head().size(), map.size());
//        List<Point> closest = skyMap.findClosest(base, List.ofAll(asteroids));
//        System.out.println(closest);
        SkyMap skyMap = new SkyMap(map.head().size(), map.size());
        List<Point> check = skyMap.findVisible(base, List.ofAll(asteroids));
        List<Point> toKill = List.ofAll(asteroids).remove(base).filter(check::contains);
        List<PointToDestory> toKillOrdered = toKill.map(p -> new PointToDestory(p, base));
        toKillOrdered = toKillOrdered.sorted(comparator);
        System.out.println(toKill);
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < size; j++) {
                String s = strings.get(i * size + j);
                if (base.y == i && base.x == j) {
                    System.out.print("X" + " ");
                } else if (toKill.contains(new Point(j, i))) {
                    System.out.print("O" + " ");
                } else {
                    System.out.print(s + " ");
                }

            }
            System.out.println("");
        }
    }
    private static Comparator<PointToDestory> comparator = new Comparator<PointToDestory>() {
        @Override
        public int compare(PointToDestory pointToDestory, PointToDestory t1) {
            int x = Integer.valueOf(pointToDestory.relativeToBase.x).compareTo(Integer.valueOf(t1.relativeToBase.x));
            if (x == 0) {
                return Integer.valueOf(pointToDestory.relativeToBase.y).compareTo(Integer.valueOf(t1.relativeToBase.y));
            }
            return x;
        }
    };
    private static class PointToDestory {
        private Point real;
        private Point relativeToBase;

        public PointToDestory(Point real, Point base) {
            this.real = real;
            this.relativeToBase = new Point(real.x - base.x, real.y - base.y);
        }

        @Override
        public String toString() {
            return "PointToDestory{" +
                    "real=" + real +
                    ", relativeToBase=" + relativeToBase +
                    '}';
        }
    }

    private static Point partOne(List<List<String>> map, java.util.List<Point> points) {
        int max = 0;
        Point bestPoint = null;
        for (Point a : points) {
            SkyMap skyMap = new SkyMap(map.head().size(), map.size());
            List<Point> check = skyMap.findVisible(a, List.ofAll(points));
            List<Point> visible = List.ofAll(points).remove(a).filter(check::contains);

            if (visible.size() > max) {
                max = visible.size();
                bestPoint = a;
                System.out.println(visible);
            }
        }
        System.out.println(max);
        return bestPoint;
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

        List<Point> findClosest(Point asteroid, List<Point> others) {
            List<Point> result = List.ofAll(others);
            for (Point b : others) {
                List<Point> pointsBetween = List.of();
                if (asteroid == b) {
                    continue;
                }
                int dxc = b.x - asteroid.x;
                int dyc = b.y - asteroid.y;
                for (Point p : others) {
                    if (p.equals(b)) {
                        continue;
                    }
                    int dxl = p.x - asteroid.x;
                    int dyl = p.y - asteroid.y;
                    int cross = dxc * dyl - dyc * dxl;
                    if (cross == 0 && between(dxl, dyl, asteroid, b, p)) {
//                        System.out.println("\t\t" + p);
                        result = result.remove(p);
                    }
                }
//                result = result.removeAll(pointsBetween);
                int x = 0;
            }
            return result;

        }

        List<Point> findVisible(Point asteroid, List<Point> others) {
//            System.out.println(asteroid);
            List<Point> result = List.ofAll(coordinates);

            for (Point b : others) {
//                System.out.println("\t" + b);
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
//                        System.out.println("\t\t" + p);
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

    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
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
