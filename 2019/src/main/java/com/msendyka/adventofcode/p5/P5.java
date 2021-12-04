package com.msendyka.adventofcode.p5;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.Arrays;

public class P5 {

    public static void main(String[] args) {
        List<Integer> input = List.ofAll(Arrays.asList(Functions.readInput("p5/input.txt").head().split(","))).map(Integer::valueOf);

        int inputInt = 5;

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
                input = input
                        .removeAt(first)
                        .insert(first, inputInt);
                toMove = 2;
            }
            if (opcode == 4) {
                int first = input.get(i + 1);
                int param1 = param1Mode == 1 ? first : input.get(first);
                inputInt = param1;
                System.out.println(param1);
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
                System.out.println(input);
                return;
            }
            i = i + toMove;
        }
        System.out.println(input);

    }
}
