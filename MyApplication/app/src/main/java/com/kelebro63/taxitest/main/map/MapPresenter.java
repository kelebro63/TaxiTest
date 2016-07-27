package com.kelebro63.taxitest.main.map;

import android.location.Address;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.directions.route.Routing;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.kelebro63.taxitest.base.BasePresenter;
import com.kelebro63.taxitest.base.NetworkSubscriber;
import com.kelebro63.taxitest.location.ILocationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;


public class MapPresenter extends BasePresenter<IMapView> {

    private final ILocationUtil locationUtil;
    private LocationSettingsResult lastResult;
    private static final String TAG = "Location";

    @Inject
    public MapPresenter(Observable.Transformer transformer, ILocationUtil locationUtil) {
        super(transformer);
        this.locationUtil = locationUtil;
    }

    public void setupMapInfo() {
        List<Address> positions = new ArrayList<>();
        Address pointA = new Address(Locale.ENGLISH);
        pointA.setLatitude(55.88548);
        pointA.setLongitude(37.605);
        Address pointB =  new Address(Locale.ENGLISH);
        pointB.setLatitude(56.88548);
        pointB.setLongitude(38.605);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (pointA != null) {
            new LatLng(pointA.getLatitude(), pointA.getLongitude());
            positions.add(pointA);
        }
        if (pointB != null) {
            builder.include(new LatLng(pointB.getLatitude(), pointB.getLongitude()));
            positions.add(pointB);
        }
//        if (pointA == null && pointB != null) {
//            builder = MapUtils.computeZoomForCenter(pointB.toLatLng(), 200);
//        }
        final LatLngBounds.Builder finalBuilder = builder;

        subscribe(locationUtil.isRequiredPermissionsEnabled().flatMap(e -> {
            lastResult = e;
            if (e.getStatus().getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                getView().setDisplayPermissionError(false);
                return locationUtil.requestLocation();
            }
            getView().setDisplayPermissionError(true);
            return Observable.just(null);
        }), new NetworkSubscriber<Location>(getView(), this) {
            @Override
            public void onNext(@Nullable Location location) {
                super.onNext(location);
                if (location != null) {
                    finalBuilder.include(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                getView().displayMarkers(positions, finalBuilder.build());
            }
        });
    }

    public void moveMarkers() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        subscribe(Observable
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
                getView().moveMarkers();
            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
    }

    public void resolvePermissionError() {
        //  locationUtil.resolveError(((OrdersListFragment) getView()).getActivity(), lastResult);
    }

    public void drawRoute(Marker marker) {
        Log.d(TAG, "subscribe");
        subscribe(locationUtil.isRequiredPermissionsEnabled().flatMap(e -> {
            lastResult = e;
            if (e.getStatus().getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                getView().setDisplayPermissionError(false);
                return locationUtil.requestLocation();
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
