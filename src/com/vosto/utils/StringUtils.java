package com.vosto.utils;

public class StringUtils {

    public static String asPrice(double price) {
        String asprice = "R ";
        if (price>999.99) {
            asprice += String.format("%.2f", price);
        } else {
            asprice += String.format("%.2f", price);
        }
        return asprice;
    }

}
