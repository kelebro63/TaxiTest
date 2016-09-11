package com.kelebro63.taxitest.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Bistrov Alexey on 09.09.2016.
 */
public class Car implements Serializable {

    private int id;
    private double longitude;
    private double latitude;

    public Car(int id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", lat=" + latitude +
                ", lon='" + longitude + '\'' +
                '}';
    }
}
