package com.msendyka.adventofcode.p2;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.Arrays;

public class P2 {
    public static void main(String[] args) {
        int one = partOne(12, 2);
        int two = partTwo();
        System.out.println(one);
        System.out.println(two);
    }

    private static int partTwo() {
        int result = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                result = Integer.valueOf(partOne(i,  j));
                if (result == 19690720) {
                    return i * 100 + j;
                }
            }
        }
        return 0;
    }

    private static int partOne(int one, int two) {
        List<Integer> input = List.ofAll(Arrays.asList(Functions.readInput("p2/input.txt").head().split(","))).map(Integer::valueOf);
        java.util.List<Integer> newLL = input.toJavaList();
        newLL.set(1, one);
        newLL.set(2, two);
        input = List.ofAll(newLL);
        for (int i = 0; i < input.length(); ) {
            int integer = input.get(i);
            if (integer == 1) {
                int first = input.get(i + 1);
                int second = input.get(i + 2);
                java.util.List<Integer> newL = input.toJavaList();
                newL.set(input.get(i + 3), (input.get(first) + input.get(second)));
                input = List.ofAll(newL);
            }
            if (integer == 2) {
                int first = input.get(i + 1);
                int second = input.get(i + 2);
                java.util.List<Integer> newL = input.toJavaList();
                newL.set(input.get(i + 3), (input.get(first)) * input.get(second));
                input = List.ofAll(newL);
            }
            if (integer == 99) {
                return input.get(0);
            }
            i = i + 4;
        }
        return input.get(0);
    }
}
