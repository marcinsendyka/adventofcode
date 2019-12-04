package com.msendyka.adventofcode.p4;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.PrimitiveIterator;

public class P4 {

    public static void main(String[] args) {
        String[] input = Functions.readInput("p4/input.txt").head().split("-");
        int begin  = Integer.valueOf(input[0]);
        int end  = Integer.valueOf(input[1]);
        int count = 0 ;
        System.out.println(adjacent(111111));
        System.out.println(adjacent(112233));
        System.out.println(adjacent(234444));
        System.out.println(adjacent(222334));
        System.out.println(adjacent(111122));
        for (int i = begin; i < end; i++) {
            boolean twoAdjacent = adjacent(i);
            int prev;
            PrimitiveIterator.OfInt iterator1 = String.valueOf(i).chars().iterator();
            prev = iterator1.next();
            boolean satisfied = true;

            while(iterator1.hasNext()) {
                int a = iterator1.next();
                if(prev > a) {
                   satisfied = false;
                   break;
                }
                prev = a;
            }

            if(twoAdjacent && satisfied) {
                System.out.println(i);
                count++;
            }

        }
        System.out.println(count);

        int i =0 ;
    }

    private static boolean adjacent(int i) {
        boolean twoAdjacent = false;
        PrimitiveIterator.OfInt iterator = String.valueOf(i).chars().iterator();

//        int prev = iterator.next();
//        int prev2 = -1;
//        while(iterator.hasNext()) {
//            int a = iterator.next();
//            if(a != prev && prev == prev2) {
//                twoAdjacent = true;
//                break;
//            }
//            prev2 = prev;
//            prev = a;
//            if(a == prev && prev == prev2) {
//                if(iterator.hasNext() ){
//                    iterator.next();
//                }
//            }
//        }
//
//        return twoAdjacent;
        int size = List.ofAll(String.valueOf(i).chars().mapToObj(Integer::new))
                .sliding(3, 1)
                .filter(l -> l.size() == 3 && l.distinct().size() != 1)
                .filter(l -> l.size() == 3 && l.distinct().size() == 2)
                .size();

//        List<Integer> integers = List.ofAll(String.valueOf(i).chars().mapToObj(Integer::new));
//        for (int j = 0; j < integers.size() - 2; j+=2) {
//            if(
//                    ((j == 0) || (integers.get(j - 1).intValue() != integers.get(j).intValue()))
//                            && integers.get(j).intValue() == integers.get(j + 1).intValue() && integers.get(j + 1).intValue() != integers.get(j+2).intValue()) {
//                return true;
//            }
//        }
//        if(integers.last().intValue() ==  integers.get(integers.size() - 2).intValue() && integers.get(integers.size() - 2).intValue() != integers.get(integers.size() - 3).intValue()) {
//            return true;
//        }
//
//        return false;
       return size > 0;

    }
}
