package com.monique.mlang;

import java.util.Arrays;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;

public class Bus implements Memory {
    private u_byte[] memory;

    public Bus(String rom) {
        memory = new u_byte[0xFFFF];
        Arrays.fill(memory, new u_byte());
        loadRom(rom);
    }

    public void loadRom(String rom) {
        
    }

    @Override
    public u_byte memRead(int addr) {
        return memory[addr];
    }

    @Override
    public void memWrite(int addr, u_byte value) {
        memory[addr].set(value);
    }
}
