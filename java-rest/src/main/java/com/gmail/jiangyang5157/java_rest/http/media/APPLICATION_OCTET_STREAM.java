package com.gmail.jiangyang5157.java_rest.http.media;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yang Jiang on September 03, 2017
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MediaType(MediaType.APPLICATION_OCTET_STREAM)
@Documented
public @interface APPLICATION_OCTET_STREAM {
}