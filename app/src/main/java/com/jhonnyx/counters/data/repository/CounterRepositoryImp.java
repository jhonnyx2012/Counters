package com.jhonnyx.counters.data.repository;

import com.jhonnyx.counters.data.entity.CounterEntity;
import com.jhonnyx.counters.data.remote.CounterApi;
import com.jhonnyx.counters.data.repository.mapper.CounterEntityToCounterMapper;
import com.jhonnyx.counters.domain.model.Counter;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public class CounterRepositoryImp implements CounterRepository {
    private final CounterApi api;
    private final CounterEntityToCounterMapper mapper;

    @Inject public CounterRepositoryImp(CounterApi api, CounterEntityToCounterMapper mapper) {
       this.mapper=mapper;
        this.api=api;
    }

    @Override public Observable<List<Counter>> getCounters() {
        return mapToDomain(api.getCounters());
    }

    @Override public Observable<List<Counter>> createCounter(Counter counter) {
        return mapToDomain(api.createCounter(mapper.reverseMap(counter)));
    }

    @Override public Observable<List<Counter>> deleteCounter(Counter counter) {
        return mapToDomain(api.deleteCounter(mapper.reverseMap(counter)));
    }

    @Override public Observable<List<Counter>> incrementCounter(Counter counter) {
        return mapToDomain(api.incrementCounter(mapper.reverseMap(counter)));
    }

    @Override public Observable<List<Counter>> decrementCounter(Counter counter) {
        return mapToDomain(api.decrementCounter(mapper.reverseMap(counter)));
    }

    private Observable<List<Counter>> mapToDomain(Observable<List<CounterEntity>> observable) {
        return observable.map(new Function<List<CounterEntity>, List<Counter>>() {
            @Override
            public List<Counter> apply(List<CounterEntity> counterEntities) throws Exception {
                Collections.reverse(counterEntities);
                return mapper.map(counterEntities);
            }
        });
    }
}