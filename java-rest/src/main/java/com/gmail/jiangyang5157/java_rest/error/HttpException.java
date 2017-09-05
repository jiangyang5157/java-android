package com.gmail.jiangyang5157.java_rest.error;

/**
 * Created by Yang Jiang on September 03, 2017
 */
public class HttpException extends RestException {

    final int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int code() {
        return code;
    }

    @Override
    public String toString() {
        return "HttpException{" +
                code + '\'' +
                message + '\'' +
                '}';
    }
}
