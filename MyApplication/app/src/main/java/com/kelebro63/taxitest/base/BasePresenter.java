package com.kelebro63.taxitest.base;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public abstract class BasePresenter<T extends IView> {
    private final Observable.Transformer transformer;
    private T view;
    private boolean initialized;

    public BasePresenter(Observable.Transformer transformer) {
        this.transformer = transformer;
    }

    @NonNull
    public T getView() {
        if (view == null)
            throw new IllegalStateException("View is not attached to presenter");
        return view;
    }



    public void setView(@NonNull T view) {
        this.view = view;
    }

    protected <S> void subscribe(Observable<S> original, NetworkSubscriber<S> subscriber) {
        original.compose(transformer).subscribeOn(getBackgroundThreadScheduler()).observeOn(getMainThreadScheduler()).subscribe(subscriber);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler getBackgroundThreadScheduler() {
        return Schedulers.io();
    }
}
