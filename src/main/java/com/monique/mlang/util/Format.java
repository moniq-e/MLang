package com.monique.mlang.util;

import static com.monique.mlang.util.u_byte.ubyte;

public class Format {
    
    public static String toString(u_byte[] bytes) {
        var charArr = new char[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            charArr[i] = (char) bytes[i].get();
        }
        return String.valueOf(charArr);
    }

    public static int toInt(u_byte[] bytes) {
        int res = bytes[0].get();

        for (int i = 1; i < bytes.length; i++) {
            res = res | (bytes[i].get() << (8 * i));
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