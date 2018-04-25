package com.android.xjq.controller.live;

import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by zhouyi on 2017/3/7.
 */

public abstract class BaseLiveController<T extends BaseActivity> {
    protected T context;

    protected final String TAG = getClass().getName();

    public BaseLiveController(T context) {
        this.context = context;
    }

    abstract void init(View view);

    abstract void setView();

    public abstract void onPause();

    public abstract void onResume();

    public void onHiddenChanged(boolean isHide) {
    }

    public void onDestroy() {
        context = null;
    }

    public void responseSuccessHttp(RequestContainer requestContainer, JSONObject jsonObject) {
    }
}
