package com.monique;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import static com.monique.mlang.util.u_byte.ubyte;
import com.monique.mlang.Bus;

public class TestBus {
    @Test
    public void testMemReadWriteToRam() {
        var bus = new Bus("");

        bus.memWrite(0x01, ubyte(0x55));
        assertEquals(bus.memRead(0x01), 0x55);
        assertEquals(bus.memRead(0x01), ubyte(0x55));

        assertNotEquals(bus.memRead(0x01), 0x54);
        assertNotEquals(bus.memRead(0x01), ubyte(0x54));

        assertNotEquals(0x55, bus.memRead(0x02).get());
    }
}
