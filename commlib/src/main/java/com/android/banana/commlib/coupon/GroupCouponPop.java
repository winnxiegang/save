package com.android.banana.commlib.coupon;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.GroupCouponEntity;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.couponenum.CouponUrlEnum;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.loadmore.LoadMoreListView;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingjiu on 2017/7/25.
 */

public class GroupCouponPop extends BottomAndSidePop implements OnHttpResponseListener {
    LoadMoreListView lv;
    private HttpRequestHelper httpRequestHelper;
    private int currentPage = 1;
    private int maxPages;
    private List<GroupCouponInfoBean> mList = new ArrayList<>();
    private GroupCouponAdapter mAdapter;
    //当前选择的红包
    private GroupCouponInfoBean couponInfoBean;
    //当前发红包所在的平台id以及type
    private String objectId;
    private String objectType;

    public void close() {
        dismiss();
    }

    //用于群聊
    public GroupCouponPop(Context context, String objectType, String objectId) {
        this(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, objectType, objectId);
    }

    //用于直播
    public GroupCouponPop(Context context, int orientation, String objectType, String objectId) {
        super(context, R.layout.layout_popupwindow_group_coupon, orientation);
        this.objectId = objectId;
        this.objectType = objectType;
    }

    @Override
    protected void onCreate() {
        lv = (LoadMoreListView) parentView.findViewById(R.id.lv);
        parentView.findViewById(R.id.closeIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        httpRequestHelper = new HttpRequestHelper(mContext, this);
    }

    @Override
    protected void onResume() {
        requestData();
        setListView();
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.GET_COUPON_FETCH_INFO, true);
        map.put("platformObjectId", objectId);
        map.put("platformObjectType", objectType);
        map.put("currentPage", currentPage + "");
        httpRequestHelper.startRequest(map, false);
    }

    //查询红包是否可抢
    private void queryAvailableCoupon(String couponId) {
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.QUERY_AVAILABLE_COUPON, true);
        map.put("groupCouponId", couponId);
        httpRequestHelper.startRequest(map, true);
    }

    private void setListView() {
        mAdapter = new GroupCouponAdapter(mContext, mList);
        lv.initBottomView(true);
        lv.setAdapter(mAdapter);
        lv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage == maxPages) {

                    lv.getFooterView().showEnd();

                    return;
                }
                currentPage++;

                requestData();
            }
        });

        mAdapter.setCouponItemClickListener(new GroupCouponAdapter.CouponItemClickListener() {
            @Override
            public void couponValidate(GroupCouponInfoBean groupCouponInfo, int position) {
                couponInfoBean = groupCouponInfo;
                queryAvailableCoupon(groupCouponInfo.getCouponNo());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {
        if (event instanceof GroupCouponInfoBean) {
            mAdapter.updateItemData((GroupCouponInfoBean) event);
        }
    }

    private void responseSuccessGroupCouponInfo(JSONObject jsonObject) {
        GroupCouponEntity groupCouponEntity = new Gson().fromJson(jsonObject.toString(), GroupCouponEntity.class);
        groupCouponEntity.operatorData();
        maxPages = groupCouponEntity.getPaginator().getPages();
        List<GroupCouponInfoBean> groupCouponList = groupCouponEntity.getGroupCouponList();
        mList.addAll(groupCouponList);
        mAdapter.notifyDataSetChanged();
    }

    private void responseSuccessCouponAvailable(JSONObject jsonObject) {
        new OpenCouponDialog(mContext, couponInfoBean).show();
    }

    @Override
    protected void onDestroy() {
        httpRequestHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
       // try {
            switch (((CouponUrlEnum) requestContainer.getRequestEnum())) {
                case GET_COUPON_FETCH_INFO:
                    responseSuccessGroupCouponInfo(jsonObject);
                    //((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
                    break;
                case QUERY_AVAILABLE_COUPON:
                    responseSuccessCouponAvailable(jsonObject);
                    break;
            }
       /* } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((CouponUrlEnum) requestContainer.getRequestEnum())) {
                case GET_COUPON_FETCH_INFO:
                    ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
                    break;
                case QUERY_AVAILABLE_COUPON:
                    FetCouponValidateUtils fetCouponValidateUtils = new FetCouponValidateUtils((BaseActivity) mContext, couponInfoBean);
                    fetCouponValidateUtils.handleFalse(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
