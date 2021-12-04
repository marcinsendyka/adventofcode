package com.msendyka.adventofcode;

import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.IntStream;

public class Functions {

    public static List<String> readInput(String taskInput) {

        return reader()
                .andThen(BufferedReader::new)
                .andThen(BufferedReader::lines)
                .andThen(List::ofAll)
                .apply(taskInput);
    }

    public static List<String> readInputOneLineChars(String taskInput) {

        return List.ofAll(readInput(taskInput)
                .head()
                .chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf));
    }

    public static String[] readInputOneLine(String taskInput) {
        return Functions.readInput(taskInput).head().split(",");
    }

    public static <T> List<List<T>> splitEvery(List<T> list, int every) {
        return
                List.ofAll(
                        IntStream.range(0, every)
                                .mapToObj(layerIndex -> list
                                        .zipWithIndex()
                                        .filter(tuple -> tuple._2 % every == layerIndex)
                                        .map(Tuple2::_1)
                                )
                );

    }

    private static Function1<String, Reader> reader() {
        return resourceName -> new InputStreamReader(Functions.class.getResourceAsStream(resourceName));
    }


    public static long[] stringArrayToLongArray(String[] strings) {
        long[] arr = new long[strings.length];
        for (int j = 0; j < arr.length; j++) {
            arr[j] = Long.valueOf(strings[j]);
        }
        return arr;
    }
}
