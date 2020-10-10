package com.mani.car.mekongk1.ui.personcenter.carmanager.carmodule;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.ui.home.ActivityHome;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(CarMoudulePresenter.class)
public class ActivityCaUnActiveSuccess  extends BaseMvpActivity<CarMouduleView,CarMoudulePresenter>implements CarMouduleView {
    @BindView(R.id.moudle_name)
    TextView moudleName;
    @BindView(R.id.title)
    ClipTitleHead titleHead;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_unactive);
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }


    @Override
    public void initEvents() {
        moudleName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cmb = (ClipboardManager)
                        GlobalContext.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(ManagerPublicData.terminalNum.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可		        cm.getText();//获取粘贴信息
                return false;
            }
        });
        titleHead.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(ActivityCaUnActiveSuccess.this, ActivityHome.class);
            }
        });
    }

    @Override
    public void initViews() {
        if(ManagerPublicData.terminalNum!=null&&!ManagerPublicData.terminalNum.equals("")){
            moudleName.setText("模组号为"+ManagerPublicData.terminalNum);
        }
        moudleName.setTextIsSelectable(true);
    }
}
