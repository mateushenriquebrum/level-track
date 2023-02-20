package com.king.application;

import com.king.application.Router.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.king.application.Router.METHOD.GET;
import static com.king.application.Router.METHOD.POST;
import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouterTest {

    private final Params NOTHING = new Params(of());

    private List<Route> selections =
            List.of(
                    new Route(GET, new Template("/home/<name>"), request -> "Welcome home !!!"),
                    new Route(POST, new Template("/exception"), request -> { throw new RuntimeException(); })
            );

    @Test
    void shouldProvideTheHandlerAssociated() {
        assertEquals("Welcome home !!!", new Router(selections).of(GET, "/home/user").apply(NOTHING));
    }

    @Test
    void shouldThrowIfTheHandlerNotAssociated() {
        assertThrows(NotFoundException.class, () -> new Router(selections).of(POST, "/home/user"));
        assertThrows(NotFoundException.class, () -> new Router(selections).of(POST, "/index"));
    }

    @Test
    void shouldPropagateException() {
        assertThrows(RuntimeException.class, () -> new Router(selections).of(GET, "/exception").apply(NOTHING));
    }
}
