package com.android.banana.commlib.base;

import android.view.View;

/**
 * <页面基础公共功能抽象>
 * <p>
 * Created by lingjiu on 2017/10/23.
 */
public interface PresentationLayerFunc extends IMvpView{
    /**
     * 弹出消息
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    void showSoftKeyboard(View focusView);

    /**
     * 隐藏软键盘
     */
    void hideSoftKeyboard();
}
