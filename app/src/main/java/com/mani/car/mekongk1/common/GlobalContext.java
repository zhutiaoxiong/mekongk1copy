package com.mani.car.mekongk1.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.igexin.sdk.PushManager;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.BuildConfig;
import com.mani.car.mekongk1.common.socketutils.BootBroadcastReceiver;
import com.mani.car.mekongk1.ctrl.OCtrlBaseHttp;
import com.mani.car.mekongk1.model.ManagerGesture;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.gusturepassword.ActivityGesturePassword;
import com.mani.car.mekongk1.ui.home.ActivityHome;

import java.lang.ref.WeakReference;

public class GlobalContext extends Application implements Thread.UncaughtExceptionHandler{
    private static Context                 context;
    private static WeakReference<Activity> sCurrentActivityWeakRef;
    public static boolean IS_DEBUG_MODEL = false;

    private int activityAount = 0;//可见的activity数量
    private        Thread.UncaughtExceptionHandler mDefaultHandler;
    @Override
//    public void attachBaseContextByDaemon(Context base) {
//        super.attachBaseContextByDaemon(base);
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        //加载推送
        PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyPushReceiveService.class);
        OCtrlBaseHttp.getInstance().init();
        //安卓7.0拍照适配
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        ApplicationInfo info= context.getApplicationInfo();
        IS_DEBUG_MODEL = (info.flags& ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        super.onCreate();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        BootBroadcastReceiver.getInstance().initReceiver(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(Activity activity) {
                activityAount--;
//                if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<Stopped activity>>> : "+activity.getClass().getName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
//                if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<Started activity>>> : "+activity.getClass().getName());
                if (activityAount == 0 || ActivityHome.isFirstInto) {
                    if(activityAount != 0 && ActivityHome.isFirstInto)ActivityHome.isFirstInto = false;
//                    if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<StartedCheckGesture activity>>> : "+activity.getClass().getName());
                    //app回到前台
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                    String token = PHeadHttp.getInstance().getToken();
                    if (user == null || user.userId == 0 || token == null || token.length() == 0) {

                    }else{
                        String isOpenGesture= ManagerGesture.getInstance().getIsOpenGesture();
                        if(isOpenGesture==null)return;
                        if (isOpenGesture .equals("开启")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        if(ManagerPublicData.isNotPopGusture){
                                            ManagerPublicData.isNotPopGusture =false;
                                        }else{
                                            ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityGesturePassword.class);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
                activityAount++;
//                Log.e("APP","onActivityStarted activityAount:"+activityAount);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (sCurrentActivityWeakRef != null) sCurrentActivityWeakRef.clear();
                sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
//                if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<Resumed activity>>> : "+activity.getClass().getName());

            }

            @Override
            public void onActivityPaused(Activity activity) {
//                if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<Paused activity>>> : "+activity.getClass().getName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<Destroyed activity>>> : "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if(BuildConfig.DEBUG)Log.e("GlobalContext","<<<<Created activity>>> : "+activity.getClass().getSimpleName());
            }
        });
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handleException(ex) && mDefaultHandler != null){
            mDefaultHandler.uncaughtException(thread,ex);
        }
    }
    private boolean handleException(Throwable ex){
        if (ex == null || context == null)
            return false;
        final String crashReport = getCrashReport(context, ex);
        Log.e("handleException","全局异常"+crashReport);
        return true;
    }
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo  pinfo        = getPackageInfo(context);
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: " + pinfo.versionName + "("
                + pinfo.versionCode + ")\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    public static Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }
    /**
     * @return the context
     */
    public static Context getContext() {
        return context;
    }
    /**
     * 设置状态栏颜色 5.0 以上,这里保留页面色
     */
    private static int pageColor = -1;
    public static void setStatusBarColorForPage(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(getCurrentActivity()!=null) {
                getCurrentActivity().getWindow().setStatusBarColor(color);
                pageColor = color;
            }
        }
    }
  public  static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
    /**
     * 回复,保留的页面色 5.0 以上
     */
    public static void setStatusBarColorRecover(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(getCurrentActivity()!=null)getCurrentActivity().getWindow().setStatusBarColor(pageColor);
        }
    }
    /**
     * 弹出消息,这里不保留页面色
     */
    public static void popMessage(String message,int colorValue){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(getCurrentActivity()!=null) {
                if(getCurrentActivity()!=null){
                    ClipTipWindow.getInstance().show(getCurrentActivity(),message,colorValue);
                }
            }
        }
    }

//    @Override
//    public void callback(String key, Object value) {
//        if (SocketConnSer.SOCKET_RECEIVE_MESSAGE.equals(key)) {
//            final JsonObject obj = (JsonObject) value;
//            if (obj == null) return;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int cmd = OJsonGet.getInteger(obj, "cmd");
//                    if (cmd == 101) return;// 第一个消息还未初始化
//                    // 判断是否提示消息
//                    int mType = OJsonGet.getInteger(obj, "mType");
//                    if (cmd == 3 && mType == 4) {// 消息推送
//                        int isNotice = OJsonGet.getInteger(obj, "isNotice");
//                        if (isNotice == 1) {
//                            JsonObject data       = OJsonGet.getJsonObject(obj, "data");
//                            int        alertType  = OJsonGet.getInteger(data, "alertType");// 1：消息，2：警报，3：安全
//                            String     content    = OJsonGet.getString(data, "content");
//                            long       createTime = OJsonGet.getLong(data, "createTime");
//                            Intent     broadcastB = new Intent();
//                            broadcastB.setAction("SERVICE_B_NEED_SENDMESSAGE");
//                            broadcastB.putExtra("title", "么控K1消息提醒:");
//                            broadcastB.putExtra("alertType", alertType);
//                            broadcastB.putExtra("info", content + "  " + ODateTime.time2StringHHmm(createTime));
//                            sendBroadcast(broadcastB);
//                            try {
//                                Thread.sleep(400L);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    // 返回数据给UI
//                    Intent broadcast = new Intent();
//                    broadcast.setAction("android.intent.action.SERVICE_A_BACKMESSAGE");
//                    broadcast.putExtra("SERVICE_A_BACKMESSAGE_OBJ", obj.toString());
//                    sendBroadcast(broadcast);
//                }
//            }).start();
//        }
//    }
    /*public static boolean isInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                //BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;//后台
                } else {//前台
                    return false;
                }
            }
        }
        return true;
    }*/
    //==============================
}
