package com.android.xjq.bean;

import android.support.annotation.DrawableRes;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.banana.commlib.view.URLImageParser;
import com.android.xjq.R;
import com.android.xjq.model.SubjectObjectSubType2;
import com.android.xjq.model.SubjectPropertiesType;
import com.google.gson.annotations.Expose;

/**
 * Created by qiaomu on 2018/2/7.
 */

public class SubjectsComposeBean2 extends BaseSubjectBean2 {
    private static final String TAG = "SubjectsComposeBean2";

    @Expose
    private String sourceSummary;
    private String articleUrl;//转发的时候 要传递的标题 摘要
    private String sourceTitle;//转发时 需要填写的标题
    private String sourceDescription;//转发到群 群空间时 需要填入的描述
    private int defaultIconResource;//6种动态的默认图标


    public String videoUrl, firstFrameUrl;//如果是视频item  那么它的第一针url 和视频地址
    public String rewardLogo;//打赏动态类型的url头像

    public boolean isSubjectDeleted, isSourceDeleted;//话题 ，话题源是否被删除了

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public String getSourceSummary() {
        return sourceSummary;
    }

    public String getArticleFirstUrl() {
        return articleUrl;
    }

    @DrawableRes
    public int getDefaultIconResource() {
        return defaultIconResource;
    }


    public @interface TargetActivity {
        int NEWS = 0;

        int ARTICLE = 1;

        int PICTURE = 2;
    }

    //获取该条bean对象
    @Expose
    private int layoutRes = -1; //该条bean对象对应的布局资源id，该方法的判断只会走一次
    private static final int LAYOUT_RESOURCE_NONE = 0;//该条资源解析不出来

    private int targetActivity;//该条目点击时跳转的目标Activity

    public int getTargetActivity() {
        return targetActivity;
    }

    public int getLayoutViewType() {
        if (layoutRes != -1) {
            return layoutRes;
        }
        //这里是 【我的关注，个人主页动态】带subject对象，【推荐，群空间，个人主页事件】不带subject对象的判断
        SubjectObjectSubType2 subType2 = SubjectObjectSubType2.safeEnumOf(subject != null ? objectSubType.getName() : objectType.getName());

        //这里是获取 转发 分享到群，群空间时要携带的描述文字
        String description = subject != null ? subject.summary : summary;
        String loginNamePrefix = subject != null ? "@" + subject.loginName : "@" + loginName;
        sourceDescription = TextUtils.isEmpty(description) ? loginNamePrefix : loginNamePrefix + "  " + description;

        //这里是得到 话题最初源的标题 摘要
        sourceTitle = subject != null ?
                (subject.sourceSubjectSimple != null ? subject.sourceSubjectSimple.title : subject.title)
                : (sourceSubjectSimple != null ? sourceSubjectSimple.title : title);

        sourceSummary = subject != null ? (subject.sourceSubjectSimple != null ? subject.sourceSubjectSimple.memo : subject.memo) :
                (sourceSubjectSimple != null ? sourceSubjectSimple.memo : memo);

        //字段分不清取舍 为空再取一次
        if (TextUtils.isEmpty(sourceSummary)) {
            sourceSummary = subject != null ?
                    (subject.sourceSubjectSimple != null ? subject.sourceSubjectSimple.summary : subject.summary) :
                    (sourceSubjectSimple != null ? sourceSubjectSimple.summary : summary);

        }

        //这里是判断【话题 】和 【最初的源】是否被删除了
        if (subject != null) {
            isSubjectDeleted = subject.deleted;
            if (subject.sourceSubjectSimple != null)
                isSourceDeleted = subject.sourceSubjectSimple.deleted;
        } else {
            if (sourceSubjectSimple != null)
                isSourceDeleted = sourceSubjectSimple.deleted;
            isSubjectDeleted = deleted;
        }


        switch (subType2) {
            case NORMAL:
                tryGetInsertPictureCardType();
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_speak;//普通
                return tryGetSubjectProperties(false);
            case ARTICLE:
            case PERSONAL_ARTICLE:  //文章，个人文章
                sourceDescription = subject != null ? "@" + subject.loginName + "  " + subject.title : "@" + loginName + "  " + title;
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_article;
                return tryGetSubjectProperties(false);

            case TRANSMIT_SUBJECT: //转发
                SubjectObjectSubType2 subTypeLevel2 = SubjectObjectSubType2.safeEnumOf(subject != null ? subject.sourceSubjectSimple.objectType.getName() : sourceSubjectSimple.objectType.getName());

                return getSecondLevelLayoutViewType(subTypeLevel2, true);
        }

        return getSecondLevelLayoutViewType(subType2, false);
    }


    //第一级判断 和 第二集判断都有可能用到这些类型
    private int getSecondLevelLayoutViewType(SubjectObjectSubType2 subType2, boolean isTransform) {
        switch (subType2) {
            case DEFAULT:
            case NORMAL:
                tryGetInsertPictureCardType();
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_speak;//普通
                return isTransform ? R.layout.layout_subject_transform_article2 : tryGetSubjectProperties(isTransform);
            case ARTICLE://文章，
            case PERSONAL_ARTICLE:  //个人文章
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_article;
                tryGetInsertPictureCardType();
                return isTransform ? R.layout.layout_subject_transform_article2 : tryGetSubjectProperties(isTransform);

            case XJQ_VIDEO: //香蕉球视频
                defaultIconResource = com.android.banana.R.drawable.ic_trans_dynmic_video;
                return isTransform ? R.layout.layout_subject_transform_video2 : R.layout.layout_subject_video2;

            case MARK_ATTITUDE_PERSONAL_ARTICLE://个人文章被标记态度                //---
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_article;
                tryGetInsertPictureCardType();
                return layoutRes = R.layout.layout_subject_transform_article2;    //----

            case MARK_ATTITUDE_COMMENT://评论被标记态度
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_article;
                tryGetInsertPictureCardType();
                return layoutRes = R.layout.layout_subject_system_dynmic2;    //----

            case MARK_ATTITUDE_MSG_SCREEN://弹幕被标记上态度
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_comment;
                return isTransform ? layoutRes = R.layout.layout_subject_system_dynmic2 : R.layout.layout_subject_normal2;

            case MARK_ATTITUDE_ROCKETS: //火箭
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_rocket;
                return layoutRes = R.layout.layout_subject_system_dynmic2;
            case EVENT_WIN_PK://PK
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_pk;
                return layoutRes = R.layout.layout_subject_system_dynmic2;
            case EVENT_WIN_CHEER: //助威
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_cheer;
                return layoutRes = R.layout.layout_subject_system_dynmic2;
            case EVENT_SEND_COUPON://发红包
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_coupon;
                return layoutRes = R.layout.layout_subject_system_dynmic2;
            case EVENT_SPONSOR_DRAW://赞助抽奖
                defaultIconResource = com.android.banana.R.drawable.ic_dynmic_lucky_draw;
                return layoutRes = R.layout.layout_subject_system_dynmic2;
            case EVENT_REWARD_ANCHOR: //打赏主播
                defaultIconResource = com.android.banana.R.drawable.ic_dynamic_gift_default;
                //todo 2018/3/15 15：34添加try catch 不想判断太多层级了  by-qiaomu
                try {
                    if (isTransform) {
                        rewardLogo = subject != null ? subject.sourceSubjectSimple.properties.giftUrl : sourceSubjectSimple.properties.giftUrl;
                    } else {
                        rewardLogo = subject != null ? subject.properties.giftUrl : properties.giftUrl;
                    }

                } catch (Exception e) {
                }
                return layoutRes = R.layout.layout_subject_system_dynmic2;
        }
        return isTransform ? R.layout.layout_subject_normal2 : LAYOUT_RESOURCE_NONE;
    }

    private int tryGetSubjectProperties(boolean isTransform) {
        SubjectProperties subjectProperties = getProperties();

        if (subjectProperties == null) {
            return isTransform ? layoutRes = R.layout.layout_subject_transform_article2 : R.layout.layout_subject_normal2;
        }

        String firstCardType = subjectProperties.subjectFirstCardType;

        if (TextUtils.equals(SubjectPropertiesType.INSERT_PICTURE, firstCardType)) {
            //普通话题插入图片
            articleUrl = subjectProperties.midImageUrl == null ? "" : subjectProperties.midImageUrl.get(0);
            return layoutRes = R.layout.layout_subject_photos2;
        }
        if (TextUtils.equals(SubjectPropertiesType.INSERT_ARTICLE_BT, firstCardType)) {
            //普通话题篮球名片
            return layoutRes = R.layout.layout_subject_item_race_card2;
        }
        if (TextUtils.equals(SubjectPropertiesType.INSERT_ARTICLE_FT, firstCardType)) {
            //普通话题足球名片
            return layoutRes = R.layout.layout_subject_item_race_card2;
        }
        return layoutRes = R.layout.layout_subject_normal2;
    }

    private void tryGetInsertPictureCardType() {
        SubjectProperties properties = getProperties();
        if (properties == null)
            return;
        String firstCardType = properties.subjectFirstCardType;

        if (TextUtils.equals(SubjectPropertiesType.INSERT_PICTURE, firstCardType)) {
            //普通话题插入图片
            articleUrl = properties.midImageUrl == null ? "" : properties.midImageUrl.get(0);
        }
    }


    private SubjectProperties getProperties() {
        SubjectProperties subjectProperties;
        if (subject != null) {
            if (subject.sourceSubjectSimple != null) {
                subjectProperties = subject.sourceSubjectSimple.properties;
            } else {
                subjectProperties = subject.properties;
            }
        } else {
            if (sourceSubjectSimple != null) {
                subjectProperties = sourceSubjectSimple.properties;
            } else {
                subjectProperties = properties;
            }
        }
        return subjectProperties;
    }

    public void makeVideoProperties() {

        if (subject != null) {
            if (subject.sourceSubjectSimple != null) {
                firstFrameUrl = subject.sourceSubjectSimple.properties.videoFirstFrameImageUrl;
                videoUrl = subject.sourceSubjectSimple.properties.videoUrl;
            } else {
                firstFrameUrl = subject.properties.videoFirstFrameImageUrl;
                videoUrl = subject.properties.videoUrl;
            }
        } else {
            if (sourceSubjectSimple != null) {
                firstFrameUrl = sourceSubjectSimple.properties.videoFirstFrameImageUrl;
                videoUrl = sourceSubjectSimple.properties.videoUrl;
            } else {
                firstFrameUrl = properties.videoFirstFrameImageUrl;
                videoUrl = properties.videoUrl;
            }
        }
    }


    @Expose
    private SpannableStringBuilder summarySpan;//获取摘要格式化字符串

    public SpannableStringBuilder getSummarySpan(TextView textView, String summary) {
        if (summarySpan != null)
            return summarySpan;

        if (TextUtils.isEmpty(summary) || null == textView)
            return null;

        URLImageParser p = new URLImageParser(textView, textView.getContext());
        return summarySpan = (SpannableStringBuilder) Html.fromHtml(summary, p, null);
    }
}
