package com.monique.mlang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.monique.mlang.util.Memory;
import com.monique.mlang.util.u_byte;
import static com.monique.mlang.util.u_byte.ubyte;

public class Bus implements Memory {
    private u_byte[] memory;
    private int romSize;

    public Bus(File rom) throws IOException {
        this();
        loadRom(rom);
    }

    public Bus() {
        memory = new u_byte[0xFFFF];
        Arrays.setAll(memory, i -> ubyte(0));
    }

    public void loadRom(File rom) throws IOException {
        clear();

        var fis = new FileInputStream(rom);
        var buffer = new byte[1024];
        int bytesRead, k = 0;

        while ((bytesRead = fis.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                memory[k++].set(buffer[i]);
            }
        }
        fis.close();
        romSize = k;
    }

    private void clear() {
        for (int i = 0; i < romSize; i++) {
            memory[i].set(0);
        }
        romSize = 0;
    }

    @Override
    public u_byte memRead(int addr) {
        return memory[addr];
    }
    
    @Override
    public void memWrite(int addr, u_byte value) {
        memory[addr].set(value);
    }

    public int getRomSize() {
        return romSize;
    }
}
