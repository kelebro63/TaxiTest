package com.kelebro63.taxitest.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsResult;

import rx.Observable;

public interface ILocationUtil extends LocationListener {
    Observable<Location> requestLocation();

    Observable<LocationSettingsResult> isRequiredPermissionsEnabled();

    void resolveError(Context context, LocationSettingsResult result);
}
