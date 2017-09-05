package com.gmail.jiangyang5157.java_rest.http.var;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yang Jiang on September 03, 2017
 */

/**
 * eg:
 *
 * @GET("/") Call<ResponseBody> foo(@Header("Accept-Language") String acceptLang);
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Header {

    String value();
}