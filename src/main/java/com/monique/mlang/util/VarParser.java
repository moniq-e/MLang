package com.monique.mlang.util;

import java.util.HashMap;

public class VarParser {
    private int counter = 0;
    private HashMap<String, String> variables = new HashMap<>();

    public String getVariableAddr(String var) {
        var addr = variables.get(var);

        if (addr != null) return addr;

        addr = String.valueOf(counter++);
        variables.put(var, addr);
        return addr;
    }

    public int getCounter() {
        return counter;
    }
}
