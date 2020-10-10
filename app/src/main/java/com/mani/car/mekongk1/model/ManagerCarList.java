package com.mani.car.mekongk1.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;
import com.mani.car.mekongk1.model.loginreg.DataUser;

import java.util.ArrayList;
import java.util.List;


public class ManagerCarList {
    private        List<DataCarInfo> carInfoList;
    private        List<DataCarInfo> carInfoListOther;//其它人的车辆列表
    private        long              currentCarId = -1;
    private DataCarStatus currentCarStatus;
    // ========================out======================
    private static ManagerCarList _instance;
    public DataCarInfo currentPayCar;
    public static ManagerCarList getInstance() {
        if (_instance == null)
            _instance = new ManagerCarList();
        return _instance;
    }
    // ========================out======================

    public DataCarStatus getCurrentCarStatus() {
        if(currentCarStatus == null)currentCarStatus = new DataCarStatus();
        currentCarStatus.carId  = getCurrentCarID();
        return currentCarStatus;
    }

    // ========================out======================
    public void saveCarInfo(JsonObject carinfo, String fromWhere){
        DataCarInfo carInfo=DataCarInfo.fromJsonObject(carinfo);
        if(carInfo==null)return;
//        currentPayCar=carInfo;
        if(carInfoList==null){
            carInfoList=new ArrayList<DataCarInfo>();
            carInfoList.add(carInfo);
        }else{
            for(int i=0;i<carInfoList.size();i++){
                if(carInfo!=null&&carInfoList.get(i)!=null){
                    if(carInfo.ide==carInfoList.get(i).ide){
                      carInfoList.remove(i);
                      carInfoList.add(carInfo);
                    }
                }
            }
        }
        saveCarListLocal();
    }

    // local=================================
    public List<DataCarInfo> getCarInfoList() {
        if (carInfoList == null || carInfoList.size() == 0) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            long userId = user == null ? 0 : user.userId;
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId, "carList");
            if (result == null || result.length() == 0){
                carInfoList = new ArrayList<DataCarInfo>();
                carInfoList.add(new DataCarInfo());
                return carInfoList;
            }
            JsonObject obj = ODBHelper.convertJsonObject(result);
//                JsonObject obj = ODataShare.loadJsonObject(GlobalContext.getContext(), ODataShare.MODE_PRIVATE, "carList");
            if (obj != null) {
                JsonArray arr = OJsonGet.getJsonArray(obj, "carList");
                carInfoList = DataCarInfo.fromJsonArray(arr);
            }
        }
        return carInfoList;
    }
    public void saveCarListLocal() {
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if(carInfoList == null || carInfoList.size() == 0){
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "carList", "");
        }else{
            JsonObject obj = new JsonObject();
            JsonArray  arr = DataCarInfo.toJsonArrayLocal(carInfoList);
            obj.add("carList", arr);
            if(user !=null){
                ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "carList", ODBHelper.convertString(obj));
            }
        }
    }

    // ==========================get=================================
//    public List<PopChooseCarWarp.DataCarChoose> getCarAnnualList() {
//        List<PopChooseCarWarp.DataCarChoose> list = new ArrayList<PopChooseCarWarp.DataCarChoose>();
//        if (getCarInfoList() == null) return list;
//        for (int i = 0; i < carInfoList.size(); i++) {
//            DataCarInfo car = carInfoList.get(i);
//            if (car.isActive == 1) {
//                PopChooseCarWarp.DataCarChoose data = new PopChooseCarWarp().new DataCarChoose();
//                data.carId = car.ide;
//                data.carLogo = car.logo;
//                data.carName = car.num;
//                list.add(data);
//            }
//        }
//        return list;
//    }
    public List<DataCarInfo> getCarListActive() {
        if (getCarInfoList() == null) return new ArrayList<DataCarInfo>();
        List<DataCarInfo> list = new ArrayList<DataCarInfo>();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.isActive == 1) list.add(car);
        }
        return list;
    }

    public String[] getCarNameListOther() {
        if (carInfoListOther == null) return new String[]{};
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < carInfoListOther.size(); i++) {
            DataCarInfo car = carInfoListOther.get(i);
            if (car.isActive == 1) list.add(car.num);
        }
        return list.toArray(new String[list.size()]);
    }
    public String[] getCarNameListActive() {
        if (getCarInfoList() == null) return new String[]{};
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.isActive == 1) list.add(car.num);
        }
        return list.toArray(new String[list.size()]);
    }

    public String[] getCarNameListAll() {
        if (getCarInfoList() == null || carInfoList.size() == 0) {
            return new String[]{};
        }
        String[] arr = new String[carInfoList.size()];
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            arr[i] = car.num;
        }
        return arr;
    }

    public long getCarIdByName(String carName) {
        if (getCarInfoList() == null) return -1;
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.num.equals(carName)) {
                return car.ide;
            }
        }
        return -1;
    }
    public DataCarInfo getCarByNameOther(String carName) {
        if (carInfoListOther == null) return null;
        for (int i = 0; i < carInfoListOther.size(); i++) {
            DataCarInfo car = carInfoListOther.get(i);
            if (car.num.equals(carName)) {
                return car;
            }
        }
        return null;
    }

    public DataCarInfo getCarByName(String carName) {
        if (getCarInfoList() == null) return null;
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.num.equals(carName)) {
                return car;
            }
        }
        return null;
    }

    //only for viewcar
    public DataCarInfo getCarByID(long carId) {
        if (getCarInfoList() == null) return new DataCarInfo();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.ide == carId) {
                return car;
            }
        }
        return new DataCarInfo();
    }
    // ==========================get=================================

    public long getCurrentCarID() {//不要缓存，重等数据不会刷新
        return getCurrentCar().ide;
    }

    public DataCarInfo getCurrentCar() {//不要缓存，重等数据不会刷新
        boolean haveCar = false;
        if (getCarInfoList() == null) return new DataCarInfo();
        for (DataCarInfo car : carInfoList) {
            if (car.ide == currentCarId) {
                return car;
            }
        }
        if (!haveCar && carInfoList!=null&&carInfoList.size() > 0){
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if(user !=null){
                String id = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(user.userId, "currentCarID");
                if(id!=null && id.length()>0){
                    for (DataCarInfo car1 : carInfoList) {
                        if (car1.ide == Integer.valueOf(id)) {
                            currentCarId = car1.ide;
                            return car1;
                        }
                    }
                }
            }
            return carInfoList.get(0);
        }
        return new DataCarInfo();
    }
    public void setCurrentCar(long carId) {
        if (getCarInfoList() == null) return;
        boolean haveCar = false;
        for (DataCarInfo car : carInfoList) {
            if (car.ide == carId) {
                currentCarId = carId;
                haveCar = true;
                DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                if(user !=null){
                    ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "currentCarID", ""+currentCarId);
                }
                ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);
            }
        }
        if (!haveCar) carId = 0;
    }
    // ==========================get=================================

    public void setCurrentCarByName(String carname) {
        if (getCarInfoList() == null) return;
        for (DataCarInfo car : carInfoList) {
            if (car.num.equals(carname)) {
                setCurrentCar(car.ide);
                return;
            }
        }
    }
    /**列表回来后立即检查是否有新的借车*/
    public void checkBrowerCar() {
        if (getCarInfoList() == null) return;
        for (DataCarInfo car : carInfoList) {
            if (car.isMyCar == 0) {
                setCurrentCar(car.ide);
                return;
            }
        }
    }

    // ==========================set=================================
    public void exit() {
        carInfoList = new ArrayList<DataCarInfo>();
        saveCarListLocal();
    }

    public void saveCarList(JsonArray carInfos, String fromPosForTest) {
        if (carInfos != null) {
            this.carInfoList = DataCarInfo.fromJsonArray(carInfos);
        } else {
            this.carInfoList = new ArrayList<DataCarInfo>();
        }
        saveCarListLocal();
    }

    public void saveCarStatus(JsonObject carStatus) {
        DataCarInfo carInfo = getCurrentCar();
        DataCarStatus getStatus = DataCarStatus.fromJsonObject(carStatus);
        if(carInfo.ide !=getStatus.carId)return;
        currentCarStatus = getStatus;
    }
    public void saveCurrCarFunButtons(JsonArray jsonArray) {
        if (jsonArray == null) return;
        getCurrentCar().funInfos = jsonArray;
    }
}
