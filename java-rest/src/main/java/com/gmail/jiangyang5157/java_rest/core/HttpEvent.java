package com.gmail.jiangyang5157.java_rest.core;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public interface HttpEvent<T> {

    okhttp3.Request request();

    okhttp3.Response response();
}
