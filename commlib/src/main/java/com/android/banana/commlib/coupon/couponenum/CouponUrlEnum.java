package com.android.banana.commlib.coupon.couponenum;

import com.android.banana.commlib.http.AppParam;
import com.android.httprequestlib.BaseRequestHttpName;

/**
 * Created by qiaomu on 2017/10/30.
 */

public enum CouponUrlEnum implements BaseRequestHttpName {
    USER_FETCH_COUPON("USER_FETCH_COUPON", AppParam.S_URL),

    QUERY_ALLOCATED_COUPON_ITEMS("QUERY_ALLOCATED_COUPON_ITEMS", AppParam.S_URL),

    QUERY_AVAILABLE_COUPON("QUERY_AVAILABLE_COUPON", AppParam.S_URL),

    GET_COUPON_FETCH_INFO("GET_COUPON_FETCH_INFO", AppParam.S_URL),

    GET_COUPON_CONFIG("GET_COUPON_CONFIG", AppParam.S_URL),


    COUPON_CREATE("COUPON_CREATE", AppParam.S_URL),

    GET_ALLOW_FETCH_COUNT("GET_ALLOW_FETCH_COUNT", AppParam.S_URL);

    private String name;

    private String url;

    CouponUrlEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
