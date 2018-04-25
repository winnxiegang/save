package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;

import org.json.JSONObject;

/**
 * Created by lingjiu on 2017/2/7 18:35.
 */
public class SpeechRuleDescriptionActivity extends BaseActivity implements IHttpResponseListener {

    private WebView webView;
    private WrapperHttpHelper httpRequestHelper;

    public static void startSpeechRuleDescriptionActivity(Activity activity) {

        Intent intent = new Intent();

        intent.setClass(activity, SpeechRuleDescriptionActivity.class);

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_at_rule_description);

        setTitleBar(true, "言论规范协议",null);

        webView = ((WebView) findViewById(R.id.webView));

        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javaScript

        webView.getSettings().setSaveFormData(false);// 不保存表单数据

        webView.getSettings().setSupportZoom(false);// 不支持页面放大功能

        httpRequestHelper = new WrapperHttpHelper(this);

        requestData();

    }

    private void requestData() {
        showProgressDialog();

        XjqRequestContainer map = new XjqRequestContainer(JczjURLEnum.CMS_INFO_CONTENT_QUERY, true);

        map.put("websiteCode", "xjq_cms");

        map.put("infoCode", "ylgf");

        httpRequestHelper.startRequest(map, true);

    }

    @Override
    public void onSuccess(RequestContainer request, Object obj) {
        closeProgressDialog();

        webView.setVisibility(View.VISIBLE);

        String content = ((JSONObject) obj).optString("content");

        webView.loadData(content, "text/html; charset=UTF-8", null);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
    }
}
