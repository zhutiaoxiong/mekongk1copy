package com.mani.car.mekongk1.ctrl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mani.car.mekongk1.common.PHeadHttp;
import com.mani.car.mekongk1.common.socketutils.BootBroadcastReceiver;
import com.mani.car.mekongk1.model.DataBase;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.mani.car.mekongk1.model.ManagerGesture;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.links.http.HttpConn;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.OJsonGet;

/**
 * 100-299
 */
public class OCtrlRegLogin {
    // ========================out======================
    private static OCtrlRegLogin _instance;

    protected OCtrlRegLogin() {
    }

    public static OCtrlRegLogin getInstance() {
        if (_instance == null)
            _instance = new OCtrlRegLogin();
        return _instance;
    }

    // ========================out======================
    /**
     * status == 1 正常的回包,无错误
     **/
    public void processResult(int protocol, int status, JsonObject obj, String CACHE_ERROR) {
        switch (protocol) {
            case 1103:
                SCMD_1103_login(status, obj, CACHE_ERROR);
                break;
            case 1101:
                SCMD_1101_getVerficode(status, obj, CACHE_ERROR);
            case 1104:
                SCMD_1104_checkPassword(status, obj, CACHE_ERROR);
                break;
            case 1109:
                SCMD_1109_exitlogin(status, obj, CACHE_ERROR);
                break;
            case 1110:
                SCMD_1110_changeUserInfo(status, obj, CACHE_ERROR);
                break;
            case 1116:
                SCMD_1116_checkVerifycode(status, obj, CACHE_ERROR);
                break;
            case 1102:
                SCMD_1102_reg(status, obj, CACHE_ERROR);
            case 1113:
                SCMD_1113_changePhoneNum(status, obj, CACHE_ERROR);
                break;
            case 1117:
                SCMD_1117_resetPassword_from_phoneNum(status, obj, CACHE_ERROR);
            case 1128:
                backdata_1128_changePhoneNum(status, obj, CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================
    /**
     * 登陆
     **/
    public void CCMD_1103_login(String phoneNum, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("password", MD5.MD5generator(password));
        HttpConn.getInstance().request(1103, data);
    }
    /**
     * 注册信息提交
     **/
    public void  CCMD_1102_Reg(String phoneNum, String password, String verifyCode) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("password", MD5.MD5generator(password));
        data.addProperty("verifyCode", verifyCode);
        HttpConn.getInstance().request( 1102,data);
    }

    public void  CCMD_1104_checkPassword(String pass) {
        JsonObject data = new JsonObject();
        data.addProperty("password", MD5.MD5generator(pass));
        HttpConn.getInstance().request( 1104,data);
    }
    /**
     * 手机号重设密码
     **/
    public void  CCMD_1117_resetPassword_from_phoneNum(String phoneNum, String verifyStr, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyStr", verifyStr);
        data.addProperty("password", MD5.MD5generator(password));
        HttpConn.getInstance().request(1117,data);
    }

    /**
     * 获取服务端注册校验码,回包只有result entrance 1：注册，2：修改手机号，3：重设密码
     * entrance :1：注册，2：修改手机号，3：重设密码，4：修改邮箱，5：修改安全问题
     * type : 1：短信，2：语音
     **/
    public void CCMD_1101_getVerifyCode(String phoneNum, int entrance, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("entrance", entrance);
        data.addProperty("type", type);
        HttpConn.getInstance().request( 1101,data);
    }
    /**
     * 退出登录
     **/
    public void CCMD_1109_exitlogin() {
        HttpConn.getInstance().request(1109, null);
    }
    /**
     * 验证验证码 entrance 1：注册，2：修改手机号，3：重设密码
     **/
    /**
     * 改用户信息
     **/
    public void CCMD_1110_changeUserInfo(JsonObject userInfo) {
        JsonObject data = new JsonObject();
        data.add("userInfo", userInfo);
        HttpConn.getInstance().request( 1110,data);
    }
    /**
     * 修改手机号
     **/
    public void CCMD_1113_changePhoneNum(String phoneNum, String verifyCode, String verifyStr) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyCode", verifyCode);
        data.addProperty("verifyStr", verifyStr);
        HttpConn.getInstance().request( 1113,data);
    }
    public void CCMD_1116_checkVerifycode(String phoneNum, String verifyCode, int entrance) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyCode", verifyCode);
        data.addProperty("entrance", entrance);
        HttpConn.getInstance().request( 1116,data);
    }
    public void CCMD_1128_checkPhoneNum(String phoneNum) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        HttpConn.getInstance().request( 1128,data);
    }
    // ============================scmd==================================
    /**
     * 获取验证码
     */
    public void SCMD_1101_getVerficode(int status, JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            ODispatcher.dispatchEvent(OEventName.VERIFICATION_CODE_BACKOK);
//        }else{
//            ODispatcher.dispatchEvent(OEventName.VERIFICATION_CODE_BACKOK,CACHE_ERROR);
//        }
        ODispatcher.dispatchEvent(OEventName.VERIFICATION_CODE_BACKOK,CACHE_ERROR);
    }
    /**
     * 注册信息提交
     **/
    public void SCMD_1102_reg(int status,JsonObject obj,String CACHE_ERROR) {
        if (status==1) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            String token = OJsonGet.getString(obj, "token");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            DataUser user = DataUser.fromJsonObject(userInfo);
            if(user!=null)PHeadHttp.getInstance().changeUserToken(user.userId,token);
            ManagerCarList.getInstance().saveCarList(null,"SCMD_1102_reg");
            ODispatcher.dispatchEvent(OEventName.REGISTER_SUCCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
            //加入个人信息
//            OCtrlCommon.getInstance().ccmd1305_getSwitchInfo();
            //改用户ID
            BootBroadcastReceiver.initOrChangeSocket();
        } else {
            ODispatcher.dispatchEvent(OEventName.REG_FAILED,CACHE_ERROR);
        }
    }
    /**
     * 取登陆信息
     **/
    private void SCMD_1103_login(int status, JsonObject obj, String CACHE_ERROR) {
        if (status == 1){
            JsonObject result = OJsonGet.getJsonObject(obj, "result");
            JsonArray carInfos = OJsonGet.getJsonArray(obj, "carInfos");
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            String token = OJsonGet.getString(obj, "token");
            String watchToken = OJsonGet.getString(obj, "watchToken");
            int isFirst = OJsonGet.getInteger(obj, "isFirst");
            int isOpen = OJsonGet.getInteger(obj, "isOpen");//手势密码是否开启	0：关闭，1：开启
            String signPassword = OJsonGet.getString(obj, "signPassword");//用MD5加密，手势密码：从左到右，kulala_sign_ + 0-8
            if(isOpen==0){
                ManagerGesture.getInstance().saveGesture("关闭", signPassword);
            }else {
                ManagerGesture.getInstance().saveGesture("开启", signPassword);
            }

            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ManagerLoginReg.getInstance().putOneUserHistory(userInfo);
            DataUser user = (DataUser)DataBase.fromJsonObject(userInfo,DataUser.class);
            if(user!=null) PHeadHttp.getInstance().changeUserToken(user.userId,token);
            if(user!=null)PHeadHttp.getInstance().changeUserWatchToken(user.userId,watchToken);
            int hasMaintance = OJsonGet.getInteger(obj, "hasMaintance");
//            ManagerMaintainList.getInstance().savehasMaintance(hasMaintance);
            ManagerCarList.getInstance().saveCarList(carInfos,"backdata_1103_login");
            ManagerCarList.getInstance().checkBrowerCar();//重登录也要检测是否有借车
            ODispatcher.dispatchEvent(OEventName.LOGIN_SUCCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
            //重登录，重取皮肤表
            OCtrlCar.getInstance().ccmd1410_getSkinList(ManagerCarList.getInstance().getCurrentCarID());
        }else{
            ODispatcher.dispatchEvent(OEventName.LOGIN_FAILED,CACHE_ERROR);
        }
    }

    /**
     * 验证密码
     **/
    public void SCMD_1104_checkPassword(int status, JsonObject obj,String CACHE_ERROR) {
        if (status==1) {
            String verifyStr = OJsonGet.getString(obj, "verifyStr");
            ManagerPublicData.verfyStr = verifyStr;
            ODispatcher.dispatchEvent(OEventName.CHECK_PASSWORD_RESULTBACK, true);
        } else {
            ODispatcher.dispatchEvent(OEventName.CHECK_PASSWORD_RESULTBACK, false);
        }
    }

    /**
     * 退出登录
     **/
    public void SCMD_1109_exitlogin(int status, JsonObject obj,String CACHE_ERROR) {
        if (status==1) {
            ManagerCommon.getInstance().exitToLogin("");
        }
    }


    /**
     * 改用户信息
     **/
    public void SCMD_1110_changeUserInfo(int status, JsonObject obj,String CACHE_ERROR) {
        if (status==1) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_USER_INFO_OK,1);
        }else{
            ODispatcher.dispatchEvent(OEventName.CHANGE_USER_INFO_OK,2);
        }
    }
    /**
     * 修改手机号
     **/
    public void SCMD_1113_changePhoneNum(int status, JsonObject obj,String CACHE_ERROR) {
        if (status==1) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_PHONENUM_RESULTBACK,"");
        }else{
            ODispatcher.dispatchEvent(OEventName.CHANGE_PHONENUM_RESULTBACK,CACHE_ERROR);
        }
    }

    /**
     * 验证验证码 entrance 1：注册，2：修改手机号，3：重设密码
     **/
    public void SCMD_1116_checkVerifycode(int status, JsonObject obj, String CACHE_ERROR) {
            String verifyStr = OJsonGet.getString(obj, "verifyStr");
            if(verifyStr!=null&&!verifyStr.equals("")){
                ManagerPublicData.verfyStr=verifyStr;
            }
            ODispatcher.dispatchEvent(OEventName.CHECK_VERIFYCODE_SUCCESS,CACHE_ERROR);
    }
    /**
     * 手机号重设密码
     **/
    public void SCMD_1117_resetPassword_from_phoneNum(int status,JsonObject obj,String CACHE_ERROR) {
        if (status==1) {
            ODispatcher.dispatchEvent(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS,"");
        }else {
            ODispatcher.dispatchEvent(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS,CACHE_ERROR);
        }
    }
    /**
     * 修改手机号
     **/
    public void backdata_1113_changePhoneNum(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_PHONENUM_RESULTBACK);
        }
    }
    /**
     * 检查手机号是否存在
     **/
    public void backdata_1128_changePhoneNum(int status,JsonObject obj,String CACHE_ERROR) {
            ODispatcher.dispatchEvent(OEventName.CHANGE_PHONENUM_RESULTBACK,CACHE_ERROR);
    }
}
