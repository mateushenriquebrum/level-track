package com.king.application;

import java.util.*;
import java.util.function.*;

import static java.util.regex.Pattern.compile;

/**
 * Resolver is responsible to identify a path and match with some registered template
 * Also it supply the correspondent handler with parameters extracted from the URL
 * It has no side effect
 */
public class Resolver {

    private List<Resolve> resolves;

    public Resolver(List<Resolve> resolves) {
        this.resolves = resolves;
    }

    /**
     * Verify if a URL conforms with a template
     * @param template The template using custom marks
     * @param sample The URL to compare
     * @return True iff they are equivalent
     */
    public static boolean equivalent(String template, String sample) {
        return compile(expression(template)).matcher(sample).find();
    }

    /**
     * Extract values from URL according a template
     * Premise: The sample is equivalent to the template
     * @see Resolver#equivalent(String, String)
     * @param template The template URL marking the values
     * @param sample The URL with values
     * @return Map with variables from URL its values from sample
     */
    public static Map<String, String> extract(String template, String sample) {
        var params = new HashMap<String, String>();
        var regex = expression(template);
        var names = compile(regex).matcher(sanitize(template));
        var values = compile(regex).matcher(sample);
        //if the sample match with the template proceed
        if (names.find() && values.find() && names.groupCount() == values.groupCount()){
            for(int i = 1 ; i <= names.groupCount() ; i++) {
                params.put(names.group(i), values.group(i));
            }
        }
        return params;
    }

    /**
     * Get the handler associated with a URL as long as its parameters
     * @param path Full path to be match against a template
     * @return Supplier that lazily evaluate the handler with its request when needed or NotFoundException
     */

    public Supplier<String> of(METHOD method, String path) {
        var found = this
                .resolves
                .stream()
                .filter(res -> equivalent(res.path, path) && res.method.equals(method))
                .findFirst()
                .orElseThrow(NotFoundException::new);

        var params = extract(found.path, path);
        return () -> found.handler.apply(params);
    }

    private static String sanitize(String template) {
        return template.replaceAll("[<>]", "");
    }

    private static String expression(String template) {
        return  "^" +
                template
                        .replace("/", "\\/")
                        .replaceAll("<\\w+?>", "([\\\\d\\\\w]*)") +
                "$";
    }

    public record Resolve(METHOD method, String path, Function<Map<String, String>, String> handler){}
    public enum METHOD {
        GET,
        POST
    }
}
