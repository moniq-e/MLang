package com.monique.mlang;

public class Compiler {

    public static String compile(String raw) {
        var bin = "";

        var inst = raw.split(" |\n");

        for (var i : inst) {
            bin += (switch (i) {
                case "BRK":
                    yield parse(0x00);
                case "PRT":
                    yield parse(0x01);
                default:
                    yield parse(i);
            });
        }
        return bin;
    }

    private static String parse(int value) {
        if (value <= 255) {
            return Integer.toBinaryString((1 << 8) | value).substring(1);
        }

        var res = "";
        var toSplit = Integer.toBinaryString(value);
        var bytes = toSplit.split("(?=(.{8})+$)");

        for (var b : bytes) {
            var i = Integer.parseInt(b, 2);
            res += Integer.toBinaryString((1 << 8) | i).substring(1);
        }
        return res;
    }

    private static String parse(String value) {
        try {
            var i = Integer.valueOf(value);
            return parse(i);
        } catch (Exception e) {
            var bytes = value.getBytes();
            var res = "";

            for (byte b : bytes) {
                res += Integer.toBinaryString((1 << 8) | b).substring(1);
            }
            return res;
        }
    }
}
