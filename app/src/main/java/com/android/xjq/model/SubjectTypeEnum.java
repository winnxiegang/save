package com.android.xjq.model;

/**
 * Created by lingjiu on 2016/12/13 9:44.
 */
public enum SubjectTypeEnum {

    NORMAL_SUBJECT("话题",null),

    RACE_ANALYSIS("分析",new SubjectSubTypeEnum[] {
        SubjectSubTypeEnum.COMPREHENSIVE_ANALYSIS,
                SubjectSubTypeEnum.FUNDAMENTAL_ANALYSIS,
                SubjectSubTypeEnum.EUROPE_ANALYSIS,
                SubjectSubTypeEnum.ASIA_ANALYSIS}),

    DEFAULT("",null);

    String message;

    SubjectSubTypeEnum[] child;

    SubjectTypeEnum(String message, SubjectSubTypeEnum[] child) {
        this.message = message;
        this.child = child;
    }

    public static enum SubjectSubTypeEnum {

        COMPREHENSIVE_ANALYSIS("综合分析"),

        FUNDAMENTAL_ANALYSIS("基本面分析"),

        EUROPE_ANALYSIS("欧盘分析"),

        ASIA_ANALYSIS("亚盘分析");

        String message;

        SubjectSubTypeEnum(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static boolean contains(String type){
            for(SubjectSubTypeEnum typeEnum : SubjectSubTypeEnum.values()){
                if(typeEnum.name().equals(type)){
                    return true;
                }
            }
            return false;
        }
    }


    public static SubjectTypeEnum safeValueOf(String name) {

        try {
            return SubjectTypeEnum.valueOf(name);

        } catch (Exception e) {
        }

        return DEFAULT;
    }

    public SubjectSubTypeEnum[] getChild() {
        return child;
    }

    public String getMessage() {
        return message;
    }

}
