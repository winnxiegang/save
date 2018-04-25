package com.android.xjq.bean.program;

import com.android.banana.commlib.utils.TimeUtils;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ajiao on 2018\2\1 0001.
 */

public class ProgramEntityBean2 {

    /**
     * success : true
     * nowDate : 2018-02-07 17:31:33
     * jumpLogin : false
     * dateAndChannelAreaConfigMap : {"20180206":[{"id":100030,"areaTitle":"kaishuitest","status":"LIVE","objectType":"BASKETBALL","objectId":"4000332014487107890980016008","gmtStart":"2018-02-06 16:48:09","masterAnchorId":100160,"masterAnchorUserId":"8201711018675831","masterAnchorName":"大鱼1","auxiliaryAnchorId":100145,"auxiliaryUserId":"8201703290460012","auditStatus":"PASS","raceDataClientSimple":{"id":"4000332014487107890980016008","matchName":"NBA","guestTeamName":"独行侠","homeTeamName":"太阳","gmtStart":"2018-02-01 11:30:00","raceStatus":"FINISH"},"userOrderChannelArea":false},{"id":100030,"areaTitle":"kaishuitest","status":"LIVE","objectType":"BASKETBALL","objectId":"4000332014487107890980016008","gmtStart":"2018-02-06 16:48:09","masterAnchorId":100160,"masterAnchorUserId":"8201711018675831","masterAnchorName":"大鱼1","auxiliaryAnchorId":100145,"auxiliaryUserId":"8201703290460012","auditStatus":"PASS","raceDataClientSimple":{"id":"4000332014487107890980016008","matchName":"NBA","guestTeamName":"独行侠","homeTeamName":"太阳","gmtStart":"2018-02-01 11:30:00","raceStatus":"FINISH"},"userOrderChannelArea":false}]}
     * lastestDate : 2018-02-08 23:59:59
     * oldestDate : 2018-02-06 00:00:00
     */

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private HashMap<String, List<ChannelProgramBean>> dateAndChannelAreaConfigMap;
    private String lastestDate;
    private String oldestDate;

    @Expose
    private List<ChannelProgramBean> channelAreaConfigClientSimpleList = new ArrayList<>();

    public List<ChannelProgramBean> getChannelAreaConfigClientSimpleList() {
        channelAreaConfigClientSimpleList.clear();
        if (dateAndChannelAreaConfigMap != null && dateAndChannelAreaConfigMap.size() > 0) {
            for(Map.Entry<String, List<ChannelProgramBean>> entry :  dateAndChannelAreaConfigMap.entrySet()) {
                channelAreaConfigClientSimpleList.addAll(entry.getValue());
            }
        }
        return channelAreaConfigClientSimpleList;
    }

    public void setChannelAreaConfigClientSimpleList(List<ChannelProgramBean> channelAreaConfigClientSimpleList) {
        this.channelAreaConfigClientSimpleList = channelAreaConfigClientSimpleList;
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

    public HashMap<String, List<ChannelProgramBean>>  getDateAndChannelAreaConfigMap() {
        return dateAndChannelAreaConfigMap;
    }

    public void setDateAndChannelAreaConfigMap(HashMap<String, List<ChannelProgramBean>>  dateAndChannelAreaConfigMap) {
        this.dateAndChannelAreaConfigMap = dateAndChannelAreaConfigMap;
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

    public static class ChannelProgramBean {
        /**
         * id : 100030
         * areaTitle : kaishuitest
         * status : LIVE
         * objectType : BASKETBALL
         * objectId : 4000332014487107890980016008
         * gmtStart : 2018-02-06 16:48:09
         * masterAnchorId : 100160
         * masterAnchorUserId : 8201711018675831
         * masterAnchorName : 大鱼1
         * auxiliaryAnchorId : 100145
         * auxiliaryUserId : 8201703290460012
         * auditStatus : PASS
         * raceDataClientSimple : {"id":"4000332014487107890980016008","matchName":"NBA","guestTeamName":"独行侠","homeTeamName":"太阳","gmtStart":"2018-02-01 11:30:00","raceStatus":"FINISH"}
         * userOrderChannelArea : false
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
        private RaceDataClientSimpleBean raceDataClientSimple;
        private boolean userOrderChannelArea;
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

        public List<HeadPortraitBean> getChannelAreaGuestList() {
            return channelAreaGuestList;
        }

        public void setChannelAreaGuestList(List<HeadPortraitBean> channelAreaGuestList) {
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

        public boolean isUserOrderChannelArea() {
            return userOrderChannelArea;
        }

        public void setUserOrderChannelArea(boolean userOrderChannelArea) {
            this.userOrderChannelArea = userOrderChannelArea;
        }

        public static class RaceDataClientSimpleBean {
            /**
             * id : 4000332014487107890980016008
             * matchName : NBA
             * guestTeamName : 独行侠
             * homeTeamName : 太阳
             * gmtStart : 2018-02-01 11:30:00
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

}
