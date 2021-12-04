package com.msendyka.adventofcode.p7;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.Arrays;

public class P7 {

    public static void main(String[] args) {
        List<Integer> input = List.ofAll(Arrays.asList(Functions.readInput("p7/input.txt").head().split(","))).map(Integer::valueOf);

        List<Integer> possibilities = List.ofAll(5, 6, 7, 8, 9);
        int maxOutput = 0;
        for (int i = 0; i < 10000; i++) {
            int output = 0;
            List<Integer> shuffle = possibilities.shuffle();
            OpCode a = new OpCode(input, shuffle.get(0));
            OpCode b = new OpCode(input, shuffle.get(1));
            OpCode c = new OpCode(input, shuffle.get(2));
            OpCode d = new OpCode(input, shuffle.get(3));
            OpCode e = new OpCode(input, shuffle.get(4));
            while (!e.isHalted) {
                output = a.oneInstruction(output);
                output = b.oneInstruction(output);
                output = c.oneInstruction(output);
                output = d.oneInstruction(output);
                output = e.oneInstruction(output);
            }
            if(output > maxOutput) {
                maxOutput = output;
            }
        }

        System.out.println(maxOutput);

    }

    private static class OpCode {
        List<Integer> input;
        int phaseSetting;
        boolean firstInstructionUsed = false;
        int currentPointer = 0;
        boolean isHalted = false;
        int lastOutput = 0;

        public OpCode(List<Integer> input, int phaseSetting) {
            this.input = input;
            this.phaseSetting = phaseSetting;
        }

        public int oneInstruction(int secondInputInt) {

            boolean secondInsUsed = false;
            while (true) {
                int toMove = 0;
                Integer pointer = input.get(currentPointer);
                int param2Mode = pointer / 1000;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
                if (opcode == 1) {
                    int first = input.get(currentPointer + 1);
                    int second = input.get(currentPointer + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);

                    Integer indexToSwitch = input.get(currentPointer + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, param1 + param2);
                    toMove = 4;
                }
                if (opcode == 2) {
                    int first = input.get(currentPointer + 1);
                    int second = input.get(currentPointer + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    Integer indexToSwitch = input.get(currentPointer + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, param1 * param2);
                    toMove = 4;

                }
                if (opcode == 3) {
                    int first = input.get(currentPointer + 1);
                    int val = phaseSetting;
                    if (!firstInstructionUsed) {
                        val = phaseSetting;
                        firstInstructionUsed = true;
                    } else if (!secondInsUsed) {
                        val = secondInputInt;
                        secondInsUsed = true;
                    }
                    input = input
                            .removeAt(first)
                            .insert(first, val);
                    toMove = 2;
                }
                if (opcode == 4) {
                    int first = input.get(currentPointer + 1);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    currentPointer = currentPointer + 2;
//                    phaseSetting = param1;
                    lastOutput = param1;
                    return param1;
//                    toMove = 2;
                }
                if (opcode == 5) {
                    int first = input.get(currentPointer + 1);
                    int second = input.get(currentPointer + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    if (param1 != 0) {
                        currentPointer = param2;
                    } else {
                        toMove = 3;

                    }

                }
                if (opcode == 6) {
                    int first = input.get(currentPointer + 1);
                    int second = input.get(currentPointer + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    if (param1 == 0) {
                        currentPointer = param2;
                    } else {
                        toMove = 3;

                    }
                }
                if (opcode == 7) {
                    int first = input.get(currentPointer + 1);
                    int second = input.get(currentPointer + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    int val = 0;
                    if (param1 < param2) {
                        val = 1;
                    }
                    Integer indexToSwitch = input.get(currentPointer + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, val);
                    toMove = 4;

                }
                if (opcode == 8) {
                    int first = input.get(currentPointer + 1);
                    int second = input.get(currentPointer + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    int val = 0;
                    if (param1 == param2) {
                        val = 1;
                    }
                    Integer indexToSwitch = input.get(currentPointer + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, val);
                    toMove = 4;

                }
                if (opcode == 99) {
                    isHalted = true;
                    return lastOutput;
                }
                currentPointer = currentPointer + toMove;
            }
        }

        public int opcodeCOmputer(int secondInputInt) {

            boolean secondInsUsed = false;
            for (int i = 0; i < input.length(); ) {
                int toMove = 0;
                Integer pointer = input.get(i);
                int param2Mode = pointer / 1000;
                int param1Mode = (pointer % 1000) / 100;
                int opcode = pointer % 1000 % 100;
                if (opcode == 1) {
                    int first = input.get(i + 1);
                    int second = input.get(i + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);

                    Integer indexToSwitch = input.get(i + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, param1 + param2);
                    toMove = 4;
                }
                if (opcode == 2) {
                    int first = input.get(i + 1);
                    int second = input.get(i + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    Integer indexToSwitch = input.get(i + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, param1 * param2);
                    toMove = 4;

                }
                if (opcode == 3) {
                    int first = input.get(i + 1);
                    int val = phaseSetting;
                    if (!firstInstructionUsed) {
                        val = phaseSetting;
                        firstInstructionUsed = true;
                    } else if (!secondInsUsed) {
                        val = secondInputInt;
                        secondInsUsed = true;
                    }
                    input = input
                            .removeAt(first)
                            .insert(first, val);
                    toMove = 2;
                }
                if (opcode == 4) {
                    int first = input.get(i + 1);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    phaseSetting = param1;
                    toMove = 2;
                }
                if (opcode == 5) {
                    int first = input.get(i + 1);
                    int second = input.get(i + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    if (param1 != 0) {
                        i = param2;
                    } else {
                        toMove = 3;

                    }

                }
                if (opcode == 6) {
                    int first = input.get(i + 1);
                    int second = input.get(i + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    if (param1 == 0) {
                        i = param2;
                    } else {
                        toMove = 3;

                    }
                }
                if (opcode == 7) {
                    int first = input.get(i + 1);
                    int second = input.get(i + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    int val = 0;
                    if (param1 < param2) {
                        val = 1;
                    }
                    Integer indexToSwitch = input.get(i + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, val);
                    toMove = 4;

                }
                if (opcode == 8) {
                    int first = input.get(i + 1);
                    int second = input.get(i + 2);
                    int param1 = param1Mode == 1 ? first : input.get(first);
                    int param2 = param2Mode == 1 ? second : input.get(second);
                    int val = 0;
                    if (param1 == param2) {
                        val = 1;
                    }
                    Integer indexToSwitch = input.get(i + 3);
                    input = input
                            .removeAt(indexToSwitch)
                            .insert(indexToSwitch, val);
                    toMove = 4;

                }
                if (opcode == 99) {
                    return phaseSetting;
                }
                i = i + toMove;
            }
            return 0;
        }


    }


}
