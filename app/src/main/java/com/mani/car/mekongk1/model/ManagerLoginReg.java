package com.mani.car.mekongk1.model;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.PHeadHttp;
import com.mani.car.mekongk1.common.socketutils.BootBroadcastReceiver;
import com.mani.car.mekongk1.model.loginreg.DataUser;

import java.util.ArrayList;
import java.util.List;

public class ManagerLoginReg {
    private DataUser       user;
    private List<DataUser> userHistory;
    public static  String loginPhoneNum="";
    // ========================out======================
    private static ManagerLoginReg _instance;

    private ManagerLoginReg() {
    }

    public static ManagerLoginReg getInstance() {
        if (_instance == null)
            _instance = new ManagerLoginReg();
        return _instance;
    }

    public DataUser getCurrentUser() {
        if(user == null){
            //用户信息确定不要异步
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("user");
            JsonObject obj = ODBHelper.convertJsonObject(result);
            if (obj != null) {
                DataUser userr = DataUser.fromJsonObject(obj);
                if (userr.userId != 0) {
                    user = userr;
                    PHeadHttp.changeUserInfo(user.userId);//初始有用户就要改useid
                    BootBroadcastReceiver.initOrChangeSocket();
                }
            }
        }
        if(user == null)return new DataUser();
        return user;
    }

    public List<DataUser>  getUserHistory(){
        if(userHistory == null || userHistory.size() == 0){
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("userHistory");
                JsonArray arr = ODBHelper.convertJsonArray(result);
                if(arr==null)return null;
                 userHistory = DataUser.fromJsonArray(arr);
        }
        return userHistory;
    }

    // ========================out======================
//    public boolean checkIsFirstLogin() {
//        JsonObject obj = ODataShare.loadJsonObject(GlobalContext.getContext(), ODataShare.MODE_STATIC, "firstLogin");
//        if (obj == null) return false;
//        String isfist = OJsonGet.getString(obj, "firstLogin");
//        if (isfist.equals("true")) return true;
//        return false;
//    }
//
//    public void saveFristLogin() {
//        JsonObject obj = new JsonObject();
//        obj.addProperty("firstLogin", "true");
//        ODataShare.saveJsonObject(GlobalContext.getContext(), ODataShare.MODE_STATIC, "firstLogin", obj);
//    }

    //user为ＮＵＬＬ就是没登录
    public void exitLogin() {
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("user","");
//        PHeadHttp.changeUserInfo(0);
        if(user!=null)PHeadHttp.getInstance().changeUserToken(user.userId,"");
        user = null;
    }

    public void saveUserInfo(JsonObject userr) {
        if (userr == null)
            return;
        user = DataUser.fromJsonObject(userr);
        if (user == null)
            return;
        //用户信息改变，可能是重登录
//        PHeadHttp.changeUserInfo(user.userId);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("user",ODBHelper.convertString(this.user.toJsonObject()));
    }

//    public void saveUserInfoforSetQue(boolean hasQus) {
//        if (hasQus) {
//            if(user==null)return;
//            this.user.hasSecretQuestion = 1;
//        } else {
//            if(user==null)return;
//            this.user.hasSecretQuestion = 0;
//        }
//        ODataShare.saveJsonObject(GlobalContext.getContext(), ODataShare.MODE_PRIVATE, "user", this.user.toJsonObject());
//    }
    // ===========================================================
    public void putOneUserHistory(JsonObject userr) {
        if (userr == null)
            return;
        DataUser  serviceBackUser = DataUser.fromJsonObject(userr);
        if (serviceBackUser == null)
            return;
        if(userHistory == null)userHistory = new ArrayList<DataUser>();
        if(ManagerLoginReg.loginPhoneNum==null||ManagerLoginReg.loginPhoneNum.length()!=11)return;
        if(serviceBackUser.phoneNum.equals(""))return;
        DataUser historyUser=serviceBackUser;
        historyUser.phoneNum=ManagerLoginReg.loginPhoneNum;
        for(DataUser dataUser:userHistory){
            if(dataUser.phoneNum.equals(historyUser.phoneNum) || dataUser.userId == historyUser.userId){
                return;
            }
        }
        userHistory.add(historyUser);//没有就新增
        JsonArray arr = DataUser.toJsonArray(userHistory);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo( "userHistory", ODBHelper.convertString(arr));
    }
    public void clearOneUserHistory(String phoneNum) {
        if(phoneNum == null)return;
        if(userHistory == null||userHistory.size()==0)return;
        for(int i = 0;i<userHistory.size();i++){
            DataUser dataUser = userHistory.get(i);
            if(dataUser.phoneNum.equals(phoneNum) ){
                userHistory.remove(i);
                JsonArray arr = DataUser.toJsonArray(userHistory);
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo( "userHistory", ODBHelper.convertString(arr));
                return;
            }
        }
    }
    public DataUser getUserFromHistory(String phoneNum){
        if(phoneNum == null || phoneNum.equals(""))return null;
        if(userHistory == null)return null;
        for(int i = 0;i<userHistory.size();i++){
            DataUser dataUser = userHistory.get(i);
            if(dataUser.phoneNum.equals(phoneNum) ){
                return dataUser;
            }
        }
        return null;
    }
}
