package com.msendyka.adventofcode.p14;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.Arrays;
import java.util.Objects;

public class P14 {

    public static void main(String[] args) { //522131
        testCase("p14/test6.txt", 1);
        testCase("p14/test0.txt", 31);
        testCase("p14/test2.txt", 165);
        testCase("p14/test.txt", 165);
        testCase("p14/test3.txt", 13312);
        testCase("p14/test3.txt", 13312);
        testCase("p14/test4.txt", 180697);
        testCase("p14/test5.txt", 2210736);
        System.out.println("Part1: " + part1("p14/input.txt", 1));

        testCasePart2("p14/test3.txt", 82892753);
        testCasePart2("p14/test4.txt", 5586022);
        testCasePart2("p14/test5.txt", 460664);
        part2("p14/input.txt");
    }

    private static void testCasePart2(String taskInput, long expected) {
        long amount = part2(taskInput);
        if (amount != expected) {
            throw new IllegalStateException("Expected " + expected + " actual " + amount);
        }
        System.out.println(amount);
    }

    private static long part2(String taskInput) {

        long ore = 1000000000000L;
        long fuelRequired = binarySearch(taskInput, ore, 1, ore);

        System.out.println(fuelRequired);
        return fuelRequired;
    }

    private static long binarySearch(String taskInput, long hi, long lo, long oreLimit) {
        if(lo == hi || lo + 1 == hi) {
            return lo;
        }
        long mid = (hi + lo) / 2;
        long middle = part1(taskInput, mid);
        if (middle > oreLimit) {
            return binarySearch(taskInput, mid, lo, oreLimit);

        } else if (middle < oreLimit) {
            return binarySearch(taskInput, hi, mid, oreLimit);

        } else {
            throw new IllegalStateException();
        }
    }

    private static void testCase(String taskInput, int expected) {
        long amount = part1(taskInput, 1);
        if (amount != expected) {
            throw new IllegalStateException("Expected " + expected + " actual " + amount);
        }
        System.out.println(amount);
    }

    private static long part1(String taskInput, long ore) {
        List<String> strings = Functions.readInput(taskInput);
        List<Reaction> reactions = createReaction(strings);
        Reaction fuel1 = findFuel(reactions, "FUEL");
        fuel1.output.amount = fuel1.output.amount * ore;
        fuel1.input.forEach(f -> f.amount = f.amount * ore);
        react(reactions, "FUEL");
        return findFuel(reactions, "FUEL").input.get(0).amount;
    }


    private static List<Reaction> createReaction(List<String> strings) {
        return strings.map(s -> s.split("=>")).map(P14::reaction);
    }

    private static void react(List<Reaction> reactions, String fuel) {
        if (findFuel(reactions, fuel).input.size() == 1 && findFuel(reactions, fuel).input.get(0).compound.equals("ORE")) {
            return;
        }
        Reaction head = findFuel(reactions, fuel);
        Reaction toRemove = null;
        for (Reaction r : reactions) {
            if (head.input.contains(r.output)) {
                if (reactions
                        .remove(head)
                        .flatMap(reaction -> reaction.input)
                        .map(reactionCompound -> reactionCompound.compound)
                        .find(s -> s.equals(r.output.compound))
                        .isDefined()) {
                    continue;
                }
                head.changeInput(r.output, r.input);
                sumInputs(head);
                toRemove = r;
                break;
            }

        }
        react(reactions.remove(toRemove), fuel);

    }

    private static void sumInputs(Reaction head) {
        Map<String, List<ReactionCompound>> grouped = head.input.groupBy(a -> a.compound);
        head.input = grouped.map(gr -> new ReactionCompound(gr._1, gr._2.map(g -> g.amount).sum().longValue())).toList();
    }

    private static Reaction findFuel(List<Reaction> reactions, String fuel) {
        return reactions.find(reaction -> reaction.output.compound.equals(fuel)).get();
    }

    private static Reaction reaction(String[] arr) {
        String[] inputs = arr[0].split(",");
        List<ReactionCompound> inputsComp = List.ofAll(Arrays.asList(inputs)).map(P14::comp);

        ReactionCompound reactionCompound = comp(arr[1]);
        return new Reaction(inputsComp, reactionCompound);
    }

    private static ReactionCompound comp(String s) {
        String[] output = s.trim().split(" ");
        return new ReactionCompound(output[1], Integer.valueOf(output[0]));
    }


    public static class Reaction {
        private List<ReactionCompound> input;
        private ReactionCompound output;

        Reaction(List<ReactionCompound> input, ReactionCompound output) {
            this.input = input;
            this.output = output;
        }

        void changeInput(ReactionCompound toChange, List<ReactionCompound> instead) {
            int indexOf = input.indexOf(toChange);
            long amount = toChange.amount;
            long required = input.get(indexOf).amount;
            long multiply = getToMultiply(amount, required);
            List<ReactionCompound> toMutate = instead
                    .map(a -> a.multiply(multiply));

            input = input.removeAt(indexOf);
            input = input.appendAll(toMutate);
        }

        private long getToMultiply(long amount, long required) {
            long multiply = 1;
            if (required > amount) {
                multiply = required / amount;
                if (required % amount > 0) {
                    multiply++;
                }
            }
            return multiply;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Reaction reaction = (Reaction) o;
            return Objects.equals(input, reaction.input) &&
                    Objects.equals(output, reaction.output);
        }

        @Override
        public int hashCode() {
            return Objects.hash(input, output);
        }

        @Override
        public String toString() {
            return "Reaction{" +
                    "input=" + input +
                    ", output=" + output +
                    '}';
        }
    }

    public static class ReactionCompound {
        private String compound;
        private long amount;

        ReactionCompound(String compound, long amount) {
            this.compound = compound;
            this.amount = amount;
        }

        ReactionCompound multiply(long i) {
            return new ReactionCompound(compound, amount * i);
        }

        ReactionCompound plusWaste(int i) {
            return new ReactionCompound(compound, amount + i);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReactionCompound that = (ReactionCompound) o;
            return compound.equals(that.compound);
        }

        @Override
        public int hashCode() {
            return Objects.hash(compound);
        }

        @Override
        public String toString() {
            return amount + " " + compound;
        }
    }


}
