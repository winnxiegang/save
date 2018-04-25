package com.android.banana.commlib.http;


import android.text.TextUtils;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.httprequestlib.BaseRequestHttpName;
import com.android.httprequestlib.RequestContainer;
import com.android.httprequestlib.body.Body;
import com.android.httprequestlib.body.FormBody;
import com.android.httprequestlib.body.MutilFormBody;
import com.android.library.Utils.encryptUtils.SecurityUtil;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by qiaomu on 2017/6/13.
 */

public class RequestFormBody extends RequestContainer implements Body {
    public Body body;

    public RequestFormBody(BaseRequestHttpName requestHttpName) {
        this(requestHttpName, false, false);
    }

    public RequestFormBody(BaseRequestHttpName requestHttpName, boolean sign) {
        this(requestHttpName, sign, false);
    }

    public RequestFormBody(BaseRequestHttpName requestHttpName, boolean sign, boolean mutltiForm) {
        super(requestHttpName, sign);
        if (mutltiForm)
            body = new MutilFormBody();
        else
            body = new FormBody();
    }

    @Override
    public Body put(String key, String value) {
        body.put(key, value);
        map.put(key, value);
        return this;
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
    public Body putFormDataPart(String name, String filename, File file) {
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        return this.body.putFormDataPart(name, filename, body);
    }

    @Override
    public Body putFormDataPart(String name, String filename, RequestBody body) {
        return this.body.putFormDataPart(name, filename, body);
    }

    @Override
    public Body putPart(RequestBody body) {
        return this.body.putPart(body);
    }


    public RequestBody getRequestBody() {
        super.getRequestBody();

        if (!map.containsKey("service") && requestEnum != null) {
            put("service", requestEnum.getName());
        }

        if (true) {
            if (!map.containsKey("timestamp")) {
                put("timestamp", System.currentTimeMillis());
            }
            put("authedUserId", LoginInfoHelper.getInstance().getUserId());
            put("loginKey", LoginInfoHelper.getInstance().getUserLoginKey());
            String sign = SecurityUtil.md5Sign(LoginInfoHelper.getInstance().getUserSignKey(), map, "GBK");
            put("sign", sign);
        }
        if (requestEnum != null && TextUtils.isEmpty(requestUrl))
            setRequestUrl(AppParam.API_DOMAIN + requestEnum.getUrl());

        return body.getRequestBody();
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

    public Body putStringArray(String messageId, String... array) {
        if (array == null || array.length <= 0)
            return this;
        List list = java.util.Arrays.asList(array);
        return putStringList(messageId, list);
    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return map;
    }


    @Override
    public RequestParams getConvertRequestParams() {

        RequestParams params = new RequestParams();

        for (Map.Entry<?, ?> entry : map.entrySet()) {

            params.put(entry.getKey().toString(), entry.getValue());

        }

        return params;

    }
}
