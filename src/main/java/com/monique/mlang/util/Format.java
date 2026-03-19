package com.monique.mlang.util;

import static com.monique.mlang.util.u_byte.ubyte;

import java.nio.charset.StandardCharsets;

public class Format {
    
    public static String toString(u_byte[] ubytes) {
        var byteArr = new byte[ubytes.length];

        for (int i = 0; i < ubytes.length; i++) {
            byteArr[i] = (byte) ubytes[i].get();
        }
        return new String(byteArr, StandardCharsets.UTF_8);
    }

    public static int toInt(u_byte[] ubytes) {
        int res = ubytes[0].get();

        for (int i = 1; i < ubytes.length; i++) {
            res = res | (ubytes[i].get() << (8 * i));
        }
        return res;
    }

    public static u_byte[] toUnsignedByteArray(int value) {
        var ubyteArr = new u_byte[((32 - Integer.numberOfLeadingZeros(value)) + 7) / 8];

        for (int i = 0; i < ubyteArr.length; i++) {
            ubyteArr[i] = ubyte(value >> (8 * i));
        }
        return ubyteArr;
    }
}