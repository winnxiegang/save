package com.android.xjq.bean;

import com.android.xjq.bean.myzhuwei.FootballRacesBean;

/**
 * Created by ajiao on 2017/11/6 0006.
 */

public class GameParentBean {
    private String changci;
    private String hostName;
    private String guestName;
    private String gmtTime;
    private String matchName;
    private double hostHotScore;
    private double guestHotScore;
    private boolean hasChild;
    private boolean isExpanded;
    private String id;
    private boolean isShowZhuWei;
    private String raceStatus;
    private double hostPriceFee;
    private double guestPriceFee;
    //助威相关
    private String playType;
    private String raceId;
    private String raceType;
    private String boardId;
    private double plate;
    private String innerMatchId;
    private boolean isHostSelected;
    private boolean isGuestSelected;
    private boolean existedDefaultGameBoard;
    private boolean gameBoardAllPrized;
    private FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean.GameBoardOptionEntry giftHomeEntry;
    private FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean.GameBoardOptionEntry giftGuestEntry;
    private int homeRate = 1;
    private int guestRate = 1;

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

    public FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean.GameBoardOptionEntry getGiftHomeEntry() {
        return giftHomeEntry;
    }

    public void setGiftHomeEntry(FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean.GameBoardOptionEntry giftHomeEntry) {
        this.giftHomeEntry = giftHomeEntry;
    }

    public FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean.GameBoardOptionEntry getGiftGuestEntry() {
        return giftGuestEntry;
    }

    public void setGiftGuestEntry(FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean.GameBoardOptionEntry giftGuestEntry) {
        this.giftGuestEntry = giftGuestEntry;
    }

    public boolean isGameBoardAllPrized() {
        return gameBoardAllPrized;
    }

    public void setGameBoardAllPrized(boolean gameBoardAllPrized) {
        this.gameBoardAllPrized = gameBoardAllPrized;
    }

    public boolean isExistedDefaultGameBoard() {
        return existedDefaultGameBoard;
    }

    public void setExistedDefaultGameBoard(boolean existedDefaultGameBoard) {
        this.existedDefaultGameBoard = existedDefaultGameBoard;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
    public double getPlate() {
        return plate;
    }

    public void setPlate(double plate) {
        this.plate = plate;
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

    public String getRaceStatus() {
        return raceStatus;
    }

    public void setRaceStatus(String raceStatus) {
        this.raceStatus = raceStatus;
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

    public String getInnerMatchId() {
        return innerMatchId;
    }

    public void setInnerMatchId(String innerMatchId) {
        this.innerMatchId = innerMatchId;
    }

    public boolean isShowZhuWei() {
        return isShowZhuWei;
    }

    public void setShowZhuWei(boolean showZhuWei) {
        isShowZhuWei = showZhuWei;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getChangci() {
        return changci;
    }

    public void setChangci(String changci) {
        this.changci = changci;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGmtTime() {
        return gmtTime;
    }

    public void setGmtTime(String gmtTime) {
        this.gmtTime = gmtTime;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public double getHostHotScore() {
        return hostHotScore;
    }

    public void setHostHotScore(double hostHotScore) {
        this.hostHotScore = hostHotScore;
    }

    public double getGuestHotScore() {
        return guestHotScore;
    }

    public void setGuestHotScore(double guestHotScore) {
        this.guestHotScore = guestHotScore;
    }

}
