package com.monique.mlang.util;

import static com.monique.mlang.util.u_short.ushort;

public final class u_byte {
    private short ubyte = 0;

    private u_byte(int value) {
        set(value);
    }

    public static u_byte ubyte(int value) {
        return new u_byte(value);
    }

    public u_short u_short() {
        return ushort(this);
    }

    public short get() {
        return Unsign.unsignByte(ubyte);
    }

    public void set(int value) {
        this.ubyte = Unsign.unsignByte(value);
    }

    public void set(u_byte ubyte) {
        set(ubyte.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;

        if (obj instanceof u_byte) {
            return ((u_byte) obj).get() == get();
        }
        if (obj instanceof Byte) {
            return equals((byte) obj);
        }
        if (obj instanceof Number) {
            return equals(((Number) obj).intValue());
        }
        return false;
    }

    public boolean equals(int value) {
        return value == get();
    }
    public boolean equals(short value) {
        return value == get();
    }
    public boolean equals(byte value) {
        return Unsign.unsignByte(value) == get();
    }
}