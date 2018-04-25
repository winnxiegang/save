package com.android.xjq.bean.comment;

/**
 * Created by zaozao on 2017/2/10.
 */

public class Properties {
    private String purchaseNo;

    private String jczqBizId;

    private String jclqBizId;

    private ImageUrlBean midImageUrl;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getJczqBizId() {
        return jczqBizId;
    }

    public void setJczqBizId(String jczqBizId) {
        this.jczqBizId = jczqBizId;
    }

    public String getJclqBizId() {
        return jclqBizId;
    }

    public void setJclqBizId(String jclqBizId) {
        this.jclqBizId = jclqBizId;
    }

    public ImageUrlBean getMidImageUrl() {
        return midImageUrl;
    }

    public void setMidImageUrl(ImageUrlBean midImageUrl) {
        this.midImageUrl = midImageUrl;
    }


    public static class ImageUrlBean {

        private String srcHeight;
        private String srcWidth;
        private String url;

        public String getSrcHeight() {
            return srcHeight;
        }

        public void setSrcHeight(String srcHeight) {
            this.srcHeight = srcHeight;
        }

        public String getSrcWidth() {
            return srcWidth;
        }

        public void setSrcWidth(String srcWidth) {
            this.srcWidth = srcWidth;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
