package com.android.xjq.activity.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.activity.userInfo.ChangePayPasswordActivity.REQUEST_FIND_PASSWORD;
import static com.android.xjq.utils.XjqUrlEnum.FIND_ACCOUNT_PASSWORD_BY_CELL;
import static com.android.xjq.utils.XjqUrlEnum.FIND_ACCOUNT_PASSWORD_SEND_SMS_ACK;

/**
 * Created by lingjiu on 2017/3/3 16:12.
 */

public class FindPayPassWordActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.smsEt)
    EditText smsEt;
    @BindView(R.id.smsTv)
    CountdownTextView smsTv;
    @BindView(R.id.input_phone_num_lay)
    LinearLayout inputPhoneNumLay;
    @BindView(R.id.confirmBtn)
    TextView confirmBtn;
    private HttpRequestHelper httpRequestHelper;
    private String smsId;


    @OnClick(R.id.smsTv)
    public void sendSms() {
        startGetSms();
    }

    @OnClick(R.id.confirmBtn)
    public void confirmBtn() {
        if (TextUtils.isEmpty(smsId)) {
            showToast("请先获取验证码");
            return;
        }
        if (isBlank(smsEt, "验证码不能为空")) {
            return;
        }

        showProgressDialog("正在验证..");
        XjqRequestContainer map = new XjqRequestContainer(FIND_ACCOUNT_PASSWORD_BY_CELL, true);
        map.put("validateCode", smsEt.getText().toString());
        map.put("smsId", smsId);
        httpRequestHelper.startRequest(map);
    }

    public static void startForgetPasswordActivity(Activity activity) {
        activity.startActivity(new Intent(activity, FindPayPassWordActivity.class));
    }

    public static void startForgetPasswordActivityForResult(Activity activity) {
        activity.startActivityForResult(new Intent(activity, FindPayPassWordActivity.class), REQUEST_FIND_PASSWORD);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pay_psd);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        ButterKnife.bind(this);
        setTitleBar(true, "找回支付密码",null);
        nameTv.setText(String.format(getString(R.string.role_nickname), LoginInfoHelper.getInstance().getNickName()));
        httpRequestHelper = new HttpRequestHelper(this, TAG);

        smsTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                smsTv.setText("" + countdownTime / 1000);
            }

            @Override
            public void countdownEnd() {
                smsTv.setEnabled(true);
                smsTv.setText("获取验证码");
            }
        });

        smsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    confirmBtn.setEnabled(false);
                } else {
                    confirmBtn.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        smsTv.cancel();
        super.onDestroy();
    }

    private void startGetSms() {
        showProgressDialog("正在获取验证码..");
        XjqRequestContainer map = new XjqRequestContainer(FIND_ACCOUNT_PASSWORD_SEND_SMS_ACK, true);
        httpRequestHelper.startRequest(map);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case FIND_ACCOUNT_PASSWORD_SEND_SMS_ACK:
                responseSuccessFirstStep(jsonObject);
                break;
            case FIND_ACCOUNT_PASSWORD_BY_CELL:
                responseSuccessSecondStep(jsonObject);
                break;
        }
    }

    private void responseSuccessSecondStep(JSONObject jsonObject) {
        try {
            String verifyCellSign = jsonObject.getString("verifyCellSign");
            String randomString = jsonObject.getString("randomString");
            LogUtils.e(TAG, "verifyCellSign = " + verifyCellSign);
            InitSetPasswordActivity.startInitSetPasswordActivity(this, verifyCellSign, randomString);
            setResult(RESULT_OK);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessFirstStep(JSONObject jsonObject) {
        try {
            smsId = jsonObject.getString("smsId");
            int timeoutSecond = jsonObject.getInt("timeoutSeconds");
            int waitNextPrepareSeconds = jsonObject.getInt("waitNextPrepareSeconds");
            smsTv.start(waitNextPrepareSeconds * 1000);
            smsTv.setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            JSONObject reqError = (JSONObject) jsonObject.get("error");
            String errorName = reqError.getString("name");
            if (errorName.equals("SMS_OUT_ACK_CODE_VALIDATE_TOO_MUCH")) {
                showToast(getResources().getString(R.string.toast_reget_checknum));
            } else {
                operateErrorResponseMessage(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {
        hideProgressDialog();
    }
}
