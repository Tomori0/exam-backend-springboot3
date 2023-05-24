package com.up9e.exam.util;

import java.util.Random;

public class RandomUtils {

    public static String getRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(36);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

    public static String getRandomStringWithLowercase(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(36);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

}
