package com.mani.car.mekongk1.ui.home.fragment.fragment1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kulala.baseclass.BaseMvpFragment;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.blue.BluePermission;
import com.mani.car.mekongk1.ctrl.OCtrlCheckCarState;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.ui.home.ActivityHome;

/**
 * Created by qq522414074 on 2017/4/26.
 * 对讲
 */
@CreatePresenter(Fragment1Presenter.class)
public class Fragment01 extends BaseMvpFragment<Fragment1View,Fragment1Presenter> {
    private boolean isNoOtherPage = true;//只有一个页面，划动无其它页面时，控车只在第一页，否则所有页面都要判断
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment01, container, false);
        return view;
    }
    //当前用户是否第一次登录,不提醒要带遥控器
    private void checkFirstLoginShowPop(){
//        long userId = ManagerLoginReg.getInstance().getCurrentUser().userId;
//        if(userId == 0)return;
//        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"CLOSETIPSKEY");
//        if(result == null || result.length() == 0 || !result.equals("TRUE")) {
//            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,true)
//                    .withTitle("重要提醒")
//                    .withInfo("无网络信号时,手机无法控制车辆,请务必携带遥控器用车!")
//                    .withButton("知道了", "下次不再提醒")
//                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                        @Override
//                        public void onClickConfirm(boolean isClickConfirm) {
//                            if(isClickConfirm){
//                                ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"CLOSETIPSKEY","TRUE");
//                            }
//                        }
//                    })
//                    .show();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ODispatcher.dispatchEvent(OEventName.FRAGMENT01_ON_STOP);
        OCtrlCheckCarState.getInstance().setNeedCheck(false,3);
    }

    @Override
    public void onResume() {
//        if(isNoOtherPage)checkFirstLoginShowPop();
        super.onResume();
        if(!SystemMe.isNetworkConnected(getContext()))GlobalContext.popMessage("请检查是否有打开网络",getResources().getColor(R.color.popTipWarning));
        DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
        //取消授權
        if(carInfo!=null){
            if(carInfo.ide!=0&&carInfo.isMyCar==1&&carInfo.isActive==1&&carInfo.authorityType1==1&&(carInfo.authorityEndTime1>System.currentTimeMillis())){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            GlobalContext.popMessage("您已把车临时借出去，请注意时间。",getResources().getColor(R.color.popTipWarning));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        OCtrlCheckCarState.getInstance().setNeedCheck(true,3);
        ODispatcher.dispatchEvent(OEventName.FRAGMENT01_ON_RESUME);
        //当打开APP或从后台到前台的条件下；当数据网络、WIFI，两者都关闭，或者 模组不在线的情况下，底部弹出提醒打开手机蓝牙的对话框，文本内容为“请靠近车辆打开手机蓝牙后控制车辆”【取消】、【确定】
        if(!SystemMe.isWifiOrNetEnable(getContext()) && carInfo.isBindBluetooth == 1){
            new ToastConfirmNormal(getActivity(),null, ActivityHome.PAGE_IS_BLACKSTYLE)
                    .withInfo("请靠近车辆打开手机蓝牙后控制车辆")
                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                        @Override
                        public void onClickConfirm(boolean isClickConfirm) {
                            if (isClickConfirm){
                                if(BluePermission.checkPermission(getActivity()) != 1){
                                    BluePermission.openBlueTooth(getActivity());
                                }
                            }
                        }
                    }).show();
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView("");
    }

    private void initView(String txt) {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerPublicData.isNotPopGusture =false;
    }
}
