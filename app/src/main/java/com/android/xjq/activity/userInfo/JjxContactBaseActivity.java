package com.android.xjq.activity.userInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.httprequestlib.okhttp.HttpUtil;
import com.android.jjx.sdk.Bean.AuthorizedLoginBean;
import com.android.jjx.sdk.Bean.ReLoginInfoBean;
import com.android.jjx.sdk.UI.BaseActivity;
import com.android.jjx.sdk.utils.MessageEvent;
import com.android.jjx.sdk.utils.RoleInfoUtils;
import com.android.xjq.R;
import com.android.xjq.activity.MainActivity;
import com.android.xjq.presenters.LoginHelper2;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 继承自jjx的BaseActivity,使用Eventbus2.4与jjx通信
 * <p>
 * Created by lingjiu on 2017/11/3.
 */

public class JjxContactBaseActivity extends BaseActivity implements UserLoginView {

    protected final String TAG = getClass().getName();
    private boolean mRegisterToReLogin = false;
    private ProgressDialog progressDialog;
    protected LoginHelper2 loginHelper2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerToReLogin(true);
        loginHelper2 = new LoginHelper2(this);
    }


    @Override
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent != null && messageEvent.isComplete()) {
            finish();
        }

    }

    @Override
    public void onEvent(AuthorizedLoginBean bean) {
        if (bean != null) {
            if (mRegisterToReLogin) {
                // loginHelper2.startLogin(bean);
            }
        }
    }

    protected void registerToReLogin(boolean registerToReLogin) {
        if (registerToReLogin != mRegisterToReLogin) {
            if (registerToReLogin) {
                RoleInfoUtils.saveReLoginRoleInfo(this, packRoleInfo(), true);
            }
            mRegisterToReLogin = registerToReLogin;
        }
    }

    private ReLoginInfoBean packRoleInfo() {
        ReLoginInfoBean infoBean = new ReLoginInfoBean();
        infoBean.setRoleId(LoginInfoHelper.getInstance().getRoleId());
        infoBean.setRoleName(LoginInfoHelper.getInstance().getNickName());
        return infoBean;
    }

    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //将之前打开的activity都销毁掉
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void setImageShow(ImageView imageView, String url) {
        if (url == null) return;

        Picasso.with(getApplicationContext())
                .load(Uri.parse(url))
                .error(R.drawable.user_default_logo)
                .into(imageView);
    }

    @Override
    protected void refresh() {

    }

    @Override
    public void showTip(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog(String loadingTxt) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在登陆..");
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancelOkHttpCall(TAG);
        if (progressDialog != null) progressDialog.dismiss();
        loginHelper2.detachView();
        super.onDestroy();
    }

    @Override
    public void authLoginSuccess(boolean userTagEnabled) {
        toMainActivity();
    }

    @Override
    public void anonymousLoginSuccess() {

    }

    @Override
    public void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject) {
    }

}
