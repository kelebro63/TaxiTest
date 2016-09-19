package com.kelebro63.taxitest.main.map;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.kelebro63.taxitest.api.ITaxiAPI;
import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BasePresenter;
import com.kelebro63.taxitest.base.NetworkSubscriber;
import com.kelebro63.taxitest.location.ILocationUtil;
import com.kelebro63.taxitest.models.Car;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;


public class MapPresenter extends BasePresenter<IMapView> {

    public static final double AREA_ZOOM_RADIUS = 10000; //in meters

    private LocationSettingsResult lastResult;
    private BaseActivity activity;
    private final ITaxiAPI api;
    private final ILocationUtil locationUtil;


    @Inject
    public MapPresenter(Observable.Transformer transformer, ILocationUtil locationUtil, BaseActivity activity, ITaxiAPI api) {
        super(transformer);
        this.locationUtil = locationUtil;
        this.activity = activity;
        this.api = api;
    }


    //determines current location, moves camera, get cars
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

    public Observable<Location> requestLocation() {
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


    public void getCars() {
        subscribe(Observable.interval(0, 1, TimeUnit.SECONDS).flatMap(n -> requestLocation()).repeat(), requestLocationSubscriber());
    }

    private NetworkSubscriber<List<Car>> requestCarsSubscriber() {
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

    private NetworkSubscriber<Location> requestLocationSubscriber() {
        return new NetworkSubscriber<Location>(getView(), this) {
            @Override
            public void onNext(Location location) {
                super.onNext(location);
                LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
                LatLngBounds bounds = getLatLonBounds(center, AREA_ZOOM_RADIUS);
                subscribe((api.requestCars(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude)), requestCarsSubscriber());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
    }

}
