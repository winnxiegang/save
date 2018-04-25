package com.android.xjq.bean;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/2/5.
 * <p>
 * 粉丝，关注列表 返回的数据结构
 */

public class UserFollowList {
    public PaginatorBean paginator;
    public ArrayList<UserFollow> userFollows;

    public static class UserFollow {
        /**
         * userId : 8201712258726342
         * userName : 0208
         * attentionNum : 1
         * fansNum : 0
         * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712258726342&mt=1514168867000
         * mySelf : false
         * focus : false
         * eachFocus : false
         */

        public String userId;
        public String userName;
        public int attentionNum;
        public int fansNum;
        public String userLogoUrl;
        public boolean mySelf;
        public boolean focus;
        public boolean eachFocus;
    }

}
