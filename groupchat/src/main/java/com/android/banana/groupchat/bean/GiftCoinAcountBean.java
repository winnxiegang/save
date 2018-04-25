package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by qiaomu on 2017/11/4.
 */

public class GiftCoinAcountBean {

    /**
     * receiveUserLogoUrl	String	接受人头像地址
     * receiveUserLoginName	String	接受人用户名
     * minRewardAmount	Money	最小打赏金额
     * success	boolean	是否成功
     * jumpLogin	boolean	是否跳到登陆页面
     * error	EnumBase	错误枚举
     * detailMessage	String	详细信息
     * nowDate	String	当前时间
     * goldCoinAmount	Money	金币余额
     * maxRewardAmount	Money	最大打赏金额
     */
    public String receiveUserLogoUrl;
    public String receiveUserLoginName;
    public double minRewardAmount;
    public boolean success;
    public NormalObject error;
    public String nowDate;
    public String goldCoinAmount;
    public double maxRewardAmount;

    public boolean accountPasswordSet;
}
