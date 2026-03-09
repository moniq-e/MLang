package com.monique.mlang;

public class Compiler {
    private static Instructions instmap = new Instructions();

    public static String compile(String raw) {
        var bin = "";

        var insts = raw.split(" |\n");

        for (var value : insts) {
            var inst = instmap.get(value);
            bin += inst != null ? parse(inst) : parse(value);
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
