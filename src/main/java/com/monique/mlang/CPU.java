package com.monique.mlang;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;
import com.monique.mlang.util.u_short;

import static com.monique.mlang.util.u_byte.ubyte;
import static com.monique.mlang.util.u_short.ushort;

public class CPU implements Memory {
    private u_short pc = ushort(0);
    private Bus bus;

    public CPU(Bus bus) {
        this.bus = bus;
    }

    public void run() {
        while (true) {
            var code = memRead(pc).get();
            incPC();

            switch (code) {
                //BRK
                case 0x00 -> {
                    return;
                }
                //PRT
                case 0x01 -> {
                    var addr = memRead(pc);
                    System.out.println(memRead(addr.get()).get());
                    incPC();
                }
                //STR
                case 0x02 -> {
                    var addr = memRead(pc);
                    var value = memRead(pc.get() + 1);
                    memWrite(addr.get(), value);
                    incPC(2);
                }
                //ADD
                case 0x03 -> {
                    var addr = memRead(pc);
                    var a = memRead(pc.get() + 1);
                    var b = memRead(pc.get() + 2);
                    memWrite(addr.get(), ubyte(a.get() + b.get()));
                    incPC(3);
                }
            }
        }
    }

    public void incPC() {
        pc.set(pc.get() + 1);
    }

    public void incPC(int value) {
        pc.set(pc.get() + value);
    }

    @Override
    public u_byte memRead(int addr) {
        return bus.memRead(addr);
    }

    @Override
    public void memWrite(int addr, u_byte value) {
        bus.memWrite(addr, value);
    }

    public u_byte ramRead(int addr) {
        return bus.ramRead(addr);
    }

    public void ramWrite(int addr, u_byte value) {
        bus.ramWrite(addr, value);
    }
}
