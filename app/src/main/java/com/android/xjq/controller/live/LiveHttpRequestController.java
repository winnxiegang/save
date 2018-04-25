package com.android.xjq.controller.live;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.coupon.FetCouponValidateUtils;
import com.android.banana.commlib.coupon.FetchCouponResultDialog;
import com.android.banana.commlib.coupon.OpenCouponDialog;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.ThirdWebActivity;
import com.android.xjq.bean.LivePkBean;
import com.android.xjq.bean.TreasureList;
import com.android.xjq.bean.draw.DrawIssueEntity;
import com.android.xjq.bean.live.ChannelInfoBean;
import com.android.xjq.bean.live.ChannelUserConfigBean;
import com.android.xjq.bean.live.EnterRoomBean;
import com.android.xjq.bean.live.MsgSendSuccessBean;
import com.android.xjq.bean.live.RecentlyPrizeRecord;
import com.android.xjq.bean.live.main.PrizeCoreInfoBean;
import com.android.xjq.bean.live.main.gift.GiftShowConfigBean;
import com.android.xjq.bean.medal.CurrentUserMedalDetail;
import com.android.xjq.bean.medal.UserFansMedalInfoEntity;
import com.android.xjq.dialog.ShengZhiWindowPop;
import com.android.xjq.dialog.live.AchievementEffectDialog;
import com.android.xjq.listener.live.OnMessageSendListener;
import com.android.xjq.listener.live.RocketFlyListener;
import com.android.xjq.liveanim.RocketWaterFireDialog;
import com.android.xjq.liveanim.TreasureBoxOpenDialog3;
import com.android.xjq.model.draw.DrawActivityType;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.model.medal.MedalOperateActionTypeEnum;
import com.android.xjq.model.medal.MedalTypeEnum;
import com.android.xjq.presenters.InitBusinessHelper;
import com.android.xjq.presenters.LoginHelper2;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.encrypt.DesedeCryptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_CHAT_CONFIG_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_VOTE;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_LIST_QUERY_BY_ROOM_ID;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_RELATION_MARKED_ATTITUDE_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_RELATION_ON_THE_WALL_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.FANS_RANK_MEDAL_TOTAL_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.GROUP_MESSAGE_SEND;
import static com.android.xjq.utils.XjqUrlEnum.PURCHASE_ORDER_CHEER_LIST_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.QCLOUD_TLS_USER_SIGN_ANONYMOUS_GET;
import static com.android.xjq.utils.XjqUrlEnum.QCLOUD_TLS_USER_SIGN_GET;
import static com.android.xjq.utils.XjqUrlEnum.QUERY_AVAILABLE_COUPON;
import static com.android.xjq.utils.XjqUrlEnum.USER_FANS_MEDAL_CURRENT_ADORN_INFO_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.USER_FANS_MEDAL_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.USER_MEDAL_OPERATE;

/**
 * Created by zhouyi on 2017/4/1.
 */

public class LiveHttpRequestController extends BaseLiveController<LiveActivity> implements OnHttpResponseListener {

    private String TAG = "LiveHttpRequestController";

    //用于其他的基础接口
    private HttpRequestHelper baseHttpRequestHelper = null;

    //用于每五秒一次的心跳接口
    private HttpRequestHelper heartBeatHttpRequestHelper = null;

    private String channelId;

    private String chatRoomId;

    private OnMessageSendListener mMessageSendListener;

    private boolean isFirstEnter;

    private String enterKey = null;

    private boolean mAnonymousEnter = false;

    private String prizeRecordTimestamp = "0";

    private Gson gson;
    //如果匿名登录,第一在首页时,由于网络原因登录失败,这里重新获取一波UUID去请求相应的接口
    private String anonymousIdentifier = null;
    private String raceId;
    private String raceType;
    private RocketFlyListener rocketFlyListener;
    private ShengZhiWindowPop shengZhiWindowPop;
    private boolean mIsLandscape = false;

    public LiveHttpRequestController(LiveActivity context, String channelId,
                                     OnMessageSendListener messageSendListener,
                                     RocketFlyListener rocketFlyListener) {
        super(context);
        this.channelId = channelId;
        CurLiveInfo.channelAreaId = Integer.valueOf(channelId);
        this.rocketFlyListener = rocketFlyListener;
        mMessageSendListener = messageSendListener;
        gson = new Gson();
        anonymousIdentifier = InitBusinessHelper.isHasInitApp() ?
                LoginInfoHelper.getInstance().getGuestIdentifier() : UUID.randomUUID().toString();
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public XjqRequestContainer enterChannel() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.ENTER_CHANNEL_AREA, true);
        map.put("channelAreaId", channelId);
        return map;
    }

    //成就查询
    public XjqRequestContainer achievementMedalQuery() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.NEWLY_ACQUIRED_ACHIEVEMENT_MEDAL_QUERY, true);
        return map;
    }


    public XjqRequestContainer setHeartBeat() {
        XjqRequestContainer map = null;
        if (mAnonymousEnter) {
            map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_ANONYMOUS_HEART_BEAT_SET, false);
            map.put("AnonymousId", LoginInfoHelper.getInstance().getGuestIdentifier());
        } else {
            map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_AREA_USER_HEART_BEAT_SET, true);
        }
        map.put("channelAreaId", channelId);
        map.put("enterKey", enterKey);
        return map;
    }

    public void startHeartBeatAndGetUserCount() {
        List<RequestContainer> list = new ArrayList<>();
        list.add(setHeartBeat());
        list.add(achievementMedalQuery());
        list.add(getChannelUserCountQueryByChannelId());
        list.add(getPkProgressingCount());
        // list.add(queryTheme());
        //查询主播是否在直播间
        // list.add(getAnchorInChannelInfo());
        list.add(queryCurrentDraw());
        heartBeatHttpRequestHelper.startRequest(list, false);
    }

    public void startGetPrizeRecord() {
        baseHttpRequestHelper.startRequest(getRecentlyPrizeRecord(), false);
    }

    public void startGetPkProgressAndTreasureBox() {
        List<RequestContainer> list = new ArrayList<>();
        list.add(getPKProgress(true));
        list.add(getTreasureBox());
        heartBeatHttpRequestHelper.addRequest(list);
    }

    public void queryCurrentPk() {
        baseHttpRequestHelper.startRequest(getPKProgress(true));
    }

    private RequestContainer getPkProgressingCount() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PROCESSING_GAME_QUERY, true);
        map.put("channelAreaId", channelId);
        return map;
    }

    private RequestContainer getTreasureBox() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.USER_PACKAGE_QUERY, true);
        map.put("sourceId", channelId);
        return map;
    }

    private XjqRequestContainer getPKProgress(boolean queryDetail) {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_AREA_TOP_PK_GAME_BOARD_QUERY, true);
        map.put("channelAreaId", channelId);
        map.put("queryDetail", queryDetail);
        return map;
    }

    public XjqRequestContainer getRecentlyPrizeRecord() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.GET_RECENTLY_PRIZE_RECORD, false);
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        }
        map.put("timestamp", prizeRecordTimestamp);
        return map;
    }

    public XjqRequestContainer getChannelUserCountQueryByChannelId() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_AREA_USER_QUERY_BY_CHANNEL_AREA_ID, true);
        map.put("channelAreaId", channelId);
        return map;
    }

    public void groupMessageSend(String text, String fontColor) {
        XjqRequestContainer map = new XjqRequestContainer(GROUP_MESSAGE_SEND, true);
        map.put("channelAreaId", channelId);
        map.put("groupId", chatRoomId);
        map.put("content", text);
        // map.put("fontColor", fontColor);
        baseHttpRequestHelper.startRequest(map, false);
    }

    public void chatConfigQuery() {
        baseHttpRequestHelper.startRequest(userChatConfigQuery(), false);
    }

    public XjqRequestContainer userChatConfigQuery() {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_AREA_USER_CHAT_CONFIG_QUERY, true);
        map.put("channelAreaId", channelId);
        return map;
    }


    /**
     * 子频道列表
     *
     * @param currentPage
     */
    public void getChannelListByRoomId(int currentPage) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_LIST_QUERY_BY_ROOM_ID, false);
        map.put("currentPage", currentPage + "");
        map.put("roomId", CurLiveInfo.getRoomId() + "");
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        }
        baseHttpRequestHelper.startRequest(map, false);
    }

    public void startRequest(boolean isFirstEnter) {
        this.isFirstEnter = isFirstEnter;
        ArrayList<RequestContainer> list = new ArrayList<>();
        //int size = 3;
        list.add(enterChannel());
        // list.add(new XjqRequestContainer(XjqUrlEnum.PRIZECORE_QUERY_ACTIVE_ISSUE_INFO, false));
        list.add(getGiftShowConfig());
        list.add(getCouponCount());
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            list.add(userChatConfigQuery());
            //list.add(getCurrentMedalInfo());
            //size = 4;
        }
        //如果之前app没有初始化,则这里需要重新请求接口获取最新的sign
        if (!InitBusinessHelper.isHasInitApp()) {
            XjqRequestContainer map;
            if (LoginInfoHelper.getInstance().getUserId() != null) {
                map = new XjqRequestContainer(QCLOUD_TLS_USER_SIGN_GET, true);
            } else {
                map = new XjqRequestContainer(QCLOUD_TLS_USER_SIGN_ANONYMOUS_GET, false);
            }
            map.put("identifier", anonymousIdentifier);
            map.addSubDependencyRequest(list);
            baseHttpRequestHelper.startRequest(map, false);
        } else {
            //httpRequestHelper.startRequest(list, size, true);
            baseHttpRequestHelper.startRequest(list, false);
        }
        //todo
        // baseHttpRequestHelper.startRequest(list, false);
    }

    private RequestContainer queryCurrentDraw() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CURRENT_ACTIVITY_AND_GMT_START_QUERY, true);
        map.put("channelAreaId", channelId);
        // map.setGenericClaz(DrawIssueEntity.class);
        return map;
    }

    //查询主播是否在房间内
    public void queryAnchorIsInChannel() {
        XjqRequestContainer map = getAnchorInChannelInfo();
        baseHttpRequestHelper.startRequest(map);
    }

    private XjqRequestContainer getAnchorInChannelInfo() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.ANCHOR_IN_CHANNEL_AREA_QUERY, true);
        map.put("channelAreaId", channelId);
        map.put("anchorUserId", CurLiveInfo.anchorUserId);
        return map;
    }

    public XjqRequestContainer getGiftShowConfig() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.GIFT_SHOW_CONFIG_QUERY, false);
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        }
        return map;
    }

    public void getPrizeCoreInfo() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PRIZECORE_QUERY_ACTIVE_ISSUE_INFO, true);
        baseHttpRequestHelper.startRequest(map, false);
    }

    public void getSeatInfoList(String channelId) {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.VIP_SEAT_LIST_QUERY, true);
        map.put("channelAreaId", channelId);
        baseHttpRequestHelper.startRequest(map, false);
    }

    public void fansMedalIntroduction() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.FANS_MEDAL_INTRODUCTION, false);
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        }
        baseHttpRequestHelper.startRequest(map, false);
    }

    /**
     * 得到可抢红包数
     */
    public void queryCouponAllowFetchCount() {
        XjqRequestContainer map = getCouponCount();
        baseHttpRequestHelper.startRequest(map, false);
    }

    private XjqRequestContainer getCouponCount() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.GET_ALLOW_FETCH_COUNT, true);
        map.put("platformObjectType", "CHANNEL_AREA");
        map.put("platformObjectId", channelId);
        return map;
    }

    //查询红包是否可抢
    public void queryAvailableCoupon(String couponUniqueId) {
        XjqRequestContainer map = new XjqRequestContainer(QUERY_AVAILABLE_COUPON, true);
        map.put("groupCouponId", couponUniqueId);
        baseHttpRequestHelper.startRequest(map, true);
    }

    public void queryCurrentMedalInfo() {
        baseHttpRequestHelper.startRequest(getCurrentMedalInfo(), false);
    }

    public XjqRequestContainer getCurrentMedalInfo() {
        XjqRequestContainer map = new XjqRequestContainer(USER_FANS_MEDAL_CURRENT_ADORN_INFO_QUERY, true);
        map.put("medalType", MedalTypeEnum.FAN_MEDAL.name());
        map.put("channelAreaId", channelId);
        return map;
    }

    public void getUserFansMedalQuery() {
        XjqRequestContainer map = new XjqRequestContainer(USER_FANS_MEDAL_QUERY, true);
        map.put("medalType", MedalTypeEnum.FAN_MEDAL.name());
        map.put("channelAreaId", channelId);
        map.put("queryUserCurrentAdronInfo", true);
        baseHttpRequestHelper.startRequest(map, false);
    }

    public void getRankedMedalList() {
        XjqRequestContainer map = new XjqRequestContainer(FANS_RANK_MEDAL_TOTAL_QUERY, true);
        map.put("service", "FANS_RANK_MEDAL_TOTAL_QUERY");
        map.put("channelAreaId", channelId);
        map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        baseHttpRequestHelper.startRequest(map, false);
    }

    public void operateMedal(String id) {
        XjqRequestContainer map = new XjqRequestContainer(USER_MEDAL_OPERATE, true);
        String action;
        if (!TextUtils.isEmpty(id)) {
            map.put("userMedalDetailId", id);
            action = MedalOperateActionTypeEnum.ADORN.name();
        } else {
            action = MedalOperateActionTypeEnum.CANCEL.name();
        }
        map.put("actionType", action);
        map.put("medalType", MedalTypeEnum.FAN_MEDAL.name());
        baseHttpRequestHelper.startRequest(map, false);
    }

    //榜单查询
    public void queryCheerList() {
        XjqRequestContainer map = new XjqRequestContainer(PURCHASE_ORDER_CHEER_LIST_QUERY, true);
        map.put("raceId", raceId);
        map.put("raceType", raceType);
        baseHttpRequestHelper.startRequest(map, false);
    }

    //上墙查询
    public void queryLiveWallList(int currentPage) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_RELATION_ON_THE_WALL_QUERY, true);
        map.put("channelAreaId", channelId);
        map.put("currentPage", String.valueOf(currentPage));
        map.put("pageSize", 20);
        baseHttpRequestHelper.startRequest(map);
    }

    //态度查询
    public void queryMarkedAttitude(int currentPage) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_RELATION_MARKED_ATTITUDE_QUERY, true);
        map.put("channelAreaId", channelId);
        map.put("currentPage", String.valueOf(currentPage));
        map.put("pageSize", 20);
        baseHttpRequestHelper.addRequest(map);
    }

    //投票选择队伍
    public void voteTeam(int id) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_AREA_USER_VOTE, true);
        map.put("voteItemId", id);
        baseHttpRequestHelper.startRequest(map, false);
    }

    public RequestContainer queryTheme() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.THEME_QUERY, true);
        map.put("objectType", "LIVE_ROOM");
        map.put("objectId", channelId);
        return map;
    }


    /**
     * 将你们登录改为实名登录
     */
    public void changeToUserLogin() {
        mAnonymousEnter = false;
    }

    @Override
    public void init(View view) {
        baseHttpRequestHelper = new HttpRequestHelper(context, this);

        heartBeatHttpRequestHelper = new HttpRequestHelper(context, this);
    }

    @Override
    void setView() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    RocketWaterFireDialog rocketWaterFireDialog;

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        if (context == null) {
            return;
        }
        XjqUrlEnum requestEnum = (XjqUrlEnum) requestContainer.getRequestEnum();
        switch (requestEnum) {
            case QCLOUD_TLS_USER_SIGN_GET:
                responseSuccessUserSignGet(jsonObject);
                break;
            case QCLOUD_TLS_USER_SIGN_ANONYMOUS_GET:
                responseSuccessUserSignAnonymousGet(jsonObject);
                break;
            case ENTER_CHANNEL_AREA:
                responseSuccessEnterChannel(jsonObject);
                break;
            case ANCHOR_IN_CHANNEL_AREA_QUERY:
                responseSuccessAnchorInChannel(jsonObject);
                break;
            case CHANNEL_INFO_QUERY:
                responseSuccessChannelInfoQuery(jsonObject);
                break;
            case GROUP_MESSAGE_SEND:
                responseSuccessGroupMessageSend(jsonObject);
                break;
            case CURRENT_ACTIVITY_AND_GMT_START_QUERY:
                responseSuccessDrawActivityQuery(jsonObject);
                break;
            case CHANNEL_LIST_QUERY_BY_ROOM_ID:
                break;
            case VIP_SEAT_LIST_QUERY:
                if (context.getLivePortraitController().getGuestController() != null)
                    context.getLivePortraitController().getGuestController().responseSuccessHttp(requestContainer, jsonObject);
                break;
            case PURCHASE_ORDER_CHEER_LIST_QUERY:
                if (context.getLivePortraitController().getCelebrityController() != null)
                    context.getLivePortraitController().getCelebrityController().responseSuccessHttp(requestContainer, jsonObject);
                break;
            case CHANNEL_RELATION_MARKED_ATTITUDE_QUERY:
                if (context.getLivePortraitController().getAttitudeController() != null)
                    context.getLivePortraitController().getAttitudeController().responseSuccessHttp(requestContainer, jsonObject);
                break;
            case CHANNEL_RELATION_ON_THE_WALL_QUERY:
                if (context.getLivePortraitController().getWallController() != null)
                    context.getLivePortraitController().getWallController().responseSuccessHttp(requestContainer, jsonObject);
                break;
            case PRIZECORE_QUERY_ACTIVE_ISSUE_INFO:
                responseSuccessPrizeCore(jsonObject);
                break;
            case CHANNEL_AREA_USER_QUERY_BY_CHANNEL_AREA_ID:
                responseSuccessChannelUserCount(jsonObject);
                break;
            case GET_RECENTLY_PRIZE_RECORD:
                responseSuccessGetRecentlyPrize(jsonObject);
                break;
            case CHANNEL_ANONYMOUS_HEART_BEAT_SET:
            case CHANNEL_AREA_USER_HEART_BEAT_SET:
                responseSuccessHeartBeat(jsonObject);
                break;
            case CHANNEL_AREA_USER_CHAT_CONFIG_QUERY:
                responseSuccessChatConfigQuery(jsonObject);
                break;
            case GIFT_SHOW_CONFIG_QUERY:
                responseSuccessGiftShowConfig(jsonObject);
                break;
            case GET_ALLOW_FETCH_COUNT:
                responseSuccessCouponAllowFetchCount(jsonObject);
                break;
            case QUERY_AVAILABLE_COUPON:
                responseSuccessCouponAvailable(jsonObject);
                break;
            case USER_FANS_MEDAL_CURRENT_ADORN_INFO_QUERY:
                responseSuccessCurrentMedalInfo(jsonObject);
                break;
            case USER_FANS_MEDAL_QUERY:
                responseSuccessUserMedalQuery(jsonObject);
                break;
            case USER_MEDAL_OPERATE:
                responseSuccessMedalOperate(jsonObject);
                break;
            case FANS_RANK_MEDAL_TOTAL_QUERY:
                if (context.getLivePortraitController().getCurrentPortraitController() == null)
                    return;
                context.getLivePortraitController().getCurrentPortraitController().responseSuccessHttp(requestContainer, jsonObject);
                break;
            case FANS_MEDAL_INTRODUCTION:
                responseSuccessFansMedalIntroduction(jsonObject);
                break;
            case THEME_QUERY:
                responseSuccessThemeQuery(jsonObject);
                break;
            case NEWLY_ACQUIRED_ACHIEVEMENT_MEDAL_QUERY:
                responseSuccessAchievementQuery(jsonObject);
                break;
            case CHANNEL_AREA_TOP_PK_GAME_BOARD_QUERY:
                responseSuccessPKProgress(requestContainer, jsonObject);
                break;
            case USER_PACKAGE_QUERY:
                responseSuccessTreasureBox(jsonObject);
                break;
            case PROCESSING_GAME_QUERY:
                responseSuccessProcessGame(jsonObject);
                break;
        }
    }

    private void responseSuccessProcessGame(JSONObject jsonObject) {
        try {
            int processingPkGameBoardCount = 0;
            if (jsonObject.has("processingPkGameBoardCount")) {
                processingPkGameBoardCount = jsonObject.getInt("processingPkGameBoardCount");
                context.getLivePortraitController().getMainController().setPkRedView(processingPkGameBoardCount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessTreasureBox(JSONObject jsonObject) {
        //TODO 弹出宝箱
        try {
            if (jsonObject.has("treasureChestList")) {
                JSONArray treasureChestList = jsonObject.getJSONArray("treasureChestList");
                ArrayList<TreasureList.Treasure> treasureList = new Gson().fromJson(treasureChestList.toString(),
                        new TypeToken<List<TreasureList.Treasure>>() {
                        }.getType());
                Log.i("treasureBox", " jsonObject = " + jsonObject.toString());
                if (treasureList != null && treasureList.size() > 0) {
                    Log.i("treasureBox", "jsonObject = >>>>>>>>>>>>" + jsonObject.toString());
                    TreasureBoxOpenDialog3.newInstance(treasureList).show(context.getSupportFragmentManager());
                }
            }
        } catch (JSONException e) {
            Log.i("treasureBox", " error " + e.toString());
            e.printStackTrace();
        }

    }

    private void responseSuccessPKProgress(RequestContainer requestContainer, JSONObject jsonObject) {
        LivePkBean livePkBean = new Gson().fromJson(jsonObject.toString(), LivePkBean.class);
        boolean queryDetail = requestContainer.getBoolean("queryDetail");
        if (queryDetail) {
            //详情面板数据
        } else {
            //进度条数据

        }
        if (livePkBean.pkGameBoard != null) {
            if (context.getLivePortraitController().getMainController() != null) {
                context.getLivePortraitController().getMainController().showPkData(livePkBean);
            }
            int leftMoney = livePkBean.pkGameBoard.getOptionOneEntry().getTotalMultiple();
            String leftUrl = livePkBean.pkGameBoard.getOptionOneEntry().getBetFormImageUrl();
            int rightMoney = livePkBean.pkGameBoard.getOptionTwoEntry().getTotalMultiple();
            String rightUrl = livePkBean.pkGameBoard.getOptionTwoEntry().getBetFormImageUrl();
            context.getLivePortraitController().refreshPKProgress(leftMoney, leftUrl, rightMoney, rightUrl);
        } else {
            context.getLivePortraitController().hidePKProgressBar();
        }

    }

    private void responseSuccessAchievementQuery(JSONObject jsonObject) {
        try {
            List<String> medalCodeList = null;
            if (jsonObject.has("medalCodeList")) {
                JSONArray medalCodeListJson = jsonObject.getJSONArray("medalCodeList");
                medalCodeList = new Gson().fromJson(medalCodeListJson.toString(), new TypeToken<List<String>>() {
                }.getType());
            }
            if (medalCodeList != null && medalCodeList.size() > 0) {
                for (String medalCode : medalCodeList) {
                    AchievementEffectDialog.newInstance(medalCode).show(context.getSupportFragmentManager());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //当前正在进行中的活动
    private List<DrawIssueEntity.IssueSimpleBean> currentActivityList = new ArrayList<>();


    private void responseSuccessDrawActivityQuery(JSONObject jsonObject) {
        DrawIssueEntity drawIssueEntity = gson.fromJson(jsonObject.toString(), DrawIssueEntity.class);
        if (drawIssueEntity.issueSimple != null && drawIssueEntity.issueSimple.size() > 0) {
            for (DrawIssueEntity.IssueSimpleBean issueSimpleBean : drawIssueEntity.issueSimple) {
                if (!currentActivityList.contains(issueSimpleBean)) {
                    currentActivityList.add(issueSimpleBean);
                    switch (issueSimpleBean.activityCode) {
                        case DrawActivityType.ROCKET:
                            // TODO: 2018/3/8 火箭活动
                            //context.setDrawViewShow(timeDiff, issueSimpleBean.drawStatus.getName(), issueSimpleBean.id);
                            if (rocketWaterFireDialog != null && rocketWaterFireDialog.getPopupWindow().isShowing())
                                return;
                            rocketWaterFireDialog = new RocketWaterFireDialog(context, issueSimpleBean.id, channelId, rocketFlyListener);
                            break;
                        case DrawActivityType.DECREE:
                            // TODO: 2018/3/8 圣旨到活动
                            long gmtEndDraw = TimeUtils.timeStrToLong(issueSimpleBean.gmtEndDraw);
                            long gmtStartDraw = TimeUtils.timeStrToLong(issueSimpleBean.gmtStartDraw);
                            long decreeCountDownTime = gmtEndDraw - gmtStartDraw;
                            long differSec = TimeUtils.getDiffSecTwoDateStr(issueSimpleBean.gmtEndDraw, issueSimpleBean.gmtStartDraw);
                            LogUtils.e("DECREE", "gmtEndDraw = " + gmtEndDraw + ", gmtStartDraw = " + gmtStartDraw + ", differSec = " + differSec);
                            shengZhiWindowPop = new ShengZhiWindowPop.Builder(context)
                                    .setTouchOutside(false).setReqIssueId(issueSimpleBean.id).setOrientation(mIsLandscape)
                                    .setCountDownTime(Math.abs(decreeCountDownTime))
                                    .setOnFinishListener(new ShengZhiWindowPop.onFinishListener() {
                                        @Override
                                        public void onOpenRedPacketMoney(String moneyType, double totalPrice, boolean success) {
                                            //TODO 弹出红包
                                            shengZhiWindowPop.dismiss();
                                            if (context == null) return;
                                            if (success) {
                                                new FetchCouponResultDialog(context, totalPrice, moneyType).show();
                                            } else {
                                                new FetchCouponResultDialog(context, "").show();
                                            }

                                        }
                                    }).create();
                            shengZhiWindowPop.getDialog().showAtLocation(context.findViewById(R.id.closeIv), Gravity.NO_GRAVITY, 0, 0);
                            break;
                    }
                }
                //long timeDiff = TimeUtils.dateSubDate(issueSimpleBean.gmtStartParticipate, drawIssueEntity.nowDate);
                switch (issueSimpleBean.activityCode) {
                    case DrawActivityType.LUCKY_DRAW:
                    case DrawActivityType.HAND_SPEED:
                        LogUtils.e("issueSimple", "--------" + issueSimpleBean.activityCode + "-------");
                        context.setDrawViewShow(issueSimpleBean, drawIssueEntity.nowDate, true);
                        break;
                }
            }
            //移除已经结束的活动
            Iterator<DrawIssueEntity.IssueSimpleBean> iterator = currentActivityList.iterator();
            while (iterator.hasNext()) {
                DrawIssueEntity.IssueSimpleBean issueSimpleBean = iterator.next();
                if (!drawIssueEntity.issueSimple.contains(issueSimpleBean)) {
                    LogUtils.e("issueSimple", "issueSimpleBean = " + issueSimpleBean.drawStatus.getName());
                    switch (issueSimpleBean.activityCode) {
                        case DrawActivityType.HAND_SPEED:
                        case DrawActivityType.LUCKY_DRAW:
                            context.setDrawViewShow(issueSimpleBean, drawIssueEntity.nowDate, false);
                    }
                    iterator.remove();
                }
            }
        } else {
            //所有活动都已结束
            currentActivityList.clear();
            //隐藏抽奖信息
            context.setDrawViewShow(null, null, false);
        }

    }

    private void responseSuccessAnchorInChannel(JSONObject jsonObject) {
            /*String newChannelId = null;
            if (jsonObject.has("newPushStreamId")) {
                newChannelId = jsonObject.getString("newPushStreamId");
            }*/
        try {
            boolean anchorInChannelArea = false;
            if (jsonObject.has("anchorInChannelArea")) {
                anchorInChannelArea = jsonObject.getBoolean("anchorInChannelArea");
            }
            if (anchorInChannelArea) {
                context.anchorInChannelPrepareLive();
            } else {
                context.setLiveRoomPause();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessThemeQuery(JSONObject jsonObject) {
        /*GroupChatTopic1 groupChatTopic1 = gson.fromJson(jsonObject.toString(), GroupChatTopic1.class);
        if (context.getLivePortraitController().getMainController() != null) {
            context.getLivePortraitController().getMainController().showRaceTheme(groupChatTopic1);
        }*/
    }

    private void responseSuccessFansMedalIntroduction(JSONObject jsonObject) {
        try {
            String detailUrl = jsonObject.getString("detailUrl");
            ThirdWebActivity.startThirdWebActivity(context, detailUrl, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessMedalOperate(JSONObject jsonObject) {
        LogUtils.e(TAG, "jsonObject=" + jsonObject.toString());
        //Toast.makeText(context, "佩戴成功", Toast.LENGTH_SHORT).show();
        queryCurrentMedalInfo();
    }

    private void responseSuccessUserMedalQuery(JSONObject jsonObject) {
        UserFansMedalInfoEntity userFansMedalInfoEntity = gson.fromJson(jsonObject.toString(), UserFansMedalInfoEntity.class);
        context.getInputCommentController().setUserMedalData(userFansMedalInfoEntity);
    }

    private void responseSuccessCurrentMedalInfo(JSONObject jsonObject) {
        CurrentUserMedalDetail currentUserMedalDetail = gson.fromJson(jsonObject.toString(), CurrentUserMedalDetail.class);
        context.getInputCommentController().setUserCurrentMedalInfo(currentUserMedalDetail);
    }

    private void responseSuccessUserSignAnonymousGet(JSONObject jsonObject) {
        JSONObject liveInfo = null;
        try {
            liveInfo = jsonObject.getJSONObject("liveInfo");
            int accountType = liveInfo.getInt("accountType");
            String appSdkId = liveInfo.getString("appSdkId");

            String identifier = liveInfo.getString("identifier");

            String userSig = liveInfo.getString("userSig");

            String decryptUserSign = new DesedeCryptor(LoginHelper2.KEY).decrypt(userSig);

            LoginInfoHelper.getInstance().setAccountNo(String.valueOf(accountType));
            LoginInfoHelper.getInstance().setAppId(appSdkId);

            LoginInfoHelper.getInstance().setGuestAppUserSign(decryptUserSign);
            LoginInfoHelper.getInstance().setGuestIdentifier(identifier);

            LoginInfoHelper.getInstance().writeAppInfoToCache();
            LoginInfoHelper.getInstance().writeGuestInfoToCache();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessUserSignGet(JSONObject jsonObject) {
        try {
            JSONObject liveInfo = jsonObject.getJSONObject("liveInfo");
            int accountType = liveInfo.getInt("accountType");
            String appSdkId = liveInfo.getString("appSdkId");
            String identifier = liveInfo.getString("identifier");
            String userSig = liveInfo.getString("userSig");

            String decryptUserSign = new DesedeCryptor(LoginInfoHelper.getInstance().getUserCryptKey()).decrypt(userSig);

            LoginInfoHelper.getInstance().setAccountNo(String.valueOf(accountType));
            LoginInfoHelper.getInstance().setAppId(appSdkId);

            LoginInfoHelper.getInstance().setUserAppUserSign(decryptUserSign);
            LoginInfoHelper.getInstance().setUserIdentifier(identifier);

            LoginInfoHelper.getInstance().writeAppInfoToCache();
            LoginInfoHelper.getInstance().writeUserInfoToCache();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void responseSuccessCouponAvailable(JSONObject jsonObject) {
        try {
            if (jsonObject.has("couponInfo")) {
                JSONObject couponInfo = jsonObject.getJSONObject("couponInfo");
                GroupCouponInfoBean couponBean = gson.fromJson(couponInfo.toString(), GroupCouponInfoBean.class);
                new OpenCouponDialog(context, couponBean).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessCouponAllowFetchCount(JSONObject jsonObject) {
        context.getFetchCouponController().setCouponView(jsonObject);
    }

    private void responseSuccessGiftShowConfig(JSONObject jsonObject) {
        GiftShowConfigBean giftShowConfigBean = gson.fromJson(jsonObject.toString(), GiftShowConfigBean.class);
        context.getGiftController().setGiftConfig(giftShowConfigBean);
        context.getPushMessageController().setGiftConfig(giftShowConfigBean);
    }

    private void responseSuccessGetRecentlyPrize(JSONObject jsonObject) {
        LogUtils.e("responseSuccessGetRecentlyPrize", "jsonObject=" + jsonObject.toString());
        RecentlyPrizeRecord recentlyPrizeRecord = gson.fromJson(jsonObject.toString(), RecentlyPrizeRecord.class);
        recentlyPrizeRecord.operatorData();
        if (!TextUtils.isEmpty(recentlyPrizeRecord.getPrizeRecordInfo())) {
            context.getLivePortraitController().getMainController().prizeCoreNoticeShow(recentlyPrizeRecord.getPrizeRecordInfo());
        }
        if (recentlyPrizeRecord.getIntervalTime() != 0) {
            context.fixQueryTime(recentlyPrizeRecord.getIntervalTime());
        }
        prizeRecordTimestamp = recentlyPrizeRecord.getTimestamp();
    }

    private void responseSuccessChatConfigQuery(JSONObject jsonObject) {
        ChannelUserConfigBean channelUserConfigBean = gson.fromJson(jsonObject.toString(), ChannelUserConfigBean.class);
        channelUserConfigBean.operatorData();
        context.getInputCommentController().setBaseConfig(channelUserConfigBean);

    }

    private void responseSuccessHeartBeat(JSONObject jsonObject) {

    }

    private void responseSuccessPrizeCore(JSONObject jsonObject) {
        PrizeCoreInfoBean prizeCoreInfoBean = gson.fromJson(jsonObject.toString(), PrizeCoreInfoBean.class);
        context.prizeInfoQueryResponse(prizeCoreInfoBean);
    }

    private void responseSuccessChannelUserCount(JSONObject jsonObject) {
        try {
            long userCount = 0;
            if (jsonObject.has("userCount")) {
                userCount = jsonObject.getLong("userCount");
            }
            context.changePeopleCount(userCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessGroupMessageSend(JSONObject jsonObject) {
        MsgSendSuccessBean msgSendSuccessBean = gson.fromJson(jsonObject.toString(), MsgSendSuccessBean.class);
        if (mMessageSendListener != null)
            mMessageSendListener.onMessageSendSuccess(msgSendSuccessBean.roleCodes, msgSendSuccessBean.fontColorList,
                    msgSendSuccessBean.userVotedContent, msgSendSuccessBean.getMedalInfoBeanList());
    }

    private void responseSuccessChannelInfoQuery(JSONObject jo) {
        ChannelInfoBean bean = gson.fromJson(jo.toString(), ChannelInfoBean.class);
        CurLiveInfo.setRoomId(bean.getChannelInfo().getRoomId());
        CurLiveInfo.setHostAvator(bean.getAnchorUserInfo().getUserLogoUrl());
        CurLiveInfo.setHostName(bean.getAnchorUserInfo().getLoginName());
        CurLiveInfo.setAnchorUserId(bean.getAnchorUserInfo().getUserId());
        CurLiveInfo.setNotice(bean.getChannelInfo().getNotice());
        CurLiveInfo.setTitle(bean.getChannelInfo().getChannelTitle());
        if (isFirstEnter) {
            context.startPlay();
        }
    }


    private void responseSuccessEnterChannel(JSONObject jo) {
        EnterRoomBean enterRoomBean = gson.fromJson(jo.toString(), EnterRoomBean.class);
        chatRoomId = enterRoomBean.getGroupId();
        enterKey = enterRoomBean.getEnterKey();
        JczqDataBean raceDataBean = enterRoomBean.getRaceDataSimple();
        raceId = raceDataBean.id;
        raceType = raceDataBean.raceType.getName();
        CurLiveInfo.setHostName(enterRoomBean.getFirstAnchorUserInfo().userName);
        CurLiveInfo.pushStreamId = enterRoomBean.getPushStreamId();
        CurLiveInfo.setHostAvator(enterRoomBean.getFirstAnchorUserInfo().userLogoUrl);
        CurLiveInfo.setAnchorUserId(enterRoomBean.getMasterAnchorUserId());
        CurLiveInfo.setJczqDataBean(raceDataBean);
        CurLiveInfo.setAvChatRoomId(enterRoomBean.getAvChatRoomId());
        CurLiveInfo.setGroupId(enterRoomBean.getGroupId());
        CurLiveInfo.setWebUrl(enterRoomBean.getWebUrl());
        CurLiveInfo.setEnterKey(enterRoomBean.getEnterKey());
        CurLiveInfo.setAnchorPushStream(enterRoomBean.isAnchorPushStream());
        CurLiveInfo.setGiftPurchaseInterval(enterRoomBean.getGiftPurchaseInterval());
        //设置节目关联赛事
        context.getLiveHeaderController().setMatchData(enterRoomBean.getRaceDataSimple());
        //设置主播view
        context.setAnchorView(enterRoomBean);
        //主播是否推流
        context.liveStateChange(enterRoomBean.isAnchorPushStream());
        //是否投票
        context.showVoteView(enterRoomBean.getUserVotedContent(), enterRoomBean);
        if (isFirstEnter) {
            context.startPlay();
        }
    }


    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            //抢红包错误处理
            if (((XjqUrlEnum) requestContainer.getRequestEnum()).equals(XjqUrlEnum.QUERY_AVAILABLE_COUPON)) {
                if (jsonObject.has("couponInfo")) {
                    JSONObject couponInf = jsonObject.getJSONObject("couponInfo");
                    GroupCouponInfoBean couponInfoBean = gson.fromJson(couponInf.toString(), GroupCouponInfoBean.class);
                    FetCouponValidateUtils fetCouponValidateUtils = new FetCouponValidateUtils(context, couponInfoBean);
                    fetCouponValidateUtils.handleFalse(jsonObject);
                }
                return;
            } else if (requestContainer.getRequestEnum().equals(XjqUrlEnum.ANCHOR_IN_CHANNEL_AREA_QUERY)) {
                context.setLiveRoomPause();
                return;
            }

            if (jsonObject.has("error")) {
                JSONObject error = null;
                error = jsonObject.getJSONObject("error");
                String name = error.getString("name");
                LogUtils.e(TAG, name + "=======" + "UserId=" + LoginInfoHelper.getInstance().getUserId());
                if ("USER_NOT_ENTERED".equals(name) || "CHANNEL_AREA_USER_ENTER_OTHER_CHANNEL".equals(name)) {
                    context.cancelHeartBeat();
//                    context.quitLiveByPurpose(true);
                    EventBus.getDefault().post(new EventBusMessage(EventBusMessage.CHANNEL_USER_ENTER_OTHER_CHANNEL));
                    return;
                }

                if ("USER_IN_BLACK_LIST".equals(name)) {
                    ShowSimpleMessageDialog dialog = new ShowSimpleMessageDialog(context, error.getString("message"), new OnMyClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.finish();
                        }
                    }, Gravity.CENTER);
                    return;
                }

                if ("USER_KICK_OUT_FROM_CHANNEL".equals(name)) {
                    context.cancelHeartBeat();
                    EventBus.getDefault().post(new EventBusMessage(EventBusMessage.USER_KICK_OUT_FROM_CHANNEL));
                    return;
                }

                if ("FIRST_CHAT_NEED_WAIT_AFTER_ENTER_CHANNEL".equals(name) || "USER_CHAT_INTERVAL_SECONDS_TOO_SHORT".equals(name)) {
                    int remainSeconds = 0;
                    if (jsonObject.has("remainSeconds")) {

                        remainSeconds = jsonObject.getInt("remainSeconds");
                    }
                    int intervalSeconds = 0;
                    if (jsonObject.has("intervalSeconds")) {

                        intervalSeconds = jsonObject.getInt("intervalSeconds");
                    }

                    if ("FIRST_CHAT_NEED_WAIT_AFTER_ENTER_CHANNEL".equals(name)) {
                        ToastUtil.showLong(XjqApplication.getContext(), "本频道限制用户进入后" + intervalSeconds + "秒可以发言,还剩" + remainSeconds + "秒");
                    } else if ("USER_CHAT_INTERVAL_SECONDS_TOO_SHORT".equals(name)) {
                        ToastUtil.showLong(XjqApplication.getContext(), "本频道限制聊天间隔" + intervalSeconds + "秒,还剩" + remainSeconds + "秒");
                    }
                    return;
                }
                if ("USER_LEVEL_CAN_NOT_CHAT".equals(name)) {
                    if (jsonObject.has("detailMessage")) {
                        String detailMessage = jsonObject.getString("detailMessage");

                        LibAppUtil.showTip(context, detailMessage);
                    }
                    return;
                }
                //直播间内被顶掉,需要停止轮询
                if ("USER_LOGIN_EXPIRED".equals(name) || "LOGIN_ELSEWHERE".equals(name)) {
                    context.cancelHeartBeat();
                    context.showReLoginDialog();
                    return;
                }

                if ("GAME_BOARD_NOT_EXISTS".equals(name)) {
                    return;
                }

            }
            context.operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rocketWaterFireDialog != null) rocketWaterFireDialog.onDestroy();
        if (baseHttpRequestHelper != null) baseHttpRequestHelper.onDestroy();
        if (heartBeatHttpRequestHelper != null) heartBeatHttpRequestHelper.onDestroy();
        rocketFlyListener = null;
    }

    public void setDeviceOrientation(boolean isLandscape) {
        mIsLandscape = isLandscape;
    }
}
