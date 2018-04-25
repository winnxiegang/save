package com.android.xjq.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.FetCouponValidateUtils;
import com.android.banana.commlib.coupon.GroupCouponController;
import com.android.banana.commlib.coupon.LiveCouponCallback;
import com.android.banana.commlib.coupon.SendCouponController;
import com.android.banana.commlib.coupon.SendCouponSuccessListener;
import com.android.banana.commlib.coupon.couponenum.CouponPlatformType;
import com.android.banana.commlib.coupon.couponenum.CouponPublishType;
import com.android.banana.commlib.coupon.couponenum.CouponUrlEnum;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.banana.commlib.view.MyTabLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.dialog.base.DialogBase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.banana.commlib.coupon.couponenum.CouponUrlEnum.GET_COUPON_CONFIG;

/**
 * Created by lingjiu on 2017/7/18.
 */

public class LiveCouponDialog extends DialogBase implements OnHttpResponseListener, View.OnClickListener, LiveCouponCallback {
    private final static int POS_GROUP_COUPON = 0;//频道红包
    private final static int POS_SEND_COUPON = 1;//发红包

    private HttpRequestHelper httpRequestHelper;
    private MyTabLayout mTabLayout;
    private FrameLayout contentLayout;

    //当前发红包所在的平台id以及type
    private String objectId;
    private String objectType;
    private SendCouponSuccessListener sendCouponSuccessListener;

    private String title;
    private String couponType;
    private GroupCouponInfoBean couponInfoBean;
    private SendCouponController sendCouponController;
    private GroupCouponController groupCouponController;
    private CommonStatusLayout statusLayout;

    public void setSendCouponSuccessListener(SendCouponSuccessListener sendCouponSuccessListener) {
        this.sendCouponSuccessListener = sendCouponSuccessListener;
    }

    public LiveCouponDialog(Context context, int orientation, String objectType, String objectId) {
        super(context, R.layout.dialog_send_coupon, R.style.dialog_base_no_shadow, orientation);
        this.objectType = objectType;
        this.objectId = objectId;

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = -getScreenHeight(context);
        lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        dialogWindow.setAttributes(lp);

        httpRequestHelper = new HttpRequestHelper(context, this);
        mTabLayout = ((MyTabLayout) rootView.findViewById(com.android.banana.commlib.R.id.tabLayout));
        rootView.findViewById(com.android.banana.commlib.R.id.closeIv).setOnClickListener(this);
        contentLayout = ((FrameLayout) rootView.findViewById(com.android.banana.commlib.R.id.contentLayout));
        statusLayout = ((CommonStatusLayout) rootView.findViewById(com.android.banana.commlib.R.id.statusLayout));
        //mJcdxController = new JcdxController();
        sendCouponController = new SendCouponController(((BaseActivity) context), this);
        groupCouponController = new GroupCouponController(((BaseActivity) context), this);

        if (CouponPlatformType.GROUP_CHAT.name().equals(objectType))
            rootView.findViewById(com.android.banana.commlib.R.id.sendTimeLayout).setVisibility(View.INVISIBLE);
        setUpTabLayout();

    }

    private void querySendCouponConfig() {
        httpRequestHelper.startRequest(new XjqRequestContainer(GET_COUPON_CONFIG, true), false);
    }


    private void setUpTabLayout() {
        mTabLayout
                .setTabMargin(40)
                .setBottomLineColor(Color.TRANSPARENT)
                .addTabs(context.getString(com.android.banana.commlib.R.string.pos_group_coupon), context.getString(com.android.banana.commlib.R.string.pos_send_coupon))
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        if (reSelected)
                            return;
                        statusLayout.showLoading();
                        switch (tab.getPosition()) {
                            case POS_GROUP_COUPON:
                                groupCouponController.setContentView(contentLayout);
                                queryGroupCouponList(1);
                                break;
                            case POS_SEND_COUPON:
                                sendCouponController.setContentView(contentLayout);
                                querySendCouponConfig();
                                break;
                        }
                    }
                }).setSelectTab(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {
        if (event instanceof GroupCouponInfoBean) {
            // mAdapter.updateItemData((GroupCouponInfoBean) event);
            groupCouponController.updateItemData((GroupCouponInfoBean) event);
        } else if (event instanceof EventBusMessage) {
            if (((EventBusMessage) event).isInitPasswordOk()) {
                sendCouponController.setPasswordSuccess();
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        httpRequestHelper.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            statusLayout.hideStatusView();
            switch (((CouponUrlEnum) requestContainer.getRequestEnum())) {
                case GET_COUPON_CONFIG:
                    sendCouponController.setConfigData(jsonObject);
                    break;
                case COUPON_CREATE:
                    responseSuccessCouponCreate(jsonObject);
                    break;
                case GET_COUPON_FETCH_INFO:
                    groupCouponController.responseSuccessGroupCouponInfo(jsonObject);
                    break;
                case QUERY_AVAILABLE_COUPON:
                    groupCouponController.responseSuccessCouponAvailable(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessCouponCreate(JSONObject jsonObject) throws JSONException {
        if (sendCouponController != null) sendCouponController.setCreateCouponResult(true);
        dismiss();
        String groupCouponId = null;
        if (jsonObject.has("groupCouponId")) {
            groupCouponId = jsonObject.getString("groupCouponId");
        }
        if (sendCouponSuccessListener != null) {
            sendCouponSuccessListener.sendCouponSuccess(couponType, groupCouponId, title);
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((CouponUrlEnum) requestContainer.getRequestEnum())) {
                case QUERY_AVAILABLE_COUPON:
                    FetCouponValidateUtils fetCouponValidateUtils = new FetCouponValidateUtils((BaseActivity) context, couponInfoBean);
                    fetCouponValidateUtils.handleFalse(jsonObject);
                    return;
                case COUPON_CREATE:
                    ErrorBean errorBean = new ErrorBean(jsonObject);
                    String name = errorBean.getError().getName();
                    if (sendCouponController != null)
                        sendCouponController.setCreateCouponResult(false);
                    if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(name)) {
                        ToastUtil.showLong(context.getApplicationContext(), "您的礼金不够");
                        return;
                    }

                    break;
            }
            ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        if (GET_COUPON_CONFIG == ((CouponUrlEnum) requestContainer.getRequestEnum())) {
            //statusLayout.showRetry();
        } else if (CouponUrlEnum.COUPON_CREATE == requestContainer.getRequestEnum()) {
            if (sendCouponController != null) sendCouponController.setCreateCouponResult(false);
        }
    }

    @Override
    public void executorFinish() {
    }

    @Override
    public void createCoupon(String psd, String amountAllocateType, String couponType, String couponPublishType,
                             String gmtPublish, String title, String totalAmount, String totalCount) {
        this.title = title;
        this.couponType = couponType;
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.COUPON_CREATE, true);
        //map.put("accountPassword", psd);
        map.put("sendUserId", LoginInfoHelper.getInstance().getUserId());
        map.put("couponFetchType", "ALL");//FOLLOW_USER
        map.put("amountAllocateType", amountAllocateType);//AVERAGE
        map.put("couponType", couponType);
        map.put("couponPublishType", couponPublishType);
        if (CouponPublishType.TIMED.name().equals(couponPublishType)) {
            map.put("gmtPublish", gmtPublish);
        }
        map.put("platformObjectType", objectType);
        map.put("platformObjectId", objectId);//channelId
        map.put("objectType", objectType);
        map.put("title", title);
        map.put("totalAmount", totalAmount);
        map.put("totalCount", totalCount);
        httpRequestHelper.startRequest(map, false);
    }

    @Override
    public void queryGroupCouponList(int currentPage) {
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.GET_COUPON_FETCH_INFO, true);
        map.put("platformObjectId", objectId);
        map.put("platformObjectType", objectType);
        map.put("currentPage", currentPage + "");
        httpRequestHelper.startRequest(map, false);
    }

    @Override
    public void queryAvailableCoupon(GroupCouponInfoBean groupCouponInfo) {
        this.couponInfoBean = groupCouponInfo;
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.QUERY_AVAILABLE_COUPON, true);
        map.put("groupCouponId", groupCouponInfo.getCouponNo());
        httpRequestHelper.startRequest(map, true);
    }
}
