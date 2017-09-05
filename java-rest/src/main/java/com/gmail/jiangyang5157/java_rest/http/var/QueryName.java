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
 * Query parameter appended to the URL that has no value.
 * <p>
 * eg:
 *
 * @GET("/friends") Call<ResponseBody> friends(@QueryName String filter);
 * {@code foo.friends("contains(Bob)")} -> {@code /friends?contains(Bob)}
 * <p>
 * @GET("/friends") Call<ResponseBody> friends(@QueryName String... filters);
 * {@code foo.friends("contains(Bob)", "age(42)")} -> {@code /friends?contains(Bob)&age(42)}
 * <p>
 * @GET("/friends") Call<ResponseBody> friends(@QueryName(encoded=true) String filter);
 * {@code foo.friends("name+age"))} -> {@code /friends?name+age}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface QueryName {

    boolean encoded() default false;
}