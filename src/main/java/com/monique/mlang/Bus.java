package com.monique.mlang;

import java.util.Arrays;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;
import static com.monique.mlang.util.u_byte.ubyte;

public class Bus implements Memory {
    private u_byte[] memory;

    public Bus(String rom) {
        memory = new u_byte[0xFFFF];
        Arrays.setAll(memory, i -> ubyte(0));
        loadRom(rom);
    }

    public void loadRom(String rom) {
        for (int i = 0; i < rom.length(); i += 8) {
            memory[i / 8].set(Integer.parseInt(rom.substring(i, i + 8), 2));
        }
    }

    public void clear() {
        for (var ub : memory) {
            ub.set(0);
        }
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
