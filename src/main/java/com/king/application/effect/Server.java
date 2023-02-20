package com.king.application.effect;

import com.king.application.*;
import com.king.application.Router.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.logging.*;

import static com.sun.net.httpserver.HttpServer.create;
import static java.lang.String.format;

/**
 * Wrap around the HttpServer with a configurable resolver and a thread based request
 * It has side effect as it uses IO
 */
public class Server {
    private HttpServer http;
    private Dispatcher dispatcher;
    private Router router;

    public Server(int port, Dispatcher dispatcher, Router router) throws IOException {
        this.http = create(new InetSocketAddress(port), 0);
        this.dispatcher = dispatcher;
        this.router = router;
    }

    /**
     * It starts the server, it uses just the root context and determine
     * the correct handler using dispatcher all together with the resolver
     */
    public void start() {
        logger.finest("Defining dispatcher and selector");
        this.http.createContext("/", exchange -> {
            logger.info(format("URL:%s", exchange.getRequestURI().getRawPath()));
            var handler = router.of(
                    METHOD.valueOf(exchange.getRequestMethod().toUpperCase()),
                    exchange.getRequestURI().getPath());
            var params = extract(exchange);
            dispatcher
                    .schedule(handler, params)
                    .thenAccept(result -> {
                        try {
                            evaluate(exchange, result);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                            System.exit(1);
                        }
                    });
        });
        http.setExecutor(null);
        this.http.start();
        logger.info(format("Server started on : %s", http.getAddress()));

    }

    private Params extract(HttpExchange exchange) throws IOException {
        var params = router.params(exchange.getRequestURI().getPath());
        var body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.US_ASCII);
        if (!body.isBlank()) params.put("body", body);
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

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

}
