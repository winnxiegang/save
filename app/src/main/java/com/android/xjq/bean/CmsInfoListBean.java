package com.android.xjq.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qiaomu on 2017/11/29.
 * <p>
 * 首页轮播图
 */

public class CmsInfoListBean {
    @SerializedName("infoClientSimpleList")
    public List<NewsInfoBean.NewsInfo> infoList;
}
