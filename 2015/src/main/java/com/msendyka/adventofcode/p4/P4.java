package com.msendyka.adventofcode.p4;

import com.msendyka.adventofcode.Functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class P4 {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String input = Functions.readInput("p4/input.txt").head();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        System.out.println(toHexString(md5.digest("abcdef609043".getBytes())));
        int counter = 0 ;
        while(!toHexString(md5.digest((input + counter).getBytes())).startsWith("00000")) {
            counter++;
        }
        System.out.println(counter);
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
