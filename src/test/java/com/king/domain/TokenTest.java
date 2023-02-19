package com.king.domain;

import org.junit.jupiter.api.*;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenTest {

    private Encryptor DO_NOTHING = new Encryptor() {

        @Override
        public String enc(String raw) {
            return raw;
        }

        @Override
        public String dec(String sec) {
            return sec;
        }
    };

    @Test
    void shouldGenerateTokenWithExpiringTime() {
        var parts = new Token(DO_NOTHING).generate(123).split("::");
        var id = parseInt(parts[0]);
        var expire = parse(parts[1]);
        assertEquals(123, id);
        assertNotNull(expire);
    }

    @Test
    void shouldValidatedBasedOnTime() {
        var token = new Token(DO_NOTHING);
        var generated = token.generate(123);
        assertTrue(token.valid(generated));
    }
}
