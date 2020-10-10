package com.mani.car.mekongk1.ui.personcenter.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftText;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ui.personcenter.help.service.ActivityPersonService;
import com.mani.car.mekongk1.ui.personcenter.help.sugest.ActivitySugest;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(HelpPresenter.class)
public class ActivityHelp extends BaseMvpActivity<HelpView,HelpPresenter> implements HelpView ,OEventObject{
    @BindView(R.id.sugest)
    LeftText sugest;
    @BindView(R.id.service)
    LeftText service;
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_help);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SUGGEST_HTTP_RESULTBACK, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.SUGGEST_HTTP_RESULTBACK, this);
    }

    @Override
    public void initEvents() {
        sugest.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityHelp.this, ActivitySugest.class);
            }
        });
        service.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityHelp.this, ActivityPersonService.class);
            }
        });
    }

    @Override
    public void initViews() {

    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.SUGGEST_HTTP_RESULTBACK)){
            boolean isResultOk= (boolean) o;
            if(isResultOk){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
                            GlobalContext.popMessage("提交成功，我们会尽快处理",getResources().getColor(R.color.normal_txt_color_cyan));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
