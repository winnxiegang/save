package com.android.xjq.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.view.CenterVerticalImageSpan;
import com.android.banana.commlib.view.HtmlEmojiTextView;
import com.android.banana.commlib.view.URLImageParser;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.residemenu.lt_lib.enumdata.FtRaceStatusEnum;
import com.android.xjq.R;
import com.android.xjq.bean.BaseSubjectBean;
import com.android.xjq.bean.SubjectProperties;
import com.android.xjq.bean.SubjectTag;
import com.android.xjq.bean.SubjectsComposeBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/30.
 * <p>
 * 话题相关的工具类
 */

public class SubjectUtils {
    /**
     * 获取该条bean对象所拥有的标签
     *
     * @param context        上下文对象
     * @param subjectTagList 标签集合
     * @param subjectId      该条bean对象中的subjectId
     * @return
     */
    public static SpannableStringBuilder getSubjectTagSpan(Context context, HashMap<String, List<SubjectTag>> subjectTagList, String subjectId, boolean setTop) {

        if (subjectTagList == null || subjectTagList.size() == 0)
            return null;

        SpannableStringBuilder tagSpans = new SpannableStringBuilder();
        List<SubjectTag> subjectTags = subjectTagList.get(subjectId);
        if (subjectTags == null || subjectTags.size() == 0)
            return tagSpans;
        int tagSize = subjectTags.size();
        for (int i = 0; i < tagSize; i++) {
            String code = subjectTags.get(i).getCode();
            switch (code) {
                case "ARTICLE_PERSONAL_EXP":
                    addIcon(context, tagSpans, R.drawable.icon_jcdx_experience);
                    break;
                case "ARTICLE_REPLAY":
                    addIcon(context, tagSpans, R.drawable.icon_jcdx_replay);
                    break;
                case "ARTICLE_ANALYSIS":
                    addIcon(context, tagSpans, R.drawable.icon_jcdx_analysy);
                    break;
                case "ARTICLE_PREDICT":
                    addIcon(context, tagSpans, R.drawable.icon_jcdx_forecast);
                    break;
                case "ELITE":
                    addIcon(context, tagSpans, R.drawable.icon_elite);
                    break;
                case "TOP":
                    addIcon(context, tagSpans, R.drawable.icon_ding);
                    break;
            }
        }

        if (setTop)
            addIcon(context, tagSpans, R.drawable.icon_ding);

        return tagSpans;
    }

    /**
     * 设置顶精热图标
     *
     * @param isElite
     * @param isTop
     * @return
     */
    public static SpannableString getDingEliteHotIcon(Context context, boolean isTop, boolean isElite) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (isTop) {
            addIcon(context, ssb, R.drawable.icon_ding);
        }
        if (isElite) {
            addIcon(context, ssb, R.drawable.icon_elite);
        }
        return new SpannableString(ssb);
    }


    public static void addIcon(Context context, SpannableStringBuilder ssb, int iconId) {
        SpannableString str1 = new SpannableString("精");
        Drawable ding = context.getResources().getDrawable(iconId);
        ding.setBounds(0, 0, ding.getIntrinsicWidth(), ding.getIntrinsicHeight() - 3);
        CenterVerticalImageSpan span = new CenterVerticalImageSpan(ding, ImageSpan.ALIGN_BOTTOM);
        str1.setSpan(span, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(str1);
        ssb.append(" ");
    }

    /**
     * 获取话题bean对象的摘要富媒体渲染对象
     *
     * @param textView 需要渲染的 TextView
     * @param summary  需要渲染的摘要文本
     * @return
     */
    public static SpannableStringBuilder getSubjectSummarySpan(TextView textView, String summary) {
        if (TextUtils.isEmpty(summary))
            return null;
        if (textView == null) return null;
        URLImageParser p = new URLImageParser(textView, textView.getContext());
        SpannableStringBuilder htmlSpan = (SpannableStringBuilder) Html.fromHtml(summary, p, null);
        /*new Html.TagHandler() {
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
//                int len = output.length();
//                ImageSpan[] images = output.getSpans(0, len, ImageSpan.class);
//                if (images == null || images.length == 0)
//                    return;
//                String imgURL = images[0].getSource();
//                output.setSpan(new CustomSubjectURLSpan(tag), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        });*/
        return htmlSpan;
    }


    private static class CustomSubjectURLSpan extends URLSpan {
        public CustomSubjectURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

    }

    /**
     * 绑定话题类别下，普通类型的item视图，即只有标题，摘要，发布时间，点赞，评论数
     *
     * @param mContext       上下文对象
     * @param holder         recyclerView中该条布局所持有的{@link ViewHolder}对象
     * @param composeBean    该条目的bean对象
     * @param mSubjectTagMap 标签集合【分析，复盘,顶,精】
     */
    public static void setNormalSubjectItemView(Context mContext, ViewHolder holder, SubjectsComposeBean composeBean, HashMap<String, List<SubjectTag>> mSubjectTagMap, String tagKey) {
        boolean isTitleEmpty = TextUtils.isEmpty(composeBean.title);
        SpannableStringBuilder tagSpans = composeBean.getSubjectTagSpan(mContext, mSubjectTagMap, tagKey);
        HtmlEmojiTextView titleTv = holder.getView(R.id.item_title);
        if (!isTitleEmpty && titleTv != null) {
            //titleTv.setSingleLine(TextUtils.isEmpty(composeBean.summary) ? false : true);
            titleTv.setMaxLines(TextUtils.isEmpty(composeBean.summary) ? 2 : 1);
            titleTv.setEllipsizeText(composeBean.getTitleSpan(tagSpans));
        }
        HtmlEmojiTextView summaryHtmlTv = holder.getView(R.id.item_summary);
        if (summaryHtmlTv != null) {
            summaryHtmlTv.setHtmlText(composeBean.getSummarySpan(summaryHtmlTv, composeBean.summary), isTitleEmpty ? tagSpans : null, false);//摘要
        }

        holder.setText(R.id.item_support_tv, composeBean.likeCount == 0 ? "支持" : String.valueOf(composeBean.likeCount))//点赞数
                .setTextColor(R.id.item_support_tv, composeBean.liked ? ContextCompat.getColor(mContext, R.color.main_red1) : ContextCompat.getColor(mContext, R.color.subject_footer_gray))
                //.setImageResource(R.id.item_support_iv, composeBean.liked ? R.drawable.icon_red_support : R.drawable.icon_gray_support)
                .setText(R.id.item_comment, composeBean.replyCount == 0 ? "回复" : String.valueOf(composeBean.replyCount));//评论数
        // .setText(R.id.item_date, composeBean.getPublishTime())//发布时间
        // .setText(R.id.item_author, composeBean.loginName + "    ");//作者名称
    }

    /**
     * 绑定话题类别下，图片类型的item视图
     *
     * @param mContext    上下文对象
     * @param holder      recyclerView中该条布局所持有的{@link ViewHolder}对象
     * @param composeBean 该条目的bean对象
     * @param position    该条目在Adapter中的位置
     */
    public static void setPhotoItemView(Context mContext, ViewHolder holder, SubjectsComposeBean composeBean, int position, boolean isJCDX) {

        List<String> midImageUrl = null;
        if (isJCDX) {
            SubjectProperties properties = composeBean.properties;
            if (properties != null) {
                midImageUrl = properties.midImageUrl;
            }
        } else {
            BaseSubjectBean.PictureUrls pictureUrls = composeBean.pictureMaterialClientSimple;
            if (pictureUrls != null) {
                midImageUrl = pictureUrls.midSubjectPictureUrls;
            }
        }
        if (midImageUrl == null) {
            holder.setViewVisibility(R.id.photo_layout, View.GONE);
            return;
        }

        holder.setViewVisibility(R.id.photo_layout, View.VISIBLE);

        holder.setImageDrawable(R.id.imageIv1, null);
        holder.setImageDrawable(R.id.imageIv2, null);
        holder.setImageDrawable(R.id.imageIv3, null);
        holder.setImageDrawable(R.id.imageIv4, null);
        holder.setViewVisibility(R.id.moreIvLayout, View.INVISIBLE);
        holder.setViewVisibility(R.id.imageCountTv, View.INVISIBLE);

        if (midImageUrl.size() > 0)
            holder.setImageByUrl(mContext, R.id.imageIv1, midImageUrl.get(0), R.drawable.image_empty).setViewVisibility(R.id.imageIv1, View.VISIBLE);
        if (midImageUrl.size() > 1)
            holder.setImageByUrl(mContext, R.id.imageIv2, midImageUrl.get(1), R.drawable.image_empty).setViewVisibility(R.id.imageIv2, View.VISIBLE);
        if (midImageUrl.size() > 2)
            holder.setImageByUrl(mContext, R.id.imageIv3, midImageUrl.get(2), R.drawable.image_empty).setViewVisibility(R.id.imageIv3, View.VISIBLE);
        if (midImageUrl.size() > 3) {
            holder.setViewVisibility(R.id.moreIvLayout, View.VISIBLE);
            holder.setImageByUrl(mContext, R.id.imageIv4, midImageUrl.get(3), R.drawable.image_empty).setViewVisibility(R.id.imageIv4, View.VISIBLE);
        }
        if (midImageUrl.size() > 4) {
            holder.setViewVisibility(R.id.imageCountTv, View.VISIBLE);
            holder.setText(R.id.imageCountTv, String.format(mContext.getString(R.string.article_photo_size), midImageUrl.size()));
        }

    }

    /**
     * 竞彩大学列表中文章Item视图 的数据绑定,包括篮球，看足球赛事
     *
     * @param mContext    上下文对象
     * @param holder      recyclerView中该条布局所持有的{@link ViewHolder}对象
     * @param composeBean 该条目的bean对象
     * @param position    该条目在Adapter中的位置
     */
    public static void setArticleItemView(Context mContext, ViewHolder holder, SubjectsComposeBean composeBean, int position) {
        boolean isBtRace = composeBean.isBtRace;
        JczqDataBean raceDataBean = isBtRace ? composeBean.basketballRace : composeBean.footballRace;
        if (raceDataBean == null) {
            return;
        }
        holder.setViewVisibility(R.id.JcdxCardLayout, View.VISIBLE);

        boolean isFinish = FtRaceStatusEnum.saveValueOf(raceDataBean.getRaceStatus().getName()) == FtRaceStatusEnum.FINISH;

        String homeTeamName = raceDataBean.getHomeTeamName();
        if (isBtRace && raceDataBean.getHomeTeamName() != null && raceDataBean.getHomeTeamName().length() >= 3) {
            homeTeamName = raceDataBean.getHomeTeamName().substring(0, 2);
        }
        String rightName = isBtRace ? homeTeamName + "[主]" : raceDataBean.getGuestTeamName();
        String leftName = isBtRace ? raceDataBean.getGuestTeamName() : raceDataBean.getHomeTeamName();
        String leftUrl = isBtRace ? raceDataBean.getBTGuestLogoUrl() : raceDataBean.getFTHomeLogoUrl();
        String rightUrl = isBtRace ? raceDataBean.getBTHomeLogoUrl() : raceDataBean.getFTGuestLogoUrl();

        String total = "总分:" + (raceDataBean.getFullHomeScore() + raceDataBean.getFullGuestScore());
        String diff = "分差:" + (raceDataBean.getFullHomeScore() - raceDataBean.getFullGuestScore());
        String fullScore = isBtRace ? (raceDataBean.getFullGuestScore() + ":" + raceDataBean.getFullHomeScore())
                : (raceDataBean.getFullHomeScore() + ":" + raceDataBean.getFullGuestScore());

        holder.setText(R.id.match_name, raceDataBean.getMatchName() + "\n" + composeBean.getFormatRaceTime())
                .setText(R.id.artical_tvname_left, leftName)
                .setText(R.id.artical_tvname_right, rightName)
                .setImageByUrl(mContext, R.id.artical_iv_left, leftUrl)
                .setImageByUrl(mContext, R.id.artical_iv_right, rightUrl);

        if (isBtRace) {
            holder.setText(R.id.artical_match_status, isFinish ? fullScore : "VS");
            holder.setText(R.id.artical_match_total, isFinish ? total : "");
            holder.setText(R.id.artical_match_diff, isFinish ? diff : "");
        } else {
            holder.setText(R.id.artical_match_status, isFinish ? fullScore : "VS");
            holder.setText(R.id.artical_match_total, "");
            holder.setText(R.id.artical_match_diff, "");
        }
    }
}
