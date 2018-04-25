package com.android.xjq.adapter.comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.bean.JczjMedalBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.StringUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.xjq.R;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.message.ReplyDetailActivity;
import com.android.xjq.bean.comment.CommentReplyBean;
import com.android.xjq.bean.comment.Properties;
import com.android.xjq.bean.comment.UserCommentBean;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterCallback;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterHelper;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.android.xjq.utils.XjqUtils;
import com.android.xjq.utils.details.ReportDialogUtils;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhouyi on 2016/1/8 11:17.
 */
public class CommentAdapter extends MyBaseAdapter<UserCommentBean.CommentListBean> implements SubjectMultiAdapterCallback {

    private String louZhuId = "0";

    private String subjectId = null;

    private String objectType = null;

    private int mTouchPosition = 0;

    private ViewHolder mCurrentClickSupportViewHolder;

    private OnMyClickListener mRefreshListener;

    private View mCurrentZhiDingIndicator = null;

    private int mCurrentZhiDingPos = -1;

    private SubjectMultiAdapterHelper mAdapterHelper;

    private ReportDialogUtils reportDialogUtils;

    private boolean haveNoClick = true;

    public void clickScreen(){
        if(mCurrentZhiDingPos>-1&&list.size()>mCurrentZhiDingPos
                &&list.get(mCurrentZhiDingPos).popLayoutVisible){
            list.get(mCurrentZhiDingPos).popLayoutVisible = false;
            notifyDataSetChanged();
        }
    }

    public CommentAdapter(Context context, List<UserCommentBean.CommentListBean> list) {

        super(context, list);

        mAdapterHelper = new SubjectMultiAdapterHelper(this);
    }

    public void setRefreshListener(OnMyClickListener refreshListener) {

        mRefreshListener = refreshListener;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setLouZhuId(String louZhuId) {
        this.louZhuId = louZhuId;
    }


    @Override
    public int getCount() {
        if (list == null || list.size() == 0) {
            return 1;
        } else {
            return super.getCount();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_user_comment, null);

            holder = new ViewHolder(convertView);

            View contentView = createContentView(holder);

            holder.mainLayout.addView(contentView, 1);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        setItemView(position, holder);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list != null && list.size() > 0) {
                    showCommentDetail(louZhuId,
                            list.get(position).getCommentId(),
                            null,
                            subjectId,
                            CommentTypeEnum.safeValueOf(objectType),
                            false,
                            list.get(position).getFloor(),
                            false);

                }

            }
        });

        return convertView;
    }

    private View createContentView(ViewHolder viewHolder) {

        View view = layoutInflater.inflate(R.layout.layout_details_comment, null);

        ContentViewHolder contentViewHolder = new ContentViewHolder(view);

        viewHolder.contentViewHolder = contentViewHolder;

        return view;
    }


    private void setItemView(final int position, final ViewHolder holder) {

        if (list == null || list.size() == 0) {

            holder.mainLayout.setVisibility(View.GONE);

            holder.emptyLayout.setVisibility(View.VISIBLE);

            holder.emptyLayout.setBackgroundColor(Color.WHITE);

            return;

        } else {

            holder.mainLayout.setVisibility(View.VISIBLE);

            holder.emptyLayout.setVisibility(View.GONE);
        }

        final UserCommentBean.CommentListBean bean = list.get(position);

        if (bean.isVip()) {
            holder.vipIv.setVisibility(View.VISIBLE);
        } else {
            holder.vipIv.setVisibility(View.GONE);
        }

        if (!CommentTypeEnum.LOTTERY_PROJECT.name().equals(objectType) && bean.getUserId().equals(louZhuId)) {
            holder.louzhuIv.setVisibility(View.VISIBLE);
            //设置勋章
            setJczjUserMedalShow(bean.getUserMedalBeanList(), holder.userInfoLayout, 2);
        } else {
            holder.louzhuIv.setVisibility(View.GONE);

            setJczjUserMedalShow(bean.getUserMedalBeanList(), holder.userInfoLayout, 1);
        }

        holder.louTv.setText((bean.getFloor()) + "楼");

        setIconView(holder.portraitIv, bean.getUserLogoUrl());

        holder.userNameTv.setText(bean.getUserName());

        holder.timeTv.setText(bean.getGmtCreate());


        holder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(context,bean.getUserId());
            }
        });

        holder.userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(context,bean.getUserId());
            }
        });

        setContentShow(position, holder);

        if (bean.getReplyCount() > 0) {

            final List<CommentReplyBean> replyList = bean.getReplyComment();

            for (int j = 0; j < replyList.size(); j++) {
                if (louZhuId.equals(replyList.get(j).getUserId())) {
                    replyList.get(j).setLouZhu(true);
                }
            }

            holder.commentFooterLayout.setVisibility(View.VISIBLE);
            if (bean.getReplyCount() > 3) {
                holder.showMoreTv.setVisibility(View.VISIBLE);
            } else {
                holder.showMoreTv.setVisibility(View.GONE);
            }

            setReplyCommentView(position, holder, replyList);

        } else {

            holder.commentFooterLayout.setVisibility(View.GONE);

        }
        if (bean.isSetTop()) {
            holder.zhiDingIv.setVisibility(View.VISIBLE);
        } else {
            holder.zhiDingIv.setVisibility(View.GONE);
        }

        if(bean.popLayoutVisible){
            holder.popLayout.setVisibility(View.VISIBLE);
        }else{
            holder.popLayout.setVisibility(View.GONE);
        }

        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.popLayout.setVisibility(View.VISIBLE);
                mCurrentZhiDingPos = position;
                bean.popLayoutVisible = true;
                haveNoClick = true;
                showPopUp(holder, v);
            }
        });

        setLikeCount(holder.contentViewHolder.supportCountTv, holder.contentViewHolder.supportImageIv, bean.getLikeCount(), bean.isLiked());


        //回复点击
        holder.contentViewHolder.replyImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCommentDetail(
                        louZhuId,
                        list.get(position).getCommentId(),
                        null,
                        subjectId,
                        CommentTypeEnum.safeValueOf(objectType),
                        false,
                        list.get(position).getFloor(),
                        true);
            }
        });

        //支持点击
        holder.contentViewHolder.supportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentClickSupportViewHolder = holder;
                if (bean.isLiked()) {
                    mAdapterHelper.unLike(position, String.valueOf(bean.getCommentId()), "COMMENT");
                } else {
                    mAdapterHelper.like(position, String.valueOf(bean.getCommentId()), "COMMENT");
                }
            }
        });


        if(position==list.size()-1){
            holder.cutLine.setVisibility(View.GONE);
        }else{
            holder.cutLine.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 一级评论内容的显示
     *
     * @param position
     * @param holder
     */
    private void setContentShow(final int position, ViewHolder holder) {
        if (list.get(position).getNum() > 1) {

            holder.contentViewHolder.lookMoreTv.setVisibility(View.VISIBLE);
        } else {
            holder.contentViewHolder.lookMoreTv.setVisibility(View.GONE);
        }
        if(list.get(position).getReplyCount()>0){
            holder.contentViewHolder.replyTv.setText(list.get(position).getReplyCount()+"");
        }else{
            holder.contentViewHolder.replyTv.setText("");
        }


        if (StringUtils.isBlank(list.get(position).getContent())) {

            holder.contentViewHolder.commentContentTv.setVisibility(View.GONE);

        } else {
            holder.contentViewHolder.commentContentTv.setVisibility(View.VISIBLE);

            holder.contentViewHolder.commentContentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCommentDetail(
                            louZhuId,
                            list.get(position).getCommentId(),
                            null, subjectId,
                            CommentTypeEnum.safeValueOf(objectType),
                            false, list.get(position).getFloor(),
                            false);
                }
            });

            DetailsHtmlShowUtils.setHtmlText(holder.contentViewHolder.commentContentTv, list.get(position).getSummary(),
                    true, list.get(position).getPredictSpanMessage());
        }

        holder.contentViewHolder.commentImageView.setVisibility(View.GONE);

        if (list.get(position).getProperties() != null) {

            Properties properties = list.get(position).getProperties();

            UserCommentBean.CommentListBean commentBean = list.get(position);

            if (properties.getJczqBizId() != null) {

                // setMatchShow(holder, commentBean.getJcSnapshotBean(), commentBean.getDiscoveryEnum());

            } else if (properties.getJclqBizId() != null) {

                //setMatchShow(holder, commentBean.getJcSnapshotBean(), commentBean.getDiscoveryEnum());

            } else if (properties.getPurchaseNo() != null) {

                // setOrderShow(holder, commentBean.getPurchaseSnapshotBean());

            } else if (properties.getMidImageUrl() != null) {

                setPhotoShow(holder, commentBean);
            }
        }
    }


    private void setPhotoShow(ViewHolder holder, final UserCommentBean.CommentListBean commentBean) {

        holder.contentViewHolder.commentImageView.setVisibility(View.VISIBLE);

        int screenWidth = LibAppUtil.getScreenWidth(context);

        Uri uri = Uri.parse(commentBean.getProperties().getMidImageUrl().getUrl());

        int width = Integer.parseInt(commentBean.getProperties().getMidImageUrl().getSrcWidth());

        int height = Integer.parseInt(commentBean.getProperties().getMidImageUrl().getSrcHeight());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                holder.contentViewHolder.commentImageView.getLayoutParams();

        if (width > screenWidth) {//宽度大于屏幕宽度   用屏幕宽度

            params.width = screenWidth;

        } else if (width * 2 < screenWidth) {

            params.width = (int) (1.5 * width);

            height = (int) (1.5 * height);

        } else {

            params.width = width;
        }

        if (height > 1000) {

            params.height = 1000;

        } else {

            params.height = height;
        }

        holder.contentViewHolder.commentImageView.setLayoutParams(params);

        holder.contentViewHolder.commentImageView.setImageURI(uri);

        final ArrayList<String> list = new ArrayList<>();

        list.add(0, commentBean.getProperties().getMidImageUrl().getUrl());

        holder.contentViewHolder.commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageGalleryActivity.startFullScreenImageGalleryActivity((Activity) context, list, 0);
            }
        });

    }


    private void setReplyCommentText(CommentReplyBean bean, TextView textView) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        SpannableString ss = new SpannableString(bean.getLoginName());

        ssb.append(ss);

        ssb.append(" ");
        //添加楼主
        if (!ObjectTypeEnum.LOTTERY_PROJECT.name().equals(objectType) && bean.isLouZhu()) {

            ss = new SpannableString("作者 ");

            Drawable d = context.getResources().getDrawable(R.drawable.icon_author);

            d.setBounds(0, -3, d.getIntrinsicWidth(), d.getIntrinsicHeight() - 3);

            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);

            ss.setSpan(span, 0, "作者".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.append(ss);
        }

        //添加勋章
        if (bean.getUserMedalBeanList() != null && bean.getUserMedalBeanList().size() != 0) {
            for (JczjMedalBean userMedalBean : bean.getUserMedalBeanList()) {
                if ("MANAGER".equals(userMedalBean.getMedalCode())) {
                    ssb.append(DetailsHtmlShowUtils.getHtmlText(textView, "<img src=\"" + userMedalBean.getUserMedalUrl() + "\"/>"));
                    break;
                }
            }
        }

        ssb.append(" : ");

        ss = new SpannableString(ssb);

        ss.setSpan(new CommentReplyDetailAdapter.MyClickableSpan(bean.getUserId(), (Activity) context), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb = new SpannableStringBuilder();

        ssb.append(ss);

        ssb.append(DetailsHtmlShowUtils.getHtmlText(textView, XjqUtils.formatReplyString(bean.getContent())));
        ssb.append(" ");

        ss = new SpannableString(bean.getGmtCreate());

        ss.setSpan(new TextAppearanceSpan(context, R.style.smallDateStyle), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb.append(ss);

        textView.setText(ssb);
    }

    /**
     * 二级评论显示
     *
     * @param position
     * @param holder
     * @param replyList
     */
    private void setReplyCommentView(final int position, ViewHolder holder, List<CommentReplyBean> replyList) {

        TextView[] textViews = {holder.firstComment, holder.secondComment, holder.thirdComment};

        holder.firstComment.setVisibility(View.GONE);

        holder.secondComment.setVisibility(View.GONE);

        holder.thirdComment.setVisibility(View.GONE);

        for (int i = 0; i < replyList.size(); i++) {

            textViews[i].setVisibility(View.VISIBLE);

            final CommentReplyBean bean = replyList.get(i);

            setReplyCommentText(bean, textViews[i]);

            setTouchListener(textViews[i]);

            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCommentDetail(
                            bean.getUserId(),
                            list.get(position).getCommentId(),
                            bean.getCommentReplyId(),
                            subjectId,
                            CommentTypeEnum.safeValueOf(objectType),
                            false,
                            list.get(position).getFloor(),
                            false);
                }
            });

            textViews[i].setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    //TODO
    private void showPopUp(final ViewHolder holder, View v) {

        //置顶的条件:1、话题详情和方案详情2、必须是自己发的方案或者话题
        if (LoginInfoHelper.getInstance().getUserId().equals(louZhuId)) { //判断是否是自己发的
            switch (CommentTypeEnum.safeValueOf(objectType)) {
                case LOTTERY_PROJECT:
                case SUBJECT:
                    holder.zhiDingLayout.setVisibility(View.VISIBLE);

                    String text = "";
                    if (list.get( mCurrentZhiDingPos).isSetTop()) {
                        text = "取消置顶";
                    } else {
                        text = "置顶";
                    }
                    holder.zhiDingTv.setText(text);
                    break;
            }
        }

        holder.jubaoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveNoClick = false;
                if(reportDialogUtils==null){
                    reportDialogUtils = new ReportDialogUtils();
                }
                reportDialogUtils.showReportDialog(context);

                holder.popLayout.setVisibility(View.GONE);

            }
        });

        holder.zhiDingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveNoClick = false;
                mCurrentZhiDingIndicator = holder.zhiDingIv;
                holder.popLayout.setVisibility(View.GONE);
                if (list.get(mCurrentZhiDingPos).isSetTop()) {
                    showCancelZhiDingDialog(list.get(mCurrentZhiDingPos).getCommentId());
                } else {
                    mAdapterHelper.createSetTop(0,list.get(mCurrentZhiDingPos).getCommentId(), true);
                }
            }
        });
    }

    private void showCancelZhiDingDialog(final String commentId) {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setMessage("确定要取消置顶该回复？");
        builder.setPositiveMessage("确定");
        builder.setNegativeMessage("取消");
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterHelper.createSetTop(0,commentId, false);
            }
        });
        ShowMessageDialog dialog = new ShowMessageDialog(context, builder);
    }


    protected void setLikeCount(TextView tv, ImageView iv, int likeCount, boolean liked) {

        if (liked) {
            iv.setImageResource(R.drawable.ic_support_red);
            tv.setTextColor(context.getResources().getColor(R.color.liked_text_color));
        } else {
            iv.setImageResource(R.drawable.ic_support_gray);
            tv.setTextColor(context.getResources().getColor(R.color.unlike_text_color));
        }

        if (likeCount == 0) {
            tv.setText("");
            tv.setVisibility(View.VISIBLE);

        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(likeCount + "");
        }
    }


    protected void showCommentDetail(String louzhuId,
                                     String commentId,
                                     String commentReplyId,
                                     String objectId,
                                     CommentTypeEnum type,
                                     boolean showDetail,
                                     int floor,
                                     boolean showKeyboard) {
        ReplyDetailActivity.ShowCommentDetailBean bean = getCommentDetailBean(louzhuId,
                commentId,
                commentReplyId,
                objectId,
                type,
                showDetail,
                floor
        );
        bean.setShowKeyboard(showKeyboard);
        if(objectType==null){
            ToastUtil.show(context,"抱歉，话题不存在",1000);
            return;
        }
        ReplyDetailActivity.startReplyDetailActivity((Activity) context, bean);
    }


    public ReplyDetailActivity.ShowCommentDetailBean getCommentDetailBean(String louzhuId,
                                                                          String commentId,
                                                                          String commentReplyId,
                                                                          String objectId,
                                                                          CommentTypeEnum type,
                                                                          boolean showDetail,
                                                                          int floor
    ) {
        ReplyDetailActivity.ShowCommentDetailBean bean = new ReplyDetailActivity.ShowCommentDetailBean();
        bean.setCommentId(commentId);
        bean.setCommentReplyId(commentReplyId);
        bean.setCommentType(type);
        bean.setObjectId(objectId);
        bean.setShowDetail(showDetail);
        bean.setLouZhuId(louzhuId);
        bean.setFloor(floor);

        return bean;
    }

    @Override
    public void onLikeOrSetTopResult(JSONObject jsonObject, int position, boolean success, boolean setTopRequest) {
        if (success){
            if (setTopRequest) {
                if (list.get(mCurrentZhiDingPos).isSetTop()) {
                    list.get(mCurrentZhiDingPos).setSetTop(false);
                    mCurrentZhiDingIndicator.setVisibility(View.GONE);
                    LibAppUtil.showTip(context, "取消置顶成功", R.drawable.icon_subscribe_success);
                } else {
                    LibAppUtil.showTip(context, "置顶成功", R.drawable.icon_subscribe_success);
                    list.get(mCurrentZhiDingPos).setSetTop(true);
                    if (mRefreshListener != null) {
                        mRefreshListener.onClick(null);
                    }
                }
                notifyDataSetChanged();
            } else {

                int likeCount = list.get(position).getLikeCount();
                list.get(position).setLiked(true);
                list.get(position).setLikeCount(likeCount + 1);

                setLikeCount(mCurrentClickSupportViewHolder.contentViewHolder.supportCountTv,
                        mCurrentClickSupportViewHolder.contentViewHolder.supportImageIv, likeCount + 1, true);
            }
        }
        else{
            try {
                ((BaseActivity)context).operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }




    @Override
    public void onUnLikeResult(JSONObject jsonObject, int position, boolean success) {
        if (success){
            int likeCount = list.get(position).getLikeCount();
            list.get(position).setLikeCount(likeCount - 1);
            list.get(position).setLiked(false);
            setLikeCount(mCurrentClickSupportViewHolder.contentViewHolder.supportCountTv,
                    mCurrentClickSupportViewHolder.contentViewHolder.supportImageIv, likeCount - 1, false);
        }else{
            try {
                ((BaseActivity)context).operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.userLogoAndNameInfoLayout)
        LinearLayout userLogoAndNameInfoLayout;

        @BindView(R.id.portraitIv)
        CircleImageView portraitIv;
        @BindView(R.id.vipIv)
        ImageView vipIv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.louzhuIv)
        ImageView louzhuIv;
        @BindView(R.id.userInfoLayout)
        LinearLayout userInfoLayout;
        @BindView(R.id.louTv)
        TextView louTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.moreIv)
        ImageView moreIv;
        @BindView(R.id.popLayout)
        LinearLayout popLayout;
        @BindView(R.id.zhiDingIv)
        ImageView zhiDingIv;
        @BindView(R.id.layout_comment_top)
        FrameLayout layoutCommentTop;
        @BindView(R.id.firstComment)
        TextView firstComment;
        @BindView(R.id.secondComment)
        TextView secondComment;
        @BindView(R.id.thirdComment)
        TextView thirdComment;
        @BindView(R.id.showMoreTv)
        TextView showMoreTv;
        @BindView(R.id.commentFooterLayout)
        LinearLayout commentFooterLayout;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        @BindView(R.id.emptyIv)
        ImageView emptyIv;
        @BindView(R.id.emptyTipTv)
        TextView emptyTipTv;
        @BindView(R.id.emptyLayout)
        LinearLayout emptyLayout;
        @BindView(R.id.cutLine)
        View cutLine;
        @BindView(R.id.zhiDingTv)
        TextView zhiDingTv;
        @BindView(R.id.juBaoTv)
        TextView jubaoTv;
        @BindView(R.id.zhiDingLayout)
        LinearLayout zhiDingLayout;
        ContentViewHolder contentViewHolder;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ContentViewHolder {
        @BindView(R.id.commentContentTv)
        TextView commentContentTv;
        @BindView(R.id.commentImageView)
        SimpleDraweeView commentImageView;
        @BindView(R.id.lookMoreTv)
        TextView lookMoreTv;
        @BindView(R.id.analysisTv)
        TextView analysisTv;
        @BindView(R.id.focusMatchShowTimeTv)
        TextView focusMatchShowTimeTv;
        @BindView(R.id.supportImageIv)
        ImageView supportImageIv;
        @BindView(R.id.supportCountTv)
        TextView supportCountTv;
        @BindView(R.id.supportLayout)
        LinearLayout supportLayout;
        @BindView(R.id.replyImageIv)
        ImageView replyImageIv;
        @BindView(R.id.replyTv)
        TextView replyTv;
        @BindView(R.id.commitLayout)
        LinearLayout commitLayout;
        @BindView(R.id.list_item_buttom)
        RelativeLayout listItemButtom;


        ContentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
