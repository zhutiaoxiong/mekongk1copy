package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.kulala.staticsview.dragGrid.DragGridViewZhuAllCanMove;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.ManagerControlButtonsZhu;

/**上滑显示的按扭列表*/
public class PopChangeButtons extends PopupWindow{
    private PopupWindow  popContain;//弹出管理
//    private View         parentView;//父对象显示
//    private        RelativeLayout     thisView;//本对象
//    private Context      context;
    private AdapterChangeButtons adapterChangeButtons;
    private DragGridViewZhuAllCanMove grid_sort;

//    private DragGridView grid_sort;
//    private ImageView img_bg;
    // ========================out======================
    private static PopChangeButtons _instance;
    public static PopChangeButtons getInstance() {
        if (_instance == null)
            _instance = new PopChangeButtons();
        return _instance;
    }

    //===================================================
    public void show(View parentView) {//,List<String> list
//        this.parentView = parentView;
        final Context context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RelativeLayout thisView = (RelativeLayout) layoutInflater.inflate(R.layout.control_change_buttons, null);
         grid_sort = (DragGridViewZhuAllCanMove) thisView.findViewById(R.id.grid_sort);
        final ScrollLayout scroll_layout=(ScrollLayout) thisView.findViewById(R.id.scroll_layout);
//        ImageView img_bg = (ImageView) thisView.findViewById(R.id.img_bg);
        //
        scroll_layout.setIsNeedScrollDown(true);
        adapterChangeButtons = new AdapterChangeButtons(context);
        grid_sort.setAdapter(adapterChangeButtons);
        grid_sort.setRunOnTouchEvent(new DragGridViewZhuAllCanMove.RunOnTouchEvent() {
            @Override
            public void runOnTouch(MotionEvent event) {
                scroll_layout.onTouchEvent(event);
            }
        });
//        img_bg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(popContain!=null){
//                    if(grid_sort!=null)grid_sort.removeDragImage();
//                    popContain.dismiss();
//                }
//            }
//        });

        scroll_layout.setOnScrollDownListLister(new ScrollLayout.OnScrollDownListLister() {
            @Override
            public void onScrollDown(boolean isScrollDown) {
                Log.e("测试", "onScrollDown:== "+isScrollDown );
                if(isScrollDown){
                    if(popContain!=null){
                        if(grid_sort!=null)grid_sort.removeDragImage();
                         popContain.dismiss();
                    }
                }
            }
        });
//        grid_sort.setOnChangeListener(new DragGridView.OnChangeListener() {
//            @Override
//            public void onChange(int from, int to) {
//                ManagerControlButtons.getInstance().switchButtons(from,to);
//                adapterChangeButtons = new AdapterChangeButtons(context);
//                grid_sort.setAdapter(adapterChangeButtons);
//            }
//            @Override
//            public boolean isNoMove(int pos) {
//                return adapterChangeButtons.isNoMove(pos);
//            }
//        });
        grid_sort.setOnFlashScrollListner(new DragGridViewZhuAllCanMove.OnFlashScrollListner() {
            @Override
            public void flashScroll() {
                if(popContain!=null){
                    if(grid_sort!=null)grid_sort.removeDragImage();
                    popContain.dismiss();
                }
            }
        });
        grid_sort.setOnChangeListener(new DragGridViewZhuAllCanMove.OnChangeListener() {
            @Override
            public void onChange(int from, int to) {
                ManagerControlButtonsZhu.getInstance().switchButtons(from,to);
                adapterChangeButtons = new AdapterChangeButtons(context);
                grid_sort.setAdapter(adapterChangeButtons);
            }

            @Override
            public boolean isNoMove(int pos) {
                return adapterChangeButtons.isNoMove(pos);
            }
        });



        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setAnimationStyle(R.style.PopupAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.TOP, 0, 0);
    }
    public void closePop(){
        if(popContain!=null){
            if(grid_sort!=null)grid_sort.removeDragImage();
            popContain.dismiss();
        }
    }

}
