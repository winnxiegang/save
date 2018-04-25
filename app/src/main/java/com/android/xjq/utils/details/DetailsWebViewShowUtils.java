package com.android.xjq.utils.details;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.android.library.Utils.LogUtils;
import com.android.xjq.activity.ThirdWebActivity;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by zaozao on 2017/12/5.
 */

public class DetailsWebViewShowUtils {
    /**
     * 设置webview参数
     *
     * @param webView
     */
    public static void setWebView(final Activity activity, final WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.getSettings().setBlockNetworkImage(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                webView.loadUrl("javascript:MyApp.resize(document.body.scrollHeight)");

                super.onPageFinished(view, url);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e("kk",url);
//                return XjqUtils.operateOverloadingUrl(activity, url);

                ThirdWebActivity.startThirdWebActivity(activity,url);
                return true;
            }
        });

        webView.addJavascriptInterface(new JSObject(webView, activity), "MyApp");
    }


    private static class JSObject {

        private WebView webView;

        private Context context;

        public JSObject(WebView webView, Context context) {

            this.webView = webView;

            this.context = context;

        }

        @JavascriptInterface
        public void resize(final float height) {
            webView.post(new Runnable() {
                @Override
                public void run() {

                    int layoutHeight = (int) (height * context.getResources().getDisplayMetrics().density + 27);

                    webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight));

                }
            });
        }

        @JavascriptInterface
        public void showImage(final int index, final String imageUrl) {

            webView.post(new Runnable() {
                @Override
                public void run() {

                    ArrayList<String> strArray = new Gson().fromJson(imageUrl, new TypeToken<ArrayList<String>>() {
                    }.getType());

                    FullScreenImageGalleryActivity.startFullScreenImageGalleryActivity((Activity) context, strArray, index);

                }
            });
        }
    }
}
