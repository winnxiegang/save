package com.android.xjq.activity.login;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.activity.UserAgreeLicenseActivity;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.view.expandtv.ValidateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.CLIENT_PROTOCOL_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.REGISTER;

/**
 * Created by lingjiu on 2017/11/20.
 */

public class RegisterSecondStepActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.psdIv)
    ImageView psdIv;
    @BindView(R.id.confirmPsdIv)
    ImageView confirmPsdIv;
    @BindView(R.id.nickNameIv)
    ImageView nickNameIv;
    @BindView(R.id.passwordEt)
    ValidateEditText passwordEt;
    @BindView(R.id.confirmPasswordEt)
    ValidateEditText confirmPasswordEt;
    @BindView(R.id.nickNameEt)
    ValidateEditText nickNameEt;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    @BindView(R.id.userAgreeTv)
    TextView userAgreeTv;
    @BindView(R.id.passwordShownCb)
    CheckBox passwordShownCb;
    @BindView(R.id.confirmPasswordShownCb)
    CheckBox confirmPasswordShownCb;

    private HttpRequestHelper httpRequestHelper;
    private String mRandomString = null;
    private String mCellSign = null;
    private String cell;

    @OnClick(R.id.userAgreeTv)
    public void userAgree() {
        postUserAgree();
    }

    @OnClick(R.id.registerBtn)
    public void register() {
        if (isBlank(passwordEt, "请输入密码") ||
                isBlank(confirmPasswordEt, "再次输入密码不能为空") ||
                isBlank(nickNameEt, "请输入昵称")) {
            return;
        }
        if (!passwordEt.getText().toString().equals(confirmPasswordEt.getText().toString())) {
            LibAppUtil.showTip(this, "两次密码输入不一致");
            return;
        }
        if (mRandomString != null) {
            startRegister();
        }
    }

    public static void startRegisterSecondStepActivityForResult(Activity activity, String cell, String randomString, String cellSign) {
        Intent intent = new Intent(activity, RegisterSecondStepActivity.class);
        intent.putExtra("randomString", randomString);
        intent.putExtra("cellSign", cellSign);
        intent.putExtra("cell", cell);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_register_second_step);
        ButterKnife.bind(this);
        setTitleBar(true, "注册");
        httpRequestHelper = new HttpRequestHelper(this, TAG);
        getIntentData();
        initView();
        requestData();
    }

    private void initView() {
        setUserAgree();
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

        confirmPasswordShownCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    confirmPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    confirmPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                confirmPasswordEt.setSelection(confirmPasswordEt.getText().length());
            }
        });

        setFocusListener(passwordEt, psdIv);
        setFocusListener(confirmPasswordEt, confirmPsdIv);
        setFocusListener(nickNameEt, nickNameIv);
    }

    private void setFocusListener(EditText editText, final ImageView imageView) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imageView.setSelected(hasFocus);
            }
        });
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.BASE_CONFIG_QUERY, false);
        map.put("configNames", "LOGIN_PASSWORD_VALIDATE_REG_EXP,LOGIN_NAME_VALIDATE_REG_EXP");
        httpRequestHelper.startRequest(map, false);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        this.mRandomString = intent.getStringExtra("randomString");
        this.mCellSign = intent.getStringExtra("cellSign");
        this.cell = intent.getStringExtra("cell");
    }

    private void setUserAgree() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("我已同意并阅读");
        ssb.append(SpannableStringHelper.changeTextColor("《用户注册协议》", Color.parseColor("#1f9ce4")));
        userAgreeTv.setText(ssb);
    }

    private void startRegister() {
        httpRequestHelper.startRequest(postRegister(), true);
    }

    private XjqRequestContainer postRegister() {
        showProgressDialog("正在注册..");
        XjqRequestContainer map = new XjqRequestContainer(REGISTER, false);
        map.put("loginName", nickNameEt.getText().toString());
        map.put("cell", cell);
        map.put("loginPassword", passwordEt.getText().toString());
        map.put("randomString", mRandomString);
        map.put("verifyCellSign", mCellSign);
        return map;
    }

    private void postUserAgree() {
        showProgressDialog();
        XjqRequestContainer map = new XjqRequestContainer(CLIENT_PROTOCOL_QUERY, false);
        map.put("protocolType", "register");
        httpRequestHelper.startRequest(map, true);
    }

    private void responseSuccessRegister() {
        LibAppUtil.showTip(this, "注册成功");
        setResult(RESULT_OK);
        finish();
        //注册成功之后，不用执行登录操作
       /* mLoginHelper.startLogin(phoneEt.getText().toString(),
                passwordEt.getText().toString());*/
    }


    private void responseSuccessProtocolQuery(JSONObject jsonObject) {
        try {
            String protocolUrl = jsonObject.getString("protocolUrl");
            UserAgreeLicenseActivity.startUserAgreeLicenseActivity(this, protocolUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessConfig(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("configMap")) {
            JSONObject configMap = jsonObject.getJSONObject("configMap");
            String psdRex = configMap.getString("LOGIN_PASSWORD_VALIDATE_REG_EXP");
            String loginNameRex = configMap.getString("LOGIN_NAME_VALIDATE_REG_EXP");
            if (!TextUtils.isEmpty(psdRex) && !TextUtils.isEmpty(loginNameRex)) {
                nickNameEt.setInputFilter(loginNameRex, 12);
                passwordEt.setInputFilter(psdRex, 20);
                confirmPasswordEt.setInputFilter(psdRex, 20);
            }

        }
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case REGISTER:
                    responseSuccessRegister();
                    break;
                case BASE_CONFIG_QUERY:
                    responseSuccessConfig(jsonObject);
                case CLIENT_PROTOCOL_QUERY:
                    responseSuccessProtocolQuery(jsonObject);
                    break;
            }
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
        hideProgressDialog();
    }
}
