package com.kelebro63.taxitest.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kelebro63.taxitest.models.Car;
import com.kelebro63.taxitest.models.VectorCar;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

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
            createListVectorCars();
        } else {
            updateCoordinates();
        }
        createListCars(vectorCars);
        return Observable.just(cars) ;
    }

    private void updateCoordinates() {
        for (VectorCar vectorCar : vectorCars) {
            vectorCar.createSpeed(); //change speed
            Vector newLocation = new BasicVector();

            do{
                availableVisibility = true;
                Vector vectorDirection = vectorCar.getVectorDirection();
                Vector vectorPath = vectorDirection.multiply(vectorCar.getSpeed());
                newLocation = vectorCar.getLocationVector().add(vectorPath);
                LatLng latLng = new LatLng(newLocation.get(0), newLocation.get(1));

                //if car out of sight, rotate vectorDirection
                if (!latLngBounds.contains(latLng)) {
                    vectorCar.setLatLng(generateNewLocation(new Random(), latLngBounds));
                    vectorCar.rotateVectorDirection();
                } else {
                    availableVisibility = false;
                }

            } while (availableVisibility);

            vectorCar.setLatitude(newLocation.get(0));
            vectorCar.setLongitude(newLocation.get(1));
        }
    }

    private void createListCars(ArrayList<VectorCar> vectorCars) {
        cars = new ArrayList<>();
        for (VectorCar vectorCar : vectorCars) {
            Car car = new Car(vectorCar.getId(), vectorCar.getLatitude(), vectorCar.getLongitude());
            cars.add(car);
        }
    }

    private void createListVectorCars() {
        Random r = new Random();
        vectorCars = new ArrayList<>();
        for (int i = 0; i < COUNT_CARS; i++) {
            LatLng locationCoordinates = generateNewLocation(r, latLngBounds);
            VectorCar car = new VectorCar(i, locationCoordinates.latitude, locationCoordinates.longitude);
            car.createVectorDirection();
            car.createSpeed();
            vectorCars.add(car);
        }
    }

    //generate new LatLng within this range
    private LatLng generateNewLocation(Random r, LatLngBounds latLngBounds) {
        double lat = latLngBounds.southwest.latitude + (latLngBounds.northeast.latitude - latLngBounds.southwest.latitude) * r.nextDouble();
        double lon = latLngBounds.southwest.longitude + (latLngBounds.northeast.longitude - latLngBounds.southwest.longitude) * r.nextDouble();
        return new LatLng(lat, lon);
    }
}
