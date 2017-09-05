package com.gmail.jiangyang5157.java_rest.core.convert;

import java.io.IOException;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public final class RequestBodyConverter implements Converter<RequestBody, RequestBody> {

    @Override
    public RequestBody convert(RequestBody value) {
      return value;
    }
}
