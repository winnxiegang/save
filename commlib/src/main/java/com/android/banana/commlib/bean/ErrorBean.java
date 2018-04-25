package com.android.banana.commlib.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouyi on 2015/11/4 14:15.
 */
public class ErrorBean {

    public ErrorEntity error;

    public boolean success;

    public String nowDate;

    public String detailMessage;

    public boolean jumpLogin;

    public ErrorBean(JSONObject jo) throws JSONException {

        JSONObject errorJo = jo.getJSONObject("error");

        if (jo.has("detailMessage")) {
            this.detailMessage = jo.getString("detailMessage");
        }
        if (jo.has("jumpLogin"))
            this.jumpLogin = jo.getBoolean("jumpLogin");

        this.error = new ErrorEntity(errorJo);

    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setError(ErrorEntity error) {
        this.error = error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public ErrorEntity getError() {
        return error;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public static class ErrorEntity {

        public String message;

        public String name;

        public ErrorEntity(JSONObject jo) throws JSONException {
            message = jo.getString("message");
            name = jo.getString("name");
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }
    }
}
