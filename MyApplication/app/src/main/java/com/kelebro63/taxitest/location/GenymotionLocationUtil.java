package com.kelebro63.taxitest.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kelebro63.taxitest.base.BaseActivity;

import rx.Observable;

public class GenymotionLocationUtil implements ILocationUtil {
    private Location location;

    public GenymotionLocationUtil() {
        location = new Location("");
        location.setLongitude(49.41878);
        location.setLatitude(53.51099);
    }


    @Override
    public Observable<Location> requestLocation(BaseActivity activity) {
        return Observable.just(location);
    }

    @Override
    public Observable<LocationSettingsResult> isRequiredPermissionsEnabled() {
        return Observable.just(new LocationSettingsResult(new Status(LocationSettingsStatusCodes.SUCCESS)));
    }

    @Override
    public void resolveError(Context context, LocationSettingsResult result) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
