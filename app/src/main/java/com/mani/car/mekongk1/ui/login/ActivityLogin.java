package com.mani.car.mekongk1.ui.login;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.ClipPopLoading;
import com.kulala.staticsview.style.LeftImg_CenterEdit_RightImg;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.home.ActivityHome;
import com.mani.car.mekongk1.ui.registerorchangepassword.ActivityRigisterOrChangePassWord;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMent;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMentPravate;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
@CreatePresenter(LoginPresenter.class)
public class ActivityLogin extends BaseMvpActivity<LoginView,LoginPresenter>implements LoginView{
    @BindView(R.id.input_username)      LeftImg_CenterEdit_RightImg input_username;
    @BindView(R.id.input_password)
    LeftImg_CenterEdit_RightImg input_password;
    @BindView(R.id.btn_confirm)         TextView btn_confirm;
    @BindView(R.id.txt_register)        TextView txt_register;
    @BindView(R.id.txt_forget_password) TextView txt_forget_password;
    @BindView(R.id.txt_agreement)       TextView txt_agreement;
    @BindView(R.id.yinsizhengce)       TextView yinsizhengce;
    @BindView(R.id.img_click_agree)     ImageView img_click_agree;
    @BindView(R.id.recycleview_users)   RecyclerView recycleview_users;
    @BindView(R.id.titile)              ClipTitleHead titile;
    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;
    @BindView(R.id.layoutContent)              RelativeLayout layoutContent;
    private boolean isClickAgreeImg=true;
    private loginRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isShowUserList=false;
    private String checkedUsername;
    private String checkedPassword;
    private int bottomHeight;
    private KeyboardHelper keyboardHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        titile.my_progress.setTestInfo("登录");
        keyboardHelper = new KeyboardHelper(this);
        keyboardHelper.onCreate();
        keyboardHelper.setOnKeyboardStatusChangeListener(onKeyBoardStatusChangeListener);
        layoutBottom.post(new Runnable() {
            @Override
            public void run() {
                bottomHeight = layoutBottom.getHeight();
            }
        });
        initViews();
        initEvents();
    }
    private void closeKeyBoard() {
        //关闭软键盘
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(input_username.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private KeyboardHelper.OnKeyboardStatusChangeListener onKeyBoardStatusChangeListener = new KeyboardHelper.OnKeyboardStatusChangeListener() {

        @Override
        public void onKeyboardPop(int keyboardHeight) {

            final int height = keyboardHeight;
            if (bottomHeight >height) {
                layoutBottom.setVisibility(View.GONE);
            } else {
                int offset = bottomHeight - height;
                final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutContent
                        .getLayoutParams();
                lp.topMargin =offset;
                layoutContent.setLayoutParams(lp);
            }

        }

        @Override
        public void onKeyboardClose(int keyboardHeight) {
            if (View.VISIBLE != layoutBottom.getVisibility()) {
                layoutBottom.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutBottom.setVisibility(View.VISIBLE);
                    }
                }, 300);
            }
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutContent
                    .getLayoutParams();
            if (lp.topMargin != 0) {
                lp.topMargin = 0;
                layoutContent.setLayoutParams(lp);
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardHelper.onDestroy();
    }

    @Override
    public void showLoading() {
        /**显示进度条*/
//     ClipPopLoading.getInstance().show(btn_confirm);
    }

    @Override
    public void hideLoading() {
//        handleHide();
    }

    @Override
    public void showLoginResult(String result) {
        handleShowLoginResult(result);
    }


    @Override
    public void recycleViewAndCenterImgShow(final List<DataUser> userHistory) {
        isShowUserList=true;
        recycleview_users.setVisibility(View.VISIBLE);
        input_username.img_right.setSelected(true);
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new loginRecycleViewAdapter(userHistory,ActivityLogin.this);
            recycleview_users.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.setData(userHistory);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new loginRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ManagerLoginReg.getInstance().clearOneUserHistory(userHistory.get(position).phoneNum);
//                userHistory.remove(position);

                myRecycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUserNameSelect(View view, int position) {
                recycleview_users.setVisibility(View.INVISIBLE);
                isShowUserList=false;
                input_username.img_right.setSelected(false);
                input_username.txt_left.setText(userHistory.get(position).phoneNum);

            }
        });
    }

    @Override
    public void recycleViewAndCenterImgHide() {
        isShowUserList=false;
        recycleview_users.setVisibility(View.INVISIBLE);
        input_username.img_right.setSelected(false);
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
    public void clearUserName() {
        input_username.txt_left.setText("");
    }

    @Override
    public void clearPassWord() {
        input_password.txt_left.setText("");
    }

    @Override
    public void toHomePage() {
        handleToHomePage();
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

                String phoneNum = input_username.txt_left.getText().toString();
                String password = input_password.txt_left.getText().toString();
                if (password.length() >= 1) {
                    input_password.img_right.setVisibility(View.VISIBLE);
                } else {
                    input_password.img_right.setVisibility(View.INVISIBLE);
                }
                if (phoneNum.length() >= 1) {
                    input_username.img_center.setVisibility(View.VISIBLE);
                } else {
                    input_username.img_center.setVisibility(View.INVISIBLE);
                }
                checkBtnisVisible();
            }
        };
        input_username.txt_left.addTextChangedListener(watcher);
        input_password.txt_left.addTextChangedListener(watcher);

        input_username.txt_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                recycleview_users.setVisibility(View.INVISIBLE);
                isShowUserList=false;
                input_username.img_right.setSelected(false);
            }
        });
        input_username.img_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                //從緩存中拿數據，有數據展開列表，沒數據不管他
                getMvpPresenter().showUserList(isShowUserList);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (!isClickAgreeImg) {
                    Toast.makeText(ActivityLogin.this,"请先同意用户使用协议",Toast.LENGTH_SHORT).show();
                    return;
                }
                String result = checkPhoneNumResultNotip();
                String result1 = checkPassWordResultNotip();
                if (result.equals("")&&result1.equals("")) {
                        getMvpPresenter().login(checkedUsername,checkedPassword);
                }
            }
        });
        txt_register.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivityTakeData(ActivityLogin.this,ActivityRigisterOrChangePassWord.class,"注册");
            }
        });
        txt_forget_password.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivityTakeData(ActivityLogin.this,ActivityRigisterOrChangePassWord.class,"忘记密码");
            }
        });
        txt_agreement.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                ActivityUtils.startActivity(ActivityUserAgreeMent.class);
                ActivityUtils.startActivity(ActivityLogin.this,ActivityUserAgreeMent.class);
            }
        });
        yinsizhengce.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                ActivityUtils.startActivity(ActivityUserAgreeMent.class);
                ActivityUtils.startActivity(ActivityLogin.this, ActivityUserAgreeMentPravate.class);
            }
        });
        img_click_agree.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                getMvpPresenter().clickAgreeImg(isClickAgreeImg);
                checkBtnisVisible();
            }
        });
        input_username.img_center.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                getMvpPresenter().clearUserName();
            }
        });
        input_password.img_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                getMvpPresenter().clearPassWord();
            }
        });
    }

    /**
     * 检查下一步按钮是否可见
     **/
    private void checkBtnisVisible() {
        btn_confirm.setEnabled(false);
        if(checkPhoneNumResultNotip().equals("")&&checkPassWordResultNotip().equals("")&&isClickAgreeImg){
            btn_confirm.setEnabled(true);
        }
    }

    @Override
    public void initViews() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        recycleview_users.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        recycleview_users.setHasFixedSize(true);
        recycleview_users.setItemAnimator(new DefaultItemAnimator());
        recycleview_users.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,1), getResources().getColor(R.color.normal_txt_color_white)));
        //下划线
        txt_agreement.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        yinsizhengce.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        input_username.img_center.setVisibility(View.INVISIBLE);
        input_password.img_right.setVisibility(View.INVISIBLE);
        input_username.txt_left.setInputType(InputType.TYPE_CLASS_PHONE);
        input_password.txt_left.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        input_username.txt_left.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        input_password.txt_left.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
    }
    /**
     * 检查用户名
     **/
    private String checkPhoneNumResult() {
        checkedUsername = input_username.txt_left.getText().toString();
        if (!OInputValidation.chkInputPhoneNum(checkedUsername)){
            Toast.makeText(ActivityLogin.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
            return "请输入正确的手机号码";
        }
        return "";
    }

    /**
     * 检查密码
     **/
    private String checkPassWordResult() {
        checkedPassword = input_password.txt_left.getText().toString();
        if (!OInputValidation.chkInputPassword(checkedPassword))
        {
            return "请输入6到20位密码";
        }
        return "";
    }
    /**
     * 检查用户名
     **/
    private String checkPhoneNumResultNotip() {
        checkedUsername = input_username.txt_left.getText().toString();
        if (!OInputValidation.chkInputPhoneNum(checkedUsername)){
            return "请输入正确的手机号码";
        }
        return "";
    }
    /**
     * 检查密码
     **/
    private String checkPassWordResultNotip() {
        checkedPassword = input_password.txt_left.getText().toString();
        if (!OInputValidation.chkInputPassword(checkedPassword))
        {
            return "请输入6到20位密码";
        }
        return "";
    }

    private final MyHandler handler=new MyHandler(ActivityLogin.this);
    private static class MyHandler extends Handler {
        private final WeakReference<ActivityLogin> mActivity;
        public MyHandler(ActivityLogin activity) {
            mActivity = new WeakReference<ActivityLogin>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivityLogin activityLogin=mActivity.get();
            if(activityLogin!=null){
                if(msg.what==110){
                    /**隐藏进度条*/
                    ClipPopLoading.getInstance().stopLoading();
                }else   if(msg.what==111){
                    ActivityUtils.startActivity(activityLogin,ActivityHome.class);
                    activityLogin.finish();
                }else   if(msg.what==112){
                    String loginResult= (String) msg.obj;
                    if(loginResult.equals("")){
//                        GlobalContext.popMessage("登陆成功", activityLogin.getResources().getColor(R.color.normal_txt_color_cyan));
                    }else{
                        GlobalContext.popMessage(loginResult, activityLogin.getResources().getColor(R.color.normal_bg_color_tip_red));
                    }
                }
            }
        }
    }


    private void handleHide(){
        Message message=Message.obtain();
        message.what=110;
        handler.sendMessage(message);
    }

    private void handleToHomePage(){
        Message message=Message.obtain();
        message.what=111;
        handler.sendMessage(message);
    }
    /**设置提醒文字*/
    private void handleShowLoginResult(String loginresult){
        Message message=Message.obtain();
        message.what=112;
        message.obj=loginresult;
        handler.sendMessage(message);
    }
}
