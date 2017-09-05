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
 * Query parameter keys and values appended to the URL.
 * <p>
 * eg:
 *
 * @GET("/friends") Call<ResponseBody> friends(@QueryMap Map<String, String> filters);
 * {@code foo.friends(ImmutableMap.of("group", "coworker", "age", "42"))} -> {@code /friends?group=coworker&age=42}.
 * <p>
 * @GET("/friends") Call<ResponseBody> friends(@QueryMap(encoded=true) Map<String, String> filters);
 * {@code foo.list(ImmutableMap.of("group", "coworker+bowling"))} -> {@code /friends?group=coworker+bowling}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface QueryMap {

    boolean encoded() default false;
}