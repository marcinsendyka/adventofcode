package com.msendyka.adventofcode.p10;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.abs;

public class P10 {

    public static void main(String[] args) {
        List<List<String>> map = Functions.readInput("p10/test.txt")
                .map(line -> line.chars().mapToObj(c -> (char) c).map(String::valueOf))
                .map(stream -> List.ofAll(stream));


        java.util.List<Point> points = new ArrayList<>();
        int size = map.head().size();
        List<String> strings = map.flatMap(List::ofAll);
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < size; j++) {
                String s = strings.get(i * size + j);
                System.out.print(s + " ");
                if (s.equals("#")) {
                    points.add(new Point(j, i));

                }
            }
            System.out.println("");
        }
        System.out.println(points);
        int max = 0 ;
        Point bestPoint = null;
        for (Point a : points) {
            SkyMap skyMap = new SkyMap(size, map.size());
            List<Point> check = skyMap.check(a, List.ofAll(points));
            int count = 0;
            for (Point p : points) {
                if (p == a) continue;
                if (check.contains(p)) {
                    count++;
                }
            }
            if(count > max) {
                max = count;
                bestPoint = a;
            }
        }
        System.out.println(max);
        System.out.println(bestPoint);
        SkyMap skyMap = new SkyMap(size, map.size());
        List<Point> check = skyMap.check(bestPoint, List.ofAll(points));
        java.util.List<Point> toRemove = new ArrayList<>();
        for (Point p : points) {
            if (p == bestPoint) continue;
            if (check.contains(p)) {
                toRemove.add(p);
            }
        }
        System.out.println(toRemove);
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
                    points.add(new Point(j, i));
                }
            }
            coordinates = List.ofAll(points);
        }

        List<Point> check(Point asteroid, List<Point> others) {
//            System.out.println(asteroid);
            List<Point> result = List.ofAll(coordinates);

            for (Point b : others) {
//                System.out.println("\t" + b);
                if (asteroid == b) continue;
                int xdist = b.x - asteroid.x;
                int ydist = b.y - asteroid.y;
                int dxc = b.x - asteroid.x;
                int dyc = b.y - asteroid.y;
                for (Point p : coordinates) {
                    if(p.equals(b)) continue;
                    int dxl = p.x - asteroid.x;
                    int dyl = p.y - asteroid.y;
                    int cross = dxc * dyl - dyc * dxl;
                    int distxp = p.x - asteroid.x;
                    int distyp = p.y - asteroid.y;
                    if (cross == 0 && between(dxl, dyl, asteroid, b, p)) {
//                        System.out.println("\t\t" + p);
                        result = result.remove(p);
                    }
                }

            }
//            int count = 0;
//            for (Point p : others) {
//                if (p == a) continue;
//                if (result.contains(p)) {
//                    count++;
//                }
//            }
            return result;

        }
    }
    static boolean  between(int dxl, int dyl, Point point1, Point currPoint, Point point2) {
        if (abs(dxl) >= abs(dyl))
            return dxl > 0 ?
                    point1.x <= currPoint.x && currPoint.x <= point2.x :
                    point2.x <= currPoint.x && currPoint.x <= point1.x;
        else
            return dyl > 0 ?
                    point1.y <= currPoint.y && currPoint.y <= point2.y :
                    point2.y <= currPoint.y && currPoint.y <= point1.y;
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
