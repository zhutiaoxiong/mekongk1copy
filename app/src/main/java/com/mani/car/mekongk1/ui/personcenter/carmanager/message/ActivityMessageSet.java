package com.mani.car.mekongk1.ui.personcenter.carmanager.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftText;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(MessageSetPresenter.class)
public class ActivityMessageSet extends BaseMvpActivity<MessageSetView,MessageSetPresenter>  implements MessageSetView{
    @BindView(R.id.voice_package)
    LeftText voicePackage;
    @BindView(R.id.message_switch)
    LeftText messageSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_set);
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
        voicePackage.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                //警报消息  语音库
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityVoiceAlertLibrary.class);
            }
        });
        messageSwitch.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityMessageSwitch.class);
            }
        });
    }

    @Override
    public void initViews() {

    }
}
