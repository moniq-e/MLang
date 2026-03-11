package com.monique.mlang;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Compiler {
    private static Instructions instmap = new Instructions();

    public static void compile(String path) throws IOException, URISyntaxException {
        var raw = new String(Compiler.class.getResourceAsStream(path).readAllBytes(), StandardCharsets.UTF_8);
        var bin = "";

        var insts = raw.split(" |\\R");

        var posix = new ArrayList<Integer>();
        var k = 0;
        for (int i = 0; i < insts.length; i++) {
            var value = insts[i];
            if (value.equals("")) continue;

            if (value.equals("_")) {
                bin += "_";
                k++;
                continue;
            }

            if (value.equals("END")) {
                posix.add(k);
            }

            var inst = instmap.get(value);
            var res = inst != null ? parse(inst) : parse(value);
            bin += res;
            k += res.length() / 8;
        }

        while (!posix.isEmpty()) {
            bin = bin.replaceFirst("_", parse(posix.removeFirst()));
        }

        Files.write(changeExtension(new File(Compiler.class.getResource(path).toURI()), ".mbin"), binaryStringToByteArray(bin));
    }

    public static byte[] binaryStringToByteArray(String source) {
        var res = new byte[source.length() / 8];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) (Integer.parseInt(source.substring(i * 8, i * 8 + 8), 2) & 0xFF);
        }
        return res;
    }

    public static Path changeExtension(File f, String ext) {
        int i = f.getName().lastIndexOf('.');
        String name = i >= 0 ? f.getName().substring(0, i) : f.getName();
        return new File(f.getParent(), name + ext).toPath();
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
