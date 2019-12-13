package com.msendyka.adventofcode.p13;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.Point;

import io.vavr.collection.List;

public class P13 {

    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p13/input.txt").head().split(",")));
        java.util.List<String> strings = new ArrayList<>(1000000);
        strings.addAll(input.toJavaList());
        for (int i = 0; i < 1848142; i++) {
            strings.add("0");
        }
//        partTwo(strings);
        int resultCount = 0;

        OpCode opCode = new OpCode(List.ofAll(strings), 0);
        Map<Point, String> map = new HashMap<>();
        while (!opCode.isHalt) { //part1
            int x = opCode.opcodeComputer(0);
            int y = opCode.opcodeComputer(0);
            int code = opCode.opcodeComputer(0);
            if (code == 2) {
                resultCount++;
            }
            Point point = new Point(x, y);
            String toPrint = "";
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
                    toPrint = "-";
                    break;
                case 4:
                    toPrint = "O";
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            map.put(point, toPrint);


        }
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 60; j++) {
                String toPrint = map.getOrDefault(new Point(j,i), " ");
                System.out.print(toPrint);
            }
            System.out.println();
        }
        System.out.println(resultCount);
    }

    private static void partTwo(java.util.List<String> strings) {
        List<String> input;
        strings.set(0, "2");

        int maxScore = 0;
        input = List.ofAll(strings);

        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            if (i % 100 == 0) {
                System.out.println("Count: " + i);
            }
            OpCode opCode = new OpCode(input, 0);
            while (!opCode.isHalt) {
                int nextInt = r.nextInt(3) - 1;

                int result1 = opCode.opcodeComputer(nextInt);
                int result2 = opCode.opcodeComputer(nextInt);
                int result3 = opCode.opcodeComputer(nextInt);
                if (result1 == -1 && result2 == 0) {
                    if (result3 > maxScore) {
                        maxScore = result3;
                    }
                }
            }

        }

        System.out.println(maxScore);
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
