package com.android.xjq.bean.recharge;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lingjiu on 2017/6/8.
 */

public class DepositBaseInfoBean {


    /**
     * success : true
     * nowDate : 2017-06-08 14:47:55
     * jumpLogin : false
     * priceList : ["6","8","68","108","298","588"]
     * depositActivityImageUrl : http://img-livemapi.huohongshe.net/deposit/activity.png
     * pointcoinExchangeRate : 100.0
     * goldcoinExchangeRate : 1000.0
     * userBaseInfo : {"success":true,"nowDate":"2017-06-08 14:47:55","jumpLogin":false,"userInfo":{"userId":"8201704060510016","loginName":"lingjiu_01","thirdChannelAndLogoUrlMap":{},"userLevelPicUrl":"http://livemapi.huohongshe.net/userLevelImg.resource?levelCode=lv5","avatarUrl":"http://livemapi.huohongshe.net/userLogo.resource?userId=8201704060510016&timestamp=1491465028000"},"pointCoinAmount":0,"goldCoinAmount":3735014}
     * awardGoldcoin : true
     */
    private String depositActivityImageUrl;
    private double pointcoinExchangeRate;
    private UserBaseInfoBean userBaseInfo;
    private List<String> priceList;


    public String getDepositActivityImageUrl() {
        return depositActivityImageUrl;
    }

    public void setDepositActivityImageUrl(String depositActivityImageUrl) {
        this.depositActivityImageUrl = depositActivityImageUrl;
    }

    public double getPointcoinExchangeRate() {
        return pointcoinExchangeRate;
    }

    public void setPointcoinExchangeRate(double pointcoinExchangeRate) {
        this.pointcoinExchangeRate = pointcoinExchangeRate;
    }

    public UserBaseInfoBean getUserBaseInfo() {
        return userBaseInfo;
    }

    public void setUserBaseInfo(UserBaseInfoBean userBaseInfo) {
        this.userBaseInfo = userBaseInfo;
    }

    public List<String> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<String> priceList) {
        this.priceList = priceList;
    }

    public static class UserBaseInfoBean {
        /**
         * success : true
         * nowDate : 2017-06-08 14:47:55
         * jumpLogin : false
         * userInfo : {"userId":"8201704060510016","loginName":"lingjiu_01","thirdChannelAndLogoUrlMap":{},"userLevelPicUrl":"http://livemapi.huohongshe.net/userLevelImg.resource?levelCode=lv5","avatarUrl":"http://livemapi.huohongshe.net/userLogo.resource?userId=8201704060510016&timestamp=1491465028000"}
         * pointCoinAmount : 0.0
         * goldCoinAmount : 3735014.0
         */
        private UserInfoBean userInfo;
        private BigDecimal pointCoinAmount;
        private BigDecimal goldCoinAmount;
        private BigDecimal giftCoinAmount;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public BigDecimal getPointCoinAmount() {
            return pointCoinAmount;
        }

        public void setPointCoinAmount(BigDecimal pointCoinAmount) {
            this.pointCoinAmount = pointCoinAmount;
        }

        public BigDecimal getGoldCoinAmount() {
            return goldCoinAmount;
        }

        public void setGoldCoinAmount(BigDecimal goldCoinAmount) {
            this.goldCoinAmount = goldCoinAmount;
        }

        public BigDecimal getGiftCoinAmount() {
            return giftCoinAmount;
        }

        public void setGiftCoinAmount(BigDecimal giftCoinAmount) {
            this.giftCoinAmount = giftCoinAmount;
        }
    }

}
