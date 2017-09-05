package com.gmail.jiangyang5157.java_rest.core.convert;

import java.io.IOException;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public final class BufferingResponseBodyConverter implements Converter<ResponseBody, ResponseBody> {

    @Override
    public ResponseBody convert(ResponseBody value) throws IOException {
      try {
        Buffer buffer = new Buffer();
        value.source().readAll(buffer);
        return ResponseBody.create(value.contentType(), value.contentLength(), buffer);
      } finally {
        value.close();
      }
    }
}
