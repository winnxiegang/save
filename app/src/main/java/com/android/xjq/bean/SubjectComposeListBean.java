package com.android.xjq.bean;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/30.
 * <p>
 * 首页推荐 新鲜事对象,竞彩大学
 */

public class SubjectComposeListBean {
    @SerializedName("materialSimpleList")
    public List<SubjectsComposeBean> subjectList;   //首页新鲜事


    @SerializedName("subjects")
    public List<SubjectsComposeBean> articleList;                                 //竞彩大学数据列表
    public HashMap<String, JczqDataBean> basketballRaceDataMap;//数据列表中,插入的篮球赛事元素
    public HashMap<String, JczqDataBean> footballRaceDataMap;////数据列表中,插入的足球赛事元素

    public PaginatorBean paginator;
    public HashMap<String, List<SubjectTag>> subjectTagMap;//标签map

}
