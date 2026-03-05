package com.monique.mlang.util;

public interface Memory {

    public u_byte memRead(int addr);

    public void memWrite(int addr, u_byte value);

    public default u_short memRead16(int pos) {
        return u_short.of((memRead(pos + 1).get() << 8) | memRead(pos).get());
    }

    public default void memWrite16(int pos, u_short value) {
        memWrite(pos, u_byte.of(value.get() & 0xFF));
        memWrite(pos + 1, u_byte.of((value.get() >> 8) & 0xFF));
    }
}