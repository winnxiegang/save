package com.android.banana.commlib.base;

/**
 * Created by lingjiu on 2017/10/23.
 */

public interface Presenter<V extends IMvpView> {
    void detachView();

    String getName();
}
