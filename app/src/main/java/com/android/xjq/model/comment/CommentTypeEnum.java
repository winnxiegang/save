package com.android.xjq.model.comment;

/**
 * Created by zhouyi on 2015/12/28 15:38.
 */
public enum CommentTypeEnum {

    SUBJECT("话题"),

    LOTTERY_PROJECT("方案"),

    FT_SEASON_RACE("足球赛事"),

    BT_SEASON_RACE("篮球赛事"),

    MAGAZINE("期刊"),

    CMS_NEWS("新闻"),

    JCZQ_RACE("竞彩足球析"),

    DEFAULT("");

    String message;

    CommentTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static CommentTypeEnum safeValueOf(String name){

        try {
            return CommentTypeEnum.valueOf(name);
        }catch (Exception e){
            return DEFAULT;
        }
    }
}
