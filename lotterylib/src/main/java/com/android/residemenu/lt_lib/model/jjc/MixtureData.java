package com.android.residemenu.lt_lib.model.jjc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author leslie
 * @version $Id: MixtureData.java, v 0.1 2014年4月28日 上午11:52:16 leslie Exp $
 */
public class MixtureData extends RaceData implements Parcelable, Cloneable {

    /**
     * 比赛数据id
     */
    private String id;
    /**
     * 足彩每期销售截止日期
     */
    protected String gmtEnd;

    /**
     * 每个格子数据
     */
    private List<GridData> gridList = new ArrayList<GridData>();

    /**
     * 让球数
     */
    private int concede;

    /**
     * 让分
     */
    private double letScore;
    /**
     * 竞蓝总分
     */
    private double preSetScore;

    /**
     * 是否是胆
     */
    private boolean dan;

    /**
     * 玩法，如果是单一，设置
     */
    private String playType;

    /**
     * 至少有一场比赛选中
     */
    private boolean leastChecked;

    /**
     * 选择了投注项后的标题
     */
    private String title;
    /**
     * 要展开选择按钮的标题 (最初的,后期不要改变)
     */
    private String btnTip;
    /**
     * 是否能点展开按钮
     */
    private boolean enabled = true;

    /**
     * 是否显示析
     */
    private boolean showMiniInfo = false;


    /**
     * 固定单关是否支持胜平负
     */
    private boolean gddgIsSupportSpf = false;

    /**
     * 固定单关是否支持让球胜平负
     */
    private boolean gddgIsSupportRqspf = false;

    /**
     * 设置混合过关中是否有支持固定单关的
     */
    private boolean isRqspfSupportGddg = false;

    private boolean isSpfSupportGddg = false;

    private boolean isBqcSupportGddg = false;

    private boolean isJqsSupportGddg = false;

    private boolean isBfSupportGddg = false;

    //是否显示红色框背景
    private boolean isShowRedBg = false;

    private int zhushu;

    /**
     * 奖金优化，计划购买的钱
     */
    private int planBuyPrice[];

    /**
     * 每场比赛所选的注数
     */
    private int gridCheckedAmount;

    private double spSum;

    //以下是篮球是否支持这几种单关(胜负单关，大小单关，胜分差单关，让分单关)
    private boolean sfDg;

    private boolean dxDg;

    private boolean sfcDg;

    private boolean rfDg;

    //是否已经奖金优化
    private boolean isBonusOptimization = true;

    private long objectId;

    private boolean innerTeamReverse;

    private transient boolean showJclqSmallAnalysis;

    private String homeTeamRanking;

    private String guestTeamRanking;

    private boolean showRanking;


    public MixtureData() {

    }


    protected MixtureData(Parcel in) {
        id = in.readString();
        gmtEnd = in.readString();
        gridList = in.createTypedArrayList(GridData.CREATOR);
        concede = in.readInt();
        letScore = in.readDouble();
        preSetScore = in.readDouble();
        dan = in.readByte() != 0;
        playType = in.readString();
        leastChecked = in.readByte() != 0;
        title = in.readString();
        btnTip = in.readString();
        enabled = in.readByte() != 0;
        showMiniInfo = in.readByte() != 0;
        gddgIsSupportSpf = in.readByte() != 0;
        gddgIsSupportRqspf = in.readByte() != 0;
        isRqspfSupportGddg = in.readByte() != 0;
        isSpfSupportGddg = in.readByte() != 0;
        isBqcSupportGddg = in.readByte() != 0;
        isJqsSupportGddg = in.readByte() != 0;
        isBfSupportGddg = in.readByte() != 0;
        isShowRedBg = in.readByte() != 0;
        zhushu = in.readInt();
        planBuyPrice = in.createIntArray();
        gridCheckedAmount = in.readInt();
        spSum = in.readDouble();
        sfDg = in.readByte() != 0;
        dxDg = in.readByte() != 0;
        sfcDg = in.readByte() != 0;
        rfDg = in.readByte() != 0;
        isBonusOptimization = in.readByte() != 0;
        objectId = in.readLong();
        innerTeamReverse = in.readByte() != 0;
        homeTeamRanking = in.readString();
        guestTeamRanking = in.readString();
        showRanking = in.readByte() != 0;
    }

    public static final Creator<MixtureData> CREATOR = new Creator<MixtureData>() {
        @Override
        public MixtureData createFromParcel(Parcel in) {
            return new MixtureData(in);
        }

        @Override
        public MixtureData[] newArray(int size) {
            return new MixtureData[size];
        }
    };

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getHomeTeamRanking() {
        return homeTeamRanking;
    }

    public void setHomeTeamRanking(String homeTeamRanking) {
        this.homeTeamRanking = homeTeamRanking;
    }

    public String getGuestTeamRanking() {
        return guestTeamRanking;
    }

    public void setGuestTeamRanking(String guestTeamRanking) {
        this.guestTeamRanking = guestTeamRanking;
    }

    public boolean isShowRanking() {
        return showRanking;
    }

    public void setShowRanking(boolean showRanking) {
        this.showRanking = showRanking;
    }

    public boolean isInnerTeamReverse() {
        return innerTeamReverse;
    }

    public void setInnerTeamReverse(boolean innerTeamReverse) {
        this.innerTeamReverse = innerTeamReverse;
    }

    public boolean isShowRedBg() {
        return isShowRedBg;
    }

    public void setShowRedBg(boolean isShowRedBg) {
        this.isShowRedBg = isShowRedBg;
    }

    public boolean isJqsSupportGddg() {
        return isJqsSupportGddg;
    }

    public void setJqsSupportGddg(boolean isJqsSupportGddg) {
        this.isJqsSupportGddg = isJqsSupportGddg;
    }

    public boolean isBqcSupportGddg() {
        return isBqcSupportGddg;
    }

    public void setBqcSupportGddg(boolean isBqcSupportGddg) {
        this.isBqcSupportGddg = isBqcSupportGddg;
    }

    public boolean isSpfSupportGddg() {
        return isSpfSupportGddg;
    }

    public void setSpfSupportGddg(boolean isSpfSupportGddg) {
        this.isSpfSupportGddg = isSpfSupportGddg;
    }

    public boolean isRqspfSupportGddg() {
        return isRqspfSupportGddg;
    }

    public void setRqspfSupportGddg(boolean isRqspfSupportGddg) {
        this.isRqspfSupportGddg = isRqspfSupportGddg;
    }

    public boolean isBfSupportGddg() {
        return isBfSupportGddg;
    }

    public void setBfSupportGddg(boolean isBfSupportGddg) {
        this.isBfSupportGddg = isBfSupportGddg;
    }

    public boolean isGddgIsSupportRqspf() {
        return gddgIsSupportRqspf;
    }

    public void setGddgIsSupportRqspf(boolean gddgIsSupportRqspf) {
        this.gddgIsSupportRqspf = gddgIsSupportRqspf;
    }

    public boolean isGddgIsSupportSpf() {
        return gddgIsSupportSpf;
    }

    public boolean isGddgSupportSpf() {
        return gddgIsSupportSpf;
    }

    public void setGddgIsSupportSpf(boolean gddgIsSupportSpf) {
        this.gddgIsSupportSpf = gddgIsSupportSpf;
    }

    public boolean isShowMiniInfo() {
        return showMiniInfo;
    }

    public void setShowMiniInfo(boolean showMiniInfo) {
        this.showMiniInfo = showMiniInfo;
    }

  /*  public String getbizId(){
        return bizId;
    }

    public void setbizId(String bizId){
       this.bizId = bizId;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getConcede() {
        return concede;
    }

    public void setConcede(int concede) {
        this.concede = concede;
    }

    public List<GridData> getGridList() {
        return gridList;
    }

    public void setGridList(List<GridData> gridList) {
        this.gridList = gridList;
    }

    public boolean isDan() {
        return dan;
    }

    public void setDan(boolean dan) {
        this.dan = dan;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public boolean isLeastChecked() {
        return leastChecked;
    }

    public void setLeastChecked(boolean leastChecked) {
        this.leastChecked = leastChecked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBtnTip() {
        return btnTip;
    }

    public void setBtnTip(String btnTip) {
        this.btnTip = btnTip;
    }

    public double getLetScore() {
        return letScore;
    }

    public void setLetScore(double letScore) {
        this.letScore = letScore;
    }

    public double getPreSetScore() {
        return preSetScore;
    }

    public void setPreSetScore(double preSetScore) {
        this.preSetScore = preSetScore;
    }

    public String getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(String gmtEnd) {
        this.gmtEnd = gmtEnd;
    }

    public int getZhushu() {
        return zhushu;
    }

    public void setZhushu(int zhushu) {
        this.zhushu = zhushu;
    }

    public int[] getPlanBuyPrice() {
        return planBuyPrice;
    }

    public void setPlanBuyPrice(int[] planBuyPrice) {
        this.planBuyPrice = planBuyPrice;
    }

    public int getGridCheckedAmount() {
        return gridCheckedAmount;
    }

    public void setGridCheckedAmount(int gridCheckedAmount) {
        this.gridCheckedAmount = gridCheckedAmount;
    }

    public double getSpSum() {
        return spSum;
    }

    public void setSpSum(double spSum) {
        this.spSum = spSum;
    }

    public boolean isSfDg() {
        return sfDg;
    }

    public void setSfDg(boolean sfDg) {
        this.sfDg = sfDg;
    }

    public boolean isRfDg() {
        return rfDg;
    }

    public void setRfDg(boolean rfDg) {
        this.rfDg = rfDg;
    }

    public boolean isSfcDg() {
        return sfcDg;
    }

    public void setSfcDg(boolean sfcDg) {
        this.sfcDg = sfcDg;
    }

    public boolean isDxDg() {
        return dxDg;
    }

    public void setDxDg(boolean dxDg) {
        this.dxDg = dxDg;
    }

    public boolean isBonusOptimization() {
        return isBonusOptimization;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public void setBonusOptimization(boolean isBonusOptimization) {
        this.isBonusOptimization = isBonusOptimization;
    }

    public boolean isShowJclqSmallAnalysis() {
        return showJclqSmallAnalysis;
    }

    public void setShowJclqSmallAnalysis(boolean showJclqSmallAnalysis) {
        this.showJclqSmallAnalysis = showJclqSmallAnalysis;
    }

    @Override
    public String toString() {
        return "MixtureData{" +
                "id='" + id + '\'' +
                ", gmtEnd='" + gmtEnd + '\'' +
                ", gridList=" + gridList +
                ", concede=" + concede +
                ", letScore=" + letScore +
                ", preSetScore=" + preSetScore +
                ", dan=" + dan +
                ", playType='" + playType + '\'' +
                ", leastChecked=" + leastChecked +
                ", title='" + title + '\'' +
                ", btnTip='" + btnTip + '\'' +
                ", enabled=" + enabled +
                ", showMiniInfo=" + showMiniInfo +
                ", gddgIsSupportSpf=" + gddgIsSupportSpf +
                ", gddgIsSupportRqspf=" + gddgIsSupportRqspf +
                ", isRqspfSupportGddg=" + isRqspfSupportGddg +
                ", isSpfSupportGddg=" + isSpfSupportGddg +
                ", isBqcSupportGddg=" + isBqcSupportGddg +
                ", isJqsSupportGddg=" + isJqsSupportGddg +
                ", isBfSupportGddg=" + isBfSupportGddg +
                ", isShowRedBg=" + isShowRedBg +
                ", zhushu=" + zhushu +
                ", planBuyPrice=" + Arrays.toString(planBuyPrice) +
                ", gridCheckedAmount=" + gridCheckedAmount +
                ", spSum=" + spSum +
                ", sfDg=" + sfDg +
                ", dxDg=" + dxDg +
                ", sfcDg=" + sfcDg +
                ", rfDg=" + rfDg +
                ", isBonusOptimization=" + isBonusOptimization +
                ", objectId=" + objectId +
                ", innerTeamReverse=" + innerTeamReverse +
                ", showJclqSmallAnalysis=" + showJclqSmallAnalysis +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(gmtEnd);
        dest.writeTypedList(gridList);
        dest.writeInt(concede);
        dest.writeDouble(letScore);
        dest.writeDouble(preSetScore);
        dest.writeByte((byte) (dan ? 1 : 0));
        dest.writeString(playType);
        dest.writeByte((byte) (leastChecked ? 1 : 0));
        dest.writeString(title);
        dest.writeString(btnTip);
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeByte((byte) (showMiniInfo ? 1 : 0));
        dest.writeByte((byte) (gddgIsSupportSpf ? 1 : 0));
        dest.writeByte((byte) (gddgIsSupportRqspf ? 1 : 0));
        dest.writeByte((byte) (isRqspfSupportGddg ? 1 : 0));
        dest.writeByte((byte) (isSpfSupportGddg ? 1 : 0));
        dest.writeByte((byte) (isBqcSupportGddg ? 1 : 0));
        dest.writeByte((byte) (isJqsSupportGddg ? 1 : 0));
        dest.writeByte((byte) (isBfSupportGddg ? 1 : 0));
        dest.writeByte((byte) (isShowRedBg ? 1 : 0));
        dest.writeInt(zhushu);
        dest.writeIntArray(planBuyPrice);
        dest.writeInt(gridCheckedAmount);
        dest.writeDouble(spSum);
        dest.writeByte((byte) (sfDg ? 1 : 0));
        dest.writeByte((byte) (dxDg ? 1 : 0));
        dest.writeByte((byte) (sfcDg ? 1 : 0));
        dest.writeByte((byte) (rfDg ? 1 : 0));
        dest.writeByte((byte) (isBonusOptimization ? 1 : 0));
        dest.writeLong(objectId);
        dest.writeByte((byte) (innerTeamReverse ? 1 : 0));
        dest.writeString(homeTeamRanking);
        dest.writeString(guestTeamRanking);
        dest.writeByte((byte) (showRanking ? 1 : 0));
    }
}
