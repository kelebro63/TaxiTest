package com.kelebro63.taxitest.models;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.util.Random;

/**
 * Created by Bistrov Alexey on 11.09.2016.
 */
public class VectorCar extends Car{

    private Random r;

    private Vector vectorDirection;
    private double speed;

    private final double minValueDirection = -1;
    private final double maxValueDirection = 1;

    private final double minValueVelocity = 0.005;
    private final double maxValueVelocity = 0.02;

    public VectorCar(int id, double latitude, double longitude) {
        super(id, latitude, longitude);
        r = new Random();
    }

//    public void createVectorDirection() {
//        double latVectorDirection =  minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble();
//        double lonVectorDirection = minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble();
//        double normal = Math.sqrt(Math.pow(latVectorDirection, 2) + Math.pow(lonVectorDirection, 2));
//                vectorDirection = new double[]{
//                        latVectorDirection / normal,
//                        lonVectorDirection / normal
//        };
//    }

    public void createVectorDirection() {
        double latVectorDirection =  minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble();
        double lonVectorDirection = minValueDirection + (maxValueDirection - minValueDirection) * r.nextDouble();
        Vector vector = new BasicVector(new double[] {latVectorDirection, lonVectorDirection});
        double norm = vector.euclideanNorm();
        vectorDirection = vector.divide(norm);
    }

    public void createSpeed() {
        speed =  minValueVelocity + (maxValueVelocity - minValueVelocity) * r.nextDouble();
    }

    public Vector getVectorDirection() {
        return vectorDirection;
    }

    public double getSpeed() {
        return speed;
    }

    public Vector getLocationVector() {
        return new BasicVector(new double[] {getLatitude(), getLongitude()});
    }

}
