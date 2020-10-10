package com.mani.car.mekongk1.common;

import android.util.Log;

import java.util.HashMap;

public class SoundAlertZhu {
    private static HashMap<Integer, String> voiceList;
    // =========================================
    public static String getSoundIdFrom(int alertId) {
        if (voiceList == null) {
            voiceList = new HashMap<Integer, String>();
            voiceList.put(1, "voice_01_startcar");
            voiceList.put(2,  "voice_02_openbackpag");
            voiceList.put(3, "voice_03_lock");
            voiceList.put(4, "voice_04_unlock");
            voiceList.put(5, "voice_05_stopcar");
            voiceList.put(6, "voice_06_lockfail_lfdoorfail");
            voiceList.put(7, "voice_07_lockfail_rfdoorfail");
            voiceList.put(8, "voice_08_lockfail_lbdoorfail");
            voiceList.put(9, "voice_09_lockfail_rbdoorfail");
            voiceList.put(10, "voice_10_lockfail_innerlightfail");
            voiceList.put(11, "voice_11_lockfail_backpagfail");
            voiceList.put(12, "voice_12_lockok_innerlightfail");
            voiceList.put(13, "voice_13_lockok_backpagfail");
            voiceList.put(14, "voice_14_start_fail");
            voiceList.put(15, "voice_15_stopcar_fail");
//            voiceList.put(16, ""voice_16_areainto);
            voiceList.put(17, "voice_17_areaout");
//            voiceList.put(18, ""voice_18_toofast);
            voiceList.put(19, "voice_19_lowpower");

            voiceList.put(21, "voice_21_uclock_car");
            voiceList.put(22, "voice_22_window_lf_unclose");
            voiceList.put(23, "voice_23_window_rf_unclose");
            voiceList.put(24, "voice_24_window_lb_unclose");
            voiceList.put(25, "voice_25_window_rb_unclose");
            voiceList.put(26, "voice_26_cutline");
//            voiceList.put(27, ""voice_27_carmove);
            voiceList.put(28, "voice_28_dooropen_unpermission");
            voiceList.put(29, "voice_29_backpag_open_unconfirm");
            voiceList.put(30, "voice_30_startcar_unpermission");
            voiceList.put(31, "voice_31_car_online_off");
//            voiceList.put(32, ""voice_32_car_unstoped);
            voiceList.put(33, "voice_33_backpag_close");
            voiceList.put(34, "voice_34_power_unclose");
            voiceList.put(37, "voice_37_backpag_unclose");
            voiceList.put(39, "voice_39_skylight_unclose");

//            voiceList.put(60, ""voice_start);
//            voiceList.put(61, ""voice_lock);
//            voiceList.put(62, ""voice_backpag);
//            voiceList.put(63, ""voice_findcar);
//            voiceList.put(64, ""voice_warrning);
//
//            voiceList.put(70, ""voice_lock);//for keep
            Log.e("SoundPlay", "init3 voiceList size:" + voiceList.size());
        }
        try {
            return voiceList.get(alertId);
        }catch (NullPointerException e){
            return "";//无此声音
        }
    }

}





