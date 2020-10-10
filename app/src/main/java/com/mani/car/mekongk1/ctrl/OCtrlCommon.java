package com.mani.car.mekongk1.ctrl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.links.http.HttpConn;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerAlert;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.mani.car.mekongk1.model.ManagerDownloadVoice;

/**
 * 100-299
 */
public class OCtrlCommon {
    // ========================out======================
    private static OCtrlCommon _instance;

    protected OCtrlCommon() {
    }

    public static OCtrlCommon getInstance() {
        if (_instance == null)
            _instance = new OCtrlCommon();
        return _instance;
    }

    // ========================out======================
    /**
     * status == 1 正常的回包,无错误
     **/
    public void processResult(int protocol, int status, JsonObject obj, String CACHE_ERROR) {
        switch (protocol) {
            case 1001:
                backdata_1001_wxpay(status,obj,CACHE_ERROR);
                break;
            case 1002:
                backdata_1002_checkpay(status,obj,CACHE_ERROR);
                break;
            case 1004:
                backdata_1004_pifupay(status,obj,CACHE_ERROR);
                break;
            case 1005:
                backdata_1005_recharge_vouchers(status,obj,CACHE_ERROR);
                break;
//            case 1223:
//                backdata_1223_getLostNotifation(status,obj,CACHE_ERROR);
//                break;
//            case 1205:
//                backdata_1205_violationGetList(status,obj,CACHE_ERROR);
//                break;
            case 1302:
                backdata_1302_getVersionInfo(status,obj,CACHE_ERROR);
                break;
            case 1303:
                backdata_1303_getCommonInfo(status,obj,CACHE_ERROR);
                break;
            case 1305:
                backdata_1305_getSwitchInfo(status,obj,CACHE_ERROR);
                break;
//            case 1306://不需要处理,是没有钥一钥列表的
//                backdata_1306_changeSwitch(status,obj,CACHE_ERROR);
//                break;
//            case 1309:
//                backdata_1309_chatlist(status,obj,CACHE_ERROR);
//                break;
            case 1310:
                backdata_1310_getBrandList(status,obj,CACHE_ERROR);
                break;
            case 1312:
                backdata_1312getuplloadPic(status,obj,CACHE_ERROR);
                break;
            case 1313:
                backdata_1313_sendSuggest(status,obj,CACHE_ERROR);
                break;
//            case 1315:
//                backdata_1315_setSwitchShakeOpen(status,obj,CACHE_ERROR);
//                break;
            case 1316:
                backdata_1316_nokeyin_set_signal(status,obj,CACHE_ERROR);
                break;
            case 1318:
                backdata_1318_online_message(status,obj,CACHE_ERROR);
                break;
            case 1319:
                backdata_1319_getAlertList(status,obj,CACHE_ERROR);
                break;
            case 1320:
                backdata_1320_ChangeAlertStatus(status,obj,CACHE_ERROR);
                break;
            case 1321:
                backdata_1321_get_downLoad_VoiceUrls(status,obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    // ============================ccmd==================================

    /**
     * 取支付宝信息
     **/
    public void ccmd1001_wxpay(float fee, long carId, int time) {
        JsonObject data = new JsonObject();
        data.addProperty("hasSdk", 1);
        data.addProperty("fee", fee);
        data.addProperty("carId", carId);
        data.addProperty("time", time);
        HttpConn.getInstance().sendMessage(data, 1001);
    }


    /**
     * 取支付宝信息
     **/
    public void ccmd1002_checkpay(float fee, long carId, int time) {
        JsonObject data = new JsonObject();
        data.addProperty("hasSdk", 1);
        data.addProperty("fee", fee);
        data.addProperty("carId", carId);
        data.addProperty("time", time);
        HttpConn.getInstance().sendMessage(data, 1002);
    }
    /**
     * 皮肤支付
     **/
    public void ccmd1004_pifupay(float fee, int carTypeId, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("hasSdk", 1);
        data.addProperty("fee", fee);
        data.addProperty("carTypeId", carTypeId);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1004);
    }
    /**
     *充值卷充值（1005）
     请求参数（封装到data参数中上传）
     名称 类型 说明 备注
     phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     code String 充值卷码 必填
     carId long 车辆id 必填
     下发数据参数
     carInfo jsonObject 汽车详情 具体见：汽车详情对象
     **/
    public void ccmd_1005_recharge_vouchers(String code, long carId) {
        JsonObject data = new JsonObject();
        data.addProperty("code", code);
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 1005);
    }

    /**
     * 违章查询
     **/
    public void ccmd1205_violationGetList(long carId,int isDemo) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        HttpConn.getInstance().sendMessage(data, 1205);
    }

    /**
     * 取版本信息
     * @param type 0：首页发送，1：设置界面发送
     **/
    public void ccmd1302_getVersionInfo(int type) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1302);
    }

    /**
     * 判断是否过期，需要重新登录
     **/
    public void ccmd1115_getLoginState() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1115);
    }

    /**
     * 汽车终止授权，授权和围栏通知
     **/
    public void ccmd1223_getLostNotifation() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1223);
    }

    /**
     * 取品牌列表
     **/
    public void ccmd1310_getBrandList(long lastUpdateTime) {
        JsonObject data = new JsonObject();
        data.addProperty("lastUpdateTime", lastUpdateTime);
        HttpConn.getInstance().sendMessage(data, 1310);
    }

    /**
     * 取通用信息
     **/
    public void ccmd1303_getCommonInfo() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1303);
    }

    /**
     * 取开关列表
     **/
    public void ccmd1305_getSwitchInfo() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1305);
    }

    /**
     * 修改开关列表
     **/
    public void ccmd1306_changeSwitch(int noticeId, boolean isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("noticeId", noticeId);
        data.addProperty("isOpen", isOpen ? 1 : 0);
        HttpConn.getInstance().sendMessage(data, 1306);
    }

    /**
     * 取聊天列表
     **/
    public void ccmd1309_chatlist() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1309);
    }
    /**
     * 投诉建议接口
     **/
    public void ccmd1313_sendSuggest(int type,String comment) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        data.addProperty("comment", comment);
        HttpConn.getInstance().sendMessage(data, 1313);
    }
    /**
     * 解除警报消息推送 （1314）
     **/
    public void ccmd1314_isGetMessage(long carId,int alertType) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("alertType", alertType);
        HttpConn.getInstance().sendMessage(data, 1314);
    }
    /**
     * 摇一摇开关
     **/
    public void ccmd1315_setSwitchShakeOpen(long carId,boolean isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("isOpen", isOpen ? 1 : 0);
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 1315);
    }

    public void ccmd1318_OnlineMessage(String name, String phone,String city ,String brand ,String series, String time) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("phone", phone);
        data.addProperty("city", city);
        data.addProperty("brand", brand);
        data.addProperty("series", series);
        data.addProperty("time", time);
        HttpConn.getInstance().sendMessage(data, 1318);
    }
    public void ccmd1319_getAlertList() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1319);
    }
    /**noticeId
     int
     控制开关id
     必填，具体见推送开关对象的ide
     isOpen
     int
     是否开启
     必填，0：关闭，1：开启*/
    public void ccmd1320_ChangeAlertStatus(int noticeId ,int isOpen ) {
        JsonObject data = new JsonObject();
        data.addProperty("noticeId", noticeId);
        data.addProperty("isOpen", isOpen);
        HttpConn.getInstance().sendMessage(data, 1320);
    }
    /**
     * 信号值设定 （1316）
     * @param noticeId 开关id
     * @param signalValue 信号值
     **/
    public void ccmd1316_nokeyin_set_signal(int noticeId, double signalValue) {
        JsonObject data = new JsonObject();
        data.addProperty("noticeId", noticeId);
        data.addProperty("signalValue", signalValue);
        HttpConn.getInstance().sendMessage(data, 1316);
    }
    // ============================scmd==================================


    /**
     * 请求上传千牛服务器的token
     */
    public  void cmmd1312_uplloadPic(){
        JsonObject data=new JsonObject();
        HttpConn.getInstance().sendMessage(data,1312);
    }
    /**
     * phead
     jsonObject
     信息头
     必填，具体见协议框架中的请求头信息
     type
     int
     类型
     必填：1：我要投诉；2：商务合作；3：软件功能
     4：硬件质量；5：售后服务；6：硬件价格
     comment
     String
     建议文字
     必填
     picUrls
     String
     图片地址
     必填，地址用#号分割
     */

    public  void ccmd1313_sendSuggest(int type,String comment,String picUrls){
        JsonObject data=new JsonObject();
        data.addProperty("type", type);
        data.addProperty("comment", comment);
        data.addProperty("picUrls", picUrls);
        HttpConn.getInstance().sendMessage(data,1313);
    }

    public  void ccmd1321_get_downLoad_VoiceUrls(){
        JsonObject data=new JsonObject();
        HttpConn.getInstance().sendMessage(data,1321);
    }

    /**
     * 取微信支付信息
     **/
    private void backdata_1001_wxpay(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            JsonObject tenpayParam = OJsonGet.getJsonObject(obj, "tenpayParam");
            ManagerCommon.getInstance().saveWxPay(tenpayParam);
            ODispatcher.dispatchEvent(OEventName.PAY_WX_RESULTBACK);
        }
    }

    /**
     * 取支付宝信息
     **/
    private void backdata_1002_checkpay(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            String paystr = OJsonGet.getString(obj, "paystr");
            ODispatcher.dispatchEvent(OEventName.PAY_CHECKPAY_RESULTBACK, paystr);
        }
    }
    /**
     * 取支付宝信息
     **/
    private void backdata_1004_pifupay(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            ODispatcher.dispatchEvent(OEventName.SKIN_PAY_RESULTBACK, obj);
        }
    }
    /**
     * 取充值券信息
     **/
    private void backdata_1005_recharge_vouchers(int status,JsonObject obj,String CACHE_ERROR) {
            if (status == 1) {
                ManagerCarList.getInstance().saveCarInfo(OJsonGet.getJsonObject(obj,"carInfo"),"from_1005");
                ODispatcher.dispatchEvent(OEventName.VECHARGE_VOUCHERS_RESULTBACK);
            }
    }


    /**
     * 取品牌列表
     **/
    public void backdata_1310_getBrandList(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            long      updateTime = OJsonGet.getLong(obj, "updateTime");
            JsonArray arr        = OJsonGet.getJsonArray(obj, "brands");
            ManagerCommon.getInstance().saveBrandList(arr, updateTime);
            ODispatcher.dispatchEvent(OEventName.COMMON_RESULTBACK);
        }
    }

    /**
     * 取通用信息
     **/
    private void backdata_1303_getCommonInfo(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            ManagerCommon.getInstance().saveAuthorList(OJsonGet.getJsonArray(obj, "authors"));
            ManagerCommon.getInstance().saveBrandList(OJsonGet.getJsonArray(obj, "brands"),System.currentTimeMillis());
            ManagerCommon.getInstance().savePayWay(OJsonGet.getJsonArray(obj, "onlinePayInfos"));
            ManagerCommon.getInstance().saveContact(obj);
            ManagerCommon.getInstance().saveShare(obj);
            ManagerCommon.hotLine = OJsonGet.getString(obj, "hotLine");
            ManagerCommon.email = OJsonGet.getString(obj, "email");
            ManagerCommon.dealerLine = OJsonGet.getString(obj, "dealerLine");
            JsonObject trackShareObj=OJsonGet.getJsonObject(obj,"trackShareObj");
//            ManagerSkins.getInstance().saveFlash(OJsonGet.getString(obj, "splashAddress"));
            ManagerCommon.getInstance().saveTrackShareObj(trackShareObj);
            JsonObject adventInfoObj=OJsonGet.getJsonObject(obj,"adventInfo");
            if(adventInfoObj!=null){
                ManagerCommon.getInstance().saveDataAdvertising(adventInfoObj);
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventInfo",ODBHelper.convertString(adventInfoObj));
                if(ManagerCommon.getInstance().dataAdvertising!=null&&!TextUtils.isEmpty(ManagerCommon.getInstance().dataAdvertising.url)){
//                    ManagerSkins.getInstance().saveAdvent(ManagerCommon.getInstance().dataAdvertising.url);
                    ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventUrl",ManagerCommon.getInstance().dataAdvertising.url);
                }
            }else{
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventUrl","");
            }
            ODispatcher.dispatchEvent(OEventName.COMMON_RESULTBACK);
        }
    }

    /**
     * 违章查询
     **/
//    private void backdata_1205_violationGetList(int status,JsonObject obj,String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray illegalInfos = OJsonGet.getJsonArray(obj, "illegalInfos");
//            ManagerCommon.getInstance().saveViolationList(illegalInfos);
//            ODispatcher.dispatchEvent(OEventName.VIOLATION_LIST_BACK);
//        }
//    }

    /**
     * 取开关列表
     **/
    private void backdata_1305_getSwitchInfo(int status,JsonObject obj,String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray ctrlNoticeInfos   = OJsonGet.getJsonArray(obj, "ctrlNoticeInfos");
//            JsonArray alertNoticeInfos  = OJsonGet.getJsonArray(obj, "alertNoticeInfos");
//            JsonArray safeNoticeInfos   = OJsonGet.getJsonArray(obj, "safeNoticeInfos");
//            JsonArray secretNoticeInfos = OJsonGet.getJsonArray(obj, "secretNoticeInfos");
////            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
////            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
//            JsonArray watchNoticeInfos = OJsonGet.getJsonArray(obj, "watchNoticeInfos");
//            String watchToken = OJsonGet.getString(obj, "watchToken");
//            PHeadHttp.changeUserWatchToken(ManagerLoginReg.getInstance().getCurrentUser().userId,watchToken);
//            Log.e("watchToken","switchback:"+watchToken);
//            ManagerSwitchs.getInstance().saveSwitchControls(ctrlNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchWarnings(alertNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchSafetys(safeNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchPrivates(secretNoticeInfos);
////            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
////            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchWear(watchNoticeInfos);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_ALL_RESULTBACK);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_WARNINGS_RESULTBACK);
////            ODispatcher.dispatchEvent(OEventName.SWITCH_NOKEYS_RESULTBACK);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_WEARS_RESULTBACK);
////            ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);//刷新me界面
//        }
    }
    /**
     * 修改开关列表
     **/
//    private void backdata_1306_changeSwitch(int status,JsonObject obj,String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray ctrlNoticeInfos   = OJsonGet.getJsonArray(obj, "ctrlNoticeInfos");
//            JsonArray alertNoticeInfos  = OJsonGet.getJsonArray(obj, "alertNoticeInfos");
//            JsonArray safeNoticeInfos   = OJsonGet.getJsonArray(obj, "safeNoticeInfos");
//            JsonArray secretNoticeInfos = OJsonGet.getJsonArray(obj, "secretNoticeInfos");
////            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
//            JsonArray watchNoticeInfos = OJsonGet.getJsonArray(obj, "watchNoticeInfos");
//            String watchToken = OJsonGet.getString(obj, "watchToken");
//            PHeadHttp.changeUserWatchToken(ManagerLoginReg.getInstance().getCurrentUser().userId,watchToken);
//            Log.e("watchToken","switchback:"+watchToken);
//            ManagerSwitchs.getInstance().saveSwitchControls(ctrlNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchWarnings(alertNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchSafetys(safeNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchPrivates(secretNoticeInfos);
////            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchWear(watchNoticeInfos);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_WARNINGS_RESULTBACK);
////            ODispatcher.dispatchEvent(OEventName.SWITCH_NOKEYS_RESULTBACK);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_WEARS_RESULTBACK);
//        }
//    }
    /**
     * 取聊天列表
     **/
//    private void backdata_1309_chatlist(int status,JsonObject obj,String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray msgInfos = OJsonGet.getJsonArray(obj, "msgInfos");
//            ManagerChat.getInstance().saveChatList(msgInfos);
//            ODispatcher.dispatchEvent(OEventName.CHAT_INFO_BACK);
//        }
//
//    }

    /**
     * 汽车终止授权，授权和围栏通知
     **/
//    private void backdata_1223_getLostNotifation(int status,JsonObject obj,String CACHE_ERROR) {
//        if (status == 1) {
//            //电子围栏处理消息
//            JsonObject   radiusMsgs = OJsonGet.getJsonObject(obj, "radiusMsgs");
//            DataWarnings war        = DataWarnings.fromJsonObject(radiusMsgs);
//            if (war != null && war.alertId == 17) {
//                ActivityKulalaMain.areaWar = war;//不能线程内用handler
//                ODispatcher.dispatchEvent(OEventName.GLOBAL_NEED_CANCEL_AREA);
//                ManagerWarnings.getInstance().saveNewWarnings(radiusMsgs);
//            }
//            //终止授权，同意授权消息
//            JsonObject authorityMsgs = OJsonGet.getJsonObject(obj, "authorityMsgs");
//            //保养到期消息
//            JsonObject maintenanceMsg=OJsonGet.getJsonObject(obj,"maintenanceMsg");
//            ManagerMaintainList.getInstance().maintain= DataMaintain.fromJsonObject(maintenanceMsg);
//            if(ManagerMaintainList.getInstance().maintain!=null){
//                ODispatcher.dispatchEvent(OEventName.MAINTAIN_MESSAGEBACK);
//            }
//            //紧急消息
//            JsonObject urgentMsgs = OJsonGet.getJsonObject(obj, "urgentMsgs");
//            if (urgentMsgs != null) {
//                ManagerCommon.getInstance().saveMessageUserList(urgentMsgs);
//                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null)
//                        .withTitle("提示")
//                        .withInfo(ManagerCommon.getInstance().messageUserList.content)
//                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                            @Override
//                            public void onClickConfirm(boolean isClickConfirm) {
////                                if (isClickConfirm)ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_message_user);
//                            }
//                        }).show();
//            }
//            //年检弹窗消息
//            JsonObject annualMsg = OJsonGet.getJsonObject(obj, "annualMsg");
//            if (annualMsg != null) {
//                int alertType = OJsonGet.getInteger(annualMsg, "alertType");//弹框类型 1：过期提醒 2：其他弹窗
//                final String alertContent = OJsonGet.getString(annualMsg, "alertContent");//弹窗内容
//                final String comment = OJsonGet.getString(annualMsg, "comment");//备注
//                long dueTime = OJsonGet.getLong(annualMsg, "dueTime");//提醒消息过期时间
//
//                new TimeDelayTask().runTask(2000L, new TimeDelayTask.OnTimeEndListener() {
//                    @Override
//                    public void onTimeEnd() {
//                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null)
//                                .withTitle(alertContent)
//                                .withInfo(comment)
//                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                                    @Override
//                                    public void onClickConfirm(boolean isClickConfirm) {
//                                        if(!isClickConfirm)return;
////                                        ManagerAnnual.getInstance().saveCarActiveList(ManagerCarList.getInstance().getCarAnnualList());
////                                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_annual_reminder_main);
//                                    }
//                                }).show();
//
//                    }
//                });
//            }
//        }
//    }

    /**
     * 取版本信息
     **/
    private void backdata_1302_getVersionInfo(int status,JsonObject obj,String CACHE_ERROR) {
////        if (status == 1) {
        Log.i("1302",obj.toString());
        ODispatcher.dispatchEvent(OEventName.GETVERSIONINFO_RESULTBACK, obj);
//        }
    }

    /**
     * 取上传头像到千牛的token
     */
    private void backdata_1312getuplloadPic(int status,JsonObject obj,String CACHE_ERROR){
        if(status == 1){
            ODispatcher.dispatchEvent(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK,obj);
        }
    }

    /**
     * 投诉建议接口
     **/
    private void backdata_1313_sendSuggest(int status,JsonObject obj,String CACHE_ERROR){
        if(status == 1){
            ODispatcher.dispatchEvent(OEventName.SUGGEST_HTTP_RESULTBACK,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.SUGGEST_HTTP_RESULTBACK,false);
        }
    }
    /**
     * 摇一摇开关
     **/
//    public void backdata_1315_setSwitchShakeOpen(int status,JsonObject obj,String CACHE_ERROR) {
//        if (status == 1) {
//            //2018/08/09承车显示摇
////            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
//            ManagerCarList.getInstance().saveCarInfo(OJsonGet.getJsonObject(obj,"carInfo"),"from_1315");
////            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
//            if(ViewSwitchShake.viewSwitchShakeThis!=null) ViewSwitchShake.viewSwitchShakeThis.handlerChangeSwitch();
//            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//            BlueLinkReceiver.needChangeCar(carInfo.ide,carInfo.bluetoothName,carInfo.blueCarsig);
//        }
//
//    }
    /**
     * 信号值设定 （1316）
     **/
    public void backdata_1316_nokeyin_set_signal(int status,JsonObject obj,String CACHE_ERROR) {
        if (status == 1) {
            //成功
//            ODispatcher.dispatchEvent(OEventName.SWITCH_NOKEYS_SETVALUE_OK);
        }

    }
    /**
     * 信号值设定 （1318）
     **/
    public void backdata_1318_online_message(int status,JsonObject obj,String CACHE_ERROR) {
        if(status == 1) {
            //成功
            ODispatcher.dispatchEvent(OEventName.ONLINE_MESSAGE_RESULT, CACHE_ERROR);
        }
    }
    /**
     * 信号值设定 （1318）
     **/
    public void backdata_1319_getAlertList(int status,JsonObject obj,String CACHE_ERROR) {
        if(status == 1){
            JsonArray alertInfos = OJsonGet.getJsonArray(obj, "alertInfos");
            ManagerAlert.getInstance().saveAlertList(alertInfos);
            ODispatcher.dispatchEvent(OEventName.ALERT_LIST_RESULT_BACK);
        }
    }
    /**
     * 信号值设定 （1318）
     **/
    public void backdata_1320_ChangeAlertStatus(int status,JsonObject obj,String CACHE_ERROR) {
        if(status == 1){
            JsonArray alertInfos = OJsonGet.getJsonArray(obj, "alertInfos");
            ManagerAlert.getInstance().saveAlertList(alertInfos);
            ODispatcher.dispatchEvent(OEventName.ALERT_LIST_RESULT_BACK);
        }
    }
    /**
     * 信号值设定 （1318）
     **/
    public void backdata_1321_get_downLoad_VoiceUrls(int status,JsonObject obj,String CACHE_ERROR) {
        if(status == 1){
            JsonArray sounds = OJsonGet.getJsonArray(obj, "sounds");
            ManagerDownloadVoice.getInstance().saveDownLoadVoiceListInfo(sounds);
            ODispatcher.dispatchEvent(OEventName.VOICE_LIST_RESULT_BACK);
        }
    }
}
