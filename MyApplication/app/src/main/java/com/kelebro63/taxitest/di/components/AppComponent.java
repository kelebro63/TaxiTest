package com.kelebro63.taxitest.di.components;

import com.kelebro63.taxitest.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

//    ILocationUtil locationUtil();
//
//    DistanceCounter distanceCounter();

//    void inject(GcmService service);
//
//    void inject(InstanceIDListenerService service);
//
//    void inject(OrderNormalViewHolder orderNormalViewHolder);
}