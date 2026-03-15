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
        map.put("REG", 0x03);
        map.put("END", 0x04);
        map.put("ENT", 0x05);
        map.put("EQJ", 0x0F);
        map.put("NEJ", 0x0E);
        map.put("FOR", 0x0D);
        map.put("FRE", 0x10);
    }

    public Integer get(String name) {
        return map.get(name.toUpperCase());
    }

    public String export() {
        var keys = map.keySet().toArray(new String[map.keySet().size()]);
        var res = "";
        for (int i = 0; i < keys.length; i++) {
            res += keys[i] + "|";
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }
}
