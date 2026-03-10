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
                //break
                case 0x00 -> {
                    return;
                }
                //print
                case 0x01 -> {
                    System.out.println(ramRead(memRead(pc).get()).get());
                    incPC();
                }
                //store
                case 0x02 -> {
                    ramWrite(memRead(pc).get(), memRead(pc.get() + 1));
                    incPC(2);
                }
                //add
                case 0x03 -> {
                    ramWrite(memRead(pc).get(), ubyte(memRead(pc.get() + 1).get() + memRead(pc.get() + 2).get()));
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
