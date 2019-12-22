package com.msendyka.adventofcode.p22;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.stream.LongStream;

public class P22 {

    public static void main(String[] args) {
        List<String> input = Functions.readInput("p22/input.txt");
        partOne(input);
        List<Long> bigDeck = List.ofAll(LongStream.range(0, 119315717514047l).toArray());
        List<Long> deck = execute(input, bigDeck);

    }

    private static void partOne(List<String> input) {
        List<Long> smallDeck = List.of(0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l);
        List<Long> bigDeck = List.ofAll(LongStream.range(0, 10007).toArray());
        List<Long> deck = execute(input, bigDeck);
        System.out.println(deck.zipWithIndex().filter(a -> a._1 == 2019));
    }

    private static List<Long> execute(List<String> input, List<Long> deck) {
        for (String line : input) {
            ShuffleType type = type(line);
            if (type == ShuffleType.DEAL_NEW) {
                deck = dealNew(deck);
            }
            if (type == ShuffleType.DEAL_INCREMENT) {
                String inc = line.split(" ")[3];
                deck = dealIncrement(deck, Integer.valueOf(inc));
            }
            if (type == ShuffleType.CUT) {
                String cut = line.split(" ")[1];
                deck = cut(deck, Integer.valueOf(cut));

            }
        }
        System.out.println(deck);
        return deck;
    }

    private static List<Long> cut(List<Long> input, Integer cut) {
        if (cut > 0) {
            return doCut(input, cut);
        }
        if (cut < 0) {
            return doCut(input, input.length() - Math.abs(cut));
        }
        throw new IllegalArgumentException();
    }

    private static List<Long> doCut(List<Long> input, Integer cut) {
        List<Long> toCut = input.subSequence(0, cut);
        return input.subSequence(cut).appendAll(toCut);
    }

    private static List<Long> dealIncrement(List<Long> cards, Integer increment) {
        long[] arr = new long[cards.size()];
        int counter = 0;
        for (int i = 0; counter < cards.size(); i = i + increment) {
            arr[i % cards.size()] = cards.get(counter++);
        }
        return List.ofAll(arr);
    }

    private static List<Long> dealNew(List<Long> cards) {
        return cards.reverse();
    }

    private static ShuffleType type(String line) {
        if (line.equals("deal into new stack")) {
            return ShuffleType.DEAL_NEW;
        }
        if (line.contains("deal with increment")) {
            return ShuffleType.DEAL_INCREMENT;
        }
        if (line.contains("cut")) {
            return ShuffleType.CUT;
        }
        throw new IllegalArgumentException();
    }

    private enum ShuffleType {
        DEAL_NEW,
        CUT,
        DEAL_INCREMENT
    }
}
