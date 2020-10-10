package com.mani.car.mekongk1.ui.useragreement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

import butterknife.BindView;
import butterknife.ButterKnife;

/**主页*/
@CreatePresenter(UserAgreeMentPresenter.class)
public class ActivityUserAgreeMent extends BaseMvpActivity<UserAgreeMentView,UserAgreeMentPresenter>  implements UserAgreeMentView {
    @BindView(R.id.txt_info)
    TextView txt_info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useragreement);
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
        txt_info.setText(Html.fromHtml((getResources().getString(R.string.me_licence))));
    }
}
