package com.kulala.staticsview.toast;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;

public class ToastConfirmNormalForAllClick extends RelativeLayout {
    private static boolean isShowing = false;
    private TextView txt_title, txt_info, btn_cancel, btn_confirm;
    //    private ImageView img_splitline;//view_background
    private RelativeLayout lin_views;
    private LinearLayout lin_shows;

    private ToastConfirmNormalForAllClick.OnButtonClickListener listener;
    private Activity useActivity;//activity的根View
    private ViewGroup decorViewGroup;//activity的根View
    public static final int COLOR_BLUE = 0xFF1F89FE;
    public static final int COLOR_GRAY = 0xFFCFCFD5;
    public static final int STYLE_BLACK = 90016;
    public static final int STYLE_WHITE = 90017;

    public static ToastConfirmNormalForAllClick ToastConfirmNormalThis;//外部用来判断是否弹出了窗
    // ===================================================
    /**
     * @param isBlack ActivityHome.PAGE_IS_BLACKSTYLE
     */
    public ToastConfirmNormalForAllClick(Activity contextActivity, AttributeSet attrs, boolean isBlack) {
        super(contextActivity, attrs);
        if (contextActivity == null) return;
        this.useActivity = contextActivity;
        Log.e("ToastConfirmNormal","弹窗的是Activity:"+contextActivity.getLocalClassName());
        LayoutInflater.from(contextActivity).inflate(R.layout.toast_confirm_anim, this, true);
        lin_shows = (LinearLayout) findViewById(R.id.lin_shows);
        lin_views = (RelativeLayout) findViewById(R.id.lin_views);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_info = (TextView) findViewById(R.id.txt_text);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
//        view_background = (ImageView) findViewById(R.id.view_background);
        initViews();
        initEvent();
        if(contextActivity.getLocalClassName().contains("ActivityHome") && isBlack){
//            withStyle(STYLE_BLACK);
        }else{
            withStyle(STYLE_WHITE);
        }
    }
    @Override
    protected void onAttachedToWindow() {
        isShowing = true;
        super.onAttachedToWindow();
    }
    @Override
    protected void onDetachedFromWindow() {
        isShowing = false;
        super.onDetachedFromWindow();
    }
    private void initViews() {
        lin_shows.setVisibility(INVISIBLE);
        txt_title.setVisibility(GONE);
        txt_info.setVisibility(GONE);
//        txt_info.setTextColor(Color.BLACK);
    }
    private void initEvent() {
        ToastConfirmNormalForAllClick.this.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                exit();
                if (listener != null) listener.onClickConfirm(0);
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                exit();
                if (listener != null) listener.onClickConfirm(1);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                exit();
                if (listener != null) listener.onClickConfirm(2);
            }
        });
        lin_shows.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //不要点了消失了
            }
        });
//        txt_info.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //不要点了消失了
//            }
//        });
    }
    private void exit() {
        popView(true, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (decorViewGroup != null) {
                    decorViewGroup.removeView(ToastConfirmNormalForAllClick.this);
                    decorViewGroup = null;
                    useActivity = null;
                    ToastConfirmNormalThis = null;
                    Log.e("TOAST", "decorViewGroup removed ConfirmView");
                } else {
                    Log.e("TOAST", "decorViewGroup null");
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    // ===================================================
    //外部其它指令调用点击确定取消
    public void callConfirm() {
        btn_confirm.callOnClick();
    }
    public void callCancle() {
        btn_cancel.callOnClick();
    }
    //    @Override
//    protected void onAttachedToWindow() {
//        OAnimSet.startAnimation(lin_shows,OAnimSet.FadeIn);
//        super.onAttachedToWindow();
//    }
//    @Override
//    protected void onDetachedFromWindow() {
//        OAnimSet.startAnimation(lin_shows,OAnimSet.SlideLeft);//无效的
//        super.onDetachedFromWindow();
//    }
    // ===================================================
    private int cacheStyle;
    private ToastConfirmNormalForAllClick withStyle(int styleInner) {
        if(styleInner == STYLE_BLACK){

        }else if(styleInner == STYLE_WHITE){
            cacheStyle = styleInner;
            btn_cancel.setBackground(getResources().getDrawable(R.drawable.bgst_button_left_wt));
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.bgst_button_right_wt));
            lin_shows.setBackground(getResources().getDrawable(R.drawable.bgst_white_round8));
            txt_title.setTextColor(Color.parseColor("#000000"));
            txt_info.setTextColor(Color.parseColor("#ABABAB"));
            btn_cancel.setTextColor(Color.parseColor("#000000"));
            btn_confirm.setTextColor(Color.parseColor("#000000"));
        }
        return this;
    }
    public ToastConfirmNormalForAllClick withTitle(CharSequence title) {
        if (title != null && title.length() > 0) {
            txt_title.setVisibility(VISIBLE);
            txt_title.setText(title);
        }
        return this;
    }
    public ToastConfirmNormalForAllClick withInfo(CharSequence info) {
        if (info != null && info.length() > 0) {
            txt_info.setVisibility(VISIBLE);
            txt_info.setText(info);
        }
        return this;
    }
    public ToastConfirmNormalForAllClick withAddView(ViewGroup view) {
        if (view != null) {
            lin_views.setVisibility(VISIBLE);
            lin_views.addView(view);
        }
        return this;
    }
    public ToastConfirmNormalForAllClick withInfoTxtSize(int size) {
        txt_info.setTextSize(14);
        return this;
    }
    public ToastConfirmNormalForAllClick withAddView(View view) {
        if (view != null) {
            lin_views.setVisibility(VISIBLE);
            lin_views.addView(view);
        }
        return this;
    }
    /**
     * 有一个为空，或空字符，就只显示一个按扭
     */
    public ToastConfirmNormalForAllClick withButton(String cancle, String confirm) {
        if (cancle == null || cancle.length() == 0) {
            btn_cancel.setVisibility(View.GONE);
        } else {
            btn_cancel.setText(cancle);
            if(cacheStyle == STYLE_WHITE)btn_cancel.setBackground(getResources().getDrawable(R.drawable.bgst_button_single_wt));
            else btn_cancel.setBackground(getResources().getDrawable(R.drawable.bgst_button_single_bk));
        }
        if (confirm == null || confirm.length() == 0) {
            btn_confirm.setVisibility(View.GONE);
        } else {
            btn_confirm.setText(confirm);
            if(cacheStyle == STYLE_WHITE)btn_confirm.setBackground(getResources().getDrawable(R.drawable.bgst_button_single_wt));
            else btn_confirm.setBackground(getResources().getDrawable(R.drawable.bgst_button_single_bk));
        }
        return this;
    }
    //    //Gravity.LEFT
//    public ToastConfirmNormal withGravity(int gravity) {
//        txt_info.setGravity(gravity);
//        return this;
//    }
//    public ToastConfirmNormal withTextSize(int dp) {
//        txt_info.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
//        return this;
//    }
    public ToastConfirmNormalForAllClick withClick(ToastConfirmNormalForAllClick.OnButtonClickListener listener) {
        this.listener = listener;
        return this;
    }
    private long preTime = 0;
    public void show() {
        if(isShowing)return;
        long now = System.currentTimeMillis();
        if (now - preTime < 2000) return;
        preTime = now;
        useActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (decorViewGroup == null && useActivity != null) {
                    Log.e("TOASTNormal", "show");
                    View decorView = useActivity.getWindow().getDecorView();
                    if (decorView == null) return;
                    decorViewGroup = (ViewGroup) decorView.findViewById(android.R.id.content);
                    decorViewGroup.addView(ToastConfirmNormalForAllClick.this);
                    ToastConfirmNormalThis = ToastConfirmNormalForAllClick.this;
                    popView(false,null);
                }else{
                    Log.e("TOASTNormal", "unshow");
                }
            }
        });
    }
    // ===================================================
    public void popView(final boolean isHide,final Animation.AnimationListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation ;
                //相对自身位移百分比 1f==100%
                if(isHide)animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                else animation= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
                animation.setInterpolator(new LinearInterpolator());//OvershootInterpolator
                animation.setFillAfter(true);
                animation.setDuration(300);
                animation.setAnimationListener(listener);
                if(lin_shows.getVisibility() == INVISIBLE)lin_shows.setVisibility(VISIBLE);
                lin_shows.startAnimation(animation);
            }
        },100L);
    }
    // ===================================================
    public interface OnButtonClickListener {
        void onClickConfirm(int isClickConfirm);
    }
}
