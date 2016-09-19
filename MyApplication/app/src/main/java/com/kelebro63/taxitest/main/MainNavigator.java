package com.kelebro63.taxitest.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.base.BaseActivity;
import com.kelebro63.taxitest.base.BaseFragment;
import com.kelebro63.taxitest.main.map.MapFragment;

import java.util.List;

import javax.inject.Inject;


public class MainNavigator {
    private final FragmentManager fragmentManager;
    private final BaseActivity activity;

    @Inject
    public MainNavigator(FragmentManager fragmentManager, BaseActivity activity) {
        this.fragmentManager = fragmentManager;
        this.activity = activity;
    }

    //show MapFragment
    public void navigateToMap() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.container, MapFragment.newInstance()).commit();
    }

    public void sendResultToFragment(int requestCode, int resultCode, Intent data) {
        getVisibleFragment().onActivityResult(requestCode, resultCode, data);
    }

    public void sendPermissionsResultToFragment(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        ((BaseFragment) getVisibleFragment()).onPermissionResult(requestCode, permissions, grantResults);
    }


    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

//    public void goBack() {
//        fragmentManager.popBackStackImmediate();
//    }
}
