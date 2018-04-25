package com.android.banana.commlib.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by lingjiu on 2017/7/25.
 */
public class GroupCouponInfoBean {

    private AmountAllocateTypeBean amountAllocateType;
    private String couponNo;
    private String createrUserId;
    private String gmtPay;
    private String loginName;
    private String logoUrl;
    private boolean published;
    private StatusBean allocateStatus;
    private String title;
    private String uniqueId;
    private String gmtTimed;
    /**
     * 是否领取过该红包
     */
    @Expose
    private boolean isOwnAllocated;

    public String getGmtTimed() {
        return gmtTimed;
    }

    public void setGmtTimed(String gmtTimed) {
        this.gmtTimed = gmtTimed;
    }

    public boolean isOwnAllocated() {
        return isOwnAllocated;
    }

    public void setIsOwnAllocated(boolean isOwnAllocated) {
        this.isOwnAllocated = isOwnAllocated;
    }

    public AmountAllocateTypeBean getAmountAllocateType() {
        return amountAllocateType;
    }

    public void setAmountAllocateType(AmountAllocateTypeBean amountAllocateType) {
        this.amountAllocateType = amountAllocateType;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getCreaterUserId() {
        return createrUserId;
    }

    public void setCreaterUserId(String createrUserId) {
        this.createrUserId = createrUserId;
    }

    public String getGmtPay() {
        return gmtPay;
    }

    public void setGmtPay(String gmtPay) {
        this.gmtPay = gmtPay;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public StatusBean getStatus() {
        return allocateStatus;
    }

    public void setStatus(StatusBean status) {
        this.allocateStatus = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static class AmountAllocateTypeBean {
        /**
         * message : 随机
         * name : RANDOM
         */

        private String message;
        private String name;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class StatusBean {
        /**
         * message : 初始
         * name : INIT
         */

        private String message;
        private String name;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
