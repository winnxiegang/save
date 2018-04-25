package com.jl.jczj.im.body;


import android.text.TextUtils;


import com.android.httprequestlib.BaseRequestHttpName;
import com.android.httprequestlib.RequestContainer;
import com.android.httprequestlib.body.Body;
import com.android.httprequestlib.body.FormBody;
import com.android.httprequestlib.body.MutilFormBody;
import com.jl.jczj.im.bean.IMParams;
import com.jl.jczj.im.callback.IMComCallback;
import com.android.banana.commlib.encrypt.SecurityUtil;
import com.jl.jczj.im.im.ImManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by qiaomu on 2017/6/13.
 */
public class ImFormBody extends RequestContainer implements Body {
    public Body body;
    public IMComCallback imComCallback;

    public ImFormBody(BaseRequestHttpName requestHttpName) {
        this(requestHttpName, false, false);
    }

    public ImFormBody(BaseRequestHttpName requestHttpName, boolean sign) {
        this(requestHttpName, sign, false);
    }

    public ImFormBody(BaseRequestHttpName requestHttpName, boolean sign, boolean mutltiForm) {
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

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i != array.length - 1)
                builder.append(",");
        }
        return put(messageId, builder.toString());
    }


    public RequestBody getRequestBody() {
        super.getRequestBody();
        if (!map.containsKey("service") && requestEnum != null) {
            put("service", requestEnum.getName());
        }

        if (sign) {
            put("authedUserId", IMParams.userId);
            put("loginKey", IMParams.loginKey);
            put("sign", SecurityUtil.md5Sign(IMParams.signKey, getFiledMap(), "GBK"));
        }

        if (requestEnum != null && TextUtils.isEmpty(requestUrl))
            setRequestUrl(ImManager.URL);

        return body.getRequestBody();
    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return map;
    }

    public void setImComCallback(IMComCallback imComCallback) {
        this.imComCallback = imComCallback;
    }
}
