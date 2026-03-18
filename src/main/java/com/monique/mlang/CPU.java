package com.monique.mlang;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;
import com.monique.mlang.util.u_short;
import com.monique.mlang.util.Format;

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
                    var addr = memRead(pc).get();
                    int value = readInt(addr);

                    System.out.println(value);
                    incPC();
                }
                //PRS addr_value
                case 0x56 -> {
                    var addr = memRead(pc);
                    var size = ram.getSize(addr.get());
                    String value;

                    if (size > 1) {
                        value = Format.toString(ram.readAllBytes(addr.get()));
                    } else {
                        value = String.valueOf((char) ram.memRead(addr).get());
                    }

                    System.out.println(value);
                    incPC();
                }
                //STR size addr_dest value
                case 0xFF -> {
                    var size = memRead(pc).get();
                    var addr = memRead(pc.get() + 1);

                    ram.malloc(addr.get(), size);

                    if (size > 1) {
                        var ubArr = new u_byte[size];
                        for (int i = 0; i < size; i++) {
                            ubArr[i] = memRead(pc.get() + 2 + i);
                        }
                        ram.memWrite(addr, ubArr);
                    } else {
                        var value = memRead(pc.get() + 2);
                        ram.memWrite(addr, value);
                    }
                    incPC(2 + size);
                }
                //FRE addr
                case 0x10 -> {
                    var addr = memRead(pc);
                    ram.free(addr.get());
                    incPC();
                }
                //ADD addr_dest addr_1 addr_2
                case 0x81 -> {
                    var addr = memRead(pc);
                    var aAddr = memRead(pc.get() + 1).get();
                    var bAddr = memRead(pc.get() + 2).get();
                    var a = readInt(aAddr);
                    var b = readInt(bAddr);
                    var res = a + b;

                    if (res > 255) {
                        ram.memWrite(addr, Format.toUnsignedByteArray(res));
                    } else {
                        ram.memWrite(addr, ubyte(res));
                    }
                    incPC(3);
                }
                //CMP addr_dest addr_1 addr_2
                case 0x42 -> {
                    var addr = memRead(pc);
                    var aAddr = memRead(pc.get() + 1).get();
                    var bAddr = memRead(pc.get() + 2).get();
                    var a = readInt(aAddr);
                    var b = readInt(bAddr);

                    var res = Math.min(Math.max(a - b, -1), 1);
                    ram.memWrite(addr, ubyte(res));
                    incPC(3);
                }
                //REG addr_dest addr_end
                case 0x03 -> {
                    var addr = memRead(pc);
                    var end = memRead(pc.get() + 1);
                    incPC(2);

                    ram.malloc(addr.get(), 1);
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
                    var aAddr = memRead(pc.get()).get();
                    var bAddr = memRead(pc.get() + 1).get();
                    var addr = ram.memRead(memRead(pc.get() + 2)).get();
                    var a = readInt(aAddr);
                    var b = readInt(bAddr);

                    if (a == b) {
                        pc.set(addr);
                    } else {
                        incPC(3);
                    }
                }
                //NQJ addr_1 addr_2 addr_dest
                case 0x0E -> {
                    var aAddr = memRead(pc.get()).get();
                    var bAddr = memRead(pc.get() + 1).get();
                    var addr = ram.memRead(memRead(pc.get() + 2)).get();
                    var a = readInt(aAddr);
                    var b = readInt(bAddr);

                    if (a != b) {
                        pc.set(addr);
                    } else {
                        incPC(3);
                    }
                }
                //FOR addr_dest addr_length addr_i
                case 0x0D -> {
                    var addr = ram.memRead(memRead(pc.get()));
                    var length = readInt(memRead(pc.get() + 1).get());
                    var i = readInt(memRead(pc.get() + 2).get());

                    if (i < length) {
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

    private int readInt(int addr) {
        int size = ram.getSize(addr);
        int value;

        if (size > 1) {
            value = Format.toInt(ram.readAllBytes(addr));
        } else {
            value = ram.memRead(addr).get();
        }
        return value;
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
