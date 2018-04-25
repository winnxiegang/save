package com.android.xjq.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.login.LoginActivity;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.expandtv.ValidateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.FIND_LOGIN_PASSWORD_BY_CELL_SET_PASSWORD;

/**
 * Created by ajiao on 2017/9/15 0015.
 */

public class ResetPassWordActivity extends BaseActivity implements OnHttpResponseListener {
    private HttpRequestHelper mHttpRequestHelper;

    @BindView(R.id.psdIv)
    ImageView psdIv;
    @BindView(R.id.confirmPsdIv)
    ImageView confirmPsdIv;
    @BindView(R.id.new_pwd_edt)
    ValidateEditText newPwdEdt;
    @BindView(R.id.confirm_pwd_edt)
    ValidateEditText confirmPwdEdt;
    @BindView(R.id.confirm_pwd_lay)
    LinearLayout confirmPwdLay;
    @BindView(R.id.confirm_success_lay)
    LinearLayout mSuccessLay;
    public static final String REG_STR = "!@#$%^&*()";
    @BindView(R.id.confirm_reset_pwd_txt)
    TextView confirmTxt;
    @BindView(R.id.titleLayout)
    LinearLayout titleLay;
    @BindView(R.id.passwordShownCb)
    CheckBox passwordShownCb;
    @BindView(R.id.confirmPasswordShownCb)
    CheckBox confirmPasswordShownCb;

    @OnClick(R.id.confirm_success_txt)
    public void onConfirmClick() {
        startActivity(new Intent(ResetPassWordActivity.this, LoginActivity.class));
        setResult(RESULT_OK);
        this.finish();
    }

    @OnClick(R.id.confirm_reset_pwd_txt)
    public void onConfirmPwdClick() {
        if (isBlank(newPwdEdt, "密码不能为空") || isBlank(confirmPwdEdt, "密码不能为空")) {
            return;
        }
        if (!TextUtils.equals(newPwdEdt.getText().toString(), confirmPwdEdt.getText().toString())) {
            showToast("两次密码输入不一致");
            return;
        }
        ForthStepToFindPwd();
    }

    public static void startResetPassWordActivity(Activity activity, String verifyCellSign, String randomString, String cell) {
        Intent intent = new Intent(activity, ResetPassWordActivity.class);
        intent.putExtra("verifyCellSign", verifyCellSign);
        intent.putExtra("randomString", randomString);
        intent.putExtra("cell", cell);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        setTitleBar(true, "找回密码",null);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mHttpRequestHelper = new HttpRequestHelper(this, this);
        requestData();
        confirmTxt.setEnabled(false);
        newPwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && confirmPwdEdt.getText().length() > 0) {
                    confirmTxt.setEnabled(true);
                    confirmTxt.setBackgroundResource(R.drawable.shape_btn_red);
                } else if (charSequence.length() == 0) {
                    confirmTxt.setBackgroundResource(R.drawable.shape_forget_pwd_n);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        confirmPwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && newPwdEdt.getText().length() > 0) {
                    confirmTxt.setEnabled(true);
                    confirmTxt.setBackgroundResource(R.drawable.shape_btn_red);
                } else if (charSequence.length() == 0) {
                    confirmTxt.setBackgroundResource(R.drawable.shape_forget_pwd_n);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        passwordShownCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newPwdEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    newPwdEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                newPwdEdt.setSelection(newPwdEdt.getText().length());
            }
        });

        confirmPasswordShownCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    confirmPwdEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    confirmPwdEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                confirmPwdEdt.setSelection(confirmPwdEdt.getText().length());
            }
        });

        setFocusListener(newPwdEdt, psdIv);
        setFocusListener(confirmPwdEdt, confirmPsdIv);
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
        mHttpRequestHelper.startRequest(map, false);
    }

    private void ForthStepToFindPwd() {
        XjqRequestContainer container = new XjqRequestContainer(FIND_LOGIN_PASSWORD_BY_CELL_SET_PASSWORD, false);
        String verifyCellSign = getIntent().getStringExtra("verifyCellSign");
        String randomString = getIntent().getStringExtra("randomString");
        String cell = getIntent().getStringExtra("cell");
        container.put("cell", cell);
        container.put("randomString", randomString);
        container.put("verifyCellSign", verifyCellSign);
        container.put("newPassword", newPwdEdt.getText().toString());
        mHttpRequestHelper.startRequest(container, true);
    }

    private boolean isMatchSetPwdRule(String str) {
        boolean isMatch = false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                if (isSupportChar(str.charAt(i))) {
                    isMatch = true;
                } else {
                    isMatch = false;
                }
            } else {
                isMatch = true;
            }
        }
        return isMatch;
    }

    private boolean isSupportChar(char curchar) {
        boolean b = false;
        for (int i = 0; i < REG_STR.length(); i++) {
            if (REG_STR.charAt(i) == curchar) {
                b = true;
                break;
            }
        }
        return b;
    }

    private void responseSuccessForthStep(JSONObject jsonObject) {
        try {
            boolean result = jsonObject.getBoolean("success");
            if (result) {
                confirmPwdLay.setVisibility(View.GONE);
                mSuccessLay.setVisibility(View.VISIBLE);
                setTitleNone();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void responseSuccessConfig(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("configMap")) {
            JSONObject configMap = jsonObject.getJSONObject("configMap");
            String psdRex = configMap.getString("LOGIN_PASSWORD_VALIDATE_REG_EXP");
            if (!TextUtils.isEmpty(psdRex)) {
                newPwdEdt.setInputFilter(psdRex,20);
                confirmPwdEdt.setInputFilter(psdRex,20);
            }

        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case BASE_CONFIG_QUERY:
                    responseSuccessConfig(jsonObject);
                    break;
                case FIND_LOGIN_PASSWORD_BY_CELL_SET_PASSWORD:
                    responseSuccessForthStep(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject, true);
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
