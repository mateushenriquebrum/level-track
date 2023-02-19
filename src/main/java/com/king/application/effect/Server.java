package com.king.application.effect;

import com.king.application.*;
import com.king.application.Selector.*;
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
    private Selector selector;

    public Server(int port, Dispatcher dispatcher, Selector selector) throws IOException {
        this.http = create(new InetSocketAddress(port), 0);
        this.dispatcher = dispatcher;
        this.selector = selector;
    }

    /**
     * It starts the server, it uses just the root context and determine
     * the correct handler using dispatcher all together with the resolver
     */
    public void start() {
        this.http.createContext("/", exchange -> {
            var handler = selector.of(
                    METHOD.valueOf(exchange.getRequestMethod().toUpperCase()),
                    exchange.getRequestURI().getPath());
            var params = extract(exchange);
            dispatcher
                    .schedule(handler, params)
                    .thenAccept(result -> {
                        try {
                            evaluate(exchange, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //general exception have occurred
                            System.exit(1);
                        }
                    });
        });
        http.setExecutor(null);
        this.http.start();
    }

    private Params extract(HttpExchange exchange) throws IOException {
        var params = selector.params(exchange.getRequestURI().getPath());
        params.put("body", new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.US_ASCII));
        params.putAll(new Query(exchange.getRequestURI().getQuery()).params());
        return new Params(params);
    }

    private void evaluate(HttpExchange exchange, String result) throws IOException {
        byte[] content;
        var response = exchange.getResponseBody();
        try {
            content = result == null ? new byte[]{} : result.getBytes(StandardCharsets.US_ASCII);
            exchange.sendResponseHeaders(200, content.length);
        } catch (NotFoundException n) {
            content = "Not Found".getBytes(StandardCharsets.US_ASCII);
            exchange.sendResponseHeaders(404, content.length);
        } catch (Exception e) {
            content = "Something went wrong".getBytes(StandardCharsets.US_ASCII);
            exchange.sendResponseHeaders(500, content.length);
        }
        response.write(content);
        response.close();
    }
}
