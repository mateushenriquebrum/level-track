package com.king.application;


import java.util.concurrent.*;
import java.util.function.*;
import java.util.logging.*;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * Dispatcher schedule a path's handler
 * It has no side effect
 */
public class Dispatcher {
    private ExecutorService service = newCachedThreadPool();

    public CompletableFuture<String> schedule(Function<Params, String> handler, Params params) {
        return supplyAsync(() -> handler.apply(params), service);
    }

    private Logger logger = Logger.getLogger(this.getClass().getPackageName());
}
