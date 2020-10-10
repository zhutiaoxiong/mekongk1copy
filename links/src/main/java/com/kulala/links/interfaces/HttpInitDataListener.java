package com.kulala.links.interfaces;

import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2017/2/10.
 */
public interface HttpInitDataListener {
    JsonObject getPHeadJsonObj(String subscription);
    void onReceiveData(int cmd, String receiveStr);//收包了就是成功了
    String getBasicUrl();
}
