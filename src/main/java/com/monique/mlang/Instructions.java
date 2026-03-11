package com.monique.mlang;

import java.util.HashMap;

public class Instructions {
    private HashMap<String, Integer> map = new HashMap<>();

    public Instructions() {
        map.put("BRK", 0x00);
        map.put("PRT", 0x55);
        map.put("STR", 0xFF);
        map.put("ADD", 0x81);
        map.put("CMP", 0x42);
        map.put("JMP", 0xF0);
        map.put("POS", 0x03);
        map.put("EQJ", 0x0F);
        map.put("NEJ", 0x0E);
    }

    public Integer get(String name) {
        return map.get(name.toUpperCase());
    }
}
