package com.jhonnyx.counters.data.repository.mapper;

import com.core.data.repository.mapper.Mapper;
import com.jhonnyx.counters.data.entity.CounterEntity;
import com.jhonnyx.counters.domain.model.Counter;
import javax.inject.Inject;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public class CounterEntityToCounterMapper extends Mapper<CounterEntity,Counter> {
   @Inject public CounterEntityToCounterMapper() {}

    @Override public Counter map(CounterEntity value) {
        Counter result = new Counter();
        result.count=value.count;
        result.id=value.id;
        result.title=value.title;
        return result;
    }

    @Override public CounterEntity reverseMap(Counter value) {
        CounterEntity result = new CounterEntity();
        result.count=value.count;
        result.id=value.id;
        result.title=value.title;
        return result;
    }
}