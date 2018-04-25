package com.android.xjq.bean.live;

import com.android.library.Utils.encryptUtils.StringUtils;
import com.android.xjq.bean.medal.MedalInfoBean;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2017/4/5.
 */

public class LiveCommentBean {

    /**
     * id : 8928
     * createTime : 2017-04-05 17:51:50
     * groupId : 55
     * content : {"properties":{"fontColor":"","channelId":"200025"},"text":"hghg","color":"","bold":false,"italic":false,"size":0}
     * typeCode : TEXT
     * senderId : 8201704050500001
     * uniqueId : 156ed8cee0bd4ddbbf8738b64257836f
     * systemMessage : false
     * senderName : zhouyi1992
     */

    private String id;
    private String createTime;
    private int groupId;
    private ContentBean content;
    private String typeCode;
    private String senderId;
    private String uniqueId;
    private boolean systemMessage;
    private String senderName;
    private UserPropertiesBean properties;
    //新规则是否生效(特定礼物,特定时间)
    @Expose
    private boolean isRuleEffect;
    //规则生效后是否显示在浮层上
    @Expose
    private boolean isShowFloatingLayer;

    public boolean isRuleEffect() {
        return isRuleEffect;
    }

    public void setRuleEffect(boolean ruleEffect) {
        isRuleEffect = ruleEffect;
    }

    public boolean isShowFloatingLayer() {
        return isShowFloatingLayer;
    }

    public void setShowFloatingLayer(boolean showFloatingLayer) {
        isShowFloatingLayer = showFloatingLayer;
    }

    public UserPropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(UserPropertiesBean properties) {
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getType() {
        return typeCode;
    }

    public void setType(String type) {
        this.typeCode = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(boolean systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public static class UserPropertiesBean {
        //普通发言:等级/马甲/等级图标
        private String levelCode;
        private String roleCodes;
        private String userlevelImgUrl;
        private String medalInfos;
        //成就勋章
        private String achieves;
        //当前已选择主客队选项
        private String userVotedContent;
        @Expose
        private List<String> horseList;
        @Expose
        List<MedalInfoBean> medalInfoBeanList;
        //如果不是自己发的，解析取出腾讯推送的，如果是自己发送的，从InputCommentController的currentFansMedalBean取。
        @Expose
        String medalLevelCode;
        @Expose
        String medalName;

        public String getMedalLevelCode() {
            String lvCode = "";
            if (medalInfos != null && !"".equals(medalInfos)) {
                List<MedalInfoBean> medalInfoBeanList = getMedalInfoBeanList();
                if (medalInfoBeanList != null && medalInfoBeanList.size() > 0) {
                    lvCode = medalInfoBeanList.get(0) != null ? medalInfoBeanList.get(0).getMedalLevelConfigCode() : "";
                }
                return lvCode;
            } else {
                lvCode = this.medalLevelCode;
            }
            return lvCode;
        }

        public String getMedalName() {
            String medalName = "";
            if (medalInfos != null && !"".equals(medalInfos)) {
                List<MedalInfoBean> medalInfoBeanList = getMedalInfoBeanList();
                if (medalInfoBeanList != null && medalInfoBeanList.size() > 0) {
                    MedalInfoBean medalInfoBean = medalInfoBeanList.get(0);
                    if (medalInfoBean != null && medalInfoBean.getLabelInfoList().size() > 0) {
                        medalName = medalInfoBean.getLabelInfoList().get(0).getContent();
                    }
                    return medalName;
                }
            } else {
                medalName = this.medalName;
            }
            return medalName;
        }


        public List<MedalInfoBean> getMedalInfoBeanList() {
            if (medalInfoBeanList != null) return medalInfoBeanList;
            medalInfoBeanList = new ArrayList<>();
            Gson gson = new Gson();
            if (!StringUtils.isEmpty(medalInfos)) {
                List<MedalInfoBean> medalInfoBeans = gson.fromJson(medalInfos, new TypeToken<List<MedalInfoBean>>() {
                }.getType());
                if (medalInfoBeans != null)
                    medalInfoBeanList.addAll(medalInfoBeans);
            }
            if (!StringUtils.isEmpty(achieves)) {
                List<MedalInfoBean> achieveInfoBeans = gson.fromJson(achieves, new TypeToken<List<MedalInfoBean>>() {
                }.getType());
                if (achieveInfoBeans != null)
                    medalInfoBeanList.addAll(achieveInfoBeans);
            }
            return medalInfoBeanList;
        }

        public void setAchieves(String achieves) {
            this.achieves = achieves;
        }

        public void setUserVotedContent(String userVotedContent) {
            this.userVotedContent = userVotedContent;
        }

        public void setMedalName(String medalName) {
            this.medalName = medalName;
        }

        public void setMedalLevelCode(String medalLevelCode) {
            this.medalLevelCode = medalLevelCode;
        }

        public void setMedalInfoBeanList(List<MedalInfoBean> medalInfoBeanList) {
            this.medalInfoBeanList = medalInfoBeanList;
        }

        public String getAchieves() {
            return achieves;
        }

        public String getUserVotedContent() {
            return userVotedContent;
        }

        public String getMedalInfos() {
            return medalInfos;
        }

        public void setMedalInfos(String medalInfos) {
            this.medalInfos = medalInfos;
        }

        public List<String> getHorseList() {
            if (!StringUtils.isEmpty(roleCodes)) {
                horseList = new Gson().fromJson(roleCodes, new TypeToken<List<String>>() {
                }.getType());
            }
            //添加主客队标识
            if (horseList == null)
                horseList = new ArrayList<>();
            horseList.add(userVotedContent);
            return horseList;
        }

        public void setHorseList(List<String> horseList) {
            this.horseList = horseList;
        }

        public String getLevelCode() {
            return levelCode;
        }

        public void setLevelCode(String levelCode) {
            this.levelCode = levelCode;
        }

        public String getRoleCodes() {
            return roleCodes;
        }

        public void setRoleCodes(String roleCodes) {
            this.roleCodes = roleCodes;
        }

        public String getUserlevelImgUrl() {
            return userlevelImgUrl;
        }

        public void setUserlevelImgUrl(String userlevelImgUrl) {
            this.userlevelImgUrl = userlevelImgUrl;
        }

    }

    public static class ContentBean {
        /**
         * properties : {"fontColor":"","channelId":"200025"}
         * text : hghg
         * color :
         * bold : false
         * italic : false
         * size : 0
         */

        private PropertiesBean properties;
        private String text;
        private String fontColor;
        private boolean bold;
        private boolean italic;
        private int size;
        private BodyBean body;


        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public boolean isBold() {
            return bold;
        }

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        public boolean isItalic() {
            return italic;
        }

        public void setItalic(boolean italic) {
            this.italic = italic;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public static class PropertiesBean {
            /**
             * fontColor :
             * channelId : 200025
             */

            private String fontColor;
            private String channelId;

            public String getFontColor() {
                return fontColor;
            }

            public void setFontColor(String fontColor) {
                this.fontColor = fontColor;
            }

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }
        }

        public static class BodyBean {

            /**
             * reveiveUserId : 8201702240300010
             * giftEffectContent : http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/mvp.gif
             * totalCount : 1
             * giftConfigId : 1
             * platformObjectId : 200003
             * giftShowImageUrl : http://img-giftcore.huohongshe.net/images/gift/effect/publicChat/mvp.gif
             * reveiveUserName : 主播4
             * platformObjectType : CHANNEL
             * giftEffectMemo : 公共聊天效果
             * sendUserId : 8201704050500001
             * giftEffectShowSceneType : COMMON
             * giftConfigName : MVP
             * doubleHit : 1
             */

            private String giftConfigCode;
            private String doubleHitGroupNo;
            private String resourceConfigList;
            private String reveiveUserId;
            private String giftEffectContent;
            private String totalCount;
            private String giftConfigId;
            private String platformObjectId;
            private String giftShowImageUrl;
            private String receiveUserName;
            private String platformObjectType;
            private String giftEffectMemo;
            private String sendUserId;
            private String giftEffectShowSceneType;
            private String giftConfigName;
            private String doubleHit;
            private String giftEffectPropertyList;
            //是否使用模板
            private String useModelResource;
            //模板的基本配置
            private String modelResourceConfigList;
            //模板的字体配置
            private String modelRsPropertyList;

            //爆奖池
            private String loginName;
            private String prizeAmount;
            private String prizeItemType;
            private String userId;
            //红包类型
            private String couponType;

            //主播当前推流状态
            private String channelAreaId;
            private String pushStream;
            //传送直播间("飞机票")信息
            private String fromChannelAreaAnchorName;
            private String fromChannelAreaId;
            private String toChannelAreaAnchorName;
            private String toChannelAreaId;
            private String toChannelTitle;
            //礼物图片url
            private String giftImageUrl;

            //礼物赠送数量以及金额
            private String payType;
            private String totalAmount;

            //首次获得粉丝勋章
            private String fristAward;
            private String medalContent;
            private String medalLevelCode;
            private String userMedalDetailId;
            //节目名
            private String channelAreaName;
            private String showSeconds;
            @Expose
            private boolean isOwnGift;

            public String getSecondsTime() {
                return showSeconds;
            }

            public void setSecondsTime(String secondsTime) {
                this.showSeconds = secondsTime;
            }

            public String getChannelAreaName() {
                return channelAreaName;
            }

            public void setChannelAreaName(String channelAreaName) {
                this.channelAreaName = channelAreaName;
            }

            public String getModelResourceConfigList() {
                return modelResourceConfigList;
            }

            public void setModelResourceConfigList(String modelResourceConfigList) {
                this.modelResourceConfigList = modelResourceConfigList;
            }

            public String getModelRsPropertyList() {
                return modelRsPropertyList;
            }

            public void setModelRsPropertyList(String modelRsPropertyList) {
                this.modelRsPropertyList = modelRsPropertyList;
            }

            public boolean isUseModelResource() {
                return Boolean.valueOf(useModelResource);
            }

            public void setUseModelResource(String useModelResource) {
                this.useModelResource = useModelResource;
            }

            public boolean isFristAward() {
                return Boolean.valueOf(fristAward);
            }

            public void setFristAward(String fristAward) {
                this.fristAward = fristAward;
            }

            public String getMedalContent() {
                return medalContent;
            }

            public void setMedalContent(String medalContent) {
                this.medalContent = medalContent;
            }

            public String getMedalLevelCode() {
                return medalLevelCode;
            }

            public void setMedalLevelCode(String medalLevelCode) {
                this.medalLevelCode = medalLevelCode;
            }

            public String getUserMedalDetailId() {
                return userMedalDetailId;
            }

            public void setUserMedalDetailId(String userMedalDetailId) {
                this.userMedalDetailId = userMedalDetailId;
            }

            public String getFromChannelAreaAnchorName() {
                return fromChannelAreaAnchorName;
            }

            public void setFromChannelAreaAnchorName(String fromChannelAreaAnchorName) {
                this.fromChannelAreaAnchorName = fromChannelAreaAnchorName;
            }

            public String getToChannelAreaAnchorName() {
                return toChannelAreaAnchorName;
            }

            public void setToChannelAreaAnchorName(String toChannelAreaAnchorName) {
                this.toChannelAreaAnchorName = toChannelAreaAnchorName;
            }

            public String getFromChannelAreaId() {
                return fromChannelAreaId;
            }

            public void setFromChannelAreaId(String fromChannelId) {
                this.fromChannelAreaId = fromChannelId;
            }

            public String getToChannelAreaId() {
                return toChannelAreaId;
            }

            public void setToChannelAreaId(String toChannelId) {
                this.toChannelAreaId = toChannelId;
            }

            public String getToChannelTitle() {
                return toChannelTitle;
            }

            public void setToChannelTitle(String toChannelTitle) {
                this.toChannelTitle = toChannelTitle;
            }

            public String getCouponType() {
                return couponType;
            }

            public void setCouponType(String couponType) {
                this.couponType = couponType;
            }

            public boolean isOwnGift() {
                return isOwnGift;
            }

            public void setOwnGift(boolean ownGift) {
                isOwnGift = ownGift;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getGiftImageUrl() {
                return giftImageUrl == null ? "" : giftImageUrl;
            }

            public void setGiftImageUrl(String giftImageUrl) {
                this.giftImageUrl = giftImageUrl;
            }

            public String getChannelAreaId() {
                return channelAreaId;
            }

            public void setChannelAreaId(String channelAreaId) {
                this.channelAreaId = channelAreaId;
            }

            public String getPushStream() {
                return pushStream;
            }

            public void setPushStream(String pushStream) {
                this.pushStream = pushStream;
            }

            public String getGiftConfigCode() {
                return giftConfigCode;
            }

            public void setGiftConfigCode(String giftConfigCode) {
                this.giftConfigCode = giftConfigCode;
            }


            public String getLoginName() {
                return loginName;
            }

            public void setLoginName(String loginName) {
                this.loginName = loginName;
            }

            public String getPrizeAmount() {
                return prizeAmount;
            }

            public void setPrizeAmount(String prizeAmount) {
                this.prizeAmount = prizeAmount;
            }

            public String getPrizeItemType() {
                return prizeItemType;
            }

            public void setPrizeItemType(String prizeItemType) {
                this.prizeItemType = prizeItemType;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }


            public String getResourceConfigList() {
                return resourceConfigList;
            }

            public void setResourceConfigList(String resourceConfigList) {
                this.resourceConfigList = resourceConfigList;
            }

            public String getReveiveUserId() {
                return reveiveUserId;
            }

            public void setReveiveUserId(String reveiveUserId) {
                this.reveiveUserId = reveiveUserId;
            }

            public String getGiftEffectContent() {
                return giftEffectContent;
            }

            public void setGiftEffectContent(String giftEffectContent) {
                this.giftEffectContent = giftEffectContent;
            }

            public String getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(String totalCount) {
                this.totalCount = totalCount;
            }

            public String getGiftConfigId() {
                return giftConfigId;
            }

            public void setGiftConfigId(String giftConfigId) {
                this.giftConfigId = giftConfigId;
            }

            public String getPlatformObjectId() {
                return platformObjectId;
            }

            public void setPlatformObjectId(String platformObjectId) {
                this.platformObjectId = platformObjectId;
            }

            public String getGiftShowImageUrl() {
                return giftShowImageUrl;
            }

            public void setGiftShowImageUrl(String giftShowImageUrl) {
                this.giftShowImageUrl = giftShowImageUrl;
            }

            public String getReceiveUserName() {
                return receiveUserName;
            }

            public void setReceiveUserName(String receiveUserName) {
                this.receiveUserName = receiveUserName;
            }

            public String getPlatformObjectType() {
                return platformObjectType;
            }

            public void setPlatformObjectType(String platformObjectType) {
                this.platformObjectType = platformObjectType;
            }

            public String getGiftEffectMemo() {
                return giftEffectMemo;
            }

            public void setGiftEffectMemo(String giftEffectMemo) {
                this.giftEffectMemo = giftEffectMemo;
            }

            public String getSendUserId() {
                return sendUserId;
            }

            public void setSendUserId(String sendUserId) {
                this.sendUserId = sendUserId;
            }

            public String getGiftEffectShowSceneType() {
                return giftEffectShowSceneType;
            }

            public void setGiftEffectShowSceneType(String giftEffectShowSceneType) {
                this.giftEffectShowSceneType = giftEffectShowSceneType;
            }

            public String getGiftConfigName() {
                return giftConfigName;
            }

            public void setGiftConfigName(String giftConfigName) {
                this.giftConfigName = giftConfigName;
            }

            public String getDoubleHit() {
                return doubleHit;
            }

            public void setDoubleHit(String doubleHit) {
                this.doubleHit = doubleHit;
            }

            public String getDoubleHitGroupNo() {
                return doubleHitGroupNo;
            }

            public void setDoubleHitGroupNo(String doubleHitGroupNo) {
                this.doubleHitGroupNo = doubleHitGroupNo;
            }

            public String getGiftEffectPropertyList() {
                return giftEffectPropertyList;
            }

            public void setGiftEffectPropertyList(String giftEffectPropertyList) {
                this.giftEffectPropertyList = giftEffectPropertyList;
            }
        }
    }

}
