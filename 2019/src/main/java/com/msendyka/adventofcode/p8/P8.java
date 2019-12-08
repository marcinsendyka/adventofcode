package com.msendyka.adventofcode.p8;

import com.msendyka.adventofcode.Functions;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class P8 {

    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int TRANSPARENT = 2;

    public static void main(String[] args) {
        List<String> input = Functions.readInputOneLine("p8/input.txt");

        int width = 25;
        int height = 6;
        int numberOfLayers = width * height;
        System.out.println(partOne(input, width, height));
        List<Integer> pixels = partTwo(input, numberOfLayers);
        printImage(width, height, pixels);
    }

    private static List<Integer> partTwo(List<String> input, int numberOfLayers) {
        return List.ofAll(IntStream.range(0, numberOfLayers)
                .mapToObj(layerIndex -> input
                        .map(Integer::valueOf)
                        .zipWithIndex()
                        .filter(tuple -> tuple._2 % numberOfLayers == layerIndex))
                .map(layersForEachPixel -> layersForEachPixel
                        .map(Tuple2::_1)
                        .find(P8::blackOrWhite)
                        .getOrElse(TRANSPARENT))

                .collect(Collectors.toList()));
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
