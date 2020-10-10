package com.mani.car.mekongk1.ui.controlcar;

import android.graphics.PointF;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.mani.car.mekongk1.model.ManagerControlButtons;
import com.mani.car.mekongk1.model.carlist.DataControlButton;

import java.util.ArrayList;
import java.util.List;
@Deprecated
public class AdapterControlButton extends PagerAdapter {
    //    private int currentPos=0;
    private List<DataControlButton> names;
    private ControlButtonSingle     curButtons;
    private int mChildCount = 0;
//    public static final int POS_BASE = 50000,POS_MAX = 100000;//无限循环取消
    public AdapterControlButton(List<DataControlButton> list) {
        names = new ArrayList<DataControlButton>(list);
    }
    public void OnDownPoint(PointF point) {
        if(curButtons!=null)curButtons.OnDownPoint(point);
    }
    public void OnUpPoint(PointF point) {
        if(curButtons!=null)curButtons.OnUpPoint(point);
    }
    public void OnClickPoint(PointF point) {
        if(curButtons!=null)curButtons.OnClickPoint(point);
    }
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        curButtons = (ControlButtonSingle) object;
    }
    public void changeData(ArrayList<DataControlButton> list){
        names = new ArrayList<DataControlButton>(list);
        notifyDataSetChanged();
    }
    public void clearData(){
        curButtons = null;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = container.findViewWithTag(position);
        if (view != null) {
            container.removeView(view);// 删除页卡
        }
    }
    private int getPage(){
        double size = names.size();
        if(size == 0)names = ManagerControlButtons.getInstance().getShowingButtons();
        Double page = Math.ceil(size/3.0);
        return page.intValue();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
//        Log.e("adapter","getCount():"+getCount()+" position:"+position +" names.size()"+names.size());
        ControlButtonSingle buttons = new ControlButtonSingle(container.getContext(), null);
        //有限循环
        double size = names.size();
        if(size == 0)return buttons;
        Double page = Math.ceil(size/3.0);
        //无限循环
//        int pos = (position-POS_BASE) % getPage();
//        if(pos<0)pos = getPage()+pos;
//        Log.e("instantiateItem","position:"+position+"   pos:"+pos);
        //有限循环
        DataControlButton name1   = (names.size() >= position * 3 + 1 && names.get(position * 3 + 0).status == 1) ? names.get(position * 3 + 0) : null;
        DataControlButton name2   = (names.size() >= position * 3 + 2 && names.get(position * 3 + 1).status == 1) ? names.get(position * 3 + 1) : null;
        DataControlButton name3   = (names.size() >= position * 3 + 3 && names.get(position * 3 + 2).status == 1) ? names.get(position * 3 + 2) : null;

        //无限循环
//        String name1   = (names.size() >= pos * 3 + 1 && names.get(pos * 3 + 0).status == 1) ? names.get(pos * 3 + 0).name : null;
//        String name2   = (names.size() >= pos * 3 + 2 && names.get(pos * 3 + 1).status == 1) ? names.get(pos * 3 + 1).name : null;
//        String name3   = (names.size() >= pos * 3 + 3 && names.get(pos * 3 + 2).status == 1) ? names.get(pos * 3 + 2).name : null;
        buttons.setButtonName(name1, name2, name3);
        buttons.setTag(position);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(buttons);// 添加页卡
        return buttons;
    }

    @Override
    public int getCount() {
        //有限循环
        int page = names.size() / 3;
        int left = names.size() % 3;
        if (left > 0) return page + 1;
        return page;
        //无限循环
//        return POS_MAX;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;// 官方提示这样写
    }
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    @Override
    public void startUpdate(ViewGroup container) {
    }
    @Override
    public void finishUpdate(ViewGroup container) {
    }
    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}
