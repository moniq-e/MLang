package com.monique.mlang.util;

import java.nio.file.Path;

public record CompiledFile(Path path, VarParser varParser) { }
