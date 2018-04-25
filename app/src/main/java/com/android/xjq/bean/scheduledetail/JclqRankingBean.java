package com.android.xjq.bean.scheduledetail;

import com.android.residemenu.lt_lib.utils.lang.MathUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhouyi on 2016/7/14 14:52.
 */
public class JclqRankingBean {

    @SerializedName("guestMatch")
    private String guestMatch;

    @SerializedName("homeSeasonText")
    private String homeSeasonText;

    @SerializedName("guestSeasonText")
    private String guestSeasonText;

    @SerializedName("guestDiv")
    private String guestDiv;

    @SerializedName("homeMatch")
    private String homeMatch;

    @SerializedName("homeDiv")
    private String homeDiv;

    @SerializedName("guestRankList")
    private List<JclqRankingInfoBean> guestRankList;

    @SerializedName("homeRankList")
    private List<JclqRankingInfoBean> homeRankList;

    @SerializedName("allRankList")
    private List<JclqRankingInfoBean> allRankList;

    private boolean cup;

    /**
     * 主队标题名
     */
    @Expose
    private String homeTitle;

    /**
     * 客队标题名
     */
    @Expose
    private String guestTitle;

    public String getGuestMatch() {
        return guestMatch;
    }

    public void setGuestMatch(String guestMatch) {
        this.guestMatch = guestMatch;
    }

    public String getHomeSeasonText() {
        return homeSeasonText;
    }

    public void setHomeSeasonText(String homeSeasonText) {
        this.homeSeasonText = homeSeasonText;
    }

    public String getGuestSeasonText() {
        return guestSeasonText;
    }

    public void setGuestSeasonText(String guestSeasonText) {
        this.guestSeasonText = guestSeasonText;
    }

    public String getGuestDiv() {
        return guestDiv;
    }

    public void setGuestDiv(String guestDiv) {
        this.guestDiv = guestDiv;
    }

    public String getHomeMatch() {
        return homeMatch;
    }

    public void setHomeMatch(String homeMatch) {
        this.homeMatch = homeMatch;
    }

    public String getHomeDiv() {
        return homeDiv;
    }

    public void setHomeDiv(String homeDiv) {
        this.homeDiv = homeDiv;
    }

    public List<JclqRankingInfoBean> getGuestRankList() {
        return guestRankList;
    }

    public void setGuestRankList(List<JclqRankingInfoBean> guestRankList) {
        this.guestRankList = guestRankList;
    }

    public List<JclqRankingInfoBean> getHomeRankList() {
        return homeRankList;
    }

    public void setHomeRankList(List<JclqRankingInfoBean> homeRankList) {
        this.homeRankList = homeRankList;
    }

    public List<JclqRankingInfoBean> getAllRankList() {
        return allRankList;
    }

    public void setAllRankList(List<JclqRankingInfoBean> allRankList) {
        this.allRankList = allRankList;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getGuestTitle() {
        return guestTitle;
    }

    public void setGuestTitle(String guestTitle) {
        this.guestTitle = guestTitle;
    }

    public boolean isCup() {
        return cup;
    }

    public void setCup(boolean cup) {
        this.cup = cup;
    }

    public void operateData(int homeId,int guestId) {


        if(homeSeasonText!=null){
            homeTitle = homeSeasonText + "赛季" + homeMatch + homeDiv + "排名";
        }else{
            homeTitle="赛季排名";
        }

        if(guestSeasonText!=null){
            guestTitle = guestSeasonText + "赛季" + guestMatch + guestDiv + "排名";
        }else{
            guestTitle="赛季排名";
        }

        calculateData(guestRankList, homeId, guestId);

        calculateData(homeRankList, homeId, guestId);

        calculateData(allRankList, homeId, guestId);

//        if(isInnerTeamReverse){
//
//            List<JclqRankingInfoBean> tempRankList = homeRankList;
//
//            String tempTitle = homeTitle;
//
//            homeTitle = guestTitle;
//
//            guestTitle = tempTitle;
//
//            homeRankList = guestRankList;
//
//            guestRankList = tempRankList;
//
//        }

    }

    private void calculateData(List<JclqRankingInfoBean> list, int homeId, int guestId) {
        if (list == null) {
            return;
        }
        for (JclqRankingInfoBean bean : list) {
            if (bean.getHoldCount() > 0) {
                bean.setHoldCountStr(bean.getHoldCount() + "连胜");
            } else {
                bean.setHoldCountStr(Math.abs(bean.getHoldCount()) + "连败");
            }
            if (bean.getTeamId() == homeId) {
                bean.setShowHome(true);
            }
            if (bean.getTeamId() == guestId) {
                bean.setShowGuest(true);
            }
            bean.setGetScoreAve(MathUtils.scale(bean.getInScoreAve() - bean.getLostScoreAve(), 2, true));
        }
    }
}
