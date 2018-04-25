package com.android.banana.commlib.coupon;

import android.app.Activity;
import android.view.ViewGroup;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.GroupCouponEntity;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.loadmore.LoadMoreListView;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/2/6.
 */

public class GroupCouponController extends BaseController4JCZJ<BaseActivity> {
    LoadMoreListView lv;
    private int currentPage = 1;
    private int maxPages;
    private List<GroupCouponInfoBean> mList = new ArrayList<>();
    private GroupCouponAdapter mAdapter;
    //当前选择的红包
    private GroupCouponInfoBean couponInfoBean;
    private LiveCouponCallback couponCallback;


    public GroupCouponController(Activity activity, LiveCouponCallback couponCallback) {
        super(activity);
        this.couponCallback = couponCallback;
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_group_coupon_view);
    }

    @Override
    public void onSetUpView() {
        lv = (LoadMoreListView) contentView.findViewById(R.id.lv);
        mAdapter = new GroupCouponAdapter(context, mList);
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
                couponCallback.queryGroupCouponList(currentPage);
            }
        });

        mAdapter.setCouponItemClickListener(new GroupCouponAdapter.CouponItemClickListener() {
            @Override
            public void couponValidate(GroupCouponInfoBean groupCouponInfo, int position) {
                couponInfoBean = groupCouponInfo;
                couponCallback.queryAvailableCoupon(groupCouponInfo);
            }
        });
    }

    public void responseSuccessGroupCouponInfo(JSONObject jsonObject) {
        GroupCouponEntity groupCouponEntity = new Gson().fromJson(jsonObject.toString(), GroupCouponEntity.class);
        groupCouponEntity.operatorData();
        maxPages = groupCouponEntity.getPaginator().getPages();
        List<GroupCouponInfoBean> groupCouponList = groupCouponEntity.getGroupCouponList();
        if (currentPage == 1) {
            mList.clear();
        }
        mList.addAll(groupCouponList);
        mAdapter.notifyDataSetChanged();
    }

    public void responseSuccessCouponAvailable(JSONObject jsonObject) {
        new OpenCouponDialog(context, couponInfoBean).show();
    }


    public void updateItemData(GroupCouponInfoBean groupCouponInfoBean) {
        mAdapter.updateItemData(groupCouponInfoBean);
    }
}
