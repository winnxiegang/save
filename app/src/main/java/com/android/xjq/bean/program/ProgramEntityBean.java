package com.android.xjq.bean.program;

import com.android.banana.commlib.utils.TimeUtils;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajiao on 2018\2\2 0002.
 */

public class ProgramEntityBean {

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    //private HashMap<String, List<HeadPortraitBean>> channelAreaIdAndChannelAreaGuestMap;
    private ArrayList<ChannelProgramBean> channelAreaConfigClientSimpleList;
    private String lastestDate = "";
    private String oldestDate = "";
    private String date = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastestDate() {
        return lastestDate;
    }

    public void setLastestDate(String lastestDate) {
        this.lastestDate = lastestDate;
    }

    public String getOldestDate() {
        return oldestDate;
    }

    public void setOldestDate(String oldestDate) {
        this.oldestDate = oldestDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

/*    public HashMap<String, List<HeadPortraitBean>> getChannelAreaIdAndChannelAreaGuestMap() {
        return channelAreaIdAndChannelAreaGuestMap;
    }

    public void setChannelAreaIdAndChannelAreaGuestMap(HashMap<String, List<HeadPortraitBean>> channelAreaIdAndChannelAreaGuestMap) {
        this.channelAreaIdAndChannelAreaGuestMap = channelAreaIdAndChannelAreaGuestMap;
    }*/

    public ArrayList<ChannelProgramBean> getChannelAreaConfigClientSimpleList() {
        return channelAreaConfigClientSimpleList;
    }

    public void setChannelAreaConfigClientSimpleList(ArrayList<ChannelProgramBean> channelAreaConfigClientSimpleList) {
        this.channelAreaConfigClientSimpleList = channelAreaConfigClientSimpleList;
    }

    public static class HeadPortraitBean {

        /**
         * userId : 8201712168726320
         * userName : 巴克开盘0
         * channelAreaId : 100000
         * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712168726320&timestamp=1513394779000
         */

        private String userId;
        private String userName;
        private int channelAreaId;
        private String userLogoUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getChannelAreaId() {
            return channelAreaId;
        }

        public void setChannelAreaId(int channelAreaId) {
            this.channelAreaId = channelAreaId;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

    }

    public static class ChannelProgramBean {
        /**
         * id : 100000
         * areaTitle : 频道创建测试
         * status : LIVE
         * objectType : FOOTBALL
         * objectId : 2664715427407890000004534192
         * gmtStart : 2018-01-27 17:26:53
         * masterAnchorId : 100280
         * masterAnchorUserId : 8201711048675862
         * masterAnchorName : OAP_QB
         * auxiliaryAnchorId : 100274
         * auxiliaryUserId : 8201711108676246
         * auditStatus : PASS
         * raceDataClientSimple : {"id":"2664715427407890000004534192","matchName":"俄青联","guestTeamName":"图拉青年队","homeTeamName":"SKA哈巴罗夫斯克青年队","gmtStart":"2017-11-05 09:00:00","raceStatus":"FINISH"}
         */

        private int id;
        private String areaTitle;
        private String status;
        private String objectType;
        private String objectId;
        private String gmtStart;
        private int masterAnchorId;
        private String masterAnchorUserId;
        private String masterAnchorName;
        private int auxiliaryAnchorId;
        private String auxiliaryUserId;
        private String auditStatus;
        private boolean userOrderChannelArea;
        private RaceDataClientSimpleBean raceDataClientSimple;
        private List<HeadPortraitBean> channelAreaGuestList;  //特邀嘉宾 如果没有则不返回该字段
        @Expose
        private String sectionDate = "";

        public String getSectionDate() {
            if (!"".trim().equals(gmtStart)) {
                sectionDate = TimeUtils.getYueRuiStr(gmtStart) + "  " + "星期" + TimeUtils.getDate(gmtStart, TimeUtils.LONG_DATEFORMAT);
            }
            return sectionDate;
        }

        public void setSectionDate(String sectionDate) {
            this.sectionDate = sectionDate;
        }


        public boolean isUserOrderChannelArea() {
            return userOrderChannelArea;
        }

        public void setUserOrderChannelArea(boolean userOrderChannelArea) {
            this.userOrderChannelArea = userOrderChannelArea;
        }

        public List<HeadPortraitBean> getHeadPortraitBeanList() {
            return channelAreaGuestList;
        }

        public void setHeadPortraitBeanList(List<HeadPortraitBean> channelAreaGuestList) {
            this.channelAreaGuestList = channelAreaGuestList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAreaTitle() {
            return areaTitle;
        }

        public void setAreaTitle(String areaTitle) {
            this.areaTitle = areaTitle;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getGmtStart() {
            return gmtStart;
        }

        public void setGmtStart(String gmtStart) {
            this.gmtStart = gmtStart;
        }

        public int getMasterAnchorId() {
            return masterAnchorId;
        }

        public void setMasterAnchorId(int masterAnchorId) {
            this.masterAnchorId = masterAnchorId;
        }

        public String getMasterAnchorUserId() {
            return masterAnchorUserId;
        }

        public void setMasterAnchorUserId(String masterAnchorUserId) {
            this.masterAnchorUserId = masterAnchorUserId;
        }

        public String getMasterAnchorName() {
            return masterAnchorName;
        }

        public void setMasterAnchorName(String masterAnchorName) {
            this.masterAnchorName = masterAnchorName;
        }

        public int getAuxiliaryAnchorId() {
            return auxiliaryAnchorId;
        }

        public void setAuxiliaryAnchorId(int auxiliaryAnchorId) {
            this.auxiliaryAnchorId = auxiliaryAnchorId;
        }

        public String getAuxiliaryUserId() {
            return auxiliaryUserId;
        }

        public void setAuxiliaryUserId(String auxiliaryUserId) {
            this.auxiliaryUserId = auxiliaryUserId;
        }

        public String getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
        }

        public RaceDataClientSimpleBean getRaceDataClientSimple() {
            return raceDataClientSimple;
        }

        public void setRaceDataClientSimple(RaceDataClientSimpleBean raceDataClientSimple) {
            this.raceDataClientSimple = raceDataClientSimple;
        }

        public static class RaceDataClientSimpleBean {
            /**
             * id : 2664715427407890000004534192
             * matchName : 俄青联
             * guestTeamName : 图拉青年队
             * homeTeamName : SKA哈巴罗夫斯克青年队
             * gmtStart : 2017-11-05 09:00:00
             * raceStatus : FINISH
             */

            private String id;
            private String matchName;
            private String guestTeamName;
            private String homeTeamName;
            private String gmtStart;
            private String raceStatus;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMatchName() {
                return matchName;
            }

            public void setMatchName(String matchName) {
                this.matchName = matchName;
            }

            public String getGuestTeamName() {
                return guestTeamName;
            }

            public void setGuestTeamName(String guestTeamName) {
                this.guestTeamName = guestTeamName;
            }

            public String getHomeTeamName() {
                return homeTeamName;
            }

            public void setHomeTeamName(String homeTeamName) {
                this.homeTeamName = homeTeamName;
            }

            public String getGmtStart() {
                return gmtStart;
            }

            public void setGmtStart(String gmtStart) {
                this.gmtStart = gmtStart;
            }

            public String getRaceStatus() {
                return raceStatus;
            }

            public void setRaceStatus(String raceStatus) {
                this.raceStatus = raceStatus;
            }
        }
    }

}
