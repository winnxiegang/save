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
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.commlib.utils.HhsUtils.PHONE_PATTERN;
import static com.android.xjq.utils.XjqUrlEnum.MODIFY_BIND_CELL_SEND_NEW_CELL_SMS_ACK;
import static com.android.xjq.utils.XjqUrlEnum.MODIFY_BIND_CELL_SEND_OLD_CELL_SMS_ACK;
import static com.android.xjq.utils.XjqUrlEnum.MODIFY_BIND_CELL_VERIFY_OLD_CELL_SMS_ACK;
import static com.android.xjq.utils.XjqUrlEnum.MODIFY_BIND_CELL_VETIFY_NEW_CELL_SMS_ACK;

/**
 * Created by ajiao on 2017/10/25 0025.
 */

public class BindPhoneNumActivity extends BaseActivity implements IHttpResponseListener {
    private WrapperHttpHelper mHttpRequestHelper;
    private boolean mIsBindOldSuccess = false;
    private String mSmsId;
    private String mRandomString;
    private String mVerifyCellSign;
    private String mNewPhoneNum;
    private String mPhoneNumCode;
    private boolean mIsGetNewSmsCode = false;
    @BindView(R.id.phoneIv)
    ImageView phoneIv;
    @BindView(R.id.checkSmsIv)
    ImageView checkSmsIv;
    @BindView(R.id.set_check_number_ctv)
    CountdownTextView count_down_txt;
    @BindView(R.id.check_code_edt)
    EditText check_code_edt;
    @BindView(R.id.set_new_phone_num_edt)
    EditText new_phone_num_edt;
    @BindView(R.id.confirm_bind_txt)
    TextView confirm_bind_txt;
    @BindView(R.id.bind_title_lay)
    LinearLayout bind_title_lay;
    @BindView(R.id.input_new_phone_lay)
    LinearLayout bind_new_input_lay;
    @BindView(R.id.edit_old_phone_num_txt)
    TextView phone_code_txt;

    @OnClick(R.id.set_check_number_ctv)
    public void getSmsCode() {
        XjqRequestContainer map = null;
        if (!mIsBindOldSuccess) {
            map = new XjqRequestContainer(MODIFY_BIND_CELL_SEND_OLD_CELL_SMS_ACK, true);
        } else {
            if (isBlank(new_phone_num_edt, "手机号输入不能为空")) {
                return;
            }
            mNewPhoneNum = new_phone_num_edt.getText().toString().trim();
            if (!HhsUtils.isMatchered(PHONE_PATTERN, mNewPhoneNum)) {
                showToast("手机号不符合规则");
                return;
            }
            map = new XjqRequestContainer(MODIFY_BIND_CELL_SEND_NEW_CELL_SMS_ACK, true);
            map.put("cell", mNewPhoneNum);
        }
        showProgressDialog("正在获取验证码..");
        mHttpRequestHelper.startRequest(map, true);
    }

    @OnClick(R.id.confirm_bind_txt)
    public void onConfirmClick() {
        mNewPhoneNum = new_phone_num_edt.getText().toString().trim();
        if (mIsBindOldSuccess && TextUtils.isEmpty(mNewPhoneNum)) {
            showToast("请输入手机号");
            return;
        }
        if (mIsBindOldSuccess && !HhsUtils.isMatchered(PHONE_PATTERN, mNewPhoneNum)) {
            showToast("手机号不符合规则");
            return;
        }
        if (TextUtils.isEmpty(mSmsId)) {
            showToast("请先获取验证码");
            return;
        }
        if (mIsBindOldSuccess && !mIsGetNewSmsCode) {
            showToast("请先获取验证码");
            return;
        }
        showProgressDialog("正在验证..");
        XjqRequestContainer map = null;
        String checkCode = check_code_edt.getText().toString().trim();
        if (!mIsBindOldSuccess) {
            map = new XjqRequestContainer(MODIFY_BIND_CELL_VERIFY_OLD_CELL_SMS_ACK, true);
            map.put("smsId", mSmsId);
            map.put("validateCode", checkCode);
        } else {
            map = new XjqRequestContainer(MODIFY_BIND_CELL_VETIFY_NEW_CELL_SMS_ACK, true);
            map.put("smsId", mSmsId);
            map.put("cell", mNewPhoneNum);
            map.put("validateCode", checkCode);
            map.put("verifyCellSign", mVerifyCellSign);
            map.put("randomString", mRandomString);
        }
        mHttpRequestHelper.startRequest(map, true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_num);
        setTitleBar(true, getString(R.string.edit_phone_num),null);
        ButterKnife.bind(this);
        mHttpRequestHelper = new WrapperHttpHelper(this);
        mPhoneNumCode = getIntent().getStringExtra("phoneNumCode");
        initView();
    }

    private void initView() {
        bind_title_lay.setVisibility(View.VISIBLE);
        bind_new_input_lay.setVisibility(View.GONE);
        phone_code_txt.setText(mPhoneNumCode);
        confirm_bind_txt.setText(getResources().getString(R.string.next_step));
        confirm_bind_txt.setClickable(false);
        count_down_txt.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                count_down_txt.setSelected(true);
                count_down_txt.setText(count_down_txt.stringFormat(countdownTime));
            }

            @Override
            public void countdownEnd() {
                count_down_txt.setSelected(false);
                count_down_txt.setEnabled(true);
                count_down_txt.setText("获取验证码");
            }
        });
        check_code_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bind_new_input_lay.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(new_phone_num_edt.getText().toString().trim())) {
                        confirm_bind_txt.setClickable(true);
                        confirm_bind_txt.setBackground(getDrawable(R.drawable.shape_forget_pwd_h));
                    } else {
                        confirm_bind_txt.setClickable(false);
                        confirm_bind_txt.setBackground(getDrawable(R.drawable.shape_forget_pwd_n));
                    }
                } else if (bind_new_input_lay.getVisibility() == View.GONE) {
                    if (!TextUtils.isEmpty(s)) {
                        confirm_bind_txt.setClickable(true);
                        confirm_bind_txt.setBackground(getDrawable(R.drawable.shape_forget_pwd_h));
                    } else {
                        confirm_bind_txt.setClickable(false);
                        confirm_bind_txt.setBackground(getDrawable(R.drawable.shape_forget_pwd_n));
                    }
                }
            }
        });
        new_phone_num_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(check_code_edt.getText().toString().trim()) && !TextUtils.isEmpty(s)) {
                    confirm_bind_txt.setClickable(true);
                    confirm_bind_txt.setBackground(getDrawable(R.drawable.shape_forget_pwd_h));
                } else {
                    confirm_bind_txt.setClickable(false);
                    confirm_bind_txt.setBackground(getDrawable(R.drawable.shape_forget_pwd_n));
                }
            }
        });

        check_code_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkSmsIv.setSelected(hasFocus);
            }
        });

        new_phone_num_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneIv.setSelected(hasFocus);
            }
        });
    }


    @Override
    public void onSuccess(RequestContainer request, Object object) {
        closeProgressDialog();
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case MODIFY_BIND_CELL_SEND_OLD_CELL_SMS_ACK:
                responseGetOldSmsCode(object);
                break;
            case MODIFY_BIND_CELL_VERIFY_OLD_CELL_SMS_ACK:
                responseVerifyOldSmsAck(object);
                break;
            case MODIFY_BIND_CELL_SEND_NEW_CELL_SMS_ACK:
                responseGetNewSmsCode(object);
                break;
            case MODIFY_BIND_CELL_VETIFY_NEW_CELL_SMS_ACK:
                responseVerifyNewSmsAck();
                break;

        }
    }

    private void responseVerifyNewSmsAck() {
        showToast(getString(R.string.bind_success));
        setResult(RESULT_OK);
        finish();
    }

    private void responseGetNewSmsCode(Object object) {
        mIsGetNewSmsCode = true;
        setGetSmsCode(object);
    }

    private void setGetSmsCode(Object object) {
        JSONObject jsonObject = (JSONObject) object;
        int waitNextPrepareSeconds = 0;
        try {
            waitNextPrepareSeconds = jsonObject.getInt("waitNextPrepareSeconds");
            count_down_txt.start(waitNextPrepareSeconds * 1000);
            count_down_txt.setEnabled(false);
            mSmsId = jsonObject.optString("smsId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseVerifyOldSmsAck(Object object) {
        JSONObject jsonObject = (JSONObject) object;
        mRandomString = jsonObject.optString("randomString");
        mVerifyCellSign = jsonObject.optString("verifyCellSign");
        mIsBindOldSuccess = true;
        setVerifyOldSuccessUI();
    }

    private void setVerifyOldSuccessUI() {
        bind_title_lay.setVisibility(View.GONE);
        bind_new_input_lay.setVisibility(View.VISIBLE);
        count_down_txt.cancel();
        count_down_txt.clearAnimation();
        count_down_txt.setEnabled(true);
        count_down_txt.setText("获取验证码");
        count_down_txt.setSelected(false);
        check_code_edt.setText("");
        confirm_bind_txt.setText(getResources().getString(R.string.confirm_bind));
        new_phone_num_edt.requestFocus();
    }

    private void responseGetOldSmsCode(Object object) {
        setGetSmsCode(object);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
        hideProgressDialog();
    }

    public static void startBindPhoneNumActivity(BaseActivity activity, String phoneNumCode) {
        Intent intent = new Intent(activity, BindPhoneNumActivity.class);
        intent.putExtra("phoneNumCode", phoneNumCode);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    protected void onDestroy() {
        count_down_txt.cancel();
        super.onDestroy();
    }
}
