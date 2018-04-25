package com.android.xjq.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;
import com.android.banana.commlib.bean.ErrorBean;

import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.utils.GetPollingResultUtil2;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.PollingCallback;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.EXCHANGE_APPLY;
import static com.android.xjq.utils.XjqUrlEnum.EXCHANGE_INIT;
import static com.android.xjq.utils.XjqUrlEnum.EXCHANGE_RESULT_QUERY;

/**
 * Created by lingjiu on 2017/7/28.
 */

public class ExchangeGoldActivity extends BaseActivity implements OnHttpResponseListener,PollingCallback{
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.moneyTv)
    TextView moneyTv;
    @BindView(R.id.pointTv)
    TextView pointTv;
    @BindView(R.id.inputPointEt)
    EditText inputPointEt;
    @BindView(R.id.exchangeMoneyEt)
    EditText exchangeMoneyEt;
    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.confirmBtn)
    Button confirmBtn;
    private HttpRequestHelper httpRequestHelper;
    private ProgressDialog mDialog;
    private GetPollingResultUtil2 pollingResultUtil;
    private double exchangeRate;
    private long exchangeMoney;
    private String exchangeId;
    //当前可用的点券余额
    private long pointCoinAmount;

    @OnClick(R.id.confirmBtn)
    public void confirmExchange() {
        LibAppUtil.hideSoftKeyboard(this);
        mDialog.show();
        XjqRequestContainer map = new XjqRequestContainer(EXCHANGE_APPLY, true);
        map.put("toCurrencyType", "GOLDCOIN");
        map.put("fromtCurrencyType", "POINTCOIN");
        map.put("fromAmount", inputPointEt.getText().toString());
        map.put("toAmount", "" + exchangeMoney);
        httpRequestHelper.startRequest(map, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_exchange_gold);
        ButterKnife.bind(this);
        setTitleBar(true, "金币兑换", true);

        httpRequestHelper = new HttpRequestHelper(this, this);

        setListener();

        initDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRequest();
    }

    private void initDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("兑换中");
        mDialog.setCancelable(false);
    }

    public static void startExchangeGoldActivity(Activity activity) {
        Intent intent = new Intent(activity, ExchangeGoldActivity.class);
        activity.startActivity(intent);
    }

    private void setListener() {
        pollingResultUtil = new GetPollingResultUtil2(this, this);

        /*pollingResultUtil = new GetPollingResultUtil(this, new GetPollingResultUtil.RequestEndListener() {
            @Override
            public void requestEnd(int currentRequestCount) {
                LibAppUtil.showTip(ExchangeGoldActivity.this, "兑换失败");
                if (mDialog.isShowing()) mDialog.dismiss();

            }
        });*/

        inputPointEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && 0 != Long.valueOf(s.toString())) {
                    if (Long.valueOf(s.toString()) > pointCoinAmount) {
                        inputPointEt.setText(String.valueOf(pointCoinAmount));
                    }
                    exchangeMoney = (long) (Long.valueOf(inputPointEt.getText().toString()) * exchangeRate);
                    exchangeMoneyEt.setText(String.valueOf(exchangeMoney));
                    confirmBtn.setEnabled(true);
                } else {
                    exchangeMoneyEt.setText("");
                    confirmBtn.setEnabled(false);
                }

            }
        });
    }

    private void startRequest() {
        XjqRequestContainer map = new XjqRequestContainer(EXCHANGE_INIT, true);

        httpRequestHelper.startRequest(map, true);
    }

    private void responseSuccessExchangeInit(JSONObject jsonObject) throws JSONException {
        contentLayout.setVisibility(View.VISIBLE);
        long goldCoinAmount = 0;
        if (jsonObject.has("goldcoinAvailableAmount")) {
            goldCoinAmount = jsonObject.getLong("goldcoinAvailableAmount");
        }
        if (jsonObject.has("pointcoinAvailableAmount")) {
            pointCoinAmount = jsonObject.getLong("pointcoinAvailableAmount");
        }
        if (jsonObject.has("exchangeRate")) {
            exchangeRate = jsonObject.getLong("exchangeRate");
        }
        moneyTv.setText(String.valueOf(goldCoinAmount));

        pointTv.setText(String.valueOf(pointCoinAmount));

        userNameTv.setText(LoginInfoHelper.getInstance().getNickName());

        descTv.setText(String.format(getString(R.string.gold_exchange_rate, String.valueOf((int) exchangeRate))));
    }

    private void responseSuccessExchangeApply(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("exchangeId")) {
            exchangeId = jsonObject.getString("exchangeId");
            pollingResultUtil.startGetData();
        }
    }

    private void responseSuccessExchangeResult(JSONObject jsonObject) {
        mDialog.dismiss();
        pollingResultUtil.stop();
        LibAppUtil.showTip(this.getApplicationContext(), "兑换成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pollingResultUtil.onDestroy();
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case EXCHANGE_INIT:
                    responseSuccessExchangeInit(jsonObject);
                    break;
                case EXCHANGE_APPLY:
                    responseSuccessExchangeApply(jsonObject);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {

            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case EXCHANGE_APPLY:
                    if (mDialog.isShowing()) mDialog.dismiss();
                    ErrorBean errorBean = new ErrorBean(jsonObject);
                    String name = errorBean.getError().getName();
                    if ("POINT_COIN_AVAIABLE_AMOUNT_NOT_ENOUGH".equals(name)) {
                        LibAppUtil.showTip(ExchangeGoldActivity.this, "兑换失败");
                        startRequest();
                        return;
                    }

                    break;
            }
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        //网络断开,停止轮询
       /* if (EXCHANGE_RESULT_QUERY == ((XjqUrlEnum) requestContainer.getRequestEnum())) {
            pollingResultUtil.stop();
        }

        if (mDialog.isShowing()) mDialog.dismiss();*/
    }

    @Override
    public void executorFinish() {

    }

    @Override
    public void startPollingRequest(HttpRequestHelper httpRequestHelper) {
        XjqRequestContainer map = new XjqRequestContainer(EXCHANGE_RESULT_QUERY, true);
        map.put("exchangeId", exchangeId);
        httpRequestHelper.startRequest(map, false);
    }

    @Override
    public void pollingResultSuccess() {
        if (mDialog.isShowing()) mDialog.dismiss();
        LibAppUtil.showTip(this.getApplicationContext(), "兑换成功");
        finish();
    }

    @Override
    public void pollingResultFailed() {
        if (mDialog.isShowing()) mDialog.dismiss();
        LibAppUtil.showTip(this.getApplicationContext(), "兑换失败");
    }
}
