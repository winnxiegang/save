package com.android.xjq.bean.live.main.gift;

import java.util.List;

/**
 * Created by lingjiu on 2017/6/19.
 * 批量赠送礼物,结果响应
 */

public class SendGiftResultBean {

    /**
     * success : true
     * nowDate : 2017-06-19 11:13:34
     * jumpLogin : false
     * resultList : [{"success":true,"resultMap":{},"purchaseNo":"67012017061903427094","giftConfigId":21,"giftName":"收米","giftShowUrl":"http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/Shoumi.png\n","totalCount":1},{"success":true,"resultMap":{},"purchaseNo":"67012017061903427095","giftConfigId":21,"giftName":"收米","giftShowUrl":"http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/Shoumi.png\n","totalCount":1},{"success":true,"resultMap":{},"purchaseNo":"67012017061903427096","giftConfigId":21,"giftName":"收米","giftShowUrl":"http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/Shoumi.png\n","totalCount":1}]
     */

    private boolean jumpLogin;
    private ErrorBean error;
    private List<ResultListBean> resultList;

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class ErrorBean{
        private String name;
        private String message;
        private int value;

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

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class ResultListBean {
        /**
         * success : true
         * resultMap : {}
         * purchaseNo : 67012017061903427094
         * giftConfigId : 21
         * giftName : 收米
         * giftShowUrl : http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/Shoumi.png
         * <p>
         * totalCount : 1
         */

        private boolean success;
        private ResultMapBean resultMap;
        private String purchaseNo;
        private int giftConfigId;
        private String giftName;
        private String giftShowUrl;
        private int totalCount;
        private ErrorBean errorCode;
        private float totalAmount;
        private String payType;

        public float getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(float totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }


        public ErrorBean getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ErrorBean errorCode) {
            this.errorCode = errorCode;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public ResultMapBean getResultMap() {
            return resultMap;
        }

        public void setResultMap(ResultMapBean resultMap) {
            this.resultMap = resultMap;
        }

        public String getPurchaseNo() {
            return purchaseNo;
        }

        public void setPurchaseNo(String purchaseNo) {
            this.purchaseNo = purchaseNo;
        }

        public int getGiftConfigId() {
            return giftConfigId;
        }

        public void setGiftConfigId(int giftConfigId) {
            this.giftConfigId = giftConfigId;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGiftShowUrl() {
            return giftShowUrl;
        }

        public void setGiftShowUrl(String giftShowUrl) {
            this.giftShowUrl = giftShowUrl;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public static class ResultMapBean {
        }
    }
}
