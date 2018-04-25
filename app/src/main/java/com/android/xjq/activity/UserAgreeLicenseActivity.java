package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAgreeLicenseActivity extends BaseActivity {


    @BindView(R.id.webView)
    WebView webView;

    public static void startUserAgreeLicenseActivity(Activity activity, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(activity, UserAgreeLicenseActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_agree_license);

        ButterKnife.bind(this);

        setTitleBar(true, "用户注册协议",null);

        String url = getIntent().getStringExtra("url");

        webView.loadUrl(url);

    }


}
