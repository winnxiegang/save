package com.android.xjq.model.dynamic;

/**
 * Created by Admin on 2016/5/12.
 */
public enum JczqPointTypeEnum {

    SR_AAR("总体排名"),
    SR_HAR("主场排名"),
    SR_GAR("客场排名");

    String pointsType;
    JczqPointTypeEnum(String pointsType){
        this.pointsType = pointsType;
    }

    public String getPointsType(){
        return pointsType;
    }



}
