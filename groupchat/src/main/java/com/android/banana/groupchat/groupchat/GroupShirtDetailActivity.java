package com.android.banana.groupchat.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.banana.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.BottomPayPop;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lingjiu on 2017/12/9.
 */

public class GroupShirtDetailActivity extends BaseActivity implements IHttpResponseListener {
    private WebView webView;
    private String url;
    private ProgressBar pb;
    private WrapperHttpHelper httpHelper;
    private String groupChatId;
    private BottomPayPop payPop;

    public static void startGroupShirtDetailActivity(Context context) {
        startGroupShirtDetailActivity(context, null);
    }

    public static void startGroupShirtDetailActivity(Context context, String groupChatId) {
        Intent intent = new Intent(context, GroupShirtDetailActivity.class);
        intent.putExtra("groupChatId", groupChatId);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_group_shirt_detail);
        webView = ((WebView) findViewById(R.id.webView));
        pb = ((ProgressBar) findViewById(R.id.pb));
        httpHelper = new WrapperHttpHelper(this);
        getIntentData();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setLoadWithOverviewMode(true);
        final MyJsInterface jsInterface = new MyJsInterface();
        webView.addJavascriptInterface(jsInterface, "control");
        payPop = new BottomPayPop(GroupShirtDetailActivity.this, new PayPsdInputView.OnPasswordListener() {
            @Override
            public void inputListener(boolean isInputComplete, String psd) {
                if (isInputComplete) {
                    jsInterface.sendPayRequest(psd);
                }
            }
        });
        payPop.setShowLoadingAnim(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void getIntentData() {
        groupChatId = getIntent().getStringExtra("groupChatId");
    }

    private void requestData() {
        RequestFormBody container = new RequestFormBody(JczjURLEnum.COST_ARMOR_DETAIL_SIGN_QUERY, true);
        if (!TextUtils.isEmpty(groupChatId)) {
            container.put("groupChatId", groupChatId);
        }
        httpHelper.startRequest(container, true);
    }

    private void checkUrlComplete() {
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

    private void loadData(String url) {
        //符合标准
        LogUtils.e(TAG, "url=" + url);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            //对应js中的alert()方法,可以重写该方法完成与js的交互
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }

        });

    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        try {
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject!=null&&jsonObject.has("signUrl")) {
                url = jsonObject.getString("signUrl");
            }
            checkUrlComplete();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
    }


    public class MyJsInterface {

        @JavascriptInterface
        public void openRechargeView() {
            Intent intent = new Intent();
            intent.setAction("com.android.xjq.recharge");
            startActivity(intent);
        }

        @JavascriptInterface
        public void openPayPwdManagerView() {
            Intent intent = new Intent();
            intent.putExtra("isOperateCompletedFinish", true);
            intent.setAction("com.android.xjq.passwordManagerView");
            startActivity(intent);
        }

        @JavascriptInterface
        public void exit() {
            finish();
        }

        @JavascriptInterface
        public void openPayPanel() {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    payPop.showPop();
                }
            });
        }

        @JavascriptInterface
        public void payResult(String result) {
            // finish();
        }

        public void sendPayRequest(final String password) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript: getPurchasePwd(" + "'" + password + "'" + ")");
                }
            });
        }

    }
}
