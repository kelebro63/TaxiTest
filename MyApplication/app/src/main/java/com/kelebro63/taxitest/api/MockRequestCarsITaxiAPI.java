package com.kelebro63.taxitest.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kelebro63.taxitest.models.Car;
import com.kelebro63.taxitest.models.VectorCar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;

/**
 * Created by Bistrov Alexey on 11.09.2016.
 */
public class MockRequestCarsITaxiAPI implements ITaxiAPI {

    public static final int COUNT_CARS = 10;
    private ArrayList<Car> cars;
    private ArrayList<VectorCar> vectorCars;
    private LatLngBounds latLngBounds;
    private boolean availableVisibility = true;

    @Override
    public Observable<List<Car>> requestCars(double southwest_latitude, double southwest_longitude, double northeast_latitude, double northeast_longitude) {
        latLngBounds = new LatLngBounds(new LatLng(southwest_latitude, southwest_longitude), new LatLng(northeast_latitude, northeast_longitude));
        if (vectorCars == null) {
            createListVectorCars(southwest_latitude, southwest_longitude, northeast_latitude, northeast_longitude);
        } else {
            updateCoordinates();
        }
        createListCars(vectorCars);
        return Observable.just(cars) ;
    }

    private void updateCoordinates() {
        for (VectorCar vectorCar : vectorCars) {
            double[] vectorPath = new double[] {0, 0};
            double newLat, newLon;
            do{
                availableVisibility = true;
                double vectorDirection[] = vectorCar.getVectorDirection();
                for (int i = 0; i < vectorDirection.length ; i++) {
                    vectorPath[i] = vectorDirection[i] * vectorCar.getVelocity();
                }
                newLat = vectorCar.getLatitude() + vectorPath[0];
                newLon = vectorCar.getLongitude() + vectorPath[1];
                LatLng latLng = new LatLng(newLat, newLon);
                if (!latLngBounds.contains(latLng)) { //car out of sight
                    vectorCar.createVectorDirection();
                } else {
                    availableVisibility = false;
                }
            } while (availableVisibility);

            vectorCar.setLatitude(newLat);
            vectorCar.setLongitude(newLon);
        }

    }

    private void createListCars(ArrayList<VectorCar> vectorCars) {
        cars = new ArrayList<>();
        for (VectorCar vectorCar : vectorCars) {
            Car car = new Car(vectorCar.getId(), vectorCar.getLongitude(), vectorCar.getLatitude());
            cars.add(car);
        }
    }

    private void createListVectorCars(double southwest_latitude, double southwest_longitude, double northeast_latitude, double northeast_longitude) {
        Random r = new Random();
        vectorCars = new ArrayList<>();
        for (int i = 0; i < COUNT_CARS; i++) {
            double lat = southwest_latitude + (northeast_latitude - southwest_latitude) * r.nextDouble();
            double lon = southwest_longitude + (northeast_longitude - southwest_longitude) * r.nextDouble();
            VectorCar car = new VectorCar(i, lon, lat);
            car.createVectorDirection();
            car.createVelocity();
            vectorCars.add(car);
        }
    }
}
