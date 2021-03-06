package com.kelebro63.taxitest.main.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.base.BaseFragment;
import com.kelebro63.taxitest.location.LocationUtil;
import com.kelebro63.taxitest.main.MainActivity;
import com.kelebro63.taxitest.main.map.animation.LatLngInterpolator;
import com.kelebro63.taxitest.main.map.animation.MarkerAnimation;
import com.kelebro63.taxitest.models.Car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;


public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IMapView, GoogleMap.OnMyLocationButtonClickListener {

    @Bind(R.id.mapContainer)
    FrameLayout mapContainer;
    @Bind(R.id.permissionErrorView)
    View permissionErrorView;
    @Inject
    MapPresenter presenter;
    private Map<Integer, Marker> markers = new HashMap<>();
    private GoogleMap googleMap;
    List<Polyline> polylines = new ArrayList<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.map_fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.map);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragmentComponent().inject(this);
        setHasOptionsMenu(true);
        presenter.setView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        markers.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.location) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LocationUtil.REQUEST_LOCATION);
                return false;
            } else {
                presenter.getCars();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    //initialization SupportMapFragment
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
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LocationUtil.REQUEST_LOCATION);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        presenter.setupMapInfo();
    }

    private void moveCameraToBounds(LatLngBounds bounds, GoogleMap googleMap) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, mapContainer.getMeasuredWidth(), mapContainer.getMeasuredHeight(),
                getResources().getDimensionPixelSize(R.dimen.map_padding));
        googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void displayCars(List<Car> cars) {

        //update and create markers
        Map<Integer, Marker> selectMarkers = new HashMap<>();
        LatLngInterpolator mLatLngInterpolator = new LatLngInterpolator.Linear();
        for (Car car : cars) {
            if (markers.containsKey(car.getId())) {
                selectMarkers.put(car.getId(), markers.get(car.getId()));
                MarkerAnimation.animateMarkerToGB(markers.get(car.getId()), car.getLatLng(), mLatLngInterpolator);
            } else {
                selectMarkers.put(car.getId(), googleMap.addMarker(new MarkerOptions().position(car.getLatLng()).title(car.toString())));
            }
        }
        markers.clear();
        markers.putAll(selectMarkers);
    }

    @Override
    public void setDisplayPermissionError(boolean enabled) {
        getActivity().runOnUiThread(() -> permissionErrorView.setVisibility(enabled ? View.VISIBLE : View.GONE));
    }

    @Override
    public void moveCamera(LatLngBounds bounds) {
        mapContainer.post(() -> moveCameraToBounds(bounds, googleMap));
    }

    @OnClick(R.id.resolvePermissionError)
    void resolvePermissionError() {
        presenter.resolvePermissionError();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationUtil.REQUEST_LOCATION) {
            if (resultCode == -1) {
                presenter.setupMapInfo();
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        ((MainActivity)getActivity()).getPermission();
        return true;
    }

    @Override
    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> permissionsList = Arrays.asList(permissions);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && permissionsList.contains(Manifest.permission.ACCESS_FINE_LOCATION) && permissionsList.contains(Manifest.permission.ACCESS_COARSE_LOCATION) ) {
            Toast.makeText(getActivity(), "Permission Allow!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }
}
