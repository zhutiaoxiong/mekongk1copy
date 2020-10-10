package com.mani.car.mekongk1.model;

import com.google.gson.JsonArray;
import com.mani.car.mekongk1.model.pushmessage.AlertInfos;

import java.util.List;

public class ManagerAlert {
    private List<AlertInfos> alertList;
    private static ManagerAlert    _instance;
    public static ManagerAlert getInstance() {
        if (_instance == null)
            _instance = new ManagerAlert();
        return _instance;
    }
    public void saveAlertList(JsonArray array){
        if(array==null)return;
        alertList=AlertInfos.fromJsonArray(array);
    }
    public List<AlertInfos> getAlertList(){
        return alertList;
    }
}
