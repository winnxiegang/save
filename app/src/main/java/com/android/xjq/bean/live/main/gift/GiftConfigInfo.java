package com.android.xjq.bean.live.main.gift;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/4/5.
 */

public class GiftConfigInfo {


    private int doubleHitGiftNum;

    private List<GiftEffectConfigListBean> giftEffectConfigList;

    private String giftImageUrl;

    @Expose
    private List<Integer> thisChannelGiftNumRange;
    @Expose
    private List<Integer> allChannelGiftNumRange;
    @Expose
    private long giftGiveMaxNum;

    public GiftCountInfo judgeDataRange(long num) {

        GiftCountInfo giftCountInfo = new GiftCountInfo();
        giftCountInfo.setNum(num);
        //if (num < doubleHitGiftNum) {
        if (num >= doubleHitGiftNum) {
            giftCountInfo.setDesc("触发连击");
        }
        // }
        if (thisChannelGiftNumRange != null && thisChannelGiftNumRange.size() == 2) {
            if (num >= doubleHitGiftNum && num < thisChannelGiftNumRange.get(0)) {
                giftCountInfo.setNum(num);
                giftCountInfo.setDesc("触发连击");
            } else if (num >= thisChannelGiftNumRange.get(0) && num <= thisChannelGiftNumRange.get(1)) {
                giftCountInfo.setNum(num);
                giftCountInfo.setDesc("本频道横幅");
            }
            if (num >= giftGiveMaxNum && allChannelGiftNumRange == null) {
                giftCountInfo.setNum(giftGiveMaxNum);
                giftCountInfo.setDesc("本频道横幅");
            }
        }
        if (allChannelGiftNumRange != null && allChannelGiftNumRange.size() == 2) {
            if (thisChannelGiftNumRange == null && num >= doubleHitGiftNum && num < allChannelGiftNumRange.get(0)) {
                giftCountInfo.setNum(num);
                giftCountInfo.setDesc("触发连击");
            } else if (num >= allChannelGiftNumRange.get(0) && num <= giftGiveMaxNum) {
                giftCountInfo.setNum(num);
                giftCountInfo.setDesc("全频道横幅");
            } else if (num >= giftGiveMaxNum) {
                giftCountInfo.setNum(giftGiveMaxNum);
                giftCountInfo.setDesc("全频道横幅");
            }
        }

        return giftCountInfo;
    }

    public void operatorData(List<GiftCountInfo> giftCountInfoList, long giftGiveMaxNum) {

        this.giftGiveMaxNum = giftGiveMaxNum;

        giftCountInfoList.clear();

        if (giftEffectConfigList != null && giftEffectConfigList.size() > 0) {
            for (GiftEffectConfigListBean giftEffectConfigListBean : giftEffectConfigList) {
                if (giftEffectConfigListBean.getShowSceneType() != null &&
                        "CURRENT_PLATFORM".equals(giftEffectConfigListBean.getShowSceneType().getName())) {
                    int limitMinCount = giftEffectConfigListBean.getLimitMinCount();
                    int limitMaxCount = giftEffectConfigListBean.getLimitMaxCount();
                    thisChannelGiftNumRange = new ArrayList<>();
                    thisChannelGiftNumRange.add(giftEffectConfigListBean.isIncludeMin() ? limitMinCount : limitMinCount + 1);
                    thisChannelGiftNumRange.add(giftEffectConfigListBean.isIncludeMax() ? limitMaxCount : limitMaxCount - 1);
                    if (limitMinCount > 1) {
                        String desc = "本频道横幅";
                        GiftCountInfo giftCountInfo = new GiftCountInfo(giftEffectConfigListBean.isIncludeMin() ? limitMinCount : limitMinCount + 1, desc);
                        giftCountInfoList.add(0, giftCountInfo);
                    }
                }

                if (giftEffectConfigListBean.getShowSceneType() != null &&
                        "ALL_PLATFORM".equals(giftEffectConfigListBean.getShowSceneType().getName())) {
                    int limitMinCount = giftEffectConfigListBean.getLimitMinCount();
                    int limitMaxCount = giftEffectConfigListBean.getLimitMaxCount();
                    allChannelGiftNumRange = new ArrayList<>();
                    allChannelGiftNumRange.add(giftEffectConfigListBean.isIncludeMin() ? limitMinCount : limitMinCount + 1);
                    allChannelGiftNumRange.add(giftEffectConfigListBean.isIncludeMax() ? limitMaxCount : limitMaxCount - 1);
                    if (limitMinCount > 1) {
                        String desc = "全频道横幅";
                        GiftCountInfo giftCountInfo = new GiftCountInfo(giftEffectConfigListBean.isIncludeMin() ? limitMinCount : limitMinCount + 1, desc);
                        giftCountInfoList.add(giftCountInfo);
                    }
                }
            }
        }

        giftCountInfoList.add(0, judgeDataRange(1));

        if (doubleHitGiftNum > 1 && thisChannelGiftNumRange != null && thisChannelGiftNumRange.size() == 2 &&
                doubleHitGiftNum < thisChannelGiftNumRange.get(0)) {
            giftCountInfoList.add(1, new GiftCountInfo(doubleHitGiftNum, "触发连击"));
        }

//        separateRepeatElement(giftCountInfoList);
    }

    //去重操作
    private void separateRepeatElement(List<GiftCountInfo> giftCountInfos) {
        for (int i = 0; i < giftCountInfos.size() - 1; i++) {
            for (int j = giftCountInfos.size() - 1; j > i; j--) {
                if (giftCountInfos.get(j).getNum() == giftCountInfos.get(i).getNum()) {
                    giftCountInfos.remove(j);
                }
            }
        }
    }


    public String getGiftImageUrl() {
        return giftImageUrl;
    }

    public void setGiftImageUrl(String giftImageUrl) {
        this.giftImageUrl = giftImageUrl;
    }

    public int getDoubleHitGiftNum() {
        return doubleHitGiftNum;
    }

    public void setDoubleHitGiftNum(int doubleHitGiftNum) {
        this.doubleHitGiftNum = doubleHitGiftNum;
    }


    public List<GiftEffectConfigListBean> getGiftEffectConfigList() {
        return giftEffectConfigList;
    }

    public void setGiftEffectConfigList(List<GiftEffectConfigListBean> giftEffectConfigList) {
        this.giftEffectConfigList = giftEffectConfigList;
    }

    public static class GiftEffectConfigListBean {
        /**
         * giftConfigId : 9
         * id : 25
         * includeMax : true
         * includeMin : true
         * limitMaxCount : 9
         * limitMinCount : 5
         * showSceneType : {"message":"直播间","name":"CURRENT_PLATFORM"}
         */

        private int giftConfigId;
        private int id;
        private boolean includeMax;
        private boolean includeMin;
        private int limitMaxCount;
        private int limitMinCount;
        private ShowSceneTypeBean showSceneType;

        public int getGiftConfigId() {
            return giftConfigId;
        }

        public void setGiftConfigId(int giftConfigId) {
            this.giftConfigId = giftConfigId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIncludeMax() {
            return includeMax;
        }

        public void setIncludeMax(boolean includeMax) {
            this.includeMax = includeMax;
        }

        public boolean isIncludeMin() {
            return includeMin;
        }

        public void setIncludeMin(boolean includeMin) {
            this.includeMin = includeMin;
        }

        public int getLimitMaxCount() {
            return limitMaxCount;
        }

        public void setLimitMaxCount(int limitMaxCount) {
            this.limitMaxCount = limitMaxCount;
        }

        public int getLimitMinCount() {
            return limitMinCount;
        }

        public void setLimitMinCount(int limitMinCount) {
            this.limitMinCount = limitMinCount;
        }

        public ShowSceneTypeBean getShowSceneType() {
            return showSceneType;
        }

        public void setShowSceneType(ShowSceneTypeBean showSceneType) {
            this.showSceneType = showSceneType;
        }

        public static class ShowSceneTypeBean {
            /**
             * message : 直播间
             * name : CURRENT_PLATFORM
             */

            private String message;
            private String name;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
