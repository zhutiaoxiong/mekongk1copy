package com.mani.car.mekongk1.common.socketutils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.NotificationUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_1_HEART_BEET;
import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_INIT_NOTIFI_GET;
import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_INIT_NOTIFI_POST;
import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_INIT_SOCKET_GET;
import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_INIT_SOCKET_POST;
import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_RECEIVE_MESSAGE;
import static com.mani.car.ankulak1.common.blue.BlueStaticValue.SERVICE_SEND_MESSAGE;


/**
 * @author Administrator
 */
public class KulalaServiceC extends Service {
    public static KulalaServiceC kulalaServiceCThis;
    public static long service1HeartTime = 0;
    private ServiceReceiverC myReceiver;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public void onCreate() {
        Log.e("<ServiceC>", "<<<<<onCreate>>>>>>");
        LogMeLinks.init(this);
        try {
            if (myReceiver == null) {
                myReceiver = new ServiceReceiverC();
                IntentFilter filter = new IntentFilter();
                filter.addAction(SERVICE_INIT_SOCKET_GET);
                filter.addAction(SERVICE_INIT_NOTIFI_GET);
                filter.addAction(SERVICE_SEND_MESSAGE);
//                filter.addAction(SERVICE_1_HEART_BEET);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_SCREEN_ON);
                registerReceiver(myReceiver, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SocketConnSer.getInstance().init(this);
//        SoundPlay.getInstance().init(this);
        if(SocketUtil.initData(KulalaServiceC.this)){
            SocketConnSer.getInstance().reConnect("SERVICE_A_INIT_SOCKET");//初始化成功后，重连接
        }
//        ScreenListen.getInstance().registerReceiverOnCreate(this);
        super.onCreate();
    }
//    private void acquireWakeLock() {
//        if (null != wakeLock) return;
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ServiceCWake");
//        if (null != wakeLock) wakeLock.acquire();
//    }
//    //释放设备电源锁
//    private void releaseWakeLock() {
//        if (null == wakeLock) return;
//        wakeLock.release();
//        wakeLock = null;
//    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("<ServiceC>", "<<<<<onStartCommand>>>>>>");
        SocketHeartThread.getInstance().startThread();
        needInitNotification();
        needInitSoki();


        if (kulalaServiceCThis == null){
            NotificationUtils notificationUtils = new NotificationUtils(this);
//            Notification noti              = notificationUtils.sendNotification("酷啦啦提醒您:", "酷啦啦提醒您云启动已开启");
//            startForeground(NotificationUtils.NOTI_ID,noti);
        }
        kulalaServiceCThis = this;
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        SocketConnSer.getInstance().setOnConnStateChangeListener(null);
        SocketConnSer.getInstance().close();
        kulalaServiceCThis = null;
        LogMeLinks.e("<ServiceC>", "<<<<<onDestroy>>>>>>");
        unregisterReceiver(myReceiver);//不要发stop,会一直重复
        super.onDestroy();
    }
    // ==============================

    // Broadcast
    private class ServiceReceiverC extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
//                releaseWakeLock();
            }else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
//                acquireWakeLock();
            }if (SERVICE_INIT_SOCKET_GET.equals(intent.getAction())) {
                Log.e("ServiceC","收到初始化数据");
                String jsonObj = intent.getStringExtra("jsonObjPHead");
                initSoki(content,SocketUtil.str2JsonObj(jsonObj));
            }else if (SERVICE_INIT_NOTIFI_GET.equals(intent.getAction())) {
                LogMeLinks.e("ServiceC","收到:"+SERVICE_INIT_NOTIFI_GET);
//                int IconId = intent.getIntExtra("IconId",0);
//                String projName = intent.getStringExtra("projName");
                boolean soundOpen = intent.getBooleanExtra("soundOpen",true);
                boolean vitratorOpen = intent.getBooleanExtra("vitratorOpen",true);
                initNotification(content,soundOpen,vitratorOpen);
            }else if (SERVICE_SEND_MESSAGE.equals(intent.getAction())) {
                //sendMessage(2, "")心跳
                int    cmd  = intent.getIntExtra("cmd", 0);
                String json = intent.getStringExtra("json");
                LogMeLinks.e("TsControl","收到UI发包请求 cmd:"+cmd);
                sendMessage(cmd, json);
            }else if (SERVICE_1_HEART_BEET.equals(intent.getAction())) {
                service1HeartTime = System.currentTimeMillis();
            }
        }
    }
    // ==============================
    public void needInitSoki(){
        Log.e("ServiceC","请求初始化数据");
        Intent broadcast = new Intent(SERVICE_INIT_SOCKET_POST);
        sendBroadcast(broadcast);
    }
    public void needInitNotification(){
        Intent broadcast = new Intent(SERVICE_INIT_NOTIFI_POST);
        sendBroadcast(broadcast);
    }
    public void needDataBackUI(JsonObject obj){//dataGet
        Intent broadcast = new Intent(SERVICE_RECEIVE_MESSAGE);
        broadcast.putExtra("service_receive_obj", obj.toString());
        sendBroadcast(broadcast);
        Log.e("ServiceC","DataBackUI");
    }
    public void initSoki(final Context context, final JsonObject jsonSockInitData){
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSocket(context);
                boolean isNewChange =SocketUtil.changeData(context,jsonSockInitData);
                if(isNewChange && !TextUtils.isEmpty(SocketUtil.getUserId(context))){
                    SocketConnSer.getInstance().changeUserId();
                }
            }
        }).start();
    }
    public void initNotification(Context globalContext, boolean soundOpen, boolean vitratorOpen){
//        SocketDataGet.IconId1 = IconId;
//        SocketDataGet.projName1 = projName;
        SocketDataGet.isInitedNoti = true;
        SocketDataGet.openSound1 = soundOpen;
        SocketDataGet.openVibrator1 = vitratorOpen;
    }
    //=================================================================
    public void sendMessage(int cmd, String jsonData){
        if(SocketUtil.getSocketPort(this) == 0){
            Log.e("TsControl", "没拿到端口号重连");
            needInitSoki();
        }else{
            SocketConnSer.getInstance().sendMessage(cmd, jsonData);
        }
    }
    //=================================================================

    private void initSocket(final Context context1){
        SocketConnSer.getInstance().init(context1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异常断线重连
                Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread thread, Throwable ex) {
                        Writer result      = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(result);
                        ex.printStackTrace(printWriter);
                        String stacktrace = result.toString();
                        if(SocketUtil.getSocketPort(context1)!=0)SocketConnSer.getInstance().reConnect("setDefaultUncaughtExceptionHandler"+stacktrace);
                    }
                });
            }
        }).start();//netWork in main thread
        SocketConnSer.getInstance().setOnConnStateChangeListener(new OnSocketStateListener() {
            @Override
            public void onConnFailed(String failedInfo) {
            }
            @Override
            public void onSendOK(int cmd) {
            }
            @Override
            public void onSendFailed(int cmd, String failedInfo) {
            }
        });
    }
    //=================================================================
}
