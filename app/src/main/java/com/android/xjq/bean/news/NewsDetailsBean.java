package com.android.xjq.bean.news;

/**
 * Created by zhouyi on 2016/1/13 11:16.
 */
public class NewsDetailsBean {

    private String shareUrl;

    private InfoClientSimpleBean infoClientSimple;

    private String nowDate;

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setInfoClientSimple(InfoClientSimpleBean infoClientSimple) {
        this.infoClientSimple = infoClientSimple;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public InfoClientSimpleBean getInfoClientSimple() {
        return infoClientSimple;
    }

    public String getNowDate() {
        return nowDate;
    }

    public static class InfoClientSimpleBean {

        private int id;
        private String title;
        private int replyCount;
        private int likeCount;
        private boolean liked;
        private String content;
        private int collectId;
        private String gmtPublish;
        private String summary;
        private boolean commentOff;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCollectId(int collectId) {
            this.collectId = collectId;
        }

        public void setGmtPublish(String gmtPublish) {
            this.gmtPublish = gmtPublish;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public boolean isLiked() {
            return liked;
        }

        public String getContent() {
            return content;
        }

        public int getCollectId() {
            return collectId;
        }

        public String getGmtPublish() {
            return gmtPublish;
        }

        public boolean isCommentOff() {
            return commentOff;
        }

        public void setCommentOff(boolean commentOff) {
            this.commentOff = commentOff;
        }
    }
}
