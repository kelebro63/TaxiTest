package com.kelebro63.taxitest.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kelebro63.taxitest.App;
import com.kelebro63.taxitest.di.components.DaggerFragmentComponent;
import com.kelebro63.taxitest.di.components.FragmentComponent;
import com.kelebro63.taxitest.di.modules.FragmentModule;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;

public abstract class BaseFragment extends RxFragment implements IView {

    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (getView() instanceof ViewGroup) {
            ((ViewGroup) getView()).removeAllViews();
        }
    }

    protected FragmentComponent createFragmentComponent() {
        return DaggerFragmentComponent.builder().appComponent(App.getAppComponent(getActivity())).fragmentModule(new FragmentModule(this)).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseToolbarActivity) {
            getSupportActionBar().setTitle(getTitle());
            getSupportActionBar().setSubtitle(getSubtitle());
        }
    }

    @Override
    public void setInProgress(boolean inProgress) {

    }

    @Override
    public void onStop() {
        super.onStop();
        setInProgress(false);
    }

    private ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    protected abstract String getTitle();

    @Override
    public void displayError(String error) {
        ((BaseActivity) getActivity()).displayError(error);
    }

    @Override
    public void displaySimpleError(String error) {
        ((BaseActivity) getActivity()).displaySimpleError(error);
    }

    @Override
    public void displayError(@StringRes int stringRes) {
        ((BaseActivity) getActivity()).displayError(stringRes);
    }


    @Nullable
    protected String getSubtitle() {
        return null;
    }

    @Override
    public String provideProgressTitle() {
        return "";//getString(R.string.dialogs_please_wait);
    }

}
