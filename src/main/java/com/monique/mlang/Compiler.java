package com.monique.mlang;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.monique.mlang.util.CompiledFile;
import com.monique.mlang.util.VarParser;

public class Compiler {
    private static Instructions instmap = new Instructions();

    public static CompiledFile compile(File file) throws IOException, URISyntaxException {
        var raw = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        var bin = "";

        int startIdx;
        while ((startIdx = raw.indexOf("//")) != -1) {
            int endIdx;
            while ((endIdx = raw.indexOf("\n", startIdx)) != -1) {
                var a = raw.substring(0, startIdx);
                var b = raw.substring(endIdx + 1, raw.length());
                raw = a.concat(b);
                break;
            }
        }

        var splitted = raw.split(" |\\R");
        surroundString(splitted);

        var varParser = new VarParser(); // Traduzir variáveis
        var posix = new ArrayList<Integer>(); // Posição final de region
        var k = 0; // Contador de posição final de region

        var strcount = -1; // STR indicador;
        var strsize = 0; // Tamanho a ser convertido o valor literal a ser guardado pelo STR

        for (int i = 0; i < splitted.length; i++) {
            var value = splitted[i];
            if (value == null || value.equals("")) continue;

            switch (value) {
                case "_":
                    bin += "_";
                    k++;
                    continue;
                case "END":
                    posix.add(k);
                    break;
                case "STR":
                    strcount = 0;
                    break;
            }

            if (value.startsWith("'")) {
                value = varParser.getVariableAddr(value);
            }
            
            var inst = instmap.get(value);
            var res = inst != null ? parse(inst) : parse(value);
            
            if (strcount == 1) strsize = Integer.parseInt(value);
            if (strcount == 3) {
                res = parse(value, strsize);
                strcount = -1;
            }
            if (strcount >= 0) strcount++;

            bin += res;
            k += res.length() / 8;
        }

        while (!posix.isEmpty()) {
            bin = bin.replaceFirst("_", parse(posix.removeFirst()));
        }

        var compiledPath = changeExtension(file, ".mbin");
        Files.write(compiledPath, binaryStringToByteArray(bin));

        return new CompiledFile(compiledPath, varParser);
    }

    public static void surroundString(String[] splitted) {
        for (int i = 0; i < splitted.length; i++) {
            if (splitted[i] == null) continue;

            if (splitted[i].startsWith("\"")) {
                var res = splitted[i];
                var init = i;

                do {
                    res = res.concat(" " + splitted[++i]);
                } while (!res.endsWith("\""));

                System.arraycopy(splitted, i + 1, splitted, init + 1, splitted.length - i -1);

                for (int j = splitted.length - 1; j > splitted.length - 1 - (i - init); j--) {
                    splitted[j] = null;
                }

                splitted[init] = res.replace("\"", "");
            }
        }
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

    /**
     * @param value to be parsed
     * @param size to return value in bytes
     * @return Big Endian string binary representation of {@code value} in size bytes
     */
    private static String parse(String value, int size) {
        var i = Integer.reverseBytes(Integer.parseInt(value)) >> (32 - size * 8);
        return Integer.toBinaryString((1 << (8 * size)) | i).substring(1);
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
            var bytes = value.getBytes(StandardCharsets.UTF_8);
            var res = "";

            for (byte b : bytes) {
                res += Integer.toBinaryString((1 << 8) | b).substring(1);
            }
            return res;
        }
    }
}
