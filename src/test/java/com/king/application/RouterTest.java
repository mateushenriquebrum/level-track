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

    private List<Route> routes =
            List.of(
                    new Route(GET, new Template("/home/<name>"), request -> "Welcome home !!!"),
                    new Route(POST, new Template("/exception"), request -> { throw new RuntimeException(); })
            );

    private Router router = new Router(routes);

    @Test
    void shouldProvideTheHandlerAssociated() {
        assertEquals("Welcome home !!!", router.handlerOf(GET, "/home/user").apply(NOTHING));
    }

    @Test
    void shouldThrowIfTheHandlerNotAssociated() {
        assertThrows(NotFoundException.class, () -> router.handlerOf(POST, "/home/user"));
        assertThrows(NotFoundException.class, () -> router.handlerOf(POST, "/index"));
    }

    @Test
    void shouldPropagateException() {
        assertThrows(RuntimeException.class, () -> router.handlerOf(GET, "/exception").apply(NOTHING));
    }

    @Test
    void shouldExtractParams() {
        assertEquals(of("name", "Carl"), router.paramsOf("/home/Carl"));
        assertEquals(of(), router.paramsOf("/index"));
    }
}
