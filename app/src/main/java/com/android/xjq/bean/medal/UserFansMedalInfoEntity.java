package com.android.xjq.bean.medal;

import android.content.Context;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/9/28.
 */

public class UserFansMedalInfoEntity {

    /**
     * areaIdAndLoginNameMap : {"30021":"主播3"}
     * jumpLogin : false
     * nowDate : 2017-09-28 13:19:56
     * paginator : {"items":1,"itemsPerPage":20,"page":1,"pages":1}
     * success : true
     * userMedalDetailIdAndActionTypeMap : {"2337012212506480980053810890":{"message":"增加值","name":"INCREASE_VALUE"}}
     * userMedalDetailList : [{"adorned":true,"areaId":30021,"currentMedalLevelConfigCode":"Lv4","currentMedalLevelConfigId":4,"currentValue":5880,"id":"2337012212506480980053810890","maxMedalLevelValue":8800,"medalLabelConfigList":[],"userId":"8201703310470031"}]
     */

    private HashMap<Integer, String> areaIdAndLoginNameMap;
    private PaginatorBean paginator;
    private HashMap<String, MedalActionType> userMedalDetailIdAndActionTypeMap;
    private List<UserMedalBean> userMedalDetailList;
    private int awardNum;
    private boolean anchorApply;
    private UserMedalBean adornedUserMedalDetail;
    private List<UserMedalBean.MedalLabelConfigListBean> medalLabelConfigList;
    private int pointcoinAmount;
    private int goldcoinCoinAmount;
    private int giftcoinAmount;

    public void operatorData(Context context, String newMedalId) {
        if (userMedalDetailList != null && userMedalDetailList.size() > 0) {
            for (UserMedalBean userMedalBean : userMedalDetailList) {
                if (areaIdAndLoginNameMap != null && areaIdAndLoginNameMap.size() > 0) {
                    if (areaIdAndLoginNameMap.containsKey(userMedalBean.getAreaId())) {
                        userMedalBean.setHostName(areaIdAndLoginNameMap.get(userMedalBean.getAreaId()));
                    }
                }
                if (userMedalDetailIdAndActionTypeMap != null && userMedalDetailIdAndActionTypeMap.size() > 0) {
                    if (userMedalDetailIdAndActionTypeMap.containsKey(userMedalBean.getId())) {
                        userMedalBean.setActionType(userMedalDetailIdAndActionTypeMap.get(userMedalBean.getId()).getName());
                    }
                }
                //开始默认选中已佩戴的
                if (userMedalBean.isAdorned()) {
                    userMedalBean.setSelected(true);
                }
                if (userMedalBean.getId().equals(newMedalId)) {
                    userMedalBean.setNewMedal(true);
                }

                String resName = "icon_fans_medal_" + userMedalBean.getCurrentMedalLevelConfigCode();
                int resId = context.getResources().getIdentifier(resName.toLowerCase(), "drawable", context.getPackageName());
                userMedalBean.setResId(resId);
            }
        }
    }

    public int getGiftcoinAmount() {
        return giftcoinAmount;
    }

    public void setGiftcoinAmount(int giftcoinAmount) {
        this.giftcoinAmount = giftcoinAmount;
    }

    public List<UserMedalBean.MedalLabelConfigListBean> getMedalLabelConfigList() {
        return medalLabelConfigList;
    }

    public void setMedalLabelConfigList(List<UserMedalBean.MedalLabelConfigListBean> medalLabelConfigList) {
        this.medalLabelConfigList = medalLabelConfigList;
    }

    public int getPointcoinAmount() {
        return pointcoinAmount;
    }

    public void setPointcoinAmount(int pointcoinAmount) {
        this.pointcoinAmount = pointcoinAmount;
    }

    public int getGoldcoinCoinAmount() {
        return goldcoinCoinAmount;
    }

    public void setGoldcoinCoinAmount(int goldcoinCoinAmount) {
        this.goldcoinCoinAmount = goldcoinCoinAmount;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public boolean isAnchorApply() {
        return anchorApply;
    }

    public void setAnchorApply(boolean anchorApply) {
        this.anchorApply = anchorApply;
    }

    public UserMedalBean getAdornedUserMedalDetail() {
        return adornedUserMedalDetail;
    }

    public void setAdornedUserMedalDetail(UserMedalBean adornedUserMedalDetail) {
        this.adornedUserMedalDetail = adornedUserMedalDetail;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public HashMap<Integer, String> getAreaIdAndLoginNameMap() {
        return areaIdAndLoginNameMap;
    }

    public void setAreaIdAndLoginNameMap(HashMap<Integer, String> areaIdAndLoginNameMap) {
        this.areaIdAndLoginNameMap = areaIdAndLoginNameMap;
    }

    public HashMap<String, MedalActionType> getUserMedalDetailIdAndActionTypeMap() {
        return userMedalDetailIdAndActionTypeMap;
    }

    public void setUserMedalDetailIdAndActionTypeMap(HashMap<String, MedalActionType> userMedalDetailIdAndActionTypeMap) {
        this.userMedalDetailIdAndActionTypeMap = userMedalDetailIdAndActionTypeMap;
    }

    public List<UserMedalBean> getUserMedalDetailList() {
        return userMedalDetailList;
    }

    public void setUserMedalDetailList(List<UserMedalBean> userMedalDetailList) {
        this.userMedalDetailList = userMedalDetailList;
    }

    public static class MedalActionType {
        /**
         * message : 增加值
         * name : INCREASE_VALUE
         */

        private String message;
        private String name;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
