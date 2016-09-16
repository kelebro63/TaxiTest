package com.kelebro63.taxitest.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by Bistrov Alexey on 11.09.2016.
 */
public class VectorCar extends Car{

    private Random r;

    private double[] vectorDirection;
    private double velocity;

    private final double minValueDirection = -1;
    private final double maxValueDirection = 1;

    private final double minValueVelocity = 0.005;
    private final double maxValueVelocity = 0.02;

    public VectorCar(int id, double latitude, double longitude) {
        super(id, latitude, longitude);
        r = new Random();
    }

    public void createVectorDirection() {
        double latVectorDirection =  minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble();
        double lonVectorDirection = minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble();
        double normal = Math.sqrt(Math.pow(latVectorDirection, 2) + Math.pow(lonVectorDirection, 2));
                vectorDirection = new double[]{
                        latVectorDirection / normal,
                        lonVectorDirection / normal
        };
    }

    public void createVelocity() {
        velocity =   minValueVelocity + (maxValueVelocity - minValueVelocity) * r.nextDouble();
    }

    public double[] getVectorDirection() {
        return vectorDirection;
    }

    public double getVelocity() {
        return velocity;
    }


}
