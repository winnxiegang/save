package com.android.xjq.bean.scheduledetail;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2017/1/20 10:24.
 */
public class JczqRankingBean {

    private String nowDate;

    private RankTitleBean rankTitle;

    private List<JczqRankingInfoBean> guestList;

    private List<JczqRankingInfoBean> homeList;

    private List<JczqRankingInfoBean> sameRankDataList;

    private DiffGroupRankBean secondRankMap;

    private DiffGroupRankBean allRankMap;

    private DiffGroupRankBean firstRankMap;

    private List<RankColor> rankColor;

    private List<RankColor> homeRankColor;

    private List<RankColor> guestRankColor;
    /**
     * 赛制描述
     */
    private String matchRuleText;

    private boolean showDiffGroupRank;

//    /**
//     * 比赛是否反转
//     */
//    @Expose
//    private boolean innerTeamReverse;

    @Expose
    private long homeTeamId;

    @Expose
    private long guestTeamId;

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isShowDiffGroupRank() {
        return showDiffGroupRank;
    }

    public void setShowDiffGroupRank(boolean showDiffGroupRank) {
        this.showDiffGroupRank = showDiffGroupRank;
    }

    public DiffGroupRankBean getFirstRankMap() {
        return firstRankMap;
    }

    public void setFirstRankMap(DiffGroupRankBean firstRankMap) {
        this.firstRankMap = firstRankMap;
    }

    public DiffGroupRankBean getSecondRankMap() {
        return secondRankMap;
    }

    public void setSecondRankMap(DiffGroupRankBean secondRankMap) {
        this.secondRankMap = secondRankMap;
    }

    public DiffGroupRankBean getAllRankMap() {
        return allRankMap;
    }

    public void setAllRankMap(DiffGroupRankBean allRankMap) {
        this.allRankMap = allRankMap;
    }

    public String getMatchRuleText() {
        return matchRuleText;
    }

    public void setMatchRuleText(String matchRuleText) {
        this.matchRuleText = matchRuleText;
    }

    public List<JczqRankingInfoBean> getSameRankDataList() {
        return sameRankDataList;
    }

    public void setSameRankDataList(List<JczqRankingInfoBean> sameRankDataList) {
        this.sameRankDataList = sameRankDataList;
    }

    public RankTitleBean getRankTitle() {
        return rankTitle;
    }

    public void setRankTitle(RankTitleBean rankTitle) {
        this.rankTitle = rankTitle;
    }

//    public boolean isInnerTeamReverse() {
//        return innerTeamReverse;
//    }
//
//    public void setInnerTeamReverse(boolean innerTeamReverse) {
//        this.innerTeamReverse = innerTeamReverse;
//    }

    public long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public long getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(long guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public List<JczqRankingInfoBean> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<JczqRankingInfoBean> guestList) {
        this.guestList = guestList;
    }

    public List<JczqRankingInfoBean> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<JczqRankingInfoBean> homeList) {
        this.homeList = homeList;
    }

    public List<RankColor> getRankColor() {
        return rankColor;
    }

    public void setRankColor(List<RankColor> rankColorList) {
        rankColor = rankColorList;
    }

    public List<RankColor> getHomeRankColor() {
        return homeRankColor;
    }

    public void setHomeRankColor(List<RankColor> homeRankColor) {
        this.homeRankColor = homeRankColor;
    }

    public List<RankColor> getGuestRankColor() {
        return guestRankColor;
    }

    public void setGuestRankColor(List<RankColor> guestRankColor) {
        this.guestRankColor = guestRankColor;
    }

    public boolean isEmpty() {
        return (getSameRankDataList() == null || getSameRankDataList().size() == 0)
                && (getHomeList() == null || getHomeList().size() == 0)
                && (getGuestList() == null || getGuestList().size() == 0);
    }

    public void operatorData(long homeTId,long guestTId) {

//        innerTeamReverse = data.isInnerTeamReverse();

        homeTeamId = homeTId;
        guestTeamId = guestTId;

//香蕉球不考虑反转
//        if (data.isInnerTeamReverse() && TextUtils.isEmpty(rankTitle.getSameTitle())) {
//
//            List<JczqRankingInfoBean> tempRankList = homeList;
//
//            homeList = guestList;
//
//            guestList = tempRankList;
//
//            String tempTitle = rankTitle.getHomeTitle();
//
//            rankTitle.setHomeTitle(rankTitle.getGuestTitle());
//
//            rankTitle.setGuestTitle(tempTitle);
//        }

        setTeamType(homeList);

        setTeamType(guestList);

        setTeamType(sameRankDataList);

    }

    public void operatorDiffGroupData(int mCheckCycle) {
        switch (mCheckCycle) {
            case 0:
                if (getFirstRankMap() != null) {
                    setSameRankDataList(getFirstRankMap().getList());
                }
                break;
            case 1:
                if (getSecondRankMap() != null) {
                    setSameRankDataList(getSecondRankMap().getList());
                }
                break;
            case 2:
                if (getAllRankMap() != null) {
                    setSameRankDataList(getAllRankMap().getList());
                }
                break;
        }
        setTeamType(getSameRankDataList());
    }

    private void setTeamType(List<JczqRankingInfoBean> list) {
        if (list != null && list.size() > 0) {
            for (JczqRankingInfoBean jczqRankingInfoBean : list) {
                if (homeTeamId == jczqRankingInfoBean.getTi()) {
                    jczqRankingInfoBean.setTeamType(JczqRankingInfoBean.TeamType.HOME);
                } else if (guestTeamId == jczqRankingInfoBean.getTi()) {
                    jczqRankingInfoBean.setTeamType(JczqRankingInfoBean.TeamType.GUEST);
                } else {
                    jczqRankingInfoBean.setTeamType(JczqRankingInfoBean.TeamType.NORMAL);
                }
            }
        }
    }

    public static class RankTitleBean {

        private String sameTitle;

        private String homeTitle;

        private String guestTitle;

        public String getSameTitle() {
            return sameTitle;
        }

        public void setSameTitle(String sameTitle) {
            this.sameTitle = sameTitle;
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

    }

    public static class DiffGroupRankBean {

        List<JczqRankingInfoBean> list;

        public List<JczqRankingInfoBean> getList() {
            return list;
        }

        public void setList(List<JczqRankingInfoBean> list) {
            this.list = list;
        }
    }

    public static class RankColor {
        /**
         * color: "#FF9966",
         * teamGrade: "欧冠杯小组赛资格"
         */
        public String color;
        public String teamGrade;
    }
}
