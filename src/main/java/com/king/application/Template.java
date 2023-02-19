package com.king.application;

import java.util.*;

import static java.util.regex.Pattern.compile;

public record Template(String definition) {
    /**
     * Verify if a URL conforms with a template
     * @param sample The URL to compare
     * @return True iff they are equivalent
     */
    public boolean equivalent(String sample) {
        return compile(expression(this.definition)).matcher(sample).find();
    }

    /**
     * Extract values from URL according a template
     * Premise: The sample is equivalent to the template
     * @param sample The URL with values
     * @return Map with variables from URL its values from sample
     */
    public Map<String, String> params(String sample) {
        var params = new HashMap<String, String>();
        var regex = expression(definition);
        var names = compile(regex).matcher(sanitize(definition));
        var values = compile(regex).matcher(sample);
        //if the sample match with the template proceed
        if (names.find() && values.find() && names.groupCount() == values.groupCount()){
            for(int i = 1 ; i <= names.groupCount() ; i++) {
                params.put(names.group(i), values.group(i));
            }
        }
        return params;
    }

    private String expression(String template) {
        return  "^" +
                template
                        .replace("/", "\\/")
                        .replaceAll("<\\w+?>", "([\\\\d\\\\w]*)") +
                "$";
    }

    private String sanitize(String template) {
        return template.replaceAll("[<>]", "");
    }

}
