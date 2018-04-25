package com.android.xjq.bean;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/29.
 * <p>
 * 首页新闻资讯栏目列表item
 */

public class NewsInfoBean {
    public List<NewsInfo> infoList;
    public PaginatorBean paginator;


    public static class NewsInfo {
        /**
         * commentOff : false
         * id : 2061067
         * channelTitle : 资讯活动
         * gmtPublish : 2017-11-28 17:20:42
         * title : 测试活动2
         * channelCode : xjq_activity
         * replyCount : 0
         * likeCount : 0
         * imageUrl : http://mapi1.xjq.net/imageUrl.htm?ownerId=2061067
         * liked : false
         * collectId : 0
         * delete : false
         * published : true
         * summary:***********************
         */

        public boolean commentOff;
        public int id;
        public String channelTitle;
        public String gmtPublish;
        public String title;
        public String channelCode;
        public int replyCount;
        public int likeCount;
        public String imageUrl;
        public boolean liked;
        public int collectId;
        public boolean delete;
        public boolean published;
        public String summary;
        public HashMap<String, String> propMap;
    }
}
