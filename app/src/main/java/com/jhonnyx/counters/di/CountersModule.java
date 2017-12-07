package com.jhonnyx.counters.di;

import com.core.data.remote.ApiServiceFactory;
import com.jhonnyx.counters.data.remote.CounterApi;
import com.jhonnyx.counters.data.repository.CounterRepository;
import com.jhonnyx.counters.data.repository.CounterRepositoryImp;
import com.jhonnyx.counters.data.repository.mapper.CounterEntityToCounterMapper;
import com.jhonnyx.counters.domain.usecase.CreateCounterUseCase;
import com.jhonnyx.counters.domain.usecase.DecrementCounterUseCase;
import com.jhonnyx.counters.domain.usecase.DeleteCounterUseCase;
import com.jhonnyx.counters.domain.usecase.GetCountersUseCase;
import com.jhonnyx.counters.domain.usecase.IncrementCounterUseCase;
import com.jhonnyx.counters.presentation.contract.Counters;
import com.jhonnyx.counters.presentation.CountersPresenter;
import com.jhonnyx.counters.presentation.adapter.CounterAdapter;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

@Module
public class CountersModule {
    @Provides Counters.Presenter providesPresenter(GetCountersUseCase getCountersUseCase,DeleteCounterUseCase deleteCounterUseCase, CreateCounterUseCase createCounterUseCase, IncrementCounterUseCase incrementCounterUseCase, DecrementCounterUseCase decrementCounterUseCase){
        return new CountersPresenter(getCountersUseCase, deleteCounterUseCase, createCounterUseCase, incrementCounterUseCase, decrementCounterUseCase);
    }

    @Provides CounterAdapter providesAdaoter(){return new CounterAdapter();}

    @Provides CounterRepository providesRepository(CounterApi api, CounterEntityToCounterMapper mapper){
        return new CounterRepositoryImp(api,mapper);
    }

    @Provides CounterApi providesApiService(){
        return ApiServiceFactory.build(new OkHttpClient(),CounterApi.class,CounterApi.BASE_URL);
    }
}