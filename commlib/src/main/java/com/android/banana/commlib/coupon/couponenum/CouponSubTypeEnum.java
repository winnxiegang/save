package com.android.banana.commlib.coupon.couponenum;

/**
 * Created by Duzaoqiu on 2016/11/8 14:41.
 */
public enum CouponSubTypeEnum {

    DIRECTED_COUPON("定向红包",null),

    LUCKY_GROUP_COUPON("拼手气红包",null),

    NORMAL_GROUP_COUPON("普通红包",null),

    GROUP_COUPON("群红包",new GroupCouponSubTypeEnum[]{
           GroupCouponSubTypeEnum.LUCKY_GROUP_COUPON,
            GroupCouponSubTypeEnum.NORMAL_GROUP_COUPON}),

    DEFAULT("",null);

    String message;
    GroupCouponSubTypeEnum[] child;

    CouponSubTypeEnum(String message, GroupCouponSubTypeEnum[] child) {
        this.message = message;
        this.child = child;
    }

    public static enum  GroupCouponSubTypeEnum{

        LUCKY_GROUP_COUPON("拼手气红包"),

        NORMAL_GROUP_COUPON("普通红包");

        String message;

        GroupCouponSubTypeEnum(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


    public static CouponSubTypeEnum safeValueOf(String name){

        try{
            return CouponSubTypeEnum.valueOf(name);

        }catch (Exception e){}

        return DEFAULT;
    }
}
