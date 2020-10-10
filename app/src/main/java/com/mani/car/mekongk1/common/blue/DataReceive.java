package com.mani.car.mekongk1.common.blue;

/**
 * Created by Administrator on 2016/9/21.
 */
public class DataReceive {
    public int dataType;
    public int length;
    public byte[] data;
    public int check;//校验码
    public boolean matchCheck(){
        byte result = 0;
        result+=dataType;
        result+=length;
        for(byte bt:data){
            result += bt;
        }
        result ^= 0xff;
        if(result == check)return true;
        return false;
    }

    /**
     * @param typeId 指令id
     * @param preId 业务id
     * @param data 数据
     * typeId + length(preId+data) + preId + data +OXff
     */
    public static byte[] newBlueMessage(byte typeId,byte preId,byte[] data){
        byte length = (byte) (data.length+1);
        byte[] result = new byte[length+3];
        result[0]= typeId;
        result[1]= length;
        result[2]= preId;
        System.arraycopy(data,0,result,3,data.length);
        //胶验
        byte ox = 0;
        for(byte bt:result){
            ox += bt;
        }
        ox ^= 0xff;
        result[length+2]= ox;
        return result;
    }
}
