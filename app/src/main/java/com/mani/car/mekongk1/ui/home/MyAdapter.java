package com.mani.car.mekongk1.ui.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import com.mani.car.mekongk1.ui.home.fragment.fragment1.Fragment01;

import java.util.List;

/**
 * Created by qq522414074 on 2017/4/28.
 */

public class MyAdapter extends FragmentPagerAdapter {
    public static int TOTAL_FRAGMENT = 1;
    private List<Fragment> list;
    private Context context;
    private LayoutInflater inflater;
    private FragmentManager fm;

    public MyAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }
    public MyAdapter(FragmentManager fm) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        if(TOTAL_FRAGMENT == 1)return 1;
        return Integer.MAX_VALUE;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        int newposition;
        if (position >= 0) {
            newposition = (position + 500) % TOTAL_FRAGMENT;
        } else {
            newposition = (-position) % TOTAL_FRAGMENT;
        }
        //0，1，2，3时显示的每一个页面
        switch (newposition) {
            case 0:
                fragment = new Fragment01();
                return fragment;
//            case 1:
//                fragment = new Fragment02();
//                return fragment;
//            case 2:
//                fragment = new Fragment03();
//                return fragment;
//            case 3:
//                fragment = new Fragment04();
//                return fragment;
//            case 4:
//                fragment = new Fragment05();
//                return fragment;
            default:
                return null;
        }
    }
}
