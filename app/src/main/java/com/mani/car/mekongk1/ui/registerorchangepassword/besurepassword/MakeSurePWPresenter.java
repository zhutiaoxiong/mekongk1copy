package com.mani.car.mekongk1.ui.registerorchangepassword.besurepassword;

import com.kulala.baseclass.BaseMvpPresenter;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;

public class MakeSurePWPresenter extends BaseMvpPresenter<MakeSurePWView> implements OEventObject{
    public  void  registerOrChangePassword(String phoneNum,String password,String verfyStr,String condition,String verfyCode){
        if(condition.equals("注册")){
            ODispatcher.addEventListener(OEventName.REGISTER_SUCCESS,this);
            if(verfyCode!=null&&!verfyCode.equals("")){
                OCtrlRegLogin.getInstance().CCMD_1102_Reg(phoneNum,password,verfyCode);
            }
        }else if(condition.equals("忘记密码")){
            ODispatcher.addEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS,this);
            OCtrlRegLogin.getInstance().CCMD_1117_resetPassword_from_phoneNum(phoneNum,verfyStr,password);
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.REGISTER_SUCCESS)) {
            if(getMvpView()==null)return;
            getMvpView().toActivityHome();
            getMvpView().showResult("");
            ODispatcher.removeEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS,this);
        }else if(s.equals(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS)){
            if(getMvpView()==null)return;
            String result= (String) o;
            if(result.equals("")){
                getMvpView().toActivityHome();
                getMvpView().showResult("");
            }else{
                getMvpView().showResult(result);
            }
            ODispatcher.removeEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS,this);
        }else if (s.equals(OEventName.REG_FAILED)) {
            if(getMvpView()==null)return;
            String result= (String) o;
            getMvpView().showResult(result);
            ODispatcher.removeEventListener(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS,this);
        }
    }
}
