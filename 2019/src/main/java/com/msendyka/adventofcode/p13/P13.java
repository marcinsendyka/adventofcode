package com.msendyka.adventofcode.p13;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;
import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.*;

public class P13 {

    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p13/input.txt").head().split(",")));
        java.util.List<String> strings = new ArrayList<>(100000);
        strings.addAll(input.toJavaList());
        for (int i = 0; i < 1948142; i++) {
            strings.add("0");
        }
        long[] program = new long[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            program[i] = Long.valueOf(strings.get(i));
        }
        Map<Point, String> part1 = partOne(program.clone());
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 60; j++) {
                String toPrint = part1.getOrDefault(new Point(j, i), " ");
                System.out.print(toPrint);
            }
            System.out.println();
        }
        Map<Point, String> map = partTwo(strings, 0, program.clone());


    }

    private static Map<Point, String> partTwo(java.util.List<String> strings, int move, long[] program) {

        java.util.List<String> newInput = new ArrayList<>(strings);
        newInput.set(0, "2");
        program[0] = 2;
        int joystick = 0;
        OpCode opCode = new OpCode(program.clone(), joystick);
        Map<Point, String> map = new HashMap<>();
        Map<Point, String> gameState = new HashMap<>();
        Point prevBall = null;
        int count = 0;
        java.util.List<Integer> results = new ArrayList<>();
        while (!opCode.isHalt) {
            int res = opcodeStep(opCode, map, joystick, results);
            results.add(res);
            if (results.size() == 3) {
                if (results.get(0) == -1 && results.get(1) == 0) {
                    System.out.println(results.get(2));
                }
                results.clear();
            }
            count++;

            if (count > 999 * 3) {

                Optional<Map.Entry<Point, String>> o = gameState.entrySet().stream().filter((a) -> a.getValue().equals("O")).findFirst();
                Optional<Map.Entry<Point, String>> pad = gameState.entrySet().stream().filter((a) -> a.getValue().equals("_")).findFirst();
                if (o.isPresent() && pad.isPresent() && prevBall != null) {
                    Point ball = o.get().getKey();
                    Point padKey = pad.get().getKey();
//                    joystick = deltaAlgo(prevBall, ball, padKey);
                    if (ball.x < padKey.x) { // ball <-----
                        joystick = -1;
                    } else if (ball.x > padKey.x) { // ball ---->
                        joystick = 1;
                    } else {
                        joystick = 0;
                    }
                    prevBall = ball;
//                    prevPad = padKey;
//                    System.out.println(count);
//                    System.out.println("Joystick: " + joystick);
//                    System.out.println("Ball: " + prevBall);
//                    System.out.println("Pad: " + prevPad);

//                    printState(gameState);
                }
                if (o.isPresent()) {
                    prevBall = o.get().getKey();
                }


            }
            if (count > 999 * 3) {
                long[] afterStep = opCode.program.clone();
                afterStep[0] = 1;
                gameState = partOne(afterStep);
            }
//            System.out.println(gameState.get(new Point(-1,0)));
        }
        return map;
    }

    private static int deltaAlgo(Point prevBall, Point ball, Point padKey) {
        int joystick;
        int ballYDelta = prevBall.y - ball.y;
        int ballXDelta = prevBall.x - ball.x;
        int padXPos = padKey.x;
        int padYPos = padKey.y;

        if (ballYDelta < 0) {
            int ballYExpectedPos = ball.y;
            int ballXExpectedPos = ball.x;
            while (ballYExpectedPos < padYPos) {
                ballYExpectedPos -= ballYDelta;
                ballXExpectedPos -= ballXDelta;
            }
            System.out.println("Expected x: " + ballXExpectedPos);
            if (ballXExpectedPos < padXPos) {
                joystick = -1;
            } else if (ballXExpectedPos > padXPos) {
                joystick = 1;
            } else {
                joystick = 0;
            }
        } else if (ballYDelta > 0) {
            int ballYExpectedPos = ball.y;
            int ballXExpectedPos = ball.x;
            while (ballYExpectedPos > 10) {
                ballYExpectedPos -= ballYDelta;
                ballXExpectedPos -= ballXDelta;
            }
            if (ballXExpectedPos < padXPos) {
                joystick = -1;
            } else if (ballXExpectedPos > padXPos) {
                joystick = 1;
            } else {
                joystick = 0;
            }
        } else {
            joystick = 0;
        }
        return joystick;
    }

    private static void printState(Map<Point, String> gameState) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 55; j++) {
                String toPrint = gameState.getOrDefault(new Point(j, i), " ");
                System.out.print(toPrint);
            }
            System.out.println();
        }
    }

    private static int opcodeStep(OpCode opCode, Map<Point, String> map, int move, java.util.List<Integer> results) {
//        int x = opCode.opcodeComputer(move);
//        int y = opCode.opcodeComputer(move);
        int code = opCode.opcodeComputer(move);
//        Point point = new Point(x, y);
//        String toPrint = "";
//        toPrint = processStep(map, x, y, code, point, toPrint);
//        map.put(point, toPrint);
//        results.add(x);
//        results.add(y);
//        results.add(code);
        return code;
    }

    private static String processStep(Map<Point, String> map, int x, int y, int code, Point point, String toPrint) {
        if (x == -1 && y == 0) {
//            map.put(point, "" + code);
            toPrint = "" + code;
            System.out.println("score: " + code);

        } else {
            switch (code) {
                case 0:
                    toPrint = " ";
                    break;
                case 1:
                    toPrint = "#";
                    break;
                case 2:
                    toPrint = "X";
                    break;
                case 3:
                    toPrint = "_";
                    break;
                case 4:
                    toPrint = "O";
                    break;
                default:
                    throw new IllegalArgumentException();
            }

        }
        return toPrint;
    }

    private static Map<Point, String> partOne(long[] program) {
        int resultCount = 0;

        OpCode opCode = new OpCode(program, 0);
        Map<Point, String> map = new HashMap<>();
        while (!opCode.isHalt) { //part1
            int x = opCode.opcodeComputer(0);
            int y = opCode.opcodeComputer(0);
            int code = opCode.opcodeComputer(0);
            if (code == 2) {
                resultCount++;
            }
            Point point = new Point(x, y);
            String toPrint = processStep(map, x, y, code, point, "");

            map.put(point, toPrint);

        }
        return map;
    }

    private static class OpCode {
        long[] program;
        BigInteger lastOutput;
        BigInteger relativeBase = BigInteger.ZERO;
        boolean isHalt = false;
        int currentPointer = 0;

        public OpCode(long[] input, int initialInput) {
            program = input;
            lastOutput = BigInteger.valueOf(initialInput);
        }

        private BigInteger integerValOf(int currentPointer) {
            return BigInteger.valueOf(program[currentPointer]);
        }

        private BigInteger integerValOf(BigInteger currentPointer) {
            return BigInteger.valueOf(program[currentPointer.intValue()]);
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
            for (int i = 0; i < program.length; ) {
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
                    program[param3.intValue()] = param1.add(param2).longValue();
                    toMove = 4;
                }
                if (opcode == 2) {
                    BigInteger param3 = getParam3(param3Mode, third);
                    program[param3.intValue()] = param1.multiply(param2).longValue();

                    toMove = 4;
                }
                if (opcode == 3) {
                    program[getParam3(param1Mode, first).intValue()] = secondInput;
//                    else {
//                        program[getParam3(param1Mode, first).intValue()] = thirdInput;
//
//                    }

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
                    program[param3.intValue()] = val;

                    toMove = 4;

                }
                if (opcode == 8) {
                    int val = 0;
                    if (param1.equals(param2)) {
                        val = 1;
                    }
                    BigInteger param3 = getParam3(param3Mode, third);
                    program[param3.intValue()] = val;

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


    }
}
