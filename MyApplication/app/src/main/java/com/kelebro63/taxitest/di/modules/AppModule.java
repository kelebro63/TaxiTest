package com.kelebro63.taxitest.di.modules;

import com.kelebro63.taxitest.App;
import com.kelebro63.taxitest.location.ILocationUtil;
import com.kelebro63.taxitest.location.LocationUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

//    @Singleton
//    @Provides
//    DistanceCounter distanceCounter(ILocationUtil locationUtil) {
//        return new DistanceCounter(locationUtil);
//    }
//
//
    @Singleton
    @Provides
    ILocationUtil provideLocationUtil() {
        return new LocationUtil(app);
    }


}
