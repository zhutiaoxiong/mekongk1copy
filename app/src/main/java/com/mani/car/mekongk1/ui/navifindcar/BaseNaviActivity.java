package com.mani.car.mekongk1.ui.navifindcar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.CarControlResult;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerVoiceSet;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;

import java.util.ArrayList;
import java.util.List;


public   class BaseNaviActivity extends Activity implements AMapNaviListener, AMapNaviViewListener ,OEventObject{

    protected AMapNaviView mAMapNaviView;
    protected AMapNavi mAMapNavi;
//    protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894,116.603039);
//    protected NaviLatLng mStartLatlng = new NaviLatLng(39.825934,116.342972);
    public static  List<NaviLatLng> sList=new ArrayList<>();
    public static List<NaviLatLng> eList=new ArrayList<>();
    protected List<NaviLatLng> mWayPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
//        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setUseInnerVoice(true);
        //设置模拟导航的行车速度
//        mAMapNavi.setEmulatorNaviSpeed(75);
//        sList.add(mStartLatlng);
//        eList.add(mEndLatlng);
        ODispatcher.addEventListener(OEventName.CAR_CONTROL_RESULT,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的

//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        ODispatcher.removeEventListener(OEventName.CAR_CONTROL_RESULT,this);
    }

    @Override
    public void onInitNaviFailure() {
        Log.e("高德测试", "onInitNaviFailure: " );
    }

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
        Log.e("高德测试", "onInitNaviSuccess: " );
    }

    @Override
    public void onStartNavi(int type) {
        //开始导航回调
        Log.e("高德测试", "onStartNavi: " );
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
        Log.e("高德测试", "onTrafficStatusUpdate: " );
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        //当前位置回调
        Log.e("高德测试", "onLocationChange: " );
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //播报类型和播报文字回调
        Log.e("高德测试", "onGetNavigationText: " );
    }

    @Override
    public void onGetNavigationText(String s) {
        Log.e("高德测试", "onGetNavigationText: " );
    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
        Log.e("高德测试", "onEndEmulatorNavi: " );
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        if(carInfo==null)return;
        OCtrlCar.getInstance().ccmd1233_controlCar(carInfo, 6, 0);
        PopNaviEnd.getInstance().show(mAMapNaviView, new PopNaviEnd.OnClickButtonListener() {
            @Override
            public void onClick(int pos) {
                final DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                if(currentCar==null)return;
                if(pos==1){
                    //开
                    OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 4, 0);
                }else if(pos==2){
                    //开尾箱
                    OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 5, 0);//开关同一个
                }
            }
        });
        Log.e("高德测试", "onArriveDestination: " );
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        GlobalContext.popMessage("算路失败",getResources().getColor(R.color.normal_bg_color_tip_red));
        Log.e("高德测试", "onCalculateRouteFailure: " );
        //路线计算失败
        Log.e("dm", "--------------------------------------------");
//        Log.i("dm", "路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
//        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //到达途径点
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS开关状态回调
    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
    }

    @Override
    public void onNaviMapMode(int isLock) {
        //地图的模式，锁屏或锁车
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
    }


    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //显示车道信息

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //多路径算路成功回调
    }

    @Override
    public void notifyParallelRoad(int i) {
        if (i == 0) {
            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {
            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在辅路");
        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }


    @Override
    public void onLockMap(boolean isLock) {
        //锁地图状态发生变化时回调
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CAR_CONTROL_RESULT)){
            CarControlResult result = (CarControlResult)o;
            if(result.instruction == 3 ) {
                SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getLockUpVoiceResId());
            }else if(result.instruction == 5) {
                SoundHelper.playSoundPool(GlobalContext.getContext(), R.raw.voice_backpag);
            }
            GlobalContext.popMessage("控车成功", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));//Color.parseColor("#90CF26")
        }else{
            GlobalContext.popMessage("控车失败", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));//Color.parseColor("#A00000")
        }
    }
}
