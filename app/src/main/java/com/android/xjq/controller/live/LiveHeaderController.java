package com.android.xjq.controller.live;

import android.view.View;
import android.widget.FrameLayout;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JclqDynamicData;
import com.android.banana.commlib.bean.liveScoreBean.JclqMatchLiveBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDynamicDataBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqMatchLiveBean;
import com.android.banana.commlib.bean.liveScoreBean.PathAnimLiveSourceBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.liveScore.JczqAnalysisGetLiveMatchData;
import com.android.banana.commlib.liveScore.LiveScoreController;
import com.android.banana.commlib.liveScore.PathAnimHeaderController;
import com.android.banana.commlib.liveScore.livescoreEnum.BtRaceStatusEnum;
import com.android.banana.commlib.liveScore.livescoreEnum.LiveScoreUrlEnum;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.residemenu.lt_lib.enumdata.FtRaceStatusEnum;
import com.android.xjq.utils.GetPollingResultUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.banana.commlib.liveScore.livescoreEnum.LiveScoreUrlEnum.CHANNEL_AREA_CHEER_GAME_BOARD_QUERY;

/**
 * Created by lingjiu on 2018/1/31.
 */

public class LiveHeaderController extends BaseLiveController implements IHttpResponseListener {
    public static final int STATUS_SCORE_LIVING = 1;
    public static final int STATUS_SHOW_FT_ANIM_LIVING = 2;
    public static final int STATUS_SHOW_CHEER = 3;
    private FrameLayout container;
    //助威控制器
    private CheerController cheerController;
    //动画直播控制器
    private PathAnimHeaderController mAnimHeaderController;
    //比分直播控制器
    private LiveScoreController liveScoreController;
    private JczqDataBean mRaceBean;
    private String mInnerRaceId;
    //是否是足球
    private boolean isFootball = false;
    /*动画直播需要*/
    private int curTabPosition;
    private List<PathAnimLiveSourceBean> liveSourceList;
    private List<CharSequence> liveCharList;
    private int mReplyCount;//评论数
    private JczqAnalysisGetLiveMatchData mGetLiveMatch;
    private GetPollingResultUtil cheerPollingUtil;
    //当前状态
    private int currentStatus = STATUS_SCORE_LIVING;
    //直播间默认只有一条
    private boolean isShowSingleCheer;

    public LiveHeaderController(BaseActivity context) {
        super(context);
    }

    @Override
    public void init(View view) {
        container = ((FrameLayout) view);
        setupController();
    }

    private void setupController() {
        cheerController = new CheerController();
        cheerController.setContentView(container);

        liveScoreController = new LiveScoreController();
        liveScoreController.setContentView(container);
    }

    @Override
    public void setView() {

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
        if (mAnimHeaderController != null)
            mAnimHeaderController.onDestroy();
        liveScoreController.onDestroy();
        if (cheerPollingUtil != null)
            cheerPollingUtil.onDestroy();
        if (mGetLiveMatch != null)
            mGetLiveMatch.onDestroy();
    }


    public void setShowSingleCheer(boolean showSingleCheer) {
        isShowSingleCheer = showSingleCheer;
    }

    /**
     * 设置比赛信息
     *
     * @param raceBean
     */
    public void setMatchData(JczqDataBean raceBean) {
        mRaceBean = raceBean;
        mInnerRaceId = mRaceBean.getInnerRaceId();
        isFootball = mRaceBean.isFootball();
        LogUtils.e("kk", mRaceBean.raceType.getName() + isFootball + "");
        liveScoreController.setMatchData(isFootball, raceBean);
        createGetLiveData(mRaceBean);

        // TODO: 2018/3/6  助威
     /*   RequestFormBody map = new RequestFormBody(LiveScoreUrlEnum.CHANNEL_AREA_CHEER_GAME_BOARD_QUERY, true);
        map.put("raceId", mRaceBean.id);
        map.put("raceType", isFootball ? "FOOTBALL" : "BASKETBALL");
        map.setGenericClaz(GroupChatTopic1.class);
        cheerPollingUtil = new GetPollingResultUtil(map, this);
        cheerPollingUtil.startGetData();
        cheerController.setMatchData(isFootball,mRaceBean);*/
    }

    /**
     * 控制头部是否隐藏或者显示比分直播/动画直播
     *
     * @param isShowLiveMatch
     */
    public void controlPollMatchLive(boolean isShowLiveMatch) {
        if (isShowLiveMatch) {
            currentStatus = STATUS_SCORE_LIVING;
            createGetLiveData(mRaceBean);
            liveScoreController.showContentView(false);
        } else {
            if (mGetLiveMatch != null) mGetLiveMatch.stop();
            if (cheerPollingUtil != null) cheerPollingUtil.stop();
            liveScoreController.getContentView().setVisibility(View.GONE);
            if (mAnimHeaderController != null)
                mAnimHeaderController.getContentView().setVisibility(View.GONE);
            if (cheerController != null)
                cheerController.getContentView().setVisibility(View.GONE);
        }
    }

    private boolean getMatchIsFinish(String statusName) {
        return FtRaceStatusEnum.FINISH.name().equals(statusName);
    }

    private void createGetLiveData(JczqDataBean bean) {
        NormalObject status = bean.getRaceStatus();
        if (status != null) {
            String statusName = status.getName();
            //是否完场
            if (!getMatchIsFinish(statusName)) {
                if (mGetLiveMatch == null)
                    mGetLiveMatch = new JczqAnalysisGetLiveMatchData(isFootball, mInnerRaceId, this);
                mGetLiveMatch.startGetData();
            }
        }
    }

    private void responseSuccessFootballLiveQuery(JczqMatchLiveBean bean) {
        if (bean.getDynamicDataList().size() == 1) {
            JczqDynamicDataBean liveBean = bean.getDynamicDataList().get(0);
            switch (com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum.saveValueOf(liveBean.getS().getName())) {
                case WAIT:
                    if (currentStatus != STATUS_SHOW_CHEER && cheerPollingUtil == null) {
                        createCheerPollUtil();
                        cheerController.setMatchData(isFootball, mRaceBean);
                    }
                    break;
                case PLAY_F://上半场
                case PLAY_S://下半场
                case OVERTIME://加时
                case PAUSE://中场
                {
                    //停止助威显示,直播比分,有动画直播显示动画直播
                    if (currentStatus == STATUS_SHOW_CHEER && cheerPollingUtil != null) {
                        cheerPollingUtil.stop();
                        liveScoreController.showContentView(false);
                        //cheerController.getContentView().setVisibility(View.GONE);
                    }

                    if (currentStatus != STATUS_SHOW_FT_ANIM_LIVING) {
                        if (mAnimHeaderController == null) {
                            mAnimHeaderController = new PathAnimHeaderController(String.valueOf(mInnerRaceId), mRaceBean);
                        }
                        mAnimHeaderController.setContentView(container);
                        mAnimHeaderController.setFullScore(String.valueOf(liveBean.getHs()) + ":" + String.valueOf(liveBean.getGs()));
                        currentStatus = STATUS_SHOW_FT_ANIM_LIVING;
                    }

                }
                break;
                case FINISH: {
                    mGetLiveMatch.stop();
                    //停止助威显示,直播比分
                    if (cheerPollingUtil != null) {
                        cheerPollingUtil.stop();
                        liveScoreController.showContentView(false);
                    }
                }
                break;
                default: {
                }
                break;
            }
        }
        liveScoreController.setFTLiveScoreData(bean);
    }

    private void createCheerPollUtil() {
        cheerPollingUtil = new GetPollingResultUtil(new GetPollingResultUtil.PollingCallback() {
            @Override
            public void onTick(WrapperHttpHelper httpHelper, int currentRequestCount) {
                RequestFormBody map = new RequestFormBody(CHANNEL_AREA_CHEER_GAME_BOARD_QUERY, true);
                map.put("raceId", mRaceBean.id);
                map.put("raceType", isFootball ? "FOOTBALL" : "BASKETBALL");
                map.setGenericClaz(GroupChatTopic1.class);
                httpHelper.startRequest(map);

                LogUtils.e("GetPollingResultUtil", "CHANNEL_AREA_CHEER_GAME_BOARD_QUERY");
            }
        }, this);
        cheerPollingUtil.startGetData();
    }


    private void responseSuccessCheerQuery(GroupChatTopic1 chatTopic) {
        //直播间内头部的助威
        if (isShowSingleCheer) {
            if (chatTopic.gameBoardList == null)
                chatTopic.gameBoardList = new ArrayList<>();
            chatTopic.gameBoardList.clear();
            if (chatTopic.defaultGameBoard != null)
                chatTopic.gameBoardList.add(chatTopic.defaultGameBoard);
        }
        if (((chatTopic.gameBoardList != null && chatTopic.gameBoardList.size() > 0) || chatTopic.defaultGameBoard != null)
                && currentStatus != STATUS_SHOW_CHEER) {
            currentStatus = STATUS_SHOW_CHEER;
            cheerController.showContentView(false);
        }
        cheerController.setTopicData(chatTopic);
    }

    private void responseSuccessBasketBallLiveQuery(JclqMatchLiveBean bean) {
        if (bean.getDynamicDataList().size() == 1) {
            JclqDynamicData data = bean.getDynamicDataList().get(0);
            switch (BtRaceStatusEnum.valueOf(data.getStatus().getName())) {
                case WAIT:
                    if (currentStatus != STATUS_SHOW_CHEER && cheerPollingUtil == null) {
                        createCheerPollUtil();
                        cheerController.setMatchData(isFootball, mRaceBean);
                    }
                    break;
                case PLAY_OT_4://第4'OT
                case PLAY_OT_3://第3'OT
                case PLAY_OT_2://第2'OT
                case PLAY_OT_1://第1'OT
                case PLAY_4://第4节
                case PLAY_3://第3节
                case PLAY_2://第2节
                case PLAY_1://第1节
                case PLAY_F://上半场
                case PLAY_S://下半场
                case OVERTIME://加时
                case PAUSE://中场
                    //停止助威显示显示比分直播
                    if (currentStatus == STATUS_SHOW_CHEER && cheerPollingUtil != null) {
                        cheerPollingUtil.stop();
                        liveScoreController.showContentView(false);
                        //cheerController.getContentView().setVisibility(View.GONE);
                    }
                    break;
                case FINISH: {
                    mGetLiveMatch.stop();
                    //停止助威显示,直播比分
                    if (cheerPollingUtil != null) {
                        cheerPollingUtil.stop();
                        liveScoreController.showContentView(false);
                    }
                }
                break;
            }
        }
        liveScoreController.setBTLiveScoreData(bean);
    }

    @Override
    public void onSuccess(RequestContainer request, Object obj) {
        switch (((LiveScoreUrlEnum) request.getRequestEnum())) {
            case DYNAMIC_DATA_QUERY:
                responseSuccessFootballLiveQuery(((JczqMatchLiveBean) obj));
                break;
            case DYNAMIC_SCORE_DATA:
                responseSuccessBasketBallLiveQuery(((JclqMatchLiveBean) obj));
                break;
            case CHANNEL_AREA_CHEER_GAME_BOARD_QUERY:
                responseSuccessCheerQuery(((GroupChatTopic1) obj));
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ErrorBean errorBean = new ErrorBean(jsonObject);
        if (errorBean != null && errorBean.error != null && errorBean.error.getName().equals("GAME_BOARD_NOT_EXISTS"))
            return;
        context.operateErrorResponseMessage(jsonObject);
    }

}
