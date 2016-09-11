package com.kelebro63.taxitest.api;

import com.kelebro63.taxitest.models.Car;

import java.util.List;

import rx.Observable;

/**
 * Created by Bistrov Alexey on 11.09.2016.
 */
public class MockRequestCarsITaxiAPIError implements ITaxiAPI {

    @Override
    public Observable<List<Car>> requestCars(double southwest_latitude, double southwest_longitude, double northeast_latitude, double northeast_longitude) {
        return null;
    }
}
