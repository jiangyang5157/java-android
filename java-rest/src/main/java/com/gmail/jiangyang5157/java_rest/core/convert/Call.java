package com.gmail.jiangyang5157.java_rest.core.convert;

import java.io.IOException;

import javax.xml.ws.Response;

import okhttp3.Request;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public interface Call<T> {

    Request request();

    Response<T> execute() throws IOException;

    void cancel();
}