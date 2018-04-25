package com.android.xjq.dialog.live;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.commlib.view.VerticalScrollTextView2;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.draw.DrawPrizedUserBean;
import com.android.xjq.bean.draw.IssuePrizeItemListBean;
import com.android.xjq.bean.draw.LiveDrawInfoEntity;
import com.android.xjq.bean.draw.LuckyDrawParticipateConditionSimpleBean;
import com.android.xjq.bean.draw.PrizedRecordEntity;
import com.android.xjq.dialog.ShareGroupListDialogFragment;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.model.draw.DrawActivityType;
import com.android.xjq.model.draw.DrawStatusType;
import com.android.xjq.model.gift.PayType;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.GetPollingResultUtil;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.live.SpannableStringHelper;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2018/3/6.
 */

public class LimitHandSpeedDialog extends BaseDialog implements IHttpResponseListener {

    @BindView(R.id.prizeShowTv)
    VerticalScrollTextView2 prizeShowTv;
    @BindView(R.id.hostPortraitIv)
    CircleImageView hostPortraitIv;
    @BindView(R.id.hostNameTv)
    TextView hostNameTv;
    @BindView(R.id.hostInfoLayout)
    LinearLayout hostInfoLayout;
    @BindView(R.id.prizeNameTv)
    TextView prizeNameTv;
    @BindView(R.id.prizeNumTv)
    TextView prizeNumTv;
    @BindView(R.id.prizeInfoLayout)
    FrameLayout prizeInfoLayout;
    @BindView(R.id.selfSendNumTv)
    TextView selfSendNumTv;
    @BindView(R.id.giftIv)
    CircleImageView giftIv;
    @BindView(R.id.sendGiftBtn)
    FrameLayout sendGiftBtn;
    @BindView(R.id.gmtStartTv)
    CountdownTextView gmtStartTv;
    @BindView(R.id.shareIv)
    ImageView shareIv;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.memoTv)
    TextView memoTv;
    @BindView(R.id.noPrizedView)
    View noPrizedView;
    @BindView(R.id.prizedTitleTv)
    TextView prizedTitleTv;
    @BindView(R.id.closeIv)
    ImageView closeIv;
    @BindView(R.id.medalLayout)
    MedalLayout medalLayout;
    private WrapperHttpHelper httpHelper;
    //当前参与抽奖条件信息
    private LuckyDrawParticipateConditionSimpleBean drawParticipateBean;
    private GetPollingResultUtil queryCurrentPrizeUtil;
    //当前参与个数
    private int userParticipateCount;
    private String drawStatus;
    private long timeDiff;
    private String issueId = "";
    private boolean isChangeCoin;
    private Animation btnAnim;

    @OnClick({R.id.shareIv, R.id.sendGiftBtn, R.id.closeIv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareIv:
                ShareGroupListDialogFragment.newInstance(true, issueId, "HAND_SPEED", "实力&运气,来和我一起来极限手速")
                        .show(((BaseActivity) mContext).getSupportFragmentManager());
                break;
            case R.id.sendGiftBtn:
                joinGame();
                break;
            case R.id.closeIv:
                dismiss();
                break;
        }
    }

    public LimitHandSpeedDialog(Context context, String issueId, String drawStatus, long timeDiff) {
        super(context);
        this.issueId = issueId;
        this.drawStatus = drawStatus;
        this.timeDiff = timeDiff;
        setMargin(20);
        setDimAmount(0.0f);
        setVerticalMargin(150);
        setShowBottom(true);
        httpHelper = new WrapperHttpHelper(this);
        // getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA);
        //getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_limit_hand_speed_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnAnim = AnimationUtils.loadAnimation(getContext(), com.android.banana.R.anim.rocket_button_click);
        createPrizedUtil();
        if (TextUtils.equals(drawStatus, DrawStatusType.FINISH)) {
            //drawIssueEntity.nowDate;
            animSwitchShowHistoryRecord();
        } else if (TextUtils.equals(drawStatus, DrawStatusType.WAIT_DRAW)) {
            startAnimToJoin();
            queryCurrentPrizeUtil.startGetData();
        } else if (TextUtils.equals(drawStatus, DrawStatusType.INIT)) {
            gmtStartTv.start(timeDiff);
        }
        gmtStartTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                gmtStartTv.setVisibility(View.VISIBLE);
                gmtStartTv.setText(stringFormat(countdownTime));
            }

            @Override
            public void countdownEnd() {
                startAnimToJoin();
                queryCurrentPrizeUtil.startGetData();
            }
        });
        queryHandSpeedInfo();
    }

    private void animSwitchShowHistoryRecord() {
        shareIv.setVisibility(View.GONE);
        gmtStartTv.setVisibility(View.GONE);
        prizeShowTv.setVisibility(View.GONE);
        sendGiftBtn.setVisibility(View.VISIBLE);
        selfSendNumTv.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        prizeShowTv.stopScroll();
        memoTv.animate().alpha(0).setDuration(1000).start();
        selfSendNumTv.animate().alpha(0).setDuration(1000).start();
        sendGiftBtn.animate().alpha(0).setDuration(1000).start();
        listView.animate().alpha(1).setDuration(1500).start();
        prizeInfoLayout.animate().scaleX(0.7f).scaleY(0.7f).setDuration(1500).setDuration(1500).start();
    }

    private void startAnimToJoin() {
        shareIv.setVisibility(View.GONE);
        sendGiftBtn.setVisibility(View.VISIBLE);
        selfSendNumTv.setVisibility(View.VISIBLE);
        sendGiftBtn.animate().setDuration(1000).alpha(1);
        selfSendNumTv.animate().setDuration(1000).alpha(1);
        gmtStartTv.animate().setDuration(1000).alpha(0);
    }

    //查询当前中奖的信息
    private void createPrizedUtil() {
        queryCurrentPrizeUtil = new GetPollingResultUtil(new GetPollingResultUtil.PollingCallback() {
            @Override
            public void onTick(WrapperHttpHelper httpHelper, int currentRequestCount) {
                RequestFormBody map = new RequestFormBody(XjqUrlEnum.HAND_SPEED_USER_PRIZED_QUERY, true);
                map.put("issueId", issueId);
                map.setGenericClaz(DrawPrizedUserBean.class);
                httpHelper.startRequest(map, true);
            }
        }, this);
    }

    //抽奖信息
    private void queryHandSpeedInfo() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.HAND_SPEED_QUERY, true);
        map.put("issueId", issueId);
        map.setGenericClaz(LiveDrawInfoEntity.class);
        httpHelper.startRequest(map, true);
    }


    //参与抽奖
    private void joinGame() {
        sendGiftBtn.clearAnimation();
        sendGiftBtn.startAnimation(btnAnim);

        if (drawParticipateBean == null) return;
        /*issueId(期次id),
        participateConditionId(参与条件id),
        participateValue(参与值, 例如点一次送1个礼物是1，点一次送五个是5，本次项目写死为1),
        totalAmount(总金额，参与值 * 每个礼物单价后的总金额)，
        currencyType(货币应用代码, GIFTCOIN(礼金)，POINTCOIN(香蕉币)，GOLDCOIN(金锭)),
        channelAreaId：频道空间id*/
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.HAND_SPEED_PARTICIPATE, true);
        map.put("issueId", issueId);
        map.put("participateConditionId", drawParticipateBean.id);
        map.put("participateValue", "1");
        map.put("totalAmount", drawParticipateBean.perPrice);
        map.put("currencyType", drawParticipateBean.defaultCurrencyType);
        map.put("channelAreaId", CurLiveInfo.channelAreaId);
        httpHelper.startRequest(map, true);
    }

    //查询历史抽奖
    private void queryHistoryRecord() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.ACTIVITY_ISSUE_PRIZED_QUERY, true);
        map.put("issueId", issueId);
        map.put("channelAreaId", "100140");
        map.put("activityCode", DrawActivityType.HAND_SPEED);
        map.setGenericClaz(PrizedRecordEntity.class);
        httpHelper.startRequest(map);
    }

    private void responseSuccessHandSpeedQuery(LiveDrawInfoEntity liveDrawInfoEntity) {
        List<LuckyDrawParticipateConditionSimpleBean> drawParticipateSimpleBeanList = liveDrawInfoEntity.luckyDrawParticipateConditionSimple;
        if (drawParticipateSimpleBeanList != null && drawParticipateSimpleBeanList.size() > 0)
            drawParticipateBean = drawParticipateSimpleBeanList.get(0);
        LuckyDrawParticipateConditionSimpleBean drawParticipateSimpleBean = drawParticipateSimpleBeanList.get(0);
        PicUtils.load(mContext.getApplicationContext(), hostPortraitIv, liveDrawInfoEntity.patronUrl);
        hostNameTv.setText(liveDrawInfoEntity.patronLoginName);
        userParticipateCount = liveDrawInfoEntity.userParticipateCount;
        selfSendNumTv.setText(String.format(mContext.getString(R.string.self_send_gift_num), String.valueOf(userParticipateCount)));
        PicUtils.load(mContext.getApplicationContext(), giftIv, liveDrawInfoEntity.giftUrl);
        //赠送[啤酒](2礼金/个)夺好礼,点击越快,中奖几率越高
        String content = "<font color='#BF8D74'>赠送</font>" +
                "<font color='#F0885F'>[" + liveDrawInfoEntity.giftName + "]" +
                "(" + new Money(drawParticipateSimpleBean.perPrice).toSimpleString() +
                PayType.valueOf(drawParticipateSimpleBean.defaultCurrencyType).getMessage() + "/个)</font>" +
                "<font color='#BF8D74'>夺好礼,点击越快,中奖几率越高</font>";
        memoTv.setText(Html.fromHtml(content));
        List<IssuePrizeItemListBean> issuePrizeItemList = liveDrawInfoEntity.issuePrizeItemList;
        if (issuePrizeItemList != null && issuePrizeItemList.size() > 0) {
            IssuePrizeItemListBean issuePrizeItemListBean = issuePrizeItemList.get(0);
            prizeNameTv.setText(issuePrizeItemListBean.prizeItemName);
            prizeNumTv.setText(String.format(mContext.getString(R.string.total_num), issuePrizeItemListBean.totalCount));
        }

        if (liveDrawInfoEntity.userMedalLevelList != null) {
            for (UserMedalLevelBean userMedalLevelBean : liveDrawInfoEntity.userMedalLevelList) {
                medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(mContext,
                        userMedalLevelBean.medalConfigCode, userMedalLevelBean.currentMedalLevelConfigCode));
            }
        }
    }

    private void responseSuccessParticipate(JSONObject jsonObject) {
        try {
            String drawStatus;
            if (jsonObject.has("drawStatus")) {
                drawStatus = jsonObject.getString("drawStatus");
            }
            userParticipateCount += 1;
            if (selfSendNumTv != null)
                selfSendNumTv.setText(String.format(mContext.getString(R.string.self_send_gift_num), String.valueOf(userParticipateCount)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessHistoryPrizedQuery(PrizedRecordEntity prizedRecordEntity) {
        List<String> prizedNameList = prizedRecordEntity.getPrizedNameList();
        LogUtils.e("ACTIVITY_ISSUE_PRIZED_QUERY", "prizedNameList.size=" + prizedNameList.size());
        if (prizedNameList != null && prizedNameList.size() > 0) {
            prizedTitleTv.setVisibility(View.VISIBLE);
            prizedTitleTv.animate().alpha(1.0f).setDuration(800).start();
            listView.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_simple2_listview,
                    R.id.tv, prizedNameList));
        } else {
            listView.setVisibility(View.GONE);
            noPrizedView.setVisibility(View.VISIBLE);
            prizeInfoLayout.animate().alpha(0).setDuration(800).start();
            noPrizedView.animate().alpha(1.0f).setDuration(800).start();
        }

        //十秒钟之后关闭弹窗
        closeIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 10 * 1000);
    }

    private void responseSuccessPrizedQuery(DrawPrizedUserBean drawPrizedUserBean) {
        drawPrizedUserBean.operatorData();
        List<DrawPrizedUserBean.UserPrizedSimpleListBean> userPrizedSimpleList = drawPrizedUserBean.userPrizedSimpleList;
        if (userPrizedSimpleList != null && userPrizedSimpleList.size() > 0) {
            prizeShowTv.addMsg(userPrizedSimpleList);
        }
        if (TextUtils.equals(drawPrizedUserBean.drawStatus, DrawStatusType.FINISH)) {
            animSwitchShowHistoryRecord();
            queryCurrentPrizeUtil.stop();
            queryHistoryRecord();
        } else if (TextUtils.equals(drawPrizedUserBean.drawStatus, DrawStatusType.INIT)) {

        }
    }

    private CharSequence stringFormat(long time) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(SpannableStringHelper.changeTextColor(TimeUtils.timeFormat(time), ContextCompat.getColor(mContext, R.color.colorSendName12)));
        ssb.append("后开始");
        return ssb;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        queryCurrentPrizeUtil.onDestroy();
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
    }

    @Override
    public void onSuccess(RequestContainer request, Object obj) {
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case HAND_SPEED_QUERY:
                responseSuccessHandSpeedQuery((LiveDrawInfoEntity) obj);
                break;
            case HAND_SPEED_PARTICIPATE:
                responseSuccessParticipate((JSONObject) obj);
                break;
            case HAND_SPEED_USER_PRIZED_QUERY:
                responseSuccessPrizedQuery((DrawPrizedUserBean) obj);
                break;
            case ACTIVITY_ISSUE_PRIZED_QUERY:
                responseSuccessHistoryPrizedQuery((PrizedRecordEntity) obj);
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
        ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
    }

    private void showCoinNotEnoughDialog() {
        if (isChangeCoin) {
            ((BaseActivity) mContext).showRechargeView();
            return;
        }
        ShowMessageDialog dialog = new ShowMessageDialog(mContext, R.drawable.icon_dialog_title_success, "是", "否", new OnMyClickListener() {
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
