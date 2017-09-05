package com.gmail.jiangyang5157.java_rest.http.method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yang Jiang on September 03, 2017
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface MethodType {

    /**
     * The GET method requests a representation of the specified resource. Requests using GET should only retrieve data.
     * <p>
     * Visibility: Data is visible to everyone in the URL
     * Cached: Can be cached
     * History: Parameters remain in browser history
     * Bookmarked: Can be bookmarked
     * Restrictions on data type: Only ASCII characters allowed
     * Restrictions on data length: The length of a URL is limited (maximum URL length is 2048 characters)
     */
    String GET = "GET";

    /**
     * The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server.
     * <p>
     * Visibility: Data is not displayed in the URL
     * Cached: Not cached
     * History: Parameters are not saved in browser history
     * Bookmarked: Cannot be bookmarked
     * Restrictions on data type: No restrictions. Binary data is also allowed
     * Restrictions on data length: No restrictions
     */
    String POST = "POST";

    /**
     * The HEAD method asks for a response identical to that of a GET request, but without the response body.
     */
    String HEAD = "HEAD";

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     */
    String PUT = "PUT";

    /**
     * The PATCH method is used to apply partial modifications to a resource.
     */
    String PATCH = "PATCH";

    /**
     * The DELETE method deletes the specified resource.
     */
    String DELETE = "DELETE";

    /**
     * The OPTIONS method is used to describe the communication options (HTTP methods that the server supports) for the target resource.
     */
    String OPTIONS = "OPTIONS";

    String value() default "";
}
