package com.example.formulabasic;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverDataBaseService {
    @GET("f1/drivers.json")
    Observable<ErgastDriverResponse> listDrivers(@Query("limit") int limit, @Query("offset") int offset);

    @GET("f1/drivers/{driverId}.json")
    Observable<ErgastDriverResponse> getDriverDetails(@Path("driverId") String driverId);
}