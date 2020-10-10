package com.mani.car.mekongk1.ui.personcenter.carmanager.carblue;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.personcenter.know_product.ActivityKnowProduct;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 蓝牙绑定解绑页面
 * 绑定蓝牙
 * 1进入页面后系统会自动搜索蓝牙设备号，2 点击返回 返回车辆管理页面
 * 3 点击下拉选项 用户根据说明书选择蓝牙设备号，选择后显示在选择框中，同时出现绑定确认按钮，再次点击下拉则再次启动搜索功能
 * <p>
 * 解绑蓝牙
 * 如果在没有绑定模组的情况下， 点击绑定蓝牙则 绿色提示框“请先绑定模组后再试”
 * 点击解绑后如果成功 则弹绿色提示框“解绑成功" 跳转到车辆管理页面 文本显示绑定蓝牙
 * 如果手机没有连接到车机蓝牙，则解绑失败 弹出提示框”没有接到车机蓝牙请靠近车辆后再试“
 */
@CreatePresenter(CarBluePresenter.class)
public class ActivityCarBlue extends BaseMvpActivity<CarBlueView, CarBluePresenter> implements CarBlueView,OEventObject {
    @BindView(R.id.title_head)
    ClipTitleHead title_head;
    @BindView(R.id.img_blue)
    ImageView     img_blue;
    @BindView(R.id.txt_blue)
    TextView      txt_blue;
    @BindView(R.id.img_drop_down)
    ImageView     img_drop_down;
    @BindView(R.id.btn_confirm)
    TextView      btn_confirm;
    @BindView(R.id.recycleview_blues)
    RecyclerView  recycleview_blues;
    @BindView(R.id.txt_tip)
    TextView      txt_tip;
    @BindView(R.id.txt_tip_content)
    TextView      txt_tip_content;
    @BindView(R.id.txt_koow_product)
    TextView      txt_koow_product;

    private static final String BIND_CN = "绑定";
    private static final String UNBIND_CN = "解绑";
    public static final String CARID = "CARID";
    private long useCarId = 0;//当前用的是哪辆车
    private boolean isForBind = false, isForUnBind = false;
    private BlueRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_blue);
        ButterKnife.bind(this);
        //    得到跳转到该Activity的Intent对象
        Intent intent = getIntent();
        useCarId = intent.getLongExtra("condition", 0);
        if(useCarId == 0){
            GlobalContext.popMessage("此车未激活", getResources().getColor(R.color.popTipWarning));
        }
        initViews();
        initEvents();
        //一进页面先扫
        DataCarInfo data = ManagerCarList.getInstance().getCarByID(useCarId);
        if (data == null) return;
        if(data.isBindBluetooth == 0){//未绑定
            getMvpPresenter().ScanBlue(getApplicationContext(),false);
        }else{//已绑定

        }
        ODispatcher.addEventListener(OEventName.BLUE_BOUND_CANCEL, this);
        ODispatcher.addEventListener(OEventName.BLUE_BOUND_FAILED, this);
        ODispatcher.addEventListener(OEventName.BLUE_BOUND_SUCESS, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.BLUE_BOUND_CANCEL, this);
        ODispatcher.removeEventListener(OEventName.BLUE_BOUND_FAILED, this);
        ODispatcher.removeEventListener(OEventName.BLUE_BOUND_SUCESS, this);
        super.onDestroy();
    }
    @Override
    public void initViews() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        recycleview_blues.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        recycleview_blues.setHasFixedSize(true);
        recycleview_blues.setItemAnimator(new DefaultItemAnimator());
        recycleview_blues.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this, 1), getResources().getColor(R.color.normal_txt_color_cyan)));
        //下划线
        //下划线
        txt_koow_product.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        DataCarInfo dataCarInfo = ManagerCarList.getInstance().getCarByID(useCarId);
        if (dataCarInfo.isBindBluetooth == 1) {
            img_blue.setImageResource(R.drawable.img_un_bind);
            img_drop_down.setVisibility(View.INVISIBLE);
            recycleview_blues.setVisibility(View.INVISIBLE);
            btn_confirm.setSelected(true);
            btn_confirm.setEnabled(true);
            txt_tip.setText(UNBIND_CN+"蓝牙");
            txt_tip_content.setText("解绑蓝牙后将无法使用手机蓝牙控制车辆。");
            btn_confirm.setText(UNBIND_CN);
            txt_blue.setText(dataCarInfo.num);
        } else {
            img_blue.setImageResource(R.drawable.img_blue_bind);
            img_drop_down.setVisibility(View.VISIBLE);
            btn_confirm.setSelected(false);
            txt_tip.setText(BIND_CN+"蓝牙");
            txt_tip_content.setText("打开手机蓝牙，在栏目框中，\n搜索说明书的蓝牙号点选绑定");
            btn_confirm.setText(BIND_CN);
            txt_blue.setText("");
        }
    }
    //等待扫描结束，以便显示
    private void clickCheckBlueListShow(){
//        if(BlueScanner.getInstance().getIsScanning()){
//            GlobalContext.popMessage("正在扫描，请稍等", getResources().getColor(R.color.popTipWarning));
//        }else if(myRecycleViewAdapter!=null){
//            if(recycleview_blues.getVisibility() == View.VISIBLE){
//                recycleview_blues.setVisibility(View.INVISIBLE);
//                img_drop_down.setSelected(false);
//            }else{
//                if(getMvpPresenter().preScanTime < System.currentTimeMillis() - 15*1000L){//超15秒重扫
//                    getMvpPresenter().ScanBlue(getApplicationContext(),true);
//                }else {
//                    recycleview_blues.setVisibility(View.VISIBLE);
//                    img_drop_down.setSelected(true);
//                }
//            }
//        }else{//没有数据，直接去扫
//            getMvpPresenter().ScanBlue(getApplicationContext(),true);
//        }
    }
    @Override
    public void initEvents() {
        txt_blue.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clickCheckBlueListShow();
            }
        });
        img_drop_down.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                clickCheckBlueListShow();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(useCarId == 0){
                    GlobalContext.popMessage("请先绑定模组后再试",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                isForBind = false;
                isForUnBind = false;
                //绑定操作
                if(BIND_CN.equals(btn_confirm.getText().toString().trim())){
                    isForBind = true;
                    String blueDeviceName = txt_blue.getText().toString();//名称有空格
                    if (blueDeviceName.length() == 0) {
                        GlobalContext.popMessage("蓝牙未输入名称",getResources().getColor(R.color.popTipWarning));
                        return;
                    }
//                    OCtrlBlueTooth.getInstance().ccmd1236_bluetoothBond_GetCarSin(useCarId, blueDeviceName);//先获取验证串，再绑定
                }else{//解绑操作
                    isForUnBind = true;
                    DataCarInfo data = ManagerCarList.getInstance().getCarByID(useCarId);
                    if (data.bluetoothName == null || data.bluetoothName.length() == 0) {
                        GlobalContext.popMessage("蓝牙无名称",getResources().getColor(R.color.popTipWarning));
                        return;
                    }
//                    BlueLinkNetSwitch.setSwitchNetModel(false, data.ide);
//                    if (BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
//                        if (BlueLinkReceiver.getUsedCarId() == data.ide) blueDiscoverOK();
//                        else
//                            BlueLinkReceiver.getInstance().needChangeCar(data.ide, data.bluetoothName, data.blueCarsig);
//                    } else {
//                        title_head.my_progress.begin();
//                        BlueLinkReceiver.getInstance().needChangeCar(data.ide, data.bluetoothName, data.blueCarsig);
//                    }
                }
            }
        });
        txt_koow_product.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityCarBlue.this, ActivityKnowProduct.class);
            }
        });
    }

    private static boolean isToastConfirmNormalShow = false;
    public void blueDiscoverOK() {
        if (isToastConfirmNormalShow) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
                //绑定页
                DataCarInfo data = ManagerCarList.getInstance().getCurrentCar();
                if (isForUnBind) {//已绑解绑
                    // 测试连接,成功后解绑，弹窗，已发验证串
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,true)
                            .withTitle("解绑蓝牙")
                            .withInfo("确定要解绑 " + data.bluetoothName + " 吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) {//发送解绑指令 01 01 02 FB
//                                        BlueLinkReceiver.getInstance().sendMessage("01 01 02 FB", true);//发送成功后才算是解绑成功
                                    } else {
                                        isForUnBind = false;
                                    }
                                    isToastConfirmNormalShow = false;
                                }
                            }).show();
                    isToastConfirmNormalShow = true;
                }
            }
        }).start();
    }
    public void onBlueMessageSended(byte[] bytes) {
//        if (bytes == null) return;
//        DataCarInfo data = ManagerCarList.getInstance().getCurrentCar();
//        if (isForBind) {//发验证串成功,绑定成功
//            isForBind = false;
//            byte[] bytesig = data.blueCarsig.getBytes();
//            byte[] mess    = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytesig);
//            if (ByteHelper.isByteEqual(mess, bytes)) {
//                title_head.my_progress.success();
//                GlobalContext.popMessage("蓝牙绑定成功",getResources().getColor(R.color.popTipNormal));
//                initViews();
//                ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);/**绑定解绑都发包改速度进入*/
//            }
//            //01,01,02,FB 解绑发包成功
//        } else if (bytes.length == 4 && bytes[0] == 1 && bytes[1] == 1 && bytes[2] == 2 && bytes[3] == ByteHelper.hexStringToBytes("FB")[0]) {
//            OCtrlBlueTooth.getInstance().ccmd1237_bluetoothUnBondOK(data.ide);
//            isForUnBind = false;
//            getMvpPresenter().clearScanedDevices();//解绑成功后清掉扫描的数据
//        }
    }
    //===========================================================
    @Override
    public void changeBlueAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myRecycleViewAdapter = new BlueRecycleViewAdapter(getMvpPresenter().getScanedDevicesNames(), ActivityCarBlue.this);
                recycleview_blues.setAdapter(myRecycleViewAdapter);
                myRecycleViewAdapter.setOnItemClickListener(new BlueRecycleViewAdapter.OnItemClickListener() {
                    @Override
                    public void onBlueNameSelect(View view, int position) {
                        recycleview_blues.setVisibility(View.INVISIBLE);
                        img_drop_down.setSelected(false);
                        txt_blue.setText(myRecycleViewAdapter.getData(position));
                        btn_confirm.setSelected(true);
                        btn_confirm.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObject) {
//        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//        if (eventName.equals(OEventName.BLUE_BOUND_FAILED)) {//绑定失败，已绑过
//            title_head.my_progress.failed();
//            BlueLinkReceiver.getInstance().closeBlueConnClearName("from BLUE_BOUND_FAILED");//断开连接
//        } else if (eventName.equals(OEventName.BLUE_BOUND_SUCESS)) {//连接后取了验证串
//            BlueLinkNetSwitch.setSwitchNetModel(false, useCarId);
//            BluetoothDevice devicebind = BlueGet.getDeviceFromList(carInfo.bluetoothName, getMvpPresenter().getScanedDevices());
//            if (devicebind == null || devicebind.getAddress() == null) return;
//            title_head.my_progress.begin();
//            BlueLinkReceiver.getInstance().needChangeCar(carInfo.ide, carInfo.bluetoothName, carInfo.blueCarsig);
//        } else if (eventName.equals(OEventName.BLUE_BOUND_CANCEL)) {// 解绑完成
//            title_head.my_progress.failed();
//            if (carInfo == null) return;
//            BlueLinkNetSwitch.setSwitchNetModel(true, carInfo.ide);
//            initViews();
//            GlobalContext.popMessage("解绑成功!",getResources().getColor(R.color.popTipNormal));
//            new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
//                @Override
//                public void onTimeEnd() {
//                    BlueLinkReceiver.getInstance().closeBlueConnClearName("from BLUE_BOUND_CANCEL解绑完成");//解绑完成,退出关蓝牙
//                }
//            });
//            ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);/**绑定解绑都发包改速度进入*/
//        }
    }
}
