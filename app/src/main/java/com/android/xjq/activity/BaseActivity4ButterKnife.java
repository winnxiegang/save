package com.android.xjq.activity;

import com.android.banana.groupchat.base.BaseActivity4Jczj;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qiaomu on 2018/2/6.
 */

public abstract class BaseActivity4ButterKnife<T> extends BaseActivity4Jczj<T> {

    private Unbinder mBind;

    @Override
    protected void bindView() {
        mBind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
