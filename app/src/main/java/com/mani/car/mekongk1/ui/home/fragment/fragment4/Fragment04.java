package com.mani.car.mekongk1.ui.home.fragment.fragment4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kulala.baseclass.BaseMvpFragment;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.R;


/**
 * Created by qq522414074 on 2017/4/26.
 * 对讲
 */
@CreatePresenter(Fragment4Presenter.class)
public class Fragment04 extends BaseMvpFragment<Fragment4View,Fragment4Presenter> {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment04, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView("");
        initEvents();
    }

    private void initView(String txt) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {

    }

}
