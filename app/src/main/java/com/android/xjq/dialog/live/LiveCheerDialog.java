package com.android.xjq.dialog.live;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.liveScore.livescoreEnum.LiveScoreUrlEnum;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.controller.live.CheerController;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.utils.GetPollingResultUtil;

import org.json.JSONObject;

/**
 * Created by lingjiu on 2018/3/6.
 */

public class LiveCheerDialog extends BaseDialog implements IHttpResponseListener {
    private GetPollingResultUtil cheerPollingUtil;
    private JczqDataBean matchDataBean;
    private CheerController cheerController;

    public LiveCheerDialog(Context context, JczqDataBean dataBean) {
        super(context);
        setShowBottom(true);
        setDimAmount(0.0f);
        matchDataBean = dataBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cheerController = new CheerController();
        cheerController.setContentView(((FrameLayout) findViewById(R.id.container)));

        cheerController.setMatchData(matchDataBean.isFootball(), matchDataBean);
        findViewById(R.id.closeIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        createCheerUtils();
        cheerPollingUtil.startGetData();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cheerPollingUtil.onDestroy();
    }

    private void createCheerUtils() {
        cheerPollingUtil = new GetPollingResultUtil(new GetPollingResultUtil.PollingCallback() {
            @Override
            public void onTick(WrapperHttpHelper httpHelper, int currentRequestCount) {
                RequestFormBody map = new RequestFormBody(LiveScoreUrlEnum.CHANNEL_AREA_CHEER_GAME_BOARD_QUERY, true);
                map.put("raceId", matchDataBean.id);
                map.put("raceType", matchDataBean.isFootball() ? "FOOTBALL" : "BASKETBALL");
                map.setGenericClaz(GroupChatTopic1.class);
                httpHelper.startRequest(map);
            }
        }, this);
        cheerPollingUtil.startGetData();
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_cheer_layout;
    }

    private void responseSuccessCheerQuery(GroupChatTopic1 topic1) {
        cheerController.setTopicData(topic1);
    }


    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        responseSuccessCheerQuery((GroupChatTopic1) o);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ErrorBean errorBean = new ErrorBean(jsonObject);
        if (errorBean.getError() != null && TextUtils.equals(errorBean.getError().getName(), "GAME_BOARD_NOT_EXISTS")) {
            return;
        }
        ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
    }
}
