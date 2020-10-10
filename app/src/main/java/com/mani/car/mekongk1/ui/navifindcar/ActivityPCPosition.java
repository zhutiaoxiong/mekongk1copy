package com.mani.car.mekongk1.ui.navifindcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.model.NaviLatLng;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.RxCoundDown;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerCurrentCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(PCPositionPresenter.class)
public class ActivityPCPosition extends BaseMvpActivity<PCPositionView,PCPositionPresenter> implements PCPositionView, AMap.OnMyLocationChangeListener, AMap.OnMapLoadedListener,OEventObject{
    @BindView( R.id.mapview)
    MapView mapview;
//    @BindView( R.id.navi_start)ImageView navi_start;
    @BindView( R.id.btn_left)Button btn_left;
    @BindView( R.id.btn_center)Button btn_center;
    @BindView( R.id.btn_right)Button btn_right;
    @BindView( R.id.layout_set_navi)LinearLayout layout_set_navi;


    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AMap aMap;//地图操作类
    private LatLng myLocation;
    private boolean canMove = true;//变量控制拖动不返回中心点
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_app);
        ButterKnife.bind(this);
        mapview.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapview.getMap();
        }
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnMapLoadedListener(this);
        setShowSelfPoint();
        setCarPosition(getCarPosition());
        initViews();
        layout_set_navi.setVisibility(View.VISIBLE);
        showButton(1);
        initEvents();
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            // 判断GPS模块是否开启
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(800);
                        GlobalContext.popMessage("请打开手机GPS，地下停车场有可能有偏差",getResources().getColor(R.color.normal_txt_color_cyan));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }



/**
 * 放自身定位
 * */
    private void setShowSelfPoint(){
       MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
//以下三种模式从5.1.0版本开始提供
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
    }
    /**放置车位置*/
    private void setCarPosition(LatLng latLng){
        if(latLng==null)return;
//        LatLng latLng = new LatLng(39.906901,116.397972);
//        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
        MarkerOptions markerOption = new MarkerOptions();
//        LatLng latLng = new LatLng(39.906901,116.397972);
        if(getCarPosition()==null)return;
            markerOption.position(latLng);
//        markerOption.title("天下第一").snippet("下啥啊啥阿三阿三");
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(zoomImage(BitmapFactory
                .decodeResource(getResources(),R.drawable.car_positon), ODipToPx.dipToPx(this,30),ODipToPx.dipToPx(this,45))));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        markerOption.visible(true);
        aMap.addMarker(markerOption);
    }
    private LatLng getCarPosition(){
        DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
        if(carInfo==null)return null;
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        if(carInfo.isActive == 0)ManagerCarList.getInstance().getCurrentCarStatus().gps = "23.018696,113.95182399999999";//东管的坐标
        if(carStatus.gps==null||carStatus.gps.equals(""))return null;
        String[] st = carStatus.gps.split(",");
        if(st==null||st.length<2)return null;
        LatLng latLng=new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
        LatLng carPostion=	CoordinateUtil.transformFromWGSToGCJ(latLng);
        return carPostion;
    }
    /**
     * 缩放bitmap
     * */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        ODispatcher.removeEventListener(OEventName.GET_CURRENT_CAR_RESULT_BACK,this);
        mapview.onDestroy();
        RxCoundDown.getInstance().closeTimer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
        ODispatcher.addEventListener(OEventName.GET_CURRENT_CAR_RESULT_BACK,this);
        mapview.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapview.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapview.onSaveInstanceState(outState);
    }



    @Override
    public void initEvents() {
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (canMove) {
                    //用户拖动地图后，不再跟随移动，直到用户点击定位按钮
                    canMove = false;
                }
            }
        });
        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        };
// 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);

        // 定义 Marker拖拽的监听
        AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

            // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub

            }

            // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub

            }

            // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub

            }
        };
// 绑定marker拖拽事件
        aMap.setOnMarkerDragListener(markerDragListener);

//        navi_start.setOnClickListener(new OnClickListenerMy(){
//            @Override
//            public void onClickNoFast(View v) {
                //导航
//                Poi start = new Poi("三元桥", new LatLng(39.96087,116.45798), "");
//                Poi start = new Poi("我的位置", myLocation, "");
///**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
//                Poi end = new Poi("北京站", new LatLng(39.904556, 116.427231), "B000A83M61");
//                List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
//                wayList.add(new Poi("团结湖", new LatLng(39.93413,116.461676), ""));
//                wayList.add(new Poi("呼家楼", new LatLng(39.923484,116.461327), ""));
//                wayList.add(new Poi("华润大厦", new LatLng(39.912914,116.434247), ""));
//
//                int pos= ManagerCarList.getInstance().getCurrentCar().carPos;
//                DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
//                if(carInfo==null)return;
//                if(carStatus==null)return;
//                if(carStatus.gps==null||carStatus.gps.equals(""))return;
//                String[] st = carStatus.gps.split(",");
//                if(st==null||st.length<2)return;
//                if(isInstallByRead("com.autonavi.minimap")){
//                    goToNaviActivity(ActivityPCPosition.this,"test",null,st[0],st[1],"1","2");
//                }else{
//                    GlobalContext.popMessage("请先安装高德地图APP",getResources().getColor(R.color.normal_bg_color_tip_red));
//                }
//                showButton(1);

//                AmapNaviPage.getInstance().showRouteActivity(ActivityNaviApp.this, new AmapNaviParams(start, wayList, end, AmapNaviType.DRIVER), new NaviInfoBackInterface());
//                AmapNaviPage.getInstance().showRouteActivity(ActivityPCPosition.this, new AmapNaviParams(null), new NaviInfoBackInterface());
//                ClipSetNaviWindow.getInstance().show(navi_start, new ClipSetNaviWindow.OnNaviSetListner() {
//                    @Override
//                    public void walk() {
////                        showButton(2);
//                        ActivityUtils.startActivity(ActivityPCPosition.this, WalkRouteCalculateActivity.class);
//                    }
//
//                    @Override
//                    public void ride() {
////                        showButton(1);
//                        ActivityUtils.startActivity(ActivityPCPosition.this, RideRouteCalculateActivity.class);
//                    }
//
//                    @Override
//                    public void driveCar() {
////                        showButton(0);
//                        ActivityUtils.startActivity(ActivityPCPosition.this, CustomCarActivity.class);
//                    }
//                });
//            }
//        });
        btn_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                showButton(0);
                ActivityUtils.startActivity(ActivityPCPosition.this, CustomCarActivity.class);
            }
        });
        btn_center.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                showButton(1);
                ActivityUtils.startActivity(ActivityPCPosition.this, WalkRouteCalculateActivity.class);
            }
        });
        btn_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                //导航
                showButton(2);
                ActivityUtils.startActivity(ActivityPCPosition.this, RideRouteCalculateActivity.class);
            }
        });
    }
    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:02
     * <h3>UpdateTime</h3> 2016/6/27,13:02
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
    /**
     * 启动高德App进行导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:58
     * <h3>UpdateTime</h3> 2016/6/27,13:58
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname 非必填 POI 名称
     * @param lat 必填 纬度
     * @param lon 必填 经度
     * @param dev 必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style 必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static  void goToNaviActivity(Context context, String sourceApplication , String poiname , String lat , String lon , String dev , String style){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }
    public static  void goToNaviActivity(Context context, String sourceApplication , String poiname , String dlat , String dlon , String dev , String t,String rideType){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(dlat)
                .append("&lon=").append(dlon)
                .append("&dev=").append(dev)
                .append("&t=").append(t)
                .append("&rideType=").append(rideType);
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    private NaviLatLng getEndPosition(){
        DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
        if(carInfo==null)return null;
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        if(carStatus.gps==null||carStatus.gps.equals(""))return null;
        String[] st = carStatus.gps.split(",");
        if(st==null||st.length<2)return null;
        LatLng latLng=new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
        LatLng carPostion=	CoordinateUtil.transformFromWGSToGCJ(latLng);
        return new NaviLatLng(carPostion.latitude,carPostion.longitude);
    }


    private void showButton(int who){
        btn_right.setSelected(false);
        btn_center.setSelected(false);
        btn_left.setSelected(false);
        btn_left.setTextColor(getResources().getColor(R.color.normal_txt_color_black));
        btn_right.setTextColor(getResources().getColor(R.color.normal_txt_color_black));
        btn_center.setTextColor(getResources().getColor(R.color.normal_txt_color_black));
        switch (who){
            case 0:
                btn_left.setSelected(true);
                btn_left.setTextColor(getResources().getColor(R.color.normal_txt_color_white));
                break;
            case 1:
                btn_center.setSelected(true);
                btn_center.setTextColor(getResources().getColor(R.color.normal_txt_color_white));
                break;
            case 2:
                btn_right.setSelected(true);
                btn_right.setTextColor(getResources().getColor(R.color.normal_txt_color_white));
                break;
        }
    }

    @Override
    public void initViews() {
        BaseNaviActivity.eList.clear();
        if(getEndPosition()!=null){
            BaseNaviActivity.eList.add (getEndPosition());
        }
        requestData();
        RxCoundDown.getInstance().setOnTimeCompleteListner(new RxCoundDown.OnTimeCompleteListner() {
            @Override
            public void onStart() {

            }

            @Override
            public void Complete() {
                Log.e("计时", "OnCount: "+"发包");
                requestData();
                RxCoundDown.getInstance().startTime(15);
            }

            @Override
            public void OnCount(Long value) {
                Log.e("计时", "OnCount: "+value);
            }
        });
        RxCoundDown.getInstance().startTime(15);
    }
    private void requestData(){
        long carId=ManagerCarList.getInstance().getCurrentCarID();
        DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
        if(carInfo.isActive==0)return;
        OCtrlCar.getInstance().ccmd1213_getCarPosition(carId,0);
    }


    /**自身位置*/
    @Override
    public void onMyLocationChange(Location location) {
        if(location!=null){
            myLocation=new LatLng(location.getLatitude(),location.getLongitude());
            if(myLocation!=null){
                BaseNaviActivity.sList.clear();

                BaseNaviActivity.sList.add(AMapUtil.convertToNaviLatLng(myLocation));

//                BaseNaviActivity.eList.add(AMapUtil.convertToNaviLatLng(new LatLng(39.904556, 116.427231)));
            }
        }
        if (canMove) {
            aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
        }
    }

    @Override
    public void onMapLoaded() {
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.GET_CURRENT_CAR_RESULT_BACK)){
          NaviLatLng endPos = getEndNaviPositionNow(ManagerCurrentCarInfo.getInstance().latlng);
          if(endPos!=null){
              BaseNaviActivity.eList.clear();
              BaseNaviActivity.eList.add (endPos);
          }
         final LatLng carPos=   getEndPositionNow(ManagerCurrentCarInfo.getInstance().latlng);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    aMap.clear();
                    setCarPosition(carPos);
                }
            });
        }
    }
    private NaviLatLng getEndNaviPositionNow(String lalng){
        if(lalng==null||lalng.equals(""))return null;
        String[] st = lalng.split(",");
        if(st==null||st.length<2)return null;
        LatLng latLng=new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
        LatLng carPostion=	CoordinateUtil.transformFromWGSToGCJ(latLng);
        return new NaviLatLng(carPostion.latitude,carPostion.longitude);
    }
    private LatLng getEndPositionNow(String lalng){
        if(lalng==null||lalng.equals(""))return null;
        String[] st = lalng.split(",");
        if(st==null||st.length<2)return null;
        LatLng latLng=new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
        LatLng carPostion=	CoordinateUtil.transformFromWGSToGCJ(latLng);
        return carPostion;
    }
}
