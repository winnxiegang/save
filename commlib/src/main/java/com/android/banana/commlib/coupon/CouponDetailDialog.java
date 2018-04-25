package com.android.banana.commlib.coupon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.CouponDetailInfoBean;
import com.android.banana.commlib.bean.CouponDetailItemBean;
import com.android.banana.commlib.coupon.couponenum.CouponUrlEnum;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.coupon.couponenum.CouponEnum;
import com.android.banana.commlib.loadmore.LoadMoreListView;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2017/7/26.
 */

public class CouponDetailDialog extends CouponDialogBase implements OnHttpResponseListener {

    CircleImageView portraitIv;
    TextView userNameTv;
    TextView couponTitleTv;
    LinearLayout sendCouponInfoLayout;
    TextView couponMoneyTv;
    LinearLayout allocatedDetailLayout;
    TextView couponInfoTv;
    LoadMoreListView listView;
    ImageView dialogCloseIv;

    private Context mContext;
    private List<CouponDetailItemBean> mList = new ArrayList<>();
    private CouponDetailAdapter mAdapter;
    private int currentPage = 1;
    private int maxPages;
    private HttpRequestHelper httpRequestHelper;
    private String groupCouponId;


    public CouponDetailDialog(Context context, String groupCouponId) {
        this(context, groupCouponId, 0);
    }

    public CouponDetailDialog(Context context, String groupCouponId, int amount) {
        super(context, R.layout.dialog_coupon_detail);
        this.mContext = context;
        this.groupCouponId = groupCouponId;
        adjustDialogSize(context);
        initView(amount);
        requestData();
    }

    private void initView(int amount) {
        httpRequestHelper = new HttpRequestHelper(mContext, this);
        listView = (LoadMoreListView) findViewById(R.id.listView);
        couponInfoTv = (TextView) findViewById(R.id.couponInfoTv);
        couponMoneyTv = (TextView) findViewById(R.id.couponMoneyTv);
        userNameTv = (TextView) findViewById(R.id.userNameTv);
        couponTitleTv = (TextView) findViewById(R.id.couponTitleTv);
        portraitIv = (CircleImageView) findViewById(R.id.portraitIv);
        allocatedDetailLayout  = (LinearLayout) findViewById(R.id.allocatedDetailLayout);
        sendCouponInfoLayout  = (LinearLayout) findViewById(R.id.sendCouponInfoLayout);
        dialogCloseIv = (ImageView) findViewById(R.id.dialogCloseIv);
        dialogCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (amount != 0) {
            allocatedDetailLayout.setVisibility(View.VISIBLE);
            couponMoneyTv.setText("" + amount);
        } else {
            sendCouponInfoLayout.setVisibility(View.VISIBLE);
        }
        setListView();
    }

    private void setListView() {
        mAdapter = new CouponDetailAdapter(mContext, mList);
        listView.initBottomView(true);
        listView.setAdapter(mAdapter);
        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage == maxPages) {

                    listView.getFooterView().showEnd();

                    return;
                }
                currentPage++;

                requestData();
            }
        });
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.QUERY_ALLOCATED_COUPON_ITEMS, true);
        map.put("groupCouponId", groupCouponId);
        map.put("currentPage", String.valueOf(currentPage));
        httpRequestHelper.startRequest(map, false);
    }

    public void adjustDialogSize(Context context) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        int height;
        int screenHeight = LibAppUtil.getScreenHeight(context);
        int screenWidth = LibAppUtil.getScreenWidth(context);
        if (screenHeight > screenWidth) {
            height = (int) (screenHeight * 3 / 4);
        } else {
            height = screenHeight;
        }
        lp.height = height;
        lp.width = (screenHeight > screenWidth ? screenWidth : screenHeight) * 3 / 4;//宽高可设置具体大小
        getWindow().setAttributes(lp);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        CouponDetailInfoBean detailInfoBean = new Gson().fromJson(jsonObject.toString(), CouponDetailInfoBean.class);
        detailInfoBean.operatorData();
        CouponDetailInfoBean.GroupCouponRemainInfoBean groupCouponRemainInfo = detailInfoBean.getGroupCouponRemainInfo();
        if (groupCouponRemainInfo != null) {
            int totalCount = groupCouponRemainInfo.getTotalCount();
            int hasAllocatedCount = totalCount - groupCouponRemainInfo.getRemainNum();
            int totalAmount = (int) groupCouponRemainInfo.getTotalAmount();
            int receivedAmount = (int) groupCouponRemainInfo.getReceivedAmount();
            couponInfoTv.setText("已领取" + hasAllocatedCount + "/" + totalCount + "个,共" + receivedAmount + "/" + totalAmount + "礼金");
        }
        setView(detailInfoBean);
        maxPages = detailInfoBean.getPaginator().getPages();
        List<CouponDetailItemBean> groupCouponItemList = detailInfoBean.getGroupCouponItemList();
        if (groupCouponItemList != null && groupCouponItemList.size() > 0) {
            mList.addAll(groupCouponItemList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setView(CouponDetailInfoBean detailInfoBean) {
        if (detailInfoBean.getAmount() != 0) {
            sendCouponInfoLayout.setVisibility(View.GONE);
            allocatedDetailLayout.setVisibility(View.VISIBLE);
            couponMoneyTv.setText("" + (long) detailInfoBean.getAmount());
        } else {
            allocatedDetailLayout.setVisibility(View.GONE);
            sendCouponInfoLayout.setVisibility(View.VISIBLE);
            setImageShow(portraitIv, detailInfoBean.getLogoUrl());
            userNameTv.setText(detailInfoBean.getCreateUserLoginName());
            couponTitleTv.setText(detailInfoBean.getTitle());
            if (CouponEnum.NORMAL_GROUP_COUPON.name().equals(detailInfoBean.getCouponType())) {
                Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.icon_normal_coupon);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 2 / 3, drawable.getIntrinsicHeight() * 2 / 3);
                userNameTv.setCompoundDrawables(null, null, drawable, null);
            } else {
                Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.icon_lucky_coupon);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 2 / 3, drawable.getIntrinsicHeight() * 2 / 3);
                userNameTv.setCompoundDrawables(null, null, drawable, null);
            }
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
