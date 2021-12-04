package com.msendyka.adventofcode;

import java.util.Objects;
import java.util.StringJoiner;

public class Point implements Comparable<Point> {
    public final int x;
    public final int y;

    public Point(int x, int y) {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Point.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }

    @Override
    public int compareTo(Point o) {
        int x = this.x - o.x;
        if (x == 0) {
            return this.y - o.y;
        }
        return x;
    }
}
