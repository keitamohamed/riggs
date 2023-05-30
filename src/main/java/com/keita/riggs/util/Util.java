package com.keita.riggs.util;

import java.util.Random;

public class Util {

    public static Long generateID(int bound) {
        Random random = new Random();
        return (long) random.nextInt(bound);
    }
}
