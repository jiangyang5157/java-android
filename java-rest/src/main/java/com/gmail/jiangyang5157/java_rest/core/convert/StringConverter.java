package com.gmail.jiangyang5157.java_rest.core.convert;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public final class StringConverter implements Converter<Object, String> {

    @Override
    public String convert(Object value) {
      return value.toString();
    }
}
