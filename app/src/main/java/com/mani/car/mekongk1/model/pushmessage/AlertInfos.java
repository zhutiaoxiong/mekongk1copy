package com.mani.car.mekongk1.model.pushmessage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AlertInfos {
    /**
     * ide
     * int
     * 开关id
     *
     * name
     * String
     * 开关的名字
     *
     * isOpen
     * int
     * 是否开启
     * 0：未开启，1：开启
     *
     * signalValue
     * double
     * 信号值
     * 无匙开关信号值，开启时下发
     */
    public int ide;
    public String name;
    public int isOpen;
    public double signalValue;

    public static AlertInfos fromJsonObject(JsonObject obj) {
        Gson gson = new Gson();
        AlertInfos thisobj = gson.fromJson(obj, AlertInfos.class);
        return thisobj;
    }

    public static JsonObject toJsonObject(AlertInfos info) {
        Gson gson = new Gson();
        String json = gson.toJson(info);
        JsonObject obj = gson.fromJson(json, JsonObject.class);
        return obj;
    }

    public static List<AlertInfos> fromJsonArray(JsonArray carBackgroundInfos) {
        if (carBackgroundInfos == null || carBackgroundInfos.size() == 0)
            return null;
        List<AlertInfos> data = new ArrayList<AlertInfos>();
        for (int i = 0; i < carBackgroundInfos.size(); i++) {
            JsonObject objjj = carBackgroundInfos.get(i).getAsJsonObject();
            AlertInfos info = AlertInfos.fromJsonObject(objjj);
            data.add(info);
        }
        return data;
    }

    public static JsonArray toJsonArray(List<AlertInfos> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        JsonArray arr = gson.fromJson(json, JsonArray.class);
        return arr;
    }
}
