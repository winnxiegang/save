package com.android.xjq.bean;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/11/30.
 */

public class BaseSubjectBean {
    /*-------------------通用的类型字段--------新闻资讯 图片，文章类型话题会用到下面的----------------*/
    public String title;//标题
    public String summary;//摘要

    public NormalObject thirdObjectType;    //name : SUBJECT/方案/资讯           * message : 话题对象
    public NormalObject thirdObjectSubType; //name : NORMAL/ARTICAL            * message : 普通对象
    public NormalObject objectType;         //name : SUBJECT/方案/资讯          * message : 话题对象
    public NormalObject objectSubType;      //name : INSERT_PICTURE           * message : 图片

    public int id;
    public int thirdObjectId;

    public boolean liked;//是否已点赞
    public int likeCount;//点赞数
    public int replyCount;//回复数

    public String gmtModified;  //一些时间
    public String gmtExpired;
    public String gmtCreate;

    public String userId;    //发布者的id
    public String loginName;  //发布者的名字
    public String userLogoUrl;//发布者的头像

    public boolean enabled;
    public int floor;
    public int orderNo;

    /*-------------------------------------图片类型话题独有---------------------------------*/
    public PictureUrls pictureMaterialClientSimple;   //cmsMaterialSClientSimple
    public String propertyValue;

    public static class PictureUrls {
        public ArrayList<String> subjectPictureUrls;
        public ArrayList<String> midSubjectPictureUrls;
        public ArrayList<String> smallSujectPictureUrls;
    }

    /*--------------------------------------资讯活动类型话题专属----------------------------*/
    @SerializedName("cmsMaterialSClientSimple")  //cmsMaterialSClientSimple
    public CmsSubject cmsSubject;

    public static class CmsSubject {
        public String url;//url : http://mapi1.xjq.net/imageUrl.htm?ownerId=2061083
    }


    /*----------------------------------------文章类型话题专属-------------------*/
    public boolean commentOff;
    public String subjectId;
    public String content;
    public boolean delete;
    public SubjectProperties properties;
    public int collectId;
    public int profitCopyCount;
    public boolean setTop;
    public String objectId;
    public JczqDataBean footballRace;
    public JczqDataBean basketballRace;
    @Expose
    public boolean isBtRace;//标志改对象是不是篮球赛事类型，只针对篮球，足球赛事
}
