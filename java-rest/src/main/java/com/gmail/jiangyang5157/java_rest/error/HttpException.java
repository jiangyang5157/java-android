package com.gmail.jiangyang5157.java_rest.error;

/**
 * Created by Yang Jiang on September 03, 2017
 */
public class HttpException extends RestException {

    final int mCode;

    public HttpException(int code, String message) {
        super(message);
        this.mCode = code;
    }

    public int code() {
        return mCode;
    }

    @Override
    public String toString() {
        return "HttpException{" +
                mCode + '\'' +
                mMessage + '\'' +
                '}';
    }
}
