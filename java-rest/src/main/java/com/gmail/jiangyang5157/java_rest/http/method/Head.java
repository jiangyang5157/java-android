package com.gmail.jiangyang5157.java_rest.http.method;

/**
 * Created by Yang Jiang on September 03, 2017
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod(HttpMethod.HEAD)
@Documented
public @interface Head {
}
