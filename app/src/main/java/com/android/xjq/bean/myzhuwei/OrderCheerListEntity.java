package com.android.xjq.bean.myzhuwei;

import android.text.TextUtils;

import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2018/2/27.
 */

public class OrderCheerListEntity implements BaseOperator {


    /**
     * gameBoardMap : {"4000549613252807890000009105":{"gameBoardOptionEntries":[{"betForm":"GIFT","betFormNo":"24","boardId":"4000549613252807890000009105","currencyType":{"message":"金币","name":"GOLD_COIN"},"enabled":true,"gmtCreate":"2018-02-26 14:42:19","gmtModified":"2018-02-26 14:42:19","id":"4000549614133707890000009657","optionCode":"GUEST_WIN","optionName":"客胜","totalFee":1100,"totalMultiple":0,"totalPlayOrderCount":2,"totalPlayUserCount":1},{"betForm":"GIFT","betFormNo":"23","boardId":"4000549613252807890000009105","currencyType":{"message":"金币","name":"GOLD_COIN"},"enabled":true,"gmtCreate":"2018-02-26 14:42:19","gmtModified":"2018-02-26 14:42:19","id":"4000549614133407890000009956","optionCode":"HOME_WIN","optionName":"主胜","totalFee":1100,"totalMultiple":0,"totalPlayOrderCount":2,"totalPlayUserCount":1}],"guestTeamName":"尼特拉","homeTeamName":"鲁森比洛克","id":"4000549613252807890000009105","plate":0.5,"raceId":"4000549607443107890980014714","raceStageType":{"code":"FULL","enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-11 20:56:53","id":1,"name":"全场","orderNum":1},"raceType":"FOOTBALL","sourceId":"8201711028675848"}}
     * guestWinlist : [{"multiple":11,"userAlias":"新角","userId":"8201711028675844","userLogoUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711028675844&timestamp="}]
     * homeWinlist : [{"multiple":11,"userAlias":"新角","userId":"8201711028675844","userLogoUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711028675844&timestamp="}]
     * jumpLogin : false
     * nowDate : 2018-02-27 19:25:25
     * success : true
     * totalGuestPaid : 1100.0
     * totalHomePaid : 1100.0
     */
    private String nowDate;
    private boolean success;
    private long totalGuestPaid;
    private long totalHomePaid;
    private List<CheerInfoBean> guestWinlist;
    private List<CheerInfoBean> homeWinlist;
    private ZhuweiDetailBean.GameBoardBean gameBoardClientSimple;

    //盘口描述(-0.5球)
    @Expose
    private String plateDescribe;


    @Override
    public void operatorData() {

    }

    public String getPlateDescribe() {
        if (!TextUtils.isEmpty(plateDescribe))
            return plateDescribe;
        if (gameBoardClientSimple != null) {
            double plate = gameBoardClientSimple.getPlate();
            if ("FOOTBALL".equals(gameBoardClientSimple.getRaceType())) {
                plateDescribe = (plate > 0 ? "+" : "") + (plate + "球");
            } else {
                plateDescribe = (plate > 0 ? "+" : "") + (plate + "分");
            }
            plateDescribe = (plate > 0 ? "<font color='#FF2335'>" : "<font color='#00C10D'>")
                    + plateDescribe + "</font>";
        }
        return plateDescribe;
    }

    public ZhuweiDetailBean.GameBoardBean getGameBoardClientSimple() {
        return gameBoardClientSimple;
    }

    public void setGameBoardClientSimple(ZhuweiDetailBean.GameBoardBean gameBoardClientSimple) {
        this.gameBoardClientSimple = gameBoardClientSimple;
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

    public long getTotalGuestPaid() {
        return totalGuestPaid;
    }

    public long getTotalHomePaid() {
        return totalHomePaid;
    }

    public void setTotalGuestPaid(long totalGuestPaid) {
        this.totalGuestPaid = totalGuestPaid;
    }

    public void setTotalHomePaid(long totalHomePaid) {
        this.totalHomePaid = totalHomePaid;
    }

    public List<CheerInfoBean> getGuestWinlist() {
        return guestWinlist;
    }

    public void setGuestWinlist(List<CheerInfoBean> guestWinlist) {
        this.guestWinlist = guestWinlist;
    }

    public List<CheerInfoBean> getHomeWinlist() {
        return homeWinlist;
    }

    public void setHomeWinlist(List<CheerInfoBean> homeWinlist) {
        this.homeWinlist = homeWinlist;
    }

    public static class CheerInfoBean {
        /**
         * multiple : 11
         * userAlias : 新角
         * userId : 8201711028675844
         * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711028675844&timestamp=
         */

        private int multiple;
        private String userAlias;
        private String userId;
        private String userLogoUrl;

        public int getMultiple() {
            return multiple;
        }

        public void setMultiple(int multiple) {
            this.multiple = multiple;
        }

        public String getUserAlias() {
            return userAlias;
        }

        public void setUserAlias(String userAlias) {
            this.userAlias = userAlias;
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


    }


    public static class GameBoardOption {

        /**
         * id : 4000729798233807890970026598
         * boardId : 4000729798232607890970028660
         * optionCode : GUEST_WIN
         * optionName : 客胜
         * enabled : true
         * gmtCreate : 2018-03-19 11:13:02
         * gmtModified : 2018-03-19 11:13:02
         * totalFee : 29700
         * totalPlayUserCount : 1
         * totalPlayOrderCount : 0
         * totalMultiple : 297
         * betForm : GIFT
         * betFormNo : 60018
         */

        public String id;
        public String boardId;
        public String optionCode;
        public String optionName;
        public boolean enabled;
        public String gmtCreate;
        public String gmtModified;
        public int totalFee;
        public int totalPlayUserCount;
        public int totalPlayOrderCount;
        public int totalMultiple;
        public String betForm;
        public String betFormNo;
    }
}
