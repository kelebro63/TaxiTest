package com.kelebro63.taxitest.main.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.directions.route.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;


public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IMapView {

    @Bind(R.id.mapContainer)
    FrameLayout mapContainer;
    @Bind(R.id.permissionErrorView)
    View permissionErrorView;
    @Inject
    MapPresenter presenter;
    private Map<Marker, Address> markers = new HashMap<>();
    private GoogleMap googleMap;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.map_fragment;
    }

    @Override
    protected String getTitle() {
        return "map";//getString(R.string.orders_delivery_address);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createFragmentComponent().inject(this);
        setHasOptionsMenu(true);
        presenter.setView(this);
        initMap();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.location) {
//            showMyLocation();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
        return false;
    }

    private void showMyLocation() {
        if (googleMap != null) {
            Location location = googleMap.getMyLocation();
            if (location != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.mapContainer, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        presenter.setupMapInfo();
    }

    private void moveCameraToBounds(LatLngBounds bounds, GoogleMap googleMap) {
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, mapContainer.getMeasuredWidth(), mapContainer.getMeasuredHeight(),
//                getResources().getDimensionPixelSize(R.dimen.orders_map_padding));
//        googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
       // bottomPanel.show(markers.get(marker));
        return false;
    }

    @Override
    public void onRoutingFailure() {
        setInProgress(false);
    }

    @Override
    public void onRoutingStart() {
        setInProgress(true);
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
//        setInProgress(false);
//        if (!isAdded() || routes.size() == 0)
//            return;
//        Route route = routes.get(shortestRouteIndex);
//        PolylineOptions polyOptions = new PolylineOptions();
//        polyOptions.color(Color.BLUE);
//        polyOptions.width(getResources().getDimensionPixelSize(R.dimen.polyline_width));
//        polyOptions.addAll(route.getPoints());
//        googleMap.addPolyline(polyOptions);
    }

    @Override
    public void onRoutingCancelled() {
        setInProgress(false);
    }

    @Override
    public void displayMarkers(List<Address> addresses, LatLngBounds bounds) {
        for (Address address : addresses) {
            markers.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude()))), address);
        }
        mapContainer.post(() -> moveCameraToBounds(bounds, googleMap));
    }


    @Override
    public void setDisplayPermissionError(boolean enabled) {
        getActivity().runOnUiThread(() -> permissionErrorView.setVisibility(enabled ? View.VISIBLE : View.GONE));
    }

    @OnClick(R.id.resolvePermissionError)
    void resolvePermissionError() {
        presenter.resolvePermissionError();
    }

    private Location getMyLocation() { //FIX ME to LocationServices.FusedLocationApi.getLastLocation
//        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return null;
//        }
//        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        if (myLocation == null) {
//            Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//            String provider = lm.getBestProvider(criteria, true);
//            myLocation = lm.getLastKnownLocation(provider);
//        }
//
//        return myLocation;
        return null;
    }
}
