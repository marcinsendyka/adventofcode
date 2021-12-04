package com.msendyka.adventofcode.p15;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;
import io.vavr.Function2;
import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.*;

public class P15 {

    private static Integer NORTH = 1;
    private static Integer SOUTH = 2;
    private static Integer WEST = 3;
    private static Integer EAST = 4;

    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p15/input.txt").head().split(",")));
        java.util.List<String> strings = new ArrayList<>(100000);
        strings.addAll(input.toJavaList());
        for (int i = 0; i < 100000; i++) {
            strings.add("0");
        }
        Map<Point, String> map = partOne(strings);
        for (int i = -25; i < 25; i++) {
            for (int j = -30; j < 30; j++) {
                String toDraw = map.getOrDefault(new Point(j, i), " ");
                if (0 == j && 0 == i) {
                    System.out.print("X");
                } else {
                    System.out.print(toDraw);
                }
            }
            System.out.println();
        }
    }

    private static Map<Point, String> partOne(java.util.List<String> strings) {
        int resultCount = 0;

        Point startPosition = new Point(0, 0);
        OpCode opCode = new OpCode(List.ofAll(strings), 1);
        Map<Point, String> map = new HashMap<>();
        map.put(startPosition, ".");
        Random random = new Random();
        int moveInstruction = 1;
        while (!opCode.isHalt) { //part1
            System.out.println(startPosition);

            int code = opCode.opcodeComputer(moveInstruction);

            Point attemptedPoint = move().apply(startPosition, moveInstruction);
            String toPrint = processStep(code);
//            if (toPrint.equals("#")) {
            moveInstruction = findNextInstruction(startPosition, map, moveInstruction, random, code == 0);
//            }
            map.put(attemptedPoint, toPrint);


            if (code != 0) {
                startPosition = attemptedPoint;
            }

            for (int i = -25; i < 25; i++) {
                for (int j = -30; j < 30; j++) {
                    String toDraw = map.getOrDefault(new Point(j, i), " ");
                    if (startPosition.x == j && startPosition.y == i) {
                        System.out.print("D");
                    } else {
                        System.out.print(toDraw);

                    }
                }
                System.out.println();
            }
            if(map.size() >= (859 + 797)) {
                break;
            }
//            if (toPrint.equals("O")) {
//                break;
//            }
        }
        System.out.println(resultCount);
        return map;
    }

    private static int findNextInstruction(Point startPosition, Map<Point, String> map, int moveInstruction, Random random, boolean previousSucceed) {
        if(previousSucceed) {
            if(moveInstruction == NORTH) {
                return EAST;
            } else if(moveInstruction == EAST) {
                return SOUTH;
            } else if (moveInstruction == SOUTH) {
                return WEST;
            } else {
                return NORTH;
            }
        } else {
            if(moveInstruction == NORTH) {
                return WEST;
            } else if(moveInstruction == EAST) {
                return NORTH;
            } else if (moveInstruction == SOUTH) {
                return EAST;
            } else {
                return SOUTH;
            }
        }
//        Point candidate = move().apply(startPosition, 1);
//        if (!map.containsKey(candidate)) {
//            return 1;
//        } else if (!map.containsKey(move().apply(startPosition, 3))) {
//            return 3;
//        } else if (!map.containsKey(move().apply(startPosition, 2))) {
//            return 2;
//        } else if (!map.containsKey(move().apply(startPosition, 4))) {
//            return 4;
//        }
//        return random.nextInt(4) + 1;
//        if(map.get(move().apply(startPosition, 1)).equals(".")) {
//            return 1;
//        }
//        if(map.get(move().apply(startPosition, 3)).equals(".")) {
//            return 3;
//        }
//        if (map.get(move().apply(startPosition, 4)).equals(".")) {
//            return 4;
//        }
//        if(map.get(move().apply(startPosition, 2)).equals(".")) {
//            return 2;
//        }

//        throw new IllegalStateException();
    }

    private static Function2<Point, Integer, Point> move() {
        return (point, integer) -> {
            switch (integer) {
                case 1:
                    return new Point(point.x, point.y - 1);
                case 2:
                    return new Point(point.x, point.y + 1);
                case 3:
                    return new Point(point.x - 1, point.y);
                case 4:
                    return new Point(point.x + 1, point.y);
                default:
                    throw new IllegalStateException();
            }
        };
    }

    private static String processStep(int code) {
        switch (code) {
            case 0:
                return "#";
            case 1:
                return ".";
            case 2:
                return "O";
            default:
                System.out.println("invalid output " + code);
//                    throw new IllegalArgumentException();
                return "X";
        }
    }


    private static class OpCode {
        List<String> input;
        BigInteger lastOutput;
        BigInteger relativeBase = BigInteger.ZERO;
        boolean isHalt = false;
        int currentPointer = 0;

        public OpCode(List<String> input, int initialInput) {
            this.input = input;
            lastOutput = BigInteger.valueOf(initialInput);
        }

        public List<String> getInput() {
            return input;
        }

        private BigInteger integerValOf(int currentPointer) {
            return new BigInteger(input.get(currentPointer));
        }

        private BigInteger integerValOf(BigInteger currentPointer) {
            return new BigInteger(input.get(currentPointer.intValue()));
        }

        private BigInteger getParam(int param1Mode, BigInteger first) {
            if (param1Mode == 0) {
                return integerValOf(first);
            } else if (param1Mode == 1) {
                return first;
            } else if (param1Mode == 2) {
                return integerValOf(relativeBase.intValue() + first.intValue());
            } else {
                throw new IllegalStateException();
            }
        }

        public int opcodeComputer(int secondInput) {

            for (int i = 0; i < input.length(); ) {
                int toMove = 0;
                int pointer = integerValOf(currentPointer).intValue();
                int param3Mode = pointer / 1000 / 10;
                int param2Mode = pointer / 1000 % 10;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
                if (opcode == 99) {
                    isHalt = true;
                    return lastOutput.intValue();
                }
                BigInteger first = integerValOf(currentPointer + 1);
                BigInteger second = integerValOf(currentPointer + 2);
                BigInteger third = integerValOf(currentPointer + 3);
                BigInteger param1 = getParam(param1Mode, first);
                BigInteger param2 = getParam(param2Mode, second);

                if (opcode == 1) {
                    BigInteger param3 = getParam3(param3Mode, third);
                    input = replaceElement(param1.add(param2).toString(), param3.intValue());
                    toMove = 4;
                }
                if (opcode == 2) {
                    BigInteger param3 = getParam3(param3Mode, third);
                    input = replaceElement(param1.multiply(param2).toString(), param3.intValue());
                    toMove = 4;
                }
                if (opcode == 3) {
                    input = replaceElement(String.valueOf(secondInput), getParam3(param1Mode, first).intValue());
                    toMove = 2;
                }
                if (opcode == 4) {
                    lastOutput = param1;
//                    System.out.println(lastOutput);
                    currentPointer = currentPointer + 2;
                    return lastOutput.intValue();
                }
                if (opcode == 5) {
                    if (!param1.equals(BigInteger.ZERO)) {
                        currentPointer = param2.intValue();
                    } else {
                        toMove = 3;
                    }
                }
                if (opcode == 6) {
                    if (param1.equals(BigInteger.ZERO)) {
                        currentPointer = param2.intValue();
                    } else {
                        toMove = 3;
                    }
                }
                if (opcode == 7) {
                    int val = 0;
                    if (param1.compareTo(param2) < 0) {
                        val = 1;
                    }
                    BigInteger param3 = getParam3(param3Mode, third);
                    input = replaceElement(String.valueOf(val), param3.intValue());
                    toMove = 4;

                }
                if (opcode == 8) {
                    int val = 0;
                    if (param1.equals(param2)) {
                        val = 1;
                    }
                    BigInteger param3 = getParam3(param3Mode, third);
                    input = replaceElement(String.valueOf(val), param3.intValue());
                    toMove = 4;

                }
                if (opcode == 9) {
                    relativeBase = relativeBase.add(param1);
                    toMove = 2;
                }

                currentPointer = currentPointer + toMove;
            }
            throw new IllegalStateException();
        }

        private BigInteger getParam3(int param3Mode, BigInteger third) {
            if (param3Mode == 2) {
                return relativeBase.add(third);
            } else {
                return third;
            }
        }

        private List<String> replaceElement(String element, int i) {
            return input
                    .removeAt(i)
                    .insert(i, element);
        }


    }
}
