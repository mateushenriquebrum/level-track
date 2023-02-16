package com.king.application;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

import static com.sun.net.httpserver.HttpServer.create;

public class Server {
    private HttpServer server;
    private Dispatcher dispatcher;

    public Server(int port, Dispatcher dispatcher) throws IOException {
        this.server = create(new InetSocketAddress(port), 0);
        this.dispatcher = dispatcher;
    }

    public void start() {
        this.server.createContext("/", (exchange) -> {
             dispatcher
                     .schedule(exchange.getRequestURI().getPath())
                     .thenAccept(result -> {
                         var response = exchange.getResponseBody();
                         try {
                             var body = result.getBytes(StandardCharsets.US_ASCII);
                             exchange.sendResponseHeaders(200, body.length);
                             response.write(body);
                             response.close();
                         } catch (IOException e) {
                             throw new RuntimeException(e);
                         }
                     });
        });
        server.setExecutor(null);
        this.server.start();
    }
}
