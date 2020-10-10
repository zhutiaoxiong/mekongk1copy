package com.mani.car.mekongk1.ui.personcenter.personinformation.changeusername;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(PersonChangeUserNamePresenter.class)
public class ActivityPersonChangeUserName extends BaseMvpActivity<ChangeUserNameView,PersonChangeUserNamePresenter> implements ChangeUserNameView ,OEventObject{
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @BindView(R.id.user_name)
    EditText user_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        ButterKnife.bind(this);
        ODispatcher.addEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
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
        titile.txt_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                String ttt = user_name.getText().toString();
                if(ttt.length()>0){
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
                    user.name = ttt;
                    OCtrlRegLogin.getInstance().CCMD_1110_changeUserInfo(user.toJsonObject());
                }else{
                    GlobalContext.popMessage("未输入信息",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
                }
            }
        });
    }

    @Override
    public void initViews() {
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if(user==null)return;
        if(user.name!=null&&!user.name.equals("")){
            user_name.setText(user.name);
        }
        user_name.setFocusable(true);
        user_name.setFocusableInTouchMode(true);
        user_name.requestFocus();
        //开启软键盘
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(user_name, 0);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CHANGE_USER_INFO_OK)){
            int a=(Integer) o;
            if(a==1){
                handleClosePage();
            }
        }
    }
    private final ActivityPersonChangeUserName.MyHandler handler=new ActivityPersonChangeUserName.MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<ActivityPersonChangeUserName> mActivity;
        public MyHandler(ActivityPersonChangeUserName activity) {
            mActivity = new WeakReference<ActivityPersonChangeUserName>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivityPersonChangeUserName activityPersonChangeUserName=mActivity.get();
            if(activityPersonChangeUserName!=null){
                if(msg.what==110){
                    activityPersonChangeUserName.finish();
                }
            }
        }
    }

    /**设置提醒文字*/
    private void handleClosePage(){
        Message message=Message.obtain();
        message.what=110;
        handler.sendMessage(message);
    }

}
