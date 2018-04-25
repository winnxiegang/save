/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.xjq.model.recharge;


import com.android.xjq.model.EnumBase;

/**
 * 资金通道提供者类型
 *
 * @author chenbug
 */
public enum FundChannelProviderTypeEnum implements EnumBase {

    ANTFINANCIAL("蚂蚁金服"),

    WEIXINPAY("微信支付"),

    KUAIQIAN("快钱"),

    UMPAY("U付"),

    TENPAY("财富通"),

    SHENGPAY("盛付通"),

    OAPPAY("寄居蟹"),

    CHANGPAY("畅捷支付");


    private String message;

    private FundChannelProviderTypeEnum(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public Number value() {
        return 0;
    }

}
