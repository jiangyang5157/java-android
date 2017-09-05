package com.gmail.jiangyang5157.java_rest.http.media;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yang Jiang on September 03, 2017
 */

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MediaType {

    String ALL = "*/*";

    String APPLICATION_JSON = "application/json";
    String APPLICATION_XML = "application/xml";
    String APPLICATION_WILDCARD_XML = "application/*+xml";
    String APPLICATION_ATOM_XML = "application/atom+xml";
    String APPLICATION_RSS_XML = "application/rss+xml";
    String APPLICATION_XHTML_XML = "application/xhtml+xml";
    String APPLICATION_OCTET_STREAM = "application/octet-stream";
    String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    String IMAGE_JPEG = "image/jpeg";
    String IMAGE_PNG = "image/png";
    String IMAGE_GIF = "image/gif";

    String TEXT_PLAIN = "text/plain";
    String TEXT_XML = "text/xml";
    String TEXT_HTML = "text/html";

    String value();
}