package com.kulala.links.http;

import android.os.Process;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kulala.links.BuildConfig;
import com.kulala.links.interfaces.HttpInitDataListener;
import com.kulala.links.tools.GZIP1;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http://域名/项目名/common?funid=x&rd=系统时间毫秒数
 * funid：接口id【生成规则：模块编号（两位数字，从10开始）+接口编号（两位数字，从00开始）】
 */
public class HttpConn {

    private static HttpConn             sClient;
    private        OkHttpClient         client;
    private        HttpInitDataListener httpInitDataListener;
    public void setHttpInitDataListener(HttpInitDataListener listener) {
        this.httpInitDataListener = listener;
    }
    //=============================================
    public static HttpConn getInstance() {
        if (sClient == null) {
            sClient = new HttpConn();
        }
        return sClient;
    }
    //=============================================
    public void request(int protocol, JsonObject data) {
        sendMessage(data,protocol);
    }
    public void sendMessage(JsonObject params, int protocol) {
        if (params == null) params = new JsonObject();
        params.addProperty("protocol", protocol);
        new HttpThread(params, protocol).start();
    }
    private class HttpThread extends Thread {
        private JsonObject params;// +common?funid=1101&rd=24324
        private int        protocol;

        HttpThread(JsonObject params, int protocol) {
            this.params = params;
            this.protocol = protocol;
            if (params == null) this.params = new JsonObject();
        }

        public void run() {
            if (protocol != 1219) Log.e("<<<Http>>>", "发包>>>> cmd:" + protocol+" params:"+params);
            try {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                String subscription = "common?funid=" + protocol + "&rd=" + System.currentTimeMillis();
                String useUrl       = httpInitDataListener.getBasicUrl().concat(subscription);
                int    handle       = 0;//发包gzip
                int    shandle      = 0;//收包gzip
                params.add("phead", httpInitDataListener.getPHeadJsonObj(subscription));
                String zip;
                if(handle == 1){
                    zip = GZIP1.toGZIPString(params.toString());
                }else{
                    zip = params.toString();
                }
                OkHttpClient mOkHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("handle", handle + "")
                        .add("data", zip)
                        .add("shandle", shandle + "")
                        .build();
                Request request = new Request.Builder()
                        .url(useUrl)
                        .post(formBody)
                        .build();
                okhttp3.Call call = mOkHttpClient.newCall(request);
                if (httpStatusListner == null)Log.e("进度条", "http无进度条");
                if (httpStatusListner != null && protocol != 1219) httpStatusListner.begin();
                call.enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Log.e("HTTP", "onFailure:" + e.toString());
                        if (httpStatusListner != null && protocol != 1219)httpStatusListner.failed();
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException{
                        if (httpStatusListner != null && protocol != 1219)httpStatusListner.success();
                        if (response != null && response.body() != null) {
                            try {
                                String result = response.body().string();
                                if(BuildConfig.DEBUG && protocol!=1219)Log.e("HTTP", " onResponse " + protocol +" body:" + result);
                                if (httpInitDataListener != null)httpInitDataListener.onReceiveData(protocol, result);
                            } catch (Exception e) {
                                if(BuildConfig.DEBUG)Log.e("HTTP", "onResponse ERROR:" + e.toString());
                                e.printStackTrace();
                            }
                        }
                    }

                });
            } catch (Exception e) {
                Log.e("HTTP", "onFailure:" + e.toString());
                if (httpStatusListner != null && protocol != 1219)httpStatusListner.failed();
            }

        }
    }

    public interface OnHttpStatusListner {
        void begin();
        void success();
        void failed();
    }

    private OnHttpStatusListner httpStatusListner;

    private String testInfoT = "";
    public void setHttpStatus(OnHttpStatusListner httpStatusListner,String testInfo) {
        this.httpStatusListner = httpStatusListner;
        testInfoT = testInfo;
        Log.e("进度条", "setHttpStatus"+testInfo );
    }
    public void removeHttpStatus(String testInfo) {
        if (httpStatusListner != null && testInfoT.equals(testInfo)) {
            Log.e("进度条", "removeHttpStatus"+testInfo );
            httpStatusListner = null;
            testInfoT = "";
        }
    }
}

