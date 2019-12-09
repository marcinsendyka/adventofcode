package com.msendyka.adventofcode.p9;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.Arrays;

public class P9 {

    public static void main(String[] args) {
        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p9/test.txt").head().split(",")));
        for (int i = 0; i < 10000; i++) {
            input = input.append("0");
        }
        OpCode opCode = new OpCode(input, 1);
        int result = opCode.opcodeCOmputer();
    }

    private static class OpCode {
        List<String> input;
        int phaseSetting;
        boolean firstInstructionUsed = false;
        BigInteger lastOutput = BigInteger.ZERO;
        BigInteger relativeBase = BigInteger.ZERO;

        public OpCode(List<String> input, int phaseSetting) {
            this.input = input;
            this.phaseSetting = phaseSetting;
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

        public int opcodeCOmputer() {

            for (int i = 0; i < input.length(); ) {
                int toMove = 0;
                int pointer = integerValOf(i).intValue();
                int param2Mode = pointer / 1000;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
                if (opcode == 1) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger second = integerValOf(i + 2);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger param2 = getParam(param2Mode, second);

                    BigInteger indexToSwitch = integerValOf(i + 3);
                    input = input
                            .removeAt(indexToSwitch.intValue())
                            .insert(indexToSwitch.intValue(), param1.add(param2).toString());
                    toMove = 4;
                }
                if (opcode == 2) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger second = integerValOf(i + 2);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger param2 = getParam(param2Mode, second);
                    Integer indexToSwitch = integerValOf(i + 3).intValue();
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, param1.multiply(param2).toString());
                    toMove = 4;

                }
                if (opcode == 3) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger val = lastOutput;
                    if (!firstInstructionUsed) {
                        val = BigInteger.valueOf(phaseSetting);
                        firstInstructionUsed = true;
                    }
                    input = input
                            .removeAt(param1.intValue())
                            .insert(param1.intValue(), String.valueOf(val));
                    toMove = 2;
                }
                if (opcode == 4) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger param1 = getParam(param1Mode, first);
                    phaseSetting = param1.intValue();
                    lastOutput = param1;
                    System.out.println(lastOutput);
                    toMove = 2;
                }
                if (opcode == 5) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger second = integerValOf(i + 2);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger param2 = getParam(param2Mode, second);
                    if (!param1.equals(BigInteger.ZERO)) {
                        i = param2.intValue();
                    } else {
                        toMove = 3;
                    }
                }
                if (opcode == 6) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger second = integerValOf(i + 2);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger param2 = getParam(param2Mode, second);
                    if (param1.equals(BigInteger.ZERO)) {
                        i = param2.intValue();
                    } else {
                        toMove = 3;

                    }
                }
                if (opcode == 7) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger second = integerValOf(i + 2);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger param2 = getParam(param2Mode, second);
                    int val = 0;
                    if (param1.compareTo(param2) < 0) {
                        val = 1;
                    }
                    Integer indexToSwitch = integerValOf(i + 3).intValue();
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, String.valueOf(val));
                    toMove = 4;

                }
                if (opcode == 8) {
                    BigInteger first = integerValOf(i + 1);
                    BigInteger second = integerValOf(i + 2);
                    BigInteger param1 = getParam(param1Mode, first);
                    BigInteger param2 = getParam(param2Mode, second);
                    int val = 0;
                    if (param1.equals(param2)) {
                        val = 1;
                    }
                    Integer indexToSwitch = integerValOf(i + 3).intValue();
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, String.valueOf(val));
                    toMove = 4;

                }
                if (opcode == 9) {
                    BigInteger first = integerValOf(i + 1);
                    relativeBase = relativeBase.add(first);
                    toMove = 2;
                }
                if (opcode == 99) {
                    return lastOutput.intValue();
                }
                i = i + toMove;
            }
            return 0;
        }


    }
}
