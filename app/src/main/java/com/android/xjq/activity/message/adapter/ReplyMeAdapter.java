//package com.android.xjq.activity.message.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.text.Html;
//import android.text.SpannableString;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.method.LinkMovementMethod;
//import android.text.style.ImageSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.android.banana.commlib.bean.NormalObject;
//import com.android.banana.commlib.utils.LibAppUtil;
//import com.android.banana.commlib.utils.picasso.PicUtils;
//import com.android.banana.groupchat.chatenum.DiscoveryEnum;
//import com.android.library.Utils.LogUtils;
//import com.android.xjq.R;
//import com.android.xjq.activity.NewsDetailActivity;
//import com.android.xjq.activity.SubjectDetailActivity;
//import com.android.xjq.activity.homepage.HomePageActivity;
//import com.android.xjq.activity.message.ReplyDetailActivity;
//import com.android.xjq.activity.message.ReplyMeActivity;
//import com.android.xjq.activity.message.WriteMySubjectActivity;
//import com.android.xjq.adapter.main.MyBaseAdapter;
//import com.android.xjq.bean.BaseObject;
//import com.android.xjq.bean.JcSnapshotBean;
//
//import com.android.xjq.bean.message.CommentBean;
//import com.android.xjq.bean.message.InfoBean;
//import com.android.xjq.bean.message.ReplyMeBean;
//import com.android.xjq.bean.message.SubjectsBean;
//import com.android.xjq.bean.order.PurchaseSnapshotBean;
//import com.android.xjq.model.SubjectObjectTypeEnum;
//import com.android.xjq.model.comment.CommentTypeEnum;
//import com.android.xjq.utils.details.DetailsHtmlShowUtils;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by zhouyi on 2016/1/6.
// */
//public class ReplyMeAdapter extends BaseAdapter{
//
//
//    protected Context context;
//
//    protected LayoutInflater layoutInflater;
//
//    protected List<ReplyMeBean.MessagesBean> list;
//    //  private SnapMatchShowController matchShowController = new SnapMatchShowController();
//
//    public ReplyMeAdapter(Context context, List list) {
//        this.context = context;
//        this.list = list;
//        layoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.item_reply_me, null);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        setItemView(position, viewHolder);
//        return convertView;
//    }
//
//
//    protected void setItemView(int position, ViewHolder holder) {
//
//        final ReplyMeBean.MessagesBean bean = list.get(position);
//
//        PicUtils.load(context,holder.portraitIv,bean.getUserLogoUrl());
//
//        if (bean.isVip()) {
//            holder.vipIv.setVisibility(View.VISIBLE);
//        } else {
//            holder.vipIv.setVisibility(View.GONE);
//        }
//
//        holder.timeTv.setText(bean.getGmtCreate());
//
//        holder.userNameTv.setText(bean.getSenderName());
//
//        setMessage(position, holder);
//
//        setSnapLayout(position, holder);
//
//        holder.portraitIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                  HomePageActivity.startHomepageActivity((Activity) context, bean.getSenderId());
//
//            }
//        });
//    }
//
//    private void setSnapLayout(int position, ViewHolder viewHolder) {
//        com.android.jjx.sdk.utils.LogUtils.e("kk",position+"---setSnapLayout-");
//        viewHolder.subjectLayout.setVisibility(View.VISIBLE);
//
//        viewHolder.subjectDeleteLayout.setVisibility(View.GONE);
//
//        //  viewHolder.matchShowHolder.matchShowLayout.setVisibility(View.GONE);
//
//        viewHolder.snapContentLayout.setVisibility(View.VISIBLE);
//
//        viewHolder.messageTypeTv.setVisibility(View.VISIBLE);
//
//        viewHolder.messageTypeTv.setText("回复");
//
//        viewHolder.newsTitleTv.setVisibility(View.VISIBLE);
//
//        viewHolder.matchTypeIv.setVisibility(View.GONE);
//
//        final ReplyMeBean.MessagesBean bean = list.get(position);
//
//        CommentBean comment = bean.getComment();
//
//        final InfoBean info = bean.getInfo();
//
//        final PurchaseSnapshotBean purchaseOrder = bean.getPurchaseOrder();
//
//        final SubjectsBean subject = bean.getSubject();
//        final NormalObject objectType = comment.getCommentObjectType();
//        if(objectType!=null){
//
//
//            switch (CommentTypeEnum.safeValueOf(objectType.getName())) {
//                case CMS_NEWS:
//                    viewHolder.qiuBaoTypeIv.setImageResource(R.drawable.icon_qiubao_news);
//                    viewHolder.newsLayout.setVisibility(View.VISIBLE);
//                    viewHolder.subjectTv.setVisibility(View.GONE);
//                    if (info.isDelete() || !info.isPublished()) {
//                        viewHolder.subjectDeleteLayout.setVisibility(View.VISIBLE);
//                        viewHolder.snapContentLayout.setVisibility(View.GONE);
//                        viewHolder.warnMessageTv.setText("抱歉，该资讯已删除");
//                    } else {
//                        viewHolder.newsTitleTv.setText(info.getTitle());
//                        viewHolder.newsContentTv.setText(info.getSummary());
//                        viewHolder.newsContentTv.setMaxLines(1);
//                    }
//                    break;
//                case SUBJECT:
//                    viewHolder.qiuBaoTypeIv.setImageResource(R.drawable.icon_qiubao_subject);
//                    viewHolder.newsLayout.setVisibility(View.VISIBLE);
//                    viewHolder.subjectTv.setVisibility(View.GONE);
//                    if (subject.isDelete()) {
//                        viewHolder.subjectDeleteLayout.setVisibility(View.VISIBLE);
//                        viewHolder.snapContentLayout.setVisibility(View.GONE);
//                        viewHolder.warnMessageTv.setText("抱歉，该话题已删除");
//                    } else {
//                        if (bean.getSubject() != null) {
//                            switch (SubjectObjectTypeEnum.safeValueOf(bean.getSubject().getObjectType().getName())) {
//                                case ARTICLE:
//                                    viewHolder.qiuBaoTypeIv.setImageResource(R.drawable.icon_article_subject_logo);
//                                case NORMAL:
//                                    setSubjectNormalView(bean.getSubject(), viewHolder);
//                                    break;
//                                case FT_SEASON_RACE:
//                                case BT_SEASON_RACE:
//                                    viewHolder.qiuBaoTypeIv.setImageResource(R.drawable.icon_qiubao_match);
//                                    // setSubjectFocusMatchView(bean.getSubject(), viewHolder);
//                                    break;
//                            }
//                        }
//                    }
//                    break;
//                case LOTTERY_PROJECT:
//                    viewHolder.qiuBaoTypeIv.setImageResource(R.drawable.icon_qiubao_order);
//                    viewHolder.newsLayout.setVisibility(View.VISIBLE);
//                    viewHolder.subjectTv.setVisibility(View.GONE);
//                    viewHolder.newsTitleTv.setText(purchaseOrder.getUserName());
//                    viewHolder.newsContentTv.setText(purchaseOrder.getPurchaseNo());
//                    viewHolder.newsContentTv.setMaxLines(1);
//                    break;
//                case JCZQ_RACE:
//                    viewHolder.snapContentLayout.setVisibility(View.GONE);
//                    viewHolder.subjectLayout.setVisibility(View.GONE);
//                    viewHolder.messageTypeTv.setText("回复");
//                    // setMatchShow(viewHolder, bean.getJczqRaceData(), DiscoveryEnum.JCZQ_MATCH);
//                    break;
//                default:
//                    viewHolder.subjectDeleteLayout.setVisibility(View.VISIBLE);
//                    viewHolder.snapContentLayout.setVisibility(View.GONE);
//                    viewHolder.messageTypeTv.setVisibility(View.GONE);
//                    viewHolder.warnMessageTv.setText(context.getResources().getString(R.string.defaultShowMessage));
//                    break;
//            }
//        }
//        final View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isDeleted(bean)) {
//                    return;
//                }
//                switch (CommentTypeEnum.safeValueOf(objectType.getName())) {
//                    case CMS_NEWS:
//                        NewsDetailActivity.startNewsDetailActivity((Activity) context, String.valueOf(info.getId()));
//                        break;
//                    case SUBJECT:
//                        switch (SubjectObjectTypeEnum.safeValueOf(bean.getSubject().getObjectType().getName())) {
//                            case ARTICLE:
//                            case NORMAL:
//                                SubjectDetailActivity.startSubjectDetailActivity((Activity) context, subject.getSubjectId());
//                                break;
//                            case FT_SEASON_RACE:
//                            case BT_SEASON_RACE:
//                                // JcSnapshotBean jcSnapshotBean = bean.getSubject().getJcSnapshotBean();
//                                // MatchDetailActivity.startMatchDetailActivity((Activity) context, jcSnapshotBean.getMatchType(), jcSnapshotBean.getBizId(), jcSnapshotBean.getRaceId());
//                                break;
//                        }
//                        break;
//                    case LOTTERY_PROJECT:
//                        // OrderDetailsActivity.startOrderDetailsActivity((Activity) context, purchaseOrder.getPurchaseNo());
//                        break;
//                }
//            }
//        };
//
//        viewHolder.subjectLayout.setOnClickListener(listener);
//
//        viewHolder.subjectTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(v);
//            }
//        });
//    }
//
//    private void setSubjectNormalView(SubjectsBean subject, ViewHolder viewHolder) {
//        if (subject.getTitle() != null) {
//            viewHolder.newsTitleTv.setText(subject.getTitle());
//            viewHolder.newsContentTv.setMaxLines(1);
//            DetailsHtmlShowUtils.setHtmlText(viewHolder.newsContentTv, subject.getFilterHtmlSummary(), false);
//        } else {
//            viewHolder.newsTitleTv.setVisibility(View.GONE);
//            viewHolder.newsContentTv.setMaxLines(2);
//            DetailsHtmlShowUtils.setHtmlText(viewHolder.newsContentTv, subject.getFilterHtmlSummary(), false);
//        }
//    }
//
//
//    private boolean isDeleted(ReplyMeBean.MessagesBean bean) {
//
//        InfoBean info = bean.getInfo();
//
//        SubjectsBean subject = bean.getSubject();
//
//        if (subject != null && subject.isDelete()) {
//            LibAppUtil.showTip(context, context.getResources().getString(R.string.deleteSubjectMessage));
//            return true;
//        }
//
//        if (info != null && (!info.isPublished() || info.isDelete())) {
//            LibAppUtil.showTip(context, "抱歉，该资讯已删除");
//            return true;
//        }
//        return false;
//
//    }
//
//    private void setMessage(final int position, ViewHolder holder) {
//
//        final ReplyMeBean.MessagesBean bean = list.get(position);
//
//        holder.contentShowLayout.setVisibility(View.VISIBLE);
//
//        final View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (bean.getComment().getCommentObjectType()!=null&&isDefaultShow(bean.getComment().getCommentObjectType().getName())) {
//                    LibAppUtil.showTip(context, "请到网站上操作");
//                    return;
//                }
//
//                if (isDeleted(position)) {
//                    LibAppUtil.showTip(context, "抱歉，原内容已删除");
//                    return;
//                }
//                if (bean.getCommentReply() == null) {
//                    showCommentDetail(getLouZhuId(bean),
//                            bean.getComment().getCommentId(),
//                            null,
//                            getObjectId(bean),
//                            getCommentTypeEnum(bean),
//                            true,
//                            bean.getComment().getFloor(),
//                            true, false,
//                            null);
//                } else {
//                    showCommentDetail(getLouZhuId(bean),
//                            bean.getComment().getCommentId(),
//                            bean.getCommentReply().getCommentReplyId(),
//                            getObjectId(bean),
//                            getCommentTypeEnum(bean),
//                            true,
//                            bean.getComment().getFloor(),
//                            true, false,
//                            null);
//                }
////                LogUtils.i("xxl","commentType-"+commentType+"-"+objectId);
//                //     WriteMySubjectActivity.startWriteMySubjectActivity( context, WriteMySubjectActivity.WriteTypeEnum.WRITE_COMMENT,  objectId, commentType);
//            }
//        };
//
//        holder.mainLayout.setOnClickListener(listener);
//
//        //对话题或者方案评论
//        if (bean.getCommentReply() == null) {
//
//            holder.myContentTv.setVisibility(View.GONE);
//
//            holder.replyTv.setMaxLines(2);
//
//            if (bean.getComment().isUserDeleted() || bean.getComment().isSystemDeleted()) {
//
//                setDeleteMessage(holder.replyTv, context.getString(R.string.deleteCommentMessage));
//
//            } else {
//
//                DetailsHtmlShowUtils.setHtmlText(holder.replyTv, bean.getComment().getContent());
//
//            }
//
//
//        } else {  //对评论进行回复、对回复进行回复
//
//            holder.replyTv.setMaxLines(1);
//
//            holder.myContentTv.setMaxLines(1);
//
//            holder.myContentTv.setVisibility(View.VISIBLE);
//            if(bean.getComment().getCommentObjectType()!=null){
//                //TODO
//                switch (CommentTypeEnum.safeValueOf(bean.getComment().getCommentObjectType().getName())) {
//
//                    case JCZQ_RACE:
//                    case CMS_NEWS:
//                    case SUBJECT:
//                    case LOTTERY_PROJECT: {
//
//                        if (bean.getCommentParentReply() == null) {   //对评论进行回复
//
//                            if ( bean.getCommentReply().isSystemDeleted() || bean.getCommentReply().isUserDeleted()) {
//                                holder.replyTv.setText("抱歉,该内容已删除");
//                            } else {
//                                DetailsHtmlShowUtils.setHtmlText(holder.replyTv, bean.getCommentReply().getContent());
//                            }
//
//                            if (bean.getComment().isUserDeleted() || bean.getComment().isSystemDeleted()) {
//
//                                holder.myContentTv.setText(getDeleteMessage(holder.myContentTv, bean.getComment().getUserName(), bean.getComment().getUserId()));
//
//                            } else {
//                                try {
//                                    //TODO
//                                    String str = "//<a href='[USER_ID]'>@[USER_NAME]:</a>".replace("[USER_ID]", "userId=" + bean.getComment().getUserId()).replace("[USER_NAME]", bean.getComment().getUserName());
//
//                                    if (bean.getComment().getPredictSpanMessage() != null) {
//
//                                        SpannableStringBuilder atSpan = (SpannableStringBuilder) Html.fromHtml(str);
//
//                                        DetailsHtmlShowUtils.formatAtSpan(atSpan);
//
//                                        atSpan.append(bean.getComment().getPredictSpanMessage());
//
//                                        DetailsHtmlShowUtils.setHtmlText(holder.myContentTv, bean.getComment().getFilterHtmlContent(), true, atSpan);
//
//                                    } else {
//
//                                        String htmlContent = str + bean.getComment().getFilterHtmlContent();
//
//                                        DetailsHtmlShowUtils.setHtmlText(holder.myContentTv, htmlContent, true, bean.getComment().getPredictSpanMessage());
//
//                                    }
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        } else {  //对回复进行回复
//
//                            DetailsHtmlShowUtils.setHtmlText(holder.replyTv, bean.getCommentReply().getContent());
//
//                            if (bean.getCommentParentReply().isUserDeleted() || bean.getCommentParentReply().isSystemDeleted()) {
//
//                                holder.myContentTv.setText(getDeleteMessage(holder.myContentTv, bean.getCommentParentReply().getLoginName(), bean.getCommentParentReply().getUserId()));
//
//                            } else {
//
//                                String str = "//<a href='[USER_ID]'>[USER_NAME]</a>".replace("[USER_ID]", "userId=" + bean.getComment().getUserId()).replace("[USER_NAME]", "@" + bean.getComment().getUserName());
//
//                                String htmlContent = str + bean.getCommentParentReply().getContent();
//
//                                DetailsHtmlShowUtils.setHtmlText(holder.myContentTv, htmlContent, true);
//
//                            }
//
//                        }
//                    }
//                    break;
//
//                    default: {
//
//                        holder.contentShowLayout.setVisibility(View.GONE);
//
//                    }
//                    break;
//                }
//            }
//        }
//
//        //点击回复按钮显示回复输入框
//        holder.replyLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (bean.getComment().getCommentObjectType()!=null&&isDefaultShow(bean.getComment().getCommentObjectType().getName())) {
//                    LibAppUtil.showTip(context, "请到网站上操作");
//                    return;
//                }
//
//                if (isDeleted(position)) {
//                    LibAppUtil.showTip(context, "抱歉,原内容已删除,无法进行回复");
//                    return;
//                }
//
//                if (isCommentOff(position)) {
//                    LibAppUtil.showTip(context, context.getResources().getString(R.string.comment_off_tip));
//                    return;
//                }
//
//                String commentId = bean.getComment().getCommentId();
//
//                String commentReplyId = null;
//
//                if (bean.getCommentReply() != null) {
//                    commentReplyId = bean.getCommentReply().getCommentReplyId();
//                }
//
//                ((ReplyMeActivity) context).showReplyLayout(commentId, commentReplyId, bean.getSenderName());
//            }
//        });
//
//        holder.replyTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(v);
//            }
//        });
//
//        holder.myContentTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(v);
//            }
//        });
//    }
////
////    private CommentShowMatchDetail createShowMatchDetail(SubjectsBean subjectsBean) {
////        if (subjectsBean == null) return null;
////        switch (SubjectObjectTypeEnum.safeValueOf(subjectsBean.getObjectType().getName())) {
////            case FT_SEASON_RACE:
////            case BT_SEASON_RACE:
////                CommentShowMatchDetail detail = new CommentShowMatchDetail();
////                JcSnapshotBean jcSnapshotBean = subjectsBean.getJcSnapshotBean();
////                detail.setBizId(jcSnapshotBean.getBizId());
////                detail.setRaceId(jcSnapshotBean.getRaceId());
////                detail.setType(jcSnapshotBean.getMatchType());
////                return detail;
////        }
////        return null;
////    }
////
////    private void setMatchShow(ViewHolder holder, final JcSnapshotBean bean, DiscoveryEnum discoveryEnum) {
////
////        if (bean == null) {
////            return;
////        }
////
////        matchShowController.setMatchShow(context, holder.matchShowHolder, bean, discoveryEnum);
////    }
//
//    /**
//     * 判断该评论是否已经关闭评论了。
//     *
//     * @param position
//     * @return
//     */
//    private boolean isCommentOff(int position) {
//
//        ReplyMeBean.MessagesBean bean = list.get(position);
//
//        InfoBean info = bean.getInfo();
//
//        SubjectsBean subject = bean.getSubject();
//
//        PurchaseSnapshotBean purchaseOrder = bean.getPurchaseOrder();
//
//        if (subject != null && subject.isCommentOff()) {
//            return true;
//        }
//
//        if (info != null && info.isCommentOff()) {
//            return true;
//        }
//
//        if (purchaseOrder != null && purchaseOrder.isCommentOff()) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private SpannableStringBuilder getDeleteMessage(TextView tv, String userName, String userId) {
//
//        SpannableStringBuilder ssb = new SpannableStringBuilder();
//
//        ssb.append("//");
//
//        //  ssb.append(getAtSpan("@" + userName, "userId=" + userId));
//
//        ssb.append(" ");
//
//        SpannableString ss = new SpannableString(" ");
//
//        //   ss.setSpan(new ImageSpan(JczjUtils.getImageSpanDrawable(tv, R.drawable.icon_subject_deleted)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        ssb.append(ss);
//
//        ssb.append(" ");
//
//        ssb.append("抱歉,该内容已删除");
//
//        tv.setLinkTextColor(Color.parseColor("#559ce8"));
//
//        tv.setMovementMethod(LinkMovementMethod.getInstance());
//
//        //    setOnTouchListener(tv);
//
//        return ssb;
//
//    }
//
//    /**
//     * 该内容是否是默认显示的
//     *
//     * @return
//     */
//    private boolean isDefaultShow(String type) {
//        switch (CommentTypeEnum.safeValueOf(type)) {
//            case CMS_NEWS:
//            case SUBJECT:
//            case LOTTERY_PROJECT:
//            case JCZQ_RACE:
//                return false;
//            default:
//                return true;
//        }
//    }
//
//    private boolean isDeleted(int position) {
//        ReplyMeBean.MessagesBean bean = list.get(position);
//        SubjectsBean subject = bean.getSubject();
//        InfoBean info = bean.getInfo();
//        if (subject != null && subject.isDelete()) {
//            return true;
//        }
//        if (bean.getComment() != null && (bean.getComment().isSystemDeleted() || bean.getComment().isUserDeleted())) {
//            return true;
//        }
//        if (info != null && (info.isDelete() || !info.isPublished())) {
//            return true;
//        }
//        return false;
//    }
//
//    private void setDeleteMessage(TextView tv, String message) {
//        tv.setText(message);
//    }
//
//    private String getObjectId(ReplyMeBean.MessagesBean bean) {
//        if (bean.getSubject() != null) {
//            return bean.getSubject().getSubjectId();
//        }
//        if (bean.getInfo() != null) {
//            return String.valueOf(bean.getInfo().getId());
//        }
//        if (bean.getPurchaseOrder() != null) {
//            return bean.getPurchaseOrder().getPurchaseNo();
//        }
//        if (bean.getJczqRaceData() != null) {
//            return bean.getJczqRaceData().getBizId() + ":" + bean.getJczqRaceData().getId();
//        }
//        return null;
//    }
//
//    private String getLouZhuId(ReplyMeBean.MessagesBean bean) {
//        if (bean.getSubject() != null) {
//            return bean.getSubject().getUserId();
//        }
//        if (bean.getInfo() != null) {
//            return "0";
//        }
//        if (bean.getPurchaseOrder() != null) {
//            return bean.getPurchaseOrder().getUserId();
//        }
//        return null;
//    }
//    private CommentTypeEnum getCommentTypeEnum(ReplyMeBean.MessagesBean bean) {
//        if (bean.getSubject() != null) {
//            return CommentTypeEnum.SUBJECT;
//        }
//        if (bean.getInfo() != null) {
//            return CommentTypeEnum.CMS_NEWS;
//        }
//        if (bean.getPurchaseOrder() != null) {
//            return CommentTypeEnum.LOTTERY_PROJECT;
//        }
//        if (bean.getJczqRaceData() != null) {
//            return CommentTypeEnum.JCZQ_RACE;
//        }
//        return null;
//    }
//    private String getCommentType(ReplyMeBean.MessagesBean bean) {
//        if (bean.mainType!= null) {
//            return bean.mainType.getName();
//        }
//        return "";
//    }
//    protected void showCommentDetail(String louzhuId,
//                                     String commentId,
//                                     String commentReplyId,
//                                     String objectId,
//                                     CommentTypeEnum type,
//                                     boolean showDetail,
//                                     int floor,
//                                     boolean showKeyboard) {
//        ReplyDetailActivity.ShowCommentDetailBean bean = getCommentDetailBean(louzhuId,
//                commentId,
//                commentReplyId,
//                objectId,
//                type,
//                showDetail,
//                floor
//        );
//        bean.setShowKeyboard(showKeyboard);
//        ReplyDetailActivity.startReplyDetailActivity((Activity) context, bean);
//    }
//    public ReplyDetailActivity.ShowCommentDetailBean getCommentDetailBean(String louzhuId,
//                                                                          String commentId,
//                                                                          String commentReplyId,
//                                                                          String objectId,
//                                                                          CommentTypeEnum type,
//                                                                          boolean showDetail,
//                                                                          int floor
//    ) {
//        ReplyDetailActivity.ShowCommentDetailBean bean = new ReplyDetailActivity.ShowCommentDetailBean();
//        bean.setCommentId(commentId);
//        bean.setCommentReplyId(commentReplyId);
//        bean.setCommentType(type);
//        bean.setObjectId(objectId);
//        bean.setShowDetail(showDetail);
//        bean.setLouZhuId(louzhuId);
//        bean.setFloor(floor);
//
//        return bean;
//    }
//    protected void showCommentDetail(String louzhuId,
//                                     String commentId,
//                                     String commentReplyId,
//                                     String objectId,
//                                     CommentTypeEnum type,
//                                     boolean showDetail,
//                                     int floor,
//                                     boolean showReply,
//                                     boolean showKeyboard,
//                                     BaseObject baseObject) {
//
//        ReplyDetailActivity.ShowCommentDetailBean bean = getCommentDetailBean(louzhuId,
//                commentId,
//                commentReplyId,
//                objectId,
//                type,
//                showDetail,
//                floor);
//
//        bean.setBaseObject(baseObject);
//        bean.setShowKeyboard(showKeyboard);
//        ReplyDetailActivity.startReplyDetailActivity((Activity) context, bean);
//    }
//
//
//    static class ViewHolder {
//
//        @BindView(R.id.portraitIv)
//        CircleImageView portraitIv;
//        @BindView(R.id.vipIv)
//        ImageView vipIv;
//        @BindView(R.id.userNameTv)
//        TextView userNameTv;
//        @BindView(R.id.timeTv)
//        TextView timeTv;
//        @BindView(R.id.messageTypeTv)
//        TextView messageTypeTv;
//        @BindView(R.id.replyCountTv)
//        TextView replyCountTv;
//        @BindView(R.id.replyLayout)
//        LinearLayout replyLayout;
//        @BindView(R.id.replyTv)
//        TextView replyTv;
//        @BindView(R.id.myContentTv)
//        TextView myContentTv;
//        @BindView(R.id.contentShowLayout)
//        LinearLayout contentShowLayout;
//        @BindView(R.id.qiuBaoTypeIv)
//        ImageView qiuBaoTypeIv;
//        @BindView(R.id.subjectTv)
//        TextView subjectTv;
//        @BindView(R.id.newsTitleTv)
//        TextView newsTitleTv;
//        @BindView(R.id.newsContentTv)
//        TextView newsContentTv;
//        @BindView(R.id.newsLayout)
//        LinearLayout newsLayout;
//        @BindView(R.id.snapContentLayout)
//        LinearLayout snapContentLayout;
//        @BindView(R.id.warnMessageTv)
//        TextView warnMessageTv;
//        @BindView(R.id.subjectDeleteLayout)
//        LinearLayout subjectDeleteLayout;
//        @BindView(R.id.subjectLayout)
//        LinearLayout subjectLayout;
//
//        @BindView(R.id.mainLayout)
//        LinearLayout mainLayout;
//
//        @BindView(R.id.matchTypeIv)
//        ImageView matchTypeIv;
//
//
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
