package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.Money;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/6/7.
 */

public class MyWalletActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.pointTv)
    TextView pointTv;
    @BindView(R.id.goldTv)
    TextView goldTv;
    @BindView(R.id.giftTv)
    TextView giftTv;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;


    private HttpRequestHelper httpRequestHelper;

    public static void startMyWalletActivity(Activity activity) {
        Intent intent = new Intent(activity, MyWalletActivity.class);

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_my_wallet);

        ButterKnife.bind(this);

        setTitleBar(true, "我的钱包", true);

        httpRequestHelper = new HttpRequestHelper(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRequest();
    }

    private void startRequest() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MY_FUND_ACCOUNT_QUERY, true);

        httpRequestHelper.startRequest(map, true);
    }

    private void responseSuccessAccountQuery(JSONObject jsonObject) throws JSONException {
        contentLayout.setVisibility(View.VISIBLE);

        double goldCoinAmount = 0;
        if (jsonObject.has("goldCoinAmount")) {
            goldCoinAmount = jsonObject.getDouble("goldCoinAmount");
        }
        double pointCoinAmount = 0;
        if (jsonObject.has("pointCoinAmount")) {
            pointCoinAmount = jsonObject.getDouble("pointCoinAmount");
        }
        double giftCoinAmount = 0;
        if (jsonObject.has("giftCoinAmount")) {
            giftCoinAmount = jsonObject.getDouble("giftCoinAmount");
        }
        goldTv.setText(new Money(goldCoinAmount).toSimpleString());
        pointTv.setText(new Money(pointCoinAmount).toSimpleString());
        giftTv.setText(new Money(giftCoinAmount).toSimpleString());
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            responseSuccessAccountQuery(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
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
}
