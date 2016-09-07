package com.kelebro63.taxitest.main;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BasePresenter;
import com.kelebro63.taxitest.base.IView;
import com.kelebro63.taxitest.main.map.MapFragment;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import rx.Observable;

public class MainPresenter extends BasePresenter<IView> {

    private final MainNavigator navigator;
    private static final String TAG = "RxPermissions";
    private final BaseActivity activity;


    @Inject
    public MainPresenter(Observable.Transformer transformer, MainNavigator navigator, BaseActivity activity) {
        super(transformer);
        this.navigator = navigator;
        this.activity = activity;
    }

    public void determineScreenToShow() {
        if (navigator.getVisibleFragment() != null && navigator.getVisibleFragment() instanceof MapFragment) {
            return;
        }
        this.navigator.navigateToMap();
    }

    public void sendResult(int requestCode, int resultCode, Intent data) {
        this.navigator.sendResultToFragment(requestCode, resultCode, data);
    }

    public void sendPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        this.navigator.sendPermissionsResultToFragment(requestCode, permissions, grantResults);
    }

    public void getPermission() {
        RxPermissions mRxPermissions = RxPermissions.getInstance(activity);
        mRxPermissions.setLogging(true);
        Observable<Object> observable = createObserver();
        mRxPermissions
                .request(observable, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                            Log.i(TAG, "Received result " + granted);
                            if (!granted) {
                                Toast.makeText(activity,
                                        "Permission denied, can't enable the location",
                                        Toast.LENGTH_SHORT).show();
                            }
                        },
                        t -> Log.e(TAG, "onError", t),
                        () -> Log.i(TAG, "OnComplete")
                );
    }

    public Observable<Object> createObserver() {
        return Observable.create(observer -> observer.onNext(new Object()));
    }

}
