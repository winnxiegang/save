package com.android.xjq.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.dialog.ShareDialogFragment;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.HtmlEmojiTextView;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.library.Utils.LogUtils;
import com.android.residemenu.lt_lib.enumdata.FtRaceStatusEnum;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.SubjectDetailActivity;
import com.android.xjq.activity.TransformTransitionActivity;
import com.android.xjq.activity.dynamic.TransmitDetailsActivity;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.wall.LiveWallDetailActivity;
import com.android.xjq.bean.SubjectProperties;
import com.android.xjq.bean.SubjectsComposeBean;
import com.android.xjq.bean.SubjectsComposeBean2;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.dynamic.ImpressionDataBean;
import com.android.xjq.dialog.ShareGroupListDialogFragment;
import com.android.xjq.model.SubjectObjectSubType2;
import com.android.xjq.utils.singleVideo.SinglePlayCallback;
import com.android.xjq.utils.singleVideo.VideoViewHolder;
import com.android.xjq.view.ImpressionLayout;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2018/2/7.
 */

public class SubjectUtils2 {
    /**
     * 绑定 话题列表 对应的视图
     *
     * @param activity
     * @param holder      {@link com.android.banana.pullrecycler.multisupport.ViewHolder}
     * @param composeBean 话题聚合类对象
     * @param position
     */
    public static void bindViewHolder(final BaseActivity activity, ViewHolder holder, SubjectsComposeBean2 composeBean, int position, SinglePlayCallback playCallback) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case R.layout.layout_subject_transform_article2:
                //文章
                bindArticleTransformItemView(activity, holder, composeBean);
                break;
            case R.layout.layout_subject_system_dynmic2:
                //动态
                bindDynamicItemView(activity, holder, composeBean);
                break;
            case R.layout.layout_subject_photos2:
                //图片
                bindPhotoItemView(activity, holder, composeBean, position);
                break;
            case R.layout.layout_subject_video2:
                //视频类型
                bindVideoItemView(activity, holder, composeBean, playCallback);
                break;
            case R.layout.layout_subject_transform_video2:
                bindTransformVideoItemView(activity, holder, composeBean, position);
                break;
            case R.layout.layout_subject_normal2:
                //普通 标题摘要类型
                bindNormalItemView(activity, holder, composeBean);
        }

    }


    public static void onItemClick(Activity context, SubjectsComposeBean2 composeBean) {
        SubjectObjectSubType2 enumOf = SubjectObjectSubType2.safeEnumOf(composeBean.subject != null ? composeBean.objectSubType.getName() : composeBean.objectType.getName());

        if (SubjectObjectSubType2.DEFAULT == enumOf) {
            return;
        }
        switch (enumOf) {
            //    文章详情
            case NORMAL:
            case ARTICLE:
            case PERSONAL_ARTICLE:
                SubjectDetailActivity.startSubjectDetailActivity(context, composeBean.subject != null ? composeBean.subject.subjectId : composeBean.subjectId);//话题id
                break;
            case XJQ_VIDEO:
                LiveWallDetailActivity.startLiveWallDetailActivity(context,
                        composeBean.subject != null ? composeBean.subject.subjectId : composeBean.subjectId);
                break;
            //跳转到转发详情
            case TRANSMIT_SUBJECT:         //转发文章                                    //--
            case MARK_ATTITUDE_PERSONAL_ARTICLE:  //个人文章被标记态度                                //---
            case MARK_ATTITUDE_COMMENT://评论被标记态度
                //直播里面发起的---先跳转到详情页-----》再链接到直播间（视频有视频详情）
            case MARK_ATTITUDE_MSG_SCREEN: //弹幕被标记上态度
            case MARK_ATTITUDE_ROCKETS://火箭
            case EVENT_WIN_PK://PK
            case EVENT_WIN_CHEER://助威
            case EVENT_SEND_COUPON://发红包
            case EVENT_SPONSOR_DRAW: //赞助抽奖
            case EVENT_REWARD_ANCHOR://打赏主播
            case DEFAULT:
                TransmitDetailsActivity.startTransmitDetailsActivity(context, composeBean.subject != null ? composeBean.subject.subjectId : composeBean.subjectId);//转发id

                break;
        }

        return;

    }

    //转发的视频样式 只有用户信息 摘要 标题 转发点赞等信息
    private static void bindTransformVideoItemView(BaseActivity context, ViewHolder holder, SubjectsComposeBean2 composeBean, int position) {
        bindNormalItemView(context, holder, composeBean);

        setDynamicItemInfo(context, holder, composeBean);
    }

    //视频样式 只有用户信息 摘要 标题 转发点赞等信息
    private static void bindVideoItemView(final BaseActivity mContext, ViewHolder holder, final SubjectsComposeBean2 composeBean, SinglePlayCallback playCallback) {

        bindNormalItemView(mContext, holder, composeBean);

        LogUtils.e("kk","bindVideoItemView");
        VideoViewHolder videoViewHolder = new VideoViewHolder(mContext, holder, playCallback);

        videoViewHolder.bindVideoItemView(composeBean);

    }

    //普通样式 只有用户信息 摘要 标题 转发点赞等信息
    private static void bindNormalItemView(BaseActivity mContext, ViewHolder holder, SubjectsComposeBean2 composeBean) {

        setUserItemInfo(mContext, holder, composeBean);

        setTitleSummaryItemInfo(mContext, holder, composeBean);

        setFooterItemInfo(mContext, holder, composeBean);
    }

    //设置普通话题文章转发Item
    private static void bindArticleTransformItemView(BaseActivity mContext, ViewHolder holder,
                                                     SubjectsComposeBean2 composeBean) {

        bindNormalItemView(mContext, holder, composeBean);

        bindArticleTransformItemInfo(mContext, holder, composeBean);
    }

    //设置动态Item
    private static void bindDynamicItemView(BaseActivity mContext, ViewHolder holder, SubjectsComposeBean2 composeBean) {

        setUserItemInfo(mContext, holder, composeBean);

        setDynamicItemInfo(mContext, holder, composeBean);

        setFooterItemInfo(mContext, holder, composeBean);

    }


    //绑定话题类别下，图片类型的item视图
    private static void bindPhotoItemView(final BaseActivity mContext, ViewHolder holder, SubjectsComposeBean2 composeBean,
                                          int position) {
        bindNormalItemView(mContext, holder, composeBean);

        SubjectProperties properties = null;
        SubjectObjectSubType2 subType2 = SubjectObjectSubType2.safeEnumOf(composeBean.subject != null ? composeBean.subject.objectType.getName() : composeBean.objectType.getName());
        if (subType2 == SubjectObjectSubType2.TRANSMIT_SUBJECT) {
            properties = composeBean.subject != null ? composeBean.subject.sourceSubjectSimple.properties : composeBean.sourceSubjectSimple.properties;
        } else {
            properties = composeBean.subject != null ? composeBean.subject.properties : composeBean.properties;
        }


        ArrayList<String> midImageUrl = null;
        if (properties != null) {
            midImageUrl = properties.midImageUrl;
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

        final SubjectProperties finalProperties = properties;
        holder.setOnClickListener(R.id.photo_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageGalleryActivity.startFullScreenImageGalleryActivity(mContext, finalProperties.midImageUrl, 0);
            }
        });
    }

    //绑定话题类别下，篮球 足球名片的item视图
    public static void setRaceCardItemView(BaseActivity mContext, ViewHolder holder, SubjectsComposeBean composeBean, boolean haveSubject, int position) {

        bindNormalItemView(mContext, holder, null);

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


    //用户基本信息
    private static void setUserItemInfo(final Activity activity, ViewHolder holder, final SubjectsComposeBean2 composeBean2) {
        TextView dynamicTypeTv = holder.getView(R.id.dynamicTypeTv);
        View spilt_view = holder.getView(R.id.spilt_view);
        spilt_view.setVisibility(View.VISIBLE);
        dynamicTypeTv.setVisibility(View.GONE);
        if (composeBean2.showHotIcon || composeBean2.showNewsDynamicIcon) {
            dynamicTypeTv.setVisibility(View.VISIBLE);
            spilt_view.setVisibility(View.GONE);
            if (composeBean2.showHotIcon) {
                dynamicTypeTv.setText("热门动态");
                dynamicTypeTv.setCompoundDrawablesWithIntrinsicBounds((activity.getResources().getDrawable(R.drawable.icon_homepage_hot)), null, null, null);
            } else {
                dynamicTypeTv.setText("最新动态");
                dynamicTypeTv.setCompoundDrawablesWithIntrinsicBounds((activity.getResources().getDrawable(R.drawable.icon_homepage_newest)), null, null, null);
            }
        }
        //动态勋章 ，循环遍历添加
        MedalLayout medalLayout = holder.getView(R.id.medal_layout);
        medalLayout.removeAllViews();
        if (composeBean2.userMedalLevelList != null) {
            for (UserMedalLevelBean medalLevel : composeBean2.userMedalLevelList) {
                medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(activity, medalLevel.medalConfigCode, medalLevel.currentMedalLevelConfigCode));
            }
        }

        //是否在直播
        holder.getView(R.id.live).setVisibility(composeBean2.inChannelArea ? View.VISIBLE : View.GONE);
        holder.setOnClickListener(R.id.live, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveActivity.startLiveActivity(activity, composeBean2.channelAreaId);
            }
        });

        //用户名 头像啥的
        String logoUrl = composeBean2.subject == null ? composeBean2.userLogoUrl : composeBean2.subject.userLogoUrl;
        holder.setImageByUrl(activity, R.id.title_portraitIv, logoUrl, R.drawable.user_default_logo)
                .setText(R.id.title_name, composeBean2.subject == null ? composeBean2.loginName : composeBean2.subject.loginName)//判断
                .setText(R.id.title_date, TimeUtils.formatTime(TimeUtils.getCurrentTime(), composeBean2.gmtCreate));

        TextView mTvName = holder.getView(R.id.title_name);
        int parentWidth = ((LinearLayout) mTvName.getParent()).getMeasuredWidth();
        int textWidth = (int) mTvName.getPaint().measureText(composeBean2.subject == null ? composeBean2.loginName : composeBean2.subject.loginName);
        if ((textWidth + medalLayout.getFinalWidth() + DimensionUtils.dpToPx(5, activity)) > parentWidth) {
            mTvName.getLayoutParams().width = parentWidth - medalLayout.getFinalWidth() - (int) DimensionUtils.dpToPx(5, activity);
        } else {
            mTvName.getLayoutParams().width = textWidth;
        }

        //用户头像点击去个人主页
        holder.getView(R.id.title_portraitIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(activity, composeBean2.userId);
            }
        });
    }

    //标题 摘要
    private static void setTitleSummaryItemInfo(Context mContext, ViewHolder holder,
                                                SubjectsComposeBean2 composeBean2) {
        boolean isTitleEmpty = TextUtils.isEmpty(composeBean2.subject != null ? composeBean2.subject.title : composeBean2.title);

        HtmlEmojiTextView titleTv = holder.getView(R.id.item_title);

        String summary = composeBean2.subject != null ? composeBean2.subject.summary : composeBean2.summary;

        if (!isTitleEmpty) {
            titleTv.setMaxLines(TextUtils.isEmpty(summary) ? 2 : 1);
            titleTv.setEllipsizeText(composeBean2.subject != null ? composeBean2.subject.title : composeBean2.title);
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        HtmlEmojiTextView summaryHtmlTv = holder.getView(R.id.item_summary);

        if (summaryHtmlTv != null) {

            summaryHtmlTv.setHtmlText(composeBean2.getSummarySpan(summaryHtmlTv, summary), null, false);//摘要
        }
        summaryHtmlTv.setVisibility(TextUtils.isEmpty(summary) ? View.GONE : View.VISIBLE);
    }


    //文章转发信息
    private static void bindArticleTransformItemInfo(Context mContext, ViewHolder holder, SubjectsComposeBean2 composeBean2) {
        if (composeBean2.isSourceDeleted) {
            holder.setViewVisibility(R.id.transCard_container_layout, View.GONE);
            holder.setViewVisibility(R.id.transCard_delete_layout, View.VISIBLE);
            return;
        }

        holder.setImageByUrl(mContext, R.id.transform_iv, composeBean2.getArticleFirstUrl(), composeBean2.getDefaultIconResource())

                .setText(R.id.transform_title, composeBean2.getSourceTitle())

                .setViewVisibility(R.id.transform_title, TextUtils.isEmpty(composeBean2.getSourceTitle()) ? View.GONE : View.VISIBLE);

        HtmlEmojiTextView view = holder.getView(R.id.transform_content);
        view.setHtmlText(SubjectUtils.getSubjectSummarySpan(view, composeBean2.getSourceSummary()), null, false);
        view.setMaxLines(TextUtils.isEmpty(composeBean2.getSourceTitle()) ? 2 : 1);
    }

    //转发 点赞 回复

    private static void setFooterItemInfo(final AppCompatActivity mContext, final ViewHolder holder, final SubjectsComposeBean2 composeBean2) {

        boolean liked = composeBean2.subject != null ? composeBean2.subject.liked : composeBean2.liked;
        composeBean2.liked = liked;
        int likeCount = composeBean2.subject != null ? composeBean2.subject.likeCount : composeBean2.likeCount;
        composeBean2.likeCount = likeCount;
        int replyCount = composeBean2.subject != null ? composeBean2.subject.replyCount : composeBean2.replyCount;
        composeBean2.replyCount = replyCount;

        int transmitCount = composeBean2.subject != null ? composeBean2.subject.transmitCount : composeBean2.transmitCount;
        holder.setText(R.id.item_support_share, transmitCount > 0 ? transmitCount + "" : "分享")
                .setTextColor(R.id.item_support_tv, liked ? ContextCompat.getColor(mContext, R.color.main_red1) : ContextCompat.getColor(mContext, R.color.subject_footer_gray))
                .setText(R.id.item_support_tv, likeCount == 0 ? "支持" : String.valueOf(likeCount))//点赞数
                .setText(R.id.item_comment, replyCount == 0 ? "回复" : String.valueOf(replyCount))//评论数
                .setCompoundDrawables(R.id.item_support_tv, liked ? R.drawable.ic_support_red : R.drawable.ic_support_gray, 0, 0, 0);

        holder.setOnClickListener(R.id.item_support_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeBean2.liked) {
                    unLike4Subject(composeBean2, mContext, holder);
                } else {
                    like4Subject(composeBean2, mContext, holder);
                }
            }
        });

        holder.setOnClickListener(R.id.item_comment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(mContext, composeBean2);
            }
        });

        holder.setOnClickListener(R.id.item_support_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShareDialogFragment shareDialog = ShareDialogFragment.newInstance();
                shareDialog.show(mContext.getSupportFragmentManager());
                shareDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subjectId = composeBean2.subject == null ? composeBean2.subjectId : composeBean2.subject.subjectId;
                        String objectType = "SUBJECT";
                        String author = composeBean2.subject == null ? composeBean2.loginName : composeBean2.subject.loginName;
                        String logo = TextUtils.isEmpty(composeBean2.getArticleFirstUrl()) ? composeBean2.rewardLogo : composeBean2.getArticleFirstUrl();
                        showShareDialog(mContext,
                                view,
                                author,
                                subjectId,
                                objectType,
                                composeBean2.getSourceTitle(),
                                composeBean2.getSourceSummary(),
                                logo,
                                composeBean2.getSourceDescription(),
                                composeBean2.getDefaultIconResource());
                        shareDialog.dismiss();
                    }
                });

            }
        });

        List<ImpressionDataBean.ImpressionTagSimple> tagSimples = composeBean2.subject != null ? composeBean2.subject.tagViewDatas : composeBean2.tagViewDatas;
        ImpressionLayout impressionLayout = holder.getView(R.id.impressionLayout);
        impressionLayout.setShowAddLayout(false, null);
        impressionLayout.setLoadMoreEnable(false);
        impressionLayout.setVisibility((tagSimples == null || tagSimples.size() <= 0) ? View.GONE : View.VISIBLE);
        impressionLayout.notifyDataView(tagSimples);
    }

    /**
     * 转发话题：objectId为话题id，objectType为SUBJECT
     * 转发PK：objectId为PK游戏局id，objectType为PK_GAME_BOARD
     * 转发极限手速：objectId为期次id，objectType为HAND_SPEED
     *
     * @param activity           所在界面的Activity
     * @param v                  点击个人主页 或者 群空间 群聊的按钮View
     * @param author             上一级话题的作者loginName
     * @param subjectId          话题subjectId
     * @param objectType         话题类型，具体看上面
     * @param title              源标题
     * @param summary            源摘要
     * @param firstArticleUrl    如果是插入了图片的文章 ，第一个插图 的url
     * @param defaultDescription 如果是转发到群聊，群空间的描述
     * @param defaultIcon        如果没有url  那么默认的图片icon资源
     */
    public static void showShareDialog(AppCompatActivity activity, View v, String author, String subjectId, String objectType, String title, String summary, String firstArticleUrl, String defaultDescription, int defaultIcon) {
        int tag = (int) v.getTag();

        switch (tag) {
            case ShareDialogFragment.SHARE_HOME_PAGER://个人主页

                TransformTransitionActivity.startTransformTransitionActivity(activity, author, firstArticleUrl, subjectId, objectType, title, summary, defaultIcon);
                break;
            case ShareDialogFragment.SHARE_GROUP_CHAT://群聊
            case ShareDialogFragment.SHARE_GROUP_ZONE://群空间
                boolean isShareGroup = tag == ShareDialogFragment.SHARE_GROUP_CHAT;
                ShareGroupListDialogFragment dialogFragment = ShareGroupListDialogFragment.newInstance(isShareGroup, subjectId, objectType, defaultDescription);
                dialogFragment.show(activity.getSupportFragmentManager());
                break;
        }

    }

    private static void unLike4Subject(final SubjectsComposeBean2 composeBean2, final AppCompatActivity mContext, final ViewHolder holder) {
        holder.getView(R.id.item_support_tv).setEnabled(false);
        SocialTools.unLike(composeBean2.subject != null ? composeBean2.subject.subjectId : composeBean2.subjectId, "SUBJECT", new SocialTools.onSocialCallback() {
            @Override
            public void onResponse(JSONObject response, boolean success) {
                holder.getView(R.id.item_support_tv).setEnabled(true);
                if (response != null) {
                    try {
                        ((BaseActivity) mContext).operateErrorResponseMessage(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (success) {
                    composeBean2.liked = false;
                    composeBean2.likeCount--;

                    holder.setTextColor(R.id.item_support_tv, composeBean2.liked ? ContextCompat.getColor(mContext, R.color.main_red1) : ContextCompat.getColor(mContext, R.color.subject_footer_gray))
                            .setText(R.id.item_support_tv, composeBean2.likeCount == 0 ? "点赞" : String.valueOf(composeBean2.likeCount))//点赞数
                            .setCompoundDrawables(R.id.item_support_tv, R.drawable.ic_support_gray, 0, 0, 0);
                }
            }
        });
    }

    private static void like4Subject(final SubjectsComposeBean2 composeBean2, final AppCompatActivity mContext, final ViewHolder holder) {
        holder.getView(R.id.item_support_tv).setEnabled(false);
        SocialTools.like(composeBean2.subject != null ? composeBean2.subject.subjectId : composeBean2.subjectId, "SUBJECT", new SocialTools.onSocialCallback() {
            @Override
            public void onResponse(JSONObject response, boolean success) {
                holder.getView(R.id.item_support_tv).setEnabled(true);
                if (response != null) {
                    try {
                        ((BaseActivity) mContext).operateErrorResponseMessage(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (success) {
                    composeBean2.liked = true;
                    composeBean2.likeCount++;
                    holder.setTextColor(R.id.item_support_tv, composeBean2.liked ? ContextCompat.getColor(mContext, R.color.main_red1) : ContextCompat.getColor(mContext, R.color.subject_footer_gray))
                            .setText(R.id.item_support_tv, composeBean2.likeCount == 0 ? "点赞" : String.valueOf(composeBean2.likeCount))//点赞数
                            .setCompoundDrawables(R.id.item_support_tv, R.drawable.ic_support_red, 0, 0, 0);
                }
            }
        });
    }

    //动态 信息  PK 助威  红包 抽奖 火箭  送礼
    public static void setDynamicItemInfo(Context mContext, ViewHolder holder, SubjectsComposeBean2 composeBean2) {

        try {
            String summary = null;
            String title = null;

            if (composeBean2.subject != null) {
                summary = composeBean2.subject.summary;
                title = composeBean2.subject.title;
            } else {
                summary = composeBean2.summary;
                title = composeBean2.title;

            }
            String logo = TextUtils.isEmpty(composeBean2.rewardLogo) ? composeBean2.getArticleFirstUrl() : composeBean2.rewardLogo;
            holder.setImageByUrl(mContext, R.id.transform_iv, logo, composeBean2.getDefaultIconResource());

            HtmlEmojiTextView titleTv = holder.getView(R.id.item_title);
            titleTv.setText(title);
            titleTv.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);

            HtmlEmojiTextView summaryTv = holder.getView(R.id.item_summary);
            summaryTv.setHtmlText(SubjectUtils.getSubjectSummarySpan(summaryTv, summary), null, false);
            summaryTv.setMaxLines(TextUtils.isEmpty(title) ? 2 : 1);
            summaryTv.setVisibility(TextUtils.isEmpty(summary) ? View.GONE : View.VISIBLE);


            if (composeBean2.isSourceDeleted) {
                holder.setViewVisibility(R.id.transCard_container_layout, View.GONE);
                holder.setViewVisibility(R.id.transCard_delete_layout, View.VISIBLE);
                return;
            }

            TextView transTitle = holder.getView(R.id.transform_title);
            transTitle.setText(composeBean2.getSourceTitle());
            transTitle.setVisibility(TextUtils.isEmpty(composeBean2.getSourceTitle()) ? View.GONE : View.VISIBLE);

            HtmlEmojiTextView transContent = holder.getView(R.id.transform_content);
            String sourceSummary = composeBean2.getSourceSummary();
            transContent.setHtmlText(SubjectUtils.getSubjectSummarySpan(transContent, sourceSummary), null, false);
            transContent.setMaxLines(TextUtils.isEmpty(composeBean2.getSourceTitle()) ? 2 : 1);
            transContent.setVisibility(TextUtils.isEmpty(sourceSummary) ? View.GONE : View.VISIBLE);

        } catch (Exception e) {
        }
    }
}
