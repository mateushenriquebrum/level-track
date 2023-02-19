package com.king.application;

import com.king.application.*;
import com.king.application.Selector.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.king.application.Selector.METHOD.GET;
import static com.king.application.Selector.METHOD.POST;
import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectorTest {

    private final Params NOTHING = new Params(of());

    private List<Select> selections =
            List.of(
                    new Select(GET, new Template("/home/<name>"), request -> "Welcome home !!!"),
                    new Select(POST, new Template("/exception"), request -> { throw new RuntimeException(); })
            );

    @Test
    void shouldProvideTheHandlerAssociated() {
        assertEquals("Welcome home !!!", new Selector(selections).of(GET, "/home/user").apply(NOTHING));
    }

    @Test
    void shouldThrowIfTheHandlerNotAssociated() {
        assertThrows(NotFoundException.class, () -> new Selector(selections).of(POST, "/home/user"));
        assertThrows(NotFoundException.class, () -> new Selector(selections).of(POST, "/index"));
    }

    @Test
    void shouldPropagateException() {
        assertThrows(RuntimeException.class, () -> new Selector(selections).of(GET, "/exception").apply(NOTHING));
    }
}
