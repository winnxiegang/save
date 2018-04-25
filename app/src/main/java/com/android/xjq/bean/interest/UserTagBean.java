package com.android.xjq.bean.interest;

import java.util.List;
import java.util.Map;

/**
 * Created by danao on 2018/4/20.
 */
public class UserTagBean {

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private List<GroupTag> userTags;
    private List<Tag> defaultTags;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public List<GroupTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(List<GroupTag> userTags) {
        this.userTags = userTags;
    }

    public List<Tag> getDefaultTags() {
        return defaultTags;
    }

    public void setDefaultTags(List<Tag> defaultTags) {
        this.defaultTags = defaultTags;
    }

    public static class GroupTag {
        private String groupId;
        private String groupName;
        private int groupOrderNum;
        private List<Tag> tagList;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getGroupOrderNum() {
            return groupOrderNum;
        }

        public void setGroupOrderNum(int groupOrderNum) {
            this.groupOrderNum = groupOrderNum;
        }

        public List<Tag> getTagList() {
            return tagList;
        }

        public void setTagList(List<Tag> tagList) {
            this.tagList = tagList;
        }
    }

    public static class Tag {

        public static final int NORMAL = 1;
        public static final int DIVIDER = 2;

        private String id;
        private String tagName;
        private int orderName;
        private String groupId;
        private String imageUrl;
        private int type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public int getOrderName() {
            return orderName;
        }

        public void setOrderName(int orderName) {
            this.orderName = orderName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
