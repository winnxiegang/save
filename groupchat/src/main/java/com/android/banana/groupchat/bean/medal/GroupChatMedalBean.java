package com.android.banana.groupchat.bean.medal;

import java.util.List;

/**
 * 群勋章数据bean
 * <p>
 * Created by lingjiu on 2017/9/25 0025.
 */

public class GroupChatMedalBean {

    /**
     * adorned : true
     * areaId : 30018
     * currentMedalLevelConfigCode : Lv3
     * currentMedalLevelConfigId : 3
     * id : 2336630010806480980055237539
     * medalLabelConfigList : [{"content":"大佬"}]
     * userId : 8201703150380001
     * <p>
     * medalConfigCode":"GROUP_VIP_MEDAL","medalLevelConfigCode":"Lv1","ownerId":"","ownerName":""
     */

    private boolean adorned;
    private int areaId;
    private String currentMedalLevelConfigCode;
    private int currentMedalLevelConfigId;
    private int currentValue;
    private int maxMedalLevelValue;
    private String id;
    private String userId;
    private String medalConfigCode;
    private String medalLevelConfigCode;
    private String ownerId;
    private String ownerName;
    private List<MedalLabelConfigListBean> medalLabelConfigList;


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

    public String getMedalConfigCode() {
        return medalConfigCode;
    }

    public void setMedalConfigCode(String medalConfigCode) {
        this.medalConfigCode = medalConfigCode;
    }

    public String getMedalLevelConfigCode() {
        return medalLevelConfigCode;
    }

    public void setMedalLevelConfigCode(String medalLevelConfigCode) {
        this.medalLevelConfigCode = medalLevelConfigCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
