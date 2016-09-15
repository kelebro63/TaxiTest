package com.kelebro63.taxitest.main.map;

import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.directions.route.Routing;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.SphericalUtil;
import com.kelebro63.taxitest.api.ITaxiAPI;
import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BasePresenter;
import com.kelebro63.taxitest.base.NetworkSubscriber;
import com.kelebro63.taxitest.location.ILocationUtil;
import com.kelebro63.taxitest.models.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;


public class MapPresenter extends BasePresenter<IMapView> {

    private LocationSettingsResult lastResult;
    private BaseActivity activity;
    private final ITaxiAPI api;

    private final ILocationUtil locationUtil;

    private static final String TAG = "Location";
    public static final double AREA_ZOOM_RADIUS = 10000; //in meters

    @Inject
    public MapPresenter(Observable.Transformer transformer, ILocationUtil locationUtil, BaseActivity activity, ITaxiAPI api) {
        super(transformer);
        this.locationUtil = locationUtil;
        this.activity = activity;
        this.api = api;
    }

    public void setupMapInfo() {
        subscribe(locationUtil.isRequiredPermissionsEnabled().flatMap(e -> {
            lastResult = e;
            if (e.getStatus().getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                getView().setDisplayPermissionError(false);
                return locationUtil.requestLocation(activity);
            }
            getView().setDisplayPermissionError(true);
            return Observable.just(null);
        }), new NetworkSubscriber<Location>(getView(), this) {
            @Override
            public void onNext(@Nullable Location location) {
                super.onNext(location);
                LatLngBounds bounds =  getLatLonBounds(new LatLng(location.getLatitude(), location.getLongitude()), AREA_ZOOM_RADIUS);
                getView().moveCamera(bounds);
                getCars();
            }
        });
    }

    private LatLngBounds getLatLonBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    private Observable<LatLngBounds> getLatLonBoundsObservable(Location location, double radius) {
        LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
        return Observable.just(getLatLonBounds(center, AREA_ZOOM_RADIUS));
    }

    public Observable<Location> getLocation() {
        return locationUtil.isRequiredPermissionsEnabled().flatMap(e -> {
            lastResult = e;
            if (e.getStatus().getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                getView().setDisplayPermissionError(false);
                return locationUtil.requestLocation(activity);
            }
            getView().setDisplayPermissionError(true);
            return Observable.just(null);
        });
    }



    public void resolvePermissionError() {
        locationUtil.resolveError(((MapFragment) getView()).getActivity(), lastResult);
    }

    public void drawRoute(Marker marker) {
        Log.d(TAG, "subscribe");
        subscribe(locationUtil.isRequiredPermissionsEnabled().flatMap(e -> {
            lastResult = e;
            if (e.getStatus().getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                getView().setDisplayPermissionError(false);
                return locationUtil.requestLocation(activity);
            }
            getView().setDisplayPermissionError(true);
            return Observable.just(null);
        }), new NetworkSubscriber<Location>(getView(), this) {
            @Override
            public void onNext(@Nullable Location location) {
                super.onNext(location);
                List<LatLng> wayPoints = new ArrayList<>();

                if (location != null) {
                    wayPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                }

                wayPoints.add(marker.getPosition());
                if (wayPoints.size() < 2)
                    return;
                Routing routing = new Routing.Builder().waypoints(wayPoints).withListener(getView()).build();
                routing.execute();
            }
        });
    }

    public void getCars() {
        subscribe(Observable.interval(0, 1, TimeUnit.SECONDS).flatMap(n -> getLocation()).flatMap(location -> getLatLonBoundsObservable(location, AREA_ZOOM_RADIUS)).flatMap(bounds -> api.requestCars(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude)).repeat(), getCarsSubscriber()); //bounds
    }

    private NetworkSubscriber<List<Car>> getCarsSubscriber() {
        return new NetworkSubscriber<List<Car>>(getView(), this) {
            @Override
            public void onNext(List<Car> cars) {
                super.onNext(cars);
                getView().displayCars(cars);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
    }

}
