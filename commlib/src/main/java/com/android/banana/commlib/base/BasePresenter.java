package com.android.banana.commlib.base;

/**
 * <基础业务类> 继承处理主要的业务逻辑
 *
 * Created by lingjiu on 2017/10/23.
 */

public abstract class BasePresenter<V extends IMvpView> implements Presenter<V> {
    protected V mvpView;

    public BasePresenter(V view) {
        mvpView = view;
    }

    @Override
    public void detachView() {
        mvpView = null;
    }

    @Override
    public String getName() {
        return mvpView.getClass().getSimpleName();
    }
}
