package com.msendyka.adventofcode.p12;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.Objects;

public class P12 {

    public static final String INPUT = "p12/test.txt";

    public static void main(String[] args) {
        partOne();

        List<Moon> moons = Functions.readInput(INPUT)
                .map(string -> string.split(","))
                .map(strings -> findMoon(strings));

        Number initial = moons.map(moon -> moon.energy())
                .sum();
        long count = 0;
        while(true) {
            count++;
            updateGravity2(moons);
            updatePositions(moons);
            Number sum = moons.map(moon -> moon.energy())
                    .sum();
            if(sum.equals(initial)) {
                break;
            }
        }
        System.out.println(count);
    }

    private static void updatePositions(List<Moon> moons) {
        for (Moon m: moons) {
            m.x += m.velocityX;
            m.y += m.velocityY;
            m.z += m.velocityZ;
        }
    }
    private static void updateGravity2(List<Moon> moons) {
        int length = moons.length();
        List<Moon> byX = moons.sortBy(a -> a.x);
        List<Moon> byY= moons.sortBy(a -> a.y);
        List<Moon> byZ= moons.sortBy(a -> a.z);
        moons.forEach(
                moon -> {
                    int indexX = byX.indexOf(moon);
                    Option<Moon> firstSameX = byX.find(m -> m.x == moon.x);
                    Option<Moon> lastSameX = byX.findLast(m -> m.x == moon.x);
                    int firstIndexSameX = byX.indexOf(firstSameX.getOrElse(moon));
                    int indexSameX = byX.indexOf(lastSameX.getOrElse(moon));
                    moon.velocityX -= firstIndexSameX;
                    moon.velocityX += length - 1 - indexSameX;

                    int indexY = byY.indexOf(moon);
                    Option<Moon> firstSameY = byY.find(m -> m.y == moon.y);
                    int firstIndexSameY = byY.indexOf(firstSameY.getOrElse(moon));
                    Option<Moon> lastSameY = byY.findLast(m -> m.y == moon.y);
                    int indexSameY = byY.indexOf(lastSameY.getOrElse(moon));
                    moon.velocityY -= firstIndexSameY;
                    moon.velocityY += length - 1 - indexSameY;

                    int indexZ = byZ.indexOf(moon);
                    Option<Moon> firstSameZ = byZ.find(m -> m.z == moon.z);
                    int firstIndexSameZ = byZ.indexOf(firstSameZ.getOrElse(moon));
                    Option<Moon> lastSameZ = byZ.findLast(m -> m.z == moon.z);
                    int indexSameZ = byZ.indexOf(lastSameZ.getOrElse(moon));
                    moon.velocityZ -= firstIndexSameZ;
                    moon.velocityZ += length - 1 - indexSameZ;
                }
        );
    }

    private static void updateGravity(List<Moon> moons) {
        for (Moon m : moons) {
            for (Moon m2 : moons) {
                if (m == m2) continue;
                if(m.x > m2.x) {
                    m.velocityX -=1;
                } else if (m.x < m2.x) {
                    m.velocityX +=1;
                }
                if(m.y > m2.y) {
                    m.velocityY -=1;
                } else if (m.y < m2.y) {
                    m.velocityY +=1;
                }
                if(m.z > m2.z) {
                    m.velocityZ -=1;
                } else if (m.z < m2.z) {
                    m.velocityZ +=1;
                }
            }
        }
    }

    private static void partOne() {
        List<Moon> moons = Functions.readInput(INPUT)
                .map(string -> string.split(","))
                .map(strings -> findMoon(strings));


        for (int i = 0; i < 100; i++) {
            updateGravity2(moons);
            updatePositions(moons);
        }
        Number sum = moons.map(moon -> moon.energy())
                .sum();

        System.out.println(sum);
    }

    private static Moon findMoon(String[] strings) {
        String[] xSplit = strings[0].split("=");
        int x = Integer.valueOf(xSplit[xSplit.length - 1]);
        String[] ySplit = strings[1].split("=");
        int y = Integer.valueOf(ySplit[ySplit.length - 1]);
        String[] zSPlit = strings[2].split("=");
        int z = Integer.valueOf(zSPlit[zSPlit.length - 1].substring(0, zSPlit[zSPlit.length - 1].length() - 1));
        return new Moon(x, y, z);
    }

    private static class Moon {
        int x;
        int y;
        int z;
        int velocityX;
        int velocityY;
        int velocityZ;

        public Moon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            velocityX = 0;
            velocityY = 0;
            velocityZ = 0;
        }

        public int energy() {
            int potential = Math.abs(x) + Math.abs(y) +Math.abs(z);
            int kinetic = Math.abs(velocityX) + Math.abs(velocityY) +Math.abs(velocityZ);
            return potential * kinetic;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return x == moon.x &&
                    y == moon.y &&
                    z == moon.z &&
                    velocityX == moon.velocityX &&
                    velocityY == moon.velocityY &&
                    velocityZ == moon.velocityZ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, velocityX, velocityY, velocityZ);
        }

        @Override
        public String toString() {
            return "Moon{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", velocityX=" + velocityX +
                    ", velocityY=" + velocityY +
                    ", velocityZ=" + velocityZ +
                    '}';
        }
    }
}
