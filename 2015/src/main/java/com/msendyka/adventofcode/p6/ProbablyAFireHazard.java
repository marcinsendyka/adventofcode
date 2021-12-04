package com.msendyka.adventofcode.p6;

import com.msendyka.adventofcode.Functions2015;
import com.msendyka.adventofcode.Point;
import io.vavr.API;
import io.vavr.Predicates;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

import java.util.StringJoiner;

import static io.vavr.API.*;


public class ProbablyAFireHazard {

    public static void main(String[] args) {

        List<String> strings = Functions2015.readInput("p6/input.txt");
        List<Instruction> instructions = strings.map(ProbablyAFireHazard::toInstruction);
        int partOne = partOne(instructions);
        int partTwo = partTwo(instructions);
        System.out.println(partOne);
        System.out.println(partTwo);
    }

    private static int partOne(List<Instruction> instructions) {
        Set<Point> points = Stream.range(0, 1000)
                .flatMap(y -> Stream.range(0, 1000)
                        .map(x -> new Point(x, y))).sorted().toLinkedSet();
        Set<Point> litPoints = instructions
                .map(ins -> getPointsWithType(ins.from, ins.to, ins.instructionType, points))
                .foldLeft(HashSet.empty(), (points1, instruction) -> API.Match(instruction._2)
                        .of(
                                Case($(Predicates.is(InstructionType.ON)), points1.addAll(instruction._1)),
                                Case($(Predicates.is(InstructionType.OFF)), points1.removeAll(instruction._1)),
                                Case($(Predicates.is(InstructionType.TOGGLE)), points1.removeAll(instruction._1).addAll(instruction._1.removeAll(points1)))
                        ));

        return litPoints.size();
    }

    private static int partTwo(List<Instruction> instructions) {
//        int[][] arr = new int[1000][1000];
//        for (int i = 0; i < 1000; i++) {
//            for (int j = 0; j < 1000; j++) {
//                arr[i][j] = 0;
//            }
//        }
//        int[][] litPoints = instructions
//                .map(ins -> getPointsWithType(ins.from, ins.to, ins.instructionType, points))
//                .foldLeft(arr, (points, instruction) -> {
//                    switch (instruction._2) {
//                        case ON:
//                            for (Point point : instruction._1) {
//                                points[point.x][point.y] += 1;
//                            }
//
//                            return points;
//                        case OFF:
//                            for (Point point : instruction._1) {
//                                points[point.x][point.y] -= 1;
//                                if (points[point.x][point.y] < 0) {
//                                    points[point.x][point.y] = 0;
//                                }
//                            }
//                            return points;
//                        case TOGGLE:
//                            for (Point point : instruction._1) {
//                                points[point.x][point.y] += 2;
//                            }
//                            return points;
//                        default:
//                            return null;
//                    }
//                });

        int sum1 = 0;
//        for (int[] arr1 : litPoints) {
//            for (int i : arr1) {
//                sum1 += i;
//            }
//
//        }
        return sum1;
    }

    private static Tuple2<Set<Point>, InstructionType> getPointsWithType(
            Point from,
            Point to,
            InstructionType instructionType,
            Set<Point> allPoints) {
        Set<Point> points1 = allPoints.filter(point -> point.x >= from.x)
                .filter(point -> point.x <= to.x)
                .filter(point -> point.y >= from.y)
                .filter(point -> point.y <= to.y).toSet();
        System.out.println("get points");
        return new Tuple2<>(points1, instructionType);
    }

    private static Instruction toInstruction(String instruction) {
        return Match(instruction).of(
                Case($(ins -> ins.startsWith("turn off")), createInstruction(instruction, InstructionType.OFF)),
                Case($(ins -> ins.startsWith("turn on")), createInstruction(instruction, InstructionType.ON)),
                Case($(ins -> ins.startsWith("toggle")), createInstruction(instruction, InstructionType.TOGGLE))
        );
    }

    private static Instruction createInstruction(String line, InstructionType instructionType) {
        String[] byComma = line.split(",");
        String[] first = byComma[0].split(" ");
        int x1 = Integer.parseInt(first[first.length - 1]);
        String[] second = byComma[1].split(" ");
        int y1 = Integer.parseInt(second[0]);
        int x2 = Integer.parseInt(second[second.length - 1]);
        int y2 = Integer.parseInt(byComma[byComma.length - 1]);
        return new Instruction(new Point(x1, y1), new Point(x2, y2), instructionType);
    }

    private static class Instruction {
        Point from;
        Point to;
        InstructionType instructionType;

        Instruction(Point from, Point to, InstructionType instructionType) {
            this.from = from;
            this.to = to;
            this.instructionType = instructionType;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Instruction.class.getSimpleName() + "[", "]")
                    .add("from=" + from)
                    .add("to=" + to)
                    .add("instructionType=" + instructionType)
                    .toString();
        }
    }

    private enum InstructionType {
        OFF, ON, TOGGLE
    }
}
