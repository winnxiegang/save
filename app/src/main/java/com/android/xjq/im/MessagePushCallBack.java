package com.android.xjq.im;

/**
 * Created by zhouyi on 2017/5/4.
 */

public interface MessagePushCallBack {

    public void onSuccess(String message,String code);

    public void onFailed(String message,String code);

}
