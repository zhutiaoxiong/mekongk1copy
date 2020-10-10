package com.mani.car.mekongk1.ui.flash;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import com.kulala.baseclass.BaseMvpPresenter;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.model.ManagerSkins;

public class FlashPresenter extends BaseMvpPresenter<FlashView> {
    private String condition = "登录设置过手势密码";
    public static boolean isVerCheckResultBack = false;
    private Handler handler = new Handler();

    public void delayJump() {
        //解压默认皮肤成功后，才跳转
        ManagerSkins.getInstance().loadSkinByIdReal(GlobalContext.getContext(), 0,2,"car_lightfind", new ManagerSkins.OnLoadPngListener() {
            @Override
            public void loadCompleted(Drawable drawable) {
                ManagerSkins.getInstance().loadSkinByIdReal(GlobalContext.getContext(), 0,1,"car_lightfind", new ManagerSkins.OnLoadPngListener() {
                    @Override
                    public void loadCompleted(Drawable drawable) {
                        OCtrlCommon.getInstance().ccmd1302_getVersionInfo(0);
                        //三秒没回包就直接finish
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                jugeCondition();
                                if(isVerCheckResultBack)return;
                                getMvpView().jumpHomePage(condition);
                            }
                        }, 3000);
                    }
                    @Override
                    public void loadFail(String error) {
                        Log.e("Flash","load CAR Error"+error);
                    }
                });
            }
            @Override
            public void loadFail(String error) {
                Log.e("Flash","load SUV Error"+error);
            }
        });
    }

    /**
     * 判断条件,不用进手势密码
     */
//    private void jugeCondition() {
//        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
//        String adventUrl = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("adventUrl");
//        String token = PHeadHttp.getInstance().getToken();
//        if (user == null || user.userId == 0 || token == null || token.length() == 0) {
//            condition = "未注册未登录";
//        }else {
//            String isOpenGesture= ManagerGesture.getInstance().getIsOpenGesture();
//            if(isOpenGesture!=null){
//                if (isOpenGesture.equals("开启")) {
//                    condition = "登录设置过手势密码";
//                } else {
//                    condition = "登录未设置手势密码";
//                }
//            }
//        }
//        if (getMvpView() == null) return;
//        if(condition!=null){
//            if (condition.equals("登录设置过手势密码")) {
////            getMvpView().jumpGesturePasswordPage();
//            } else {
//                getMvpView().jumpHomePage(condition);
//            }
//        }
//    }
}
