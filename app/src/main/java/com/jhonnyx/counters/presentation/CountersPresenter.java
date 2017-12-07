package com.jhonnyx.counters.presentation;

import com.core.domain.usecase.UseCaseObserver;
import com.jhonnyx.counters.R;
import com.jhonnyx.counters.domain.model.Counter;
import com.jhonnyx.counters.domain.usecase.CreateCounterUseCase;
import com.jhonnyx.counters.domain.usecase.DecrementCounterUseCase;
import com.jhonnyx.counters.domain.usecase.DeleteCounterUseCase;
import com.jhonnyx.counters.domain.usecase.GetCountersUseCase;
import com.jhonnyx.counters.domain.usecase.IncrementCounterUseCase;
import com.jhonnyx.counters.presentation.contract.Counters;
import java.util.List;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public class CountersPresenter implements Counters.Presenter {

    private GetCountersUseCase getCountersUseCase;
    private DeleteCounterUseCase deleteCounterUseCase;
    private CreateCounterUseCase createCounterUseCase;
    private IncrementCounterUseCase incrementCounterUseCase;
    private DecrementCounterUseCase decrementCounterUseCase;
    private Counters.View view;

    public CountersPresenter(GetCountersUseCase getCountersUseCase, DeleteCounterUseCase deleteCounterUseCase, CreateCounterUseCase createCounterUseCase, IncrementCounterUseCase incrementCounterUseCase, DecrementCounterUseCase decrementCounterUseCase) {
        this.getCountersUseCase = getCountersUseCase;
        this.deleteCounterUseCase = deleteCounterUseCase;
        this.createCounterUseCase = createCounterUseCase;
        this.incrementCounterUseCase = incrementCounterUseCase;
        this.decrementCounterUseCase = decrementCounterUseCase;
    }

    @Override public void initialize(Counters.View view) {
        this.view=view;
        requestCounters();
    }

    @Override public void requestCounters() {
        getCountersUseCase.execute(getCallback());
    }

    @Override public void createCounter() {
        Counter counter=new Counter();
        counter.title=view.getNewCounterTitle();
        view.clearTextInput();
        if(!counter.title.isEmpty())
            createCounterUseCase.setData(counter).execute(getCallback());
        else view.showMessage(R.string.enter_new_name);
    }

    private String getTotalCount(List<Counter> counters) {
        int result = 0;
        for (Counter counter : counters) {result+=counter.count;}
        return String.valueOf(result);
    }

    @Override public void onDeleteCounter(Counter item) {
        deleteCounterUseCase.setData(item).execute(getCallback());
    }

    @Override public void onIncrementCounter(Counter item) {
        incrementCounterUseCase.setData(item).execute(getCallback());
    }

    @Override public void onDecrementCounter(Counter item) {
        decrementCounterUseCase.setData(item).execute(getCallback());
    }

    private UseCaseObserver<List<Counter>> getCallback() {
        view.showProgress(true);
        return new UseCaseObserver<List<Counter>>() {
            @Override public void onNext(List<Counter> value) {
                view.showProgress(false);
                view.updateCounters(value);
                view.updateTotal(getTotalCount(value));
            }

            @Override public void onError(Throwable e) {
                view.showProgress(false);
                view.showMessage(R.string.there_has_been_a_problem);
                e.printStackTrace();
            }
        };
    }
}
