package com.android.xjq.bean.gamePK;

import com.android.banana.commlib.bean.NormalObject;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/1.
 */
public class PkOptionEntryBean {
    /**
     * betFormImageUrl : http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=40109&timestamp=1513062884000
     * betFormNo : 40109
     * betFormSingleFee : 100.0
     * boardId : 4000524381896607890000008299
     * currencyType : {"message":"金币","name":"GOLD_COIN"}
     * id : 4000524381897207890000009580
     * optionCode : OPTION_ONE
     * optionName : 能
     * rankUserList : [{"guest":false,"loginName":"香蕉球二","userId":"8201711028675848","userLogoUrl":"http://mapi1.xjq.net/userLogo.resource?userId=8201711028675848&mt=1509606366000"},{"guest":false,"loginName":"boli假面","userId":"8201711308725942","userLogoUrl":"http://mapi1.xjq.net/userLogo.resource?userId=8201711308725942&mt="}]
     * totalFee : 200.0
     * totalMultiple : 0
     * totalPaidFee : 0.0
     * totalPlayUserCount : 2
     */

    public String betFormImageUrl;
    public String betFormNo;
    public String betFormName;
    public double betFormSingleFee;
    public String boardId;
    public NormalObject currencyType;
    public String id;
    public String optionCode;
    public String optionName;
    public double totalFee;
    public int totalMultiple;
    public double totalPaidFee;
    public int totalPlayUserCount;
    public int currentUserTotalMultiple;
    public List<RankUserListBean> rankUserList;

    /* map.put("payTypeNo", "betFormNo");
            map.put("boardId", "boardId");
            map.put("totalFee", betFormSingleFee*1);
            map.put("singleTotalFee", "betFormSingleFee");
            map.put("multiple", 1);
            map.put("keyAndOptions", "boardId@OptionOne");*/

    public String getBetFormImageUrl() {
        return betFormImageUrl;
    }

    public void setBetFormImageUrl(String betFormImageUrl) {
        this.betFormImageUrl = betFormImageUrl;
    }

    public String getBetFormNo() {
        return betFormNo;
    }

    public void setBetFormNo(String betFormNo) {
        this.betFormNo = betFormNo;
    }

    public double getBetFormSingleFee() {
        return betFormSingleFee;
    }

    public void setBetFormSingleFee(double betFormSingleFee) {
        this.betFormSingleFee = betFormSingleFee;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public NormalObject getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(NormalObject currencyType) {
        this.currencyType = currencyType;
    }

    public void setRankUserList(List<RankUserListBean> rankUserList) {
        this.rankUserList = rankUserList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public int getTotalMultiple() {
        return totalMultiple;
    }

    public void setTotalMultiple(int totalMultiple) {
        this.totalMultiple = totalMultiple;
    }

    public double getTotalPaidFee() {
        return totalPaidFee;
    }

    public void setTotalPaidFee(double totalPaidFee) {
        this.totalPaidFee = totalPaidFee;
    }

    public int getTotalPlayUserCount() {
        return totalPlayUserCount;
    }

    public void setTotalPlayUserCount(int totalPlayUserCount) {
        this.totalPlayUserCount = totalPlayUserCount;
    }

    public List<RankUserListBean> getRankUserList() {
        return rankUserList;
    }

    public static class RankUserListBean {

        /**
         * guest : false
         * loginName : 香蕉球二
         * userId : 8201711028675848
         * userLogoUrl : http://mapi1.xjq.net/userLogo.resource?userId=8201711028675848&mt=1509606366000
         */
        public boolean guest;
        public String loginName;
        public String userId;
        public String userLogoUrl;

        public boolean isGuest() {
            return guest;
        }

        public void setGuest(boolean guest) {
            this.guest = guest;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
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
}
