package com.kelebro63.taxitest.main;

import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BasePresenter;
import com.kelebro63.taxitest.base.IView;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class MainPresenter extends BasePresenter<IView> {

    private final MainNavigator navigator;
    private RxPermissions mRxPermissions;
    private static final String TAG = "RxPermissions";
    private Observable<Object> observable;
    private final BaseActivity activity;


    @Inject
    public MainPresenter(Observable.Transformer transformer, MainNavigator navigator, BaseActivity activity) {
        super(transformer);
        this.navigator = navigator;
        this.activity = activity;
    }

    public void determineScreenToShow() {
        this.navigator.navigateToMap();
    }

    public void getPermission() {
        mRxPermissions = RxPermissions.getInstance(activity);
        mRxPermissions.setLogging(true);
        observable = createObserver();
        mRxPermissions
                .request(observable, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                            Log.i(TAG, "Received result " + granted);
                            if (granted) {
                            } else {
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
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> observer) {
                observer.onNext(new Object());
            }
        } );
    }

}
