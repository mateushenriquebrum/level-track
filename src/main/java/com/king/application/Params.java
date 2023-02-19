package com.king.application;

import java.util.*;

public record Params(Map<String, String> mapping) {
    public Optional<Integer> asInteger(String key) {
        try {
            var val = Integer.parseInt(this.mapping.get(key));
            return Optional.of(val);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> asString(String key) {
        return Optional.of(this.mapping.get(key));
    }
}
