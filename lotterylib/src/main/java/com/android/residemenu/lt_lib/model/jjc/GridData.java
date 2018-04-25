package com.android.residemenu.lt_lib.model.jjc;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author leslie
 * @version $Id: GridData.java, v 0.1 2014年5月6日 下午2:47:49 leslie Exp $
 */
public class GridData implements Parcelable {

    /**
     * 索引
     */
    private int index;

    /**
     * 是否选中
     */
    private boolean checked;
    /**
     * 标题
     */
    private String title;
    /**
     * 赔率
     */
    private String odds;

    /**
     * 选项
     */
    private String option;

    /**
     * 是否可点击
     */
    private boolean enabled = true;


    private   String selecterEnum;

    /**
     * 玩法
     */
    private String playType;

    /**
     * 注数
     */
    private int zhuShu;

    /**
     * 奖金：注数*sp
     */
    private int bonus;

    private long id;

    private String homeTeamName;

    private String guestTeamName;

    private int concede;

    private String bonusOptimizationOption;


    public GridData() {

    }

    public GridData(Parcel p) {
        index = p.readInt();
        checked = p.readByte() != 0;
        title = p.readString();
        odds = p.readString();
        option = p.readString();
        enabled = p.readByte() != 0;
        playType = p.readString();
        zhuShu = p.readInt();
        id = p.readLong();
        bonusOptimizationOption = p.readString();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(index);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeString(title);
        dest.writeString(odds);
        dest.writeString(option);
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeString(playType);
        dest.writeInt(zhuShu);
        dest.writeLong(id);
        dest.writeString(bonusOptimizationOption);

    }

    public static final Creator<GridData> CREATOR = new Creator<GridData>() {
        public GridData createFromParcel(Parcel p) {
            return new GridData(p);
        }

        public GridData[] newArray(int size) {
            return new GridData[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSelecterEnum() {
        return selecterEnum;
    }

    public void setSelecterEnum(String selecterEnum) {
        this.selecterEnum = selecterEnum;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public int getZhuShu() {
        return zhuShu;
    }

    public void setZhuShu(int zhuShu) {
        this.zhuShu = zhuShu;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public long getId() {
        return id;
    }

    public void setId(long innerRaceId) {
        this.id = innerRaceId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public int getConcede() {
        return concede;
    }

    public void setConcede(int concede) {
        this.concede = concede;
    }

    public String getBonusOptimizationOption() {
        return bonusOptimizationOption;
    }

    public void setBonusOptimizationOption(String bonusOptimizationOption) {
        this.bonusOptimizationOption = bonusOptimizationOption;
    }
}
