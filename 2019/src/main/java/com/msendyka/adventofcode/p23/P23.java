package com.msendyka.adventofcode.p23;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.MIN_VALUE;

public class P23 {

    public static void main(String[] args) {

        List<String> input = List.ofAll(Arrays.asList(Functions.readInput("p23/input.txt").head().split(",")));
        java.util.List<String> strings = new ArrayList<>(1000000);
        strings.addAll(input.toJavaList());
        for (int i = 0; i < 100000; i++) {
            strings.add("0");
        }

        long[] program = new long[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            program[i] = Long.valueOf(strings.get(i));
        }
        Map<Long, java.util.List<Long>> queues = new HashMap<>();
        Map<Long, java.util.List<Long>> states = new HashMap<>();
        java.util.List<OpCode> computers = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            queues.put((long) i, new ArrayList<>());
            states.put((long) i, new ArrayList<>());
            computers.add(new OpCode(program.clone(), i));
        }
        long previousY = MIN_VALUE;
        while (true) {
            boolean idle = true;
            for (int i = 0; i < 50; i++) {
                java.util.List<Long> queue = queues.get((long) i);
                if (queue.size() >= 2) {
                    long x = queue.get(0);
                    long y = queue.get(1);
                    computers.get(i).addInput(x);
                    computers.get(i).addInput(y);
                    queue.remove(0);
                    queue.remove(0);
                }
                long first = computers.get(i).opcodeComputer();
                if (first == MIN_VALUE) {
                    continue;
                }
                idle = false;
                java.util.List<Long> state = states.get((long)i);
                state.add(first);
                if(state.size() == 3) {
                    Long x = state.get(1);
                    Long y = state.get(2);
                    Long address = state.get(0);
                    System.out.println(String.format("Computer %s sends (%s,%s) to computer %s",i, x,y,address));
                    queues.get(address).add(x);
                    queues.get(address).add(y);
                    state.clear();
                }
//                break;

            }
            if(idle) {
                java.util.List<Long> nat = queues.get(255L);
                Long x = nat.get(nat.size() - 2);
                Long y = nat.get(nat.size() - 1);
                queues.get(0L).add(x);
                queues.get(0L).add(y);
                if(previousY == y) {
                    System.out.println(y);
                    break;
                }
                previousY = y;
            }
        }
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
