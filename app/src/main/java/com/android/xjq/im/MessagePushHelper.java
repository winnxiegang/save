package com.android.xjq.im;

import com.android.xjq.XjqApplication;
import com.android.xjq.presenters.MessageEvent;
import com.android.library.Utils.LogUtils;
import com.tencent.TIMManager;

/**
 * Created by zhouyi on 2017/5/4.
 */

public class MessagePushHelper {

    private static String TAG="MessagePushHelper";

    public static void initImSdk() {

        LogUtils.d(TAG,"初始化sdk");

        TIMManager.getInstance().init(XjqApplication.getContext());

        TIMManager.getInstance().addMessageListener(MessageEvent.getInstance());

    }

    public static void loginIm(String userId, String password) {



    }

    public static void logoutIm() {

    }

}
