package com.msendyka.adventofcode.p2;

import com.msendyka.adventofcode.Functions;
import com.msendyka.adventofcode.OpcodeComputer;

public class P2 {
    //7594646
    //3376
    public static void main(String[] args) {
        test("p2/test.txt");
        test("p2/test1.txt");
        long one = partOne(12, 2);
        long two = partTwo();
        System.out.println(one);
        System.out.println(two);
    }

    private static void test(String inputFile) {
        OpcodeComputer computer = new OpcodeComputer(inputFile);
        computer.processInstruction();
        System.out.println(computer.getInstructions());

    }

    private static int partTwo() {
        long result = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                result = Long.valueOf(partOne(i,  j));
                if (result == 19690720) {
                    return i * 100 + j;
                }
            }
        }
        throw new IllegalStateException("19690720 not found");
    }

    private static Long partOne(long one, long two) {
        String[] strings = Functions.readInputOneLine("p2/input.txt");
        long[] input = Functions.stringArrayToLongArray(strings);
        input[1] = one;
        input[2] = two;
        return new OpcodeComputer(input).processInstruction();
    }
}
