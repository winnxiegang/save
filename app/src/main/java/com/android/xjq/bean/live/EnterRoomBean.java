package com.android.xjq.bean.live;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by zhouyi on 2017/4/1.
 */

public class EnterRoomBean {


    private int inChannelUsers;
    private int channelUserHeartbeatInterval;
    private String enterKey;
    private String liveUrl;
    private String groupId;
    private String avChatRoomId;
    //主播是否在推流
    private boolean anchorPushStream;
    //推流id
    private int pushStreamId;
    //赠送礼物请求合并时间
    private float giftPurchaseInterval;
    //分享链接
    private String webUrl;
    //赛事信息
    private JczqDataBean raceDataSimple;
    //是否投票(主客)
    private boolean haveVoted;
    //投票id
    private List<VoteItem> voteItem;
    //当前已选择主客队选项
    private String userVotedContent;
    //直播间嘉宾信息
    private List<ChannelUserBean> channelAreaUserInfoList;
    //主播信息
    private List<AnchorUserInfoClientSimple> anchorUserInfoList;
    //主主播userId
    private String masterAnchorUserId;
    //主主播
    @Expose
    private AnchorUserInfoClientSimple firstAnchorUserInfo;

    /**
     * 根据SeqenceNumbaer获取对应的voteItemId
     *
     * @param sequenceNum
     * @return
     */
    public int getVoteIdBySequence(int sequenceNum) {
        for (VoteItem item : voteItem) {
            if (sequenceNum == item.sequenceNumber) {
                return item.id;
            }
        }
        return 0;
    }

    //返回主主播信息
    public AnchorUserInfoClientSimple getFirstAnchorUserInfo() {
        if (firstAnchorUserInfo != null)
            return firstAnchorUserInfo;
        if (anchorUserInfoList != null && anchorUserInfoList.size() > 0) {
            for (AnchorUserInfoClientSimple anchorUserInfoBean : anchorUserInfoList) {
                //主主播
                if (anchorUserInfoBean.userAnchor) {
                    firstAnchorUserInfo = anchorUserInfoBean;
                }
            }
        }
        return firstAnchorUserInfo;
    }

    public List<AnchorUserInfoClientSimple> getAnchorUserInfoList() {
        return anchorUserInfoList;
    }

    public void setAnchorUserInfoList(List<AnchorUserInfoClientSimple> anchorUserInfoList) {
        this.anchorUserInfoList = anchorUserInfoList;
    }

    public List<ChannelUserBean> getChannelAreaUserInfoList() {
        return channelAreaUserInfoList;
    }

    public void setChannelAreaUserInfoList(List<ChannelUserBean> channelAreaUserInfoList) {
        this.channelAreaUserInfoList = channelAreaUserInfoList;
    }

    public String getMasterAnchorUserId() {
        return masterAnchorUserId;
    }

    public void setMasterAnchorUserId(String masterAnchorUserId) {
        this.masterAnchorUserId = masterAnchorUserId;
    }

    public int getPushStreamId() {
        return pushStreamId;
    }

    public void setPushStreamId(int pushStreamId) {
        this.pushStreamId = pushStreamId;
    }

    public JczqDataBean getRaceDataSimple() {
        return raceDataSimple;
    }

    public void setRaceDataSimple(JczqDataBean raceDataSimple) {
        this.raceDataSimple = raceDataSimple;
    }

    public String getUserVotedContent() {
        return userVotedContent;
    }

    public void setUserVotedContent(String userVotedContent) {
        this.userVotedContent = userVotedContent;
    }

    public boolean isHaveVoted() {
        return haveVoted;
    }

    public void setHaveVoted(boolean haveVoted) {
        this.haveVoted = haveVoted;
    }

    public List<VoteItem> getVoteItem() {
        return voteItem;
    }

    public void setVoteItem(List<VoteItem> voteItem) {
        this.voteItem = voteItem;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public float getGiftPurchaseInterval() {
        return giftPurchaseInterval;
    }

    public void setGiftPurchaseInterval(float giftPurchaseInterval) {
        this.giftPurchaseInterval = giftPurchaseInterval;
    }

    public int getInChannelUsers() {
        return inChannelUsers;
    }

    public void setInChannelUsers(int inChannelUsers) {
        this.inChannelUsers = inChannelUsers;
    }

    public int getChannelUserHeartbeatInterval() {
        return channelUserHeartbeatInterval;
    }

    public void setChannelUserHeartbeatInterval(int channelUserHeartbeatInterval) {
        this.channelUserHeartbeatInterval = channelUserHeartbeatInterval;
    }

    public String getEnterKey() {
        return enterKey;
    }

    public void setEnterKey(String enterKey) {
        this.enterKey = enterKey;
    }

    public boolean isAnchorPushStream() {
        return anchorPushStream;
    }

    public void setAnchorPushStream(boolean anchorPushStream) {
        this.anchorPushStream = anchorPushStream;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAvChatRoomId() {
        return avChatRoomId;
    }

    public void setAvChatRoomId(String avChatRoomId) {
        this.avChatRoomId = avChatRoomId;
    }

    public static class VoteItem {
        /**
         * id : 550557
         * voteId : 580293
         * content : 支持客队
         * code : REMAIN_NEUTRAL
         * gmtCreate : 2018-02-27 11:16:46
         * gmtModified : 2018-03-02 13:29:58
         * voteCount : 0
         * sequenceNumber : 2
         * enabled : true
         */

        public int id;
        public int voteId;
        public String content;
        public String code;
        public String gmtCreate;
        public String gmtModified;
        public int voteCount;
        public int sequenceNumber;
        public boolean enabled;

    }
}
