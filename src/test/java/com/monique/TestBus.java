package com.monique;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.Test;

import static com.monique.mlang.util.u_byte.ubyte;
import com.monique.mlang.Bus;
import com.monique.mlang.CPU;
import com.monique.mlang.Compiler;
import com.monique.mlang.Instructions;
import com.monique.mlang.RAM;

public class TestBus {

    public static void main(String[] args) throws Exception {
        var vp = Compiler.compile("/test.mlang");
        var comp = new File(TestBus.class.getResource("/test.mbin").toURI());

        var cpu = new CPU(new Bus(comp, vp));
        cpu.run();
    }

    @Test
    public void exportInstructions() {
        System.out.println(new Instructions().export());
    }

    @Test
    public void testMemReadWriteToRam() {
        var bus = new Bus();

        bus.memWrite(0x01, ubyte(0x55));
        assertEquals(0x55, bus.memRead(0x01).get());
        assertEquals(ubyte(0x55), bus.memRead(0x01).get());

        assertNotEquals(0x54, bus.memRead(0x01).get());
        assertNotEquals(ubyte(0x54), bus.memRead(0x01).get());

        assertNotEquals(0x55, bus.memRead(0x02).get());
    }

    @Test
    public void testRAMShift() {
        var ram = new RAM(new Bus());

        ram.malloc(0, 1);
        ram.malloc(1, 2);
        ram.malloc(2, 1);
        
        ram.memWrite(0, ubyte(1));
        ram.memWrite(1, ubyte(2));
        ram.memWrite(2, ubyte(3));
        
        ram.free(1);
        
        assertEquals(3, ram.memRead(2).get());
        
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ram.memWrite(1, ubyte(2)));

        ram.malloc(1, 1);
        ram.memWrite(1, ubyte(2));

        System.out.println(ram.exportMemory());
    }
}
