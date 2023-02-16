package com.king;

import com.king.application.*;
import com.king.application.Pool;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.Collections.synchronizedList;
import static java.util.Map.of;
import static java.util.concurrent.CompletableFuture.allOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PoolTest {
    private final Map<String, Function<Map<String, String>, String>> mapping = of("/home", (req) -> "done");
    private final Dispatcher dispatcher = new Dispatcher(mapping);
    @Test
    void shouldScheduleDispatch() throws ExecutionException, InterruptedException {
        var future = new Pool(dispatcher).schedule("/home");
        assertEquals("done", future.get());
    }

    @Test
    void shouldParallelDispatch() throws InterruptedException, ExecutionException {
        var SIZE = 1_000_000;
        var futures = synchronizedList(new ArrayList<CompletableFuture>());
        var pool = new Pool(dispatcher);
        for (int i = 0; i < SIZE; i++) {
            var future = pool.schedule("/home");
            futures.add(future);
        }
        allOf(futures.toArray(CompletableFuture[]::new)).get();
        assertEquals(SIZE, futures.size());
    }
}
