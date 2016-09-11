package com.kelebro63.taxitest.main.map;

import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kelebro63.taxitest.base.IView;
import com.kelebro63.taxitest.models.Car;

import java.util.List;


public interface IMapView extends IView, RoutingListener {
    void displayCars(List<Car> cars);
    void moveCars();
    void setDisplayPermissionError(boolean enabled);
    void moveCamera(LatLngBounds bounds);
}
