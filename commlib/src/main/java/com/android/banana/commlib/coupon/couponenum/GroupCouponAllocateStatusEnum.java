package com.android.banana.commlib.coupon.couponenum;

/**
 * Created by Duzaoqiu on 2016/11/10 10:43.
 */
public enum GroupCouponAllocateStatusEnum {
    //初始化
    INIT,
    //等待拆分
    WAIT_SPLIT,
    //(未领完)
    WAIT_ALLOCATE,
    //(已领完)
    ALL_ALLOCATED,
    //(已过期)
    UNALLOCATABLE,

    DEFAULT, ALLOCATED;

    public static GroupCouponAllocateStatusEnum safeValueOf(String name) {

        try {
            return GroupCouponAllocateStatusEnum.valueOf(name);
        } catch (Exception e) {
        }

        return DEFAULT;

    }

}
