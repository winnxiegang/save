package com.android.residemenu.lt_lib.model.gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouyi on 2015/10/10 15:03.
 */
public class ErrorBean {

    /**
     * error : {"message":"未找到彩店名称","name":"BRANCH_NAME_NOT_FOUND"}
     * success : false
     * nowDate : 2015-10-10 15:00:04
     */

    private ErrorEntity error;

    private boolean success;

    private String nowDate;

    public ErrorBean(JSONObject jo) throws JSONException{

        JSONObject errorJo = jo.getJSONObject("error");

        this.error = new ErrorEntity(errorJo);

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

    public static class ErrorEntity {
        /**
         * message : 未找到彩店名称
         * name : BRANCH_NAME_NOT_FOUND
         */

        private String message;
        private String name;

        public ErrorEntity(JSONObject jo)throws JSONException{
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
