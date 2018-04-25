package com.android.banana.commlib.bean.liveScoreBean;

import java.util.List;

/**
 * Created by zhouyi on 2016/5/26 10:37.
 */

/**
 * 竞彩足球比分直播返回的JSON
 */
public class JclqMatchLiveBean {

    private long timestamp;

    private String nowDate;

    private List<JclqDynamicData> dynamicDataList;

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

    public List<JclqDynamicData> getDynamicDataList() {
        return dynamicDataList;
    }

    public void setDynamicDataList(List<JclqDynamicData> dynamicDataList) {
        this.dynamicDataList = dynamicDataList;
    }
}
