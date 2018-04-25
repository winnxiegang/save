package com.android.residemenu.lt_lib.model.jjc;

import com.android.residemenu.lt_lib.model.PlateBean;

import java.util.List;
import java.util.Map;

/**
 * 竞技彩投注选项和sp
 *
 * @author leslie
 * @version $Id: EntityOption.java, v 0.1 2014年7月9日 下午3:43:54 leslie Exp $
 */
public class EntityOption {

    /**
     * 投注枚举名称
     */
    private String name;

    /**
     *
     */
    private String message;

    /**
     * 枚举值
     */
    private String value;
    /**
     * 投注时sp
     */
    private String sp;
    /**
     * 投注类别
     */
    private String subType;

    /**
     * 投注类别枚举
     */
    private String subTypeName;

    /**
     * 选项是否和彩果相同
     */
    private boolean selected;

    Map<String,List<PlateBean>> plateListMap;

    /**
     * @param name    WIN
     * @param message 主胜
     * @param value
     * @param sp
     */
    public EntityOption(String name, String message, String value, String sp) {
        super();
        this.name = name;
        this.message = message;
        this.value = value;
        this.sp = sp;
    }

    public EntityOption(String name, String message, String value) {
        super();
        this.name = name;
        this.message = message;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public Map<String, List<PlateBean>> getPlateListMap() {
        return plateListMap;
    }

    public void setPlateListMap(Map<String, List<PlateBean>> plateListMap) {
        this.plateListMap = plateListMap;
    }
}
