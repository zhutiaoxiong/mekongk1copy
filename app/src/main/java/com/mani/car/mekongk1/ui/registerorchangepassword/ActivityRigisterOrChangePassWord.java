package com.mani.car.mekongk1.ui.registerorchangepassword;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.OTime60;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.registerorchangepassword.besurepassword.ActivityMakeSurePW;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMent;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.tools.utils.ActivityUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**主页*/
@CreatePresenter(RigisterOrChangePassWordPresenter.class)
public class ActivityRigisterOrChangePassWord extends BaseMvpActivity<RigisterOrChangePassWordView,RigisterOrChangePassWordPresenter>  implements RigisterOrChangePassWordView {
    @BindView(R.id.title)
    ClipTitleHead title;
    @BindView(R.id.txt_get_ver_code)
    Button txt_get_ver_code;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_ver_code)
    EditText edit_ver_code;
    @BindView(R.id.txt_tips)
    TextView txt_tips;
    @BindView(R.id.img_click_agree)
    ImageView img_click_agree;
    @BindView(R.id.txt_agreement)
    TextView txt_agreement;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.txt_phone)
    TextView txt_phone;
    @BindView(R.id.layout_agreement)
    LinearLayout layout_agreement;
    private boolean isClickAgreeImg;
    private String checkedPhoneNum;
    private String checkedVerfyCode;
    private String conditon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent!=null){
            conditon=  intent.getStringExtra("condition");
        }
        setContentView(R.layout.activity_rigister);
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
    public void onDetachedFromWindow() {
        OTime60.getInstance().clearButton();
        super.onDetachedFromWindow();
    }

    @Override
    public void initEvents() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtnisVisible();
            }
        };
        edit_phone.addTextChangedListener(watcher);
        edit_ver_code.addTextChangedListener(watcher);
        txt_get_ver_code.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
               String result=checkPhoneNumResult();
               if(conditon.equals("更改账号旧手机号")){
                   getMvpPresenter().getVerfyCode(ManagerLoginReg.getInstance().getCurrentUser().phoneNum,2);
               }else{
                   if(result.equals("")){
                       if(conditon.equals("注册")){
                           getMvpPresenter().getVerfyCode(checkedPhoneNum,1);
                       }else if(conditon.equals("忘记密码")){
                           getMvpPresenter().getVerfyCode(checkedPhoneNum,3);
                       }else if(conditon.equals("更改账号新手机号")){
                           getMvpPresenter().getVerfyCode(checkedPhoneNum,6);
                       }
                   }
               }

            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(conditon.equals("注册")){
                    if (!isClickAgreeImg) {
                        Toast.makeText(ActivityRigisterOrChangePassWord.this,"请先同意用户使用协议",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String result=checkPhoneNumResult();
                String result1=checkVerfyCode();
                if(result.equals("")&&result1.equals("")){
                    if(conditon.equals("注册")){
                        getMvpPresenter().verify(checkedPhoneNum,checkedVerfyCode,1,conditon);
                    }else if(conditon.equals("忘记密码")){
                        getMvpPresenter().verify(checkedPhoneNum,checkedVerfyCode,3,conditon);
                    }else if(conditon.equals("更改账号旧手机号")){
                        DataUser user=ManagerLoginReg.getInstance().getCurrentUser();
                        if(user!=null){
                            getMvpPresenter().verify(user.phoneNum,checkedVerfyCode,2,conditon);
                        }
                    }else if(conditon.equals("更改账号新手机号")){
                        getMvpPresenter().changePhoneNum(checkedPhoneNum,checkedVerfyCode,ManagerPublicData.verfyStr);
                    }
                }
            }
        });
        txt_agreement.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityRigisterOrChangePassWord.this,ActivityUserAgreeMent.class);
            }
        });
        img_click_agree.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                getMvpPresenter().clickAgreeImg(isClickAgreeImg);
                //判断下一步按钮是否可见
                checkBtnisVisible();
            }
        });
    }

    @Override
    public void initViews() {
        //下划线
        if(conditon.equals("注册")){
            layout_agreement.setVisibility(View.VISIBLE);
            txt_agreement.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
            title.txt_title.setText("注册");
            txt_tips.setText("注册");
        }else if(conditon.equals("忘记密码")){
            edit_phone.setHint("请输入您的手机号");
            layout_agreement.setVisibility(View.INVISIBLE);
            title.txt_title.setText("重置密码");
            txt_tips.setText("重置密码");
        }else if(conditon.equals("更改账号旧手机号")){
            DataUser user= ManagerLoginReg.getInstance().getCurrentUser();
            if(user==null)return;
//            edit_phone.setText(user.phoneNum);
//            edit_phone.setHint("请输入您的手机号");
            edit_phone.setVisibility(View.INVISIBLE);
            txt_phone.setVisibility(View.VISIBLE);
            txt_phone.setText(user.phoneNum);
            layout_agreement.setVisibility(View.INVISIBLE);
            title.txt_title.setText("更改账号");
            txt_tips.setText("更改账号");
        }
        OTime60.getInstance().listener(txt_get_ver_code);
    }

    @Override
    public void imgClickAgreeOk() {
        isClickAgreeImg=true;
        img_click_agree.setImageResource(R.drawable.img_click_yes);
    }

    @Override
    public void imgClickAgreeCancle() {
        isClickAgreeImg=false;
        img_click_agree.setImageResource(R.drawable.img_click_no);
    }

    @Override
    public void txtGetVerfyCodeCountDown(String txt) {
        txt_get_ver_code.setText(txt);
        txt_get_ver_code.setEnabled(true);
        txt_get_ver_code.setBackgroundColor(getResources().getColor(R.color.normal_txt_color_gray));
    }

    @Override
    public void txtGetVerfyComPlete(String txt) {
        txt_get_ver_code.setText(txt);
        txt_get_ver_code.setEnabled(false);
        txt_get_ver_code.setBackgroundColor(getResources().getColor(R.color.normal_txt_color_cyan));
    }

    @Override
    public void toMakeSurePassWordPage() {
        if(conditon.equals("注册")||conditon.equals("忘记密码")){
            ActivityUtils.startActivityTakeData(ActivityRigisterOrChangePassWord.this,ActivityMakeSurePW.class,conditon,checkedPhoneNum);
            finish();
        }
//        else if(conditon.equals("更改账号旧手机号")){
//            conditon="更改账号新手机号";
//            edit_phone.setHint("请输入您的新手机号");
//        }
        else if(conditon.equals("更改账号新手机号")){
            finish();
        }
    }

    @Override
    public void clearEditText() {
        edit_ver_code.setHint("");
    }

    @Override
    public void setTipsText(String cacheError) {
        handleSetTipText(cacheError);
    }

    @Override
    public void changUI() {
        handleChangeUI();
    }

    /**
     * 检查用户名
     **/
    private String checkPhoneNumResult() {
        if(!conditon.equals("更改账号旧手机号")){
            checkedPhoneNum = edit_phone.getText().toString();
            if (!OInputValidation.chkInputPhoneNum(checkedPhoneNum)||checkedPhoneNum.length()!=11){
                GlobalContext.popMessage("请输入正确的手机号码",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
                return "请输入正确的手机号码";
            }
        }else{
            return "";
        }

        return "";
    }

    /**
     * 检查密码
     **/
    private String checkVerfyCode() {
        checkedVerfyCode = edit_ver_code.getText().toString();
        if (!(checkedVerfyCode.length()==6)) {
            Toast.makeText(ActivityRigisterOrChangePassWord.this,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
            return "请输入正确的验证码";
        }
        return "";
    }
    /**
     * 检查下一步按钮是否可见
     **/
    private void checkBtnisVisible() {
        checkedPhoneNum = edit_phone.getText().toString();
        checkedVerfyCode = edit_ver_code.getText().toString();
        btn_confirm.setEnabled(false);
        btn_confirm.setVisibility(View.INVISIBLE);
        if(conditon.equals("更改账号旧手机号")){
            if(checkedVerfyCode.length()==6){
                btn_confirm.setEnabled(true);
                btn_confirm.setVisibility(View.VISIBLE);
            }
        }
        if (checkedPhoneNum.length()==11&&checkedVerfyCode.length()==6) {
            if(conditon.equals("注册")){
                if(isClickAgreeImg){
                    btn_confirm.setEnabled(true);
                    btn_confirm.setVisibility(View.VISIBLE);
                }
            }else{
                btn_confirm.setEnabled(true);
                btn_confirm.setVisibility(View.VISIBLE);
            }
        }
    }
    private final MyHandler handler=new MyHandler(this);
    private static class MyHandler extends Handler{
        private final WeakReference<ActivityRigisterOrChangePassWord> mActivity;
        public MyHandler(ActivityRigisterOrChangePassWord activity) {
            mActivity = new WeakReference<ActivityRigisterOrChangePassWord>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
           final ActivityRigisterOrChangePassWord activityRigister=mActivity.get();
            if(activityRigister!=null){
                if(msg.what==110){
                    String cacheStr= (String) msg.obj;
                    if(cacheStr.equals("验证成功")||cacheStr.equals("修改成功")){
//                        GlobalContext.popMessage(cacheStr,GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                    }else{
                        GlobalContext.popMessage(cacheStr,GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
                    }
                }else if(msg.what==111){
                        activityRigister.conditon="更改账号新手机号";
                        activityRigister.edit_phone.setHint("请输入您的新手机号");
                        activityRigister.edit_phone.setVisibility(View.VISIBLE);
                        activityRigister. txt_phone.setVisibility(View.INVISIBLE);
                        activityRigister.layout_agreement.setVisibility(View.INVISIBLE);
                        activityRigister. title.txt_title.setText("更改账号");
                        activityRigister.txt_tips.setText("更改账号");
                        activityRigister.edit_ver_code.setText("");
                        activityRigister.edit_ver_code.setHint("请输入您的验证码");
                    }
                }
            }
    }

    /**设置提醒文字*/
    private void handleSetTipText(String cacheError){
        Message message=Message.obtain();
        message.what=110;
        message.obj=cacheError;
        handler.sendMessage(message);
    }
    /**设置提醒文字*/
    private void handleChangeUI(){
        Message message=Message.obtain();
        message.what=111;
        handler.sendMessage(message);
    }
}
