package com.example.formulabasic;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CircuitDataBaseService {
    @GET("f1/circuits.json?limit=80")
    Observable<ErgastResponse> listCircuits();

    @GET("f1/circuits/{circuitId}.json")
    Observable<ErgastResponse> getCircuitDetails(@Path("circuitId") String circuitId);
}

