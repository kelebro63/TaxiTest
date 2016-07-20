package com.kelebro63.taxitest;

import android.app.Application;
import android.content.Context;

import com.kelebro63.taxitest.di.components.AppComponent;
import com.kelebro63.taxitest.di.components.DaggerAppComponent;
import com.kelebro63.taxitest.di.modules.AppModule;

public class App extends Application {

    private AppComponent appComponent;

    public static AppComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
         if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        }
    }
}
