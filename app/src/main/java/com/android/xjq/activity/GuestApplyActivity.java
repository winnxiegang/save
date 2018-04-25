package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.bean.guest.IdentifyConfigBean;
import com.android.xjq.bean.guest.SeatApplyInfoBean;
import com.android.xjq.model.gift.PayType;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.IDENTITY_APPLY;
import static com.android.xjq.utils.XjqUrlEnum.VIP_SEAT_INIT;

/**
 * Created by lingjiu on 2017/7/28.
 */

public class GuestApplyActivity extends BaseActivity implements OnHttpResponseListener {

    @BindView(R.id.payBtn)
    Button payBtn;
    @BindView(R.id.moneyTv)
    TextView moneyTv;
    @BindView(R.id.validityTv)
    TextView validityTv;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    @BindView(R.id.statusLayout)
    CommonStatusLayout statusLayout;
    private HttpRequestHelper httpRequestHelper;
    private double price;

    @OnClick(R.id.payBtn)
    public void pay() {
        XjqRequestContainer map = new XjqRequestContainer(IDENTITY_APPLY, true);
        map.put("identityConfigCode", "VIP");
        map.put("currencyType", PayType.GOLD_COIN.name());
        map.put("payType", "BY_MONTH");
        map.put("sceneType", "CELL_CLIENT");
        map.put("totalAmount", "" + price);
        map.put("totalCount", "1");
        httpRequestHelper.startRequest(map, true);
    }

    @OnClick(R.id.closeIv)
    public void close() {
        finish();
    }

    public static void startGuestApplyActivity(Activity activity) {
        Intent intent = new Intent(activity, GuestApplyActivity.class);

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_guest_apply);
        ButterKnife.bind(this);
        setTitleBar(true, "");
        httpRequestHelper = new HttpRequestHelper(this, this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(VIP_SEAT_INIT, true);
        map.put("currencyType", PayType.GOLD_COIN.name());
        map.put("payType", "BY_MONTH");
        map.put("identityConfigCode", "VIP");
        httpRequestHelper.startRequest(map, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage message) {
        if (message.isRefreshLiveRoom()) finish();
    }

    private void initView() {
    }

    private void responseSuccessSeatInfo(JSONObject jsonObject) {
        statusLayout.hideStatusView();
        contentLayout.setVisibility(View.VISIBLE);
        IdentifyConfigBean identifyConfigBean = new Gson().fromJson(jsonObject.toString(), IdentifyConfigBean.class);
        IdentifyConfigBean.IdentityConfigBean identityConfig = identifyConfigBean.getIdentityConfig();
        if (identifyConfigBean.isVip()) {
            payBtn.setEnabled(false);
        } else {
            payBtn.setEnabled(true);
        }
        if (identityConfig != null) {
            price = identityConfig.getPrice();
            moneyTv.setText(new Money(identityConfig.getPrice()).toLevelString() + "");
            validityTv.setText(identityConfig.getEffectiveTime() + "天");
        }

        //handleIsEnableApply(seatApplyInfoBean);
    }

    private void handleIsEnableApply(SeatApplyInfoBean seatApplyInfoBean) {
        if (seatApplyInfoBean.isAppliable()) return;

        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setMessage("需绑定第三方账户,才可抢占贵宾席,是否立即绑定?");
        builder.setPositiveMessage("确定");
        builder.setNegativeMessage("取消");
        builder.setTitle("提示");
        builder.setShowTitle(true);
        builder.setShowMessageMiddle(true);
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindThirdCertifyActivity.startBindThirdCertifyActivity(GuestApplyActivity.this);
            }
        });
        builder.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }

    private void responseSuccessSeatApply(JSONObject jsonObject) {
        LibAppUtil.showTip(this, "申请成功");
        requestData();
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case VIP_SEAT_INIT:
                responseSuccessSeatInfo(jsonObject);
                break;
            case IDENTITY_APPLY:
                responseSuccessSeatApply(jsonObject);
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {

            if (IDENTITY_APPLY == ((XjqUrlEnum) requestContainer.getRequestEnum())) {
                ErrorBean bean = new ErrorBean(jsonObject);

                String name = bean.getError().getName();
                if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(name)) {
                    ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
                    builder.setMessage("可用余额不足");
                    builder.setPositiveMessage("去充值");
                    builder.setNegativeMessage("取消");
                    builder.setTitle("提示");
                    builder.setShowMessageMiddle(true);
                    builder.setPositiveClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RechargeActivity.startRechargeActivity(GuestApplyActivity.this);
                        }
                    });
                    ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
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
        if (VIP_SEAT_INIT == requestContainer.getRequestEnum()) {
            statusLayout.showRetry();
        }
    }

    @Override
    public void executorFinish() {

    }
}
