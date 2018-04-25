package com.android.xjq.bean;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by Pangxie on 2016/5/17 14:30.
 * 判断是否可以发表
 */
public class IsSendJudgeBean {

    /**
     * success : true
     * nowDate : 2016-05-17 14:23:43
     */

    private boolean success;

    private String nowDate;

    private NormalObject error;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public NormalObject getError() {
        return error;
    }

    public void setError(NormalObject error) {
        this.error = error;
    }
}
