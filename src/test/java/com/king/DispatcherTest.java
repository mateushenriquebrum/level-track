package com.king;

import com.king.application.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import static java.util.Collections.synchronizedList;
import static java.util.Map.of;
import static java.util.concurrent.CompletableFuture.allOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatcherTest {

    @Test
    void shouldParalyseDispatches() throws InterruptedException, ExecutionException {
        var SIZE = 1_000_000;
        var futures = synchronizedList(new ArrayList<CompletableFuture>());
        var dispatcher = new Dispatcher();
        for (int i = 0; i < SIZE; i++) {
            Function<Params, String> lightweight = param -> "";
            var future = dispatcher.schedule(lightweight, new Params(of()));
            futures.add(future);
        }
        allOf(futures.toArray(CompletableFuture[]::new)).get();
        assertEquals(SIZE, futures.size());
    }
}
