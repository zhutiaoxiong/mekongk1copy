package com.mani.car.mekongk1.ui.personcenter.carmanager.voice;

import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.mani.car.mekongk1.common.GlobalContext;

public class MyVoiceSelect {
    private String startVoice="启动默认";
    private String alertVoice = "警报音效默认";
    private String lockUpVoice = "上落锁音效默认";
    private String clickCarVoice = "点击车音效默认";
    private String scrollButtonVoice = "滑动按钮音效默认";
    // ========================out======================
    private static MyVoiceSelect _instance;
    private MyVoiceSelect() {
        init();
    }
    public static MyVoiceSelect getInstance() {
        if (_instance == null)
            _instance = new MyVoiceSelect();
        return _instance;
    }
    private void init() {
        loadLocalData();
    }

    // =================================================
    public void loadLocalData() {
        String localStartVoice=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("startVoice");
        if(localStartVoice!=null&&!localStartVoice.equals("")){
            startVoice=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("startVoice");
        }
        String localAlertVoice=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("alertVoice");
        if(localAlertVoice!=null&&!localAlertVoice.equals("")){
            alertVoice=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("alertVoice");
        }
        String localLockUpVoice=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("lockUpVoice");
        if(localLockUpVoice!=null&&!localLockUpVoice.equals("")){
            lockUpVoice=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("lockUpVoice");
        }
        String localClickCarVoice=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("clickCarVoice");
        if(localClickCarVoice!=null&&!localClickCarVoice.equals("")){
            clickCarVoice=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("clickCarVoice");
        }
        String localScrollButtonVoice=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("scrollButtonVoice");
        if(localScrollButtonVoice!=null&&!localScrollButtonVoice.equals("")){
            scrollButtonVoice=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("scrollButtonVoice");
        }
    }
    public String getStartVoice() {
        return startVoice;
    }

    public String getAlertVoice() {
        return alertVoice;
    }

    public String getLockUpVoice() {
        return lockUpVoice;
    }

    public String getScrollButtonVoice() {
        return scrollButtonVoice;
    }
    public String getClickCarVoice() {
        return clickCarVoice;
    }

    public void setStartVoice(String startVoice) {
        this.startVoice = startVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("startVoice",startVoice);
    }

    public void setAlertVoice(String alertVoice) {
        this.alertVoice = alertVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("alertVoice",alertVoice);
    }

    public void setClickCarVoice(String clickCarVoice) {
        this.clickCarVoice = clickCarVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("clickCarVoice",clickCarVoice);
    }

    public void setScrollButtonVoice(String scrollButtonVoice) {
        this.scrollButtonVoice = scrollButtonVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("scrollButtonVoice",scrollButtonVoice);
    }


    public void setLockUpVoice(String lockUpVoice) {

        this.lockUpVoice = lockUpVoice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("lockUpVoice",lockUpVoice);
    }

    // =================================================
}
