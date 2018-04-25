package com.android.xjq.model.live;


import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;

/**
 * 当前直播信息页面
 */
public class CurLiveInfo {
    private static int members;
    private static int admires;
    private static String title;
    private static double lat1;
    private static double long1;
    private static String address = "";
    private static String coverurl = "";

    //推流id
    public static int pushStreamId;
    //节目id
    public static int channelAreaId;

    /**
     * 频道id
     */
    public static int roomNum;

    public static String hostID;
    public static String hostName;
    public static String hostAvator;
    public static int currentRequestCount = 0;
    private static String avChatRoomId = null;
    /**
     * 主播userId
     */
    public static String anchorUserId;

    private static String enterKey;

    //与直播间关联的赛事信息
    public static JczqDataBean jczqDataBean;

    /**
     * 频道公告
     */
    private static String notice;

    private static String groupId = "";

    /**
     * 主播是否推流
     */
    private static boolean isAnchorPushStream;

    /**
     * roomId
     */
    private static int roomId;

    /**
     * 礼物赠送合并请求时间
     */
    private static float giftPurchaseInterval;

    /**
     * 分享链接
     */
    private static String webUrl;

    public static void setJczqDataBean(JczqDataBean jczqDataBean) {
        CurLiveInfo.jczqDataBean = jczqDataBean;
    }

    public static String getWebUrl() {
        return webUrl;
    }

    public static void setWebUrl(String webUrl) {
        CurLiveInfo.webUrl = webUrl;
    }

    public static float getGiftPurchaseInterval() {
        return giftPurchaseInterval;
    }

    public static void setGiftPurchaseInterval(float giftPurchaseInterval) {
        CurLiveInfo.giftPurchaseInterval = giftPurchaseInterval;
    }

    public static boolean isAnchorPushStream() {
        return isAnchorPushStream;
    }

    public static void setAnchorPushStream(boolean anchorPushStream) {
        isAnchorPushStream = anchorPushStream;
    }

    public static int getRoomId() {
        return roomId;
    }

    public static void setRoomId(int roomId) {
        CurLiveInfo.roomId = roomId;
    }

    public static void setNotice(String notice) {
        CurLiveInfo.notice = notice;
    }

    public static String getNotice() {
        return notice;
    }

    public static String getEnterKey() {
        return enterKey;
    }

    public static void setEnterKey(String enterKey) {
        CurLiveInfo.enterKey = enterKey;
    }

    public static String getAnchorUserId() {
        return anchorUserId;
    }

    public static void setAnchorUserId(String anchorUserId) {
        CurLiveInfo.anchorUserId = anchorUserId;
    }

    public static int getCurrentRequestCount() {
        return currentRequestCount;
    }

    public static int getIndexView() {
        return indexView;
    }

    public static void setIndexView(int indexView) {
        CurLiveInfo.indexView = indexView;
    }

    public static int indexView = 0;

    public static void setCurrentRequestCount(int currentRequestCount) {
        CurLiveInfo.currentRequestCount = currentRequestCount;
    }

    public static String getHostID() {
        return hostID;
    }

    public static void setHostID(String hostID) {
        CurLiveInfo.hostID = hostID;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        CurLiveInfo.hostName = hostName;
    }

    public static String getHostAvator() {
        return hostAvator;
    }

    public static void setHostAvator(String hostAvator) {
        CurLiveInfo.hostAvator = hostAvator;
    }

    public static int getMembers() {
        return members;
    }

    public static void setMembers(int members) {
        CurLiveInfo.members = members;
    }

    public static int getAdmires() {
        return admires;
    }

    public static void setAdmires(int admires) {
        CurLiveInfo.admires = admires;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        CurLiveInfo.title = title;
    }

    public static double getLat1() {
        return lat1;
    }

    public static void setLat1(double lat1) {
        CurLiveInfo.lat1 = lat1;
    }

    public static double getLong1() {
        return long1;
    }

    public static void setLong1(double long1) {
        CurLiveInfo.long1 = long1;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        CurLiveInfo.address = address;
    }

    public static int getRoomNum() {
        return roomNum;
    }

    public static void setRoomNum(int roomNum) {
        CurLiveInfo.roomNum = roomNum;
    }

    public static String getCoverurl() {
        return coverurl;
    }

    public static void setCoverurl(String coverurl) {
        CurLiveInfo.coverurl = coverurl;
    }

    public static String getChatRoomId() {
        return "" + roomNum;
    }

    public static String getAvChatRoomId() {
        return avChatRoomId;
    }

    public static void setAvChatRoomId(String avChatRoomId) {
        CurLiveInfo.avChatRoomId = avChatRoomId;
    }

    public static String getGroupId() {
        return groupId;
    }

    public static void setGroupId(String groupId) {
        CurLiveInfo.groupId = groupId;
    }
}
