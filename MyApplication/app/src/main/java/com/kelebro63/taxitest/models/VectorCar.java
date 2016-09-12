package com.kelebro63.taxitest.models;

import java.util.Random;

/**
 * Created by Bistrov Alexey on 11.09.2016.
 */
public class VectorCar extends Car{

    private Random r;

    private double[] vectorDirection;
    private double velocity;

    private double minValueDirection = -1;
    private double maxValueDirection = 1;

    private double minValueVelocity = 0.01;
    private double maxValueVelocity = 0.03;

    public VectorCar(int id, double longitude, double latitude) {
        super(id, longitude, latitude);
        r = new Random();
    }

    public void createVectorDirection() {
        vectorDirection = new double[]{
                minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble(),
                minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble()
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
