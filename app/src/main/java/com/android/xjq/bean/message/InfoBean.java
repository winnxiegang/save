package com.android.xjq.bean.message;

/**
 * Created by zhouyi on 2017/1/12 16:56.
 */

public class InfoBean {
    private String summary;

    private int id;

    private String title;

    private boolean delete;

    private boolean published;

    private boolean commentOff;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isCommentOff() {
        return commentOff;
    }

    public void setCommentOff(boolean commentOff) {
        this.commentOff = commentOff;
    }
}
