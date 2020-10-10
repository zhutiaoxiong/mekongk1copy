package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerWarnings;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;

import java.util.ArrayList;
import java.util.List;

import static com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll.STATE_CONNECT_OK;

/**
 * 左边状态
 */
public class ViewSideState extends RelativeLayout implements OEventObject {
    private static final String TAG = "ViewSideState";
    private RecyclerView recycler_vv;
    private AdapterSideState recyclerAdapter;
    private List<String> preArr = new ArrayList<>();
    public static final String[] NAMES = new String[]{"警告", "无连接", "警戒中", "开锁", "电压", "启动","电源", "蓝牙"};//关锁

    public ViewSideState(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.control_sidestate, this, true);
        recycler_vv = (RecyclerView) findViewById(R.id.recycler_vv);
        initEvent();
        resetData();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetData();
        ODispatcher.addEventListener(OEventName.FRAGMENT01_ON_RESUME,this);
        ODispatcher.addEventListener(OEventName.FRAGMENT01_ON_STOP,this);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.FRAGMENT01_ON_RESUME,this);
        ODispatcher.removeEventListener(OEventName.FRAGMENT01_ON_STOP,this);
        super.onDetachedFromWindow();
    }

    @Override
    public void receiveEvent(String eventName, Object eventObject) {
        if(eventName.equals(OEventName.FRAGMENT01_ON_RESUME)){
            if(recyclerAdapter!=null)recyclerAdapter.superOnResume();
        }else if(eventName.equals(OEventName.FRAGMENT01_ON_STOP)){
            if(recyclerAdapter!=null)recyclerAdapter.superOnStop();
        }
    }

    private void initEvent() {
    }
    public void resetData() {
        resetData(false);
    }
    public void resetData(boolean check) {
        List<String> cacheArr = new ArrayList<>();
        if(ManagerWarnings.getInstance().getNewWarnings()!=null){
            cacheArr.add(NAMES[0]);
        }
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//        Log.e("TSSTATE","funInfos:"+carInfo.funInfos);
        if(carStatus.isOnline == 0){//改为车辆不在线
            cacheArr.add(NAMES[1]);
        }
        if(carStatus.isTheft == 1){
            cacheArr.add(NAMES[2]);
            if(carStatus.isLock != 1){//NAMES[3]
                cacheArr.add("开锁");
            }
        }else{
            if(carStatus.isLock == 1){//NAMES[3]
                cacheArr.add("关锁");
            }else {
                cacheArr.add("开锁");
            }
        }

//        Log.e("side状态",(carStatus.isLock == 0 ? "开锁" : "上锁"));
        if(carInfo.isActive != 1) {//演示,没激活的车,假车，直接显示电压
            cacheArr.add(NAMES[4]);
        }else if(carStatus.isOnline == 1 && carStatus.voltage >= 0.1){//电压
            cacheArr.add(NAMES[4]);
        }
        if(carStatus.isStart == 1){//启动
            cacheArr.add(NAMES[5]);
        }
        if(carStatus.isON == 1){//电源
            cacheArr.add(NAMES[6]);
        }
        if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK){
            cacheArr.add(NAMES[7]);
        }

        if(recyclerAdapter == null){
            recycler_vv.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_vv.setHasFixedSize(true);
            recyclerAdapter=new AdapterSideState(cacheArr,getContext());
            recycler_vv.setAdapter(recyclerAdapter);
        }else{
            recyclerAdapter.setData(cacheArr);
        }
        recyclerAdapter.setOnItemClickListener(new AdapterSideState.OnItemClickListener() {

            @Override
            public void onClickPicture(int position, String name) {
                DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                if(name.equals(NAMES[0])){//警告
                    ManagerWarnings.getInstance().showWarning();
                    ManagerWarnings.getInstance().clearWarning();
                    resetData();
                }else    if(name.equals(NAMES[1])){//无连接
                    GlobalContext.popMessage("当前车辆不在线",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                }else    if(name.equals(NAMES[2])){//警戒中
                    GlobalContext.popMessage("您的爱车正在警戒中",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                }else    if(name.equals(NAMES[3])||name.equals("关锁")){//锁
                    if(carInfo!=null){
                        if(ManagerCarList.getInstance().getCurrentCarStatus().isLock == 1){
                            GlobalContext.popMessage("您的爱车门已锁",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));

                        }else{
                            GlobalContext.popMessage("您的爱车门锁已开",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                        }
                    }
                }else    if(name.equals(NAMES[4])){//电压
                    GlobalContext.popMessage("这是您爱车电瓶的电压值",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                }else    if(name.equals(NAMES[5])){//启动
                    GlobalContext.popMessage("您的爱车已启动了",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                }else    if(name.equals(NAMES[6])){//电源
                    GlobalContext.popMessage("您爱车电源已打开",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                }else    if(name.equals(NAMES[7])){//蓝牙
                    GlobalContext.popMessage("您爱车和手机蓝牙已连接",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
                }
            }
        });
        if(!check){
            preArr = cacheArr;
            return;
        }
        //一定放最尾,检测连发二回
        boolean isSameData = true;
        if(cacheArr.size() != preArr.size())isSameData = false;
        if(isSameData){
            for(int i = 0;i<cacheArr.size();i++){
                if (!preArr.get(i).equals(cacheArr.get(i)))isSameData = false;
            }
        }
        if(!isSameData) {
            new TimeDelayTask().runTask(700, new TimeDelayTask.OnTimeEndListener() {
                @Override
                public void onTimeEnd() {
                    resetData();
                }
            });
        }
        preArr = cacheArr;
    }
}
