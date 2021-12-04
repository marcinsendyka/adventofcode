package com.msendyka.adventofcode.p8;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class P8 {

    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int TRANSPARENT = 2;

    public static void main(String[] args) {
        List<String> input = Functions.readInputOneLineChars("p8/input.txt");

        int width = 25;
        int height = 6;
        int numberOfLayers = width * height;
        System.out.println(partOne(input, width, height));
        List<Integer> pixels = partTwo(input, numberOfLayers);
        printImage(width, height, pixels);
    }

    private static List<Integer> partTwo(List<String> input, int numberOfLayers) {
        List<List<Integer>> everyPixel = Functions.splitEvery(input.map(Integer::valueOf), numberOfLayers);
        return everyPixel.map(layersForEachPixel -> layersForEachPixel
                .find(P8::blackOrWhite)
                .getOrElse(TRANSPARENT));
    }

    private static boolean blackOrWhite(Integer layerPixel) {
        return layerPixel == WHITE || layerPixel == BLACK;
    }

    private static void printImage(int width, int height, List<Integer> pixels) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Integer pixel = pixels.get(i * width + j);
                if (pixel == 1) {
                    System.out.print("#" + " ");
                } else {
                    System.out.print(" " + " ");
                }
            }
            System.out.println();
        }
    }

    private static int partOne(List<String> input, int width, int height) {
        Option<List<Integer>> listWithSmallestAmountOfZeros =
                input.map(Integer::valueOf)
                        .sliding(width * height, width * height)
                        .minBy(a -> a.filter(integer -> integer == 0)
                                .size()
                        );
        int ones = listWithSmallestAmountOfZeros.get()
                .filter(integer -> integer == 1).size();
        int twos = listWithSmallestAmountOfZeros.get()
                .filter(integer -> integer == 2).size();

        return ones * twos;
    }
}
