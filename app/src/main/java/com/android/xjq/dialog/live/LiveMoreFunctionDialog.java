package com.android.xjq.dialog.live;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.GridView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.view.OnMyClickListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.ThirdWebActivity;
import com.android.xjq.adapter.live.LiveMoreFunctionAdapter;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.model.live.LiveFunctionEnum;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lingjiu on 2018/2/7.
 */

public class LiveMoreFunctionDialog extends BaseDialog implements IHttpResponseListener {

    private GridView gridView;
    private WebView webView;
    private LiveMoreFunctionAdapter mAdapter;

    private WrapperHttpHelper httpHelper;
    private String signParams;

    public LiveMoreFunctionDialog(Context context, int orientation) {
        super(context);
        setDimAmount(0.0f);

        //横竖屏时分别设定不同的宽度值、弹出方位以及动画
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setHeight(260);
            setShowBottom(true);
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setIsNeedConverPx(false);
            setWidth(DimensionUtils.getScreenWidth(context) / 5 * 3);
            setHeight(DimensionUtils.getScreenHeight(context));
            setAnimStyle(R.style.dialog_anim_side);
            setGravity(Gravity.RIGHT);
        }
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_layout_live_more_function;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpHelper = new WrapperHttpHelper(this);
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PRIZECORE_QUERY_ACTIVE_ISSUE_INFO, true);
        signParams = map.getConvertRequestParams().toString();
        httpHelper.startRequest(map);
    }

    @Override
    public void convertView(ViewHolder holder, final BaseDialog dialog) {
        gridView = ((GridView) holder.getView(R.id.gridView));
        webView = ((WebView) holder.getView(R.id.webView));

        mAdapter = new LiveMoreFunctionAdapter(mContext);
        gridView.setAdapter(mAdapter);

        webView.addJavascriptInterface(new MyJsInterface(), "control");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        mAdapter.setOnMyClickListener(new OnMyClickListener<LiveFunctionEnum>() {
            @Override
            public void onClick(View view, int position, LiveFunctionEnum liveFunctionEnum) {
                switch (liveFunctionEnum) {
                    case LUCK_DRAW:
                        ((LiveActivity) mContext).showLuckyDrawView();
                        break;
                    case GAME_PK:
                        ((LiveActivity) mContext).showPKView();
                        break;
                    case GAME_CHEER:
                        ((LiveActivity) mContext).showCheerView();
                        break;
                    case COUPON:
                        ((LiveActivity) mContext).showSendCouponView(gridView);
                        break;
                }
                dialog.dismiss();
            }
        });

    }


    public class MyJsInterface {

        @JavascriptInterface
        public void openUrl(String url) {
            ThirdWebActivity.startThirdWebActivity(((Activity) mContext), url);
        }
    }


    @Override
    public void onSuccess(RequestContainer request, Object o) {
        try {
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.has("detailUrl")) {
                String detailUrl = jsonObject.getString("detailUrl");
                webView.loadUrl(detailUrl + "?" + signParams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
    }
}
