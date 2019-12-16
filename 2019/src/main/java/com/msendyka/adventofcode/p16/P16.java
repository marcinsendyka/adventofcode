package com.msendyka.adventofcode.p16;

import com.msendyka.adventofcode.Functions;

import io.vavr.collection.List;

public class P16 {

    public static void main(String[] args) {
        List<Integer> phase = List.of(0, 1, 0, -1);
        List<String> input = Functions.readInputOneLine("p16/test.txt");
        System.out.println(input);
        StringBuilder phaseResultString = new StringBuilder();

        for (int j = 0; j < input.size(); j++) {
            int phaseOffset;
            if(j == 0 ) {
                phaseOffset = 1;
            } else {
                phaseOffset = 0 ;
            }
            int phaseResult = 0;
            for (int i = 0; i < input.size(); i++) {

                int partResult = Integer.valueOf(input.get(i)) * phase.get((i + (phaseOffset)) % (phase.length()));
                phaseResult += partResult;
                System.out.println(partResult);
            }
            String phaseString = String.valueOf(phaseResult);
            phaseResult = Integer.valueOf(phaseString.substring(phaseString.length() - 1));
            System.out.println(phaseResult);
            phaseResultString.append(phaseResult);
        }
        System.out.println(phaseResultString);
    }
}
