package com.king.application;

import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * Dispatcher schedule a path's handler
 * It has no side effect
 */
public class Dispatcher {
    private Resolver resolver;
    private ExecutorService service = newCachedThreadPool();

    public Dispatcher(Resolver resolver) {
        this.resolver = resolver;
    }

    public CompletableFuture<String> schedule(String path) {
        return supplyAsync(this.resolver.of(path), service);
    }
}
