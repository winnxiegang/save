package com.android.xjq.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.utils.SharePreferenceUtils;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.MainActivity;
import com.android.xjq.activity.UserTagActivity;
import com.android.xjq.activity.setting.ForgetPassWordActivity;
import com.android.xjq.presenters.InitBusinessHelper;
import com.android.xjq.presenters.LoginHelper2;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.android.xjq.view.expandtv.ValidateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.commlib.utils.SharePreferenceUtils.TEST_NETWORK_URL;

/**
 * Created by lingjiu on 2017/3/3 16:12.
 */
public class LoginActivity extends BaseActivity implements UserLoginView {
    @BindView(R.id.psdIv)
    ImageView psdIv;
    @BindView(R.id.phoneIv)
    ImageView phoneIv;
    @BindView(R.id.phoneEt)
    EditText phoneEt;
    @BindView(R.id.passwordEt)
    ValidateEditText passwordEt;
    @BindView(R.id.bottomViewLayout)
    View bottomViewLayout;
    @BindView(R.id.passwordShownCb)
    CheckBox passwordShownCb;
    private LoginHelper2 mLoginHelper;

    @OnClick(R.id.forgetPassWordTv)
    public void forgetPwd() {
        startActivity(new Intent(LoginActivity.this, ForgetPassWordActivity.class));
    }

    @OnClick(R.id.loginBtn)
    public void loginBtn() {
        if (isBlank(phoneEt, "请输入手机号") || isBlank(passwordEt, "请输入密码")) {
            return;
        }
        startLogin();
    }

    @OnClick(R.id.registerTv)
    public void register() {
        RegisterActivity.startRegisterActivity(this);
    }

    @OnClick(R.id.logoIv)
    public void logoIv() {
        if (XjqApplication.DEBUG) {
            XjqApplication.step = XjqApplication.step == 0 ? 2 : 0;
            InitBusinessHelper.setHasInitApp(false);
            SharePreferenceUtils.putData(TEST_NETWORK_URL, XjqApplication.step);
            XjqApplication.setUrl(XjqApplication.step);
            ToastUtil.showLong(XjqApplication.getContext(), "已切换" + (XjqApplication.step == 0 ? "线下" : "线上"));
        }
    }

    public static void startLoginActivity(Activity activity, boolean needSdkLogin) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("needSdkLogin", needSdkLogin);
        activity.startActivity(intent);
    }

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_login);
        ButterKnife.bind(this);
        //mNeedSdkLogin = getIntent().getBooleanExtra("needSdkLogin", true);
        if (LoginInfoHelper.getInstance().getUserName() != null) {
            phoneEt.setText(LoginInfoHelper.getInstance().getUserName());
        }
        mLoginHelper = new LoginHelper2(this);
        passwordShownCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                passwordEt.setSelection(passwordEt.getText().length());
            }
        });

        phoneEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneIv.setSelected(hasFocus);
            }
        });

        passwordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                psdIv.setSelected(hasFocus);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String cell = data.getStringExtra("cell");
            phoneEt.setText(cell);
        }
        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void startLogin() {
        mLoginHelper.startLogin(phoneEt.getText().toString().trim(), passwordEt.getText().toString().trim());
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        ErrorBean bean = null;
        try {
            bean = new ErrorBean(jsonObject);
            if (bean.getError() == null)
                return;
            if ("LOGIN_FORBIDDEN".equals(bean.getError().getName())) {
                JSONObject resultMap = jsonObject.getJSONObject("resultMap");
                String forbiddenReason = resultMap.getString("forbiddenReason");
                String gmtEnd;
                if (resultMap.has("gmtEnd")) {
                    gmtEnd = resultMap.getString("gmtEnd");
                } else {
                    gmtEnd = "永久";
                }
                showForbidden(forbiddenReason, gmtEnd);

                phoneEt.setText("");
                passwordEt.setText("");
            } else {
                operateErrorResponseMessage(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showForbidden(String forbiddenReason, String gmtEnd) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("抱歉!您的账号已被冻结\n");
        String forbiddenReasonAndTime = String.format(getString(R.string.account_forbidden), forbiddenReason, gmtEnd);
        ssb.append(SpannableStringHelper.changeTextColor(forbiddenReasonAndTime, ContextCompat.getColor(LoginActivity.this, R.color.dialog_text_color)));
        ShowSimpleMessageDialog dialog = new ShowSimpleMessageDialog(this, com.android.banana.commlib.R.drawable.icon_dialog_title_warning,
                getString(com.android.banana.commlib.R.string.confirm), null, ssb);
    }

    @Override
    public void authLoginSuccess(boolean userTagEnabled) {
        if (userTagEnabled) {
            startActivity(new Intent(LoginActivity.this, UserTagActivity.class));
        } else {
            MainActivity.startMainActivity(this);
        }
        finish();
    }

    @Override
    public void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject) {
        MainActivity.startMainActivity(this);
        finish();
    }

    @Override
    public void anonymousLoginSuccess() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomViewLayout.setBackgroundDrawable(null);
        mLoginHelper.detachView();
    }


}
