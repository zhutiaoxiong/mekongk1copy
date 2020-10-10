package com.mani.car.mekongk1.model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
/**默认是本地包的数据*/
public class DataVoice {
    public int ide = 0;//语音包id
    public String name;//名称
    public String size;//大小
    public String profileUrl = "voice_play_default.mp3";//简单说明语音地址
    public String downUrl = "voice_play_default";//语音包地址
    public static DataVoice fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        DataVoice thisobj = gson.fromJson(obj, DataVoice.class);
        return thisobj;
    }

    public static List<DataVoice> fromJsonArray(JsonArray sounds) {
        if (sounds == null || sounds.size() == 0)return null;
        List<DataVoice> data = new ArrayList<DataVoice>();
        for (int i = 0; i < sounds.size(); i++) {
            JsonObject    object = sounds.get(i).getAsJsonObject();
            DataVoice info   = DataVoice.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }

    public static JsonObject toJsonObject(DataVoice info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
