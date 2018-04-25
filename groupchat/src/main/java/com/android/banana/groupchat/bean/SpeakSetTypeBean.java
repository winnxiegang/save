package com.android.banana.groupchat.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by zaozao on 2017/5/31.
 */

public class SpeakSetTypeBean {

    private String name;
    private String message;

    @Expose
    private boolean select;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
