package com.msendyka.adventofcode.p18;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;

import io.vavr.collection.List;

public class P18 {

    private static final String ENTRANCE = "@";

    public static void main(String[] args) {
        List<String> inputRaw = Functions.readInput("p18/test0.txt");
        Map<Point, String> labirynth = new HashMap<>();
        int y = 0;
        int x = 0;
        for (String line : inputRaw) {
            java.util.List<String> lineItems = line.chars().mapToObj(c -> (char) c).map(String::valueOf).collect(Collectors.toList());
            for (String item : lineItems) {
                labirynth.put(new Point(x++, y), item);
            }
            y++;
            x = 0;
        }
        Point entrance = labirynth.entrySet().stream().filter(a -> a.getValue().equals(ENTRANCE)).findFirst().get().getKey();
        java.util.List<GraphEdge> acessibleFields;
        int distance = 0 ;
        do {
            acessibleFields = findAcessibleFields(entrance, labirynth);
            if(acessibleFields.size() > 0) {
                labirynth.put(entrance, ".");
                labirynth.put(acessibleFields.get(0).to, "@");
                entrance = acessibleFields.get(0).to;
                distance += acessibleFields.get(0).distance;

            }
            System.out.println("Fields"  + acessibleFields);
            System.out.println("Labirynth"  + labirynth);

        } while (acessibleFields.size() > 0);
        System.out.println(distance);
    }

    private static java.util.List<GraphEdge> findAcessibleFields(Point entrance, Map<Point, String> labirynth) {
        Set<Point> accessiblePoints = new HashSet<>();
        findAccessible(entrance, labirynth, a -> new Point(a.x + 1, a.y), accessiblePoints);
        findAccessible(entrance, labirynth, a -> new Point(a.x - 1, a.y), accessiblePoints);

        System.out.println(accessiblePoints);
        accessiblePoints = accessiblePoints.stream().filter(p -> smallLetter(labirynth.get(p))).collect(Collectors.toSet());
        java.util.List<GraphEdge> graph = accessiblePoints.stream().map(p -> new GraphEdge(entrance, p, entrance.manhattanDistance(p))).collect(Collectors.toList());
        System.out.println("Graph: " + graph);
        return graph;
    }


    private static void findAccessible(Point entrance, Map<Point, String> labirynth, Function<Point, Point> nextPoint, Set<Point> accessiblePoints) {
        Point right = nextPoint.apply(entrance);
        String rightToCurrent = labirynth.get(right);
        if(rightToCurrent.equals(".")) {
            findAccessible(right, labirynth, nextPoint, accessiblePoints);
        }
        if(rightToCurrent.equals("#")) {
            return;
        }
        if(bigLetter(rightToCurrent)) {
            accessiblePoints.add(right);
        }
        if(smallLetter(rightToCurrent)) {
            accessiblePoints.add(right);
        }
    }

    private static boolean bigLetter(String val) {
        return val.matches("[A-Z]");
    }

    private static boolean smallLetter(String val) {
        return val.matches("[a-z]");
    }

    public static class GraphEdge {
        private Point from;
        private Point to;
        private int distance;

        public GraphEdge(Point from, Point to, int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "GraphEdge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", distance=" + distance +
                    '}';
        }
    }
}
