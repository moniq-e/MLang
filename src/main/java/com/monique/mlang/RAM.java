package com.monique.mlang;

import static com.monique.mlang.util.u_byte.ubyte;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;

public class RAM implements Memory {
    private Bus bus;
    private short[] handler; //real unpadded memory addresses
    private u_byte[] alocated;
    private short end;

    public RAM(Bus bus) {
        this.bus = bus;
        this.handler = new short[bus.getMemorySize()];
        this.alocated = new u_byte[bus.getMemorySize()];
        this.end = 0;
    }

    public void malloc(int addr, int size) {
        if (size <= 0) return;
        if (end >= bus.getMemorySize() - bus.getRomSize()) return;

        for (int i = 0; i < size; i++) {
            handler[addr + i] = (short) (end + i);
        }
        alocated[addr] = ubyte(size);
        end += size;
    }

    public void free(int addr) {
        var size = alocated[addr].get();
        if (size <= 0) return;

        alocated[addr].set(0);
        if (size + addr < end) {
            shift(addr, size);
        }
        end -= size;
        handler[addr] = -1;
    }

    private void shift(int addr, short size) {

        for (int i = addr + 1; i < bus.getVarParserCount(); i++) {
            if (alocated[i] == null) continue;

            var iSize = alocated[i].get();
            if (iSize == 0) continue;

            for (int j = 0; j < iSize; j++) {
                memWrite(i - iSize + j, memRead(i + j));
                handler[i] -= size;
            }
        }
    }

    private int getRealAddr(int fakeAddr) {
        return handler[fakeAddr];
    }

    private int getPaddedAddr(int realAddr) {
        return Math.min(realAddr + bus.getRomSize(), bus.getMemorySize());
    }

    @Override
    public u_byte memRead(int addr) {
        return bus.memRead(getPaddedAddr(getRealAddr(addr)));
    }

    @Override
    public void memWrite(int addr, u_byte value) {
        bus.memWrite(getPaddedAddr(getRealAddr(addr)), value);
    }

    public String exportMemory() {
        return bus.exportMemory();
    }
}
