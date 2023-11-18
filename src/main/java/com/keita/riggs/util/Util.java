package com.keita.riggs.util;

import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static Long generateID(int bound) {
        Random random = new Random();
        return (long) random.nextInt(bound);
    }

    public static Calendar getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    public static List<Month> getMonths() {
//        691-03-7965
        List<Month> monthList = new ArrayList<>();
        monthList.add(Month.JANUARY);
        monthList.add(Month.FEBRUARY);
        monthList.add(Month.MARCH);
        monthList.add(Month.APRIL);
        monthList.add(Month.MAY);
        monthList.add(Month.JUNE);
        monthList.add(Month.JULY);
        monthList.add(Month.AUGUST);
        monthList.add(Month.SEPTEMBER);
        monthList.add(Month.OCTOBER);
        monthList.add(Month.NOVEMBER);
        monthList.add(Month.DECEMBER);
        return monthList;
    }
}
