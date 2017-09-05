package com.gmail.jiangyang5157.java_rest.core.convert;

import com.gmail.jiangyang5157.java_rest.core.RestEvent;
import com.gmail.jiangyang5157.java_rest.error.RestException;

/**
 * Created by Yang Jiang on September 05, 2017
 */
public interface Callback<T> {

    void onResponse(Call<T> call, RestEvent<T> event);

    void onFailure(Call<T> call, RestException exception);
}