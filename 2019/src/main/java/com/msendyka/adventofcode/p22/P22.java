package com.msendyka.adventofcode.p22;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.LongStream;

public class P22 {

    public static void main(String[] args) {
        List<String> input = Functions.readInput("p22/input.txt");
        partOne(input);

        long deckLength = 119315717514047L;
        long lookedFor = 2020L;
        long currentIndex = lookedFor;
        System.out.println(shuffle2(input, deckLength, lookedFor, 101741582076661L));

    }

    private static long shuffle2(List<String> input, long deckLenght, long lookedFor, long iterations) {
        LinearFunction foo = partTwoVer2(input, deckLenght);
        LinearFunction polypow = polypow(foo.a, foo.b, iterations, deckLenght);
        BigInteger mod = polypow.a.multiply(BigInteger.valueOf(lookedFor)).add(polypow.b).mod(BigInteger.valueOf(deckLenght));
        long l = mod.longValue();
        if (l < 0) {
            return deckLenght + l;
        }
        return l;
    }

    private static LinearFunction polypow(BigInteger a, BigInteger b, long m, long n) {
        if (m == 0) {
            return new LinearFunction(BigInteger.ONE, BigInteger.ZERO);
        }
        if (m % 2 == 0) {
            return polypow(a.multiply(a).mod(BigInteger.valueOf(n)), a.multiply(b).add(b).mod(BigInteger.valueOf(n)), m / 2, n);
        } else {
            LinearFunction foo = polypow(a, b, m - 1, n);
            return new LinearFunction(a.multiply(foo.a).mod(BigInteger.valueOf(n)), a.multiply(foo.b).add(b).mod(BigInteger.valueOf(n)));
        }
    }

    private static LinearFunction partTwoVer2(List<String> input, long deckLength) {
        java.util.List<Shuffle> types = new ArrayList<>();
        getShuffles(input, types);
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("0");
        for (int i = types.size() - 1; i >= 0; i--) {
            Shuffle shuffle = types.get(i);
            ShuffleType type = shuffle.type;
            if (type == ShuffleType.DEAL_NEW) {
                a = a.negate();
                b = BigInteger.valueOf(deckLength).subtract(b).subtract(BigInteger.ONE);
            }
            if (type == ShuffleType.DEAL_INCREMENT) {
                long n = shuffle.argument;
                BigInteger zz = BigInteger.valueOf(n).modInverse(BigInteger.valueOf(deckLength));
                long z = zz.longValue();
                a = a.multiply(zz).mod(BigInteger.valueOf(deckLength));
                b = b.multiply(zz).mod(BigInteger.valueOf(deckLength));
            }
            if (type == ShuffleType.CUT) {
                long cut = shuffle.argument;
                if (cut < 0) {
                    cut = deckLength - Math.abs(cut);
                }
                b = b.add(BigInteger.valueOf(cut)).mod(BigInteger.valueOf(deckLength));
            }
        }
        return new LinearFunction(a, b);
    }

    private static long partTwo(List<String> input, long deckLength, long currentIndex, long iterations) {
        java.util.List<Shuffle> types = new ArrayList<>();
        getShuffles(input, types);
        long result = currentIndex;
        for (long i = 0; i < iterations; i++) {
            for (int j = 0; j < input.size(); j++) {
                Shuffle shuffle = types.get(j);
                ShuffleType type = shuffle.type;
                if (type == ShuffleType.DEAL_NEW) {
                    result = (deckLength - result - 1);
                }
                if (type == ShuffleType.DEAL_INCREMENT) {
                    result = (result * shuffle.argument) % deckLength;
                }
                if (type == ShuffleType.CUT) {
                    long cut = shuffle.argument;
                    if (cut < 0) {
                        cut = deckLength - Math.abs(cut);
                    }
                    result = (result + deckLength - cut) % deckLength;
                }

            }
        }
        return result;
    }

    private static void getShuffles(List<String> input, java.util.List<Shuffle> types) {
        for (String line : input) {
            ShuffleType type = type(line);
            long arg = 0;
            if (type == ShuffleType.CUT) {
                String cutString = line.split(" ")[1];
                arg = Long.valueOf(cutString);
            } else if (type == ShuffleType.DEAL_INCREMENT) {
                String inc = line.split(" ")[3];
                arg = Long.valueOf(inc);
            }
            types.add(new Shuffle(type, arg));
        }
    }

    private static void partOne(List<String> input) {
        List<Long> smallDeck = List.of(0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l);
        List<Long> bigDeck = List.ofAll(LongStream.range(0, 10007).toArray());
        List<Long> deck = List.ofAll(bigDeck);
        int iterar = 10;
        for (int i = 0; i < iterar; i++) {
            deck = execute(input, deck);
        }
        System.out.println(deck.zipWithIndex().filter(a -> a._1 == 2019));
        System.out.println(deck.zipWithIndex().filter(a -> a._2 == 2019));
        long[] arr = new long[10007];
        long[] arr2 = new long[10007];
        for (int i = 0; i < 10007L; i++) {
            long test = test(bigDeck, i, iterar);
            long test2 = test2(bigDeck, i, iterar);
            arr[(int) test] = i;
            arr2[i] = test2;
        }

        for (long l : arr) {
            System.out.print(l + ",");
        }
        System.out.println();
        for (long l : arr2) {
            System.out.print(l + ",");
        }
        System.out.println();
        System.out.println(partTwo(input, 10007L, 2019L, iterar));
        System.out.println(shuffle2(input, 10007L, 2019L, iterar));
        System.out.println(arr[2019]);
        System.out.println(arr2[2019]);
    }

    private static long test2(List<Long> smallDeck, int i, int i1) {
        return shuffle2(Functions.readInput("p22/input.txt"), smallDeck.length(), i, i1);

    }

    private static long test(List<Long> smallDeck, int currentIndex, int i) {
        return partTwo(Functions.readInput("p22/input.txt"), smallDeck.length(), currentIndex, i);
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

    private static class Shuffle {
        ShuffleType type;
        long argument;

        public Shuffle(ShuffleType type, long argument) {
            this.type = type;
            this.argument = argument;
        }
    }

    private static class LinearFunction {
        final BigInteger a, b;

        public LinearFunction(BigInteger a, BigInteger b) {
            this.a = a;
            this.b = b;
        }
    }
}
