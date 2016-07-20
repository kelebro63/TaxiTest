package com.kelebro63.taxitest.di.modules;

import com.kelebro63.taxitest.App;

import dagger.Module;

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
//    @Singleton
//    @Provides
//    ILocationUtil provideLocationUtil() {
//        if (BuildConfig.FLAVOR.equals("genymotion"))
//            return new GenymotionLocationUtil();
//        return new LocationUtil(app);
//    }


}
