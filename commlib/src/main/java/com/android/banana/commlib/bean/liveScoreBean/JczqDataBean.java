package com.android.banana.commlib.bean.liveScoreBean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.google.gson.annotations.Expose;

/**
 * Created by qiaomu on 2017/12/4.
 */

public class JczqDataBean implements Parcelable {


    /**
     * id : 2915560971907890980017505892
     * innerRaceId : 25637059
     * innerGuestTeamId : 2295600
     * innerHomeTeamId : 9106326
     * innerMatchId : 0
     * guestTeamName : 北曼谷学院
     * homeTeamName : 曼谷吞武里大学
     * joinBet : true
     * halfHomeScore : 1
     * halfGuestScore : 0
     * fullGuestScore : 1
     * fullHomeScore : 1
     * resultConfirm : false
     * enabled : true
     * gmtStart : 2017-12-04 16:30:00
     * gmtCreate : 2017-12-04 10:46:49
     * gmtModified : 2017-12-04 18:39:03
     * color : #FF0000
     * raceStatus : {"name":"FINISH","message":"完场"}
     * hasGameBoard : true
     */

    //赛事类型 FOOTBALL/BASKETBALL
    public NormalObject raceType;

    public String id;
    public String innerRaceId;
    public int innerGuestTeamId;
    public int innerHomeTeamId;
    public int innerMatchId;
    public String guestTeamName;
    public String homeTeamName;
    public boolean joinBet;
    public int halfHomeScore;
    public int halfGuestScore;
    public int fullGuestScore;
    public int fullHomeScore;
    public boolean resultConfirm;
    public boolean enabled;
    public String gmtStart;
    public String gmtCreate;
    public String gmtModified;
    public String color;
    public NormalObject raceStatus;
    public boolean hasGameBoard;
    public String matchName;

    /*------------------下面是篮球赛事用得到的--------------------------*/
    /**
     * sectionCount : 0
     * homeScoreSection1 : 0
     * guestScoreSection1 : 0
     * homeScoreSection2 : 0
     * guestScoreSection2 : 0
     * homeScoreSection3 : 0
     * guestScoreSection3 : 0
     * homeScoreSection4 : 0
     * guestScoreSection4 : 0
     */

    public int sectionCount;
    public int homeScoreSection1;
    public int guestScoreSection1;
    public int homeScoreSection2;
    public int guestScoreSection2;
    public int homeScoreSection3;
    public int guestScoreSection3;
    public int homeScoreSection4;
    public int guestScoreSection4;

    //是否反转
    public boolean innerTeamReverse;
    //开赛时间
    public String startDate;

    @Expose
    private String guestLogoUrl;
    @Expose
    private String homeLogoUrl;
    @Expose
    private String nowDate;


    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isFootball() {
        return raceType != null && "FOOTBALL".equals(raceType.getName());
    }

    public String getId() {
        return id;
    }


    public int getInnerGuestTeamId() {
        return innerGuestTeamId;
    }

    public int getInnerHomeTeamId() {
        return innerHomeTeamId;
    }

    public int getInnerMatchId() {
        return innerMatchId;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public boolean isJoinBet() {
        return joinBet;
    }

    public int getHalfHomeScore() {
        return halfHomeScore;
    }

    public String getInnerRaceId() {
        return innerRaceId;
    }

    public void setInnerRaceId(String innerRaceId) {
        this.innerRaceId = innerRaceId;
    }

    public int getHalfGuestScore() {
        return halfGuestScore;
    }

    public int getFullGuestScore() {
        return fullGuestScore;
    }

    public int getFullHomeScore() {
        return fullHomeScore;
    }

    public boolean isResultConfirm() {
        return resultConfirm;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getGmtStart() {
        return gmtStart;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public String getColor() {
        return color;
    }

    public NormalObject getRaceStatus() {
        return raceStatus;
    }

    public boolean isHasGameBoard() {
        return hasGameBoard;
    }

    @Deprecated
    public String getGameNo() {
        return null;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public int getHomeScoreSection1() {
        return homeScoreSection1;
    }

    public int getGuestScoreSection1() {
        return guestScoreSection1;
    }

    public int getHomeScoreSection2() {
        return homeScoreSection2;
    }

    public int getGuestScoreSection2() {
        return guestScoreSection2;
    }

    public int getHomeScoreSection3() {
        return homeScoreSection3;
    }

    public int getGuestScoreSection3() {
        return guestScoreSection3;
    }

    public int getHomeScoreSection4() {
        return homeScoreSection4;
    }

    public int getGuestScoreSection4() {
        return guestScoreSection4;
    }

    public String getFTGuestLogoUrl() {
        if (!TextUtils.isEmpty(guestLogoUrl))
            return guestLogoUrl;
        guestLogoUrl = TeamImageUrlUtils.getFTGuestLogoUrl(innerGuestTeamId);
        return guestLogoUrl;
    }


    public String getFTHomeLogoUrl() {
        if (!TextUtils.isEmpty(homeLogoUrl))
            return homeLogoUrl;
        homeLogoUrl = TeamImageUrlUtils.getFTHomeLogoUrl(innerHomeTeamId);
        return homeLogoUrl;
    }

    public String getBTGuestLogoUrl() {
        if (!TextUtils.isEmpty(guestLogoUrl))
            return guestLogoUrl;
        guestLogoUrl = TeamImageUrlUtils.getBTGuestLogoUrl(innerGuestTeamId);
        return guestLogoUrl;
    }


    public String getBTHomeLogoUrl() {
        if (!TextUtils.isEmpty(homeLogoUrl))
            return homeLogoUrl;
        homeLogoUrl = TeamImageUrlUtils.getBTHomeLogoUrl(innerHomeTeamId);
        return homeLogoUrl;
    }


    public String getMatchName() {
        return matchName;
    }


    public String getHasStartTime(JczqDataBean jcData, String nowDate, boolean isLqRace) {
        long betweenTime = TimeUtils.caculateMinsDiff(nowDate, jcData.getGmtStart());
        if (isLqRace) {
            return betweenTime + "";
        }
        if (FtRaceStatusEnum.saveValueOf(jcData.getRaceStatus().getName()) == FtRaceStatusEnum.PLAY_S) {
            if (betweenTime + 45 > 90) {
                return "90+";
            } else {
                return String.valueOf(betweenTime + 45);
            }
        } else if (FtRaceStatusEnum.saveValueOf(jcData.getRaceStatus().getName()) == FtRaceStatusEnum.PLAY_F) {
            if (betweenTime > 45) {
                return "45+";
            } else {
                return String.valueOf(betweenTime);
            }
        }
        //这边为了解决一个服务端的一个问题：下半场开始的前两分钟内可能拿不到下半场比赛开始时间。这是就显示46'
        else if (FtRaceStatusEnum.saveValueOf(jcData.getRaceStatus().getName()) == FtRaceStatusEnum.PLAY_S) {
            return 46 + "";
        }
        return String.valueOf(betweenTime);
    }

    @Expose
    private String formatRaceTime;//获取赛事格式化时间字符串


    @Expose
    private boolean isSelected;//赛事选择列表中该条对象是否有被选择

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean mIsBasketballRace;

    public void setIsBasketballRace(boolean isBasketballRace) {
        mIsBasketballRace = isBasketballRace;
    }

    public boolean isBasketballRace() {
        return mIsBasketballRace;
    }

    public JczqDataBean() {
    }

    @Override
    public boolean equals(Object dataBean) {
        if (dataBean instanceof JczqDataBean) {
            return TextUtils.equals(id, ((JczqDataBean) dataBean).id);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.raceType, flags);
        dest.writeString(this.id);
        dest.writeString(this.innerRaceId);
        dest.writeInt(this.innerGuestTeamId);
        dest.writeInt(this.innerHomeTeamId);
        dest.writeInt(this.innerMatchId);
        dest.writeString(this.guestTeamName);
        dest.writeString(this.homeTeamName);
        dest.writeByte(this.joinBet ? (byte) 1 : (byte) 0);
        dest.writeInt(this.halfHomeScore);
        dest.writeInt(this.halfGuestScore);
        dest.writeInt(this.fullGuestScore);
        dest.writeInt(this.fullHomeScore);
        dest.writeByte(this.resultConfirm ? (byte) 1 : (byte) 0);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.gmtStart);
        dest.writeString(this.gmtCreate);
        dest.writeString(this.gmtModified);
        dest.writeString(this.color);
        dest.writeParcelable(this.raceStatus, flags);
        dest.writeByte(this.hasGameBoard ? (byte) 1 : (byte) 0);
        dest.writeString(this.matchName);
        dest.writeInt(this.sectionCount);
        dest.writeInt(this.homeScoreSection1);
        dest.writeInt(this.guestScoreSection1);
        dest.writeInt(this.homeScoreSection2);
        dest.writeInt(this.guestScoreSection2);
        dest.writeInt(this.homeScoreSection3);
        dest.writeInt(this.guestScoreSection3);
        dest.writeInt(this.homeScoreSection4);
        dest.writeInt(this.guestScoreSection4);
        dest.writeByte(this.innerTeamReverse ? (byte) 1 : (byte) 0);
        dest.writeString(this.startDate);
        dest.writeString(this.guestLogoUrl);
        dest.writeString(this.homeLogoUrl);
        dest.writeString(this.nowDate);
        dest.writeString(this.formatRaceTime);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsBasketballRace ? (byte) 1 : (byte) 0);
    }

    protected JczqDataBean(Parcel in) {
        this.raceType = in.readParcelable(NormalObject.class.getClassLoader());
        this.id = in.readString();
        this.innerRaceId = in.readString();
        this.innerGuestTeamId = in.readInt();
        this.innerHomeTeamId = in.readInt();
        this.innerMatchId = in.readInt();
        this.guestTeamName = in.readString();
        this.homeTeamName = in.readString();
        this.joinBet = in.readByte() != 0;
        this.halfHomeScore = in.readInt();
        this.halfGuestScore = in.readInt();
        this.fullGuestScore = in.readInt();
        this.fullHomeScore = in.readInt();
        this.resultConfirm = in.readByte() != 0;
        this.enabled = in.readByte() != 0;
        this.gmtStart = in.readString();
        this.gmtCreate = in.readString();
        this.gmtModified = in.readString();
        this.color = in.readString();
        this.raceStatus = in.readParcelable(NormalObject.class.getClassLoader());
        this.hasGameBoard = in.readByte() != 0;
        this.matchName = in.readString();
        this.sectionCount = in.readInt();
        this.homeScoreSection1 = in.readInt();
        this.guestScoreSection1 = in.readInt();
        this.homeScoreSection2 = in.readInt();
        this.guestScoreSection2 = in.readInt();
        this.homeScoreSection3 = in.readInt();
        this.guestScoreSection3 = in.readInt();
        this.homeScoreSection4 = in.readInt();
        this.guestScoreSection4 = in.readInt();
        this.innerTeamReverse = in.readByte() != 0;
        this.startDate = in.readString();
        this.guestLogoUrl = in.readString();
        this.homeLogoUrl = in.readString();
        this.nowDate = in.readString();
        this.formatRaceTime = in.readString();
        this.isSelected = in.readByte() != 0;
        this.mIsBasketballRace = in.readByte() != 0;
    }

    public static final Creator<JczqDataBean> CREATOR = new Creator<JczqDataBean>() {
        @Override
        public JczqDataBean createFromParcel(Parcel source) {
            return new JczqDataBean(source);
        }

        @Override
        public JczqDataBean[] newArray(int size) {
            return new JczqDataBean[size];
        }
    };
}
