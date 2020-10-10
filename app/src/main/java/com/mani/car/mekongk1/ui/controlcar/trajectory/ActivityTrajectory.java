package com.mani.car.mekongk1.ui.controlcar.trajectory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlGps;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerGps;
import com.mani.car.mekongk1.model.gps.DataGpsPath;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.navifindcar.AMapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(TrajectoryPresenter.class)
public class ActivityTrajectory extends BaseMvpActivity<TrajectoryView,TrajectoryPresenter> implements TrajectoryView ,OEventObject{
    @BindView( R.id.mapview)
    MapView mapview;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.img_date)
    ImageView img_date;
    @BindView(R.id.recycleview_records)
    RecyclerView recycleview_records;
    @BindView(R.id.txt_no_trajectory)
    TextView txt_no_trajectory;

    private TrajectoryRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private AMap aMap;//地图操作类
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private List<DataGpsPath> dataGpsPathList;
    private  int startPosition;
    private   boolean needData;
    private long startTime;
    private long endTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        ButterKnife.bind(this);
        mapview.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapview.getMap();
        }

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);//这个方法设置了地图是否允许通过手势来旋转
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(4));
//        aMap.setMapTextZIndex(2);
        initViews();
        getDataFromService();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_PATHLIST_RESULTBACK,this);
    }
    private void getDataFromService(){
        if (ManagerCarList.getInstance().getCurrentCar().ide > 0) {
//            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide, TimeUtils.getTodayZeroPointTimestamps(), System.currentTimeMillis(), 0, 1);
            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide,0, 0, 0, 5);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapview.onDestroy();
        ODispatcher.removeEventListener(OEventName.GPS_PATHLIST_RESULTBACK,this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
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
        img_date.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                startActivityForResult(new Intent(ActivityTrajectory.this,ActivityLendChooseDateForTrajectory.class),1);
            }
        });
        recycleview_records.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("行车轨迹", "onScrollStateChanged:  +new State "+newState );
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleitem=lm.findFirstVisibleItemPosition();
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    if(firstVisibleitem==0){
                        //滑到顶部
                        needData=false;
                        startPosition=0;
                    }else if(lastVisibleItemPosition == totalItemCount - 1//划到底部
                            && visibleItemCount > 0){
                        needData=true;
                        if(myRecycleViewAdapter!=null){
                            startPosition= myRecycleViewAdapter.getItemCount();
                        }
                        if (ManagerCarList.getInstance().getCurrentCar().ide > 0) {
//            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide, TimeUtils.getTodayZeroPointTimestamps(), System.currentTimeMillis(), 0, 1);
                            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide,startTime, endTime, startPosition, 20);
                        }
                    }
                }
                    //加载更多
                    Log.e("行车轨迹", "totalItemCount"+totalItemCount+"lastVisibleItemPosition"+lastVisibleItemPosition+"visibleItemCount"+visibleItemCount+"onScrollStateChanged:   到达屏幕底部" );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==1&&resultCode== RESULT_OK){
            startTime=data.getExtras().getLong("startTime");
            endTime=data.getExtras().getLong("endTime");
           OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide,startTime, endTime, 0, 20);
       }
    }

    @Override
    public void initViews() {
        needData=false;
        startPosition=0;
        mLinearLayoutManager = new LinearLayoutManager(this);
        recycleview_records.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        recycleview_records.setHasFixedSize(true);
        recycleview_records.setItemAnimator(new DefaultItemAnimator());
        recycleview_records.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,12), getResources().getColor(R.color.normal_bg_color_big_white)));
    }
/**有没有数据展示没有数据的页面和内容页面*/
    private  void setIsHaveData(List<DataGpsPath> list){
        txt_no_trajectory.setVisibility(View.INVISIBLE);
        recycleview_records.setVisibility(View.INVISIBLE);
        if(list==null||list.size()==0){
            txt_no_trajectory.setVisibility(View.VISIBLE);
        }else{
            recycleview_records.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.GPS_PATHLIST_RESULTBACK)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDataShowUI();
                }
            });
        }
    }
    private  void setDataShowUI(){
        //设置列表数据，画出地图上的轨迹
        List<DataGpsPath> currentPath=ManagerGps.getInstance().singleCarPath;
        if(dataGpsPathList==null)dataGpsPathList=new ArrayList<>();
        if(needData){
            if(dataGpsPathList.size()<=startPosition){
                dataGpsPathList.addAll(currentPath);
            }
        } else{
            if(dataGpsPathList.size()>0){
                dataGpsPathList.clear();
            }
            dataGpsPathList.addAll(currentPath);
        }
        setIsHaveData(dataGpsPathList);
        if(dataGpsPathList==null||dataGpsPathList.size()==0)return;
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new TrajectoryRecycleViewAdapter(dataGpsPathList, new TrajectoryRecycleViewAdapter.OnItemClickListener() {
                @Override
                public void onPathSelect(View view, int position) {
                    if(dataGpsPathList==null||dataGpsPathList.size()==0)return;
                    DataGpsPath dataGpsPath=dataGpsPathList.get(position);
                    if(dataGpsPath==null||dataGpsPath.latlngs==null||dataGpsPath.latlngs.length==0)return;
                    getLatlngAndDrawMarker(dataGpsPath);
                    txt_date.setText(ODateTime.time2StringDateAndWeek(dataGpsPath.createTime));
                }
            });
            recycleview_records.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        if(dataGpsPathList.get(0)==null)return;
        getLatlngAndDrawMarker(dataGpsPathList.get(0));
        txt_date.setText(ODateTime.time2StringDateAndWeek(dataGpsPathList.get(0).createTime));
    }
    private void getLatlngAndDrawMarker(DataGpsPath gpsPath){
        if(gpsPath!=null){
            if(aMap==null)return;
            aMap.clear();
            List<LatLng> list=AMapUtil.getLatLngs(gpsPath.latlngs);
            if(list==null||list.size()==0)return;
            LatLng startPoint=list.get(0);
            LatLng endPosition=list.get(list.size()-1);
            drawMarker(startPoint,0);
            drawMarker(endPosition,1);
            aMap.addPolyline(new PolylineOptions().addAll(list).geodesic(true).width(15).setCustomTexture(BitmapDescriptorFactory.defaultMarker()));
            //设置所有Marker可见
            LatLngBounds.Builder builder=new LatLngBounds.Builder();
            for (int i = 0; i <list.size() ; i++) {
                builder.include(list.get(i));
            }
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),10));
        }
    }

    private  void drawMarker(LatLng point,int mark){
        MarkerOptions markerOption=new MarkerOptions();
        markerOption.position(point);
        if(mark==0){
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(zoomImage(BitmapFactory
                    .decodeResource(getResources(),R.drawable.start_point),ODipToPx.dipToPx(this,20),ODipToPx.dipToPx(this,30))));
        }else if(mark==1){
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(zoomImage(BitmapFactory
                    .decodeResource(getResources(),R.drawable.end_point),ODipToPx.dipToPx(this,20),ODipToPx.dipToPx(this,30))));
        }
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        markerOption.visible(true);
        aMap.addMarker(markerOption);
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
}
