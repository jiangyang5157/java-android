package com.gmail.jiangyang5157.java_rest.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yang Jiang on September 03, 2017
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Rest {

    /**
     * @return the root url of the web service
     */
    String rootUrl();

    /**
     * @return the converter class to convert received data into Java object
     */
    Class<?> converter();

    /**
     * @return the interceptor class to do extra processing before or after request
     */
    Class<?> interceptor() default Void.class;

    /**
     * @return the error handler class to handle errors
     */
    Class<?> errorHandler() default Void.class;
}
