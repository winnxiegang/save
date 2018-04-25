package com.android.banana.commlib.coupon;

import com.android.banana.commlib.bean.GroupCouponInfoBean;

/**
 * Created by lingjiu on 2018/2/6.
 */

public interface LiveCouponCallback {

    void createCoupon(String psd, String amountAllocateType, String couponType, String couponPublishType,
                      String gmtPublish, String title, String totalAmount, String totalCount);

    void queryGroupCouponList(int currentPage);

    void queryAvailableCoupon(GroupCouponInfoBean groupCouponInfo);
}
