package com.kelebro63.taxitest.di.components;

import com.kelebro63.taxitest.di.modules.AppModule;
import com.kelebro63.taxitest.location.ILocationUtil;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    ILocationUtil locationUtil();
}
