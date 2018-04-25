package com.android.banana.commlib.http;

import com.android.banana.commlib.bean.ErrorBean;
import com.android.httprequestlib.RequestContainer;

/**
 * Created by lingjiu on 2017/10/20.
 */

public interface IRequestResponse<T> {
    void onSuccessful(RequestContainer requestContainer, T result);

    void onFalse(RequestContainer requestContainer, ErrorBean errorBean);
}
