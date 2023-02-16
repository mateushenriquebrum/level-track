package com.king;

import com.king.application.*;
import com.king.application.Resolver.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.king.application.Resolver.METHOD.GET;
import static com.king.application.Resolver.METHOD.POST;
import static com.king.application.Resolver.equivalent;
import static com.king.application.Resolver.extract;
import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResolverTest {
    @Test
    void shouldBeEquivalentUrls() {
        assertTrue(equivalent("/a/<b>", "/a/1"));
        assertTrue(equivalent("/a/<c>", "/a/2"));
        assertTrue(equivalent("/a/<d>/e", "/a/3/e"));
        assertTrue(equivalent("/a/<d>/e", "/a/d/e"));
        assertTrue(equivalent("/a/<b>", "/a/b"));
        assertTrue(equivalent("/a/<b>/c/<d>", "/a/7/c/string"));
    }

    @Test
    void shouldNotBeEquivalentUrls() {
        assertFalse(equivalent("/a/<b>", "/a"));
        assertFalse(equivalent("/a/<c>", "/a/3/e"));
        assertFalse(equivalent("/a/<d>/e", "/a/d/3"));
        assertFalse(equivalent("/a/<d>/e/<g>", "/a/d/3"));
    }

    @Test
    void shouldExtractParametersFromUrl() {
        assertEquals(of("b", "999"), extract("/a/<b>", "/a/999"));
        assertEquals(of("b", "999"), extract("/a/<b>/c", "/a/999/c"));
        assertEquals(of("b", "7", "d", "string"), extract("/a/<b>/c/<d>", "/a/7/c/string"));
        assertEquals(of("a", "a"), extract("/<a>", "/a"));
        assertEquals(of(), extract("/a/b/c", "/a/b/c"));
    }

    private List<Resolve> resolves =
            List.of(
                    new Resolve(GET, "/home/<name>", request -> String.format("Welcome home %s!!!", request.get("name"))),
                    new Resolve(GET, "/exception", request -> { throw new RuntimeException(); })
            );

    @Test
    void shouldProvideTheHandlerAssociated() {
        assertEquals("Welcome home Homer!!!", new Resolver(resolves).of(GET, "/home/Homer").get());
        assertEquals("Welcome home Bart!!!", new Resolver(resolves).of(GET, "/home/Bart").get());
    }

    @Test
    void shouldThrowIfTheHandlerNotAssociated() {
        assertThrows(NotFoundException.class, () -> new Resolver(resolves).of(GET, "/index"));
        assertThrows(NotFoundException.class, () -> new Resolver(resolves).of(POST, "/home/Homer"));
    }

    @Test
    void shouldPropagateException() {
        var handler = new Resolver(resolves).of(GET, "/exception");
        assertThrows(RuntimeException.class, handler::get);
    }
}
