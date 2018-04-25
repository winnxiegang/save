package com.android.xjq.bean.medal;

import com.android.xjq.R;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by ajiao on 2017/9/25 0025.
 */

public class UserMedalBean {

    /**
     * adorned : true
     * areaId : 30018
     * currentMedalLevelConfigCode : Lv3
     * currentMedalLevelConfigId : 3
     * id : 2336630010806480980055237539
     * medalLabelConfigList : [{"content":"大佬"}]
     * userId : 8201703150380001
     */

    private boolean adorned;
    private int areaId;
    private String currentMedalLevelConfigCode;
    private int currentMedalLevelConfigId;
    private int currentValue;
    private int maxMedalLevelValue;
    private String id;
    private String userId;
    private List<MedalLabelConfigListBean> medalLabelConfigList;
    @Expose
    private String hostName;
    @Expose
    private String actionType;
    @Expose
    private int resId;
    @Expose
    private boolean isSelected;
    @Expose
    private boolean isNewMedal;
    @Expose
    private int statusResId;
    private static final String INCREASE_VALUE = "INCREASE_VALUE";
    private static final String DECREASE_VALUE = "DECREASE_VALUE";
    public boolean isNewMedal() {
        return isNewMedal;
    }

    public void setNewMedal(boolean newMedal) {
        isNewMedal = newMedal;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public int getMaxMedalLevelValue() {
        return maxMedalLevelValue;
    }

    public void setMaxMedalLevelValue(int maxMedalLevelValue) {
        this.maxMedalLevelValue = maxMedalLevelValue;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean isAdorned() {
        return adorned;
    }

    public void setAdorned(boolean adorned) {
        this.adorned = adorned;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getCurrentMedalLevelConfigCode() {
        return currentMedalLevelConfigCode;
    }

    public void setCurrentMedalLevelConfigCode(String currentMedalLevelConfigCode) {
        this.currentMedalLevelConfigCode = currentMedalLevelConfigCode;
    }

    public int getCurrentMedalLevelConfigId() {
        return currentMedalLevelConfigId;
    }

    public void setCurrentMedalLevelConfigId(int currentMedalLevelConfigId) {
        this.currentMedalLevelConfigId = currentMedalLevelConfigId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MedalLabelConfigListBean> getMedalLabelConfigList() {
        return medalLabelConfigList;
    }

    public void setMedalLabelConfigList(List<MedalLabelConfigListBean> medalLabelConfigList) {
        this.medalLabelConfigList = medalLabelConfigList;
    }

    public int getStatusResId() {
        int resId = -1;
        if (INCREASE_VALUE.equals(actionType)) {
            resId = R.drawable.medal_level_increase;
        } else if (DECREASE_VALUE.equals(actionType)) {
            resId = R.drawable.medal_level_decrease;
        }
        return resId;
    }


    public static class MedalLabelConfigListBean {
        /**
         * content : 大佬
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
