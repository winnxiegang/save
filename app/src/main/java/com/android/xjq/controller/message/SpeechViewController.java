package com.android.xjq.controller.message;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.activity.SpeechRuleDescriptionActivity;
import com.android.xjq.bean.message.SystemNotifyBean;
import com.android.xjq.model.message.SystemMessageSubTypeEnum;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kokuma on 2017/11/28.
 */

public class SpeechViewController {
    private Context context;

    public SpeechViewController(Context context) {

        this.context = context;

    }

    public void setSpeechViewShow(ViewHolder holder, final SystemNotifyBean.NoticesBean bean) {

        holder.timeTv.setText(bean.getGmtCreate());

        holder.speechMessageNotify.setVisibility(View.VISIBLE);

        String message = "";

      final   SystemNotifyBean.MessageParameterBean messageContent = bean.getMessageContent();

        int toActivity=0;

        switch (SystemMessageSubTypeEnum.saveValueOf(bean.getMsgSubType().getName())) {

            case SUBJECT_DELETE_NOTICE_TO_USER:
                message = "您的文章《" + messageContent.getObjectContent() + "》因" + messageContent.getDeleteReason() + "已被删除,请查看《言论规范协议》";
                setSpan(message,message.length()-8,message.length(),holder.warnMessageTv);
                break;
            case COMMENT_DELETE_NOTICE_TO_USER:
                message = "您的评论《" + messageContent.getObjectContent() + "》因" + messageContent.getDeleteReason() + "已被删除,请查看《言论规范协议》" ;
               setSpan(message,message.length()-8,message.length(),holder.warnMessageTv);
                break;
            case ARTICLE_SELECTED_UNDO_NOTICE_USER:
                toActivity = 1;
                message = "您的《" + messageContent.getObjectContent() + "》因文章部分内容存在抄袭嫌疑" + "被撤销入选,稿费不予发放";
                holder.warnMessageTv.setText(message);
                break;
            case USER_FORBIDDEN_ACTION_NOTICE:
                message = "您由于" + messageContent.getForbiddenReason() + "已被禁止" + messageContent.getActionType() + ",解封时间" + messageContent.getExpiredDate() + ",请查看《言论规范协议》";
                holder.warnMessageTv.setText(message);
                break;
            case USER_INFO_AUDIT_APPLY_NOTICE_TO_USER:
                toActivity = 2;
                String status = messageContent.getApplyStatus();
                if (status != null && status.equals("AUDIT_PASS")) {
                    message = "您提交的写手认证信息已通过";
                } else if (status != null && status.equals("AUDIT_REJECT")) {
                    message = "抱歉，您提交的写手认证信息未通过，原因是" + messageContent.getMemo();
                }
                holder.warnMessageTv.setText(message);
                break;

        }


        final int toActivityF = toActivity;
        holder.speechMessageNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toActivityF==0) {
                    SpeechRuleDescriptionActivity.startSpeechRuleDescriptionActivity((Activity) context);
                }else  if (toActivityF==1) {
//                    if(messageContent!=null){
//                        SubjectDetailActivity.startSubjectDetailActivity((Activity) context, messageContent.getSUBJECT());
//                    }
                }
            }
        });
        holder.ivGo.setVisibility(View.INVISIBLE);
        View parent = (View) holder.warnMessageTv.getParent();
        parent.setBackgroundColor(Color.WHITE);
    }
    private void setSpan(String str,int start,int end,TextView tv){
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#2f81fa")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

    public static class ViewHolder {

        @BindView(R.id.timeTv)
        TextView timeTv;

        @BindView(R.id.messageTv)
        TextView warnMessageTv;
        @BindView(R.id.ivGo)
        ImageView ivGo;
        public LinearLayout speechMessageNotify;

        public ViewHolder(View view) {

            speechMessageNotify = (LinearLayout) view.findViewById(R.id.speechMessageNotify);

            ButterKnife.bind(this, speechMessageNotify);

        }
    }
}
