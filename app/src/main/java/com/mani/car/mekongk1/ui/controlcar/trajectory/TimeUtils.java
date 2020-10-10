package com.mani.car.mekongk1.ui.controlcar.trajectory;

public class TimeUtils {
    /**
     * @Author:whf
     * @param:
     * @Description: 获得“今天”零点时间戳 获得2点的加上2个小时的毫秒数就行
     * @Date:2018/4/12 0012
     */
    public static Long getTodayZeroPointTimestamps(){
        Long currentTimestamps=System.currentTimeMillis();
        Long oneDayTimestamps= Long.valueOf(60*60*24*1000);
        return currentTimestamps-(currentTimestamps+60*60*8*1000)%oneDayTimestamps;
    }
}
