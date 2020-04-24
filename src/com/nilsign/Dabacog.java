package com.nilsign;

public class Dabacog {

    private static final String dabacogVersion = "0.0.1";

    public static void main(String[] arguments) {
        if (arguments != null && arguments[0].equals("-v")) {
            System.out.println("Dabacog 0.0.1 - development version");
        } else {
            System.out.println("Dabacog 0.0.1");
        }
    }
}
