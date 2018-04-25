package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.mall.MallActivity;
import com.android.xjq.activity.setting.CertificationActivity;
import com.android.xjq.adapter.main.RechargeChannelAdapter;
import com.android.xjq.adapter.main.RechargeMoneyShowAdapter;
import com.android.xjq.bean.recharge.DepositApplyBean;
import com.android.xjq.bean.recharge.DepositBaseInfoBean;
import com.android.xjq.bean.recharge.FundChannelsBean;
import com.android.xjq.bean.recharge.UserInfoBean;
import com.android.xjq.dialog.RechargeResultShowDialog;
import com.android.xjq.model.recharge.FundChannelProviderTypeEnum;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.utils.recharge.OrderParamBuildUtil;
import com.android.xjq.utils.recharge.PayResult;
import com.android.xjq.wxapi.WeiXinConstants;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.DEPOSIT_APPLY;
import static com.android.xjq.utils.XjqUrlEnum.DEPOSIT_FALL_FINISH;

/**
 * Created by lingjiu on 2017/4/20.
 */

public class RechargeActivity extends BaseActivity implements OnHttpResponseListener {

    private static final int ALIPAY_SDK_FLAG = 1;
    @BindView(R.id.rechargeChannelLv)
    ListView rechargeChannelLv;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    @BindView(R.id.baseRechargeMoneyGv)
    GridView baseRechargeMoneyGv;
    @BindView(R.id.adIv)
    SimpleDraweeView adIv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.remainGoldTv)
    TextView remainGoldTv;
    @BindView(R.id.remainGiftTv)
    TextView remainGiftTv;
    @BindView(R.id.remainPointTv)
    TextView remainPointTv;
    @BindView(R.id.portraitIv)
    ImageView portraitIv;
    @BindView(R.id.giveGoldTv)
    TextView giveGoldTv;

    private HttpRequestHelper httpRequestHelper;
    //几个基础的价格显示
    private List<String> priceList = new ArrayList<>();
    //交易通道列表
    private List<FundChannelsBean> channelList = new ArrayList<>();
    //充值通道适配器
    private RechargeChannelAdapter rechargeChannelAdapter;
    //基础价格展示适配器
    private RechargeMoneyShowAdapter rechargeMoneyShowAdapter;
    //当前选择的交易通道
    private FundChannelsBean currentChannelsBean;
    //当前所选择的金额
    private double currentRechargeMoney;
    //金额对应赠送金币的比例
    private double goldcoinExchangeRate;
    //充值单号
    private String depositNo;
    //头部信息
    private DepositBaseInfoBean depositBaseInfoBean;
    private RechargeResultShowDialog dialog;

    @OnClick(R.id.submitBtn)
    public void submitBtn() {

        if (currentRechargeMoney == 0) {
            ToastUtil.showLong(XjqApplication.getContext(), "请输入正确金额");
            return;
        }
        if (currentChannelsBean.getMaxAllowAmount() < currentRechargeMoney) {
            ToastUtil.showLong(XjqApplication.getContext(), "超过单笔充值限额");
            return;
        }
        if (currentChannelsBean.getMinAllowAmount() > currentRechargeMoney) {
            ToastUtil.showLong(XjqApplication.getContext(), "单笔充值不小于" + currentChannelsBean.getMinAllowAmount());
            return;
        }

        XjqRequestContainer map = new XjqRequestContainer(DEPOSIT_APPLY, true);
        map.put("amount", String.valueOf(currentRechargeMoney));
        if (FundChannelProviderTypeEnum.ANTFINANCIAL.name().equals(currentChannelsBean.getBankCode())) {
            map.put("charset", "utf-8");
        }
        map.put("fundChannelId", String.valueOf(currentChannelsBean.getId()));

        httpRequestHelper.startRequest(map, true);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALIPAY_SDK_FLAG: {
                    submitBtn.setEnabled(true);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    LogUtils.e(TAG, "PayResult=" + payResult.toString());
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //找服务端核对支付结果
                        httpRequestHelper.startRequest(queryDepositFinishInfo(), true);
                    } else {
                        Toast.makeText(RechargeActivity.this, payResult.getMemo(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public static void startRechargeActivity(Activity activity) {
        activity.startActivity(new Intent(activity, RechargeActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_recharge);
        ButterKnife.bind(this);
        setTitleBar(true, "充值", null);
        init();
        startRequest();
        isSupportTouchHideKeyBoard = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        submitBtn.setEnabled(true);
    }

    private void startRequest() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.DEPOSIT_BASE_INFO_QUERY, true);

        map.setSubDependencyListener(new RequestContainer.SubDependencyListener() {
            @Override
            public void execute() {
                httpRequestHelper.startRequest(getRechargeChannelInfo(), true);
            }
        });

        httpRequestHelper.startRequest(map, true);
    }

    private XjqRequestContainer getRechargeChannelInfo() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.DEPOSIT_FUND_CHANNEL_QUERY, true);
        map.put("sortTypes", "XJQ_ANDROID");
        return map;
    }

    private XjqRequestContainer queryDepositFinishInfo() {
        XjqRequestContainer map = new XjqRequestContainer(DEPOSIT_FALL_FINISH, true);
        map.put("depositNo", depositNo);
        return map;
    }

    private void init() {
        httpRequestHelper = new HttpRequestHelper(this, this);

        rechargeChannelAdapter = new RechargeChannelAdapter(this, channelList);

        rechargeChannelAdapter.setChannelSelectedListener(rechargeChannelListener);

        rechargeChannelLv.setAdapter(rechargeChannelAdapter);

        rechargeMoneyShowAdapter = new RechargeMoneyShowAdapter(this, priceList);

        rechargeMoneyShowAdapter.setOnRechargeMoneyCountListener(rechargeMoneyCountListener);

        baseRechargeMoneyGv.setAdapter(rechargeMoneyShowAdapter);
    }

    //充值通道选择监听
    private RechargeChannelAdapter.ChannelSelectedListener rechargeChannelListener = new RechargeChannelAdapter.ChannelSelectedListener() {

        @Override
        public void onSelectedChange(int position) {
            currentChannelsBean = channelList.get(position);

            goldcoinExchangeRate = currentChannelsBean.getGoldcoinExchangeRate();

            setGiveGoldText();
        }
    };
    //充值金额选择监听
    private RechargeMoneyShowAdapter.OnRechargeMoneyCountListener rechargeMoneyCountListener = new RechargeMoneyShowAdapter.OnRechargeMoneyCountListener() {
        @Override
        public void onRechargeMoneyChange(double moneyCount) {
            currentRechargeMoney = moneyCount;
            setGiveGoldText();
        }
    };

    private void setGiveGoldText() {
        submitBtn.setText(String.format(getString(R.string.recharge_money), new Money(currentRechargeMoney).toSimpleString()));

        int giveGold = (int) (goldcoinExchangeRate * currentRechargeMoney);
        if (giveGold == 0) {
            giveGoldTv.setVisibility(View.GONE);
            return;
        }
        giveGoldTv.setVisibility(View.VISIBLE);
        SpannableStringBuilder ssb = new SpannableStringBuilder("赠送");
        ssb.append(SpannableStringHelper.changeTextColor(giveGold + "金币", getResources().getColor(R.color.light_red)));
        giveGoldTv.setText(ssb);
    }

    @Override
    public void onMessageEvent(Object event) {
        EventBusMessage msg = (EventBusMessage) event;
        if (msg.isRoleSwitchSuccess()) {
            startRequest();
        } else if (msg.isWeiXinPayResult()) {
            submitBtn.setEnabled(true);
            //找服务端核对支付结果
            httpRequestHelper.startRequest(queryDepositFinishInfo(), true);
        }
    }

    private boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {
        if (!msgApi.isWXAppInstalled()) {
            LibAppUtil.showTip(this, getString(R.string.need_install_WX));
            return false;
        } else if (!msgApi.isWXAppSupportAPI()) {
            LibAppUtil.showTip(this, getString(R.string.WX_version_not_support));
            return false;
        }
        return true;
    }

    private void responseSuccessBaseInfoQuery(JSONObject jsonObject) {

        depositBaseInfoBean = new Gson().fromJson(jsonObject.toString(), DepositBaseInfoBean.class);

        if (depositBaseInfoBean.getDepositActivityImageUrl() != null) {
            adIv.setVisibility(View.VISIBLE);
            adIv.setImageURI(Uri.parse(depositBaseInfoBean.getDepositActivityImageUrl()));
        } else {
            adIv.setVisibility(View.GONE);
        }

        DepositBaseInfoBean.UserBaseInfoBean userBaseInfo = depositBaseInfoBean.getUserBaseInfo();

        UserInfoBean userInfo = userBaseInfo.getUserInfo();

        userNameTv.setText(userInfo.getLoginName());

        setImageShow(portraitIv, userInfo.getUserLogoUrl());

        remainPointTv.setText(String.format(getString(R.string.recharge_remain_money), userBaseInfo.getPointCoinAmount() + ""));

        remainGiftTv.setText(String.format(getString(R.string.recharge_remain_money), userBaseInfo.getGiftCoinAmount() + ""));

        remainGoldTv.setText(String.format(getString(R.string.recharge_remain_money), userBaseInfo.getGoldCoinAmount() + ""));

        if (depositBaseInfoBean.getPriceList() != null && depositBaseInfoBean.getPriceList().size() > 0) {
            priceList.clear();

            priceList.addAll(depositBaseInfoBean.getPriceList());

            currentRechargeMoney = Double.valueOf(priceList.get(0));
        }

        rechargeMoneyShowAdapter.setPointCoinExchangeRate(depositBaseInfoBean.getPointcoinExchangeRate());

        rechargeMoneyShowAdapter.notifyDataSetChanged();

    }


    private void responseSuccessChannelQuery(JSONObject jsonObject) throws JSONException {

        if (jsonObject.has("fundChannels")) {
            submitBtn.setEnabled(true);

            JSONArray fundChannels = jsonObject.getJSONArray("fundChannels");

            List<FundChannelsBean> channelsBeanList = new Gson().fromJson(fundChannels.toString(), new TypeToken<List<FundChannelsBean>>() {
            }.getType());
            if (channelsBeanList != null && channelsBeanList.size() > 0) {
                contentLayout.setVisibility(View.VISIBLE);
                channelList.clear();
                channelList.addAll(channelsBeanList);

                currentChannelsBean = channelList.get(0);

                goldcoinExchangeRate = currentChannelsBean.getGoldcoinExchangeRate();

                setGiveGoldText();
            } else {
                showToast(getString(R.string.no_recharge_channel));
            }

            rechargeChannelAdapter.notifyDataSetChanged();
        }
    }

    private void responseSuccessDepositApply(JSONObject jsonObject) {
        submitBtn.setEnabled(false);
        DepositApplyBean depositApplyBean = new Gson().fromJson(jsonObject.toString(), DepositApplyBean.class);
        HashMap<String, String> parameterMap = depositApplyBean.getParameterMap();
        String providerType = depositApplyBean.getProviderType().getName();
        depositNo = depositApplyBean.getDepositNo();
        String appid = depositApplyBean.getAppId();
        if (FundChannelProviderTypeEnum.ANTFINANCIAL.name().equals(providerType)) {
            alipay(parameterMap);
        } else if (FundChannelProviderTypeEnum.WEIXINPAY.name().equals(providerType)) {
            weixinPay(parameterMap, appid);
        }
    }

    private void alipay(HashMap<String, String> parameterMap) {
        Map<String, String> params = buildOrderParamMap(parameterMap);
        String orderParam = OrderParamBuildUtil.buildOrderParam(params, parameterMap.get("charset"));
        final String orderInfo = orderParam;
        LogUtils.e(TAG, "orderInfo=" + orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtils.e(TAG, "msp" + result.toString());
                Message msg = new Message();
                msg.what = ALIPAY_SDK_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

        LogUtils.e(TAG, " ====调起支付宝支付==== ");
    }

    private Map<String, String> buildOrderParamMap(HashMap<String, String> parameterMap) {
        String sign_data = parameterMap.get("SIGN_DATA");
        Map<String, String> keyValues = new HashMap<>();
        String[] params = sign_data.split("&");
        for (String param : params) {
            String[] p = param.split("=");
            if (p.length == 2) {
                keyValues.put(p[0], p[1]);
            }
        }
        keyValues.put("sign", parameterMap.get("SIGN"));
        return keyValues;
    }

    private boolean weixinPay(HashMap<String, String> parameterMap, String appId) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, appId);
        WeiXinConstants.appId = appId;
        // 将该app注册到微信
        msgApi.registerApp(appId);
        //检测微信是否存在以及是否微信版本是否支持支付
        if (!isWXAppInstalledAndSupported(msgApi)) {
            submitBtn.setEnabled(true);
            return true;
        }
        if (parameterMap != null) {
            PayReq req = new PayReq();
            req.appId = appId;
            req.packageValue = "Sign=WXPay";
            req.partnerId = parameterMap.get("partnerid");
            req.prepayId = parameterMap.get("prepayid");
            req.nonceStr = parameterMap.get("noncestr");
            req.timeStamp = parameterMap.get("timestamp");
            req.sign = parameterMap.get("sign");
            msgApi.sendReq(req);
        }
        LogUtils.e(TAG, " ====调起微信支付==== ");
        return false;
    }


    private void responseSuccessDepositFinish(JSONObject jsonObject) {

        if (depositBaseInfoBean != null) {
            //解决多次回调引发dialog重复显示的问题
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            double rechargePoint = (currentRechargeMoney * depositBaseInfoBean.getPointcoinExchangeRate());
            if (rechargePoint > 0) {
                //成功
                showSuccessDialog();
            } else {
                //失败
                dialog = new RechargeResultShowDialog(this, rechargePoint);
            }
            //刷新头部数据
            XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.DEPOSIT_BASE_INFO_QUERY, true);
            httpRequestHelper.startRequest(map, false);
        }

    }

    private void showSuccessDialog() {
        ShowMessageDialog dialog = new ShowMessageDialog(this, R.drawable.icon_dialog_title_success, "去购买", "关闭", new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                MallActivity.startMallActivity(RechargeActivity.this);
            }
        }, null, "您已成功充值!\n是否跳转商城购买金币大礼包?");

    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case DEPOSIT_BASE_INFO_QUERY:
                    responseSuccessBaseInfoQuery(jsonObject);
                    break;
                case DEPOSIT_FUND_CHANNEL_QUERY:
                    responseSuccessChannelQuery(jsonObject);
                    break;
                case DEPOSIT_APPLY:
                    responseSuccessDepositApply(jsonObject);
                    break;
                case DEPOSIT_FALL_FINISH:
                    responseSuccessDepositFinish(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {

            if (((XjqUrlEnum) requestContainer.getRequestEnum()).equals(DEPOSIT_FALL_FINISH)) {
                //解决多次回调引发dialog重复显示的问题
                if (dialog != null && dialog.isShowing()) {
                    return;
                }
                dialog = new RechargeResultShowDialog(this, 0);
                return;
            } else if (requestContainer.getRequestEnum().equals(DEPOSIT_APPLY)) {
                ErrorBean errorBean = new ErrorBean(jsonObject);
                if (errorBean != null && "USER_IDENTIFY_NOT_EXISTS".equals(errorBean.getError().getName())) {
                    //实名验证
                    ShowMessageDialog showMessageDialog = new ShowMessageDialog(this, R.drawable.icon_dialog_title_identity, "去设置", "取消", new OnMyClickListener() {
                        @Override
                        public void onClick(View v) {
                            startIdentitySetActivity(RechargeActivity.this);
                        }
                    }, null, "您还没有实名认证,请实名认证");
                    return;
                }
            }
            operateErrorResponseMessage(jsonObject);
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

    public static void startIdentitySetActivity(Activity activity) {

        Intent intent = new Intent();

        intent.setClass(activity, CertificationActivity.class);

        activity.startActivity(intent);
    }

}
