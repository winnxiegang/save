package com.android.xjq.model.jcdx;

/**
 * Created by zaozao on 2017/8/9.
 */

public enum JCDXArticleTypeEnum {

    ARTICLE_PREDICT,//预测

    ARTICLE_ANALYSIS,//分析

    ARTICLE_REPLAY,//复盘

    ARTICLE_PERSONAL_EXP,//经验

    DEFAULT;

    public static boolean contains(String type){
        for(JCDXArticleTypeEnum typeEnum : JCDXArticleTypeEnum.values()){
            if(typeEnum.name().equals(type)){
                return true;
            }
        }
        return false;
    }

    public static JCDXArticleTypeEnum safeValueOf(String name) {

        try {
            return JCDXArticleTypeEnum.valueOf(name);

        } catch (Exception e) {
        }

        return DEFAULT;
    }

}
