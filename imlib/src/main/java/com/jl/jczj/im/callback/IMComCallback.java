package com.jl.jczj.im.callback;

import com.android.banana.commlib.bean.ErrorBean;

/**
 * Created by qiaomu on 2017/6/20.
 */

public interface IMComCallback<T> {
    public void onSuccess(T result);

    public void onError(ErrorBean errorBean);
}
