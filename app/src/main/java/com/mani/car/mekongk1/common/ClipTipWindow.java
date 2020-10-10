package com.mani.car.mekongk1.common;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mani.car.mekongk1.R;

/**
 * Created by qq522414074 on 2016/8/19.
 */
public class ClipTipWindow {
    private PopupWindow popContain;//弹出管理
    private Activity    parentView;//本对象显示
    private Context     context;

    private        RelativeLayout thisView;
    private        TextView       txt_tips;
    private        boolean        isShow = false;
    // ========================out======================
    private static ClipTipWindow  _instance;
    public static ClipTipWindow getInstance() {
        if (_instance == null)
            _instance = new ClipTipWindow();
        return _instance;
    }
    //===================================================
    private long preCheckTime = 0;
    public void show(Activity parentView, final String message, final int color) {
        if(parentView==null)return;
        this.parentView = parentView;
        context = parentView;
        parentView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                if(now - preCheckTime >8000L){
                    exitThis();
                }else if(isShow){
                    return;
                }
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_tip_window, null);
                txt_tips = (TextView) thisView.findViewById(R.id.txt_tips);
                txt_tips.setText(message);
                txt_tips.setBackgroundColor(color);
                initViews(color);
                initEvents();
                preCheckTime=now;
            }
        });
    }

    public void initViews(int color) {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setAnimationStyle(R.style.PopMessageAnimation);
        popContain.showAtLocation(parentView.getWindow().getDecorView(), Gravity.TOP, 0, 0);
        isShow = true;
        parentView.getWindow().setStatusBarColor(color);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitThis();
            }
        }, 1800);
    }
    public void initEvents() {
    }

    public void exitThis() {
        if (popContain != null) popContain.dismiss();
        isShow = false;
        popContain = null;
        thisView = null;
        GlobalContext.setStatusBarColorRecover();
    }
}
