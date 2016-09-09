package com.kelebro63.taxitest.base;

import android.support.annotation.Nullable;

import com.kelebro63.taxitest.error_handler.RetrofitException;
import com.kelebro63.taxitest.error_handler.RetrofitUtils;
import com.kelebro63.taxitest.models.ErrorModel;

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
        throwable.printStackTrace();
        view.setInProgress(false);

        if (RetrofitUtils.getErrorCode(throwable) == 403) {
            //handleBlockedUser(throwable);
            return;
        }

        ErrorModel errorModel = null;
        try {
            errorModel = RetrofitUtils.getError(throwable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (errorModel != null && errorModel.getMessage() != null && !errorModel.getMessage().isEmpty()) {
            view.displayError(errorModel.getMessage());
        } else {
            RetrofitException error = (RetrofitException) throwable;
            switch (error.getKind()) {

                case NETWORK:
                    view.displayError("network error"); //FIX ME
                    return;
                case HTTP:
                    view.displayError("http error");//FIX ME
                    return;
            }
        }
    }

    @Override
    public void onNext(T t) {
        view.setInProgress(false);
    }
}
