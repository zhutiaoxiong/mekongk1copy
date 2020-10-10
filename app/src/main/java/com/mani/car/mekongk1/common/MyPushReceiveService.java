package com.mani.car.mekongk1.common;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.mani.car.mekongk1.BuildConfig;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.ctrl.OCtrlSocketMsg;

import java.io.UnsupportedEncodingException;

public class MyPushReceiveService extends GTIntentService {


    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        try {
            String result = new String(msg.getPayload(), SystemMe.UTF8);
            if(result == null || result.length() == 0){
                GlobalContext.popMessage("推送消息出错", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
            }else {
                OCtrlSocketMsg.getInstance().onReceiveData(result);
                if (BuildConfig.DEBUG) Log.e(TAG, "onReceiveMessageData -> " + "msg = " + result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        if(BuildConfig.DEBUG)Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        PHeadHttp.getInstance().setDeviceToken(clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        if(BuildConfig.DEBUG)Log.e(TAG, "onReceiveCommandResult -> " + "msg = " + cmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        if(BuildConfig.DEBUG)Log.e(TAG, "onNotificationMessageArrived -> " + "msg = " + msg);
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
    }
}
