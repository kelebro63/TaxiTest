package com.kelebro63.taxitest.providers.cars;

import com.google.android.gms.maps.model.LatLngBounds;
import com.kelebro63.taxitest.models.Car;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Bistrov Alexey on 09.09.2016.
 */
public class MockDataAdapter implements IDataAdapter {

    public static final int COUNT_CARS = 10;

    @Override
    public ArrayList<Car> getCars(LatLngBounds bounds) {
        return generateCarsList(bounds);
    }

    public ArrayList<Car> generateCarsList(LatLngBounds bounds) {
        ArrayList<Car> cars = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < COUNT_CARS; i++) {
            double lat = bounds.southwest.latitude + (bounds.northeast.latitude - bounds.southwest.latitude) * r.nextDouble();
            double lon = bounds.southwest.longitude + (bounds.northeast.longitude - bounds.southwest.longitude) * r.nextDouble();
            Car car = new Car(i, lon, lat);
            cars.add(car);
        }
        return cars;
    }
}
