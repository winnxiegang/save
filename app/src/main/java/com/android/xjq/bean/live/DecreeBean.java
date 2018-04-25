package com.android.xjq.bean.live;

import java.util.List;

/**
 * Created by ajiao on 2018\3\10 0010.
 */

public class DecreeBean {
    /**
     * issuePrizeItemIdAndTotalAmountSimpleMap : {"4000637769881000000980052560":[{"currencyCode":"GIFTCOIN","totalAmount":300}]}
     * jumpLogin : false
     * nowDate : 2018-03-13 10:41:21
     * success : true
     */

    private List<PrizeBean> totalAmountSimple;
    private boolean jumpLogin;
    private String nowDate;

    public List<PrizeBean> getTotalAmountSimple() {
        return totalAmountSimple;
    }

    public void setTotalAmountSimple(List<PrizeBean> totalAmountSimple) {
        this.totalAmountSimple = totalAmountSimple;
    }

    private boolean success;



    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public static class PrizeBean {
        /**
         * currencyCode : GIFTCOIN
         * totalAmount : 300.0
         */

        private String currencyCode;
        private double totalAmount;
        private String prizeItemId;

        public String getPrizeItemId() {
            return prizeItemId;
        }

        public void setPrizeItemId(String prizeItemId) {
            this.prizeItemId = prizeItemId;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }


}
