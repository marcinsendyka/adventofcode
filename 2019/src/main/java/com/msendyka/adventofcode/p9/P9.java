package com.msendyka.adventofcode.p9;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.Arrays;

public class P9 {

    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p9/input.txt").head().split(",")));
        for (int i = 0; i < 30000; i++) {
            input = input.append("0");
        }
        OpCode opCode = new OpCode(input, 2);
        int result = opCode.opcodeComputer();
    }

    private static class OpCode {
        List<String> input;
        BigInteger lastOutput;
        BigInteger relativeBase = BigInteger.ZERO;

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
            if(param1Mode == 0) {
                return integerValOf(first);
            } else if(param1Mode == 1) {
                return first;
            } else if (param1Mode == 2) {
                return integerValOf(relativeBase.intValue() + first.intValue());
            } else {
                throw new IllegalStateException();
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
            if(param3Mode == 2) {
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
