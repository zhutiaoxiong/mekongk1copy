package com.mani.car.mekongk1.ui.navifindcar;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;

public class PopNaviEnd {
    /**
     * li1-li4代表分享1微信2朋友圈3qq4qq空间
     */
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
//    private LinearLayout li1,li2,li3;
private Button open_door,tail_box;
    private TextView txt_cancel;
    private        View         touch_exit;
    private OnClickButtonListener onClickButtonListener;
    private        MyHandler    handler;
    // ========================out======================
    private static PopNaviEnd _instance;

    public static PopNaviEnd getInstance() {
        if (_instance == null)
            _instance = new PopNaviEnd();
        return _instance;
    }

    public void show(View parentView, OnClickButtonListener onClickButtonListener) {
        if (handler == null) handler = new MyHandler();
        this.onClickButtonListener = onClickButtonListener;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.pop_navi_end, null);
        open_door = (Button) thisView.findViewById(R.id.open_door);
        tail_box         = (Button) thisView.findViewById(R.id.tail_box);
//        txt_cancel = (TextView) thisView.findViewById(R.id.txt_cancel);
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
        open_door.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(onClickButtonListener!=null)onClickButtonListener.onClick(1);
            }
        });
        tail_box.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
                if(onClickButtonListener!=null)onClickButtonListener.onClick(2);
            }
        });
//        txt_cancel.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
//                handlehide();
//            }
//        });
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

