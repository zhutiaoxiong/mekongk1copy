package com.kulala.staticsfunc.static_system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 用于从服务端下载文件，并保存到指定位置
 */

public class UtilFileLoading {
    private String urlServer, saveDir, fileName;
    private long preLoadTime = 0;//会有可能网络不好，无任何反应,只有重下同一个东西
    private        SetLoadProgressListener setLoadProgressListener;
    // ========================out======================
    private static UtilFileLoading         _instance;
    private UtilFileLoading() {
    }
    public static UtilFileLoading getInstance() {
        if (_instance == null)
            _instance = new UtilFileLoading();
        return _instance;
    }
    // ========================public======================
    public interface SetLoadProgressListener {
        void setMaxProgress(int max);

        void setProgress(int progress);

        void onCompleted(File savedFile) throws Exception;

        void onLoadFailed(String errorInfo);
    }
    /**
     * saveLoc 目录地址 saveDir 目录 Suffix 后缀名
     */
    public void loadFileFromServer(String urlServer, String saveDir, String fileName, SetLoadProgressListener listener) {
        if (urlServer == null || urlServer.length() == 0) return;
        long now = System.currentTimeMillis();
        if(now - preLoadTime> 1000*10){//超过10秒可以重下载
            preLoadTime = now;
        }else  if(this.urlServer == urlServer)return;//下载同一个东西
        this.urlServer = urlServer;
        this.saveDir = saveDir;
        this.fileName = fileName;
        this.setLoadProgressListener = listener;
        new DownLoadThread().start();
    }
    private void clearData(){
        this.urlServer = null;
        this.saveDir = null;
        this.fileName = null;
        this.setLoadProgressListener = null;
    }
    // ========================private======================

    private class DownLoadThread extends Thread {
        public void run() {
            try {
                File file = getFileFromServer(urlServer);
                sleep(500L);
                if (file.exists()) {
                    if (setLoadProgressListener != null) setLoadProgressListener.onCompleted(file);
                    clearData();
                }
            } catch (Exception e) {
                if (setLoadProgressListener != null)
                    setLoadProgressListener.onLoadFailed("下载" + fileName + "失败" + e.toString());
                e.printStackTrace();
                clearData();
            }
        }
    }

    private File getFileFromServer(String urlServer) throws Exception {
        URL               url  = new URL(urlServer);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        // 获取到文件的大小
        if (setLoadProgressListener != null)
            setLoadProgressListener.setMaxProgress(conn.getContentLength());
        InputStream is   = conn.getInputStream();
        File        file = new File(saveDir);
        file.mkdirs();
        file = new File(file, fileName);
        FileOutputStream    fos    = new FileOutputStream(file);
        BufferedInputStream bis    = new BufferedInputStream(is);
        byte[]              buffer = new byte[1024];
        int                 len;
        int                 total  = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            // 获取当前下载量
            if (setLoadProgressListener != null) setLoadProgressListener.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        return file;
    }
}
