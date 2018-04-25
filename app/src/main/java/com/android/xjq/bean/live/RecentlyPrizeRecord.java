package com.android.xjq.bean.live;

import android.graphics.Color;
import android.text.SpannableStringBuilder;

import com.android.xjq.model.PrizeItemType;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/6/29.
 */

public class RecentlyPrizeRecord implements BaseOperator {
    private List<PrizeRecordEntity> prizeRecordSimples;

    private HashMap<String, String> userIdAndUserNameMap;

    private int intervalTime;

    private String timestamp;

    @Expose
    private CharSequence prizeRecordInfo;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public CharSequence getPrizeRecordInfo() {
        return prizeRecordInfo;
    }

    public void setPrizeRecordInfo(CharSequence prizeRecordInfo) {
        this.prizeRecordInfo = prizeRecordInfo;
    }

    @Override
    public void operatorData() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (userIdAndUserNameMap != null && userIdAndUserNameMap.size() > 0) {
            if (prizeRecordSimples != null && prizeRecordSimples.size() > 0) {
                for (PrizeRecordEntity prizeRecordSimple : prizeRecordSimples) {
                    if (userIdAndUserNameMap.containsKey(prizeRecordSimple.getUserId())) {
                        prizeRecordSimple.setUserName(userIdAndUserNameMap.get(prizeRecordSimple.getUserId()));
                        ssb.append("恭喜");
                        ssb.append(SpannableStringHelper.changeTextColor(prizeRecordSimple.getUserName(), Color.parseColor("#f6ff00")));
                        ssb.append("获得");
                        String prizeType = PrizeItemType.safeValueOf(prizeRecordSimple.getPrizeItemType()).getMessage();
                        ssb.append(SpannableStringHelper.changeTextColor(prizeType, Color.parseColor("#f6ff00")));
                        ssb.append("(" + prizeRecordSimple.getPrizeAmount() + "金币)!赠送礼物就有机会获得超值大奖!   ");
                    }
                }
            }
        }
        prizeRecordInfo = ssb;
    }


    public static class PrizeRecordEntity {
        private String prizeItemTypeMessage;
        private String userType;
        private String issueNo;
        private String prizeItemType;
        private int prizeAmount;
        private String userId;
        @Expose
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPrizeItemTypeMessage() {
            return prizeItemTypeMessage;
        }

        public void setPrizeItemTypeMessage(String prizeItemTypeMessage) {
            this.prizeItemTypeMessage = prizeItemTypeMessage;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getIssueNo() {
            return issueNo;
        }

        public void setIssueNo(String issueNo) {
            this.issueNo = issueNo;
        }

        public String getPrizeItemType() {
            return prizeItemType;
        }

        public void setPrizeItemType(String prizeItemType) {
            this.prizeItemType = prizeItemType;
        }

        public int getPrizeAmount() {
            return prizeAmount;
        }

        public void setPrizeAmount(int prizeAmount) {
            this.prizeAmount = prizeAmount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


    public List<PrizeRecordEntity> getPrizeRecordSimples() {
        return prizeRecordSimples;
    }

    public void setPrizeRecordSimples(List<PrizeRecordEntity> prizeRecordSimples) {
        this.prizeRecordSimples = prizeRecordSimples;
    }

    public HashMap<String, String> getUserIdAndUserNameMap() {
        return userIdAndUserNameMap;
    }

    public void setUserIdAndUserNameMap(HashMap<String, String> userIdAndUserNameMap) {
        this.userIdAndUserNameMap = userIdAndUserNameMap;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }
}
