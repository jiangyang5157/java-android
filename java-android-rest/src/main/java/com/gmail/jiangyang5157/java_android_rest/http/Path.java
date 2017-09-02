package com.gmail.jiangyang5157.java_android_rest.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Yang Jiang on September 02, 2017
 */
@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Path {
    String value();
}