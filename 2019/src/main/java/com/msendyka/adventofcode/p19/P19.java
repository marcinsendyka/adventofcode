package com.msendyka.adventofcode.p19;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;

import io.vavr.collection.List;

public class P19 {

    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p19/input.txt").head().split(",")));
        java.util.List<String> strings = new ArrayList<>(1000000);
        strings.addAll(input.toJavaList());
        for (int i = 0; i < 100000; i++) {
            strings.add("0");
        }

        long[] program = new long[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            program[i] = Long.valueOf(strings.get(i));
        }
        OpCode opCode;
        int sum = 0;

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {

                int result = checkPlace(program, i, j);

                sum += result;

            }
        }
        System.out.println(sum);

        Map<Point, Integer> beamValues = new HashMap<>();
        int startX = 400 * 3;
        int endX = startX + 400;
        int startY = 450 * 3;
        int endY = startY + 450;
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {

                int result = 0;
                boolean found = true;
                for (int ii = i; ii < i + 100; ii++) {
                    for (int jj = j; jj < j + 100; jj++) {
                        if (beamValues.containsKey(new Point(ii, jj))) {
                            result = beamValues.get(new Point(ii, jj));
                        } else {
                            result = checkPlace(program, ii, jj);
                            beamValues.put(new Point(ii, jj), result);
                        }

                        if (result == 0) {
                            found = false;
                            break;
                        }
                    }
                    if (result == 0) {
                        found = false;

                        break;
                    }
                }

                if (found) {
                    System.out.println(i + " : " + j);
                }
                sum += result;

            }
            if (i % 100 == 0) {
                System.out.println(i);
            }
        }

        for (int i = startX; i < startX + 100; i++) {
            for (int j = startY; j < startY + 100; j++) {
                Integer integer = beamValues.get(new Point(i, j));
                if (integer == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print("# ");

                }
            }
            System.out.println();
        }


    }

    private static int checkPlace(long[] program, int i, int j) {
        OpCode opCode;
        int result;
        opCode = new OpCode(program.clone(), 0);
        opCode.opcodeComputer(i, j);
        result = opCode.opcodeComputer(i, j);
        return result;
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

        public int opcodeComputer(int secondInput, int thirdInput) {
            boolean inputUsed = false;
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
                    if (!inputUsed) {
                        program[getParam3(param1Mode, first).intValue()] = secondInput;
                        inputUsed = true;
                    } else {
                        program[getParam3(param1Mode, first).intValue()] = thirdInput;

                    }

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
