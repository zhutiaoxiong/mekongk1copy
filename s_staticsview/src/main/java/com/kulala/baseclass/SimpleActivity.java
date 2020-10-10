package com.kulala.baseclass;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kulala.tools.utils.ActivityUtils;

/**
 * 没有mvp的Activity基类
 * */
public class SimpleActivity extends AppCompatActivity {
    private  ActivityUtils activityUtils;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
