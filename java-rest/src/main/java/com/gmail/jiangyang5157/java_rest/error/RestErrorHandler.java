package com.gmail.jiangyang5157.java_rest.error;

/**
 * Created by Yang Jiang on September 03, 2017
 */

public interface RestErrorHandler {
    void onRestExceptionThrown(RestException e);
}
