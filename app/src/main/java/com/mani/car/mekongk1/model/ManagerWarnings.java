package com.mani.car.mekongk1.model;

import com.google.gson.JsonObject;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.carcontrol.DataWarnings;

/**
 * 警告列表
 * 1.初始化更新消息0-19
 * 2.下移上移刷新消息
 * 3.如果选中了时间，就显示时间泛围内的
 * 4.初进先删除所有数据，其它全保存
 */

public class ManagerWarnings {
    private        DataWarnings    moveingShowWarnings;//keep max 1
    // ========================out======================
    private static ManagerWarnings _instance;
    public static ManagerWarnings getInstance() {
        if (_instance == null)
            _instance = new ManagerWarnings();
        return _instance;
    }
    // ========================out======================

    public DataWarnings getNewWarnings() {
        return moveingShowWarnings;
    }

    //动态收到socket消息不加入数据库
    public void saveNewWarnings(final JsonObject data) {
        DataWarnings war = DataWarnings.fromJsonObject(data);
        moveingShowWarnings = war;
        showWarning();
    }
    //重新播放了，就清掉已看消息
    public void showWarning(){
        if(moveingShowWarnings == null || moveingShowWarnings.content == null)return;
        int color = moveingShowWarnings.alertType == 1 ? R.color.popTipNormal:R.color.popTipWarning;//1：消息，2：警报，3：安全
        GlobalContext.popMessage(moveingShowWarnings.content,GlobalContext.getContext().getResources().getColor(color));
    }
    public void clearWarning() {
        moveingShowWarnings = null;
    }
    //===========================================================

}
