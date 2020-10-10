package com.mani.car.mekongk1.ui.personcenter.help.sugest;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mani.car.mekongk1.R;
import com.kulala.staticsview.OnClickListenerMy;


public class OToastSelectPic {
    /**
     * li1-li4代表分享1微信2朋友圈3qq4qq空间
     */
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
    private TextView txt_cancel,txt_galary,txt_camara;
    private        View         touch_exit;
    private OnClickButtonListener onClickButtonListener;
    private        MyHandler    handler;
    // ========================out======================
    private static OToastSelectPic _instance;

    public static OToastSelectPic getInstance() {
        if (_instance == null)
            _instance = new OToastSelectPic();
        return _instance;
    }

    public void show(View parentView, OnClickButtonListener onClickButtonListener) {
        if (handler == null) handler = new MyHandler();
        this.onClickButtonListener = onClickButtonListener;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.otoast_select_pic, null);
        txt_galary = (TextView) thisView.findViewById(R.id.txt_galary);
        txt_camara = (TextView) thisView.findViewById(R.id.txt_camara);
        txt_cancel = (TextView) thisView.findViewById(R.id.txt_cancle);
        touch_exit = (View) thisView.findViewById(R.id.touch_exit);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }


    public void initEvents() {
        touch_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
            }
        });
        txt_camara.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(onClickButtonListener!=null)onClickButtonListener.onClick(2);
            }
        });
        txt_galary.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(onClickButtonListener!=null)onClickButtonListener.onClick(1);
            }
        });
        txt_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
            }
        });
    }
    private void handlehide() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16596:
                    if(popContain == null)return;
                    popContain.dismiss();
                    onClickButtonListener = null;
                    parentView = null;
                    thisView = null;
                    context = null;
                    break;
            }
        }
    }

    public interface OnClickButtonListener {
        void onClick(int pos);
    }
}

