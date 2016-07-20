package com.kelebro63.taxitest.main;

import android.os.Bundle;

import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.base.BaseToolbarActivity;

public class MainActivity  extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
