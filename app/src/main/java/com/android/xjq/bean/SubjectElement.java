package com.android.xjq.bean;

/**
 * 保存话题的元素（方案，联系人，赛事，表情）
 * Created by zhouyi on 2016/1/27 11:06.
 */

public class SubjectElement {


    private int id;


    private String key;

    private String showMessage;


    private String hideMessage;


    private String type;


    private String emojMessage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public String getHideMessage() {
        return hideMessage;
    }

    public void setHideMessage(String hideMessage) {
        this.hideMessage = hideMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmojMessage() {
        return emojMessage;
    }

    public void setEmojMessage(String emojMessage) {
        this.emojMessage = emojMessage;
    }

    @Override
    public boolean equals(Object o) {
        SubjectElement temp = (SubjectElement) o;
        if (temp.key.equals(key)) {
            return true;
        }
        return false;
    }
}
