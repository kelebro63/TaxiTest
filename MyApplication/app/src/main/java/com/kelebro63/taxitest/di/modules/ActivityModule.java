package com.kelebro63.taxitest.di.modules;

import android.support.v4.app.FragmentManager;

import com.kelebro63.taxitest.base.BaseActivity;
import com.trello.rxlifecycle.ActivityEvent;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

@Module
public class ActivityModule {
    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    @Provides
    BaseActivity provideActivity() {
        return activity;
    }

    @Provides
    Observable.Transformer provideLifecycleTransformer() {
        return activity.bindUntilEvent(ActivityEvent.STOP);
    }
}
