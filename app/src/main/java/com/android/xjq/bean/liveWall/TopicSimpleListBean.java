package com.android.xjq.bean.liveWall;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by lingjiu on 2018/3/1.
 */
public class TopicSimpleListBean {
    /**
     * collectId : 0
     * commentOff : false
     * delete : false
     * gmtCreate : 2018-02-27 17:52:05
     * likeCount : 0
     * liked : false
     * loginName : 13600000469_0
     * memo : 13600000469_0在火箭VS马刺节目中拍照上墙!
     * objectId : 4000559406704207880000008609
     * objectType : {"message":"香蕉球视频","name":"XJQ_VIDEO"}
     * properties : {"channelId":"100016","channelTile":"火箭VS马刺"}
     * replyCount : 0
     * setTop : false
     * subjectId : 15702897
     * transmitCount : 0
     * transmited : false
     * userId : 8201704060500480
     * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201704060500480&mt=
     */

    public int collectId;
    public boolean commentOff;
    public boolean delete;
    public String gmtCreate;
    public int likeCount;
    public boolean liked;
    public String loginName;
    public String memo;
    public String objectId;
    public NormalObject objectType;
    public PropertiesBean properties;
    public int replyCount;
    public boolean setTop;
    public String subjectId;
    public int transmitCount;
    public boolean transmited;
    public String userId;
    public String userLogoUrl;
    public String summary;

    public int getCollectId() {
        return collectId;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }

    public boolean isCommentOff() {
        return commentOff;
    }

    public void setCommentOff(boolean commentOff) {
        this.commentOff = commentOff;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public NormalObject getObjectType() {
        return objectType;
    }

    public void setObjectType(NormalObject objectType) {
        this.objectType = objectType;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isSetTop() {
        return setTop;
    }

    public void setSetTop(boolean setTop) {
        this.setTop = setTop;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getTransmitCount() {
        return transmitCount;
    }

    public void setTransmitCount(int transmitCount) {
        this.transmitCount = transmitCount;
    }

    public boolean isTransmited() {
        return transmited;
    }

    public void setTransmited(boolean transmited) {
        this.transmited = transmited;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public static class PropertiesBean {
        /**
         * channelId : 100016
         * channelTile : 火箭VS马刺
         */

        public String channelId;
        public String channelTile;
        public String videoFirstFrameImageUrl;
        public String videoUrl;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelTile() {
            return channelTile;
        }

        public void setChannelTile(String channelTile) {
            this.channelTile = channelTile;
        }
    }
}
