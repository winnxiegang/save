package com.android.xjq.liveanim;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.draw.LiveDrawInfoEntity;
import com.android.xjq.bean.draw.LuckyDrawParticipateConditionSimpleBean;
import com.android.xjq.bean.draw.RocketResultBean;
import com.android.xjq.listener.live.RocketFlyListener;
import com.android.xjq.utils.GetPollingResultUtil;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.xjq.utils.XjqUrlEnum.ROCKET_QUERY;

/**
 * Created by zaozao on 2018/3/6.
 */

public class RocketWaterFireDialog implements IHttpResponseListener {

    private LiveActivity mActivity;

    @BindView(R.id.userLogoIv)
    ImageView userLogoIv;
    @BindView(R.id.titleShowTv)
    TextView titleShowTv;
    @BindView(R.id.rocketFireLayout)
    RocketButtonClickLayout rocketFireLayout;
    @BindView(R.id.rocketWaterLayout)
    RocketButtonClickLayout rocketWaterLayout;
    @BindView(R.id.closeIv)
    ImageView closeIv;
    @BindView(R.id.countTimeTv)
    CountdownTextView countdownTextView;

    private PopupWindow mPopupWindow;
    String issueId;
    String channelAreaId;
    private String rocketTitle;
    private RocketFlyListener rocketFlyListener;
    private LuckyDrawParticipateConditionSimpleBean fireBean;
    private LuckyDrawParticipateConditionSimpleBean waterBean;
    private WrapperHttpHelper httpRequestHelper;
    private LiveDrawInfoEntity liveDrawInfoEntity;
    private GetPollingResultUtil resultQueryUtils;

    public RocketWaterFireDialog(final LiveActivity context, String issueId, String channelAreaId, RocketFlyListener rocketFlyListener) {
        mActivity = context;
        View mDialogView = View.inflate(context, R.layout.dialog_rocket_water_fire, null);
        int[] size = adjustDialogSize(context);
        mPopupWindow = new PopupWindow(mDialogView, size[0], size[1], true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        ButterKnife.bind(this, mDialogView);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(true);

        this.issueId = issueId;
        this.channelAreaId = channelAreaId;
        this.rocketFlyListener = rocketFlyListener;
        httpRequestHelper = new WrapperHttpHelper(this);
        rocketQuery();
    }

    /**
     * 计算出popupWindow宽高
     */
    public int[] adjustDialogSize(Context context) {
        int[] size = new int[2];
        int screenHeight = LibAppUtil.getScreenHeight(context);
        int screenWidth = LibAppUtil.getScreenWidth(context);
        if (screenHeight > screenWidth) {
            size[0] = screenWidth;
//            lp.gravity = Gravity.BOTTOM;
        } else {
            size[0] = screenHeight;
        }
        size[1] = (int) DimensionUtils.dpToPx(300, context);
        return size;
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    private void rocketRequestSuccess(JSONObject jsonObject) {
        liveDrawInfoEntity = new Gson().fromJson(jsonObject.toString(), LiveDrawInfoEntity.class);

        rocketTitle = liveDrawInfoEntity.content;

        titleShowTv.setText(rocketTitle);

        Picasso.with(mActivity).load(liveDrawInfoEntity.patronUrl).into(userLogoIv);

        for (int i = 0; i < liveDrawInfoEntity.luckyDrawParticipateConditionSimple.size(); i++) {
            LuckyDrawParticipateConditionSimpleBean bean = liveDrawInfoEntity.luckyDrawParticipateConditionSimple.get(i);
            if (TextUtils.equals("POSITIVE", bean.supportSide)) {
                fireBean = bean;
            }

            if (TextUtils.equals("NEGATIVE", bean.supportSide)) {
                waterBean = bean;
            }
        }

        rocketFireLayout.setPayNumAndType("(" + fireBean.getRocketPerPay() + ")");

        rocketWaterLayout.setPayNumAndType("(" + waterBean.getRocketPerPay() + ")");

        rocketFireLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rocketParticipate(fireBean.id, fireBean.defaultCurrencyType,
                        waterBean.perCount, fireBean.perCount * fireBean.perPrice);

            }
        });

        rocketWaterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rocketParticipate(waterBean.id, waterBean.defaultCurrencyType,
                        waterBean.perCount, waterBean.perCount * waterBean.perPrice);

            }
        });

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        countdownTextView.start(TimeUtils.date1SubDate2Ms(liveDrawInfoEntity.gmtEnd, liveDrawInfoEntity.nowDate));
        countdownTextView.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                countdownTextView.setText(countdownTime / 1000 + "");

            }

            @Override
            public void countdownEnd() {
                countdownTextView.setVisibility(View.GONE);
                //rocketResultQuery();
            }
        });
        mPopupWindow.showAtLocation(mActivity.findViewById(R.id.closeIv).getRootView(), Gravity.CENTER, 0, 0);

        createRocketResultQuery();
    }

    public void onDestroy() {
        if (resultQueryUtils != null) resultQueryUtils.stop();
    }

    //火箭结果查询
    private void createRocketResultQuery() {
        resultQueryUtils = new GetPollingResultUtil(new GetPollingResultUtil.PollingCallback() {
            @Override
            public void onTick(WrapperHttpHelper httpHelper, int currentRequestCount) {
                RequestFormBody requestFormBody = new RequestFormBody(XjqUrlEnum.ROCKET_PARTICIPATE_RESULT, true);
                requestFormBody.put("issueId", issueId);
                httpHelper.startRequest(requestFormBody, false);
            }
        }, this);
        resultQueryUtils.startGetData();
    }


    //火箭信息查询
    private void rocketQuery() {
        RequestFormBody requestFormBody = new RequestFormBody(ROCKET_QUERY, true);
        requestFormBody.put("issueId", issueId);
        httpRequestHelper.startRequest(requestFormBody);
    }

    //点火浇水
    private void rocketParticipate(String participateConditionId, String currencyType,
                                   double participateValue, double totalAmount) {
        RequestFormBody requestFormBody = new RequestFormBody(XjqUrlEnum.ROCKET_PARTICIPATE, true);

        requestFormBody.put("issueId", issueId);//期次id

        requestFormBody.put("participateConditionId", participateConditionId);//

        requestFormBody.put("currencyType", currencyType);

        requestFormBody.put("participateValue", (int) participateValue);

        requestFormBody.put("channelAreaId", channelAreaId);

        requestFormBody.put("totalAmount", String.valueOf(totalAmount));

        httpRequestHelper.startRequest(requestFormBody, false);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case ROCKET_PARTICIPATE_RESULT:
                RocketResultBean rocketResult = new Gson().fromJson(((JSONObject) o).toString(), RocketResultBean.class);
                if (rocketResult.getIssueStatus().getName().equals("FINISH")) {
                    resultQueryUtils.stop();
                    if (TextUtils.equals("POSITIVE", rocketResult.getRocketResult())) {
                        //发射
                        rocketFlyListener.rocketFly(rocketTitle, true);
                    } else {
                        // 落水
                        rocketFlyListener.rocketFly(rocketTitle, false);
                    }
                    mPopupWindow.dismiss();
                }
                break;
            case ROCKET_QUERY:
                rocketRequestSuccess(((JSONObject) o));
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mActivity.operateErrorResponseMessage(jsonObject);
    }
}
