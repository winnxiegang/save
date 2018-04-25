package com.android.banana.commlib.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.banana.commlib.R;
import com.android.banana.commlib.dialog.CustomDialog;
import com.android.banana.commlib.utils.toast.ToastUtil;

import org.json.JSONObject;

/**
 * <页面基础公共功能实现>
 * <p>
 * Created by lingjiu on 2017/10/23.
 */
public class PresentationLayerFuncHelper implements PresentationLayerFunc {

    private Context context;

    private CustomDialog customDialog;

    public PresentationLayerFuncHelper(Context context) {
        this.context = context;

        customDialog = new CustomDialog(context, R.style.CustomDialog);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showLong(context.getApplicationContext(),msg);
        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog(String loadingTxt) {
        if (customDialog != null) {
            customDialog.showDialog(loadingTxt);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
    }

    @Override
    public void showSoftKeyboard(View focusView) {

    }

    @Override
    public void hideSoftKeyboard() {
        if (context != null) {
            View view = ((Activity) context).getWindow().peekDecorView();
            InputMethodManager inputManger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onDestroy() {
        customDialog.dismiss();
        customDialog = null;
        context = null;
    }
}
