package com.android.xjq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.userInfo.UserInfoActivity;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.USER_IDENTITY_COMPLETE;

/**
 * Created by ajiao on 2017/10/25 0025.
 */

public class CertificationActivity extends BaseActivity implements IHttpResponseListener {
    private WrapperHttpHelper mHttpRequestHelper;
    @BindView(R.id.name_edt)
    EditText edt_name;
    @BindView(R.id.id_card_edt)
    EditText edt_id_card;
    @BindView(R.id.confirm_txt)
    TextView txt_confirm;
    private String mUserName = "";
    private String mUserId = "";

    @OnClick(R.id.confirm_txt)
    public void onConfirm() {
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setMessage("确认提交实名信息?");
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCommit();
            }
        });
        builder.setPositiveMessage("确认提交");
        builder.setNegativeMessage("取消");
        builder.setShowMessageMiddle(true);
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }

    private void confirmCommit() {
        showProgressDialog();
        mUserName = edt_name.getText().toString().trim();
        mUserId = edt_id_card.getText().toString().trim();
        XjqRequestContainer map = new XjqRequestContainer(USER_IDENTITY_COMPLETE, true);
        map.put("certNo", mUserId);
        map.put("realName", mUserName);
        mHttpRequestHelper.startRequest(map, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_id);
        setTitleBar(true, getString(R.string.real_confirm),null);
        ButterKnife.bind(this);
        mHttpRequestHelper = new WrapperHttpHelper(this);
        initView();
    }

    private void initView() {
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && !TextUtils.isEmpty(edt_id_card.getText().toString().trim())) {
                    txt_confirm.setEnabled(true);
                    txt_confirm.setBackground(ContextCompat.getDrawable(CertificationActivity.this, R.drawable.shape_forget_pwd_h));
                } else {
                    txt_confirm.setEnabled(false);
                    txt_confirm.setBackground(ContextCompat.getDrawable(CertificationActivity.this,R.drawable.shape_forget_pwd_n));
                }
            }
        });
        edt_id_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && !TextUtils.isEmpty(edt_name.getText().toString().trim())) {
                    txt_confirm.setEnabled(true);
                    txt_confirm.setBackground(ContextCompat.getDrawable(CertificationActivity.this, R.drawable.shape_forget_pwd_h));
                } else {
                    txt_confirm.setEnabled(false);
                    txt_confirm.setBackground(ContextCompat.getDrawable(CertificationActivity.this,R.drawable.shape_forget_pwd_n));
                }
            }
        });
    }

    public static void startCertificationActivity(UserInfoActivity activity) {
        Intent intent = new Intent(activity, CertificationActivity.class);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        hideProgressDialog();
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case USER_IDENTITY_COMPLETE:
                setResult(RESULT_OK);
                finish();
                break;
        }

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
        hideProgressDialog();
    }
}
