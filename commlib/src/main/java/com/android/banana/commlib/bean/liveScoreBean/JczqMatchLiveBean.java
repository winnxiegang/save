package com.android.banana.commlib.bean.liveScoreBean;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum;
import com.android.banana.commlib.utils.TimeUtils;

import java.util.List;

/**
 * Created by zhouyi on 2016/5/26 10:37.
 */

/**
 * 竞彩足球比分直播返回的JSON
 */
public class JczqMatchLiveBean implements BaseOperator {

    private long timestamp;

    private String nowDate;

    private List<JczqDynamicDataBean> dynamicDataList;

    @Override
    public void operatorData() {
        for (JczqDynamicDataBean liveBean : dynamicDataList) {
            if (FtRaceStatusEnum.saveValueOf(liveBean.getS().getName()) == FtRaceStatusEnum.PLAY_S) {
                int betweenTime = TimeUtils.date1SubDate2M(nowDate, liveBean.getSst());
                if (betweenTime + 45 > 90) {
                    liveBean.setLiveHaveStartTime("90+");
                } else {
                    liveBean.setLiveHaveStartTime(String.valueOf((betweenTime + 45)));
                }

            } else if (FtRaceStatusEnum.saveValueOf(liveBean.getS().getName()) == FtRaceStatusEnum.PLAY_F) {
                int betweenTime = 0;
                if (liveBean.getFst() == null) {
                    betweenTime = TimeUtils.date1SubDate2M(nowDate, liveBean.getSt());
                } else {
                    betweenTime = TimeUtils.date1SubDate2M(nowDate, liveBean.getFst());
                }
                if (betweenTime > 45) {
                    liveBean.setLiveHaveStartTime("45+");
                } else {
                    liveBean.setLiveHaveStartTime(String.valueOf(betweenTime));
                }
            }
            //这边为了解决一个服务端的一个问题：下半场开始的前两分钟内可能拿不到下半场比赛开始时间。这是就显示46'
            else if (FtRaceStatusEnum.saveValueOf(liveBean.getS().getName()) == FtRaceStatusEnum.PLAY_S) {
                liveBean.setLiveHaveStartTime(46 + "");
            }
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public List<JczqDynamicDataBean> getDynamicDataList() {
        return dynamicDataList;
    }

    public void setDynamicDataList(List<JczqDynamicDataBean> dynamicDataList) {
        this.dynamicDataList = dynamicDataList;
    }

}
