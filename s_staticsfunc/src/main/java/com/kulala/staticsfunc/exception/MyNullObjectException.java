package com.kulala.staticsfunc.exception;

/**
 * Created by Administrator on 2017/7/8.
 */

public class MyNullObjectException extends Exception {
    String message;
    public MyNullObjectException(String ErrorMessagr) {  //父类方法
        message = ErrorMessagr;
    }
    public String getMessage(){   //覆盖getMessage()方法
        return message;
    }
}
