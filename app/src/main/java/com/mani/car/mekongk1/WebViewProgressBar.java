package com.mani.car.mekongk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import com.kulala.links.http.HttpConn;

public class WebViewProgressBar extends ProgressBar  implements HttpConn.OnHttpStatusListner {

    public final static int CHANGE_FIRST = 7000;//起始快速所走长度
    public final static int CHANGE_SECOND = 9000;//中间减速段长度和起始长度总和
    public final static int TIME_FIRST = 1000;//起始快速所需时间
    public final static int TIME_SECOND = 5000;//中间减速段时间和起始时间总和
    public final static int TIME_SLEEP = 10;//ProgressBar每次更新进度时间

    private int schedule = 0;// web加载时进度条当前进度
    private int isFinishSchedule = 100000;// web加载完成时进度条当前进度
    private boolean isFinish = false;//web是否加载完成
    private myThread myThread;

//    public WebViewProgressBar(Context context) {
//        super(context);
//        HttpConn.getInstance().setHttpStatus(this);
//    }

    public WebViewProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        HttpConn.getInstance().setHttpStatus(this);
        Log.e("进度条", "初始化"+testInfo );
    }
//
//    public WebViewProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        HttpConn.getInstance().setHttpStatus(this);
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("进度条", "onAttachedToWindow"+testInfo );
        HttpConn.getInstance().setHttpStatus(this,testInfo);
    }
    private String testInfo = "";
    public void setTestInfo(String info){
        testInfo = info;
    }
    public void onResumeControlCar(){
        Log.e("进度条", "控车页面回复,"+testInfo );
        HttpConn.getInstance().setHttpStatus(this,testInfo);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("进度条", "onDetachedFromWindow"+testInfo );
        HttpConn.getInstance().removeHttpStatus(testInfo);
    }

    /**
     * 进度条开始加载，刷新可重复调用
     */
    public void startProgress() {
        setVisibility(View.VISIBLE);
        setMax(10000);
        setProgress(0);
        isFinish = false;
        if (myThread!= null){
            myThread.interrupt();
        }
        myThread = new myThread();
        myThread.start();
    }

    /**
     * 加载完成结束进度条
     */
    public void finishProgress() {
        isFinish = true;
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            schedule = msg.what;
            setProgress(msg.what);
            if(msg.what > isFinishSchedule){
                Animation animation = new AlphaAnimation(1.0f, 0.3f);
                animation.setDuration(400);
                startAnimation(animation);
            }
            if (msg.what >= 10000) {
                setVisibility(View.GONE);
            }
        }

    };

    @Override
    public void begin() {
        Log.e("进度条", "begin: http开始"+testInfo );
//        startProgress();
 if(!isFinish)return;
       handleProgress(1);
    }

    @Override
    public void success() {
        Log.e("进度条", "begin: http成功相应"+testInfo );
//        finishProgress();
        handleProgress(2);
    }

    @Override
    public void failed() {
//        Log.e("进度条", "begin: http失败" );
//        finishProgress();
        handleProgress(3);
    }

    public class myThread extends Thread {
        @Override
        public void run() {

            try {
                int time = 0;
                int n = (TIME_SECOND - TIME_FIRST)/TIME_SLEEP;//中间慢速段等差数列次数
                int d = (CHANGE_SECOND - CHANGE_FIRST)/(n-1);//中间慢速段每次前进长度等差数列公差
                while (true){
                    if (isFinish) {
                        finishProgressSchedule();
                        return;
                    }
                    Message message = new Message();
                    if (time <= TIME_FIRST) {//前部分快速速
                        Thread.sleep(TIME_SLEEP);
                        time = time + TIME_SLEEP;
                        message.what = (time/TIME_SLEEP)*(CHANGE_FIRST*TIME_SLEEP)/TIME_FIRST;
                        mHandler.sendMessage(message);
                    } else if (time > TIME_FIRST && time <= TIME_SECOND) {//中间慢慢减速
                        Thread.sleep(TIME_SLEEP);
                        time = time + TIME_SLEEP;
                        int thisN = (time-TIME_FIRST)/TIME_SLEEP;
                        int An = CHANGE_FIRST+(thisN-1)*d;//计算当前该走到位置
                        message.what = An;
                        mHandler.sendMessage(message);
                    }else if (time >TIME_SECOND){
                        for (int a = 1; a <= 1000; a++) {//60秒未加载完，进度条直接结束
                            Thread.sleep(60);
                            if (a == 1000) {//当访问超时快速结束
                                finishProgress();
                            }
                            if (isFinish) {
                                finishProgressSchedule();
                                return;
                            }
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 子线程中ProgressBar加载完成快速走完剩余进度
     */
    private void finishProgressSchedule() {
        isFinishSchedule = schedule;
        int i = schedule;
        do {
            Message message = new Message();
            try {
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i = i+100;
            message.what = i;
            mHandler.sendMessage(message);
        }while (i <= 10000);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==10000){
               int result= msg.arg1;
               if(result==1){
                   startProgress();
               }else if(result==2){
                   finishProgress();
               }else if(result==3){
                   finishProgress();
                   setProgress(0);
               }
           }
        }
    };
    private void handleProgress(int status){
        Message message=Message.obtain();
        message.what=10000;
        message.arg1=status;
        handler.sendMessage(message);
    }
}
