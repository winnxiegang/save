package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.login.LoginActivity;
import com.loopj.android.http.RequestParams;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/4/6.
 */

public class PrizeCoreActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.pb)
    ProgressBar pb;

    private String url;

    public static void startPrizeCoreActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, PrizeCoreActivity.class);

        intent.putExtra("url", url);

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_prize_core);
        setTitleBar(true, "活动", ContextCompat.getColor(this, R.color.halfAlpha));

        ButterKnife.bind(this);

        getIntentData();

        CookieManager cookieManager = CookieManager.getInstance();

        cookieManager.removeAllCookie();

        webView.getSettings().setJavaScriptEnabled(true);

    }

    private void loadUrl() {
        try {
            new URL(url);
            loadData(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //不符合标准
            pb.setVisibility(View.GONE);
            Toast.makeText(this, "出错啦,无法打开网页", Toast.LENGTH_SHORT).show();
        }
    }

    private void getIntentData() {
        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadData(String url) {
        //符合标准
        webView.loadUrl(getCompleteUrl(url));

        LogUtils.e(TAG, "getCompleteUrl(url)=" + getCompleteUrl(url));

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e(TAG, "url=" + url);
                operateOverloadingUrl(url);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                LogUtils.e(TAG, "url=" + request.getUrl().toString());
                operateOverloadingUrl(request.getUrl().toString());
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }

        });

    }

    private String getCompleteUrl(String url) {
        if (LoginInfoHelper.getInstance().getUserId() == null) {
            return url;
        }
        XjqRequestContainer requestContainer = new XjqRequestContainer(null, true);
        RequestParams convertRequestParams = requestContainer.getConvertRequestParams();
        String format = convertRequestParams.toString();
        LogUtils.e("爆爆爆地址", url + "?" + format);
        return url + "?" + format;
    }

    private void operateOverloadingUrl(String url) {
        if (url.contains("errorCode=")) {
            int index = url.indexOf("errorCode=");
            int length = "errorCode=".length();
            String errorCode = url.substring(index + length, url.length() - 1);
            if ("LOGIN_EXPIRED".equals(errorCode) || "USER_NOT_LOGIN".equals(errorCode)) {
                toLogin(false);
            }
        } else if (url.contains("notlogin")) {
            LoginActivity.startLoginActivity(this, false);
        }
    }
}
