package com.king.application;

import java.util.*;


public record Query(String query) {
    public Map<String, String> params() {
        return Map.of();
    }
}
