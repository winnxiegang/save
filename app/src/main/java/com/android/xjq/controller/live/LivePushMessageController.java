package com.android.xjq.controller.live;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.main.gift.GiftShowConfigBean;
import com.android.xjq.model.live.GiftShowSenseTypeEnum;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;
import com.android.xjq.utils.escapeUtils.StringEscapeUtils;
import com.android.xjq.utils.live.TencentMsgCacheUtils;

/**
 * 推送消息处理
 * Created by zhouyi on 2017/4/7.
 */

public class LivePushMessageController extends BaseLiveController<LiveActivity> implements TencentMsgCacheUtils.TimerSendReceiver {

    private GiftShowConfigBean giftShowConfigBean;
    private TencentMsgCacheUtils msgCacheUtils;

    public LivePushMessageController(LiveActivity context) {
        super(context);
    }

    public void addMessage(LiveCommentBean bean) {
        if (context == null) {
            return;
        }
        //如果推送的是自己的消息，则需要过滤掉
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            if (LoginInfoHelper.getInstance().getUserId().equals(bean.getSenderId()) &&
                    (LiveCommentMessageTypeEnum.safeValueOf(bean.getType()) == LiveCommentMessageTypeEnum.NORMAL ||
                            LiveCommentMessageTypeEnum.safeValueOf(bean.getType()) == LiveCommentMessageTypeEnum.COUPON_CREATE_SUCCESS_NOTICE_TEXT)) {
                return;
            }
        }
        msgCacheUtils.addMsg(bean);
    }

    /**
     * 礼物显示配置信息
     *
     * @param giftShowConfigBean
     */
    public void setGiftConfig(GiftShowConfigBean giftShowConfigBean) {
        this.giftShowConfigBean = giftShowConfigBean;
    }

    //自己发送的消息，添加到此处
    public void addMeMessage(LiveCommentBean liveCommentBean) {
        if (context == null) {
            return;
        }
        operateMessage(liveCommentBean);
    }

    private void operateMessage(LiveCommentBean liveCommentBean) {
        if (context == null) return;
        String type = liveCommentBean.getType();
        switch (LiveCommentMessageTypeEnum.safeValueOf(type)) {
            case USER_MEDAL_OVER_MAX_AWARD_NUM_NOTICE:
                medalOverMaxAwardHandle();
                break;
            case USER_MEDAL_AWARD_NOTICE:
                context.showMedalAwardAnim(liveCommentBean);
                break;
            case CHANNEL_PUSH_STREAM_STATUS_CHANGE:
                pushStreamStatusChange(liveCommentBean);
                break;
            case COUPON_CREATE_SUCCESS_NOTICE_TEXT:
                listViewShow(liveCommentBean);
                context.getLiveHttpRequestController().queryCouponAllowFetchCount();
                break;
            case CHANNEL_TRANSFER_NOTICE:
                context.showTransferNotice(liveCommentBean);
                break;
            case GIFTCORE_GIFT_GIVE_SUM_TEXT:
                break;
            case GIFTCORE_GIFT_GIVE_TEXT:
                operateGiftSend(liveCommentBean);
                break;
            case PRIZECORE_PRIZE_NOTICE_TEXT:
//                prizeCoreNoticeShow(liveCommentBean);
                break;
            case SEAT_LONG_SET_NOTICE:
                context.getLiveHttpRequestController().getSeatInfoList(context.getChannelId());
                break;
            case SPECIAL_EFFECT_ENTER_NOTICE:
                listViewShow(liveCommentBean);
                danMuShow(liveCommentBean);
                break;
            case NORMAL:
            case DECREE_USER_PRIZED_TEXT:
            case TEXT:
                liveCommentBean.getContent().setText(StringEscapeUtils.unescapeHtml(liveCommentBean.getContent().getText()));
                listViewShow(liveCommentBean);
                danMuShow(liveCommentBean);
                break;
            case LUCKYDRAW_DECREE_CREATE_TEXT:
                decreeEdictShow(liveCommentBean);
                break;
        }
    }

    private void decreeEdictShow(LiveCommentBean liveCommentBean) {
        String channelAreaId = liveCommentBean.getContent().getBody().getChannelAreaId();
        //过滤掉当前直播间的圣旨到消息
        if (TextUtils.equals(context.getChannelId(), channelAreaId)) return;
        context.getGiftController().addImperialEdict(liveCommentBean);
    }


    private void medalOverMaxAwardHandle() {
        Toast.makeText(context, context.getString(R.string.fans_medal_award_over_max_num), Toast.LENGTH_LONG).show();
    }

    private void pushStreamStatusChange(LiveCommentBean liveCommentBean) {
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        String pushStream = body.getPushStream();
        Boolean pushStreamStatus = Boolean.valueOf(pushStream);
        context.liveStateChange(pushStreamStatus);
    }

    private void prizeCoreNoticeShow(LiveCommentBean liveCommentBean) {
        context.getLivePortraitController().getMainController().prizeCoreNoticeShow(liveCommentBean);
    }

    private void operateGiftSend(LiveCommentBean liveCommentBean) {

        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();

        //判断礼物规则是否生效/单次礼物金额是否超过配置金额
        if (giftShowConfigBean != null) {
            boolean isEffect = giftShowConfigBean.judgeGiftRuleIsEffect(body.getGiftConfigCode());
            liveCommentBean.setRuleEffect(isEffect);
            if (isEffect) {
                boolean isShowFloatingLayer = giftShowConfigBean.judgeAmountIsEnough(body.getPayType(), body.getTotalAmount());
                liveCommentBean.setShowFloatingLayer(isShowFloatingLayer);
            }
        }
        //过滤掉圣旨到,火箭的推送
        if (TextUtils.equals("IMPERIAL_EDICT", body.getGiftConfigCode())
                || TextUtils.equals("ROCKET", body.getGiftConfigCode())) {
            return;
        }

        switch (GiftShowSenseTypeEnum.safeValueOf(body.getGiftEffectShowSceneType())) {
            case ALL_PLATFORM:
                allPlatFormShow(liveCommentBean);
                if (context != null && !liveCommentBean.getContent().getBody().getPlatformObjectId().equals(context.getChannelId()))
                    return;
                break;
            case CURRENT_PLATFORM:
                liveRoomShow(liveCommentBean);
                break;
            case LIVE_ROOM_SPECIAL:
                liveRoomSpecialShow(liveCommentBean);
                break;
            case COMMON:
                publicChatShow(liveCommentBean);
                break;
        }

        //如果是自己发送的礼物,则过滤掉
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            if (LoginInfoHelper.getInstance().getUserId().equals(liveCommentBean.getSenderId())) {
                return;
            }
        }
        //取消所有送礼记录在公屏显示
        // listViewShow(liveCommentBean);

    }

    public void allPlatFormShow(LiveCommentBean liveCommentBean) {
        context.getGiftController().addPlatformGiftShow(liveCommentBean);
    }

    public void liveRoomShow(LiveCommentBean liveCommentBean) {
        context.getGiftController().addChannelGiftShow(liveCommentBean);
    }

    public void liveRoomSpecialShow(LiveCommentBean liveCommentBean) {

    }

    public void publicChatShow(LiveCommentBean liveCommentBean) {
        danMuShow(liveCommentBean);
        //金额小于配置金额,连击不显示
        //if (liveCommentBean.isShowFloatingLayer()) return;
        if (Integer.parseInt(liveCommentBean.getContent().getBody().getDoubleHit()) >= 1) {
            context.getGiftController().addDoubleHit(liveCommentBean);
        }
    }

    /**
     * 聊天区显示
     */
    public void listViewShow(LiveCommentBean liveCommentBean) {
        context.getLivePortraitController().showMessage(liveCommentBean);
        context.getInputCommentController().showMessage(liveCommentBean);
    }

    public void danMuShow(LiveCommentBean liveCommentBean) {
        if (context.isLandscape()) {
            context.getLiveLandscapeController().showMessage(liveCommentBean);
        }
    }


    @Override
    public void init(View view) {
        msgCacheUtils = new TencentMsgCacheUtils();
        msgCacheUtils.setTimerSendReceiver(this);
        msgCacheUtils.onResume();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgCacheUtils != null) msgCacheUtils.onDestroy();
    }

    @Override
    public void pullMsg(LiveCommentBean bean) {
        operateMessage(bean);
    }
}
