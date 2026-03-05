package com.monique.mlang.util;

import static com.monique.mlang.util.u_byte.ubyte;

public interface Memory {

    public u_byte memRead(int addr);

    public void memWrite(int addr, u_byte value);

    public default u_short memRead16(int pos) {
        return u_short.of((memRead(pos + 1).get() << 8) | memRead(pos).get());
    }

    public default void memWrite16(int pos, u_short value) {
        memWrite(pos, ubyte(value.get() & 0xFF));
        memWrite(pos + 1, ubyte((value.get() >> 8) & 0xFF));
    }
}