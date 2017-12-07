package com.jhonnyx.counters.presentation.contract;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.IProgressView;
import com.jhonnyx.counters.domain.model.Counter;

import java.util.List;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public interface Counters {
    interface View extends IProgressView{
        void updateCounters(List<Counter> counters);
        void updateTotal(String totalCount);
        String getNewCounterTitle();
        void showMessage(int idString);
        void clearTextInput();
    }

    interface Presenter extends BaseViewPresenter<View>,CounterEventListener {
        void requestCounters();
        void createCounter();
    }
}
