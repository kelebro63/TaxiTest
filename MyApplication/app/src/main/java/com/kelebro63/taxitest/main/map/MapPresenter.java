package com.kelebro63.taxitest.main.map;

import com.google.android.gms.location.LocationSettingsResult;
import com.kelebro63.taxitest.base.BasePresenter;

import javax.inject.Inject;

import rx.Observable;


public class MapPresenter extends BasePresenter<IMapView> {

    // private final ILocationUtil locationUtil;
    private LocationSettingsResult lastResult;

    @Inject
    public MapPresenter(Observable.Transformer transformer) {
        //, ILocationUtil locationUtil) {
        super(transformer);
        // this.locationUtil = locationUtil;
    }

    public void setupMapInfo() {
//        List<Address> positions = new ArrayList<>();
//        Address pointA = Address.
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
//        getView().displayMarkers(positions, finalBuilder.build());

    }

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
