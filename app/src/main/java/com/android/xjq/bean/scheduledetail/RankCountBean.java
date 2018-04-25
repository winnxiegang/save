package com.android.xjq.bean.scheduledetail;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import org.json.JSONObject;

/**
 * Created by zhouyi on 2016/5/9 16:22.
 */
public class RankCountBean {
    private String homeTips;

    private String guestTips;

    private RankDataMapBean guestRankDataMap;

    private RankDataMapBean homeRankDataMap;

    @Expose
    private boolean dataChanged = true;

    @Expose
    private boolean homeRankDataNull = true;

    @Expose
    private boolean guestRankDataNull = true;

    public RankCountBean(JSONObject jo) {

        RankCountBean bean = new Gson().fromJson(jo.toString(), RankCountBean.class);

        this.homeTips = bean.homeTips;
        this.guestTips = bean.guestTips;
//        if (raceDetail.getJczqData().isInnerTeamReverse()) {
//
//            this.guestRankDataMap = bean.homeRankDataMap;
//
//            this.homeRankDataMap = bean.guestRankDataMap;
//
//        } else {
            this.guestRankDataMap = bean.guestRankDataMap;

            this.homeRankDataMap = bean.homeRankDataMap;
//        }

        if (guestRankDataMap != null) {
            if (guestRankDataMap.getAll() != null ||
                    guestRankDataMap.getGuest() != null ||
                    guestRankDataMap.getHome() != null ||
                    guestRankDataMap.getLastSix() != null) {
                guestRankDataNull = false;
            }
        }

        if (homeRankDataMap != null) {
            if (homeRankDataMap.getLastSix() != null ||
                    homeRankDataMap.getHome() != null ||
                    homeRankDataMap.getGuest() != null ||
                    homeRankDataMap.getAll() != null) {
                homeRankDataNull = false;
            }
        }
    }

    public RankDataMapBean getGuestRankDataMap() {
        return guestRankDataMap;
    }

    public void setGuestRankDataMap(RankDataMapBean guestRankDataMap) {
        this.guestRankDataMap = guestRankDataMap;
    }

    public RankDataMapBean getHomeRankDataMap() {
        return homeRankDataMap;
    }

    public void setHomeRankDataMap(RankDataMapBean homeRankDataMap) {
        this.homeRankDataMap = homeRankDataMap;
    }

    public String getHomeTips() {
        return homeTips;
    }

    public String getGuestTips() {
        return guestTips;
    }

    public static class RankDataMapBean {

        private RankInfoBean guest;

        private RankInfoBean home;

        private RankInfoBean all;

        private RankInfoBean lastSix;

        public RankInfoBean getGuest() {
            return guest;
        }

        public void setGuest(RankInfoBean guest) {
            this.guest = guest;
        }

        public RankInfoBean getHome() {
            return home;
        }

        public void setHome(RankInfoBean home) {
            this.home = home;
        }

        public RankInfoBean getAll() {
            return all;
        }

        public void setAll(RankInfoBean all) {
            this.all = all;
        }

        public RankInfoBean getLastSix() {
            return lastSix;
        }

        public void setLastSix(RankInfoBean lastSix) {
            this.lastSix = lastSix;
        }

    }


    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }

    public boolean isHomeRankDataNull() {
        return homeRankDataNull;
    }

    public void setHomeRankDataNull(boolean homeRankDataNull) {
        this.homeRankDataNull = homeRankDataNull;
    }

    public boolean isGuestRankDataNull() {
        return guestRankDataNull;
    }

    public void setGuestRankDataNull(boolean guestRankDataNull) {
        this.guestRankDataNull = guestRankDataNull;
    }
}
