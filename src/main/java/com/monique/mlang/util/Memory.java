package com.monique.mlang.util;

import static com.monique.mlang.util.u_byte.ubyte;
import static com.monique.mlang.util.u_short.ushort;

public interface Memory {

    public u_byte memRead(int addr);

    public void memWrite(int addr, u_byte value);

    public default u_byte memRead(u_byte addr) {
        return memRead(addr.get());
    }

    public default void memWrite(u_byte addr, u_byte value) {
        memWrite(addr.get(), value);
    }

    public default u_byte memRead(u_short addr) {
        return memRead(addr.get());
    }

    public default void memWrite(u_short addr, u_byte value) {
        memWrite(addr.get(), value);
    }

    public default u_short memRead16(int pos) {
        return ushort((memRead(pos + 1).get() << 8) | memRead(pos).get());
    }

    public default void memWrite16(int pos, u_short value) {
        memWrite(pos, ubyte(value.get() & 0xFF));
        memWrite(pos + 1, ubyte((value.get() >> 8) & 0xFF));
    }
}