package com.kelebro63.taxitest.main.map;

import android.location.Address;

import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kelebro63.taxitest.base.IView;

import java.util.List;


public interface IOrderMapView extends IView, RoutingListener {
    void displayMarkers(List<Address> addresses, LatLngBounds bounds);
    void setDisplayPermissionError(boolean enabled);
}
