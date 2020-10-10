package com.mani.car.mekongk1.ui.registerorchangepassword;

import com.kulala.baseclass.BaseMvpPresenter;
import com.mani.car.mekongk1.common.OTime60;
import com.mani.car.mekongk1.common.OTimeSchedule;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.mani.car.mekongk1.model.ManagerPublicData;

public class RigisterOrChangePassWordPresenter extends BaseMvpPresenter<RigisterOrChangePassWordView> implements OEventObject{
    private int countSuccsecc=0;
    private String condition;
    private String verfyCode;
    public void clickAgreeImg(boolean isClick) {
        if(getMvpView()==null)return;
        if(isClick){
            getMvpView().imgClickAgreeCancle();
        }else{
            getMvpView().imgClickAgreeOk();
        }
    }
    public void getVerfyCode(String phoneNum,int entrance){
        if(getMvpView()==null)return;
//        getMvpView().clearEditText();
        OTimeSchedule.getInstance().init();
        OTime60.getInstance().startTime();
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_BACKOK,this);
        OCtrlRegLogin.getInstance().CCMD_1101_getVerifyCode(phoneNum,entrance,1);
    }

    /***
     * 验证验证码
     * @param phoneNum 电话号码
     * @param verfyCode 验证码
     */
    public  void verify(String phoneNum,String verfyCode,int entrance,String condition){
        this.condition=condition;
        this.verfyCode=verfyCode;
        ODispatcher.addEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS,this);
        OCtrlRegLogin.getInstance().CCMD_1116_checkVerifycode(phoneNum,verfyCode,entrance);
    }
    public  void changePhoneNum(String phoneNum,String verfyCode,String  verfyStr){
        ODispatcher.addEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK,this);
        OCtrlRegLogin.getInstance().CCMD_1113_changePhoneNum(phoneNum,verfyCode,verfyStr);
    }
    public void jumpToMakeSurePassWordPage(){
        getMvpView().toMakeSurePassWordPage();
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (OEventName.VERIFICATION_CODE_BACKOK.equals( s)) {
            ODispatcher.removeEventListener(OEventName.VERIFICATION_CODE_BACKOK,this);
            String cacheError=(String)o;
            if(!cacheError.equals("")){
                countSuccsecc=0;
                getMvpView().setTipsText(cacheError);
                OTime60.getInstance().endTime();
            }else{
                countSuccsecc++;
                if(countSuccsecc>=2){
                    getMvpView().setTipsText("请检查手机是否欠费是否无网络？");
                }
            }
        }else if (OEventName.CHECK_VERIFYCODE_SUCCESS.equals( s)) {
            ODispatcher.removeEventListener(OEventName.CHECK_VERIFYCODE_SUCCESS,this);
            String cacheError=(String)o;
            if(cacheError.equals("")){
                if(condition.equals("更改账号旧手机号")){
                    getMvpView().changUI();
                }else{
                    if(condition.equals("注册")){
                        ManagerPublicData.verfyCode= verfyCode;
                    }
                    jumpToMakeSurePassWordPage();
                }
                    getMvpView().setTipsText("验证成功");
            }else{
                getMvpView().setTipsText(cacheError);
            }
        }else if (OEventName.CHANGE_PHONENUM_RESULTBACK.equals( s)) {
            ODispatcher.removeEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK,this);
            String cacheError=(String)o;
            if(cacheError.equals("")){
                    jumpToMakeSurePassWordPage();
                    getMvpView().setTipsText("更改成功");
            }else{
                getMvpView().setTipsText(cacheError);
            }
        }
    }
}
