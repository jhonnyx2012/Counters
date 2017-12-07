package com.jhonnyx.counters.domain.usecase;

import com.core.domain.usecase.UseCase;
import com.jhonnyx.counters.data.repository.CounterRepository;
import com.jhonnyx.counters.domain.model.Counter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public class CreateCounterUseCase extends UseCase<List<Counter>> {
    private final CounterRepository repository;
    private Counter data;

    @Inject public CreateCounterUseCase(CounterRepository repository) {
        this.repository=repository;
    }

    public CreateCounterUseCase setData(Counter data) {
        this.data = data;
        return this;
    }

    @Override protected Observable<List<Counter>> createObservableUseCase() {
        return repository.createCounter(data);
    }
}