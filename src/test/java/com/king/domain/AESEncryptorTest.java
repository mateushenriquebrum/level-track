package com.king.domain;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AESEncryptorTest {

    @Test
    void shouldEncodeAndDecode() {
        var sec = "some";
        var enc = new AESEncryptor();
        assertNotEquals(sec, enc.enc(sec));
        assertEquals(sec, enc.dec(enc.enc(sec)));
    }
}
