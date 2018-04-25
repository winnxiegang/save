package com.android.xjq.bean.live.main;

import java.io.Serializable;

/**
 * Created by zhiduoxing on 2016/11/8.
 */
public class AdInfo implements Serializable {

    private boolean isLogin;

    private String url;

    private String objectType;

    private String objectAddress;

    private String title;

    public AdInfo() {
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectAddress() {
        return objectAddress;
    }

    public void setObjectAddress(String objectAddress) {
        this.objectAddress = objectAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
