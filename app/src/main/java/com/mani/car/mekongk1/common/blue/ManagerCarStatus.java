package com.mani.car.mekongk1.common.blue;

import android.util.Log;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ManagerCarStatus {
    private static byte[] pre21;//电量不要刷新
    public static void setData0x21(byte[] data,long carId){
        if(data.length!=8)return;
        DataCarInfo thisCar = ManagerCarList.getInstance().getCarByID(carId);
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        if(thisCar == null)return;
        byte[] states = ByteHelper.getBitArray(data[0]);//门窗状态
        carStatus.leftFrontOpen = states[7];
        carStatus.rightFrontOpen = states[6];
        carStatus.leftBehindOpen = states[5];
        carStatus.rightBehindOpen = states[4];
//        carInfo.footbrake = states[3];//脚啥
        carStatus.isStart = states[2];
        byte[] infos = ByteHelper.getBitArray(data[1]);//车辆基本信息
        carStatus.lightOpen = infos[7];
        carStatus.isLock = infos[6];//门锁状态                0b: 开锁 1b: 上锁
        Log.e("锁状态",(infos[6] == 0 ? "开锁" : "上锁"));
        carStatus.afterBehindOpen = infos[5];
//        carInfo.powercover = infos[4];
//        carInfo.handbrake = infos[3];
        carStatus.isTheft = infos[2]*2+infos[1];//防盗
        carStatus.isON = infos[0];//ON开关状态            0b: OFF 1b: ON
        //
//        carInfo.oil_spend = noSign(data[2])/10;//平均油耗 = （平均油耗值）/ 10
//        carInfo.speed = noSign(data[3]);
//        carInfo.speed_round = noSign(data[5])*256+noSign(data[4]);
        //
        byte[] normals = ByteHelper.getBitArray(data[6]);//常规数据显示
//        carInfo.unclosedoor_warning = normals[0];
//        carInfo.shakewarning = normals[1];
//        carInfo.startunpre = normals[3];
        carStatus.leftFrontWindowOpen = normals[4];
        carStatus.rightFrontWindowOpen = normals[5];
        carStatus.leftBehindWindowOpen = normals[6];
        carStatus.rightBehindWindowOpen = normals[7];

        byte[] skys = ByteHelper.getBitArray(data[7]);//天窗
        carStatus.skyWindowOpen = skys[6];
        if(pre21!=null && !Arrays.equals(pre21,data)){
            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
            Log.e("SECOND_CHANGE","CAR_STATUS_SECOND_CHANGE");
        }
        pre21 = data;
    }
    //fin
    public static void setData0x22(byte[] data,long carId){
        if(data.length!=8)return;
        DataCarInfo thisCar = ManagerCarList.getInstance().getCarByID(carId);
        if(thisCar == null)return;
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
//        carInfo.sentence = noSign(data[2])*65536+noSign(data[1])*256+noSign(data[0]);//&0xff转为无符号
        carStatus.voltage = noSign(data[3])/10;
//        Log.e(">>blue>>","voltage电量>>>"+thisCar.status.voltage);
//        carInfo.oil = (noSign(data[7])*256+noSign(data[6]))/10;
    }
    public static double noSign(byte a){
        return a&0xff;
    }
}
