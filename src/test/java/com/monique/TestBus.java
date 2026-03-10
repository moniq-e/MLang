package com.monique;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static com.monique.mlang.util.u_byte.ubyte;
import com.monique.mlang.Bus;
import com.monique.mlang.CPU;
import com.monique.mlang.Compiler;

public class TestBus {

    public static void main(String[] args) throws IOException {
        var raw = new String(TestBus.class.getResourceAsStream("/test.mlang").readAllBytes(), StandardCharsets.UTF_8);
        var comp = Compiler.compile(raw);

        var cpu = new CPU(new Bus(comp));
        cpu.run();
    }

    @Test
    public void testMemReadWriteToRam() {
        var bus = new Bus("");

        bus.memWrite(0x01, ubyte(0x55));
        assertEquals(0x55, bus.memRead(0x01).get());
        assertEquals(ubyte(0x55), bus.memRead(0x01).get());

        assertNotEquals(0x54, bus.memRead(0x01).get());
        assertNotEquals(ubyte(0x54), bus.memRead(0x01).get());

        assertNotEquals(0x55, bus.memRead(0x02).get());
    }

    @Test
    public void testCompiler() {
        
    }
}
