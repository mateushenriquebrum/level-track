package com.king.application;

import java.util.*;
import java.util.logging.*;

import static java.lang.String.format;

public record Params(Map<String, String> mapping) {
    public Optional<Integer> asInteger(String key) {
        try {
            var val = Integer.parseInt(this.mapping.get(key));
            return Optional.of(val);
        } catch (Exception e) {
            logger.info(format("Impossible to parse param $d", key));
            return Optional.empty();
        }
    }

    public Optional<String> asString(String key) {
        return Optional.of(this.mapping.get(key));
    }

    private static Logger logger = Logger.getLogger(Params.class.getPackageName());
}
