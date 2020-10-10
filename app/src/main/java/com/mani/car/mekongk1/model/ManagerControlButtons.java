package com.mani.car.mekongk1.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.kulala.staticsfunc.dataType.ListMe;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataControlButton;

import java.util.ArrayList;
import java.util.List;

public class ManagerControlButtons {
    private ListMe<Integer,String> btns_default;
    //===========================================================
    private static ManagerControlButtons _instance;

    private ManagerControlButtons() {
        btns_default = new ListMe<>();
        btns_default.add(100,"关锁");
        btns_default.add(101,"启动");
        btns_default.add(102,"开锁");
        btns_default.add(103,"双闪鸣笛");
        btns_default.add(104,"尾箱");
        btns_default.add(105,"功能开关");
        btns_default.add(106,"导航找车");
        btns_default.add(107,"借车给TA");
        btns_default.add(108,"电子围栏");
        btns_default.add(109,"行车记录");
        btns_default.add(110,"分享APP");
        btns_default.add(111,"切换车辆");
        btns_default.add(112,"蓝牙控车");
    }

    public static ManagerControlButtons getInstance() {
        if (_instance == null)
            _instance = new ManagerControlButtons();
        return _instance;
    }

    //===========================================================
    //不存在没车的情况，不要自己初始化列表
    //演示
    private boolean noActiveArea = true;

    public void changeNoActiveArea(boolean isArea) {
        noActiveArea = isArea;
    }

    public List<Integer> getBtnsIdList(){return btns_default.getL();}
    public boolean isSameBtnsName(String name,Integer funId){
        for(int i = 0; i<btns_default.getL().size();i++){
            if(funId == btns_default.getL(i)){
                if(name.equals(btns_default.getR(i)))return true;
                else return false;
            }
        }
        return false;
    }
    private ArrayList<DataControlButton> getNoActiveList() {
        ArrayList<DataControlButton> buttonList = new ArrayList<DataControlButton>();
        for (int i = 0; i < btns_default.size(); i++) {
            DataControlButton data = new DataControlButton(btns_default.getR(i), 1, 100 + i);
            if (isSameBtnsName(data.name,108) && noActiveArea == false) data.name = "取消围栏";
            buttonList.add(data);
        }
        return buttonList;
    }

    //控制滑动按扭
    public ArrayList<DataControlButton> getShowingButtons() {
        ArrayList<DataControlButton> buttonList = getCurrentButtonList();
        ArrayList<DataControlButton> cacheList = new ArrayList<DataControlButton>();
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        for (int i = 0; i < buttonList.size(); i++) {
            DataControlButton btnData = buttonList.get(i);
            if (btnData != null && btnData.name != null) {
                if (isSameBtnsName(btnData.name,101)) {
                    if (ManagerCarList.getInstance().getCurrentCarStatus().isStart == 1) btnData.name = "熄火";//已启动
                }
                if (btnData.status == 1) cacheList.add(btnData);
            }
        }
//        if(cacheList.size() == 0)return getInitList();
//        Log.e("ButtonList", "Showing:" + Arrays.toString(OArrayConvent.ListGetStringArr(cacheList, "name")));
        return cacheList;
    }

    //控制面板按扭
    public ArrayList<DataControlButton> getCurrentButtonList() {
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        if (carInfo.isActive == 0 || carInfo.funInfos == null) return getNoActiveList();//演示
        JsonArray jsonArray = carInfo.funInfos;
        ArrayList<DataControlButton> buttonList = new ArrayList<>();
        if (jsonArray == null) return buttonList;
        buttonList = DataBase.fromJsonArray(jsonArray, DataControlButton.class);
        //防之前的出错
        boolean isFunError = false, isNoFun = true;
        for (int i = 0; i < buttonList.size(); i++) {
            DataControlButton but = buttonList.get(i);
            if (isSameBtnsName(but.name,105)) {
                isNoFun = false;
                if (but.status == 0 || but.ide == 0 || i != 5) isFunError = true;
            }
        }
        if (isFunError || isNoFun) {
            moveHideButtonEnd(buttonList);
        }
        //删重复数据
        ArrayList<DataControlButton> cacheList = new ArrayList<>();
        for  ( int  i  =   0 ; i  <  buttonList.size(); i ++ )  {
            boolean haveSame = false;
            for  ( int  j  =  0; j  < cacheList.size(); j ++ )  {
                if  (buttonList.get(i).ide == cacheList.get(j).ide){
                    Log.e("ButtonList", "haveSame Button:" + cacheList.get(j).ide + cacheList.get(j).name);
                    haveSame  = true;
                }
            }
            if(!haveSame)cacheList.add(buttonList.get(i));
        }
        if (isFunError || isNoFun) {//有错误数据
            JsonArray cacheJson = DataBase.toJsonArray(cacheList);
            OCtrlCar.getInstance().ccmd1317_changeCarFunButtons(carInfo.ide, cacheJson,"getCurrentButtonList");
        }
        return cacheList;
    }

    public void changeSingleButtons(DataControlButton button) {
        if (isSameBtnsName(button.name,105)) return;
        ArrayList<DataControlButton> buttonList = getCurrentButtonList();
        if (buttonList == null || button == null) {
//            Log.e("Manager", "ManagerControlButtons changeSingleButtons null");
            return;
        }
        for (DataControlButton but : buttonList) {
            if (but.name.equals(button.name)) but.status = button.status;
        }
        moveHideButtonEnd(buttonList);
        //交换完成，保存local&http
        JsonArray cacheJson = DataBase.toJsonArray(buttonList);
        ManagerCarList.getInstance().saveCurrCarFunButtons(cacheJson);
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        OCtrlCar.getInstance().ccmd1317_changeCarFunButtons(carInfo.ide, cacheJson,"changeSingleButtons");
    }

    //把不显示的功能，移去最尾
    private void moveHideButtonEnd(ArrayList<DataControlButton> buttonList) {
        ArrayList<DataControlButton> cacheList = new ArrayList<DataControlButton>();
        ArrayList<DataControlButton> endList = new ArrayList<DataControlButton>();
        DataControlButton funBtn = new DataControlButton(btns_default.getR(5), 1, 105);//避免未初始化
        for (int i = 0; i < buttonList.size(); i++) {
            DataControlButton btnData = buttonList.get(i);
            if (isSameBtnsName(btnData.name,105)) {
                funBtn = btnData;
                if (funBtn.ide == 0) funBtn.ide = 105;
                if (funBtn.status == 0) funBtn.status = 1;
            } else if (buttonList.get(i).status == 0) {
                endList.add(btnData);
            } else {
                cacheList.add(btnData);
            }
        }
        buttonList.clear();
        buttonList.addAll(cacheList);
        buttonList.addAll(endList);
        buttonList.add(5, funBtn);
    }

    public void switchButtons(int from, int to) {
        if (from == -1 || to == -1) return;
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        ArrayList<DataControlButton> buttonList = getCurrentButtonList();
        DataControlButton cacheFrom = buttonList.get(from);
        DataControlButton cacheTo = buttonList.get(from);
        if(isSameBtnsName(cacheFrom.name,105) || isSameBtnsName(cacheTo.name,105))return;
        if(cacheFrom.status == 0 || cacheTo.status == 0)return;
        buttonList.remove(from);
        buttonList.add(to, cacheFrom);
        moveHideButtonEnd(buttonList);
        JsonArray cacheJson = DataBase.toJsonArray(buttonList);
        ManagerCarList.getInstance().saveCurrCarFunButtons(cacheJson);
//        ODispatcher.dispatchEvent(OEventName.CAR_BUTTONS_CHANGE);//不需要发事件，秒计
        OCtrlCar.getInstance().ccmd1317_changeCarFunButtons(carInfo.ide, cacheJson,"switchButtons");
    }

}
