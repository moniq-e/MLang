package com.monique.mlang;

import java.util.Arrays;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;
import static com.monique.mlang.util.u_byte.ubyte;

public class Bus implements Memory {
    private u_byte[] memory;
    private int romSize;

    public Bus(String rom) {
        memory = new u_byte[0xFFFF];
        Arrays.setAll(memory, i -> ubyte(0));
        loadRom(rom);
    }

    public void loadRom(String rom) {
        clear();
        romSize = rom.length() / 8;
        for (int i = 0; i < romSize; i++) {
            memory[i].set(Integer.parseInt(rom.substring(i * 8, i * 8 + 8), 2));
        }
    }

    private void clear() {
        for (int i = 0; i < romSize; i++) {
            memory[i].set(0);
        }
        romSize = 0;
    }

    private int getAddrWithPadd(int addr) {
        return addr + romSize;
    }

    @Override
    public u_byte memRead(int addr) {
        return memory[addr];
    }
    
    @Override
    public void memWrite(int addr, u_byte value) {
        memory[addr].set(value);
    }

    public u_byte ramRead(int addr) {
        return memRead(getAddrWithPadd(addr));
    }
    
    public void ramWrite(int addr, u_byte value) {
        memWrite(getAddrWithPadd(addr), value);
    }
}
