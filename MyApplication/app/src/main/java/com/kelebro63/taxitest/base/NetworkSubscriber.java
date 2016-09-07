package com.kelebro63.taxitest.base;

import android.support.annotation.Nullable;

import rx.Subscriber;


public class NetworkSubscriber<T> extends Subscriber<T> {
    protected final IView view;

    @Nullable
    protected final BasePresenter presenter;

    public NetworkSubscriber(IView view, @Nullable BasePresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        view.setInProgress(true);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onNext(T t) {
        view.setInProgress(false);
    }
}
