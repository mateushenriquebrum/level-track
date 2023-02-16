package com.king;

import com.king.application.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

import static java.util.Map.of;

public class Main {
    public static void main(String[] args) throws IOException {
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        Map<String, Function<Map<String, String>, String>> mapping = of(
                "/await/<t>", req -> {
                    var w = Integer.parseInt(req.get("t"));
                    try {
                        Thread.sleep(w);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "processed after "+w;
                },
                "/home", req -> "Hello");
        var server = new Server(port, new Dispatcher(new Resolver(mapping)));
        server.start();
    }
}