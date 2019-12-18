package com.msendyka.adventofcode.p16;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

public class P16 {

    public static final int INPUT_MULTIPLIER = 10000;

    public static void main(String[] args) {

        part1();
        List<Integer> phase = List.of(1, 1, -1, -1);
        List<String> input = Functions.readInput("p16/test.txt");
        System.out.println(input);

        String previousPhase = input.head();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < INPUT_MULTIPLIER; i++) {
            builder.append(previousPhase);
        }
        previousPhase = builder.toString();
        Integer resultOffset = Integer.valueOf(previousPhase.substring(0, 7));
        System.out.println(resultOffset);
        int phaseLenght = previousPhase.length();
        int[] arrayTemp = new int[phaseLenght - resultOffset];
        for (int k = 0; k < 100; k++) {
            System.out.println(k);
            previousPhase = processPhase(phase, previousPhase, phaseLenght, resultOffset, arrayTemp);
        }
        System.out.println(previousPhase);
        System.out.println(previousPhase.substring(resultOffset, (resultOffset + 8)));
    }

    private static String processPhase(List<Integer> phase,
                                       String previousPhase,
                                       int phaseLenght,
                                       Integer resultOffset,
                                       int[] arrayTemp) {

        int sum = 0;
        for (int j = phaseLenght - 1; j >= resultOffset; j--) {
            int offsetMultiplier = j + 1;
            sum += Character.getNumericValue(previousPhase.charAt(j % previousPhase.length()));
            arrayTemp[j - resultOffset] = sum;
            int accum = sum;
            int k = 2;
            while (phaseLenght > k * offsetMultiplier - 1) {
                accum += arrayTemp[(k * offsetMultiplier - 1 - resultOffset)] * phase.get(k % 4);
                k += 1;
            }
            char[] chars = previousPhase.toCharArray();
            chars[j % previousPhase.length()] = String.valueOf(Math.abs(accum % 10)).toCharArray()[0];
            previousPhase = String.valueOf(chars);
        }
        return previousPhase;
    }

    private static int processPhaseStep(String previousPhase, List<Integer> offsetList, int offsetMultiplier, int phaseResult, int i) {
        int partResult = Character.getNumericValue(previousPhase.charAt(i % previousPhase.length())) * offsetList.get(((i + 1) / offsetMultiplier) % (offsetList.length()));
        phaseResult += partResult;
        return phaseResult;
    }

    private static void part1() {
        List<Integer> phase = List.of(1, 1, -1, -1);
        List<String> input = Functions.readInput("p16/input.txt");
        System.out.println(input);
        String previousPhase = input.head();
        int[] arrayTemp = new int[previousPhase.length()];

        for (int k = 0; k < 100; k++) {
            previousPhase = processPhase(phase, previousPhase, previousPhase.length(), 0, arrayTemp);
        }
        System.out.println(previousPhase);
        System.out.println(previousPhase.substring(0, 8));
    }

}
