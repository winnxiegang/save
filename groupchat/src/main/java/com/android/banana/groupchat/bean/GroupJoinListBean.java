package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/2/24.
 */

public class GroupJoinListBean {
    public ArrayList<GroupJoinBean> groupChatList;
    public PaginatorBean paginator;

    public static class GroupJoinBean {
        /**
         * id : 2984656687404610980057391015
         * name : 战袍测试1群
         * userCount : 19
         * joined : false
         * applied : false
         * groupLogoUrl :
         */

        public String id;
        public String name;
        public int userCount;
        public boolean joined;
        public boolean applied;
        public String groupLogoUrl;
        public String memo;
    }
}
