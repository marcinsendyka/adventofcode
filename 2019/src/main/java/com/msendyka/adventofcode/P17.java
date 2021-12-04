package com.msendyka.adventofcode;

import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class P17 {
    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p17/input.txt").head().split(",")));
        java.util.List<String> strings = new ArrayList<>(1000000);
        strings.addAll(input.toJavaList());
        for (int i = 0; i < 100000; i++) {
            strings.add("0");
        }

        long[] program = new long[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            program[i] = Long.valueOf(strings.get(i));
        }
        partOne(program);
        long[] partTwoProgram = program.clone();
        partTwoProgram[0] = 2;
        OpCode opCode = new OpCode(partTwoProgram, 0);
    }

    private static void partOne(long[] program) {
        OpCode opCode = new OpCode(program.clone(), 0);
        Set<Point> scaffold = new HashSet<>();
        int x = 0;
        int y = 0;
        while (!opCode.isHalt) {
            long result = opCode.opcodeComputer();
            char charResult = (char) result;
            System.out.print(Character.valueOf(charResult));
            if (charResult == '#') {
                scaffold.add(new Point(x, y));
            }
            x++;
            if (charResult == '\n') {
                y++;
                x = 0;
            }
        }
        int alignmentSum = 0;
        for (Point point : scaffold) {
            if (scaffold.contains(new Point(point.x, point.y + 1)) &&
                    scaffold.contains(new Point(point.x, point.y - 1)) &&
                    scaffold.contains(new Point(point.x + 1, point.y)) &&
                    scaffold.contains(new Point(point.x - 1, point.y))) {
                alignmentSum += point.x * point.y;
            }
        }
        System.out.println(alignmentSum);
    }

    private static class OpCode {
        long[] program;
        BigInteger lastOutput;
        BigInteger relativeBase = BigInteger.ZERO;
        boolean isHalt = false;
        int currentPointer = 0;
        java.util.List<Long> inputs = new ArrayList<>();

        public OpCode(long[] input, int initialInput) {
            program = input;
//            lastOutput = BigInteger.valueOf(initialInput);
            inputs.add((long) initialInput);
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

        public void addInput(long input) {
            this.inputs.add(input);
        }

        public long opcodeComputer() {
            int minusOnesCount = 0;
            for (int i = 0; i < program.length; ) {
                int toMove = 0;
                int pointer = integerValOf(currentPointer).intValue();
                int param3Mode = pointer / 1000 / 10;
                int param2Mode = pointer / 1000 % 10;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
                if (opcode == 99) {
                    isHalt = true;
                    return lastOutput.longValue();
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
                    if (inputs.isEmpty()) {
                        program[getParam3(param1Mode, first).intValue()] = -1;
                        minusOnesCount++;
                        if (minusOnesCount > 5) {
                            currentPointer = currentPointer + 2;
                            return Integer.MIN_VALUE;

                        }


                    } else {
                        program[getParam3(param1Mode, first).intValue()] = inputs.get(0);
                        inputs.remove(0);
                    }

                    toMove = 2;
                }
                if (opcode == 4) {
                    lastOutput = param1;
//                    System.out.println(lastOutput);
                    currentPointer = currentPointer + 2;
                    return lastOutput.longValue();
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
