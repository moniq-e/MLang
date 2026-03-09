package com.monique.mlang;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;
import com.monique.mlang.util.u_short;
import static com.monique.mlang.util.u_short.ushort;

public class CPU implements Memory {
    private u_short pc = ushort(0);
    private Bus bus;

    public CPU(Bus bus) {
        this.bus = bus;
    }

    @Override
    public u_byte memRead(int addr) {
        return bus.memRead(addr);
    }

    @Override
    public void memWrite(int addr, u_byte value) {
        bus.memWrite(addr, value);
    }
}
