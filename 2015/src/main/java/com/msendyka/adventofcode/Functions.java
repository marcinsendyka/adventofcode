package com.msendyka.adventofcode;

import io.vavr.Function1;
import io.vavr.collection.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Functions {

    public static List<String> readInput(String taskInput) {

        return reader()
                .andThen(BufferedReader::new)
                .andThen(BufferedReader::lines)
                .andThen(List::ofAll)
                .apply(taskInput);
    }

    private static Function1<String, Reader> reader() {
        return resourceName -> new InputStreamReader(Functions.class.getResourceAsStream(resourceName));
    }


}
