package com.android.xjq.bean;

/**
 * Created by ajiao on 2017/11/7 0007.
 */

public class MyGameChildBean {

    private String changci;
    private boolean isShowZhuWei;
    private double hostScore;
    private double gusetScore;
    private String parentHostName;
    private String parentGuestName;
    private double plate;

    //助威相关
    private String playType;
    private String raceId;
    private String raceType;
    private String boardId;

    private double hostPriceFee;
    private double guestPriceFee;

    private boolean isHostSelected;
    private boolean isGuestSelected;
    private String optionGroup;
    private GameChildBean.GameBoardListBean.GameBoardOptionEntry giftHomeEntry;
    private GameChildBean.GameBoardListBean.GameBoardOptionEntry giftGuestEntry;
    private int homeRate = 1;
    private int guestRate = 1;
    private String matchName;
    private String gmtStart;

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(String gmtStart) {
        this.gmtStart = gmtStart;
    }

    public GameChildBean.GameBoardListBean.GameBoardOptionEntry getGiftHomeEntry() {
        return giftHomeEntry;
    }

    public void setGiftHomeEntry(GameChildBean.GameBoardListBean.GameBoardOptionEntry giftHomeEntry) {
        this.giftHomeEntry = giftHomeEntry;
    }

    public GameChildBean.GameBoardListBean.GameBoardOptionEntry getGiftGuestEntry() {
        return giftGuestEntry;
    }

    public void setGiftGuestEntry(GameChildBean.GameBoardListBean.GameBoardOptionEntry                                                                                                     giftGuestEntry) {
        this.giftGuestEntry = giftGuestEntry;
    }

    public int getHomeRate() {
        return homeRate;
    }

    public void setHomeRate(int homeRate) {
        this.homeRate = homeRate;
    }

    public int getGuestRate() {
        return guestRate;
    }

    public void setGuestRate(int guestRate) {
        this.guestRate = guestRate;
    }

    public String getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(String optionGroup) {
        this.optionGroup = optionGroup;
    }

    public double getPlate() {
        return plate;
    }

    public void setPlate(double plate) {
        this.plate = plate;
    }
    public String getParentHostName() {
        return parentHostName;
    }

    public void setParentHostName(String parentHostName) {
        this.parentHostName = parentHostName;
    }

    public String getParentGuestName() {
        return parentGuestName;
    }

    public void setParentGuestName(String parentGuestName) {
        this.parentGuestName = parentGuestName;
    }

    public boolean isHostSelected() {
        return isHostSelected;
    }

    public void setHostSelected(boolean hostSelected) {
        isHostSelected = hostSelected;
    }

    public boolean isGuestSelected() {
        return isGuestSelected;
    }

    public void setGuestSelected(boolean guestSelected) {
        isGuestSelected = guestSelected;
    }

    public double getHostPriceFee() {
        return hostPriceFee;
    }

    public void setHostPriceFee(double hostPriceFee) {
        this.hostPriceFee = hostPriceFee;
    }

    public double getGuestPriceFee() {
        return guestPriceFee;
    }

    public void setGuestPriceFee(double guestPriceFee) {
        this.guestPriceFee = guestPriceFee;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceType() {
        return raceType;
    }

    public void setRaceType(String raceType) {
        this.raceType = raceType;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getChangci() {
        return changci;
    }

    public void setChangci(String changci) {
        this.changci = changci;
    }

    public boolean isShowZhuWei() {
        return isShowZhuWei;
    }

    public void setShowZhuWei(boolean showZhuWei) {
        isShowZhuWei = showZhuWei;
    }

    public double getHostScore() {
        return hostScore;
    }

    public void setHostScore(double hostScore) {
        this.hostScore = hostScore;
    }

    public double getGusetScore() {
        return gusetScore;
    }

    public void setGusetScore(double gusetScore) {
        this.gusetScore = gusetScore;
    }
}
