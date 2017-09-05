package com.gmail.jiangyang5157.java_rest.core.convert;

import java.io.IOException;

/**
 * Created by Yang Jiang on September 04, 2017
 */
public interface Converter<F, T> {

    T convert(F value) throws IOException;
}
