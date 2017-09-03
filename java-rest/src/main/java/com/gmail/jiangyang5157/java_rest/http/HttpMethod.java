package com.gmail.jiangyang5157.java_rest.http;

/**
 * Created by Yang Jiang on September 03, 2017
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpMethod {
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
    public static final String GET = "GET";

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
    public static final String POST = "POST";

    /**
     * The HEAD method asks for a response identical to that of a GET request, but without the response body.
     */
    public static final String HEAD = "HEAD";

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     */
    public static final String PUT = "PUT";

    /**
     * The PATCH method is used to apply partial modifications to a resource.
     */
    public static final String PATCH = "PATCH";

    /**
     * The DELETE method deletes the specified resource.
     */
    public static final String DELETE = "DELETE";

    /**
     * The OPTIONS method is used to describe the communication options (HTTP methods that the server supports) for the target resource.
     */
    public static final String OPTIONS = "OPTIONS";

    String value();
}
