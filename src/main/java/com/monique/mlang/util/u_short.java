package com.monique.mlang.util;

public final class u_short {
    private int ushort = 0;

    private u_short(int value) {
        set(value);
    }

    public static u_short ushort(int value) {
        return new u_short(value);
    }

    public static u_short ushort(u_byte ubyte) {
        return ushort(ubyte.get());
    }

    public int get() {
        return ushort & 0xFFFF;
    }

    public void set(int value) {
        this.ushort = value & 0xFFFF;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;

        if (obj instanceof u_short) {
            return ((u_short) obj).get() == get();
        }
        if (obj instanceof Short) {
            return equals((short) obj);
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
        return Unsign.unsignShort(value) == get();
    }
    public boolean equals(byte value) {
        return Unsign.unsignByte(value) == get();
    }
}