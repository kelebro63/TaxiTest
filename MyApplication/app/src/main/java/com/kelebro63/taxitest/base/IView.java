package com.kelebro63.taxitest.base;

import android.content.Context;
import android.support.annotation.StringRes;


public interface IView {
    void setInProgress(boolean inProgress);

    void displayError(String error);

    void displaySimpleError(String error);

    void displayError(@StringRes int stringRes);

    String provideProgressTitle();

    Context getContext();
}
