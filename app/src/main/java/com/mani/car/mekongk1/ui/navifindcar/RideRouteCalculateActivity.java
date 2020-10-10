package com.mani.car.mekongk1.ui.navifindcar;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

/**
 * 骑行
 * */
public class RideRouteCalculateActivity extends BaseNaviActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navi_map);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        if(sList!=null&&sList.size()>0&&eList!=null&&eList.size()>0){
            mAMapNavi.calculateRideRoute(sList.get(0),eList.get(0));
        }
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

}
