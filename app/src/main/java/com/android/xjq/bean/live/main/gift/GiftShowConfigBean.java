package com.android.xjq.bean.live.main.gift;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.bean.live.DoubleHitShowBean;
import com.android.xjq.model.gift.PayType;
import com.android.xjq.view.LandscapeGiftShow;

import java.util.List;

/**
 * Created by lingjiu on 2017/7/11.
 */

public class GiftShowConfigBean {

    /**
     * giftCodeList : ["MVP"]
     * pointcoinAmount : 50.0
     * goldcoinAmount : 500.0
     * showCount : 2
     * keepMinutes : 15
     * timePointList : [1,4,16,22]
     * doubleHitRangeConfigList : [{"minDoubleHit":0,"maxDoubleHit":20,"includeMinDoubleHit":true,"includeMaxDoubleHit":true,"decreaseNum":3},{"minDoubleHit":21,"maxDoubleHit":50,"includeMinDoubleHit":true,"includeMaxDoubleHit":true,"decreaseNum":10},{"minDoubleHit":51,"maxDoubleHit":100,"includeMinDoubleHit":true,"includeMaxDoubleHit":true,"decreaseNum":20},{"minDoubleHit":101,"maxDoubleHit":300,"includeMinDoubleHit":true,"includeMaxDoubleHit":true,"decreaseNum":50},{"minDoubleHit":301,"maxDoubleHit":100000,"includeMinDoubleHit":true,"includeMaxDoubleHit":true,"decreaseNum":100}]
     */

    //单次礼物金额触发连击最小值
    private double pointcoinAmount;
    private double goldcoinAmount;
    //连击显示条数
    private int showCount;
    //礼物活动生效时间
    private int keepActivityMinutes;
    private List<Integer> activityTimePointList;
    //礼物配置生效时间
    private int keepEffectMinutes;
    private List<Integer> effectTimePointList;
    //礼物配置针对的礼物id
    private List<String> giftCodeList;
    //连击往前连击数
    private List<DoubleHitRangeConfigListBean> doubleHitRangeConfigList;
    //横幅持续时间
    private String bannerIntervalSeconds;

    /**
     * 判断礼物规则是否生效
     */
    public boolean judgeGiftRuleIsEffect(String giftCode) {
        if (giftCodeList != null && giftCodeList.size() > 0) {
            for (String s : giftCodeList) {
                if (giftCode.equals(s) && TimeUtils.isRangeContainTime(effectTimePointList, keepEffectMinutes)) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 判断是否在活动时间
     */
    public boolean judgeIsActivityTime() {
        return TimeUtils.isRangeContainTime(activityTimePointList, keepActivityMinutes);
    }

    /**
     * 判断礼物显示在浮层还是公共聊天框
     */
    public boolean judgeAmountIsEnough(String payType, String amount) {
        if (PayType.GOLD_COIN.name().equals(payType)) {
            return goldcoinAmount > Double.valueOf(amount);
        } else if (PayType.POINT_COIN.name().equals(payType)) {
            return pointcoinAmount > Double.valueOf(amount);
        }
        return false;
    }

    public void setDoubleHitCount(DoubleHitShowBean addBean) {
        if (!judgeGiftRuleIsEffect(addBean.getGiftName())) return;
        //如果礼物规则生效且在活动时间,只连击当前数量
        if (judgeIsActivityTime()) {
            addBean.setCurrentShowCount(addBean.getAllDoubleHitCount());
            return;
        }
        if (doubleHitRangeConfigList != null && doubleHitRangeConfigList.size() > 0) {
            //如果礼物规则生效,判断往前连击多少次
            for (DoubleHitRangeConfigListBean bean : doubleHitRangeConfigList) {
                int allDoubleHitCount = addBean.getAllDoubleHitCount();
                if (allDoubleHitCount >= bean.getMinDoubleHit() && allDoubleHitCount <= bean.getMaxDoubleHit()) {
                    int startDoubleHitCount = allDoubleHitCount - bean.getDecreaseNum();
                    if (LandscapeGiftShow.DoubleHitShowStatusEnum.SHOWING != addBean.getShowStatusEnum()) {
                        addBean.setCurrentShowAllDoubleHit(addBean.getAllDoubleHitCount());
                    }
                    //当前连击数小于最新推过来经过计算的连击数(如果仅仅相差1,显示上无需处理)
                    if (addBean.getCurrentShowCount() + 1 < startDoubleHitCount) {
                        int currentDoubleHitCount = startDoubleHitCount;
                        //如果正在显示连击,当前连击数不是递增,则等其消失在显示
                        addBean.setNextShowCount(currentDoubleHitCount);
                    }
                    return;
                }
            }
        }

    }

    public String getBannerIntervalSeconds() {
        return bannerIntervalSeconds;
    }

    public void setBannerIntervalSeconds(String bannerIntervalSeconds) {
        this.bannerIntervalSeconds = bannerIntervalSeconds;
    }

    public double getPointcoinAmount() {
        return pointcoinAmount;
    }

    public void setPointcoinAmount(double pointcoinAmount) {
        this.pointcoinAmount = pointcoinAmount;
    }

    public int getKeepActivityMinutes() {
        return keepActivityMinutes;
    }

    public void setKeepActivityMinutes(int keepActivityMinutes) {
        this.keepActivityMinutes = keepActivityMinutes;
    }

    public List<Integer> getActivityTimePointList() {
        return activityTimePointList;
    }

    public void setActivityTimePointList(List<Integer> activityTimePointList) {
        this.activityTimePointList = activityTimePointList;
    }

    public int getKeepEffectMinutes() {
        return keepEffectMinutes;
    }

    public void setKeepEffectMinutes(int keepEffectMinutes) {
        this.keepEffectMinutes = keepEffectMinutes;
    }

    public List<Integer> getEffectTimePointList() {
        return effectTimePointList;
    }

    public void setEffectTimePointList(List<Integer> effectTimePointList) {
        this.effectTimePointList = effectTimePointList;
    }


    public double getGoldcoinAmount() {
        return goldcoinAmount;
    }

    public void setGoldcoinAmount(double goldcoinAmount) {
        this.goldcoinAmount = goldcoinAmount;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public List<DoubleHitRangeConfigListBean> getDoubleHitRangeConfigList() {
        return doubleHitRangeConfigList;
    }

    public void setDoubleHitRangeConfigList(List<DoubleHitRangeConfigListBean> doubleHitRangeConfigList) {
        this.doubleHitRangeConfigList = doubleHitRangeConfigList;
    }

    public static class DoubleHitRangeConfigListBean {
        /**
         * minDoubleHit : 0
         * maxDoubleHit : 20
         * includeMinDoubleHit : true
         * includeMaxDoubleHit : true
         * decreaseNum : 3
         */

        private int minDoubleHit;
        private int maxDoubleHit;
        private boolean includeMinDoubleHit;
        private boolean includeMaxDoubleHit;
        private int decreaseNum;

        public int getMinDoubleHit() {
            return minDoubleHit;
        }

        public void setMinDoubleHit(int minDoubleHit) {
            this.minDoubleHit = minDoubleHit;
        }

        public int getMaxDoubleHit() {
            return maxDoubleHit;
        }

        public void setMaxDoubleHit(int maxDoubleHit) {
            this.maxDoubleHit = maxDoubleHit;
        }

        public boolean isIncludeMinDoubleHit() {
            return includeMinDoubleHit;
        }

        public void setIncludeMinDoubleHit(boolean includeMinDoubleHit) {
            this.includeMinDoubleHit = includeMinDoubleHit;
        }

        public boolean isIncludeMaxDoubleHit() {
            return includeMaxDoubleHit;
        }

        public void setIncludeMaxDoubleHit(boolean includeMaxDoubleHit) {
            this.includeMaxDoubleHit = includeMaxDoubleHit;
        }

        public int getDecreaseNum() {
            return decreaseNum;
        }

        public void setDecreaseNum(int decreaseNum) {
            this.decreaseNum = decreaseNum;
        }
    }
}
