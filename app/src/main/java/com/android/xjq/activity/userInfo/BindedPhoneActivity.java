package com.android.xjq.activity.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.jjx.sdk.JjxConstants;
import com.android.jjx.sdk.UI.ChangeBindPhoneVerify;
import com.android.jjx.sdk.utils.MessageEvent;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BindedPhoneActivity extends JjxContactBaseActivity implements OnHttpResponseListener {

    public static final String CHANGE_BIND_PHONE_INPUT_VERIFY_NUM = "change_bind_phone_input_verify_num";

    HttpRequestHelper httpRequestHelper;

    private String cell;

    @BindView(R.id.bindedPhoneTv)
    TextView bindedPhoneTv;

    @BindView(R.id.warnMessageTv)
    TextView warnMessageTv;

    @BindView(R.id.backIv)
    ImageView backIv;


    @OnClick(R.id.changeBindTv)
    void changeBindTv() {

        Intent intent = new Intent();

        intent.setClass(BindedPhoneActivity.this, ChangeBindPhoneVerify.class);

        intent.putExtra("from", CHANGE_BIND_PHONE_INPUT_VERIFY_NUM);

        intent.putExtra(JjxConstants.cell, cell);

        startActivity(intent);

    }

    public static void startBindedPhoneActivity(Activity activity, String cell) {
        Intent intent = new Intent(activity, BindedPhoneActivity.class);
        intent.putExtra(JjxConstants.cell, cell);
        activity.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {

        setContentView(R.layout.at_binded_phone);

        ButterKnife.bind(this);

        setTitleBar("手机号码");

        backIv.setVisibility(View.VISIBLE);

        cell = getIntent().getStringExtra("cell");

        bindedPhoneTv.setText(cell);

        httpRequestHelper = new HttpRequestHelper(this, this);

        requestData();
    }

    public void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PROMPT_MEMO, false);
        httpRequestHelper.startRequest(map);
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        if (event.isSyncChangeBindPhoneSuccess()) {
            // httpOperate.syncUserCellFromOap(null);
            finish();
        }
    }


    private void responseWarnInfo(JSONObject jsonObject) throws JSONException {
        warnMessageTv.setText(jsonObject.getString("userIdentityValidateMemo"));
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            responseWarnInfo(jsonObject);
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
