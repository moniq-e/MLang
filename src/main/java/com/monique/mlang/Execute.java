package com.monique.mlang;

import java.io.File;

public class Execute {

    public static void main(String[] args) throws Exception {
        var file = new File(args[0]);
        var comp = Compiler.compile(file);

        var cpu = new CPU(new Bus(comp));
        cpu.run();
    }
}