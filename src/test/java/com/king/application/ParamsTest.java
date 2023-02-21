package com.king.application;

import org.junit.jupiter.api.*;

import static java.util.Map.of;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParamsTest {
    @Test
    void shouldConvert(){
        var params = new Params(of("a", "1", "b", "s"));
        assertEquals(1, params.asInteger("a").get());
        assertEquals("s", params.asString("b").get());
    }

    @Test
    void shouldBeEmpty(){
        var params = new Params(of());
        assertEquals(empty(), params.asInteger("a"));
    }
}
