package com.king;

import com.king.application.*;
import com.king.application.Dispatcher;
import com.king.application.Resolver.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.*;

import static com.king.application.Resolver.METHOD.GET;
import static java.util.Collections.synchronizedList;
import static java.util.concurrent.CompletableFuture.allOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatcherTest {
    private final List<Resolve> mapping = List.of(new Resolve(GET, "/home", (req) -> "done"));
    private final Resolver resolver = new Resolver(mapping);
    @Test
    void shouldScheduleDispatch() throws ExecutionException, InterruptedException {
        var future = new Dispatcher(resolver).schedule(GET, "/home");
        assertEquals("done", future.get());
    }

    @Test
    void shouldParalyseDispatches() throws InterruptedException, ExecutionException {
        var SIZE = 1_000_000;
        var futures = synchronizedList(new ArrayList<CompletableFuture>());
        var pool = new Dispatcher(resolver);
        for (int i = 0; i < SIZE; i++) {
            var future = pool.schedule(GET,"/home");
            futures.add(future);
        }
        allOf(futures.toArray(CompletableFuture[]::new)).get();
        assertEquals(SIZE, futures.size());
    }
}
