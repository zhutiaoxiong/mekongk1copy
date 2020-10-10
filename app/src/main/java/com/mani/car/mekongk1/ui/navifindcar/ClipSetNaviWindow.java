package com.mani.car.mekongk1.ui.navifindcar;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;

/**
 * Created by qq522414074 on 2016/8/19.
 */
public class ClipSetNaviWindow {
    private PopupWindow popContain;//弹出管理
    private View    parentView;//本对象显示
    private Context     context;
    private        RelativeLayout thisView;
    private Button btn_left;
    private Button btn_center;
    private Button btn_right;
    // ========================out======================
    private static ClipSetNaviWindow _instance;
    public static ClipSetNaviWindow getInstance() {
        if (_instance == null)
            _instance = new ClipSetNaviWindow();
        return _instance;
    }
    //===================================================
    public void show(View parentView, OnNaviSetListner listner) {
        this.listner = listner;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_set_navi_window, null);
        btn_left = (Button) thisView.findViewById(R.id.btn_left);
        btn_center = (Button) thisView.findViewById(R.id.btn_center);
        btn_right = (Button) thisView.findViewById(R.id.btn_right);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setAnimationStyle(R.style.PopNaviSetAnimation);
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popContain.setBackgroundDrawable(dw);
        int[] location=new int[2];
        parentView.getLocationOnScreen(location);
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        popContain.update();
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
    }
    public void initEvents() {
        btn_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                //导航
               if(listner!=null){
                   showButton(0);
                   listner.driveCar();
                   exitThis();
               }
            }
        });
        btn_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                //导航
                if(listner!=null){
                    showButton(1);
                    listner.ride();
                    exitThis();
                }
            }
        });
        btn_center.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                //导航
                if(listner!=null){
                    showButton(2);
                    listner.walk();
                    exitThis();
                }
            }
        });
    }
    public interface  OnNaviSetListner{
        void  walk();
        void  ride();
        void  driveCar();
    }
    private OnNaviSetListner listner;

    public void exitThis() {
        if (popContain != null) popContain.dismiss();
        popContain = null;
        parentView = null;
        thisView = null;
        listner=null;
    }
    private void showButton(int who){
        btn_right.setSelected(false);
        btn_center.setSelected(false);
        btn_left.setSelected(false);
        btn_left.setTextColor(context.getResources().getColor(R.color.normal_txt_color_black));
        btn_right.setTextColor(context.getResources().getColor(R.color.normal_txt_color_black));
        btn_center.setTextColor(context.getResources().getColor(R.color.normal_txt_color_black));
        switch (who){
            case 0:
                btn_left.setSelected(true);
                btn_left.setTextColor(context.getResources().getColor(R.color.normal_txt_color_white));
                break;
            case 1:
                btn_center.setSelected(true);
                btn_center.setTextColor(context.getResources().getColor(R.color.normal_txt_color_white));
                break;
            case 2:
                btn_right.setSelected(true);
                btn_right.setTextColor(context.getResources().getColor(R.color.normal_txt_color_white));
                break;
        }
    }
}
