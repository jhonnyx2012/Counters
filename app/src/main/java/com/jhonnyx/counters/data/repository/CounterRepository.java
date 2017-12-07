package com.jhonnyx.counters.data.repository;

import com.jhonnyx.counters.domain.model.Counter;
import java.util.List;
import io.reactivex.Observable;

public interface CounterRepository {
    Observable<List<Counter>> getCounters();
    Observable<List<Counter>> createCounter(Counter counter);
    Observable<List<Counter>> deleteCounter(Counter counter);
    Observable<List<Counter>> incrementCounter(Counter counter);
    Observable<List<Counter>> decrementCounter(Counter counter);
}