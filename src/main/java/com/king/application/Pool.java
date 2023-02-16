package com.king.application;

import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newCachedThreadPool;

public class Pool {
    private Dispatcher dispatcher;
    private ExecutorService service = newCachedThreadPool();

    public Pool(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public CompletableFuture<String> schedule(String path) {
        var handler = this.dispatcher.of(path);
        return supplyAsync(handler, service);
    }
}
