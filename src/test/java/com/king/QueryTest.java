package com.king;


import com.king.application.*;
import org.junit.jupiter.api.*;
import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryTest {

    @Test
    void shouldExtractAllParameter() {
        assertEquals(of("token", "1234"), new Query("token=1234").params());
        assertEquals(of("token", "1234", "key", "999"), new Query("token=1234&key=999").params());
        assertEquals(of(), new Query(null).params());
        assertEquals(of(), new Query(" ").params());
    }

}
