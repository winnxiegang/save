package com.android.banana.commlib.liveScore.livescoreEnum;

import com.android.banana.commlib.http.AppParam;
import com.android.httprequestlib.BaseRequestHttpName;

/**
 * Created by lingjiu on 2018/1/31.
 */

public enum LiveScoreUrlEnum implements BaseRequestHttpName {

    //助威
    CHANNEL_AREA_CHEER_GAME_BOARD_QUERY("CHANNEL_AREA_CHEER_GAME_BOARD_QUERY", AppParam.S_URL),

    //篮球比赛数据
    DATA_QUERY_BY_BIZID("DATA_QUERY_BY_BIZID", AppParam.S_URL),

    //足球比赛数据
    JCZQ_DATA_QUERY_BY_BIZID("JCZQ_DATA_QUERY_BY_BIZID", AppParam.API_DOMAIN),

    //篮球比分实时接口
    DYNAMIC_SCORE_DATA("DYNAMIC_SCORE_DATA", AppParam.BT_API_DOMAIN),

    //足球比分实时接口
    DYNAMIC_DATA_QUERY("DYNAMIC_DATA_QUERY", AppParam.FT_API_DOMAIN),

    //动画直播技术统计
    RACE_TEAM_TECH_AND_EVENT_QUERY("RACE_TEAM_TECH_AND_EVENT_QUERY", AppParam.FT_API_S_URL),

    //动画直播头部数据接口
    FLASH_SCORE_QUERY("FLASH_SCORE_QUERY", AppParam.FT_API_S_URL),

    //ft系统的获取近五场
    SEASON_RACE_LASTEST5_RESULT_QUERY("SEASON_RACE_LASTEST5_RESULT_QUERY", AppParam.FT_API_S_URL),

    //SEASON_RACE_STATUS_QUERY赛室状态查询
    SEASON_RACE_STATUS_QUERY("SEASON_RACE_STATUS_QUERY", AppParam.FT_API_S_URL);

    private String name;

    private String url;

    LiveScoreUrlEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
