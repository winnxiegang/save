package com.android.xjq.activity.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.commlib.eventBus.EventBusMessage.INIT_PASSWORD_OK;
import static com.android.xjq.utils.XjqUrlEnum.ACCOUNT_PASSWORD_RULE_VALIDATE;

/**
 * Created by lingjiu on 2017/11/1.
 */

public class InitSetPasswordActivity extends BaseActivity implements PayPsdInputView.OnPasswordListener, OnHttpResponseListener {
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.tipTv)
    TextView tipTv;
    @BindView(R.id.passwordInputView)
    PayPsdInputView passwordInputView;
    @BindView(R.id.forgetPassWordTv)
    TextView forgetPassWordTv;
    @BindView(R.id.nextBtn)
    Button nextBtn;

    private String currentPassword;
    private String waitValidatePsd;
    private HttpRequestHelper httpRequestHelper;
    private String verifyCellSign;
    private String randomString;

    @OnClick(R.id.nextBtn)
    public void next() {
        showProgressDialog(null);
        if (verifyCellSign != null) {
            requestResetPassword();
        } else {
            requestInitPassword();
        }
    }

    public static void startInitSetPasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, InitSetPasswordActivity.class);
        activity.startActivity(intent);
    }

    public static void startInitSetPasswordActivity(Activity activity, String verifyCellSign, String randomString) {
        Intent intent = new Intent(activity, InitSetPasswordActivity.class);
        intent.putExtra("verifyCellSign", verifyCellSign);
        intent.putExtra("randomString", randomString);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_init_set_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        verifyCellSign = getIntent().getStringExtra("verifyCellSign");
        randomString = getIntent().getStringExtra("randomString");
        httpRequestHelper = new HttpRequestHelper(this, TAG);
        setTitleBar(true, "初始化支付密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        descTv.setText("设置6位数字支付密码");
        if (verifyCellSign != null) {
            descTv.setText("设置6位新数字支付密码");
            titleTv.setText("重置支付密码");
        }
        passwordInputView.setPasswordListener(this);
    }

    @Override
    public void onBackPressed() {
        if (currentPassword != null) {
            currentPassword = null;
            if (verifyCellSign != null) {
                descTv.setText("设置6位新数字支付密码");
            } else {
                descTv.setText("设置6位数字支付密码");
            }
            nextBtn.setEnabled(false);
            nextBtn.setVisibility(View.GONE);
            passwordInputView.setText("");
        } else {
            finish();
        }
    }

    private void requestResetPassword() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.FIND_ACCOUNT_PASSWORD_BY_CELL_SET_PASSWORD, true);
        map.put("newPassword", currentPassword);
        map.put("verifyCellSign", verifyCellSign);
        map.put("randomString", randomString);
        httpRequestHelper.startRequest(map);
    }

    private void requestInitPassword() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.INIT_ACCOUNT_PASSWORD, true);
        map.put("accountPassword", currentPassword);
        httpRequestHelper.startRequest(map);
    }

    private void validatePasswordRule(String psd) {
        XjqRequestContainer map = new XjqRequestContainer(ACCOUNT_PASSWORD_RULE_VALIDATE, false);
        map.put("accountPassword", psd);
        httpRequestHelper.startRequest(map);
    }

    @Override
    public void inputListener(boolean isInputComplete, String psd) {
        if (isInputComplete) {
            if (currentPassword == null) {
                waitValidatePsd = psd;
                validatePasswordRule(psd);
            } else {
                if (!TextUtils.equals(currentPassword, psd)) {
                    showToast("两次密码不一致");
                } else {
                    nextBtn.setEnabled(true);
                }
            }
        } else {
            tipTv.setVisibility(View.GONE);
        }
    }


    private void responseInitPassword(JSONObject jsonObject) {
        SuccessTipActivity.startSuccessTipActivity(this, getString(R.string.init_password_success));
        setResult(RESULT_OK);
        EventBus.getDefault().post(new EventBusMessage(INIT_PASSWORD_OK));
        finish();
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case INIT_ACCOUNT_PASSWORD:
                responseInitPassword(jsonObject);
                break;
            case ACCOUNT_PASSWORD_RULE_VALIDATE:
                responsePasswordValidate();
                break;
            case FIND_ACCOUNT_PASSWORD_BY_CELL_SET_PASSWORD:
                responseResetPassword(jsonObject);
                break;
        }
    }

    private void responseResetPassword(JSONObject jsonObject) {
        SuccessTipActivity.startSuccessTipActivity(this, getString(R.string.reset_password_success));
        finish();
    }

    private void responsePasswordValidate() {
        currentPassword = waitValidatePsd;
        passwordInputView.setText("");
        nextBtn.setVisibility(View.VISIBLE);
        forgetPassWordTv.setVisibility(View.INVISIBLE);
        descTv.setText("请再次输入");
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ErrorBean errorBean = new ErrorBean(jsonObject);
            String name = errorBean.getError().getName();
            if (!"USER_LOGIN_EXPIRED".equals(name) && !"LOGIN_ELSEWHERE".equals(name)) {
                passwordInputView.setText("");
                tipTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(errorBean.getDetailMessage())) {
                    tipTv.setText(errorBean.getDetailMessage());
                } else {
                    tipTv.setText(errorBean.getError().getMessage());
                }
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
