package com.gmail.jiangyang5157.java_rest.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yang Jiang on September 03, 2017
 */

/**
 * Query parameter appended to the URL.
 * <p>
 * eg:
 *
 * @GET("/friends") Call<ResponseBody> friends(@Query("page") int page);
 * {@code foo.friends(1)} -> {@code /friends?page=1}
 * {@code foo.friends(null)} -> {@code /friends}
 * {@code foo.friends("coworker", "bowling")} -> {@code /friends?group=coworker&group=bowling}
 * <p>
 * @GET("/friends") Call<ResponseBody> friends(@Query(value="group", encoded=true) String group);
 * {@code foo.friends("foo+bar")} -> {@code /friends?group=foo+bar}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Query {

    String value();

    boolean encoded() default false;
}