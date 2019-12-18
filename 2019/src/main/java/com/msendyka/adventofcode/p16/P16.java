package com.msendyka.adventofcode.p16;

import com.msendyka.adventofcode.Functions;

import io.vavr.collection.List;

public class P16 {

    public static final int INPUT_MULTIPLIER = 10000;

    public static void main(String[] args) {

        part1();
        List<Integer> phase = List.of(1, 1, -1, -1);
        List<String> input = Functions.readInput("p16/input.txt");
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
        char[] fft = previousPhase.toCharArray();
        int[] arrayTemp = new int[phaseLenght - resultOffset];
        for (int k = 0; k < 100; k++) {
            System.out.println(k);
            fft = processPhase(phase, phaseLenght, resultOffset, arrayTemp, fft);
        }
        System.out.println(fft);
        System.out.println(new String(fft).substring(0 +resultOffset, 8 + resultOffset));
    }

    private static char[] processPhase(List<Integer> phase,
                                       int phaseLenght,
                                       Integer resultOffset,
                                       int[] arrayTemp, char[] fft) {

        int sum = 0;
        for (int j = phaseLenght - 1; j >= resultOffset; j--) {
            int offsetMultiplier = j + 1;
            sum += Character.getNumericValue(fft[j]);
            arrayTemp[j - resultOffset] = sum;
            int accum = sum;
            int k = 2;
            while (phaseLenght > k * offsetMultiplier - 1) {
                accum += arrayTemp[(k * offsetMultiplier - 1 - resultOffset)] * phase.get(k % 4);
                k += 1;
            }
            fft[j] = String.valueOf(Math.abs(accum % 10)).toCharArray()[0];
        }
        return fft;
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
        char[] fft = previousPhase.toCharArray();

        int[] arrayTemp = new int[previousPhase.length()];

        for (int k = 0; k < 100; k++) {
            fft = processPhase(phase, previousPhase.length(), 0, arrayTemp, fft);
        }
        System.out.println(fft);
        System.out.println(new String(fft).substring(0, 8));
    }

}
