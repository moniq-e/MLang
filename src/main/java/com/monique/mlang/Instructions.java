package com.monique.mlang;

import java.util.HashMap;

public class Instructions {
    private HashMap<String, Integer> map = new HashMap<>();

    public Instructions() {
        map.put("BRK", 0x00);
        map.put("PRT", 0x55);
        map.put("STR", 0xFF);
        map.put("ADD", 0x81);
    }

    public Integer get(String name) {
        return map.get(name.toUpperCase());
    }
}
