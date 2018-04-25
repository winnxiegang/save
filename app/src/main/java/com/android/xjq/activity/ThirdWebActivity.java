package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.mall.MallActivity;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.utils.XjqUrlEnum;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.android.banana.commlib.eventBus.EventBusMessage.GO_MAIN_TAB;
import static com.android.xjq.utils.XjqUrlEnum.JUMP_H5_URL_QUERY;

/**
 * Created by lingjiu on 2017/7/31.
 */

public class ThirdWebActivity extends BaseActivity implements IHttpResponseListener {
    public static final String PLAY_GUIDANCE = "PLAY_GUIDANCE";
    public static final String MEDAL_QUERY = "MEDAL_QUERY";
    public static final String UPDATE_MEDAL = "UPDATE_MEDAL";
    public static final String USER_SPECIAL_MEDAL_QUERY = "USER_SPECIAL_MEDAL_QUERY";
    private WebView webView;
    private String url, userId, type;//根据类型获url
    private ProgressBar pb;
    private TextView titleTv;
    WrapperHttpHelper httpHelper = new WrapperHttpHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_third_web);

        getIntentData();
        setTitleBar(true, "", null);

        titleTv = (TextView) findViewById(R.id.titleTv);
        webView = ((WebView) findViewById(R.id.webView));
        pb = ((ProgressBar) findViewById(R.id.pb));

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setLoadWithOverviewMode(true);

        if (TextUtils.isEmpty(type)) {
            //type为空 说明是直接传入了目标页url
            loadUrl();
        } else {
            RequestFormBody formBody = new RequestFormBody(JUMP_H5_URL_QUERY, true);
            formBody.put("jumpH5Page", type);
            formBody.put("userId", userId);
            httpHelper.startRequest(formBody);
        }
    }

    public static void startThirdWebActivity(Activity activity, String url) {
        startThirdWebActivity(activity, url, true);
    }

    public static void startThirdWebActivity(Activity activity, String url, boolean isNeedTitle) {
        Intent intent = new Intent(activity, ThirdWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isNeedTitle", isNeedTitle);
        activity.startActivity(intent);
    }

    /**
     * /**
     * JUMP_H5_URL_QUERY 接口名
     * 传入参数：jumpH5Page
     * 传入authedUserId 返回 String url;
     * <p>
     * <p>
     * PLAY_GUIDANCE("玩法引导页"),
     * <p>
     * MEDAL_QUERY("勋章查询页"),
     * <p>
     * UPDATE_MEDAL("我要升级页"),）
     * p
     *
     * @param activity
     * @param type     {@link ThirdWebActivity#PLAY_GUIDANCE,ThirdWebActivity#MEDAL_QUERY,ThirdWebActivity#UPDATE_MEDAL}
     * @param userId   传入要查看看的那个人的userId
     */
    public static void startThirdWebActivityByType(Activity activity, String type, String userId) {
        Intent intent = new Intent(activity, ThirdWebActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("userId", userId);
        intent.putExtra("isNeedTitle", false);
        activity.startActivity(intent);
    }


    private void loadUrl() {
        try {
            new URL(url);
            loadData(url);
            webView.addJavascriptInterface(new MyJsInterface(), "control");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //不符合标准
            pb.setVisibility(View.GONE);
            Toast.makeText(this, "出错啦,无法打开网页", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(String url) {
        //符合标准
        webView.loadUrl(url);
        LogUtils.e(TAG, "url=" + url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    titleTv.setText(title);
                }
            }

        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e(TAG, "url=" + url);
                if ("http://back/".equals(url)) {
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.e(TAG, "url=" + request.getUrl().toString());
                if ("http://back/".equals(request.getUrl())) {
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }

    private void getIntentData() {
        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.isEmpty(userId)) {
            userId = LoginInfoHelper.getInstance().getUserId();
        }

        boolean isNeedTitle = getIntent().getBooleanExtra("isNeedTitle", true);
        if (!isNeedTitle) {
            findViewById(R.id.titleLayout).setVisibility(View.GONE);
        }

        // url = url + "&userId=" + userId;
    }


    @Override
    public void onSuccess(RequestContainer request, Object o) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case JUMP_H5_URL_QUERY: {
                JSONObject jsonObject = (JSONObject) o;
                this.url = jsonObject.optString("url");
                // this.url = url.replace("userId=", "userId=" + userId);
                loadUrl();
                break;
            }

        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
    }


    public class MyJsInterface {

        @JavascriptInterface
        public void enterRoom(String channelId) {
            finish();
            LiveActivity.startLiveActivity(ThirdWebActivity.this, Integer.valueOf(channelId));
        }

        @JavascriptInterface
        public void exit() {
            finish();
        }

        @JavascriptInterface
        public void openRechargeView() {
            RechargeActivity.startRechargeActivity(ThirdWebActivity.this);
        }

        @JavascriptInterface
        public void program() {
            EventBus.getDefault().post(new EventBusMessage(GO_MAIN_TAB, 1));
        }

        @JavascriptInterface
        public void homepage() {
            HomePageActivity.startHomepageActivity(ThirdWebActivity.this, LoginInfoHelper.getInstance().getUserId());
        }

        @JavascriptInterface
        public void agenda() {
            BettingGameActivity.startBettingGameActivity(ThirdWebActivity.this);
        }

        @JavascriptInterface
        public void myFollowTopic() {
            EventBus.getDefault().post(new EventBusMessage(GO_MAIN_TAB, 0));
        }

        @JavascriptInterface
        public void mall() {
            MallActivity.startMallActivity(ThirdWebActivity.this);
        }

        @JavascriptInterface
        public void showPersonalCard(final String userId) {
            ThirdWebActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PersonalInfoDialog(ThirdWebActivity.this, userId).show();
                }
            });
        }

        /*@JavascriptInterface
        public void openUrl(String url) {
            if (TextUtils.isEmpty(url))
                return;
            switch (url) {
                case OpenUrlType.program:
                    EventBus.getDefault().post(new EventBusMessage(GO_MAIN_TAB, 3));
                    break;
                case OpenUrlType.homepage:
                    HomePageActivity.startHomepageActivity(ThirdWebActivity.this, LoginInfoHelper.getInstance().getUserId());
                    break;
                case OpenUrlType.agenda:
                    BettingGameActivity.startBettingGameActivity(ThirdWebActivity.this);
                    break;
                case OpenUrlType.myFollowTopic:
                    FansFollowListActivity.start2FansFollowListActivity(ThirdWebActivity.this, 1, LoginInfoHelper.getInstance().getUserId());
                    break;
                case OpenUrlType.mall:
                    MallActivity.startMallActivity(ThirdWebActivity.this);
                    break;
            }
        }*/
    }

    @interface OpenUrlType {
        //到节目单
        String program = "program";
        //到个人主页
        String homepage = "homepage";
        //到助威赛程页
        String agenda = "agenda";
        //到我的关注
        String myFollowTopic = "myFollowTopic";
        //去商城
        String mall = "mall";
    }
}
