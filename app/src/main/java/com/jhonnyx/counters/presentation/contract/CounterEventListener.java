package com.jhonnyx.counters.presentation.contract;

import com.jhonnyx.counters.domain.model.Counter;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public interface CounterEventListener {
    void onDeleteCounter(Counter item);
    void onIncrementCounter(Counter item);
    void onDecrementCounter(Counter item);
}