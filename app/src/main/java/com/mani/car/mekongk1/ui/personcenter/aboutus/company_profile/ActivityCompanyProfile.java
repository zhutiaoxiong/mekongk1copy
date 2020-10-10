package com.mani.car.mekongk1.ui.personcenter.aboutus.company_profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsfunc.static_system.OConver;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(CompanyProfilePresenter.class)
public class ActivityCompanyProfile extends BaseMvpActivity<CompanyProfileView, CompanyProfilePresenter> implements CompanyProfileView {
    @BindView(R.id.txt_info)
    TextView txt_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_aboutus_company_profile);
        ButterKnife.bind(this);
        initViews();
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
        txt_info.setMovementMethod(new ScrollingMovementMethod());
        txt_info.setText( OConver.StrToDBC(getResources().getString(R.string.me_about)));
    }
}

