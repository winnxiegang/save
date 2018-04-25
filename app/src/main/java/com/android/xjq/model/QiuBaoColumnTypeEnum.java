package com.android.xjq.model;

/**
 * 球爆所有栏目类型
 * Created by zhouyi on 2017/1/9 15:16.
 */

public enum QiuBaoColumnTypeEnum {

    TRANSMIT_LOTTERY_SHOW,//转发方案

    TRANSMIT_SUBJECT,//转发话题

    TRANSMIT_CMS_NEWS,//转发资讯

    TRANSMIT_FOCUS_JCZQ_MATCH,//转发焦点赛事

    TRANSMIT_FOCUS_JCLQ_MATCH,//转发竞彩足球焦点赛事

    COMMENT_SUBJECT,//评论话题

    COMMENT_LOTTERY,//评论方案

    COMMENT_CMS_NEWS,//评论资讯

    COMMENT_REPLY_SUBJECT,//评论回复话题

    COMMENT_REPLY_LOTTERY,//评论回复方案

    COMMENT_REPLY_CMS_NEWS,//评论回复资讯

    COUPON_DIRECT_SUBJECT,//话题定向红包

    COUPON_DIRECT_LOTTERY,//方案定向红包

    COUPON_NORMAL_SUBJECT,//话题普通红包

    COUPON_NORMAL_LOTTERY,//方案普通红包

    COUPON_FT_SEASON_RACE,//比分直播足球

    COUPON_BT_SEASON_RACE,//比分直播篮球

    COUPON_CHAT_ROOM,//聊天室红包

    COMMENT_JCZQ_ANALYSIS,//竞彩足球析评论

    COMMENT_JCLQ_ANALYSIS,//竞彩篮球析评论

    COMMENT_REPLY_JCZQ_ANALYSIS,//竞彩足球析评论回复

    COMMENT_REPLY_JCLQ_ANALYSIS,//竞彩篮球析评论回复

    COMMENT_FOCUS_JCZQ_MATCH,//焦点赛事评论

    COMMENT_FOCUS_JCLQ_MATCH,//

    COMMENT_REPLY_JCZQ_FOCUS_MATCH,//焦点赛事评论回复

    COMMENT_REPLY_JCLQ_FOCUS_MATCH,

    CREATE_SUBJECT,//话题创建

    LOTTERY_SHOW,//晒单

    CREATE_ARTICLE,//发起文章

    TRANSMIT_ARTICLE,//转发文章

    COUPON_DIRECT_ARTICLE,//文章定向红包

    COUPON_NORMAL_ARTICLE,//文章普通红包

    COMMENT_ARTICLE,//评论文章

    COMMENT_REPLY_ARTICLE,//评论回复文章

    DEFAULT;//默认

    interface ViewCreate{

        void setView();

        void setListener();

    }
    public static QiuBaoColumnTypeEnum safeValueOf(String name) {
        try {
            return QiuBaoColumnTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }

}
