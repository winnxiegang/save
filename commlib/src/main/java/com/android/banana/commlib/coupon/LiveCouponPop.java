package com.android.banana.commlib.coupon;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
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
import com.android.library.Utils.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.banana.commlib.coupon.couponenum.CouponUrlEnum.GET_COUPON_CONFIG;

/**
 * Created by lingjiu on 2018/2/6.
 */

public class LiveCouponPop extends BottomAndSidePop implements OnHttpResponseListener, View.OnClickListener, LiveCouponCallback {
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


    public void close() {
        dismiss();
    }

    public LiveCouponPop(Context context, int orientation, String objectType, String objectId) {
        super(context, R.layout.view_pop_live_coupon, orientation);
        this.objectType = objectType;
        this.objectId = objectId;
    }

    @Override
    protected void onCreate() {
        httpRequestHelper = new HttpRequestHelper(mContext, this);
        mTabLayout = ((MyTabLayout) parentView.findViewById(R.id.tabLayout));
        parentView.findViewById(R.id.closeIv).setOnClickListener(this);
        contentLayout = ((FrameLayout) parentView.findViewById(R.id.contentLayout));
        statusLayout = ((CommonStatusLayout) parentView.findViewById(R.id.statusLayout));
        //mJcdxController = new JcdxController();
        sendCouponController = new SendCouponController(((BaseActivity) mContext), this);
        groupCouponController = new GroupCouponController(((BaseActivity) mContext), this);
    }


    private void setUpTabLayout() {
        mTabLayout
                .setTabMargin(40)
                .setBottomLineColor(Color.TRANSPARENT)
                .addTabs(mContext.getString(R.string.pos_group_coupon), mContext.getString(R.string.pos_send_coupon))
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        if (reSelected)
                            return;
                        switch (tab.getPosition()) {
                            case POS_GROUP_COUPON:
                                sendCouponController.setContentView(contentLayout);
                                break;
                            case POS_SEND_COUPON:
                                groupCouponController.setContentView(contentLayout);
                                break;
                        }
                    }
                }).setSelectTab(0);
    }

    @Override
    protected void onResume() {
        LogUtils.e("SendCouponPop", "onResume");
        httpRequestHelper.startRequest(new XjqRequestContainer(GET_COUPON_CONFIG, true), false);
        if (CouponPlatformType.GROUP_CHAT.name().equals(objectType))
            parentView.findViewById(R.id.sendTimeLayout).setVisibility(View.INVISIBLE);
        setUpTabLayout();
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
    protected void onDestroy() {
        httpRequestHelper.onDestroy();
        super.onDestroy();
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
                    FetCouponValidateUtils fetCouponValidateUtils = new FetCouponValidateUtils((BaseActivity) mContext, couponInfoBean);
                    fetCouponValidateUtils.handleFalse(jsonObject);
                    return;
                case COUPON_CREATE:
                    ErrorBean errorBean = new ErrorBean(jsonObject);
                    String name = errorBean.getError().getName();
                    if (sendCouponController != null)
                        sendCouponController.setCreateCouponResult(false);
                    if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(name)) {
                        ToastUtil.showLong(mContext.getApplicationContext(), "您的金币不够");
                        return;
                    }

                    break;
            }
            ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
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
    public void onClick(View v) {

    }

    @Override
    public void createCoupon(String psd, String amountAllocateType, String couponType, String couponPublishType,
                             String gmtPublish, String title, String totalAmount, String totalCount) {
        this.title = title;
        this.couponType = couponType;
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.COUPON_CREATE, true);
        map.put("accountPassword", psd);
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
