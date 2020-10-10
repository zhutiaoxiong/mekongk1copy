package com.kulala.staticsview.style;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ClipPopWindow extends PopupWindow{
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private Context context;

    private LinearLayout thisView;
    private ListView listView;
    private        String           mark;//选择标记
    // ========================out======================
    private static ClipPopWindow _instance;
    public static ClipPopWindow getInstance() {
        if (_instance == null)
            _instance = new ClipPopWindow();
        return _instance;
    }
    //===================================================
    public void show(View parentView,String mark,LinearLayout thisView) {
        this.mark = mark;
        this.parentView = parentView;
        context = parentView.getContext();
        this.thisView=thisView;
        this.mark=mark;
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        ///这里设置进场动画
//        popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
    }
    public void initEvents() {
    }

}
