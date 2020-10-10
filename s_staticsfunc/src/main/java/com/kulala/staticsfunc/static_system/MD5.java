package com.kulala.staticsfunc.static_system;

import android.util.Log;

import com.kulala.staticsfunc.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kulala.staticsfunc.static_system.UtilFileSave.GIF_CHANGE_NAME;
import static com.kulala.staticsfunc.static_system.UtilFileSave.PNG_CHANGE_NAME;

public class MD5 {
    public static String MD5generator(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    // ==============================set=================================
    public static String getMD5FileName(String url) {//http always change
        String suffix = getUrlSuffix(url);
        String filename = MD5.MD5generator(url);
        if(suffix.equals("png"))filename+=PNG_CHANGE_NAME;
        else if(suffix.equals("gif"))filename+=GIF_CHANGE_NAME;
        else filename += "."+suffix;
        return filename;
    }
    private static String getUrlSuffix(String url){
        String  suffixes ="avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat      =Pattern.compile("[\\w]+[\\.]("+suffixes+")");//正则判断
        Matcher mc       =pat.matcher(url);//条件匹配
        while(mc.find()){
            String substring = mc.group();//截取文件名后缀名
            if(BuildConfig.DEBUG)Log.e("substring:", substring);
            return substring.split("\\.")[1];
        }
        return null;
    }
}
