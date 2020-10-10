package com.mani.car.mekongk1.ui.personcenter.aboutus.publicnumber;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

@CreatePresenter(PublicNumberPresenter.class)
public class ActivityPublicNumber extends BaseMvpActivity<PublicNumberView,PublicNumberPresenter> implements PublicNumberView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_aboutus_publicnumber);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }
    @Override
    public void initEvents() {
    }

    @Override
    public void initViews() {

    }
}
