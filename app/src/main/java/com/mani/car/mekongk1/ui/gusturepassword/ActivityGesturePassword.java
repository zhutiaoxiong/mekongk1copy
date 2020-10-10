package com.mani.car.mekongk1.ui.gusturepassword;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.JsonObject;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.BaseMvpMethod;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.image.CircleImg;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.RxCoundDown;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerGesture;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.gusturepassword.gestureedit.ActivityGestureEdit;
import com.mani.car.mekongk1.ui.gusturepassword.guestureview.GestureContentView;
import com.mani.car.mekongk1.ui.gusturepassword.guestureview.GestureDrawline;
import com.mani.car.mekongk1.ui.login.ActivityLogin;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mani.car.mekongk1.common.GlobalContext.getContext;

/**
 * 手势密码页面
 */

public class ActivityGesturePassword extends BaseMvpActivity<GusturePasswordView, GesturePasswordPresenter> implements BaseMvpMethod, OEventObject, OCallBack {
    @BindView(R.id.title_head)
    ClipTitleHead title_head;
    @BindView(R.id.user_logo)
    CircleImg mImgUserLogo;
    @BindView(R.id.text_phone_number)
    TextView mTextPhoneNumber;
    @BindView(R.id.text_tip)
    TextView mTextTip;
    @BindView(R.id.gesture_container)
    FrameLayout mGestureContainer;
    @BindView(R.id.text_forget_gesture)
    TextView mTextForget;
    @BindView(R.id.text_other_account)
    TextView mTextOther;
    @BindView(R.id.check_pass_layout)
    RelativeLayout checkPassLayout;
    @BindView(R.id.edit_login_password)
    EditText edit_login_password;
    @BindView(R.id.txt_cancle)
    TextView txt_cancle;
    @BindView(R.id.txt_confirm)
    TextView txt_confirm;
    @BindView(R.id.out_view)
    View out_view;
    private static String INPUT_CODE = "";
    GestureContentView mGestureContentView;
    private int errorCounts = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesturepassword);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
    }

    /**
     * 返回键处理
     */
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
//        Log.e("观看手势密码", "onResume: ActivityGesturePassword ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
    }

    @Override
    public void initEvents() {
        out_view.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
            }
        });
        mTextForget.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                checkPassLayout.setVisibility(View.VISIBLE);
                edit_login_password.setText("");
                edit_login_password.setFocusable(true);
                edit_login_password.setFocusableInTouchMode(true);
                edit_login_password.requestFocus();
                //开启软键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edit_login_password, 0);
            }
        });
        mTextOther.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityGesturePassword.this, ActivityLogin.class);
            }
        });
        txt_cancle.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                closeKeyBoard();
                checkPassLayout.setVisibility(View.INVISIBLE);
            }
        });
        txt_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String password = edit_login_password.getText().toString();
                OCtrlRegLogin.getInstance().CCMD_1104_checkPassword(password);
            }
        });
    }

    private void closeKeyBoard() {
        //关闭软键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txt_confirm.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void initViews() {
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null) return;
        if (user.avatarUrl.length() > 10) {
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
        mGestureContentView = new GestureContentView(getContext(), false, "",
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                        INPUT_CODE = codeCutOne(inputCode);
                        String md5Code = MD5.MD5generator("kulala_sign_" + INPUT_CODE);
                        String localCode = ManagerGesture.getInstance().getSignPasswordGesture();
                        if (md5Code.equals(localCode)) {//密码正确
                            mGestureContentView.clearDrawlineState(0L);
//                            GlobalContext.popMessage("手势密码正确", getResources().getColor(R.color.normal_txt_color_cyan));
                            mTextTip.setText("请输入手势密码");
                            errorCounts = 0;
//                            if(ManagerPublicData.isFromActivityFlash){
//                                ActivityUtils.startActivity(ActivityGesturePassword.this, ActivityHome.class);
//                            }else{
//                                finish();
//                                return;
//                            }
                            finish();
                            return;
                        } else {
                            errorCounts++;
                            mGestureContentView.clearDrawlineState(1300L);
//                            mTextTip.setVisibility(View.VISIBLE);
                            mTextTip.setText(Html.fromHtml("<font color='#B03125'>" + "密码错误" + "</font>"));
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                            mTextTip.startAnimation(shakeAnimation);
                        }
                        if (errorCounts >= 3) {
                            out_view.setVisibility(View.VISIBLE);
                            mTextTip.setText(Html.fromHtml("<font color='#B03125'>" + "输入错误请10秒后再试" + "</font>"));
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                            mTextTip.startAnimation(shakeAnimation);
                            RxCoundDown.getInstance().startTime(10);
                            RxCoundDown.getInstance().setOnTimeCompleteListner(new RxCoundDown.OnTimeCompleteListner() {
                                @Override
                                public void onStart() {
//                                    Log.e("手势密码", "不能点击");
                                }

                                @Override
                                public void Complete() {
                                    Log.e("手势密码", "可以点击");
                                    errorCounts = 0;
                                    RxCoundDown.getInstance().closeTimer();
                                    out_view.setVisibility(View.INVISIBLE);
                                    mTextTip.setText("请输入手势密码");
                                }

                                @Override
                                public void OnCount(Long value) {
                                    Log.e("手势密码", "" + value);
                                    mTextTip.setText(Html.fromHtml("<font color='#B03125'>" + "输入错误请"+value+"秒后再试" + "</font>"));
                                }
                            });

                        }
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
        Log.e("手势密码", "initViews:   show");
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


    @Override
    public void callback(String key, Object value) {
        if (key.equals("forgetGesturePass")) {
            JsonObject obj = (JsonObject) value;
            String pass = OJsonGet.getString(obj, OToastInput.PASS);
            OCtrlRegLogin.getInstance().CCMD_1104_checkPassword(pass);
        }
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {//忘记手势密码
            boolean check = (Boolean) paramObj;
            if (check) {
                handleSetEditPassWordInVisible();
            } else {
                handlePassWordError();
            }
        }
    }


    private String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }

    private final ActivityGesturePassword.MyHandler handler = new ActivityGesturePassword.MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<ActivityGesturePassword> mActivity;

        public MyHandler(ActivityGesturePassword activity) {
            mActivity = new WeakReference<ActivityGesturePassword>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivityGesturePassword activityRigister = mActivity.get();
            if (activityRigister != null) {
                if (msg.what == 110) {
                    activityRigister.edit_login_password.setText("");
                    GlobalContext.popMessage("密码错误", activityRigister.getResources().getColor(R.color.normal_bg_color_tip_red));
                } else if (msg.what == 111) {
                    activityRigister.closeKeyBoard();
                    activityRigister.checkPassLayout.setVisibility(View.INVISIBLE);
                    ManagerPublicData.fromPage = "没有进系统";
//                    GlobalContext.popMessage("密码正确", activityRigister.getResources().getColor(R.color.normal_txt_color_cyan));
                    ActivityUtils.startActivity(activityRigister, ActivityGestureEdit.class);
                }
            }
        }
    }

    /**
     * 设置提醒文字
     */
    private void handlePassWordError() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendMessage(message);
    }

    /**
     * 设置提醒文字
     */
    private void handleSetEditPassWordInVisible() {
        Message message = Message.obtain();
        message.what = 111;
        handler.sendMessage(message);
    }

    public static void enableView(View view, boolean enable) {
        if (view == null) return;
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                child.setEnabled(enable);
                enableView(child, enable);
            }
        }
    }
}
