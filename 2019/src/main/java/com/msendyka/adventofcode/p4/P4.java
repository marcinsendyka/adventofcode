package com.msendyka.adventofcode.p4;

import com.msendyka.adventofcode.Functions;
import io.vavr.collection.List;

import java.util.PrimitiveIterator;

public class P4 {

    public static void main(String[] args) {
        String[] input = Functions.readInput("input.txt").head().split("-");
        int begin  = Integer.valueOf(input[0]);
        int end  = Integer.valueOf(input[1]);
        int count = 0 ;
        System.out.println(adjacent(111122)); // true
        System.out.println(adjacent(144459)); // false
        System.out.println(adjacent(677778)); // false
        System.out.println(adjacent(137778)); // false
        System.out.println(adjacent(111111)); //false
        System.out.println(adjacent(112233)); //true
        System.out.println(adjacent(234444)); //false
        System.out.println(adjacent(222334)); //true
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
        char[] chars = String.valueOf(i).toCharArray();

        for (int j = 0; j < chars.length - 2; j++) {

            char first = chars[j];
            char second = chars[j+1];
            char third = chars[j+2];
            if(first == second && second != third) {
                twoAdjacent = true;
                break;
            }
            if(first == second && second ==  third) {
                for (int k = j; k < chars.length; k++) {
                    if(chars[k] != first) {
                        j = k-1;
                        break;
                    }
                }
            }

        }
        if(chars[chars.length -1 ] == chars[chars.length -2 ] && chars[chars.length -2 ] != chars[chars.length -3 ]) {
            return true;
        }
        return twoAdjacent;

    }
}
