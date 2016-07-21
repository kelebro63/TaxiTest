package com.kelebro63.taxitest.main.map;

import com.google.android.gms.location.LocationSettingsResult;
import com.kelebro63.taxitest.base.BasePresenter;

import javax.inject.Inject;

import rx.Observable;


public class OrderMapPresenter extends BasePresenter<IOrderMapView> {

   // private final ILocationUtil locationUtil;
    private LocationSettingsResult lastResult;

    @Inject
    public OrderMapPresenter(Observable.Transformer transformer) {
            //, ILocationUtil locationUtil) {
        super(transformer);
       // this.locationUtil = locationUtil;
    }

//    public void setupMapInfo(@NonNull Order order) {
//        List<Address> positions = new ArrayList<>();
//        Address pointA = order.getPointA();
//        Address pointB = order.getPointB();
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        if (pointA != null) {
//            builder.include(pointA.toLatLng());
//            positions.add(pointA);
//        }
//        if (pointB != null) {
//            builder.include(pointB.toLatLng());
//            positions.add(pointB);
//        }
//        if (pointA == null && pointB != null) {
//            builder = MapUtils.computeZoomForCenter(pointB.toLatLng(), 200);
//        }
//        final LatLngBounds.Builder finalBuilder = builder;
//        subscribe(locationUtil.isRequiredPermissionsEnabled().flatMap(e -> {
//            lastResult = e;
//            if (e.getStatus().getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
//                getView().setDisplayPermissionError(false);
//                return locationUtil.requestLocation();
//            }
//            getView().setDisplayPermissionError(true);
//            return Observable.just(null);
//        }), new NetworkSubscriber<Location>(getView(), this) {
//            @Override
//            public void onNext(@Nullable Location location) {
//                super.onNext(location);
//                if (location != null) {
//                    finalBuilder.include(new LatLng(location.getLatitude(), location.getLongitude()));
//                }
//                drawRoute(order, location);
//                getView().displayMarkers(positions, finalBuilder.build());
//            }
//        });
//    }

//    private void drawRoute(@NonNull Order order, @Nullable Location currentLocation) {
//        List<LatLng> wayPoints = new ArrayList<>();
//        if (currentLocation != null) {
//            wayPoints.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
//        }
//        if (order.getPointA() != null) {
//            wayPoints.add(order.getPointA().toLatLng());
//        }
//        if (order.getPointB() != null) {
//            wayPoints.add(order.getPointB().toLatLng());
//        }
//        if (wayPoints.size() < 2)
//            return;
//        Routing routing = new Routing.Builder().waypoints(wayPoints).withListener(getView()).build();
//        routing.execute();
//    }

    public void resolvePermissionError() {
      //  locationUtil.resolveError(((OrdersListFragment) getView()).getActivity(), lastResult);
    }
}
