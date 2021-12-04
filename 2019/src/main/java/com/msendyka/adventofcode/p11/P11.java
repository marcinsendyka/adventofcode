package com.msendyka.adventofcode.p11;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;
import io.vavr.Function1;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.math.BigInteger;
import java.util.Arrays;

public class P11 {

    private static int BLACK = 0;
    private static int WHITE = 1;
    private static int LEFT_CODE = 0;
    private static int RIGHT_CODE = 1;
    private static Function1<Point, Point> UP = p -> new Point(p.x, p.y + 1);
    private static Function1<Point, Point> DOWN = p -> new Point(p.x, p.y - 1);
    private static Function1<Point, Point> LEFT = p -> new Point(p.x -1, p.y );
    private static Function1<Point, Point> RIGHT = p -> new Point(p.x +1, p.y );

    public static void main(String[] args) {
        Set<Point> pointsVisited = HashSet.empty();
        Set<Point> whitePoints = HashSet.empty();
        Point startingPoint = new Point(0, 0);
        whitePoints = whitePoints.add(startingPoint);
        Function1<Point, Point> direction = UP;
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p11/input.txt").head().split(",")));
        for (int i = 0; i < 30000; i++) {
            input = input.append("0");
        }
        int inputInt = 0;
        OpCode opCode = new OpCode(input, inputInt);
        int counter = 0;
        int firstOutput = -1;
        int secondOutput = -1;
        while (!opCode.isHalt) {
            inputInt = opCode.oneInstruction(getInput(whitePoints, startingPoint));
            counter++;
            if(counter > 0 && counter % 2 == 0) {
                secondOutput = inputInt;

                direction = newDirection(direction, secondOutput);
                startingPoint = direction.apply(startingPoint);
                System.out.println(startingPoint);
            } else {
                firstOutput = inputInt;
                pointsVisited = pointsVisited.add(startingPoint);
                if(firstOutput == WHITE) {
                    whitePoints = whitePoints.add(startingPoint);
                } else {
                    whitePoints = whitePoints.remove(startingPoint);
                }
            }
        }
        System.out.println(pointsVisited);
        System.out.println(pointsVisited.size());
        for (int i = 20; i > -20; i--) {
            for (int j = -20; j < 50; j++) {
                if(whitePoints.contains(new Point(j,i))) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    private static int getInput(Set<Point> whitePoints, Point startingPoint) {
        if(whitePoints.contains(startingPoint)) {
            return WHITE;
        }
        return BLACK;
    }

    private static Function1<Point, Point> newDirection(Function1<Point, Point> direction, int secondOutput) {
        switch (secondOutput) {
            case 0:
                if(direction == UP) {
                    return LEFT;
                }
                if(direction == LEFT) {
                    return DOWN;
                }
                if(direction == DOWN) {
                    return RIGHT;
                }
                if(direction == RIGHT) {
                    return UP;
                }
                break;
            case 1:
                if(direction == UP) {
                    return RIGHT;
                }
                if(direction == LEFT) {
                    return UP;
                }
                if(direction == DOWN) {
                    return LEFT;
                }
                if(direction == RIGHT) {
                    return DOWN;
                }
                break;
            default:
                throw new IllegalStateException();
        }
        return direction;
    }


    private static class OpCode {
        List<String> input;
        BigInteger lastOutput;
        BigInteger relativeBase = BigInteger.ZERO;
        int currentPointer = 0;
        boolean isHalt = false;

        public OpCode(List<String> input, int initialInput) {
            this.input = input;
            lastOutput = BigInteger.valueOf(initialInput);
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

        public int oneInstruction(int secondInputInt) {

//            boolean secondInsUsed = false;
            lastOutput = BigInteger.valueOf(secondInputInt);
            while (true) {
                int toMove = 0;
                int pointer = integerValOf(currentPointer).intValue();
                int param3Mode = pointer / 1000 / 10;
                int param2Mode = pointer / 1000 % 10;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
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
                    input = replaceElement(String.valueOf(lastOutput), getParam3(param1Mode, first).intValue());
                    toMove = 2;
                }
                if (opcode == 4) {
                    lastOutput = param1;
                    currentPointer = currentPointer + 2;
                    System.out.println(lastOutput);
                    return param1.intValue();
//                    toMove = 2;
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
                if (opcode == 99) {
                    isHalt = true;
                    return lastOutput.intValue();
                }
                currentPointer = currentPointer + toMove;
            }
        }

        public int opcodeComputer() {

            for (int i = 0; i < input.length(); ) {
                int toMove = 0;
                int pointer = integerValOf(i).intValue();
                int param3Mode = pointer / 1000 / 10;
                int param2Mode = pointer / 1000 % 10;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
                BigInteger first = integerValOf(i + 1);
                BigInteger second = integerValOf(i + 2);
                BigInteger third = integerValOf(i + 3);
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
                    input = replaceElement(String.valueOf(lastOutput), getParam3(param1Mode, first).intValue());
                    toMove = 2;
                }
                if (opcode == 4) {
                    lastOutput = param1;
                    System.out.println(lastOutput);
                    toMove = 2;
                }
                if (opcode == 5) {
                    if (!param1.equals(BigInteger.ZERO)) {
                        i = param2.intValue();
                    } else {
                        toMove = 3;
                    }
                }
                if (opcode == 6) {
                    if (param1.equals(BigInteger.ZERO)) {
                        i = param2.intValue();
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
                if (opcode == 99) {
                    return lastOutput.intValue();
                }
                i = i + toMove;
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
