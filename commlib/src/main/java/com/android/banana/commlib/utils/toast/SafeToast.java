package com.android.banana.commlib.utils.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * 解决7.1上toast崩溃的问题
 * <p>
 * Created by qiaomu on 2018/1/29.
 */

public class SafeToast {
    private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static Toast mToast;

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
        } catch (Exception e) {
        }
    }

    public static Toast newInstance(Context context, CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), message, duration);
        }
        hook(mToast);
        return mToast;
    }

    public static Toast newInstance(Context context, @StringRes int resId, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), resId, duration);
        }
        hook(mToast);
        return mToast;
    }

    private static class SafelyHandlerWarpper extends Handler {

        private Handler impl;

        public SafelyHandlerWarpper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }
}
