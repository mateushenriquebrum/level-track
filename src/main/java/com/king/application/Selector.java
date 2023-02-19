package com.king.application;

import java.util.*;
import java.util.function.*;


/**
 * Resolver is responsible to identify a path and match with some registered template
 * Also it supply the correspondent handler with parameters extracted from the URL
 * It has no side effect
 */
public record Selector(List<Select> selects) {

    /**
     * Get the handler associated with a URL as long as its parameters
     *
     * @param sample Full sample to be match against a template
     * @return Supplier that lazily evaluate the handler with its request when needed or NotFoundException
     */

    public Function<Params, String> of(METHOD method, String sample) {
        var found = this
                .selects
                .stream()
                .filter(res -> res.template.equivalent(sample) && res.method.equals(method))
                .findFirst()
                .orElseThrow(NotFoundException::new);

        return found.handler;
    }

    public Map<String, String> params(String sample) {
        return this
                .selects
                .stream()
                .filter(res -> res.template.equivalent(sample))
                .findFirst()
                .map(select -> select.template().params(sample))
                .orElse(Map.of());
    }

    public record Select(METHOD method, Template template, Function<Params, String> handler){}
    public enum METHOD {
        GET,
        POST
    }
    //OPTIONAL and remove it
    public class NotFoundException extends RuntimeException {}

}
