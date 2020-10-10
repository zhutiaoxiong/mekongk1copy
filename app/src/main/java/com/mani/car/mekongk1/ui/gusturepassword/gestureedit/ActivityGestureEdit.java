package com.mani.car.mekongk1.ui.gusturepassword.gestureedit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsview.image.CircleImg;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlGesture;
import com.mani.car.mekongk1.model.ManagerGesture;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.gusturepassword.guestureview.GestureContentView;
import com.mani.car.mekongk1.ui.gusturepassword.guestureview.GestureDrawline;
import com.mani.car.mekongk1.ui.home.ActivityHome;

import butterknife.BindView;
import butterknife.ButterKnife;


@CreatePresenter(GustureEditPresenter.class)
public class ActivityGestureEdit extends BaseMvpActivity<GestureEditView, GustureEditPresenter> implements GestureEditView, OEventObject {
    private ClipTitleHead title_head;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    @BindView(R.id.user_logo)
    CircleImg mImgUserLogo;
    @BindView(R.id.text_phone_number)
    TextView mTextPhoneNumber;
    private static String INPUT_CODE = "";
    public static boolean isForResetGesture = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_gesture_edit);
        ButterKnife.bind(this);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        isForResetGesture = false;
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SETUP_GESTURE_RESULTBACK, this);
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
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null) return;
        if (user.avatarUrl.length() > 10 && !user.phoneNum.equals("")) {
            Glide.with(this).load(user.avatarUrl)
                    .placeholder(R.drawable.push)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mImgUserLogo.setImageDrawable(resource);
                        }
                    });
        }
        if(user.phoneNum!=null&&!user.phoneNum.equals("")){
            mTextPhoneNumber.setText(user.phoneNum);
        }
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(ActivityGestureEdit.this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#B03125'>" + "至少连接四个点" + "</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextTip.setClickable(true);
                    mTextTip.setText("再输一次手势密码");
                } else {
                    if (inputCode.equals(mFirstPassword)) {
//                        GlobalContext.popMessage("设置成功",getResources().getColor(R.color.normal_txt_color_cyan));
                        mGestureContentView.clearDrawlineState(0L);
                        OCtrlGesture.getInstance().CCMD_1311_setupGesture(1, INPUT_CODE);
                        ManagerGesture.getInstance().saveGesture("开启", MD5.MD5generator("kulala_sign_" + INPUT_CODE));
                        /**跳转到安全页面*/
                    } else {
                        mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>" + "两次输入不一致请重新输入" + "</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(ActivityGestureEdit.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.SETUP_GESTURE_RESULTBACK)) {
            /**跳转到安全页面*/
            if (ManagerPublicData.fromPage.equals("已经进系统")) {
                finish();
            } else if (ManagerPublicData.fromPage.equals("没有进系统")) {
                ActivityUtils.startActivity(ActivityGestureEdit.this, ActivityHome.class);
            }
        }
    }

    private void updateCodeList(String inputCode) {
        INPUT_CODE = codeCutOne(inputCode);
    }

    private String codeCutOne(String inputCode) {
        if (inputCode == null || inputCode.equals("")) return "";
        String result = "";
        for (int i = 0; i < inputCode.length(); i++) {
            String cut = inputCode.substring(i, i + 1);
            int value = Integer.parseInt(cut);
            result += String.valueOf(value - 1);
        }
        return result;
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }
}
