package com.android.xjq.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.android.xjq.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    public static final int WX_SUCCESS = 0;
    public static final int WX_FAIL = -1;
    public static final int WX_CANCEL = -2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, WeiXinConstants.appId);
        api.handleIntent(getIntent(), this);

        LogUtils.e(TAG, "onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            switch (resp.errCode) {
                case WX_SUCCESS:
                    EventBus.getDefault().post(new EventBusMessage(EventBusMessage.WEIXIN_RECHARGE_SUCCESS));
                    break;
                case WX_FAIL:
                    EventBus.getDefault().post(new EventBusMessage(EventBusMessage.WEIXIN_RECHARGE_FAIL));
                    break;
                case WX_CANCEL:
                    LibAppUtil.showTip(this, getString(R.string.cancel_recharge));
                    break;
            }
            finish();
        }
    }
}