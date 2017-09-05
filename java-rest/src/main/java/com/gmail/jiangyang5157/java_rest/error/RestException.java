package com.gmail.jiangyang5157.java_rest.error;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public class RestException extends RuntimeException {

    final String mMessage;

    public RestException(String message) {
        super(message);
        this.mMessage = message;
    }

    public String message() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "RestException{" +
                mMessage + '\'' +
                '}';
    }
}
