package com.kelebro63.taxitest.di.modules;

import android.support.v4.app.FragmentManager;

import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BaseFragment;
import com.trello.rxlifecycle.FragmentEvent;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

@Module
public class FragmentModule {
    private final BaseFragment fragment;

    public FragmentModule(BaseFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    BaseActivity provideActivity() {
        return ((BaseActivity) fragment.getActivity());
    }

    @Provides
    FragmentManager provideFragmentManager(BaseActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    Observable.Transformer provideTransformer() {
        return fragment.bindUntilEvent(FragmentEvent.STOP);
    }

    @Provides
    BaseFragment provideFragment() {
        return fragment;
    }
}
