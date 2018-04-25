package com.android.xjq.activity.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.xjq.R;
import com.android.xjq.activity.SubjectDetailActivity;
import com.android.xjq.activity.dynamic.TransmitDetailsActivity;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.message.ReplyDetailActivity;
import com.android.xjq.activity.message.ReplyMeActivity;
import com.android.xjq.bean.BaseObject;
import com.android.xjq.bean.message.InfoBean;
import com.android.xjq.bean.message.ReplyMeBean;
import com.android.xjq.bean.message.SubjectsBean;
import com.android.xjq.bean.order.PurchaseSnapshotBean;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.utils.XjqUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zhouyi on 2016/1/6.
 */
public class ReplyMeAdapter2 extends MyBaseAdapter<ReplyMeBean.MessagesBean> {

    public ReplyMeAdapter2(Context context, List list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_reply_me, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setItemView(position, viewHolder);
        return convertView;
    }


    protected void setItemView(int position, ViewHolder holder) {

        final ReplyMeBean.MessagesBean bean = list.get(position);

        PicUtils.load(context, holder.portraitIv, bean.getUserLogoUrl());

        holder.timeTv.setText(bean.getGmtCreate());

        holder.userNameTv.setText(bean.getSenderName());
        DetailsHtmlShowUtils.setHtmlText(holder.replyTv, bean.getShowReply());

        DetailsHtmlShowUtils.setHtmlText(holder.resourceTv, bean.getShowResource());
        click(position,holder,bean);

    }


    private void click(final int position, ViewHolder holder, final ReplyMeBean.MessagesBean bean){
        holder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomePageActivity.startHomepageActivity((Activity) context, bean.getSenderId());
            }
        });


        //点击回复按钮显示回复输入框
        holder.replyCountTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (bean.getComment().getCommentObjectType()!=null&&isDefaultShow(bean.getComment().getCommentObjectType().getName())) {
                    LibAppUtil.showTip(context, "请到网站上操作");
                    return;
                }

                if (isDeleted(position)) {
                    LibAppUtil.showTip(context, "抱歉,原内容已删除,无法进行回复");
                    return;
                }

                if (isCommentOff(position)) {
                    LibAppUtil.showTip(context, context.getResources().getString(R.string.comment_off_tip));
                    return;
                }

                String commentId = bean.getComment().getCommentId();

                String commentReplyId = null;

                if (bean.getCommentReply() != null) {
                    commentReplyId = bean.getCommentReply().getCommentReplyId();
                }

                ((ReplyMeActivity) context).showReplyLayout(commentId, commentReplyId, bean.getSenderName());
            }
        });

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bean.getComment().getCommentObjectType()!=null&&isDefaultShow(bean.getComment().getCommentObjectType().getName())) {
                    LibAppUtil.showTip(context, "请到网站上操作");
                    return;
                }

                if (isDeleted(position)) {
                    LibAppUtil.showTip(context, "抱歉，原内容已删除");
                    return;
                }
                if (bean.getCommentReply() == null) {
                    showCommentDetail(getLouZhuId(bean),
                            bean.getComment().getCommentId(),
                            null,
                            getObjectId(bean),
                            getCommentTypeEnum(bean),
                            true,
                            bean.getComment().getFloor(),
                            true, false,
                            null);
                } else {
                    showCommentDetail(getLouZhuId(bean),
                            bean.getComment().getCommentId(),
                            bean.getCommentReply().getCommentReplyId(),
                            getObjectId(bean),
                            getCommentTypeEnum(bean),
                            true,
                            bean.getComment().getFloor(),
                            true, false,
                            null);
                }
            }
        };

        holder.mainLayout.setOnClickListener(listener);

        holder.replyTv.setOnClickListener(listener);


        if(bean.getSubject()!=null&&bean.getSubject().getSubjectId()!=null){
            holder.resourceTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((TextUtils.equals(bean.getSubject().getObjectType().getName(),"ARTICLE")
                            ||TextUtils.equals(bean.getSubject().getObjectType().getName(),"NORMAL")
                            ||TextUtils.equals(bean.getSubject().getObjectType().getName(),"PERSONAL_ARTICLE")))
                    {SubjectDetailActivity.startSubjectDetailActivity(context,bean.getSubject().getSubjectId());}
                    else{
                        TransmitDetailsActivity.startTransmitDetailsActivity(context,bean.getSubject().getSubjectId());
                    }
                }
            });

        }else{
            holder.resourceTv.setOnClickListener(listener);
        }

    }

    private boolean isDeleted(ReplyMeBean.MessagesBean bean) {

        InfoBean info = bean.getInfo();

        SubjectsBean subject = bean.getSubject();

        if (subject != null && subject.isDeleted()) {
            LibAppUtil.showTip(context, context.getResources().getString(R.string.deleteSubjectMessage));
            return true;
        }

        if (info != null && (!info.isPublished() || info.isDelete())) {
            LibAppUtil.showTip(context, "抱歉，该资讯已删除");
            return true;
        }
        return false;

    }


    /**
     * 判断该评论是否已经关闭评论了。
     *
     * @param position
     * @return
     */
    private boolean isCommentOff(int position) {

        ReplyMeBean.MessagesBean bean = list.get(position);

        InfoBean info = bean.getInfo();

        SubjectsBean subject = bean.getSubject();

        PurchaseSnapshotBean purchaseOrder = bean.getPurchaseOrder();

        if (subject != null && subject.isCommentOff()) {
            return true;
        }

        if (info != null && info.isCommentOff()) {
            return true;
        }

        if (purchaseOrder != null && purchaseOrder.isCommentOff()) {
            return true;
        }

        return false;
    }

    private SpannableStringBuilder getDeleteMessage(TextView tv, String userName, String userId) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        ssb.append("//");

//        ssb.append(getAtSpan("@" + userName, "userId=" + userId));

        ssb.append("@" + userName);

        ssb.append(" ");

        SpannableString ss = new SpannableString(" ");

        ss.setSpan(new ImageSpan(XjqUtils.getImageSpanDrawable(tv, R.drawable.icon_subject_deleted)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb.append(ss);

        ssb.append(" ");

        ssb.append("抱歉,该内容已删除");

        tv.setLinkTextColor(Color.parseColor("#559ce8"));

        tv.setMovementMethod(LinkMovementMethod.getInstance());

//        setTouchListener(tv);

        return ssb;
    }

    /**
     * 该内容是否是默认显示的
     *
     * @return
     */
    private boolean isDefaultShow(String type) {
        switch (CommentTypeEnum.safeValueOf(type)) {
            case CMS_NEWS:
            case SUBJECT:
            case LOTTERY_PROJECT:
            case JCZQ_RACE:
                return false;
            default:
                return true;
        }
    }

    private boolean isDeleted(int position) {
        ReplyMeBean.MessagesBean bean = list.get(position);
        SubjectsBean subject = bean.getSubject();
        InfoBean info = bean.getInfo();
        if (subject != null && subject.isDeleted()) {
            return true;
        }
        if (bean.getComment() != null && (bean.getComment().isSystemDeleted() || bean.getComment().isUserDeleted())) {
            return true;
        }
        if (info != null && (info.isDelete() || !info.isPublished())) {
            return true;
        }
        return false;
    }

    private void setDeleteMessage(TextView tv, String message) {
        tv.setText(message);
    }

    private String getObjectId(ReplyMeBean.MessagesBean bean) {
        if (bean.getSubject() != null) {
            return bean.getSubject().getSubjectId();
        }
        if (bean.getInfo() != null) {
            return String.valueOf(bean.getInfo().getId());
        }
        if (bean.getPurchaseOrder() != null) {
            return bean.getPurchaseOrder().getPurchaseNo();
        }
        if (bean.getJczqRaceData() != null) {
            return bean.getJczqRaceData().getBizId() + ":" + bean.getJczqRaceData().getId();
        }
        return null;
    }

    private String getLouZhuId(ReplyMeBean.MessagesBean bean) {
        if (bean.getSubject() != null) {
            return bean.getSubject().getUserId();
        }
        if (bean.getInfo() != null) {
            return "0";
        }
        if (bean.getPurchaseOrder() != null) {
            return bean.getPurchaseOrder().getUserId();
        }
        return null;
    }

    private CommentTypeEnum getCommentTypeEnum(ReplyMeBean.MessagesBean bean) {
        if (bean.getSubject() != null) {
            return CommentTypeEnum.SUBJECT;
        }
        if (bean.getInfo() != null) {
            return CommentTypeEnum.CMS_NEWS;
        }
        if (bean.getPurchaseOrder() != null) {
            return CommentTypeEnum.LOTTERY_PROJECT;
        }
        if (bean.getJczqRaceData() != null) {
            return CommentTypeEnum.JCZQ_RACE;
        }
        return null;
    }

    private String getCommentType(ReplyMeBean.MessagesBean bean) {
        if (bean.mainType != null) {
            return bean.mainType.getName();
        }
        return "";
    }

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

    protected void showCommentDetail(String louzhuId,
                                     String commentId,
                                     String commentReplyId,
                                     String objectId,
                                     CommentTypeEnum type,
                                     boolean showDetail,
                                     int floor,
                                     boolean showReply,
                                     boolean showKeyboard,
                                     BaseObject baseObject) {

        ReplyDetailActivity.ShowCommentDetailBean bean = getCommentDetailBean(louzhuId,
                commentId,
                commentReplyId,
                objectId,
                type,
                showDetail,
                floor);

        bean.setBaseObject(baseObject);
        bean.setShowKeyboard(showKeyboard);
        if(type==null){
            ToastUtil.show(context,"抱歉，原内容不存在",1000);
            return;
        }
        ReplyDetailActivity.startReplyDetailActivity((Activity) context, bean);
    }


    static class ViewHolder {
        @BindView(R.id.portraitIv)
        CircleImageView portraitIv;
        @BindView(R.id.vipIv)
        ImageView vipIv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.messageTypeTv)
        TextView messageTypeTv;
        @BindView(R.id.replyCountTv)
        TextView replyCountTv;
        @BindView(R.id.replyTv)
        TextView replyTv;
        @BindView(R.id.resourceTv)
        TextView resourceTv;
        @BindView(R.id.contentShowLayout)
        LinearLayout contentShowLayout;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
