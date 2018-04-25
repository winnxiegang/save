package com.android.xjq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

/**
 * Created by ajiao on 2017/9/14 0014.
 */

public class ForgetPassWordActivity extends BaseActivity implements OnHttpResponseListener {
    private HttpRequestHelper mHttpRequestHelper;
    @BindView(R.id.phoneIv)
    ImageView phoneIv;
    @BindView(R.id.smsIv)
    ImageView smsIv;
    @BindView(R.id.next_txt)
    TextView nextTxt;
    @BindView(R.id.input_phone_num_lay)
    LinearLayout inputPhoneNumLay;
    @BindView(R.id.check_num_lay)
    LinearLayout checkNumLay;
    @BindView(R.id.phone_num_edt)
    EditText phoneEdt;
    @BindView(R.id.checkEdt)
    EditText checkEdt;
    @BindView(R.id.get_check_number)
    CountdownTextView get_check_number;
    private String smsId;

    @OnClick(R.id.next_txt)
    public void goToNext() {
        if (isBlank(phoneEdt, "手机号输入不能为空") || isBlank(checkEdt, "验证码不能为空")) {
            return;
        }

        if (TextUtils.isEmpty(smsId)) {
            showToast("请先获取验证码");
            return;
        }
        secondStepToFindPwd();
    }

    @OnClick(R.id.get_check_number)
    public void getCheckNumber() {
        if (isBlank(phoneEdt, "手机号输入不能为空")) {
            return;
        }
        firstStepToFindPwd();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        init();
    }


    private void init() {
        ButterKnife.bind(this);
        setTitleBar(true, "找回密码",null);
        nextTxt.setClickable(false);
        nextTxt.setBackgroundResource(R.drawable.shape_forget_pwd_n);
        mHttpRequestHelper = new HttpRequestHelper(this, this);
        get_check_number.setEnabled(true);
        get_check_number.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                get_check_number.setText(get_check_number.stringFormat(countdownTime));
            }

            @Override
            public void countdownEnd() {
                get_check_number.setEnabled(true);
                get_check_number.setText("获取验证码");
            }
        });

        phoneEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    nextTxt.setClickable(true);
                    nextTxt.setBackgroundResource(R.drawable.shape_btn_red);
                } else if (charSequence.length() == 0) {
                    nextTxt.setClickable(false);
                    nextTxt.setBackgroundResource(R.drawable.shape_forget_pwd_n);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        checkEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    nextTxt.setClickable(true);
                    nextTxt.setBackgroundResource(R.drawable.shape_btn_red);
                } else if (charSequence.length() == 0) {
                    nextTxt.setClickable(false);
                    nextTxt.setBackgroundResource(R.drawable.shape_forget_pwd_n);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        setFocusListener(phoneEdt, phoneIv);
        setFocusListener(checkEdt, smsIv);
    }

    private void setFocusListener(EditText editText, final ImageView imageView) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imageView.setSelected(hasFocus);
            }
        });
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case FIND_LOGIN_PASSWORD_BY_CELL_SEND_SMS_ACK:
                responseSuccessFirstStep(jsonObject);
                break;
            case FIND_LOGIN_PASSWORD_BY_CELL_VALIDTE_SMS_ACK:
                responseSuccessSecondStep(jsonObject);
                break;
        }
    }

    private void responseSuccessFirstStep(JSONObject jsonObject) {
        try {
            smsId = jsonObject.getString("smsId");
            int timeoutSecond = jsonObject.getInt("timeoutSeconds");
            int waitNextPrepareSeconds = jsonObject.getInt("waitNextPrepareSeconds");
            get_check_number.start(waitNextPrepareSeconds * 1000);
            get_check_number.setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void firstStepToFindPwd() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.FIND_LOGIN_PASSWORD_BY_CELL_SEND_SMS_ACK, false);
        map.put("cell", phoneEdt.getText().toString());
        mHttpRequestHelper.startRequest(map, true);
    }

    private void secondStepToFindPwd() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.FIND_LOGIN_PASSWORD_BY_CELL_VALIDTE_SMS_ACK, false);
        container.put("cell", phoneEdt.getText().toString());
        container.put("smsId", smsId);
        container.put("validateCode", checkEdt.getText().toString().trim());
        mHttpRequestHelper.startRequest(container, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    private void responseSuccessSecondStep(JSONObject jsonObject) {
        try {
            String verifyCellSign = jsonObject.getString("verifyCellSign");
            String randomString = jsonObject.getString("randomString");
            LogUtils.e(TAG, "verifyCellSign = " + verifyCellSign);
            ResetPassWordActivity.startResetPassWordActivity(this, verifyCellSign, randomString, phoneEdt.getText().toString());
            setResult(RESULT_OK);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject, true);
            JSONObject reqError = (JSONObject) jsonObject.get("error");
            String errorName = reqError.getString("name");
            if (errorName.equals("SMS_OUT_ACK_CODE_VALIDATE_TOO_MUCH")) {
                //mSmsOutTooMuch = true;
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

    }
}
