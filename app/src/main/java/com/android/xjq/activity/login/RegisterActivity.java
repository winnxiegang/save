package com.android.xjq.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2017/3/3 18:12.
 */
public class RegisterActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.phoneIv)
    ImageView phoneIv;
    @BindView(R.id.smsIv)
    ImageView smsIv;
    @BindView(R.id.phoneEt)
    EditText phoneEt;
    @BindView(R.id.smsCodeEt)
    EditText smsCodeEt;
    @BindView(R.id.getSmsCodeTv)
    CountdownTextView getSmsCodeTv;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    private String mSmsId = null;
    private HttpRequestHelper httpRequestHelper;
    private String mRandomString = null;
    private String mCellSign = null;

    @OnClick(R.id.getSmsCodeTv)
    public void getSmsCode() {
        if (isBlank(phoneEt, "手机号输入不能为空")) {
            return;
        }
        if (!HhsUtils.isMatchered(HhsUtils.PHONE_PATTERN, phoneEt.getText())) {
            showToast("请输入正确的手机号");
            return;
        }

        postHttpSmsCode();
    }

    @OnClick(R.id.registerBtn)
    public void register() {
        if (isBlank(phoneEt, "手机号输入不能为空") || isBlank(smsCodeEt, "验证码不能为空")) {
            return;
        }
        if (TextUtils.isEmpty(mSmsId)) {
            showToast("请先获取验证码");
            return;
        }

        postRegisterVerifySms();
    }

    public static void startRegisterActivity(Activity activity) {
        activity.startActivityForResult(new Intent(activity, RegisterActivity.class), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_register);
        ButterKnife.bind(this);
        setTitleBar(true, "注册",null);
        httpRequestHelper = new HttpRequestHelper(this, TAG);
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            Intent intent = new Intent();
            intent.putExtra("cell", phoneEt.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void setListener() {
        //默认不可编辑,获取短信验证码之后才可编辑
        //smsCodeEt.setFocusable(false);
        //smsCodeEt.setFocusableInTouchMode(false);

        getSmsCodeTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                getSmsCodeTv.setText(getSmsCodeTv.stringFormat(countdownTime));
            }

            @Override
            public void countdownEnd() {
                getSmsCodeTv.setEnabled(true);
                getSmsCodeTv.setText("获取验证码");
            }
        });

        smsCodeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!smsCodeEt.isFocusable()) {
                    LibAppUtil.showTip(RegisterActivity.this, "请获取验证码");
                }
            }
        });

        smsCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerBtn.setEnabled(!TextUtils.isEmpty(s));
            }
        });

        phoneEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneIv.setSelected(hasFocus);
            }
        });

        smsCodeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                smsIv.setSelected(hasFocus);
            }
        });
    }

    private void postHttpSmsCode() {
        showProgressDialog("正在获取验证码..");
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.REGISTER_SEND_SMS_ACK, false);
        map.put("cell", phoneEt.getText().toString());
        httpRequestHelper.startRequest(map);
    }

    private void postRegisterVerifySms() {
        showProgressDialog("正在验证..");
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.REGISTER_VALIDATE_SMS_ACK, false);
        map.put("cell", phoneEt.getText().toString());
        map.put("smsId", mSmsId);//validateCode
        map.put("validateCode", smsCodeEt.getText().toString());
        httpRequestHelper.startRequest(map);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        XjqUrlEnum urlEnum = (XjqUrlEnum) requestContainer.getRequestEnum();
        switch (urlEnum) {
            case REGISTER_SEND_SMS_ACK:
                mRandomString = null;
                mCellSign = null;
                responseSuccessGetSmsCode(jsonObject);
                break;
            case REGISTER_VALIDATE_SMS_ACK:
                responseSuccessRegisterSmsVerifySms(jsonObject);
                break;
        }
    }

    private void responseSuccessRegisterSmsVerifySms(JSONObject jsonObject) {
        try {
            mCellSign = jsonObject.getString("verifyCellSign");
            mRandomString = jsonObject.getString("randomString");
            RegisterSecondStepActivity.startRegisterSecondStepActivityForResult(this, phoneEt.getText().toString(), mRandomString, mCellSign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void responseSuccessGetSmsCode(JSONObject jsonObject) {
        try {
            mSmsId = jsonObject.getString("smsId");
            int timeoutSecond = jsonObject.getInt("timeoutSeconds");
            int waitNextPrepareSeconds = jsonObject.getInt("waitNextPrepareSeconds");
            getSmsCodeTv.start(waitNextPrepareSeconds * 1000);
            getSmsCodeTv.setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ErrorBean bean = new ErrorBean(jsonObject);
            String name = bean.getError().getName();
            if ("REGISTER_TIMEOUT".equals(name)) {//如果超时，需要将之前验证过验证码重新设置一遍
                mRandomString = null;
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
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSmsCodeTv.cancel();//如果没有倒计时完成，则关闭当前倒计时
        httpRequestHelper = null;
    }

}
