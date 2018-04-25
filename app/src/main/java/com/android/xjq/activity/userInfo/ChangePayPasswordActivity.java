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
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.ACCOUNT_PASSWORD_RULE_VALIDATE;
import static com.android.xjq.utils.XjqUrlEnum.CHANGE_ACCOUNT_PASSWORD;
import static com.android.xjq.utils.XjqUrlEnum.CHANGE_ACCOUNT_PASSWORD_VALIDATE_OLD_ACCOUNT_PASSWORD;

/**
 * Created by lingjiu on 2017/11/1.
 */

public class ChangePayPasswordActivity extends BaseActivity implements OnHttpResponseListener, PayPsdInputView.OnPasswordListener {
    public static final int REQUEST_FIND_PASSWORD = 1;
    @BindView(R.id.passwordInputView)
    PayPsdInputView passwordInputView;
    @BindView(R.id.forgetPassWordTv)
    TextView forgetPassWordTv;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.tipTv)
    TextView tipTv;

    private String oldPassword;
    private String newPassword;
    private HttpRequestHelper httpRequestHelper;
    private String waitValidateNewPsd;
    private String waitValidateOldPsd;

    @OnClick(R.id.forgetPassWordTv)
    public void forgetPassWord() {
        FindPayPassWordActivity.startForgetPasswordActivityForResult(this);
    }

    @OnClick(R.id.nextBtn)
    public void next() {
        showProgressDialog(null);
        requestChangePassword();
    }

    public static void startChangePasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, ChangePayPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        httpRequestHelper = new HttpRequestHelper(this, TAG);
        setTitleBar(true, "修改支付密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        descTv.setText("请输入旧密码");
        forgetPassWordTv.setVisibility(View.VISIBLE);
        passwordInputView.setPasswordListener(this);
    }

    @Override
    public void onBackPressed() {
        if (oldPassword != null) {
            if (newPassword != null) {
                newPassword = null;
                descTv.setText("请输入6位数字新支付密码");
                passwordInputView.setText("");
            } else {
                oldPassword = null;
                descTv.setText("请输入旧密码");
                passwordInputView.setText("");
            }
            nextBtn.setVisibility(View.GONE);
            nextBtn.setEnabled(false);
        } else {
            hideSoftKeyboard();
            finish();
        }
    }

    private void validatePasswordRule(String psd) {
        XjqRequestContainer map = new XjqRequestContainer(ACCOUNT_PASSWORD_RULE_VALIDATE, false);
        map.put("accountPassword", psd);
        httpRequestHelper.startRequest(map);
    }

    private void requestChangePassword() {
        XjqRequestContainer map = new XjqRequestContainer(CHANGE_ACCOUNT_PASSWORD, true);
        map.put("oldAccountPassword", oldPassword);
        map.put("newAccountPassword", newPassword);
        httpRequestHelper.startRequest(map);
    }

    private void validateOldPsd(String psd) {
        XjqRequestContainer map = new XjqRequestContainer(CHANGE_ACCOUNT_PASSWORD_VALIDATE_OLD_ACCOUNT_PASSWORD, true);
        map.put("accountPassword", psd);
        httpRequestHelper.startRequest(map);
    }

    @Override
    public void inputListener(boolean isInputComplete, String psd) {
        if (isInputComplete) {
            if (oldPassword == null) {
                waitValidateOldPsd = psd;
                validateOldPsd(psd);
            } else {
                if (newPassword == null) {
                    waitValidateNewPsd = psd;
                    validatePasswordRule(psd);
                } else if (!TextUtils.equals(newPassword, psd)) {
                    showToast("两次密码不一致");
                } else {
                    nextBtn.setEnabled(true);
                }
            }
        } else {
            tipTv.setVisibility(View.GONE);
        }
    }

    private void responseChangePassword(JSONObject jsonObject) {
        SuccessTipActivity.startSuccessTipActivity(this, getString(R.string.change_password_success));
        finish();
    }

    private void responsePasswordValidate() {
        newPassword = waitValidateNewPsd;
        tipTv.setText("");
        passwordInputView.setText("");
        descTv.setText("请再次输入");
        nextBtn.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            finish();
        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case CHANGE_ACCOUNT_PASSWORD:
                responseChangePassword(jsonObject);
                break;
            case ACCOUNT_PASSWORD_RULE_VALIDATE:
                responsePasswordValidate();
                break;
            case CHANGE_ACCOUNT_PASSWORD_VALIDATE_OLD_ACCOUNT_PASSWORD:
                responseOldPasswordValidate();
                break;
        }
    }

    private void responseOldPasswordValidate() {
        tipTv.setText("");
        passwordInputView.setText("");
        forgetPassWordTv.setVisibility(View.INVISIBLE);
        descTv.setText("请输入新密码");
        passwordInputView.setText("");
        oldPassword = waitValidateOldPsd;
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            if (CHANGE_ACCOUNT_PASSWORD == requestContainer.getRequestEnum()) {
                onBackPressed();
                onBackPressed();
            }

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
