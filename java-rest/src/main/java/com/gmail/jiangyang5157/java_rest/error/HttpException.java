package com.gmail.jiangyang5157.java_rest.error;

/**
 * Created by Yang Jiang on September 03, 2017
 */
public class HttpException extends RuntimeException {

    private final int mCode;

    private final String mMessage;

    public HttpException(int code, String message) {
        super("HTTP " + code + " " + message);
        this.mCode = code;
        this.mMessage = message;
    }

    public int code() {
        return mCode;
    }

    public String message() {
        return mMessage;
    }
}
