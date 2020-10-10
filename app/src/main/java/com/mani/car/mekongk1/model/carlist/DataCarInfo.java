package com.mani.car.mekongk1.model.carlist;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mani.car.mekongk1.model.DataBase;

import java.util.ArrayList;
import java.util.List;

public class DataCarInfo {
    public long ide;                                    // 汽车id

    public JsonObject authorityInfo;//汽车权限user
    public String     logo;
    public String num             = "模拟车辆";                    // 车牌号
    public int    brandId         = 0;//品牌id
    public String brand           = "";                    // 品牌
    public String series          = "";                    // 车系
    public String model           = "";                    // 车型
    public String VINNum          = "";
    public String engineNum       = "";
    public String phoneNum        = "";
    public long   buyTime         = 0;                    // 购车日期
    public long   insuranceTime   = 0;                    // 保险日期
    public long   annualAuditTime = 0;                    // 年审日期
    public long   maintenanceTime = 0;                    // 保养日期
    public long   endTime         = 0;                    // 权限到期日期

    public int    isActive    = 0;                    // 是否激活
    public int    isMyCar     = 1;                    //0：非自己的车辆，即授权所得，1：自己的车辆
    //    public boolean isSelected  = false;                // 是否选中
    public String carsig      = "";                    // 车控制加密密钥
    public String terminalNum = "";//终端号码

//    public int     carTypeId       = 0;//汽车装扮id,已废弃,用carTypeInfo
    public boolean skinSelect      = false;//汽车装扮列表选中
    public int     isBindBluetooth = 0;//是否绑定蓝牙	0：未绑定，1：已绑定（如果绑定则显示可以蓝牙控制）
    public String  bluetoothName   = "";//蓝牙名称
    public int     isMaintaince    = 0;//是否维修模式0：非维修模式下，1：维修模式下
    public String blueCarsig;//蓝牙验证串
    public int    isAuthority;// 是否授权 0：未授权，1：已授权
    /**车辆数据，不包括车辆状态的*/
//    public DataCarStatus status = new DataCarStatus();    // 车辆状态
    public JsonObject carTypeInfo;    // 汽车装扮对象 DataCarTypeInfo
    public JsonObject skinTemplateInfo;    // 汽车皮肤面板对象 DataCarIdeUrl ide+url
    public JsonObject carBackgroundInfo;   //carBackgroundInfo jsonObject 汽车背景对象 汽车背景对象，具体见：车辆背景对象
    public JsonObject carStickerInfo;   //汽车贴纸 DataCarIdeUrl ide+url


    //170509
    public long       activeTime;//		模组激活时间
    public int        isDue;//		模组是否过保修期,0：未激活，1：保修中，2：已过保
    public JsonObject shakeInfo;//DataSwitch
    public JsonArray  nonekeyNoticeInfos;//List<DataSwitch> //固定id：72靠近开，73离开锁,null means unShow

    public int carPos = 0;                                // 车滑动时的坐标,无车时为0

    public JsonArray funInfos;//new ArrayList<DataControlButton>
    public int     carType       = 1;//1：轿车，2：SUV
    public int     authorityType       = 0;//授权类型 0：沒有授权，1：信任授权带时间，2：永久授权
    public int     authorityType1       = 0;//0：没有授权 1：已经授权
    public long     authorityEndTime       = 1;//授权结束时间
    public long     authorityEndTime1       = 1;//授权结束时间
    public String     authorityPhone       = "";//授权的手机号，只有信任授权存在这个手机号，临时的不存在

    public DataCarInfo copy() {
        JsonObject  object = toJsonObject(this);
        DataCarInfo info   = DataCarInfo.fromJsonObject(object);
        return info;
    }
//
//    public DataAuthoredUser getAuthorityInfo() {
//        return DataAuthoredUser.fromJsonObjectt(authorityInfo);
//    }
    //车皮肤
    public int getCarTypeId(){
        if(carTypeInfo == null)return 0;
        DataCarTypeInfo info = DataCarTypeInfo.fromJsonObject(carTypeInfo);
        return info.ide;
    }
    //车帖
    public int getCarStickerId(){
        if(carStickerInfo == null)return 0;
        DataCarIdeUrl info = DataBase.fromJsonObject(carStickerInfo,DataCarIdeUrl.class);
        return info.ide;
    }

    public static DataCarInfo fromJsonObject(JsonObject obj) {
        Gson        gson    = new Gson();
        DataCarInfo thisobj = gson.fromJson(obj, DataCarInfo.class);
        return thisobj;
    }

    public static JsonObject toJsonObject(DataCarInfo info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }

    public static JsonArray toJsonArrayLocal(List<DataCarInfo> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }

//    public String checkDataComp() {
//        if (num.equals(""))
//            return GlobalContext.getContext().getResources().getString(R.string.not_enter_vehicle_name);
//        if (brand.equals(""))
//            return GlobalContext.getContext().getResources().getString(R.string.not_enter_vehicle_brands);
//        if (series.equals(""))
//            return GlobalContext.getContext().getResources().getString(R.string.not_enter_vehicle_series);
//        if (model.equals(""))
//            return GlobalContext.getContext().getResources().getString(R.string.not_enter_vehicle_model);
//        if (VINNum.equals(""))
//            return GlobalContext.getContext().getResources().getString(R.string.not_enter_vehicle_vin_number);
//        if (engineNum.equals(""))
//            return GlobalContext.getContext().getResources().getString(R.string.not_enter_vehicle_engine_number);
//        return "1";
//    }

    public static List<DataCarInfo> fromJsonArray(JsonArray carList) {
        if (carList == null || carList.size() == 0)
            return new ArrayList<DataCarInfo>();
        List<DataCarInfo> data = new ArrayList<DataCarInfo>();
        for (int i = 0; i < carList.size(); i++) {
            JsonObject  object = carList.get(i).getAsJsonObject();
            DataCarInfo info   = DataCarInfo.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
