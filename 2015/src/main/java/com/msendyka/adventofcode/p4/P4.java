package com.msendyka.adventofcode.p4;

import com.msendyka.adventofcode.Functions2015;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class P4 {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String input = Functions2015.readInput("p4/input.txt").head();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        System.out.println(findPrefix(input, md5, "00000"));
        System.out.println(findPrefix(input, md5, "000000"));
    }

    private static int findPrefix(String input, MessageDigest md5, String desiredPrefix) {
        int counter = 0 ;
        while(!toHexString(md5.digest((input + counter).getBytes())).startsWith(desiredPrefix)) {
            counter++;
        }
        return counter;
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
