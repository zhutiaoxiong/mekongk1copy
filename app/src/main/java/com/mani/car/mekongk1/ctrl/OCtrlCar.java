package com.mani.car.mekongk1.ctrl;

import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.links.http.HttpConn;
import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.kulala.staticsfunc.static_system.AES;
import com.kulala.staticsfunc.static_system.NotificationUtils;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.SoundAlert;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.common.blue.BlueStaticValue;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerCurrentCarInfo;
import com.mani.car.mekongk1.model.ManagerSkins;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;

import static com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll.STATE_CONNECT_OK;
import static com.mani.car.mekongk1.ctrl.CarControlResult.CARCONTROL_SENDED;

/**
 * 100-299
 */
public class OCtrlCar {
    private static long carId1219;
    // ========================out======================
    private static OCtrlCar _instance;

    public static OCtrlCar getInstance() {
        if (_instance == null)
            _instance = new OCtrlCar();
        return _instance;
    }

    // ========================out======================
    public void processResult(int protocol, int status, JsonObject obj, String CACHE_ERROR) {
        switch (protocol) {
            case 1201:
                backdata_1201_newrepairCar(status,obj, CACHE_ERROR);
                break;
            case 1202:
                backdata_1202_deleteCar(status,obj, CACHE_ERROR);
                break;
            case 1203:
                backdata_1203_getcarlist(status, obj, CACHE_ERROR);
                break;
            case 1204:
                backdata_1204_activatecar(status,obj, CACHE_ERROR);
                break;
            case 1213:
                backdata_1213_getCarPosition(status, obj, CACHE_ERROR);
                break;
            case 1214:
                backdata_1214_setArea(status, obj, CACHE_ERROR);
                break;
            case 1219:
                backdata_1219_changeSelectCar(status,obj, CACHE_ERROR);
                break;
            case 1220:
                backdata_1220_unactivatecar(status,obj, CACHE_ERROR);
                break;

//            case 1226:
//                backdata_1226_DemoMode(status,obj, CACHE_ERROR);
//                break;
//            case 1227:
//                backdata_1227_getMaintainList(status,obj, CACHE_ERROR);
//                break;
//            case 1228:
//                backdata_1228_AddModificationMaintainList(status,obj, CACHE_ERROR);
//                break;
//            case 1229:
//                backdata_1229_DeleteMaintainList(status,obj, CACHE_ERROR);
//                break;
            case 1230:
                backdata_1230_ConfirmMaintainList(status,obj, CACHE_ERROR);
                break;
//            case 1231:
//                backdata_1231_CompleteMaintainList(status,obj, CACHE_ERROR);
//                break;
            case 1233:
                backdata_1233_controlCar(status, obj, CACHE_ERROR);
                break;
            case 1238:
                backdata_1238_MantanceMode(status,obj, CACHE_ERROR);
                break;
            case 1248:
                backdata_1248_Change_Air_Condition(status,obj, CACHE_ERROR);
                break;
            case 1317:
                backdata_1317_changeCarFunButtons(status, obj, CACHE_ERROR);
                break;
            case 1410:
                backdata_1410_getSkinList(status, obj, CACHE_ERROR);
                break;
            case 1403:
                backdata_1403_change_car_skin(status, obj, CACHE_ERROR);
                break;
        }
    }
    // ============================error==================================
    // ============================ccmd==================================

    /**
     * 新增车辆,修改
     **/
    public void ccmd1201_newrepairCar(DataCarInfo info) {
        JsonObject carInfo = new JsonObject();
        carInfo.add("carInfo", DataCarInfo.toJsonObject(info));
        HttpConn.getInstance().request(1201, carInfo);
    }

    /**
     * 删除车辆
     **/
    public void ccmd1202_deletecar(long carid) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        HttpConn.getInstance().request(1202, data);
    }

    /**
     * 激活车辆
     **/
    public void ccmd1204_activatecar(long carid, String terminalNum) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        data.addProperty("terminalNum", terminalNum);
        HttpConn.getInstance().request(1204, data);
    }

    /**
     * 解绑车辆
     **/
    public void ccmd1220_unactivatecar(long carid) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        HttpConn.getInstance().request(1220, data);
    }

    /**
     * 取车辆列表,first only once
     **/
    public void ccmd1203_getcarlist() {
        HttpConn.getInstance().request(1203, null);
    }
    private boolean isNeedCheckBrrowCar1203  = false;
    public void ccmd1203_getcarlist(boolean needCheckBrrowCar) {
        isNeedCheckBrrowCar1203 = needCheckBrrowCar;
        HttpConn.getInstance().request(1203, null);
    }

    /**
     * 取车位置
     **/
    public void ccmd1213_getCarPosition(long carid, int isDemo) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        data.addProperty("isDemo", isDemo);
        HttpConn.getInstance().request(1213, data);
    }

    /**
     * 置当前车辆状态
     **/
    public boolean isRefreshBtn;

    public void ccmd1219_changeSelectCar(long carId, int isDemo, boolean isRefreshBtn) {
        this.isRefreshBtn = isRefreshBtn;
        if (carId == 0) return;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        carId1219 = carId;
        HttpConn.getInstance().request(1219, data);
    }

    //    private long pre1219CarId = 0,pre1219Time = 0;
    public void ccmd1219_changeSelectCar(long carId, int isDemo) {
        if (carId == 0) return;
//        long now = System.currentTimeMillis();
//        if(pre1219CarId == carId && now-pre1219Time<1000)return;
//        pre1219CarId = carId;
//        pre1219Time = now;
        this.isRefreshBtn = false;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        carId1219 = carId;
        HttpConn.getInstance().request(1219, data);
    }


    /**
     * 演示模式开始和结束
     **/
    public void ccmd1226_DemoMode(int type) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        HttpConn.getInstance().request(1226, data);
    }

    /**
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * carId long 汽车id 必填
     * start long 开始下标 必填，从0开始
     * size int 获取的条数 必填，默认20
     */
    public void ccmd1227_CarMaintain(long carId, long start, int size) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("start", start);
        data.addProperty("size", size);
        HttpConn.getInstance().request(1227, data);
    }

    /**
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * carId long 汽车id 必填，
     * miles int 保养里程数 必填，单位km
     * time int 保养的时间段 必填，单位月
     * maintenanceId
     * long 保养id 选填，如果是修改保养，则需要填写，具体见汽车保养对象中的ide
     */
    public void ccmd1228_CarMaintainAddModification(long carId, int miles, int time, long maintenanceId) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("miles", miles);
        data.addProperty("time", time);
        data.addProperty("maintenanceId", maintenanceId);
        HttpConn.getInstance().request(1228, data);
    }

    /**
     * 起始
     * 接口描述：用于删除保养记录
     * 请求参数（封装到data参数中上传）
     * 名称 类型 说明 备注
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * maintenanceId
     * long 保养id 必填，具体见汽车保养对象中的ide
     */
    public void ccmd1229_CarMaintainDeleteModification(long maintenanceId) {
        JsonObject data = new JsonObject();
        data.addProperty("maintenanceId", maintenanceId);
        HttpConn.getInstance().request(1229, data);
    }

    /**
     * type int 类型
     * 必填，1：为提前提示框中的点击 知道了 触发
     * 2：为到时提示框中的点击稍后提醒
     * 3：为到时提示框中的点击知道了，或者已保养提示框
     *
     * @param maintenanceId
     */
    public void ccmd1230_CarMaintainConfirmmineModification(long maintenanceId, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("maintenanceId", maintenanceId);
        data.addProperty("type", type);
        HttpConn.getInstance().request(1230, data);
    }

    /**
     * 设车半径 单位是米
     **/
    public void ccmd1214_setArea(long carId, int radius, int isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("radius", radius * 1000);
        data.addProperty("isOpen", isOpen);
        isOpenArea = (isOpen == 1) ? true : false;
        HttpConn.getInstance().sendMessage(data, 1214);
    }

    /**
     * 汽车完成保养接口（1231）
     * 转至元数据结尾
     * 被fangwentao添加，被fangwentao最后更新于十一月 02, 2016
     * 转至元数据起始
     * 接口描述：用于完成保养时调用的接口
     * 请求参数（封装到data参数中上传）
     * 名称 类型 说明 备注
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * maintenanceId
     * long 保养id 必填，具体见汽车保养对象中的ide
     */
    public void ccmd1231_CarMaintainCompleteModification(long maintenanceId) {
        JsonObject data = new JsonObject();
        data.addProperty("maintenanceId", maintenanceId);
        HttpConn.getInstance().request(1231, data);
    }


    /**
     * 控制车辆 instruction控制命令1：开启2：熄火3：设防4：撤防5：尾箱变化6：寻车
     * time只有0到7档，每档5分钟，0档位5分钟
     **/
    private String content = "";
    private int alertId = 0;
    private String cacheCmd = "";
    public void ccmd1233_controlCar(final DataCarInfo car, final int controlCmd, final int time) {
        preCarId = car.ide;
        preCmd = controlCmd;
        //演示
        if(car.isActive != 1){//没激活的车，直接就成功了,模拟
            backdata_1233_controlCar(1,null,null);
            DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();

            content = "";
            alertId = 0;
            //状态变化
            switch (controlCmd){
                case 1:status.isStart = 1;status.isON = 1;content= "【模拟车辆】汽车启动成功";alertId = 1;break;
                case 2:status.isStart = 0;status.isON = 0;content= "【模拟车辆】汽车熄火成功";alertId = 5;break;
                case 3:status.isLock = 1;status.isTheft = 1;content= "【模拟车辆】汽车设防成功";alertId = 3;break;
                case 4:status.isLock = 0;status.isTheft = 0;content= "【模拟车辆】汽车撤防成功";alertId = 4;break;
                case 5:
                    if(status.afterBehindOpen == 0){
                        status.afterBehindOpen = 1;content= "【模拟车辆】后尾箱开启";alertId = 2;
                    }else{
                        status.afterBehindOpen = 0;content= "【模拟车辆】后尾箱关闭";alertId = 33;
                    }
                    break;
                case 6:break;//寻车成功，结果返回就行
            }
            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    String            title             = "么控K1消息提醒:";
                    String            info              = content + "  " + ODateTime.time2StringHHmm(System.currentTimeMillis());
                    NotificationUtils notificationUtils = new NotificationUtils(GlobalContext.getContext());
                    notificationUtils.sendNotification(title, info, R.drawable.push);
                    int resId = SoundAlert.getSoundIdFrom(alertId);
                    if(resId!=0)SoundHelper.playSoundPool(GlobalContext.getContext(), resId);
                }
            }.start();
            return;
        }else if (BlueAdapter.getInstance().current_blue_state >= STATE_CONNECT_OK){//蓝牙模式,不收socket控制状态事件
            if (car.isBindBluetooth == 0) return;
            if (controlCmd == 1) {
                cacheCmd = BlueStaticValue.getControlCmdByID(controlCmd);
                String setTime = BlueStaticValue.getBlueTimeCmd(time);//0-7time
                BlueAdapter.getInstance().sendMessage(setTime);//发送TIME
                new TimeDelayTask().runTask(150L, new TimeDelayTask.OnTimeEndListener() {
                    @Override
                    public void onTimeEnd() {
                        BlueAdapter.getInstance().sendMessage(cacheCmd);//发送指令,间隔一点
                    }
                });
            }else {
                BlueAdapter.getInstance().sendMessage(BlueStaticValue.getControlCmdByID(controlCmd));//发送指令
            }
        }else {//发http控制，socket接收
            JsonObject data = new JsonObject();
            data.addProperty("carId", car.ide);
            //将{“instruction”:1,"time":1}利用每辆车的AES加密串进行加密，然后用base64编码
            JsonObject cache = new JsonObject();
            cache.addProperty("instruction", controlCmd);
            cache.addProperty("time", time);
            try {
                byte[] bytes = AES.AESgenerator(cache.toString(), car.carsig);
                if (bytes == null) return;
                String strBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
//                String test = URLEncoder.encode(strBase64, "UTF-8");//Retrofit一定要加密，OKhttp一定不能加
                data.addProperty("instruction", strBase64);//testok
                HttpConn.getInstance().sendMessage(data, 1233);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ccmd1238_MantanceMode(long carId, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1238);
    }

    /**
     * carId long 汽车id 必填
     * ACStatus int A/C状态 0：关闭，1：开启，不设置或值未改变为-1
     * windStatus int 风力状态
     * 0：小，1：中，2：大，不设置或值未改变为-1
     * tempStatus int 温度状态 0：强（冷），1：中（冷），2：弱（冷），3：中间档，4：低（热），5：中（热），6：高（热），不设置或值未改变为-1
     */
    public void ccmd1248_Change_Air_Condition(long carId, int ACStatus, int windStatus, int tempStatus) {
        Log.i("abcde", "发送协议 " + " carId" + carId + "ACStatus" + ACStatus + "windStatus" + windStatus + "tempStatus" + tempStatus);
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("ACStatus", ACStatus);
        data.addProperty("windStatus", windStatus);
        data.addProperty("tempStatus", tempStatus);
        carId1219 = carId;
        HttpConn.getInstance().sendMessage(data, 1248);
    }

    /**
     * 功能开关开启关闭接口（1317）顺序按上传顺序
     **/
    public void ccmd1317_changeCarFunButtons(long carId, JsonArray funInfos,String logInfo) {
        Log.e("ccmd1317",logInfo);
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.add("funInfos", funInfos);
        HttpConn.getInstance().request(1317, data);
    }

    /**
     * 车辆车贴皮肤列表接口（1410）
     **/
    public void ccmd1410_getSkinList(long carId) {
        pre1410CarId = carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        HttpConn.getInstance().request(1410, data);
    }

    /**
     * 选择要装扮的车辆
     * type;//类型	1：车辆皮肤类型，2：车辆背景类型，3：车贴
     */
    public void ccmd1403_change_car_skin(long carId, int carTypeId, int type) {
//        List<DataCarInfo> list = ManagerCarList.getInstance().getCarInfoList();
//        if (list == null) return;
//        List<Long> list1 = new ArrayList<Long>();
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getCarTypeId() == carTypeId || carId == list.get(i).ide) {
//                list1.add(list.get(i).ide);
//            }
//        }
//        Long[] result = list1.toArray(new Long[list1.size()]);

        DataSelectCar eafcer = new DataSelectCar();
        eafcer.carIds = new Long[]{carId};
        eafcer.carTypeId = carTypeId;
        eafcer.type = type;
        JsonObject data = eafcer.toJsonObject(eafcer);
        HttpConn.getInstance().sendMessage(data, 1403);
    }

    private class DataSelectCar {
        public Long[] carIds;
        public int carTypeId;
        public int type;

        public JsonObject toJsonObject(DataSelectCar info) {
            Gson gson = new Gson();
            String json = gson.toJson(info);
            JsonObject obj = gson.fromJson(json, JsonObject.class);
            return obj;
        }
    }
    // ============================scmd==================================

    /**
     * 新增车辆
     **/
    private void backdata_1201_newrepairCar(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1201_newrepairCar");
            ODispatcher.dispatchEvent(OEventName.CAR_NEW_SUCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_SKIN_CHANGE);
        }
    }

    /**
     * 删除车辆
     **/
    private void backdata_1202_deleteCar(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1202_deleteCar");
            ODispatcher.dispatchEvent(OEventName.CAR_DELETE_SUCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }

    /**
     * 激活车辆
     **/
    private void backdata_1204_activatecar(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
//            ManagerSkins.getInstance().saveFlash(OJsonGet.getString(obj, "splashAddress"));
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1204_activatecar");
//            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
//            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
//            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
//            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
            ODispatcher.dispatchEvent(OEventName.CAR_ACTIVATE_SUCESS, "");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        } else {
            ODispatcher.dispatchEvent(OEventName.CAR_ACTIVATE_SUCESS, CACHE_ERROR);
        }
    }

    /**
     * 解绑车辆
     **/
    private void backdata_1220_unactivatecar(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1220_unactivatecar");
//            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
//            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
//            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
//            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
            ODispatcher.dispatchEvent(OEventName.CAR_UNACTIVATE_SUCESS, "");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
//            if(ViewSwitchShake.viewSwitchShakeThis!=null)ViewSwitchShake.viewSwitchShakeThis.handlerChangeSwitch();
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        } else {
            ODispatcher.dispatchEvent(OEventName.CAR_UNACTIVATE_SUCESS, CACHE_ERROR);
        }
    }

    /**
     * 取车辆列表
     **/
    private void backdata_1203_getcarlist(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
//            Log.e("carList1203", obj.toString());
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1203_getcarlist");
            if(isNeedCheckBrrowCar1203){
                isNeedCheckBrrowCar1203 = false;
                ManagerCarList.getInstance().checkBrowerCar();
            }
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }


    /**
     * 下发数据参数
     * result jsonObject 响应信息头 具体见协议框架中的响应信息头
     * maintenanceInfos
     * jsonArray  保养列表  具体见 汽车保养对象
     */
//    private void backdata_1227_getMaintainList(int status,JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
//            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
//            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
//            ODispatcher.dispatchEvent(OEventName.GET_MAINTAINLIST_RESULTBACK);
//        }
//    }

    /**
     * 下发数据参数
     * result jsonObject 响应信息头 具体见协议框架中的响应信息头
     * maintenanceInfos
     * jsonArray  保养列表  具体见 汽车保养对象，只下发第一页20条
     */
//    private void backdata_1228_AddModificationMaintainList(int status,JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
//            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
//            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
//            ODispatcher.dispatchEvent(OEventName.MODIFICATION_MAINTAINLIST_RESULT_BACK);
//        }
//    }

    /**
     * 置当前车辆状态
     **/
    private void backdata_1219_changeSelectCar(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            JsonObject carStatusInfo = OJsonGet.getJsonObject(obj, "carStatusInfo");
            if (carStatusInfo != null) {
//                carStatusInfo.addProperty("ide", carId1219);//车状态数据，carid无值
//                Log.e("1219",carStatusInfo.toString());
                ManagerCarList.getInstance().saveCarStatus(carStatusInfo);
                if (isRefreshBtn) ODispatcher.dispatchEvent(OEventName.CAR_REFRESH_RESULTOK, true);
            }
        } else {
            if (isRefreshBtn) ODispatcher.dispatchEvent(OEventName.CAR_REFRESH_RESULTOK, false);
        }
    }

    /**
     * 下发数据参数
     * result jsonObject 响应信息头 具体见协议框架中的响应信息头
     * maintenanceInfos
     * jsonArray  保养列表  具体见 汽车保养对象，只下发第一页20条
     */
//    private void backdata_1229_DeleteMaintainList(int status,JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
//            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
//            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
//            ODispatcher.dispatchEvent(OEventName.MAINTAIN_DELETE);
//        }
//    }
    private void backdata_1230_ConfirmMaintainList(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {

        }
    }

//    private void backdata_1231_CompleteMaintainList(int status,JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
//            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
//            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
//            ODispatcher.dispatchEvent(OEventName.MAINTAIN_COMPELETE);
//        }
//    }


    /**
     * 设车半径
     **/
    private boolean isOpenArea = true;

    public void backdata_1214_setArea(int status, JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            JsonArray jsonArray = OJsonGet.getJsonArray(obj, "funInfos");
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            carInfo.funInfos = jsonArray;
            ManagerCarList.getInstance().saveCarInfo(DataCarInfo.toJsonObject(carInfo), "backdata_1214_setArea");
            //功能列表修改的是车辆数据
            ccmd1219_changeSelectCar(ManagerCarList.getInstance().getCurrentCarID(), 0);
            ODispatcher.dispatchEvent(OEventName.GPS_SETAREA_RESULTBACK);
            if (isOpenArea) {
                GlobalContext.popMessage("设置电子围栏成功!", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
            } else
                GlobalContext.popMessage("取消电子围栏成功!", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
        } else {
            if (isOpenArea) {
                GlobalContext.popMessage("设置电子围栏失败!", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
            } else
                GlobalContext.popMessage("取消电子围栏失败!", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
        }
    }

    /**
     * 设车半径
     **/
    public void backdata_1213_getCarPosition(int status, JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            ManagerCurrentCarInfo.getInstance().saveCurrentCarInfo(obj);
            ODispatcher.dispatchEvent(OEventName.GET_CURRENT_CAR_RESULT_BACK);
        }
    }

    /**
     * @param obj
     * @param CACHE_ERROR
     */
//    private void backdata_1226_DemoMode(int status,JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            if (DemoMode.isBeginOrFinish == 1) {
////				DemoMode.getInstance().saveIsDemoMode("演示开始")DemoMode.isDemoMode="演示开始";
//                DemoMode.saveIsDemoMode(true);
//                ODispatcher.dispatchEvent(OEventName.DEMO_MODE_START);
//            } else if (DemoMode.isBeginOrFinish == 2) {
////				DemoMode.getInstance().saveIsDemoMode("演示结束");
//                DemoMode.saveIsDemoMode(false);
////			DemoMode.isDemoMode="演示结束";
//                ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS);
//            }
//            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1226_DemoMode");
//            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_CHANGE);
//            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
//
//        } else {
//            if (DemoMode.isBeginOrFinish == 1) {
//                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.network_anomaly_temporarily_unable_to_enter_the_demo_mode));
//            } else if (DemoMode.isBeginOrFinish == 2) {
//                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.network_anomaly_temporarily_unable_to_exit_the_demo_mode));
//            }
//        }
//    }

    /**
     * 控制车辆 instruction控制命令1：开启2：熄火3：设防4：撤防5：尾箱变化6：寻车 time只有0到7档，每档5分钟，0档位5分钟
     **/
    private long preCarId;
    private int preCmd;
    public int getPreCmd(){
        return preCmd;
    }

    private void backdata_1233_controlCar(int status, JsonObject obj, String CACHE_ERROR) {
//            JsonObject result = OJsonGet.getJsonObject(obj, "result");
//            int        statusLin = OJsonGet.getInteger(result, "status");
//            if (statusLin == 1) {
        if (status == 1) {
            CarControlResult result1 = new CarControlResult();
            result1.carId = preCarId;
            result1.currentProcess = CARCONTROL_SENDED;
            result1.instruction = preCmd;
            result1.status = 1;
            ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);
            //根本不用刷新，真车没有这么快
        } else {
            GlobalContext.popMessage("控车失败", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));//Color.parseColor("#A00000")
        }
    }

    private void backdata_1238_MantanceMode(int status,JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1238_MantanceMode");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }

    private void backdata_1248_Change_Air_Condition(int status,JsonObject obj, String CACHE_ERROR) {
//        if (status == 1) {
//            JsonObject carStatusInfo = OJsonGet.getJsonObject(obj, "carStatusInfo");
//            if (carStatusInfo != null) {
//                carStatusInfo.addProperty("ide", carId1219);
//                Log.e("backdata_1248","carId:"+carId1219+"http检测车辆启动状态"+DataCarStatus.fromJsonObject(carStatusInfo).isStart);
//                ManagerCarList.getInstance().saveCarStatus(carStatusInfo);
//            }
//        }
    }

    private void backdata_1317_changeCarFunButtons(int status, JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            JsonArray jsonArray = OJsonGet.getJsonArray(obj, "funInfos");
            //无需处理,是存入车辆详情
        }
    }

    /**
     * 车辆车贴皮肤列表接口（1410）
     **/
    private long pre1410CarId = 0;

    public void backdata_1410_getSkinList(int status, JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            JsonArray carTypeInfos = OJsonGet.getJsonArray(obj, "carTypeInfos");
            JsonArray carStickerInfos = OJsonGet.getJsonArray(obj, "carStickerInfos");
            DataCarInfo carInfo = ManagerCarList.getInstance().getCarByID(pre1410CarId);
            if (carTypeInfos != null)
                ManagerSkins.getInstance().saveSkinListByType(carInfo.carType, carTypeInfos);
            if (carStickerInfos != null)
                ManagerSkins.getInstance().saveStickerListByType(carInfo.carType, carStickerInfos);
            ODispatcher.dispatchEvent(OEventName.CAR_SKIN_LIST_BACK);
        }
    }

    /**
     * 取汽车装扮选择车辆返回的信息
     */
    private void backdata_1403_change_car_skin(int status, JsonObject obj, String CACHE_ERROR) {
        if (status == 1) {
            JsonArray carInfos = OJsonGet.getJsonArray(obj, "carInfos");
            ManagerCarList.getInstance().saveCarList(carInfos, "backdata_1403getCardressupInfo");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
            ODispatcher.dispatchEvent(OEventName.CAR_SKIN_CHANGE);
        }
    }
}
