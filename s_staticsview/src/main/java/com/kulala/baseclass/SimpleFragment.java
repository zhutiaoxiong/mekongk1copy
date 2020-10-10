package com.kulala.baseclass;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 没有mvp的Fragment基类
 * */
public class SimpleFragment extends android.support.v4.app.Fragment {
//    private ActivityUtils activityUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activityUtils = new ActivityUtils(this);
    }
}
