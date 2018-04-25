package com.android.xjq.dialog.live;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.draw.DrawIssueEntity;
import com.android.xjq.bean.draw.DrawRoundInfoEntity;
import com.android.xjq.bean.draw.IssueStatusType;
import com.android.xjq.bean.draw.LiveDrawInfoEntity;
import com.android.xjq.bean.draw.LuckyDrawOpenUserEntity;
import com.android.xjq.bean.draw.LuckyDrawParticipateConditionSimpleBean;
import com.android.xjq.bean.draw.PrizedRecordEntity;
import com.android.xjq.bean.draw.RecentlyDrawIssueBean;
import com.android.xjq.controller.draw.DrawCallback;
import com.android.xjq.controller.draw.DrawCompleteController;
import com.android.xjq.controller.draw.DrawJoinController;
import com.android.xjq.controller.draw.DrawProgressingController;
import com.android.xjq.dialog.base.DialogBase;
import com.android.xjq.model.draw.DrawActivityType;
import com.android.xjq.model.gift.PayType;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.GetPollingResultUtil;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/8.
 */

public class LuckyDrawDialog extends DialogBase implements IHttpResponseListener, DrawCallback, View.OnClickListener {

    private CommonStatusLayout container;
    private WrapperHttpHelper httpHelper;
    private DrawJoinController drawController;
    private DrawProgressingController progressingController;
    private DrawCompleteController drawCompleteController;
    private String issueId = "";
    private GetPollingResultUtil queryCurrentPrizeUtil;
    //抽奖条件
    private LuckyDrawParticipateConditionSimpleBean drawParticipateBean;
    private String gmtEndParticipate;
    private boolean isStartDrawing;
    private TextView noPrizedView;
    //是否切换了礼金
    private boolean isChangeCoin;

    public LuckyDrawDialog(Context context, int orientation) {
        super(context, R.layout.dialog_lucky_draw, R.style.dialog_base, orientation);
        noPrizedView = ((TextView) findViewById(R.id.noPrizedView));
        container = (CommonStatusLayout) findViewById(R.id.container);
        findViewById(R.id.closeIv).setOnClickListener(this);
        httpHelper = new WrapperHttpHelper(this);
        queryRecentlyIssue();
    }

    public LuckyDrawDialog(Context context, int orientation, String issueId, String gmtEndParticipate) {
        super(context, R.layout.dialog_lucky_draw, R.style.dialog_base, orientation);
        this.issueId = issueId;
        this.gmtEndParticipate = gmtEndParticipate;
        noPrizedView = ((TextView) findViewById(R.id.noPrizedView));
        container = (CommonStatusLayout) findViewById(R.id.container);
        findViewById(R.id.closeIv).setOnClickListener(this);
        httpHelper = new WrapperHttpHelper(this);
       /* progressingController = new DrawProgressingController();
        progressingController.setContentView(container);*/
        createPrizedUtil();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.closeIv) {
            dismiss();
        }
    }

    private void queryRecentlyIssue() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.GET_RECENTLY_ISSUE, true);
        map.put("channelAreaId", CurLiveInfo.channelAreaId);
        map.put("activityCode", DrawActivityType.LUCKY_DRAW);
        map.setGenericClaz(RecentlyDrawIssueBean.class);
        httpHelper.startRequest(map);
    }

    //抽奖信息
    private void queryLuckyDrawInfo() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.LUCKY_DRAW_QUERY, true);
        map.put("issueId", issueId);
        map.setGenericClaz(LiveDrawInfoEntity.class);
        httpHelper.startRequest(map);
    }

    //参与抽奖
    @Override
    public void sendGift(int currentNum) {
        /*issueId(期次id),
        participateConditionId(参与条件id),
        participateValue(参与值, 例如点一次送1个礼物是1，点一次送五个是5，本次项目写死为1),
        totalAmount(总金额，参与值 * 每个礼物单价后的总金额)，
        currencyAppCode(货币应用代码, GIFTCOIN(礼金)，POINTCOIN(香蕉币)，GOLDCOIN(金锭)),
        channelAreaId：频道空间id*/
        if (drawParticipateBean == null) return;
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.LUCKY_DRAW_PARTICIPATE, true);
        map.put("issueId", issueId);
        map.put("participateConditionId", drawParticipateBean.id);
        map.put("participateValue", currentNum);
        map.put("totalAmount", drawParticipateBean.perPrice * currentNum);
        map.put("currencyType", drawParticipateBean.defaultCurrencyType);
        map.put("channelAreaId", CurLiveInfo.channelAreaId);
        httpHelper.startRequest(map, true);
    }

    //查询当前开奖状态的信息
    private void createPrizedUtil() {
        queryCurrentPrizeUtil = new GetPollingResultUtil(new GetPollingResultUtil.PollingCallback() {
            @Override
            public void onTick(WrapperHttpHelper httpHelper, int currentRequestCount) {
                RequestFormBody map;
                if (isStartDrawing) {
                    map = new RequestFormBody(XjqUrlEnum.DRAW_ROUND_INFO_QUERY, true);
                    map.put("issueId", issueId);
                    map.setGenericClaz(DrawRoundInfoEntity.class);
                } else {
                    map = new RequestFormBody(XjqUrlEnum.LUCKY_DRAW_OPEN_DRAW_QUERY, true);
                    map.put("issueId", issueId);
                    map.setGenericClaz(LuckyDrawOpenUserEntity.class);
                }
                httpHelper.startRequest(map, true);
            }
        }, this);
        queryCurrentPrizeUtil.startGetData();
    }

    //查询当前期次中奖信息
    private void queryIssuePrizedInfo() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.ACTIVITY_ISSUE_PRIZED_QUERY, true);
        map.put("issueId", issueId);
        map.put("channelAreaId", CurLiveInfo.channelAreaId);
        map.put("activityCode", DrawActivityType.LUCKY_DRAW);
        map.setGenericClaz(PrizedRecordEntity.class);
        httpHelper.startRequest(map);
    }

    private void responseSuccessRecentlyIssueQuery(RecentlyDrawIssueBean recentlyDrawIssueBean) {
        DrawIssueEntity.IssueSimpleBean issueSimple = recentlyDrawIssueBean.issueSimple;
        if (issueSimple != null) {
            issueId = issueSimple.id;
            String statusName = issueSimple.status.getName();
            if (TextUtils.equals(statusName, IssueStatusType.DRAWING)) {
                createPrizedUtil();
            } else if (TextUtils.equals(statusName, IssueStatusType.NORMAL)) {
                //queryLuckyDrawInfo();
                gmtEndParticipate = issueSimple.gmtEndParticipate;
                createPrizedUtil();
            } else {
                showCompletedView();
            }
        } else {
            container.hideStatusView();
            noPrizedView.setVisibility(View.VISIBLE);
        }
    }

    private void responseSuccessDrawQuery(LiveDrawInfoEntity liveDrawInfoEntity) {
        container.hideStatusView();
        if (drawController == null)
            drawController = new DrawJoinController(this);
        drawController.setContentView(container);
        drawController.setDrawBaseInfo(gmtEndParticipate, liveDrawInfoEntity);
        List<LuckyDrawParticipateConditionSimpleBean> drawParticipateSimpleBeanList = liveDrawInfoEntity.luckyDrawParticipateConditionSimple;
        if (drawParticipateSimpleBeanList != null && drawParticipateSimpleBeanList.size() > 0)
            drawParticipateBean = drawParticipateSimpleBeanList.get(0);

    }

    private void responseSuccessPrizedUserQuery(LuckyDrawOpenUserEntity luckyDrawOpenUserEntity) {
        String statusName = luckyDrawOpenUserEntity.issueStatus.getName();
        if (TextUtils.equals(statusName, IssueStatusType.DRAWING)) {
            progressingController = new DrawProgressingController();
            progressingController.setContentView(container);
            progressingController.setUserData(luckyDrawOpenUserEntity);
            //查询当前抽奖轮次的信息
            isStartDrawing = true;
        } else if (TextUtils.equals(statusName, IssueStatusType.FINISH)) {
            showCompletedView();
        } else if (TextUtils.equals(statusName, IssueStatusType.NORMAL) && drawController == null) {
            queryLuckyDrawInfo();
        }
    }

    @Override
    public void dismiss() {
        if (drawController != null) drawController.onDestroy();
        if (queryCurrentPrizeUtil != null) queryCurrentPrizeUtil.onDestroy();
        super.dismiss();
    }

    private void showCompletedView() {
        container.hideStatusView();
        if (drawCompleteController == null) {
            drawCompleteController = new DrawCompleteController();
        }
        drawCompleteController.setContentView(container);
        if (queryCurrentPrizeUtil != null) queryCurrentPrizeUtil.stop();
        queryIssuePrizedInfo();
    }

    private void responseSuccessRoundInfo(DrawRoundInfoEntity drawRoundInfoEntity) {
//        LogUtils.e("LuckyDraw", "抽奖轮次查询------" + drawRoundInfoEntity.currentRound.status.getName());
        if (drawRoundInfoEntity.allAllocated) {
            showCompletedView();
            return;
        }
        progressingController.setDrawRoundInfo(drawRoundInfoEntity);
    }

    private void responseSuccessPrizedIssueQuery(PrizedRecordEntity prizedRecordEntity) {
        drawCompleteController.setPrizedIssueData(prizedRecordEntity);
    }

    @Override
    public void onSuccess(RequestContainer request, Object obj) {
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case GET_RECENTLY_ISSUE:
                responseSuccessRecentlyIssueQuery((RecentlyDrawIssueBean) obj);
                break;
            case LUCKY_DRAW_QUERY:
                responseSuccessDrawQuery((LiveDrawInfoEntity) obj);
                break;
            case LUCKY_DRAW_PARTICIPATE:
                drawController.sendGiftSuccess();
                break;
            case LUCKY_DRAW_OPEN_DRAW_QUERY:
                responseSuccessPrizedUserQuery((LuckyDrawOpenUserEntity) obj);
                break;
            case DRAW_ROUND_INFO_QUERY:
                responseSuccessRoundInfo((DrawRoundInfoEntity) obj);
                break;
            case ACTIVITY_ISSUE_PRIZED_QUERY:
                responseSuccessPrizedIssueQuery((PrizedRecordEntity) obj);
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ErrorBean errorBean = new ErrorBean(jsonObject);
        if (errorBean != null && errorBean.getError() != null && errorBean.getError().getName().equals("AVAIABLE_AMOUNT_NOT_ENOUGH")) {
            showCoinNotEnoughDialog();
            return;
        }
        ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
    }

    private void showCoinNotEnoughDialog() {
        if (isChangeCoin) {
            ((BaseActivity) context).showRechargeView();
            return;
        }

        ShowMessageDialog dialog = new ShowMessageDialog(context, "是", "否", new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    drawParticipateBean.defaultCurrencyType = PayType.POINT_COIN.name();
                    drawParticipateBean.perPrice = drawParticipateBean.currencyTypeAndPrice.get(PayType.POINT_COIN.name());
                    isChangeCoin = true;
                } catch (Exception e) {
                }
            }
        }, null, "礼金不足，是否使用香蕉币支付？");

    }

}
