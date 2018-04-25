package com.android.xjq.bean;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.R;
import com.android.xjq.model.SubjectObjectSubType;
import com.android.xjq.model.SubjectObjectType;
import com.android.xjq.model.SubjectPropertiesType;
import com.android.xjq.utils.SubjectUtils;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/29.
 * <p>
 * 这是一个聚合bean对象，里面包含了新闻资讯，精彩大学，图片等多个bean对象，
 * <p>
 * 这里又将共有的字段抽取到了本类中。
 */

public class SubjectsComposeBean extends BaseSubjectBean {
    private static final String TAG = "SubjectsComposeBean";

    private static final String NORMAL = "NORMAL";
    private static final String SUBJECT = "SUBJECT";
    private static final String ARTICLE = "ARTICLE";

    public @interface TargetActivity {
        int NEWS = 0;

        int ARTICLE = 1;

        int PICTURE = 2;
    }

    //获取该条bean对象
    @Expose
    private int layoutRes = -1; //该条bean对象对应的布局资源id，该方法的判断只会走一次
    private int targetActivity;//该条目点击时跳转的目标Activity

    public int getTargetActivity() {
        return targetActivity;
    }

    public int getLayoutViewType(HashMap<String, JczqDataBean> raceMap) {

        if (objectType == null)
            return 0;

        if (layoutRes != -1)
            return layoutRes;

        if (TextUtils.equals(objectType.getName(), SubjectObjectType.CMS_NEWS)) {       //一级类型 新闻资讯
            targetActivity = TargetActivity.NEWS;
            return layoutRes = R.layout.layout_subject_item_news;
        }

        if (TextUtils.equals(objectType.getName(), SubjectObjectType.ARTICLE)) {        //一级类型 竞彩大学,此类型只会出现在竞彩大学列表页数据里面
            return layoutRes = tryGetArticleRaceElement(raceMap);
        }

        if (TextUtils.equals(objectType.getName(), SubjectObjectType.SUBJECT)) {        //话题，需要判断二级，三级类型，这个指挥出现在首页新鲜事
            return judgeThirdObjectSubType(raceMap);
        }

        return 0;
    }


    private int judgeThirdObjectSubType(HashMap<String, JczqDataBean> raceMap) {
        if (thirdObjectSubType == null) {                                                //话题下，没有插入任何元素,只有标题 摘要等，发布时间，点赞等基础信息。
            return layoutRes = R.layout.layout_subject_normal;
        }

        if (TextUtils.equals(thirdObjectSubType.getName(), NORMAL)) {                    //话题下，二级判断类型

            return judgeObjectSubType(raceMap);

        } else if (TextUtils.equals(thirdObjectSubType.getName(), ARTICLE)) {

            return judgeObjectSubType(raceMap);
        }
        return 0;
    }

    private int judgeObjectSubType(HashMap<String, JczqDataBean> raceMap) {
        if (objectSubType == null) {                                                      //话题下，没有插入任何元素,只有标题 摘要等，发布时间，点赞等基础信息。
            return layoutRes = R.layout.layout_subject_normal;
        }

        if (TextUtils.equals(objectSubType.getName(), SubjectObjectSubType.NORAML)) {
            targetActivity = TargetActivity.ARTICLE;
            return layoutRes = R.layout.layout_subject_normal;
        }

        if (TextUtils.equals(objectSubType.getName(), SubjectObjectSubType.INSERT_PICTURE)) {//话题，三级类型判断
            targetActivity = TargetActivity.PICTURE;
            return layoutRes = R.layout.layout_subject_item_photos;
        }

        if (TextUtils.equals(objectSubType.getName(), SubjectObjectSubType.INSERT_ARTICLE_JCZQ_RACE)
                || TextUtils.equals(objectSubType.getName(), SubjectObjectSubType.INSERT_JCZQ_RACE)) {
            isBtRace = false;
            targetActivity = TargetActivity.ARTICLE;
            return layoutRes = R.layout.layout_subject_item_artical;
        }

        if (TextUtils.equals(objectSubType.getName(), SubjectObjectSubType.INSERT_ARTICLE_JCLQ_RACE)
                || TextUtils.equals(objectSubType.getName(), SubjectObjectSubType.INSERT_JCLQ_RACE)) {
            isBtRace = true;
            targetActivity = TargetActivity.ARTICLE;
            return layoutRes = R.layout.layout_subject_item_artical;
        }

        return 0;
    }

    private int tryGetArticleRaceElement(HashMap<String, JczqDataBean> raceMap) {     //只针对竞彩大学的，从map集合中获取该bean对象对应的赛事 元素，
        targetActivity = TargetActivity.ARTICLE;
        if (raceMap != null && properties != null) {
            String firstCardType = properties.getSubjectFirstCardType();
            if (TextUtils.equals(firstCardType, SubjectPropertiesType.INSERT_ARTICLE_BT)) {
                basketballRace = raceMap.get(properties.getBaseketBallRaceId());
                targetActivity = TargetActivity.ARTICLE;
                isBtRace = true;
                return R.layout.layout_subject_item_artical;
            } else if (TextUtils.equals(firstCardType, SubjectPropertiesType.INSERT_ARTICLE_FT)) {
                footballRace = raceMap.get(properties.getFootballBallRaceId());
                targetActivity = TargetActivity.ARTICLE;
                isBtRace = false;
                return R.layout.layout_subject_item_artical;
            } else if (TextUtils.equals(firstCardType, SubjectPropertiesType.INSERT_PICTURE)) {
                targetActivity = TargetActivity.PICTURE;
                return R.layout.layout_subject_item_photos;
            }
        }
        return R.layout.layout_subject_normal;
    }

    @Expose
    private SpannableStringBuilder tagSpans;//该条bean对象所对应的标签集合，该方法的判断只会走一次

    public SpannableStringBuilder getSubjectTagSpan(Context context, HashMap<String, List<SubjectTag>> subjectTagList, String key) {
        if (tagSpans != null)
            return tagSpans;
        return tagSpans = SubjectUtils.getSubjectTagSpan(context, subjectTagList, key, setTop);
    }


    @Expose
    private String publishTime;//获取发布时间，该方法的判断只会走一次

    public CharSequence getPublishTime() {
        if (!TextUtils.isEmpty(publishTime))
            return publishTime;

        return publishTime = TimeUtils.formatTime(TimeUtils.getCurrentTime(), gmtCreate);
    }

    @Expose
    private String formatRaceTime;//获取赛事格式化时间字符串

    public CharSequence getFormatRaceTime() {
        if (!TextUtils.isEmpty(formatRaceTime))
            return formatRaceTime;

        if (isBtRace && basketballRace != null) {
            return formatRaceTime = TimeUtils.format(TimeUtils.LONG_DATEFORMAT, TimeUtils.MATCH_FORMAT, basketballRace.getGmtStart());
        }

        if (!isBtRace && footballRace != null) {
            return formatRaceTime = TimeUtils.format(TimeUtils.LONG_DATEFORMAT, TimeUtils.MATCH_FORMAT, footballRace.getGmtStart());
        }

        return "";
    }


    @Expose
    private SpannableStringBuilder summarySpan;//获取摘要格式化字符串

    public SpannableStringBuilder getSummarySpan(TextView textView, String summary) {
        if (summarySpan != null)
            return summarySpan;

        return summarySpan = SubjectUtils.getSubjectSummarySpan(textView, summary);
    }

    @Expose
    private SpannableStringBuilder titleSpan;//获取标题格式化字符串

    public SpannableStringBuilder getTitleSpan(CharSequence tagSpans) {
        if (titleSpan != null)
            return titleSpan;
        titleSpan = new SpannableStringBuilder();
        if (tagSpans != null) {
            titleSpan.append(tagSpans);
            titleSpan.append("");
        }
        titleSpan.append(title);

        return titleSpan;
    }
}
