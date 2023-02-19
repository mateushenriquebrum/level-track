package com.king.application;

import java.util.*;

public class Query {
    private String query;

    public Query(String query) {
        this.query = query;
    }
    public Map<String, String> params() {
        if(this.query == null || this.query.isBlank()) return Map.of();
        var params = new HashMap<String, String>();
        var pairs = this.query.split("=|&");
        for(int i = 0; i < pairs.length ;  i=i+2) {
            params.put(pairs[i], pairs[i+1]);
        }
        return params;
    }
}
