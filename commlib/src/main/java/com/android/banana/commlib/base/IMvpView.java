package com.android.banana.commlib.base;

import org.json.JSONObject;

/**
 *  抽象类
 * <功能详细描述> 页面回调
 * Created by lingjiu on 2017/10/23.
 */

public interface IMvpView {
    /**
     * 网络请求加载框
     */
    void showProgressDialog(String loadingTxt);

    /**
     * 隐藏网络请求加载框
     */
    void hideProgressDialog();

    void showErrorMsg(JSONObject jsonObject);
}
