package com.king;

import com.king.application.*;
import com.king.application.Resolver.*;
import com.king.application.effect.*;

import java.io.*;
import java.util.*;

import static com.king.application.Resolver.METHOD.GET;
import static java.util.List.of;

public class Main {

    private static List<Resolve> mapping = of(
            new Resolve(GET, "/home", req -> "Hello")
    );

    public static void main(String[] args) throws IOException {
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        var server = new Server(port, new Dispatcher(new Resolver(mapping)));
        server.start();
    }
}