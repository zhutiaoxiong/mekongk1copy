package com.mani.car.mekongk1.model;

import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

public class ManagerVoiceSet {
    private        int             isOpenStartVoice  = -1;//-1开启1关闭
    private        int             startResId;//启动声音
    private        int             isOpenAlertVoice  = -1;//-1开启1关闭
    private        int             alertResId;//警报声音
    private        int             isOpenLockUpVoice = -1;//-1开启1关闭
    private        int             LockUpResId;//上落锁资源
    private        int             isOpenClickCarVoice= -1;//点击车音效//-1开启1关闭
    private        int             clickCarVoiceResId;//点击车音效
    private        int             isOpenScrollButtonVoice= 1;//滑动按钮音效//-1开启1关闭
    private        int             scrollButtonVoiceResId;//滑动按钮音效
    private static ManagerVoiceSet _instance;

    private ManagerVoiceSet() {
        init();
    }

    public static ManagerVoiceSet getInstance() {
        if (_instance == null)
            _instance = new ManagerVoiceSet();
        return _instance;
    }
    // =================================================
    private void init() {
        String isOpenStartVoiceStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenStartVoice");
        if(isOpenStartVoiceStr!=null&&!isOpenStartVoiceStr.equals("")){
            isOpenStartVoice = Integer.parseInt(isOpenStartVoiceStr);
        }

        String startResIdStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("startResId");
        if(startResIdStr!=null&&!startResIdStr.equals("")){
            startResId = Integer.parseInt(startResIdStr);
        }

        String isOpenAlertVoiceStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenAlertVoice");
        if(isOpenAlertVoiceStr!=null&&!isOpenAlertVoiceStr.equals("")){
            isOpenAlertVoice = Integer.parseInt(isOpenAlertVoiceStr);
        }

        String alertResIdStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("alertResId");
        if(alertResIdStr!=null&&!alertResIdStr.equals("")){
            alertResId = Integer.parseInt(alertResIdStr);
        }

        String isOpenLockUpVoiceStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenLockUpVoice");
        if(isOpenLockUpVoiceStr!=null&&!isOpenLockUpVoiceStr.equals("")){
            isOpenLockUpVoice = Integer.parseInt(isOpenLockUpVoiceStr);
        }

        String LockUpResIdStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("LockUpResId");
        if(LockUpResIdStr!=null&&!LockUpResIdStr.equals("")){
            LockUpResId = Integer.parseInt(LockUpResIdStr);
        }
        String isOpenClickCarVoiceStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenClickCarVoice");
        if(isOpenClickCarVoiceStr!=null&&!isOpenClickCarVoiceStr.equals("")){
            isOpenClickCarVoice = Integer.parseInt(isOpenClickCarVoiceStr);
        }

        String clickCarVoiceResIdStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("clickCarVoiceResId");
        if(clickCarVoiceResIdStr!=null&&!clickCarVoiceResIdStr.equals("")){
            clickCarVoiceResId = Integer.parseInt(clickCarVoiceResIdStr);
        }
        String isOpenScrollButtonVoiceStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenScrollButtonVoice");
        if(isOpenScrollButtonVoiceStr!=null&&!isOpenScrollButtonVoiceStr.equals("")){
            isOpenScrollButtonVoice = Integer.parseInt(isOpenScrollButtonVoiceStr);
        }
        String scrollButtonVoiceResIdStr=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("scrollButtonVoiceResId");
        if(scrollButtonVoiceResIdStr!=null&&!scrollButtonVoiceResIdStr.equals("")){
            scrollButtonVoiceResId = Integer.parseInt(scrollButtonVoiceResIdStr);
        }


        if (startResId == 0 || startResId == -1) {
            saveStartVoiceResId(R.raw.start_a_default);
        }
        if (alertResId == 0 || alertResId == -1) {
            saveAlertVoiceId(R.raw.alert_message_a_default);
        }
        if (LockUpResId == 0 || LockUpResId == -1) {
            saveLockUpVoiceResId(R.raw.lock_up_a_deafault);
        }
        if (clickCarVoiceResId == 0 || clickCarVoiceResId == -1) {
            saveClickCarVoiceResId(R.raw.voice_click_car_body);
        }
        if (scrollButtonVoiceResId == 0 || scrollButtonVoiceResId == -1) {
            saveScrollButtonVoiceResId(R.raw.voice_control_page_change);
        }
    }
    // =================================================
    public int getIsOpenStartVoice() {
        return isOpenStartVoice;
    }
    public int getStartResId() {
        return startResId;
    }
    // =================================================
    public void saveIsOpenStartVoice(int isOpen) {
        isOpenStartVoice = isOpen;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenStartVoice",String.valueOf(isOpenStartVoice));
    }
    public void saveStartVoiceResId(int resId) {
        startResId = resId;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("startResId",String.valueOf(startResId));
    }

    public int getIsOpenAlertVoice() {
        return isOpenAlertVoice;
    }
    public int getAlertVoiceResId() {
        return alertResId;
    }
    // =================================================
    public void saveIsOpenAlertVoice(int isOpen) {
        isOpenAlertVoice = isOpen;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenAlertVoice",String.valueOf(isOpenAlertVoice));
    }
    public void saveAlertVoiceId(int resId) {
        alertResId = resId;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("alertResId",String.valueOf(alertResId));
    }

    public int getIsOpenLockUpVoice() {
        return isOpenLockUpVoice;
    }
    public int getLockUpVoiceResId() {
        return LockUpResId;
    }

    public int getIsOpenClickCarVoice() {
        return isOpenClickCarVoice;
    }

    public void saveIsOpenClickCarVoice(int isOpenClickCarVoice) {
        this.isOpenClickCarVoice = isOpenClickCarVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenClickCarVoice",String.valueOf(isOpenClickCarVoice));

    }

    public int getClickCarVoiceResId() {
        return clickCarVoiceResId;
    }

    public void saveClickCarVoiceResId(int clickCarVoiceResId) {
        this.clickCarVoiceResId = clickCarVoiceResId;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("clickCarVoiceResId",String.valueOf(clickCarVoiceResId));

    }

    public int getIsOpenScrollButtonVoice() {
        return isOpenScrollButtonVoice;
    }

    public void saveIsOpenScrollButtonVoice(int isOpenScrollButtonVoice) {
        this.isOpenScrollButtonVoice = isOpenScrollButtonVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenScrollButtonVoice",String.valueOf(isOpenScrollButtonVoice));

    }

    public int getScrollButtonVoiceResId() {
        return scrollButtonVoiceResId;
    }

    public void saveScrollButtonVoiceResId(int scrollButtonVoiceResId) {
        this.scrollButtonVoiceResId = scrollButtonVoiceResId;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("scrollButtonVoiceResId",String.valueOf(scrollButtonVoiceResId));
    }

    // =================================================
    public void saveIsOpenLockUpVoice(int isOpen) {
        isOpenLockUpVoice = isOpen;

        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenLockUpVoice",String.valueOf(isOpenLockUpVoice));
    }
    public void saveLockUpVoiceResId(int resId) {
        LockUpResId = resId;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("LockUpResId",String.valueOf(LockUpResId));
    }
}
