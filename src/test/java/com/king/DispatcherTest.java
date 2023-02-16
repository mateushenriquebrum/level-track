package com.king;

import com.king.application.*;
import com.king.application.Dispatcher;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import static java.util.Collections.synchronizedList;
import static java.util.Map.of;
import static java.util.concurrent.CompletableFuture.allOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatcherTest {
    private final Map<String, Function<Map<String, String>, String>> mapping = of("/home", (req) -> "done");
    private final Resolver resolver = new Resolver(mapping);
    @Test
    void shouldScheduleDispatch() throws ExecutionException, InterruptedException {
        var future = new Dispatcher(resolver).schedule("/home");
        assertEquals("done", future.get());
    }

    @Test
    void shouldParalyseDispatches() throws InterruptedException, ExecutionException {
        var SIZE = 1_000_000;
        var futures = synchronizedList(new ArrayList<CompletableFuture>());
        var pool = new Dispatcher(resolver);
        for (int i = 0; i < SIZE; i++) {
            var future = pool.schedule("/home");
            futures.add(future);
        }
        allOf(futures.toArray(CompletableFuture[]::new)).get();
        assertEquals(SIZE, futures.size());
    }
}
