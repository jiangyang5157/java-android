package com.gmail.jiangyang5157.java_rest.error;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public class RestException extends RuntimeException {

    final String message;

    public RestException(String message) {
        super(message);
        this.message = message;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return "RestException{" +
                message + '\'' +
                '}';
    }
}
