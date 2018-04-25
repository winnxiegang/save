package com.android.xjq.bean.live.main;

import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/4/6.
 */

public class SeatInfoBean implements BaseOperator {

    private List<ChannelVipUserSimpleInfo> channelVipUserSimpleInfoList;
    private ChannelVipUserSimpleInfo dragonSitterSimpleInfo;
    //vip的座位个数
    private int showSeatCount;
    @Expose
    private List<ChannelVipUserSimpleInfo> seatSitterSimpleList;

    @Override
    public void operatorData() {
        seatSitterSimpleList = new ArrayList<>();
        if (dragonSitterSimpleInfo != null) {
            seatSitterSimpleList.add(dragonSitterSimpleInfo);
        } else {
            seatSitterSimpleList.add(new ChannelVipUserSimpleInfo());
        }
        if (channelVipUserSimpleInfoList != null) {
            seatSitterSimpleList.addAll(channelVipUserSimpleInfoList);
        }
        for (int i = seatSitterSimpleList.size(); i < showSeatCount + 1; i++) {
            seatSitterSimpleList.add(new ChannelVipUserSimpleInfo());
        }
    }

    public List<ChannelVipUserSimpleInfo> getChannelVipUserSimpleInfoList() {
        return channelVipUserSimpleInfoList;
    }

    public void setChannelVipUserSimpleInfoList(List<ChannelVipUserSimpleInfo> channelVipUserSimpleInfoList) {
        this.channelVipUserSimpleInfoList = channelVipUserSimpleInfoList;
    }

    public List<ChannelVipUserSimpleInfo> getSeatSitterSimpleList() {
        return seatSitterSimpleList;
    }

    public void setSeatSitterSimpleList(List<ChannelVipUserSimpleInfo> seatSitterSimpleList) {
        this.seatSitterSimpleList = seatSitterSimpleList;
    }

    public ChannelVipUserSimpleInfo getDragonSitterSimpleInfo() {
        return dragonSitterSimpleInfo;
    }

    public void setDragonSitterSimpleInfo(ChannelVipUserSimpleInfo dragonSitterSimpleInfo) {
        this.dragonSitterSimpleInfo = dragonSitterSimpleInfo;
    }

    public static class ChannelVipUserSimpleInfo {
        public String loginName;
        public String userId;
        public String userLogoUrl;
        public List<UserMedalLevelBean> userMedalLevelList;


        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
