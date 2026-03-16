package com.monique;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import static com.monique.mlang.util.u_byte.ubyte;
import com.monique.mlang.Bus;
import com.monique.mlang.CPU;
import com.monique.mlang.Compiler;
import com.monique.mlang.Instructions;

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
}
