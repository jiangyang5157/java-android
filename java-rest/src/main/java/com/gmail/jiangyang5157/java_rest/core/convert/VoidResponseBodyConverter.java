package com.gmail.jiangyang5157.java_rest.core.convert;

import java.io.IOException;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public final class VoidResponseBodyConverter implements Converter<ResponseBody, Void> {

    @Override
    public Void convert(ResponseBody value) throws IOException {
      value.close();
      return null;
    }
}
