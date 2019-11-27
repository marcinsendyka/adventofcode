package com.msendyka.adventofcode.p1;

import com.msendyka.adventofcode.Functions;
import io.vavr.Function2;
import io.vavr.collection.List;

public class P1 {
    // https://adventofcode.com/2015/day/1
    public static void main(String[] args) {
        List<String> inputLines = Functions.readInput("p1/input.txt");
        String input = inputLines.head();

        partOne(input);
        partTwo(input);
    }

    private static void partTwo(String input) {
        Function2<Integer, Integer, Integer> nextElement = new Function2<>() {
            @Override
            public Integer apply(Integer position, Integer floor) {
                exceptionWhenNotFoundAndInputIsOver(position);
                int val = isFloorUpOrDown(input.charAt(position));
                int newFloor = floor + val;
                if (newFloor == -1) {
                    return position;
                }
                return apply(++position, newFloor);
            }

            private void exceptionWhenNotFoundAndInputIsOver(Integer position) {
                if (input.length() == position) {
                    throw new IllegalStateException();
                }
            }
        };
        System.out.println(nextElement.apply(0, 0) + 1);
    }

    private static void partOne(String input) {
        Integer result = input
                .chars()
                .mapToObj(c -> (char) c)
                .map(P1::isFloorUpOrDown)
                .reduce(0, Integer::sum);
        System.out.println(result);
    }

    private static int isFloorUpOrDown(Character character) {
        return character.equals(Character.valueOf("(".charAt(0))) ? 1 : -1;
    }
}
