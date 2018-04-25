package com.android.xjq;


import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.library.Utils.encryptUtils.StringUtils;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qiaomu on 2017/6/10.
 */

public class ApiHeaderInterceptor implements Interceptor {
    private String appDeviceId = null;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder mRequest = chain.request().newBuilder();
        mRequest.addHeader("appName", "hhs-android");
        mRequest.addHeader("appVersion", String.valueOf(BuildConfig.VERSION_NAME));
        mRequest.addHeader("appUserAgent", android.os.Build.VERSION.RELEASE + "-" + android.os.Build.MODEL);
        mRequest.addHeader("uuid", UUID.randomUUID().toString());
        if (appDeviceId == null)
            appDeviceId = HhsUtils.getAppDeviceId();
        mRequest.addHeader(AppParam.HTTP_HEADER_DEVICE_ID, appDeviceId);
        if (!StringUtils.isBlank(LoginInfoHelper.getInstance().getUserId())) {
            mRequest.addHeader(AppParam.HTTP_HEADER_USER_ID, LoginInfoHelper.getInstance().getUserId());
        }

        return chain.proceed(mRequest.build());
    }

}
