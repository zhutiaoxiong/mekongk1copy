package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerVoiceSet;

import java.util.ArrayList;
import java.util.List;

public class DotIndicator implements ViewPager.OnPageChangeListener {
    private int mPageCount;//页数
    private List<ImageView> mImgList;//保存img总个数
    private int img_select;
    private int img_unSelect;

    public DotIndicator(Context context, LinearLayout linearLayout, int pageCount) {
        this.mPageCount = pageCount;

        mImgList = new ArrayList<>();
        img_select = R.drawable.dot_selected;
        img_unSelect = R.drawable.dot_normal;
        final int imgSize = ODipToPx.dipToPx(context,8);

        linearLayout.removeAllViews();
        for (int i = 0; i < mPageCount; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //为小圆点左右添加间距
            params.leftMargin = ODipToPx.dipToPx(context,5);
            params.rightMargin = ODipToPx.dipToPx(context,5);
            //给小圆点一个默认大小
            params.height = imgSize;
            params.width = imgSize;
            if (i == 0) {
                imageView.setBackgroundResource(img_select);
            } else {
                imageView.setBackgroundResource(img_unSelect);
            }
            //为LinearLayout添加ImageView
            linearLayout.addView(imageView, params);
            mImgList.add(imageView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mPageCount; i++) {
            //选中的页面改变小圆点为选中状态，反之为未选中
            if ((position % mPageCount) == i) {
                (mImgList.get(i)).setBackgroundResource(img_select);
            } else {
                (mImgList.get(i)).setBackgroundResource(img_unSelect);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state==0){
            //滑动时添加音效
            if(ManagerVoiceSet.getInstance().getIsOpenScrollButtonVoice()==-1){
                SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getScrollButtonVoiceResId());
            }
        }
    }
}
