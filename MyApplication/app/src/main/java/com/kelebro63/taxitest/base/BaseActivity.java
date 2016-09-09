package com.kelebro63.taxitest.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.inputmethod.InputMethodManager;

import com.kelebro63.taxitest.App;
import com.kelebro63.taxitest.di.components.ActivityComponent;
import com.kelebro63.taxitest.di.components.DaggerActivityComponent;
import com.kelebro63.taxitest.di.modules.ActivityModule;
import com.kelebro63.taxitest.error_handler.ErrorDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


public abstract class BaseActivity extends RxAppCompatActivity implements IView, DialogInterface.OnCancelListener {




    protected ActivityComponent createActivityComponent() {
        return DaggerActivityComponent.builder().appComponent(App.getAppComponent(this)).activityModule(new ActivityModule(this)).build();
    }


    @Override
    protected void onStop() {
        super.onStop();
        setInProgress(false);
    }

    @Override
    public void displayError(@StringRes int stringRes) {
    }

    @Override
    public void displayError(String error) {
        ErrorDialog.show(this, error);
    }

    @Override
    public void displaySimpleError(String error) {
    }

    @Override
    public void setInProgress(boolean inProgress) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideSoftKeyboard();
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
