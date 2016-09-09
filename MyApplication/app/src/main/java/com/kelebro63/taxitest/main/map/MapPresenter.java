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
import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BasePresenter;
import com.kelebro63.taxitest.base.NetworkSubscriber;
import com.kelebro63.taxitest.location.ILocationUtil;
import com.kelebro63.taxitest.providers.cars.IDataAdapter;
import com.kelebro63.taxitest.providers.cars.MockDataAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;


public class MapPresenter extends BasePresenter<IMapView> {

    private LocationSettingsResult lastResult;
    private Subscription subscription;
    private BaseActivity activity;
    private IDataAdapter dataAdapter;

    private final ILocationUtil locationUtil;

    private static final String TAG = "Location";
    public static final double AREA_ZOOM_RADIUS = 10000; //in meters

    @Inject
    public MapPresenter(Observable.Transformer transformer, ILocationUtil locationUtil, BaseActivity activity) {
        super(transformer);
        this.locationUtil = locationUtil;
        this.activity = activity;
        dataAdapter = new MockDataAdapter();
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
                LatLngBounds bounds =  toBounds(new LatLng(location.getLatitude(), location.getLongitude()), AREA_ZOOM_RADIUS);
                getView().displayCars(dataAdapter.getCars(bounds), bounds);
            }
        });
    }

    private LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    public void moveMarkers() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        subscription = subscribeWithResult(Observable
                        .interval(1, TimeUnit.SECONDS)
                        .map(i -> list.get(i.intValue()))
                        .take(list.size())
                , getSubscriber());
    }

    private NetworkSubscriber<Integer> getSubscriber() {
        return new NetworkSubscriber<Integer>(getView(), this) {
            @Override
            public void onNext(Integer o) {
                super.onNext(o);
                Log.d(TAG, "onNext = " + o);
                getView().moveCars();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        };
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

}
