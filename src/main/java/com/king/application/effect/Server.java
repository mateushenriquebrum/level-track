package com.king.application.effect;

import com.king.application.*;
import com.king.application.Router.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import java.util.function.*;
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
            logger.info(format("Requested :%s", exchange.getRequestURI().getRawPath()));
            Function<Params, String> handler;
            Params params;
            try {
                handler = router.handlerOf(
                        METHOD.valueOf(exchange.getRequestMethod().toUpperCase()),
                        exchange.getRequestURI().getPath());
                params = extract(exchange);
            } catch (NotFoundException n) {
                var content = "Not Found".getBytes(StandardCharsets.US_ASCII);
                exchange.sendResponseHeaders(404, content.length);
                var response = exchange.getResponseBody();
                response.write(content);
                response.close();
                return;
            }

            dispatcher
                    .schedule(handler, params)
                    .thenAccept(result -> {
                        try {
                            var content = result == null ? new byte[]{} : result.getBytes(StandardCharsets.US_ASCII);
                            exchange.sendResponseHeaders(200, content.length);
                            var response = exchange.getResponseBody();
                            response.write(content);
                            response.close();
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                            System.exit(1);
                        }
                    })
                    .whenComplete((res, exp) -> {
                        if (exp != null) {
                            try {
                                var content = "Something went wrong".getBytes(StandardCharsets.US_ASCII);
                                exchange.sendResponseHeaders(500, content.length);
                                var response = exchange.getResponseBody();
                                response.write(content);
                                response.close();
                            } catch (Exception e) {
                                logger.severe(e.getMessage());
                                System.exit(1);
                            }
                        }
                    });
        });
        http.setExecutor(null);
        this.http.start();
        logger.info(format("Server started on : %s", http.getAddress()));

    }

    private Params extract(HttpExchange exchange) throws IOException {
        var params = router.paramsOf(exchange.getRequestURI().getPath());
        var body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.US_ASCII);
        if (!body.isBlank()) params.put("body", body);
        params.putAll(new Query(exchange.getRequestURI().getQuery()).params());
        return new Params(params);
    }

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

}
