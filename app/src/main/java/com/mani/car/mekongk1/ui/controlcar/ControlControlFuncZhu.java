package com.mani.car.mekongk1.ui.controlcar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.toast.OToastSharePath;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastConfirmNormalForAllClick;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.common.blue.BluePermission;
import com.mani.car.mekongk1.common.global.OWXShare;
import com.mani.car.mekongk1.ctrl.OCtrlAuthorization;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerControlButtonsZhu;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.tencent.tauth.TencentCommon;
import com.mani.car.mekongk1.ui.controlcar.area.ClipAreaView;
import com.mani.car.mekongk1.ui.controlcar.borrowcar.ActivityBorrowCarLongTime;
import com.mani.car.mekongk1.ui.controlcar.trajectory.ActivityTrajectory;
import com.mani.car.mekongk1.ui.home.ActivityHome;
import com.mani.car.mekongk1.ui.navifindcar.ActivityPCPosition;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.ActivityCarManager;
import com.mani.car.mekongk1.wxapi.WXEntryActivity;

import java.util.List;

import static com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll.STATE_CONNECT_OK;

public class ControlControlFuncZhu {
    // ========================out======================
    private static ControlControlFuncZhu _instance;

    public static ControlControlFuncZhu getInstance() {
        if (_instance == null)
            _instance = new ControlControlFuncZhu();
        return _instance;
    }

    // ========================out======================
    private long lastStartCarTime = 0;//上次点启动的时间,
    private long timeNoFast = 0;//上次点按扭的时间,不能太快

    public void controlButtonPress(int func, int isUse, final View parentView) {
        ODispatcher.dispatchEvent(OEventName.CLICK_FUNCBTN_HIDE_BUTTONS);
        SoundHelper.playSoundPool(parentView.getContext(), R.raw.voice_click_control_button);
        //控制不能点太快
        long now = System.currentTimeMillis();
        if (now - timeNoFast < 800L) return;
        timeNoFast = now;
        //控制不能点太快
        final DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
        final DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        List<Integer> arr = ManagerControlButtonsZhu.getInstance().getBtnsIdList();
//        if(func == arr.get(5)){//"功能开关"
//            //演示
//            if (user== null || user.userId == 0) {
//                GlobalContext.popMessage("请先点击左上角登录",parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                return;
//            }else if(currentCar.isActive == 0){
//                GlobalContext.popMessage("请先激活车辆!", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
//                return;
//            }
//            PopChangeButtons.getInstance().show(parentView);
//        }else
        if (func == 101) {//"启动" 1
            if (!com.kulala.staticsfunc.static_system.SystemMe.isNetworkConnected(GlobalContext.getContext()) && BlueAdapter.current_blue_state < STATE_CONNECT_OK) {
                GlobalContext.popMessage("请检查是否有打开网络", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            if (isUse == 1) {//已经启动
                if (ManagerCarList.getInstance().getCurrentCarStatus().isStart == 0) {//手动关电,
                    GlobalContext.popMessage("电瓶正在放电，请关闭电源或启动车辆!", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                } else {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                            .withInfo("您将要熄灭发动机，请确认车辆已安全停放好，车辆行驶中请勿操作。")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (!isClickConfirm) return;
                                    OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 2, 0);
                                }
                            }).show();
                }
            } else {
                ClipStartCarMinutes time = new ClipStartCarMinutes(GlobalContext.getContext(), null);
                time.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        ODipToPx.dipToPx(GlobalContext.getContext(), 70f)));
                ToastConfirmNormal toastConfirmNormal = new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE);
                if (ManagerCarList.getInstance().getCurrentCarStatus().isLock == 1) {//已锁车启动
                    toastConfirmNormal.withInfo("预约倒计时熄火后,如果车门开锁,预约将失效")
                            .withAddView(time);
                } else {//撤防状态下启动
                    toastConfirmNormal.withInfo("此次启动将无预约自动熄火,注意油量和防盗");
                }
                toastConfirmNormal.withClick(new ToastConfirmNormal.OnButtonClickListener() {
                    @Override
                    public void onClickConfirm(boolean isClickConfirm) {
                        if (!isClickConfirm) return;
                        long now = System.currentTimeMillis();//控制车启动未成功，要间隔十秒才能再点
                        if (now - lastStartCarTime > 10000) {
                            lastStartCarTime = now;
                            // time只有0到7档，每档5分钟，0档位5分钟
                            int time = (Integer) ClipStartCarMinutes.getLevel(ClipStartCarMinutes.selectValue);
                            OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 1, time);
                        } else {
                            GlobalContext.popMessage("请勿频繁点击!", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
                        }
                    }
                }).show();
            }
//            if(funName.equals("熄火")){//
//                if (currentCar.status.isStart == 0) {//手动关电,
//                    GlobalContext.popMessage("电瓶正在放电，请关闭电源或启动车辆!", parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                }else {
//                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(),null, ActivityHome.PAGE_IS_BLACKSTYLE)
//                            .withInfo("您将要熄灭发动机，请确认车辆已安全停放好，车辆行驶中请勿操作。")
//                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                                @Override
//                                public void onClickConfirm(boolean isClickConfirm) {
//                                    if(!isClickConfirm)return;
//                                    OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 2, 0);
//                                }
//                            }).show();
//                }
//            }else {
//                ClipStartCarMinutes time = new ClipStartCarMinutes(GlobalContext.getContext(), null);
//                time.setLayoutParams(new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.MATCH_PARENT,
//                        ODipToPx.dipToPx(GlobalContext.getContext(), 70f)));
//                ToastConfirmNormal toastConfirmNormal = new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE);
//                if (currentCar.status.isLock == 1) {//已锁车启动
//                    toastConfirmNormal.withInfo("预约倒计时熄火后,如果车门开锁,预约将失效")
//                            .withAddView(time);
//                } else {//撤防状态下启动
//                    toastConfirmNormal.withInfo("此次启动将无预约自动熄火,注意油量和防盗");
//                }
//                toastConfirmNormal.withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                    @Override
//                    public void onClickConfirm(boolean isClickConfirm) {
//                        if (!isClickConfirm) return;
//                        long now = System.currentTimeMillis();//控制车启动未成功，要间隔十秒才能再点
//                        if (now - lastStartCarTime > 10000) {
//                            lastStartCarTime = now;
//                            // time只有0到7档，每档5分钟，0档位5分钟
//                            int time = (Integer) ClipStartCarMinutes.getLevel(ClipStartCarMinutes.selectValue);
//                            OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 1, time);
//                        } else {
//                            GlobalContext.popMessage("请勿频繁点击!", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
//                        }
//                    }
//                }).show();
//            }
        } else if (func == 100) {//锁
            if (!com.kulala.staticsfunc.static_system.SystemMe.isNetworkConnected(GlobalContext.getContext()) && BlueAdapter.current_blue_state < STATE_CONNECT_OK) {
                GlobalContext.popMessage("请检查是否有打开网络", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 3, 0);
        } else if (func == 102) {//开
            if (!com.kulala.staticsfunc.static_system.SystemMe.isNetworkConnected(GlobalContext.getContext()) && BlueAdapter.current_blue_state < STATE_CONNECT_OK) {
                GlobalContext.popMessage("请检查是否有打开网络", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 4, 0);
        } else if (func == arr.get(1)) {//"尾箱"
            if (!com.kulala.staticsfunc.static_system.SystemMe.isNetworkConnected(GlobalContext.getContext()) && BlueAdapter.current_blue_state < STATE_CONNECT_OK) {
                GlobalContext.popMessage("请检查是否有打开网络", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                    .withInfo("确定要对尾箱操作吗？")
                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                        @Override
                        public void onClickConfirm(boolean isClickConfirm) {
                            if (!isClickConfirm) return;
                            OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 5, 0);//开关同一个
                        }
                    }).show();
        } else if (func == arr.get(0)) {//"双闪鸣笛" 寻车
            if (!com.kulala.staticsfunc.static_system.SystemMe.isNetworkConnected(GlobalContext.getContext()) && BlueAdapter.current_blue_state < STATE_CONNECT_OK) {
                GlobalContext.popMessage("请检查是否有打开网络", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            OCtrlCar.getInstance().ccmd1233_controlCar(currentCar, 6, 0);
        } else if (func == arr.get(4)) {//"电子围栏" 1-100km

            if (isUse == 1) {//不用确认，直接发
                //演示
                if (currentCar.isActive == 0) {
                    GlobalContext.popMessage("取消电子围栏成功!", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
                    ManagerControlButtonsZhu.getInstance().changeNoActiveArea(true);
                    return;
                }
                OCtrlCar.getInstance().ccmd1214_setArea(currentCar.ide, 0, 0);
            } else {
                final ClipAreaView areaView = new ClipAreaView(GlobalContext.getContext(), null);
                areaView.setSelectNum(1);
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                        .withAddView(areaView)
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm) {
                                    //演示
                                    if (currentCar.isActive == 0) {
                                        GlobalContext.popMessage("设置电子围栏成功!", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
                                        ManagerControlButtonsZhu.getInstance().changeNoActiveArea(false);
                                        return;
                                    }
                                    OCtrlCar.getInstance().ccmd1214_setArea(currentCar.ide, areaView.getSelectNum(), 1);
                                }
                            }
                        }).show();
            }
//            if(isUse.equals("取消围栏")){//不用确认，直接发
//                //演示
//                if(currentCar.isActive == 0){
//                    GlobalContext.popMessage("取消电子围栏成功!", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
//                    ManagerControlButtonsZhu.getInstance().changeNoActiveArea(true);
//                    return;
//                }
//                OCtrlCar.getInstance().ccmd1214_setArea(currentCar.ide, 0, 0);
//            }else {
//                final ClipAreaView areaView = new ClipAreaView(GlobalContext.getContext(), null);
//                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
//                        .withAddView(areaView)
//                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                            @Override
//                            public void onClickConfirm(boolean isClickConfirm) {
//                                if (isClickConfirm) {
//                                    //演示
//                                    if (currentCar.isActive == 0) {
//                                        GlobalContext.popMessage("设置电子围栏成功!", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
//                                        ManagerControlButtonsZhu.getInstance().changeNoActiveArea(false);
//                                        return;
//                                    }
//                                    OCtrlCar.getInstance().ccmd1214_setArea(currentCar.ide, areaView.getSelectNum(), 1);
//                                }
//                            }
//                        }).show();
//            }
        } else if (func == arr.get(7)) {//"切换车辆"
            if (user == null || user.userId == 0) {
                GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityCarManager.class);
        } else if (func == arr.get(2)) {//导航找车
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionLocation = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                //拍照权限
                if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                    GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                } else {
                    ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityPCPosition.class);
                }
            } else {
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityPCPosition.class);
            }

        } else if (func == arr.get(3)) {//信任借车
            ManagerPublicData.isBorrowCarLongTime=true;
            int authorityType = currentCar.authorityType;
            if (isUse==1) {//取消信任借车
                if (user == null || user.userId == 0) {
                    GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                } else if (currentCar.isActive == 0) {
                    GlobalContext.popMessage("请先激活车辆", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                }
                if(currentCar.isMyCar==0){
                    long carId = ManagerCarList.getInstance().getCurrentCarID();
                    if (carId == 0) return;
                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId, carId, 2);
                    return;
                }
                String info = "";
                String otherPhoneNum = currentCar.authorityPhone;
                long authorityEndTime = currentCar.authorityEndTime;
                long timeCache = authorityEndTime - System.currentTimeMillis();
                String days = (int) ((authorityEndTime - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)) + "";
                if (authorityType == 1) {
                    info = "当前车辆已经授权给了" + otherPhoneNum + "剩余" + days + "天";
                } else if (authorityType == 2) {
                    info = "当前车辆已经永久授权给了" + otherPhoneNum + "。";
                }
                new ToastConfirmNormalForAllClick(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                        .withInfo(info)
                        .withButton("取消当前授权", "借给其他人")
                        .withClick(new ToastConfirmNormalForAllClick.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(int isClickConfirm) {
                                if (isClickConfirm == 1) {
                                    //取消
                                    long carId = ManagerCarList.getInstance().getCurrentCarID();
                                    if (carId == 0) return;
                                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId, carId, 2);
                                } else if (isClickConfirm == 2) {
                                    //確定
                                    ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityBorrowCarLongTime.class);
                                }
                            }
                        })
                        .show();

            } else {//信任借车
                if (user == null || user.userId == 0) {
                    GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                } else if (currentCar.isActive == 0) {
                    GlobalContext.popMessage("请先激活车辆", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                }
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityBorrowCarLongTime.class);
            }
//            if(funName.equals("取消借车") || funName.equals("还车")){//取消借车
//                if (user== null || user.userId == 0) {
//                    GlobalContext.popMessage("请先点击左上角登录",parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                }else if (currentCar.isActive == 0) {
//                    GlobalContext.popMessage("请先激活车辆",parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                }
//                long carId=ManagerCarList.getInstance().getCurrentCarID();
//                if(carId==0)return;
//                OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId,carId);
//            }else {
//                if (user == null || user.userId == 0) {
//                    GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                } else if (currentCar.isActive == 0) {
//                    GlobalContext.popMessage("请先激活车辆", parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                }
//                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityBorrowCar.class);
//            }
        } else if (func == arr.get(8)) {//临时借车
            ManagerPublicData.isBorrowCarLongTime=false;
            ManagerPublicData.phoneNum="";
            if (isUse==1) {//取消临时借车
                if (user == null || user.userId == 0) {
                    GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                } else if (currentCar.isActive == 0) {
                    GlobalContext.popMessage("请先激活车辆", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                }
                int hours = (int) ((currentCar.authorityEndTime1 - System.currentTimeMillis()) / (60 * 60 * 1000));
                String info="";
                if(hours==0){
                    int minitues = (int) ((currentCar.authorityEndTime1 - System.currentTimeMillis()) / ( 60 * 1000));
                    info = "当前临时借车还剩" + minitues + "分钟。点击空白处退出。";
                }else{
                     info = "当前临时借车还剩" + hours + "小时。点击空白处退出。";
                }
                new ToastConfirmNormalForAllClick(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                        .withInfo(info)
                        .withButton("取消当前授权", "借给其他人")
                        .withInfoTxtSize(14)
                        .withClick(new ToastConfirmNormalForAllClick.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(int isClickConfirm) {
                                if (isClickConfirm == 2) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(100);
                                                GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        long carId = ManagerCarList.getInstance().getCurrentCarID();
                                                        if (carId == 0) return;
//                                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId,carId,1);
                                                        final ClipAreaView areaView = new ClipAreaView(GlobalContext.getContext(), null);
                                                        areaView.setSelectNum(1);
                                                        areaView.setText("设置临时授权", "小时后自动取消");
                                                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                                                                .withAddView(areaView)
                                                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                                                    @Override
                                                                    public void onClickConfirm(boolean isClickConfirm) {
                                                                        if (isClickConfirm) {
                                                                            long carId = ManagerCarList.getInstance().getCurrentCarID();
                                                                            if (carId == 0) return;
                                                                            JsonArray authoritys = new JsonArray();
                                                                            int authorityType1 = currentCar.authorityType1;
                                                                            int num = areaView.getSelectNum();
                                                                            if (authorityType1 == 0) {
                                                                                OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, "", System.currentTimeMillis(), System.currentTimeMillis() + areaView.getSelectNum() * 60 * 60 * 1000);
                                                                            } else {
                                                                                OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, "", System.currentTimeMillis(), System.currentTimeMillis() + areaView.getSelectNum() * 60 * 60 * 1000, 1);
                                                                            }
                                                                        }
                                                                    }
                                                                }).show();
                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                } else if (isClickConfirm == 1) {
                                    long carId = ManagerCarList.getInstance().getCurrentCarID();
                                    if (carId == 0) return;
                                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId, carId, 1);
                                }
                            }
                        })
                        .show();
            } else {//临时借车

                if (user == null || user.userId == 0) {
                    GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                } else if (currentCar.isActive == 0) {
                    GlobalContext.popMessage("请先激活车辆", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                    return;
                }
                final ClipAreaView areaView = new ClipAreaView(GlobalContext.getContext(), null);
                areaView.setSelectNum(1);
                areaView.setText("设置临时授权", "小时后自动取消");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                                    .withAddView(areaView)
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if (isClickConfirm) {
                                                long carId = ManagerCarList.getInstance().getCurrentCarID();
                                                if (carId == 0) return;
                                                int authorityType1 = currentCar.authorityType1;
                                                JsonArray authoritys = new JsonArray();
                                                int num1 = areaView.getSelectNum();
                                                if (authorityType1 == 0) {
                                                    OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, "", System.currentTimeMillis(), System.currentTimeMillis() + areaView.getSelectNum() * 60 * 60 * 1000);
                                                } else {
                                                    OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, "", System.currentTimeMillis(), System.currentTimeMillis() + areaView.getSelectNum() * 60 * 60 * 1000, 1);
                                                }
                                            }
                                        }
                                    }).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        } else if (func == arr.get(5)) {//行車軌跡
            if (user == null || user.userId == 0) {
                GlobalContext.popMessage("请先点击左上角登录", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            } else if (currentCar.isActive == 0) {
                GlobalContext.popMessage("请先激活车辆", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            if (currentCar.isMyCar == 0) {
                GlobalContext.popMessage("副车主没有权限", parentView.getContext().getResources().getColor(R.color.popTipWarning));
                return;
            }
            ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityTrajectory.class);
        } else if (func == arr.get(6)) {//分享APP
            OToastSharePath.getInstance().show(parentView, "分享下载", new OToastSharePath.OnClickButtonListener() {
                @Override
                public void onClick(int pos) {
                    if (pos == 1) {
//                            DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
//                            if(shareInfo ==null)return;
                        WXEntryActivity.NEED_WXSHARE_RESULT = true;
                        OWXShare.ShareFriendURL("么控K1", "下载地址", ManagerPublicData.shareDownLoadUrl);
                        ManagerPublicData.isNotPopGusture = true;
                    } else if (pos == 2) {
                        OWXShare.ShareTimeLineURL("么控K1", "下载地址", ManagerPublicData.shareDownLoadUrl);
                        ManagerPublicData.isNotPopGusture = true;
                    } else if (pos == 3) {
                        TencentCommon.toTencent(parentView.getContext(), "么控K1", "下载地址", ManagerPublicData.shareDownLoadUrl, 0, "");
                        ManagerPublicData.isNotPopGusture = true;
                    }
                }
            });
        }
//            else if(func == arr.get(11)){//蓝牙控车
//            if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK){//已经进入蓝牙，关闭
//                BlueAdapter.getInstance().closeBlueReal();
//            }else{//未进入蓝牙，开启
//                if (user== null || user.userId == 0) {
//                    GlobalContext.popMessage("请先点击左上角登录",parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                }else if (currentCar.isActive == 0) {
//                    GlobalContext.popMessage("请先激活车辆",parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                }else if (currentCar.isBindBluetooth == 0 || StringUtils.isNullOrEmpty(currentCar.bluetoothName) || StringUtils.isNullOrEmpty(currentCar.blueCarsig)) {
//                    GlobalContext.popMessage("请绑定模组后再试",parentView.getContext().getResources().getColor(R.color.popTipWarning));
//                    return;
//                }
//                //搜索并执行绑定蓝牙
//                int result = BluePermission.checkPermission(GlobalContext.getCurrentActivity());
//                switch (result) {
//                    case 6:BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());checkOpenBlueSecond(15*1000L);break;//请求打开蓝牙
//                    case 9:break;//打开权限
//                    case 1:confirmOpenBlueConn();break;//正常
//                }
//            }
//        }
    }

    /**
     * 检测是否蓝牙点了开启了
     */
    private CountDownTimerMy countDownTimerMy;
    private boolean isOpened = false;
    private int count = 0;

    private void checkOpenBlueSecond(long timeMs) {
        if (countDownTimerMy != null) countDownTimerMy.cancel();
        isOpened = false;
        countDownTimerMy = new CountDownTimerMy(timeMs, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("Func", "checkOpenBlueSecond on tick");
                if (isOpened) {//1秒后才能去搜，太快搜不到
//                    confirmOpenBlueConn();
                    countDownTimerMy.cancel();
                } else {
                    int result = BluePermission.checkPermission(GlobalContext.getCurrentActivity());
                    if (result == 1) isOpened = true;
                }
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimerMy.start();
    }


/**
 * 已经确认要开蓝牙连接了
 */
//    private boolean checkTimeForDevice = false;
//    private void confirmOpenBlueConn(){
//        checkTimeForDevice = false;
//        if(BlueScanner.getInstance().initializeOK(GlobalContext.getContext())){
//            BlueScanner.getInstance().setOnScanBlueListener(new BlueScanner.OnScanBlueListener() {
//                @Override
//                public void onScanedDevice(BluetoothDevice device) {
//                    checkTimeForDevice = true;
//                    Log.e("Func","BlueScanDevice:"+device.getName()+"  "+device.getAddress()+"  "+ManagerCarList.getInstance().getCurrentCar().carsig);
//                    if(BlueAdapter.getInstance().initializeOK(GlobalContext.getContext())){
//                        ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_BEGIN);
//                        BlueAdapter.getInstance().setOnBlueStateListener(onBlueStateListenerRoll);
//                        BlueAdapter.getInstance().gotoConnDevice(device);
//                    }
//                }
//
//                @Override
//                public void onScanedDeviceList(List<BluetoothDevice> deviceList) {
//
//                }
//
//                @Override
//                public void onScanStop() {
//                    BlueScanner.getInstance().setOnScanBlueListener(null);
//                    Log.e("Func","scanLeDevice: onScanStop");
//                    if(!checkTimeForDevice){
//                        GlobalContext.popMessage("蓝牙连接失败，请再次点击蓝牙控车按扭!",GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
//                    }
//                }
//            });
//            DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
//            BlueScanner.getInstance().scanLeDevice(true,currentCar.bluetoothName);
//            Log.e("Func","scanLeDevice:"+currentCar.bluetoothName);
//        }
//    }
//
//
//
//    private OnBlueStateListenerRoll onBlueStateListenerRoll = new OnBlueStateListenerRoll() {
//        @Override
//        public void onConnecting() {
//
//        }
//
//        @Override
//        public void onConnectedOK() {
//            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    BlueAdapter.getInstance().gotoDiscoverService();
//                }
//            });
//        }
//
//        @Override
//        public void onConnectedFailed(String error, boolean isNodevice) {
//            ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_FAIL);
//        }
//
//        @Override
//        public void onDiscovering() {
//
//        }
//
//        @Override
//        public void onDiscoverOK() {
//            TimerTask task = new TimerTask() {
//                public void run() {
//                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
//                    //有验证串的车,连接先发验证串
//                    if (currentCar != null && currentCar.blueCarsig != null && currentCar.blueCarsig.length() > 0) {
//                        byte[]       bytes = currentCar.blueCarsig.getBytes();
//                        final byte[] mess  = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytes);
//                        Log.e("blue", "onDiscoverOK sendmessage carsig:" +currentCar.blueCarsig+" "+ ByteHelper.bytesToHexString(mess));//不能UI线程，熄屏无法操作
//                        BlueAdapter.getInstance().sendMessage(ByteHelper.bytesToHexString(mess));
//                    }
//                }
//            };
//            Timer timer = new Timer();
//            timer.schedule(task, 200L);
//        }
//
//        @Override
//        public void onDiscoverFailed(String error, boolean isNoList) {
//            ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_FAIL);
//
//        }
//
//        @Override
//        public void onMessageSended(byte[] bytes) {
//            if (bytes == null) return;
//            String byteStr = ByteHelper.bytesToHexString(bytes);
//            Log.e("blue", "onMessageSended length:" + bytes.length + " " + byteStr);
//            if (bytes.length > 16) {//发验证串成功
//                TimerTask task = new TimerTask() {
//                    public void run() {
//                        BlueAdapter.getInstance().sendMessage("AA 02 55 0A F4");//连接成功!发送开启指令
//                    }
//                };
//                Timer timer = new Timer();
//                timer.schedule(task, 200L);
//            } else if (bytes.length == 5 && bytes[0] == ByteHelper.hexStringToBytes("AA")[0]//这是发送开启指令
//                    && bytes[1] == 2
//                    && bytes[2] == ByteHelper.hexStringToBytes("55")[0]
//                    && bytes[3] == ByteHelper.hexStringToBytes("0A")[0]
//                    && bytes[4] == ByteHelper.hexStringToBytes("F4")[0]) {
//                ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_SUCESS);
//            } else {
//                CarControlResult result1 = new CarControlResult();
//                result1.carId = ManagerCarList.getInstance().getCurrentCarID();
//                result1.currentProcess = CARCONTROL_SENDED;
//                result1.instruction = OCtrlCar.getInstance().getPreCmd();
//                result1.status = 1;
//                ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);
//                GlobalContext.popMessage("控车成功", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));//Color.parseColor("#90CF26")
//            }
//        }
//
//        @Override
//        public void onDataBack() {
//
//        }
//
//        @Override
//        public void onDataReceived(DataReceive data) {
//            if (data == null) return;
//            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//            if (data.dataType == 0x21) {
//                ManagerCarStatus.setData0x21(data.data, carInfo.ide);
//            } else if (data.dataType == 0x22) {
//                ManagerCarStatus.setData0x22(data.data, carInfo.ide);
//            }
//        }
//
//        @Override
//        public void onReadRemoteRssi(int rssi, int status) {
//            Log.e("blue", "读取到rssi:" + rssi + " status:" + status);
//        }
//
//        @Override
//        public void needLog(String log) {
//            Log.e("blue", "needLog:" + log);
//        }
//    };
}
