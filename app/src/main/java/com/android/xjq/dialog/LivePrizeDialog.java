package com.android.xjq.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.android.xjq.R;
import com.android.xjq.activity.PrizeCoreActivity;
import com.android.xjq.dialog.base.DialogBase;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/4/10.
 */

public class LivePrizeDialog extends DialogBase implements OnHttpResponseListener {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    private HttpRequestHelper httpRequestHelper;
    private String channelId;
    private String detailUrl;

    public LivePrizeDialog(Context context, String channelId) {
        super(context, R.layout.dialog_live_prize, R.style.dialog_base, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.channelId = channelId;

        ButterKnife.bind(this, rootView);

        init();

        requestData();
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PRIZECORE_QUERY_ACTIVE_ISSUE_INFO, true);

        httpRequestHelper.startRequest(map, false);
    }

    private void init() {
        httpRequestHelper = new HttpRequestHelper(context, this);

        webView.getSettings().setJavaScriptEnabled(true);

        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrizeCoreActivity.startPrizeCoreActivity((Activity) context,detailUrl);
            }
        });
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            if (jsonObject.has("detailUrl")) {

                detailUrl = jsonObject.getString("detailUrl");

                webView.loadUrl(detailUrl+"?hideActiveInfo");
//                PrizeCoreActivity.startPrizeCoreActivity(context, detailUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
