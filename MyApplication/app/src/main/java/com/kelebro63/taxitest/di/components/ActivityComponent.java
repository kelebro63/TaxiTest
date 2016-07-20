package com.kelebro63.taxitest.di.components;

import com.kelebro63.taxitest.di.modules.ActivityModule;
import com.kelebro63.taxitest.di.qualifiers.PerActivity;
import com.kelebro63.taxitest.main.MainActivity;

import dagger.Component;


@PerActivity
@Component(dependencies = {AppComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(MainActivity activity);

}
