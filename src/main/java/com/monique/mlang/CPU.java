package com.monique.mlang;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.Unsign;
import com.monique.mlang.util.u_byte;
import com.monique.mlang.util.u_short;

import static com.monique.mlang.util.u_byte.ubyte;
import static com.monique.mlang.util.u_short.ushort;

public class CPU implements Memory {
    private u_short pc = ushort(0);
    private Bus bus;
    private RAM ram;

    public CPU(Bus bus) {
        this.bus = bus;
        this.ram = new RAM(bus);
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
                //PRT addr_value
                case 0x55 -> {
                    var addr = memRead(pc);
                    System.out.println(ram.memRead(addr).get());
                    incPC();
                }
                //STR addr_dest value
                case 0xFF -> {
                    var addr = memRead(pc);
                    var value = memRead(pc.get() + 1);
                    ram.memWrite(addr, value);
                    incPC(2);
                }
                //ADD addr_dest addr_1 addr_2
                case 0x81 -> {
                    var addr = memRead(pc);
                    var aAddr = memRead(pc.get() + 1);
                    var bAddr = memRead(pc.get() + 2);
                    var a = ram.memRead(aAddr);
                    var b = ram.memRead(bAddr);
                    ram.memWrite(addr, ubyte(a.get() + b.get()));
                    incPC(3);
                }
                //CMP addr_dest addr_1 addr_2
                case 0x42 -> {
                    var addr = memRead(pc);
                    var aAddr = memRead(pc.get() + 1);
                    var bAddr = memRead(pc.get() + 2);
                    var a = ram.memRead(aAddr);
                    var b = ram.memRead(bAddr);
                    var res = Math.min(Math.max(a.get() - b.get(), -1), 1);
                    ram.memWrite(addr, ubyte(res));
                    incPC(3);
                }
                //REG addr_dest addr_end
                case 0x03 -> {
                    var addr = memRead(pc);
                    var end = memRead(pc.get() + 1);
                    incPC(2);
                    ram.memWrite(addr, ubyte(pc.get()));
                    pc.set(end.get());
                }
                //END
                case 0x04 -> {
                    break;
                }
                //ENT addr_dest
                case 0x05 -> {
                    var ptr_addr = memRead(pc);

                    if (ptr_addr.equals(0)) {
                        incPC();
                        break;
                    }

                    var addr = ram.memRead(ptr_addr);

                    memWrite(pc, ubyte(0));
                    pc.set(addr.get());
                }
                //JMP addr_dest
                case 0xF0 -> {
                    var addr = ram.memRead(memRead(pc));
                    pc.set(addr.get());
                }
                //EQJ addr_1 addr_2 addr_dest
                case 0x0F -> {
                    var aAddr = memRead(pc.get());
                    var bAddr = memRead(pc.get() + 1);
                    var addr = ram.memRead(memRead(pc.get() + 2));
                    var a = ram.memRead(aAddr);
                    var b = ram.memRead(bAddr);

                    if (a.equals(b)) {
                        pc.set(addr.get());
                    } else {
                        incPC(3);
                    }
                }
                //NQJ addr_1 addr_2 addr_dest
                case 0x0E -> {
                    var aAddr = memRead(pc.get());
                    var bAddr = memRead(pc.get() + 1);
                    var addr = ram.memRead(memRead(pc.get() + 2));
                    var a = ram.memRead(aAddr);
                    var b = ram.memRead(bAddr);

                    if (!a.equals(b)) {
                        pc.set(addr.get());
                    } else {
                        incPC(3);
                    }
                }
                //FOR addr_dest addr_length addr_i
                case 0x0D -> {
                    var addr = ram.memRead(memRead(pc.get()));
                    var length = ram.memRead(memRead(pc.get() + 1));
                    var i = ram.memRead(memRead(pc.get() + 2));

                    if (!length.equals(i)) {
                        pc.set(addr.get());
                    } else {
                        incPC(3);
                    }
                }
            }
        }
    }

    private void incPC() {
        pc.set(pc.get() + 1);
    }

    private void incPC(int value) {
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

    private class RAM implements Memory {
        private Bus bus;
        private short[] handler;
        private u_byte[] alocated;
        private short end;

        public RAM(Bus bus) {
            this.bus = bus;
            this.handler = new short[bus.getMemorySize()];
            this.alocated = new u_byte[bus.getMemorySize()];
            this.end = 0;
        }

        public void malloc(int addr, int size) {
            if (end >= bus.getMemorySize()) return;

            handler[addr] = end;
            alocated[addr] = ubyte(size);
            end += size;
        }

        public void free(int addr) {
            var size = alocated[addr].get();
            alocated[addr] = ubyte(0);
            if (size + addr < end) {
                shift(addr);
            }
        }

        private void shift(int addr) {
            var realAddr = getRealAddr(addr);
            
        }

        private int getRealAddr(int addr) {
            return handler[addr];
        }

        private int getPaddedAddr(int addr) {
            return Math.min(addr + bus.getRomSize(), bus.getMemorySize());
        }

        @Override
        public u_byte memRead(int addr) {
            return bus.memRead(getPaddedAddr(getRealAddr(addr)));
        }

        @Override
        public void memWrite(int addr, u_byte value) {
            bus.memWrite(getPaddedAddr(getRealAddr(addr)), value);
        }
    }
}
