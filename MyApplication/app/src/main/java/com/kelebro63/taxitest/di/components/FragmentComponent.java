package com.kelebro63.taxitest.di.components;

import com.kelebro63.taxitest.di.modules.FragmentModule;
import com.kelebro63.taxitest.di.qualifiers.PerFragment;

import dagger.Component;


@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {FragmentModule.class})
public interface FragmentComponent {

//    void inject(RequestCodeFragment fragment);


}
