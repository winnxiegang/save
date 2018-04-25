package com.android.banana.commlib.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.coupon.BasePopupWindow;
import com.android.banana.commlib.view.CustomKeyboardLayout;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.banana.commlib.view.loading.LoadingBar;
import com.android.library.Utils.LibAppUtil;


/**
 * Created by lingjiu on 2017/11/3.
 */

public class BottomPayPop extends BasePopupWindow {
    private final TextView loadingText;
    private CustomKeyboardLayout keyboardLayout;
    private LoadingBar loadingBar;
    private PayPsdInputView payInputView;
    private View loadingLayout;
    private int gravity;
    private boolean isShowLoadingAnim = true;

    public void setShowLoadingAnim(boolean showLoadingAnim) {
        isShowLoadingAnim = showLoadingAnim;
    }

    public BottomPayPop(final Context context, final PayPsdInputView.OnPasswordListener onPasswordListener) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(View.inflate(context, R.layout.layout_popupwindow_bottom_pay, null));
        initBaseConfig(context);
        loadingLayout = parentView.findViewById(R.id.loadingLayout);
        loadingText = ((TextView) parentView.findViewById(R.id.loadingText));
        keyboardLayout = ((CustomKeyboardLayout) parentView.findViewById(R.id.keyboardLayout));
        loadingBar = ((LoadingBar) parentView.findViewById(R.id.loadingBar));

        payInputView = ((PayPsdInputView) parentView.findViewById(R.id.payInputView));
        parentView.findViewById(R.id.backIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        parentView.findViewById(R.id.forgetPassWordTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFindPassword();
                if (!isShowLoadingAnim) {
                    dismiss();
                    return;
                }
            }
        });

        keyboardLayout.setOnCustomerKeyboardClickListener(new CustomKeyboardLayout.CustomerKeyboardClickListener() {

            @Override
            public void click(String number) {
                payInputView.addPassword(number);
            }

            @Override
            public void delete() {
                payInputView.deleteLastPassword();
            }
        });

        payInputView.setPasswordListener(new PayPsdInputView.OnPasswordListener() {

            @Override
            public void inputListener(boolean isInputComplete, String psd) {
                if (onPasswordListener != null && isInputComplete) {
                    onPasswordListener.inputListener(isInputComplete, psd);
                    if (!isShowLoadingAnim) {
                        dismiss();
                        return;
                    }
                    keyboardLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);
                    loadingBar.loading();
                    loadingText.setText("正在付款..");
                }
            }
        });

    }

    public void setPayResult(boolean payResult) {
        if (loadingBar == null) return;
        if (!payResult) {
            loadingText.setText("付款失败");
        } else {
            loadingText.setText("付款成功");
        }
        loadingBar.loadingComplete(payResult);

        loadingText.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1500);

    }

    @Override
    public void dismiss() {
        payInputView.setText("");
        loadingBar.onDestroy();
        super.dismiss();
    }



    private void toFindPassword() {
        Intent intent = new Intent("com.android.xjq.forgetpassword");
        mContext.startActivity(intent);
    }

    public void showPop() {
        showAtLocation(parentView, gravity, 0, 0);
        keyboardLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        // showKeyboard();
    }

    private void showKeyboard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                payInputView.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) payInputView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(payInputView, 0);
            }
        }, 200);
        //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void initBaseConfig(Context context) {
        setSupportShadow(true);
        int height = LibAppUtil.getScreenHeight(context) - context.getResources().getDimensionPixelOffset(R.dimen.pay_dialog_windows_top_margin);
        setHeight(height);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        gravity = Gravity.BOTTOM;
        setAnimationStyle(R.style.dialog_anim_bottom);
    }

}
