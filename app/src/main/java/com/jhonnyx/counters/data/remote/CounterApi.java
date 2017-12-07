package com.jhonnyx.counters.data.remote;

import com.jhonnyx.counters.data.entity.CounterEntity;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public interface CounterApi {
    String API_VERSION = "v1";
    String BASE_URL = "https://cornershop-test-elements.c9users.io/api/"+API_VERSION+"/";
    String GET_COUNTERS_ENDPOINT = "counters";
    String INC_COUNTER_ENDPOINT = "counter/inc";
    String DEC_COUNTER_ENDPOINT = "counter/dec";
    String CREATE_COUNTER_ENDPOINT = "counter";
    String DELETE_COUNTER_ENDPOINT = "counter";

    @GET(GET_COUNTERS_ENDPOINT)
    Observable<List<CounterEntity>> getCounters();

    @POST(CREATE_COUNTER_ENDPOINT)
    Observable<List<CounterEntity>> createCounter(@Body CounterEntity counter);

    @HTTP(method = "DELETE", path = DELETE_COUNTER_ENDPOINT, hasBody = true)
    Observable<List<CounterEntity>> deleteCounter(@Body CounterEntity counter);

    @POST(INC_COUNTER_ENDPOINT)
    Observable<List<CounterEntity>> incrementCounter(@Body CounterEntity counter);

    @POST(DEC_COUNTER_ENDPOINT)
    Observable<List<CounterEntity>> decrementCounter(@Body CounterEntity counter);
}