package com.android.banana.commlib.http;

import android.content.Context;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.httprequestlib.BaseRequestHttpName;
import com.android.httprequestlib.RequestContainer;
import com.android.httprequestlib.body.Body;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.encryptUtils.SecurityUtil;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by zhouyi on 2017/3/29.
 */

public class XjqRequestContainer extends RequestContainer implements Body {

    private LoginSignTypeEnum signTypeEnum = LoginSignTypeEnum.USER_SIGN;

    public Body body = new com.android.httprequestlib.body.FormBody();

    public XjqRequestContainer(BaseRequestHttpName requestEnum, boolean sign) {
        super(requestEnum, sign);
    }

    public XjqRequestContainer(BaseRequestHttpName requestEnum, boolean sign, LoginSignTypeEnum signTypeEnum) {
        super(requestEnum, sign);
        this.signTypeEnum = signTypeEnum;
    }

    @Override
    public RequestParams getConvertRequestParams() {

        if (requestEnum != null) {
            map.put("service", requestEnum.getName());
        }

        if (!map.containsKey("timestamp")) {
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        }

        if (true) {
            String sign = null;
            switch (signTypeEnum) {
                case ONE_AUTH_SIGN:
                    map.put("oneAuthId", LoginInfoHelper.getInstance().getOneAuthId());
                    map.put("loginKey", LoginInfoHelper.getInstance().getAuthLoginKey());
                    sign = SecurityUtil.md5Sign(LoginInfoHelper.getInstance().getAuthSignKey(), map, "GBK");
                    break;
                case USER_SIGN:
                    map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
                    map.put("loginKey", LoginInfoHelper.getInstance().getUserLoginKey());
                    sign = SecurityUtil.md5Sign(LoginInfoHelper.getInstance().getUserSignKey(), map, "GBK");
                    break;
            }
            map.put("sign", sign);
        }

        RequestParams params = new RequestParams();

        for (Map.Entry<?, ?> entry : map.entrySet()) {

            params.put(entry.getKey().toString(), entry.getValue());

        }

        return params;

    }

    @Override
    public void setRequestUrl(String requestUrl) {
        super.setRequestUrl(requestUrl);
    }

    @Override
    public String getRequestUrl() {
        if (requestUrl == null) {
            return AppParam.API_DOMAIN + requestEnum.getUrl();
        } else {
            return super.getRequestUrl();
        }

    }

    @Override
    public RequestBody getRequestBody() {
        if (requestEnum != null) {
            put("service", requestEnum.getName());
        }

        if (!map.containsKey("timestamp")) {
            put("timestamp", String.valueOf(System.currentTimeMillis()));
        }

        if (sign) {
            String sign = null;
            switch (signTypeEnum) {
                case ONE_AUTH_SIGN:
                    put("oneAuthId", LoginInfoHelper.getInstance().getOneAuthId());
                    put("loginKey", LoginInfoHelper.getInstance().getAuthLoginKey());
                    sign = SecurityUtil.md5Sign(LoginInfoHelper.getInstance().getAuthSignKey(), map, "GBK");
                    break;
                case USER_SIGN:
                    //8201711018675838
                    //USLaa57358352284785ba948b10e9914fb3
                    put("authedUserId", LoginInfoHelper.getInstance().getUserId());
                    put("loginKey", LoginInfoHelper.getInstance().getUserLoginKey());
                    sign = SecurityUtil.md5Sign(LoginInfoHelper.getInstance().getUserSignKey(), map, "GBK");
                    break;
            }
            put("sign", sign);
        }

        return body.getRequestBody();
    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return map;
    }

    @Override
    public Body put(String key, String value) {
        body.put(key, value);
        map.put(key, value);
        return this;
    }

    public Body putStringList(String messageId, List<String> list) {
        if (list == null || list.size() <= 0)
            return this;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1)
                builder.append(",");
        }
        return put(messageId, builder.toString());
    }

    @Override
    public Body put(String key, boolean value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, int value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, long value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, float value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, double value) {
        put(key, String.valueOf(value));
        return this;
    }

    @Override
    public Body putPart(MultipartBody.Part part) {
        return body.putPart(part);
    }

    @Override
    public Body putFormDataPart(String name, String filename, File body) {
        return null;
    }

    @Override
    public Body putFormDataPart(String name, String filename, RequestBody body) {
        return this.body.putFormDataPart(name, filename, body);
    }

    @Override
    public Body putPart(RequestBody body) {
        return this.body.putPart(body);
    }

    @Override
    public void showNetworkTimeoutTip(Context applicationContext) {
        if (applicationContext != null) {
            StringBuilder tip = new StringBuilder("网络有问题或服务器开小差了~稍后再试吧");
            LibAppUtil.showTip(applicationContext, tip);
        }

    }

}
