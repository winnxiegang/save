package com.android.xjq.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.banana.commlib.utils.SharePreferenceUtils;
import com.android.jjx.sdk.UI.JjxLoginActivity;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.userInfo.JjxContactBaseActivity;
import com.android.xjq.bean.live.main.AdInfo;
import com.android.xjq.presenters.InitBusinessHelper;
import com.android.xjq.presenters.viewinface.UserLoginView;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.commlib.utils.SharePreferenceUtils.TEST_NETWORK_URL;

public class LoginMainActivity extends JjxContactBaseActivity implements UserLoginView {

    private String roleId;
    private AdInfo adInfo;
    private final static int REQUEST_JJX_LOGIN = 2;

    public static void startLoginMainActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginMainActivity.class);
        activity.startActivity(intent);
    }

    public static void startLoginMainActivity(Activity activity, AdInfo adInfo) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginMainActivity.class);
        intent.putExtra("ad", adInfo);
        activity.startActivity(intent);
    }

    @OnClick(R.id.loginTv)
    public void clickToLogin() {
        //MainActivity.startMainActivity(this);
        JjxLoginActivity.startLoginActivityForResult(this, REQUEST_JJX_LOGIN);
    }

    @OnClick(R.id.bgIv)
    public void logoIv() {
        if (XjqApplication.DEBUG) {//false
            XjqApplication.step = XjqApplication.step == 0 ? 2 : 0;
            InitBusinessHelper.setHasInitApp(false);
            SharePreferenceUtils.putData(TEST_NETWORK_URL, XjqApplication.step);
            XjqApplication.setUrl(XjqApplication.step);
            LibAppUtil.showTip(XjqApplication.getContext(), "已切换" + (XjqApplication.step == 0 ? "线下" : "线上"));
            return;
        }
    }

    @OnClick(R.id.testTv)
    public void testNetwork() {
        //httpOperate.testUrl();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_main);

        Intent intent = getIntent();

        adInfo = (AdInfo) intent.getSerializableExtra("ad");

        ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
