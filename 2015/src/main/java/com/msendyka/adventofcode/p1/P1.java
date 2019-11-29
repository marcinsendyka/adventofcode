package com.msendyka.adventofcode.p1;

import com.msendyka.adventofcode.Functions;
import io.vavr.Function2;
import io.vavr.collection.List;

public class P1 {
    // https://adventofcode.com/2015/day/1
    public static void main(String[] args) {
        List<String> input = Functions.readInputOneLine("p1/input.txt");

        partOne(input);
        partTwo(input);
    }

    private static void partTwo(List<String> input) {
        Function2<Integer, Integer, Integer> nextElement = new Function2<>() {
            @Override
            public Integer apply(Integer position, Integer floor) {
                exceptionWhenNotFoundAndInputIsOver(position);
                int val = isFloorUpOrDown(input.get(position));
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

    private static void partOne(List<String> input) {
        Integer result = input
                .map(P1::isFloorUpOrDown)
                .fold(0, (a,b) -> a + b);
        System.out.println(result);
    }

    private static int isFloorUpOrDown(String floorMove) {
        return floorMove.equals("(") ? 1 : -1;
    }
}
