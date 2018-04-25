package com.android.xjq.model;

/**
 * Created by qiaomu on 2017/11/29.
 */

public @interface LikeObjectType {
    /**
     * 喜欢对象类型
     * 枚举字段如下：
     * COMMENT => 评论
     * SUBJECT => 话题
     * FT_SEASON_RACE => 足球赛事
     * BT_SEASON_RACE => 篮球赛事
     * LOTTERY_PROJECT => 方案
     * MAGAZINE => 杂志
     * CMS_NEWS => 新闻
     */

    String COMMENT = "COMMENT";

    String SUBJECT = "SUBJECT";

    String FT_SEASON_RACE = "FT_SEASON_RACE";

    String BT_SEASON_RACE = "BT_SEASON_RACE";

    String LOTTERY_PROJECT = "LOTTERY_PROJECT";

    String MAGAZINE = "MAGAZINE";

    String CMS_NEWS = "CMS_NEWS";
}
