package com.mani.car.mekongk1.ui.login;

import com.kulala.baseclass.BaseMvpPresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.loginreg.DataUser;

import java.util.List;

import static com.mani.car.mekongk1.common.GlobalContext.getContext;

public class LoginPresenter extends BaseMvpPresenter<LoginView>  implements OEventObject {
    private int timeCount;
    public void login(String phoneNum,String passWord) {
        ManagerLoginReg.loginPhoneNum=phoneNum;
        if(getMvpView()==null)return;
//        getMvpView().showLoading();
//        timeCount = 0;
//        OTimeSchedule.getInstance().init();
        if(!SystemMe.isNetworkConnected(GlobalContext.getContext())){
            getMvpView().showLoginResult("请检查是否有打开网络");
        }
//        ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, LoginPresenter.this);
        ODispatcher.addEventListener(OEventName.LOGIN_SUCCESS, LoginPresenter.this);
        ODispatcher.addEventListener(OEventName.LOGIN_FAILED, LoginPresenter.this);
//        Log.e("倒计时", "OnCount: "+"开始 5秒");
        OCtrlRegLogin.getInstance().CCMD_1103_login(phoneNum,passWord);
    }




    public void clickAgreeImg(boolean isClick) {
        if(getMvpView()==null)return;
        if(isClick){
            getMvpView().imgClickAgreeCancle();
        }else{
            getMvpView().imgClickAgreeOk();
        }
    }
    public void showUserList(boolean isShowUserList){
        if(getMvpView()==null)return;
        List<DataUser> userHistory= ManagerLoginReg.getInstance().getUserHistory();
        //從緩存列表中拿數據
        if(!isShowUserList){
            if(userHistory==null||userHistory.size()==0){
                getMvpView().recycleViewAndCenterImgHide();
            }else{
                getMvpView().recycleViewAndCenterImgShow(userHistory);
            }
        }else{
            getMvpView().recycleViewAndCenterImgHide();
        }

    }
    public  void clearUserName(){
        getMvpView().clearUserName();
    }
    public  void clearPassWord(){
        getMvpView().clearPassWord();
    }

    @Override
    public void receiveEvent(String s, Object o) {
//        if (OEventName.TIME_TICK_SECOND.equals( s)) {
//            timeCount++;
//            if (timeCount >= 5) {
//                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, LoginPresenter.this);
//                getMvpView().hideLoading();
//                if(!SystemMe.isNetworkConnected(GlobalContext.getContext())){
//                    getMvpView().showLoginResult("请检查是否有打开网络");
//                }
//            }
//        }else
            if (OEventName.LOGIN_SUCCESS.equals( s)) {
            ODBHelper.getInstance(getContext()).changeUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"CLOSETIPSKEY","FALSE");
            timeCount = 0;
            ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, LoginPresenter.this);
            ODispatcher.removeEventListener(OEventName.LOGIN_SUCCESS, LoginPresenter.this);
            ODispatcher.removeEventListener(OEventName.LOGIN_FAILED, LoginPresenter.this);
            if(getMvpView()==null)return;
//            getMvpView().hideLoading();
            getMvpView().toHomePage();
            getMvpView().showLoginResult("");
        }else if (OEventName.LOGIN_FAILED.equals( s)) {
            timeCount = 0;
            String error= (String) o;
//            ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, LoginPresenter.this);
            ODispatcher.removeEventListener(OEventName.LOGIN_SUCCESS, LoginPresenter.this);
            ODispatcher.removeEventListener(OEventName.LOGIN_FAILED, LoginPresenter.this);
            if(getMvpView()==null)return;
//            getMvpView().hideLoading();
            getMvpView().showLoginResult(error);
        }
    }
}
