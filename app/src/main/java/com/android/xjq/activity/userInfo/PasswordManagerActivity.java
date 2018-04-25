package com.android.xjq.activity.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2017/11/1.
 */

public class PasswordManagerActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.changePasswordTv)
    TextView changePasswordTv;
    @BindView(R.id.changePasswordLayout)
    RelativeLayout changePasswordLayout;
    @BindView(R.id.findPasswordLayout)
    RelativeLayout findPasswordLayout;
    @BindView(R.id.contentLayout)
    CommonStatusLayout contentLayout;
    private HttpRequestHelper httpRequestHelper;
    private boolean accountPasswordSet;
    private boolean isOperateCompletedFinish;

    @OnClick(R.id.findPasswordLayout)
    public void findPassword() {
        FindPayPassWordActivity.startForgetPasswordActivity(this);
    }

    @OnClick(R.id.changePasswordLayout)
    public void changePassword() {
        if (accountPasswordSet) {
            ChangePayPasswordActivity.startChangePasswordActivity(this);
        } else {
            InitSetPasswordActivity.startInitSetPasswordActivity(this);
        }
    }

    public static void startPasswordManagerActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PasswordManagerActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_password_manager);
        ButterKnife.bind(this);
        setTitleBar(true, "支付安全",null);
        httpRequestHelper = new HttpRequestHelper(this, TAG);
        requestUserInfo();
        isOperateCompletedFinish = getIntent().getBooleanExtra("isOperateCompletedFinish", false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage message) {
        if (message.isInitPasswordOk()) {
            accountPasswordSet = true;
            changePasswordLayout.setVisibility(View.VISIBLE);
            findPasswordLayout.setVisibility(View.VISIBLE);
            changePasswordTv.setText("修改支付密码");
            if (isOperateCompletedFinish)
                finish();
        }
    }


    private void requestUserInfo() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MY_USER_INFO_QUERY, true);
        httpRequestHelper.startRequest(map, true);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            if (jsonObject.has("userInfo")) {
                contentLayout.hideStatusView();
                JSONObject userInfo = jsonObject.getJSONObject("userInfo");
                accountPasswordSet = userInfo.getBoolean("accountPasswordSet");
                if (accountPasswordSet) {
                    changePasswordLayout.setVisibility(View.VISIBLE);
                    findPasswordLayout.setVisibility(View.VISIBLE);
                } else {
                    changePasswordLayout.setVisibility(View.VISIBLE);
                    changePasswordTv.setText("初始化支付密码");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        contentLayout.showRetry();
    }

    @Override
    public void executorFinish() {

    }
}
