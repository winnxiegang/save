package com.android.banana.commlib.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2017/11/6.
 */

public class BetGiftEntity {


    /**
     * giftGiveMaxNum : 9999
     * giftList : [{"content":"鼓掌","giftImageUrl":"http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=23&timestamp=1509676311000","giftType":"BET","id":23,"name":"鼓掌","payTypes":["GOLD_COIN"],"price":100,"properties":{"PRIZE_POOL":"false"}},{"content":"号角","giftImageUrl":"http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=25&timestamp=1509676326000","giftType":"BET","id":25,"name":"号角","payTypes":["GOLD_COIN"],"price":300,"properties":{"PRIZE_POOL":"false"}},{"content":"助威棒","giftImageUrl":"http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=24&timestamp=1509676319000","giftType":"BET","id":24,"name":"助威棒","payTypes":["GOLD_COIN"],"price":200,"properties":{"PRIZE_POOL":"false"}},{"content":"啦啦队","giftImageUrl":"http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=27&timestamp=1509676338000","giftType":"BET","id":27,"name":"啦啦队","payTypes":["GOLD_COIN"],"price":500,"properties":{"PRIZE_POOL":"false"}},{"content":"打鼓","giftImageUrl":"http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=26&timestamp=1509676332000","giftType":"BET","id":26,"name":"打鼓","payTypes":["GOLD_COIN"],"price":400,"properties":{"PRIZE_POOL":"false"}}]
     * jumpLogin : false
     * nowDate : 2017-11-06 20:31:54
     * success : true
     */

    private int giftGiveMaxNum;
    private boolean jumpLogin;
    private List<BetGiftBean> giftList;

    public int getGiftGiveMaxNum() {
        return giftGiveMaxNum;
    }

    public void setGiftGiveMaxNum(int giftGiveMaxNum) {
        this.giftGiveMaxNum = giftGiveMaxNum;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public List<BetGiftBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<BetGiftBean> giftList) {
        this.giftList = giftList;
    }

    public static class BetGiftBean {
        /**
         * content : 鼓掌
         * giftImageUrl : http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=23&timestamp=1509676311000
         * giftType : BET
         * id : 23
         * name : 鼓掌
         * payTypes : ["GOLD_COIN"]
         * price : 100.0
         * properties : {"PRIZE_POOL":"false"}
         */

        private String content;
        private String giftImageUrl;
        private String giftType;
        private int id;
        private String name;
        private double price;
        private PropertiesBean properties;
        private List<String> payTypes;
        @Expose
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGiftImageUrl() {
            return giftImageUrl;
        }

        public void setGiftImageUrl(String giftImageUrl) {
            this.giftImageUrl = giftImageUrl;
        }

        public String getGiftType() {
            return giftType;
        }

        public void setGiftType(String giftType) {
            this.giftType = giftType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public List<String> getPayTypes() {
            return payTypes;
        }

        public void setPayTypes(List<String> payTypes) {
            this.payTypes = payTypes;
        }

        public static class PropertiesBean {
            /**
             * PRIZE_POOL : false
             */

            private String PRIZE_POOL;

            public String getPRIZE_POOL() {
                return PRIZE_POOL;
            }

            public void setPRIZE_POOL(String PRIZE_POOL) {
                this.PRIZE_POOL = PRIZE_POOL;
            }
        }
    }
}
