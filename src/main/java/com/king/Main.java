package com.king;

import com.king.application.*;
import com.king.application.effect.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

import static java.util.Map.of;

public class Main {

    private static Map<String, Function<Map<String, String>, String>> mapping = of("/home", req -> "Hello");
    public static void main(String[] args) throws IOException {
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        var server = new Server(port, new Dispatcher(new Resolver(mapping)));
        server.start();
    }
}