package com.mani.car.mekongk1.model;

import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;

public class ManagerCurrentCarInfo {
    public DataCarInfo carInfo;
    public   String          latlng;                                            // 经纬度
    public   int          radius;                                                // 半径 单位米
    public   int          isOpen;                                            // 电子围栏是否开启，0：未开启，1：开启
    public   String          radiusLatlng;                                            // 电子围栏的经纬度
    public   int          direction;                                                // 0～359，正北为0，顺时针
    public   int          isStart;                                                // 0：熄火 1：启动 ，用来区分显示启动时间/熄火时间
    public   int          startTime;                                                // 汽车启动/熄火时间
    private static ManagerCurrentCarInfo    _instance;
    public static ManagerCurrentCarInfo getInstance() {
        if (_instance == null)
            _instance = new ManagerCurrentCarInfo();
        return _instance;
    }
    public void saveCurrentCarInfo(JsonObject jsonObject){
        if(jsonObject==null)return;
        this.carInfo=DataCarInfo.fromJsonObject(OJsonGet.getJsonObject(jsonObject,"carInfo"));
        this.latlng=OJsonGet.getString(jsonObject,"latlng");
        this.radius=OJsonGet.getInteger(jsonObject,"radius");
        this.isOpen=OJsonGet.getInteger(jsonObject,"isOpen");
        this.radiusLatlng=OJsonGet.getString(jsonObject,"radiusLatlng");
        this.direction=OJsonGet.getInteger(jsonObject,"direction");
        this.isStart=OJsonGet.getInteger(jsonObject,"isStart");
        this.startTime=OJsonGet.getInteger(jsonObject,"startTime");
    }

}
