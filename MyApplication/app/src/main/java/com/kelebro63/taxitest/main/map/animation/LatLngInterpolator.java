package com.kelebro63.taxitest.main.map.animation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bistrov Alexey on 26.07.2016.
 */
public interface  LatLngInterpolator {
    LatLng interpolate(float fraction, LatLng a, LatLng b);

    class Linear implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lng = (b.longitude - a.longitude) * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }
}
