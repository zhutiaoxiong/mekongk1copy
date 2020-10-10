package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kulala.staticsfunc.static_view_change.ODipToPx;

public class ScrollLayout extends View {
    private float actionDownY, actionUpY;
    private float dp2;
    private boolean isNeedScrollDown;


    public ScrollLayout(Context context) {
        super(context);
    }

    public ScrollLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dp2 = ODipToPx.dipToPx(context, 2);
    }

    public interface OnScrollDownListLister {
        void onScrollDown(boolean isScrollDown);
    }

    private OnScrollDownListLister listner;

    public void setOnScrollDownListLister(OnScrollDownListLister listner) {
        this.listner = listner;
    }

    public void setIsNeedScrollDown(boolean isNeedScrollDown) {
        this.isNeedScrollDown = isNeedScrollDown;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        runOnTouch(event);
        return true;
    }
    /**提供其它页面有事件，无法传递来的情况*/
    private void runOnTouch(MotionEvent event){
        Log.e("执行循序", "1" );
        Log.e("scroll",event.toString());
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                actionDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //向下滑動
                actionUpY = event.getRawY();
                if (actionUpY - actionDownY >= 0) {
                        listner.onScrollDown(true);
                } else {
                    //向上滑动
                    if (!isNeedScrollDown) {
                        if (listner != null) {
                            listner.onScrollDown(false);
                        }
                    }
                }
                break;
        }

    }
    //==========================================
}
