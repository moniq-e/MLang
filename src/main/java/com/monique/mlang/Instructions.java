package com.monique.mlang;

import java.util.HashMap;

public class Instructions {
    private HashMap<String, Integer> map = new HashMap<>();

    public Instructions() {
        map.put("BRK", 0x00);
        map.put("PRT", 0x01);
        map.put("STR", 0x02);
        map.put("ADD", 0x03);
    }

    public Integer get(String name) {
        return map.get(name);
    }
}
