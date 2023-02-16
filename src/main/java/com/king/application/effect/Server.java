package com.king.application.effect;

import com.king.application.*;
import com.king.application.Resolver.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

import static com.sun.net.httpserver.HttpServer.create;

/**
 * Wrap around the HttpServer with a configurable resolver and a thread based request
 * It has side effect as it uses IO
 */
public class Server {
    private HttpServer http;
    private Dispatcher dispatcher;

    public Server(int port, Dispatcher dispatcher) throws IOException {
        this.http = create(new InetSocketAddress(port), 0);
        this.dispatcher = dispatcher;
    }

    /**
     * It starts the server, it uses just the root context and determine
     * the correct handler using dispatcher all together with the resolver
     */
    public void start() {
        this.http.createContext("/", exchange ->
                dispatcher
                        .schedule(
                                METHOD.valueOf(exchange.getRequestMethod().toUpperCase()),
                                exchange.getRequestURI().getPath())
                        .thenAccept(result -> {
                            try {
                                evaluate(exchange, result);
                            } catch (Exception e) {
                                //general exception have occurred
                                System.exit(1);
                            }
                        }));
        http.setExecutor(null);
        this.http.start();
    }

    private void evaluate(HttpExchange exchange, String result) throws IOException {
        byte[] body;
        var response = exchange.getResponseBody();
        try {
            body = result.getBytes(StandardCharsets.US_ASCII);
            exchange.sendResponseHeaders(200, body.length);
        } catch (NotFoundException n) {
            body = "Not Found".getBytes(StandardCharsets.US_ASCII);
            exchange.sendResponseHeaders(404, body.length);
        } catch (Exception e) {
            body = "Something went wrong".getBytes(StandardCharsets.US_ASCII);
            exchange.sendResponseHeaders(500, body.length);
        }
        response.write(body);
        response.close();
    }
}
