package com.kelebro63.taxitest.main;

import android.content.Intent;
import android.os.Bundle;

import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.base.BaseToolbarActivity;
import com.kelebro63.taxitest.location.LocationUtil;

import javax.inject.Inject;

public class MainActivity  extends BaseToolbarActivity {

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.determineScreenToShow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationUtil.REQUEST_LOCATION) {
            presenter.sendResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
