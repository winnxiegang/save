package com.android.xjq.bean.gamePK;

import android.text.TextUtils;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by lingjiu on 2018/3/1.
 */
public class PkGameBoarInfoBean {
    /**
     * gmtGameFinished : 2018-02-23 16:38:14
     * gmtGamePause : 2018-02-27 20:36:58
     * gmtGameStart : 2018-02-23 16:36:58
     * id : 4000524381896607890000008299
     * lotteryResultOptionId : 4000524381897207890000009580
     * lotteryStatus : {"message":"结束","name":"FINISH"}
     * maxOptionCount : 1
     * minOptionCount : 1
     * optionOneEntry : {"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=40109&timestamp=1513062884000","betFormNo":"40109","betFormSingleFee":100,"boardId":"4000524381896607890000008299","currencyType":{"message":"金币","name":"GOLD_COIN"},"id":"4000524381897207890000009580","optionCode":"OPTION_ONE","optionName":"能","rankUserList":[{"guest":false,"loginName":"香蕉球二","userId":"8201711028675848","userLogoUrl":"http://mapi1.xjq.net/userLogo.resource?userId=8201711028675848&mt=1509606366000"},{"guest":false,"loginName":"boli假面","userId":"8201711308725942","userLogoUrl":"http://mapi1.xjq.net/userLogo.resource?userId=8201711308725942&mt="}],"totalFee":200,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":2}
     * optionTwoEntry : {"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=40111&timestamp=1513063236000","betFormNo":"40111","betFormSingleFee":100,"boardId":"4000524381896607890000008299","currencyType":{"message":"金币","name":"GOLD_COIN"},"id":"4000524381897307890000006315","optionCode":"OPTION_TWO","optionName":"不能","rankUserList":[],"totalFee":300,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":3}
     * playType : {"message":"PK","name":"PK"}
     * prizeStatus : PRIZE_FINISH
     * saleStatus : {"message":"结束","name":"FINISH"}
     * sourceId : 8201711028675848
     * sourceType : {"message":"直播","name":"LIVE"}
     * title : 这次创建能否一发入魂？
     */

    public String gmtGameFinished;
    public String gmtGamePause;
    public String gmtGameStart;
    public String id;
    public String lotteryResultOptionId;
    public NormalObject lotteryStatus;
    public int maxOptionCount;
    public int minOptionCount;
    public PkOptionEntryBean optionOneEntry;
    public PkOptionEntryBean optionTwoEntry;
    public NormalObject playType;
    public String prizeStatus;
    public NormalObject saleStatus;
    public String sourceId;
    public NormalObject sourceType;
    public String title;

    //是否是获胜方
    public boolean isLeftWin() {
        return TextUtils.equals(lotteryResultOptionId, optionOneEntry.id);
    }

    public String getGmtGameFinished() {
        return gmtGameFinished;
    }

    public void setGmtGameFinished(String gmtGameFinished) {
        this.gmtGameFinished = gmtGameFinished;
    }

    public String getGmtGamePause() {
        return gmtGamePause;
    }

    public void setGmtGamePause(String gmtGamePause) {
        this.gmtGamePause = gmtGamePause;
    }

    public String getGmtGameStart() {
        return gmtGameStart;
    }

    public void setGmtGameStart(String gmtGameStart) {
        this.gmtGameStart = gmtGameStart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotteryResultOptionId() {
        return lotteryResultOptionId;
    }

    public void setLotteryResultOptionId(String lotteryResultOptionId) {
        this.lotteryResultOptionId = lotteryResultOptionId;
    }

    public PkOptionEntryBean getOptionTwoEntry() {
        return optionTwoEntry;
    }

    public NormalObject getPlayType() {
        return playType;
    }

    public void setPlayType(NormalObject playType) {
        this.playType = playType;
    }

    public NormalObject getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(NormalObject saleStatus) {
        this.saleStatus = saleStatus;
    }

    public NormalObject getSourceType() {
        return sourceType;
    }

    public void setSourceType(NormalObject sourceType) {
        this.sourceType = sourceType;
    }

    public NormalObject getLotteryStatus() {
        return lotteryStatus;
    }

    public void setLotteryStatus(NormalObject lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

    public void setOptionTwoEntry(PkOptionEntryBean optionTwoEntry) {
        this.optionTwoEntry = optionTwoEntry;
    }

    public int getMaxOptionCount() {
        return maxOptionCount;
    }

    public void setMaxOptionCount(int maxOptionCount) {
        this.maxOptionCount = maxOptionCount;
    }

    public int getMinOptionCount() {
        return minOptionCount;
    }

    public void setMinOptionCount(int minOptionCount) {
        this.minOptionCount = minOptionCount;
    }

    public PkOptionEntryBean getOptionOneEntry() {
        return optionOneEntry;
    }

    public void setOptionOneEntry(PkOptionEntryBean optionOneEntry) {
        this.optionOneEntry = optionOneEntry;
    }

    public String getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(String prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
