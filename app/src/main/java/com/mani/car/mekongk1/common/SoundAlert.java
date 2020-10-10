package com.mani.car.mekongk1.common;

import android.util.Log;

import com.mani.car.mekongk1.R;

import java.util.HashMap;

@Deprecated
public class SoundAlert {
    private static HashMap<Integer, Integer> voiceList;
    // =========================================
    public static int getSoundIdFrom(int alertId) {
        if (voiceList == null) {
            voiceList = new HashMap<Integer, Integer>();
            voiceList.put(1, R.raw.voice_01_startcar);
            voiceList.put(2, R.raw.voice_02_openbackpag);
            voiceList.put(3, R.raw.voice_03_lock);
            voiceList.put(4, R.raw.voice_04_unlock);
            voiceList.put(5, R.raw.voice_05_stopcar);
            voiceList.put(6, R.raw.voice_06_lockfail_lfdoorfail);
            voiceList.put(7, R.raw.voice_07_lockfail_rfdoorfail);
            voiceList.put(8, R.raw.voice_08_lockfail_lbdoorfail);
            voiceList.put(9, R.raw.voice_09_lockfail_rbdoorfail);
            voiceList.put(10, R.raw.voice_10_lockfail_innerlightfail);
            voiceList.put(11, R.raw.voice_11_lockfail_backpagfail);
            voiceList.put(12, R.raw.voice_12_lockok_innerlightfail);
            voiceList.put(13, R.raw.voice_13_lockok_backpagfail);
            voiceList.put(14, R.raw.voice_14_start_fail);
            voiceList.put(15, R.raw.voice_15_stopcar_fail);
//            voiceList.put(16, R.raw.voice_16_areainto);
            voiceList.put(17, R.raw.voice_17_areaout);
//            voiceList.put(18, R.raw.voice_18_toofast);
            voiceList.put(19, R.raw.voice_19_lowpower);

            voiceList.put(21, R.raw.voice_21_uclock_car);
            voiceList.put(22, R.raw.voice_22_window_lf_unclose);
            voiceList.put(23, R.raw.voice_23_window_rf_unclose);
            voiceList.put(24, R.raw.voice_24_window_lb_unclose);
            voiceList.put(25, R.raw.voice_25_window_rb_unclose);
            voiceList.put(26, R.raw.voice_26_cutline);
//            voiceList.put(27, R.raw.voice_27_carmove);
            voiceList.put(28, R.raw.voice_28_dooropen_unpermission);
            voiceList.put(29, R.raw.voice_29_backpag_open_unconfirm);
            voiceList.put(30, R.raw.voice_30_startcar_unpermission);
            voiceList.put(31, R.raw.voice_31_car_online_off);
//            voiceList.put(32, R.raw.voice_32_car_unstoped);
            voiceList.put(33, R.raw.voice_33_backpag_close);
            voiceList.put(34, R.raw.voice_34_power_unclose);
            voiceList.put(37, R.raw.voice_37_backpag_unclose);
            voiceList.put(39, R.raw.voice_39_skylight_unclose);

//            voiceList.put(60, R.raw.voice_start);
//            voiceList.put(61, R.raw.voice_lock);
//            voiceList.put(62, R.raw.voice_backpag);
//            voiceList.put(63, R.raw.voice_findcar);
//            voiceList.put(64, R.raw.voice_warrning);
//
//            voiceList.put(70, R.raw.voice_lock);//for keep
            Log.e("SoundPlay", "init3 voiceList size:" + voiceList.size());
        }
        try {
            return voiceList.get(alertId);
        }catch (NullPointerException e){
            return 0;//无此声音
        }
    }

}





