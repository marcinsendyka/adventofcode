package com.msendyka.adventofcode;

import io.vavr.Function2;

public enum Opcode {
    ADD((x,y) -> x + y),
    MULTIPLY((x,y) -> x * y);

    Function2<Long, Long, Long> execute;

    Opcode(Function2<Long,Long, Long> execute) {
        this.execute = execute;
    }
}
