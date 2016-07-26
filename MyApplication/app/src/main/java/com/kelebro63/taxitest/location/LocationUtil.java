package com.kelebro63.taxitest.location;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kelebro63.taxitest.App;
import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.error_handler.GoogleApiException;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public final class LocationUtil implements ILocationUtil {
    private final App context;
    private final GoogleApiClient client;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;

    public LocationUtil(App context) {
        this.context = context;
        client = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        locationRequest = LocationRequest.create();
        locationRequest.setNumUpdates(1);
        locationSettingsRequest = new LocationSettingsRequest.Builder().setAlwaysShow(true).addLocationRequest(locationRequest).build();
    }

    public Observable<Location> requestLocation() {
        return connectToApi().flatMap(e -> requestLocationInternal());
    }

    private Observable<Location> requestLocationInternal() {
        return Observable.defer(() -> Observable.range(0, Integer.MAX_VALUE)).delay(1, TimeUnit.SECONDS)
                .concatMap(e -> {
                    LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this, Looper.getMainLooper());
                    return Observable.just(LocationServices.FusedLocationApi.getLastLocation(client));
                }).takeFirst(e -> e != null).switchIfEmpty(Observable.error(new IllegalStateException(context.getString(R.string.dialogs_location_cannot_be_determined))));
    }

    @Override
    public Observable<LocationSettingsResult> isRequiredPermissionsEnabled() {
        return connectToApi().flatMap(e -> Observable.defer(() -> Observable.just(LocationServices.SettingsApi.checkLocationSettings(client, locationSettingsRequest).await())));
    }

    @Override
    public void resolveError(Context context, LocationSettingsResult result) {
        switch (result.getStatus().getStatusCode()) {

            case LocationSettingsStatusCodes.SERVICE_DISABLED:
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, LocationSettingsStatusCodes.SERVICE_DISABLED, 0);
                dialog.setCancelable(false);
                dialog.show();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            case LocationSettingsStatusCodes.SIGN_IN_REQUIRED:
                try {
                    result.getStatus().startResolutionForResult((Activity) context, 0);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            default:
                Log.w("PermissionError", String.format("Unexpected status code %d", result.getStatus().getStatusCode()));
                new AlertDialog.Builder(context)
                        .setTitle(R.string.dialog_error)
                        .setMessage(context.getString(R.string.dialogs_location_cannot_be_determined, result.getStatus().toString()))
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialogs_ok, (errorDialog, which) -> {
                            ((Activity) context).finish();
                        }).show();
                break;
        }
    }

    private Observable<ConnectionResult> connectToApi() {
        if (client.isConnected()) {
            return Observable.just(null);
        }
        return Observable.defer(() -> Observable.just(client.blockingConnect())).flatMap(e -> {
            if (!e.isSuccess()) {
                return Observable.error(new GoogleApiException(e.getErrorMessage()));
            }
            return Observable.just(null);
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
