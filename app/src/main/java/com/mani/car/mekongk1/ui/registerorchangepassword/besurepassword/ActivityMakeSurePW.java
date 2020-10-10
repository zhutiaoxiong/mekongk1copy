package com.mani.car.mekongk1.ui.registerorchangepassword.besurepassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.ChildLeftEditRightImg;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.ui.home.ActivityHome;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**主页*/
@CreatePresenter(MakeSurePWPresenter.class)
public class ActivityMakeSurePW extends BaseMvpActivity<MakeSurePWView,MakeSurePWPresenter>  implements MakeSurePWView {
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @BindView(R.id.input_password)
    ChildLeftEditRightImg input_password;
    @BindView(R.id.input_password_again)
    ChildLeftEditRightImg input_password_again;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.txt_tips)
    TextView txt_tips;
    private String conditon;
    private String phoneNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent!=null){
            conditon=  intent.getStringExtra("condition");
            phoneNum=  intent.getStringExtra("phoneNum");
        }
        setContentView(R.layout.activity_makesure_pw);
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
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                String password1 = input_password.txt_left.getText().toString();
                String password2 = input_password_again.txt_left.getText().toString();
                if (!OInputValidation.chkInputPassword(password1)) {
                    input_password.txt_left.setText("");
                    return;
                }
                if (!OInputValidation.chkRepWords(password1, password2)) {
                    GlobalContext.popMessage("密码输入不一致，请重新输入", getResources().getColor(R.color.popTipWarning));
                    input_password_again.txt_left.setText("");
                    return;
                }
                getMvpPresenter().registerOrChangePassword(phoneNum,password1, ManagerPublicData.verfyStr,conditon,ManagerPublicData.verfyCode);
            }
        });
    }

    @Override
    public void initViews() {
        if(conditon.equals("注册")){
            titile.txt_title.setText("注册");
            txt_tips.setText("注册");
        }else{
            titile.txt_title.setText("重置密码");
            txt_tips.setText("重置密码");
        }
        input_password_again.txt_left.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        input_password.txt_left.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
    }

    @Override
    public void toActivityHome() {
        handleToActivityHome();

    }

    @Override
    public void showResult(String result) {
        handleShowResult(result);
    }

    private final ActivityMakeSurePW.MyHandler handler=new ActivityMakeSurePW.MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<ActivityMakeSurePW> mActivity;
        public MyHandler(ActivityMakeSurePW activity) {
            mActivity = new WeakReference<ActivityMakeSurePW>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivityMakeSurePW activityMakeSurePW=mActivity.get();
            if(activityMakeSurePW!=null){
                if(msg.what==110){
                    ActivityUtils.startActivity(activityMakeSurePW, ActivityHome.class);
                    activityMakeSurePW.finish();
                }else if(msg.what==111){
                   if(activityMakeSurePW.conditon.equals("注册")){
                       String result= (String) msg.obj;
                       if(result.equals("")){
//                           GlobalContext.popMessage("注册成功", activityMakeSurePW.getResources().getColor(R.color.normal_txt_color_cyan));
                       }else{
                           GlobalContext.popMessage(result, activityMakeSurePW.getResources().getColor(R.color.normal_bg_color_tip_red));
                       }
                   }else if(activityMakeSurePW.conditon.equals("忘记密码")){
                       String result= (String) msg.obj;
                       if(result.equals("")){
//                           GlobalContext.popMessage("密码修改成功", activityMakeSurePW.getResources().getColor(R.color.normal_txt_color_cyan));
                       }else{
                           GlobalContext.popMessage(result, activityMakeSurePW.getResources().getColor(R.color.normal_bg_color_tip_red));
                       }
                   }
                }
            }
        }
    }
    private void handleToActivityHome(){
        Message message=Message.obtain();
        message.what=110;
        handler.sendMessage(message);
    }
    private void handleShowResult(String result){
        Message message=Message.obtain();
        message.what=111;
        message.obj=result;
        handler.sendMessage(message);
    }
}
