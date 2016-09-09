package com.kelebro63.taxitest.api;

import com.kelebro63.taxitest.models.Car;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Bistrov Alexey on 09.09.2016.
 */
public interface ITaxiAPI {

    @GET("/cars")
    Observable<List<Car>> requestCars();

}
