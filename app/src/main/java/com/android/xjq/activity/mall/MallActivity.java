package com.android.xjq.activity.mall;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.BottomPayPop;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.utils.XjqUrlEnum;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/11/8.
 */

public class MallActivity extends BaseActivity implements OnHttpResponseListener {

    private String url;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.statusLayout)
    CommonStatusLayout statusLayout;
    private HttpRequestHelper httpRequestHelper;
    private BottomPayPop payPop;

    public static void startMallActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MallActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_mall);
        //setTitleBar(true, "");
        // StatusBarCompat.fix4ImmersiveStatusBar(statusLayout);
        ButterKnife.bind(this);
        httpRequestHelper = new HttpRequestHelper(this, TAG);
        requestUrl();
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

        payPop = new BottomPayPop(this, new PayPsdInputView.OnPasswordListener() {
            @Override
            public void inputListener(boolean isInputComplete, String psd) {
                if (isInputComplete) {
                    jsInterface.sendPayRequest(psd);
                }
            }
        });
        payPop.setShowLoadingAnim(false);
    }

    private void requestUrl() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MALL_URL_QUERY, false);
        httpRequestHelper.startRequest(map);
    }

    private void loadUrl(String url) {
        try {
            new URL(url);
            loadData(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //不符合标准
            statusLayout.showRetry();
            Toast.makeText(this, "出错啦,无法打开网页", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCompleteUrl(String url) {
        if (LoginInfoHelper.getInstance().getUserId() == null) {
            return url;
        }
        XjqRequestContainer requestContainer = new XjqRequestContainer(null, true);
        RequestParams convertRequestParams = requestContainer.getConvertRequestParams();
        String format = convertRequestParams.toString();
        LogUtils.e("商城地址", url + "?" + format);
        return url + "?" + format;
    }

    private void loadData(String url) {
        //先签名
        String completeUrl = getCompleteUrl(url);
        //符合标准
        webView.loadUrl(completeUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                statusLayout.hideStatusView();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e(TAG, "url=" + url);
                if ("http://back/".equals(url)) {
                    finish();
                    return true;
                }
                operateOverloadingUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.e(TAG, "url=" + request.getUrl().toString());
                if ("http://back/".equals(request.getUrl().toString())) {
                    finish();
                    return true;
                }
                operateOverloadingUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private void operateOverloadingUrl(String url) {
        if (url.contains("errorCode=")) {
            int index = url.indexOf("errorCode=");
            int length = "errorCode=".length();
            String errorCode = url.substring(index + length, url.length());
            if ("LOGIN_EXPIRED".equals(errorCode) || "USER_NOT_LOGIN".equals(errorCode)) {
                showReLoginDialog();
            }
        } else if (url.contains("notlogin")) {
            showReLoginDialog();
        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            webView.setVisibility(View.VISIBLE);
            if (jsonObject.has("mallUrl")) {
                String mallUrl = jsonObject.getString("mallUrl");
                loadUrl(mallUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        statusLayout.showRetry();
    }

    @Override
    public void executorFinish() {
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
                    webView.loadUrl("javascript: getPurchasePwd('" + password + "')");
                }
            });
        }

    }
}
