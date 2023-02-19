package com.king;

import com.king.application.*;
import org.junit.jupiter.api.*;


import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateTest {
    @Test
    void shouldBeValidateTemplateDefinition() {
        var first = new Template("/user/<name>");
        var full = new Template("/user/<name>/<surname>");
        var sub = new Template("/user/<name>/<surname>/son/<Bart>");
        assertTrue(first.equivalent("/user/Homer"));
        assertFalse(first.equivalent("/user/Homer/Simpson"));
        assertTrue(full.equivalent("/user/Homer/Simpson"));
        assertTrue(sub.equivalent("/user/Homer/Simpson/son/Bart"));
    }

    @Test
    void shouldExtractParameters() {
        var template = new Template("/user/<name>/<surname>/son/<son>");
        assertEquals(
                of("name", "Homer", "surname", "Simpson", "son", "Bart"),
                template.params("/user/Homer/Simpson/son/Bart"));
        assertEquals(of(), template.params("/user"));
    }

}
