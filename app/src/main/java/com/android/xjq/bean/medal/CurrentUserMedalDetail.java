package com.android.xjq.bean.medal;

import java.util.List;

/**
 * Created by lingjiu on 2017/9/27.
 */

public class CurrentUserMedalDetail {
    private UserMedalBean userMedalDetail;
    private int awardNum;
    private boolean anchorApply;
    private int pointcoinAmount;
    private int goldcoinCoinAmount;
    private List<UserMedalBean.MedalLabelConfigListBean> medalLabelConfigList;

    public int getGoldcoinCoinAmount() {
        return goldcoinCoinAmount;
    }

    public void setGoldcoinCoinAmount(int goldcoinCoinAmount) {
        this.goldcoinCoinAmount = goldcoinCoinAmount;
    }

    public List<UserMedalBean.MedalLabelConfigListBean> getMedalLabelConfigList() {
        return medalLabelConfigList;
    }

    public void setMedalLabelConfigList(List<UserMedalBean.MedalLabelConfigListBean> medalLabelConfigList) {
        this.medalLabelConfigList = medalLabelConfigList;
    }

    public int getPointcoinAmount() {
        return pointcoinAmount;
    }

    public void setPointcoinAmount(int pointcoinAmount) {
        this.pointcoinAmount = pointcoinAmount;
    }

    public UserMedalBean getUserMedalDetail() {
        return userMedalDetail;
    }

    public void setUserMedalDetail(UserMedalBean userMedalDetail) {
        this.userMedalDetail = userMedalDetail;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public boolean isAnchorApply() {
        return anchorApply;
    }

    public void setAnchorApply(boolean anchorApply) {
        this.anchorApply = anchorApply;
    }

    static class MedalLabelConfigSimple{
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
