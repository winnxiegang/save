package com.android.banana.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.TypeUtils;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/6/7.
 */

public class WrapperHttpHelper implements OnHttpResponseListener {

    private HttpRequestHelper helper;
    private IHttpResponseListener listener;
    private Gson gson;
    private Type GenericType;

    public WrapperHttpHelper(@NonNull IHttpResponseListener listener) {
        this.helper = new HttpRequestHelper(this, listener.getClass().getSimpleName());
        this.listener = listener;
        this.gson = new Gson();
        if (listener == null)
            return;

        Type[] interfaces = listener.getClass().getGenericInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Type typeInterface : interfaces) {
                String totalTypeName = TypeUtils.toString(typeInterface);
                if (typeInterface instanceof ParameterizedType) {
                    if (TextUtils.isEmpty(totalTypeName) || !totalTypeName.contains("IHttpResponseListener"))
                        continue;
                    Type[] arguments = ((ParameterizedType) typeInterface).getActualTypeArguments();
                    if (arguments != null && arguments.length > 0) {
                        GenericType = arguments[0];//class com.android.jczj.bean.groupchat.ChatRoomInfo
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void executorSuccess(com.android.httprequestlib.RequestContainer request, JSONObject jo) {
        if (listener == null)
            return;
        try {
            if (GenericType == null && request.getGenericClaz() != null && jo != null) {//传递泛型为空,这时尝试从request中取
                Object o = gson.fromJson(jo.toString(), request.getGenericClaz());
                listener.onSuccess(request, o);
            } else if (GenericType != null && jo != null) {//单一对象 例如 UserBean
                listener.onSuccess(request, gson.fromJson(jo.toString(), GenericType));
            } else {
                listener.onSuccess(request, jo);
            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("解析出错了,请求链接:" + request.getRequestUrl() + "---错误信息:" + e.toString());
        }
    }

    @Override
    public void executorFalse(com.android.httprequestlib.RequestContainer request, JSONObject jo) {
        if (listener != null)
            try {
                listener.onFailed(request, jo, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void executorFailed(com.android.httprequestlib.RequestContainer request) {
        if (listener != null)
            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject errorObject = new JSONObject();
                errorObject.put("name", "netError");
                errorObject.put("message", "网络有问题或服务器开小差了~稍后再试吧");
                jsonObject.put("error", errorObject);
                listener.onFailed(request, jsonObject, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void executorFinish() {

    }

    /*jchapi*/
    public void startRequestList(ArrayList<RequestContainer> list, boolean addRequest) {
        if (addRequest) {
            helper.addRequest(list);
        } else {
            helper.startRequest(list, false);
        }
    }

    public void startRequest(com.android.httprequestlib.RequestContainer container, boolean addRequest) {
        if (addRequest) {
            helper.addRequest(container);
        } else {
            helper.startRequest(container, false);
        }
    }


    public void startRequest(com.android.httprequestlib.RequestContainer container) {
        startRequest(container, true);
    }

    public boolean isRequestFinished() {
        return helper.requestFinish();
    }

    public void onDestroy() {
        helper.onDestroy();
        listener = null;
    }
}
