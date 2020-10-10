package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerControlButtons;
import com.mani.car.mekongk1.model.carlist.DataControlButton;

import java.util.ArrayList;

/**
 * 点车身弹出的按扭
 */
public class ControlButtonViewpager extends ViewPager implements OEventObject{
    private ArrayList<Button>    buttonList;
    private AdapterControlButton adapter;
    PointF downP = new PointF();//触摸时按下的点
    PointF curP  = new PointF();//触摸时当前的点

    public ControlButtonViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
    }
    public int getPageNum(){
        if(adapter == null)return 0;
        return adapter.getCount();
    }
    public void changeData(){
        ArrayList<DataControlButton> names = ManagerControlButtons.getInstance().getShowingButtons();
        if(adapter == null){
            adapter = new AdapterControlButton(names);
            ControlButtonViewpager.this.setAdapter(adapter);
            //无限循环
//            ControlButtonViewpager.this.setCurrentItem(AdapterControlButton.POS_BASE);
        }else{
            adapter.changeData(names);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        Log.e("onInterceptTouch","MotionEvent:"+event.getAction()+"只有ACTION_DOWN起作用了");
        //返回true,说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        return true;
    }
    @Override
    protected void onDetachedFromWindow() {
        if(adapter!=null)adapter.clearData();
        super.onDetachedFromWindow();
        ODispatcher.removeEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        Object tag = getTag();
        if(tag == null || (Boolean)tag == false)return super.onTouchEvent(arg0);
        curP.x = arg0.getX();
        curP.y = arg0.getY();
//        Log.e("onTouchEvent","action:"+arg0.getAction());
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.e("ViewPager","onTouchEvent: ACTION_DOWN");
            adapter.OnDownPoint(curP);//执行按下效果
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
//            Log.e("ViewPager","onTouchEvent: ACTION_MOVE");
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {
//            Log.e("ViewPager","onTouchEvent: ACTION_UP");
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            adapter.OnUpPoint(curP);//释放按下效果
            int pxV = ODipToPx.dipToPx(getContext(),20);
            if (Math.abs(downP.x - curP.x) < pxV && Math.abs(downP.y - curP.y) < pxV) {
                adapter.OnClickPoint(curP);
                return true;
            }
        }
        //return true 逻辑执行完了，不用再执行此对象的滑动方法
        return super.onTouchEvent(arg0);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.AUTHORIZATION_USER_STOPED)){
            if(GlobalContext.getCurrentActivity()!=null){
                GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter!=null){
                            ArrayList<DataControlButton> names = ManagerControlButtons.getInstance().getShowingButtons();
                            adapter.changeData(names);
                            GlobalContext.popMessage("取消授权成功",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                        }
                    }
                });
            }
        }
    }
}
