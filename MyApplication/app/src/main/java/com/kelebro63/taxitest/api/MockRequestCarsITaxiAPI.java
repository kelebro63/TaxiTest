package com.kelebro63.taxitest.api;

import com.kelebro63.taxitest.models.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;

/**
 * Created by Bistrov Alexey on 11.09.2016.
 */
public class MockRequestCarsITaxiAPI implements ITaxiAPI {

    public static final int COUNT_CARS = 10;

    @Override
    public Observable<List<Car>> requestCars(double southwest_latitude, double southwest_longitude, double northeast_latitude, double northeast_longitude) {
            ArrayList<Car> cars = new ArrayList<>();
            Random r = new Random();
            for (int i = 0; i < COUNT_CARS; i++) {
                double lat = southwest_latitude + (northeast_latitude - southwest_latitude) * r.nextDouble();
                double lon = southwest_longitude + (northeast_longitude - southwest_longitude) * r.nextDouble();
                Car car = new Car(i, lon, lat);
                cars.add(car);
            }
            return Observable.just(cars) ;
    }
}
