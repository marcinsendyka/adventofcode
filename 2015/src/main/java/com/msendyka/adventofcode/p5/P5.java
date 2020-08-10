package com.msendyka.adventofcode.p5;

import com.msendyka.adventofcode.Functions2015;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

import java.util.function.Predicate;

public class P5 {

    public static void main(String[] args) {
        List<String> strings = Functions2015.readInput("p5/input.txt");

        System.out.println(partOne(strings));
        System.out.println(partTwo(strings).size());

    }

    private static List<String> partTwo(List<String> strings) {
        return strings
                    .filter(twoLettersTwiceWithoutOverlapping())
                    .filter(repeatsWithLetterBetween());
    }

    private static Predicate<String> twoLettersTwiceWithoutOverlapping() {
        return string -> {
            int idx = 0;
            do {
                String pattern = string.substring(idx, idx + 2);
                boolean contains = string.substring(idx + 2).contains(pattern);
                if (contains) {
                    return true;
                }
            } while (++idx < string.length() - 1);

            return false;
        };
    }

    private static Predicate<String> repeatsWithLetterBetween() {
        return string -> Stream.ofAll(string.chars().boxed())
                .sliding(3)
                .filter(stream -> stream.get(0).equals(stream.get(2)))
                .size() > 0;
    }


    private static int partOne(List<String> strings) {
        return strings
                .filter(string -> !string.contains("ab"))
                .filter(string -> !string.contains("cd"))
                .filter(string -> !string.contains("pq"))
                .filter(string -> !string.contains("xy"))
                .filter(string -> containsTwoLettersOneAfterAnother(string))
                .filter(string -> string.replaceAll("[^aeiou]", "").length() >= 3)
                .size();
    }

    private static boolean containsTwoLettersOneAfterAnother(String string) {
        return Stream.ofAll(string.chars().boxed())
                .sliding(2)
                .filter(stream -> stream.distinct().size() == 1)
                .size() > 0;
    }
}
