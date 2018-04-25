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

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.SubjectDetailActivity;
import com.android.xjq.bean.message.SystemNotifyBean;
import com.android.xjq.model.message.SystemMessageSubTypeEnum;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kokuma on 2017/11/28.
 */

public class SystemMessageViewController {
    private Context context;

    public SystemMessageViewController(Context context) {

        this.context = context;

    }

    public void setSystemMessageShow(ViewHolder holder, final SystemNotifyBean.NoticesBean bean) {

        holder.timeTv.setText(bean.getGmtCreate());

        holder.systemMessageShowLayout.setVisibility(View.VISIBLE);

        final SystemNotifyBean.MessageParameterBean messageContent = bean.getMessageContent();

        String message = "";

        switch (SystemMessageSubTypeEnum.saveValueOf(bean.getMsgSubType().getName())) {
            case LT_FOLLOWER_CANCEL:
                message = "由于" + messageContent.getSourceUserName() + "撤单,您的方案认证失败";
                holder.systemMessageTv.setText(message);
                break;
            case LT_FOLLOWER_FAIL_SOLD:
                message = "由于" + messageContent.getSourceUserName() + "逾期未上传方案,您的方案认证失败。";
                holder.systemMessageTv.setText(message);
                break;
            case LT_PURCHASE_NEAR_STOP_SELL:
                message = "您的方案临近最晚上传时间,请上传方案!";
                holder.systemMessageTv.setText(message);
                break;
            case ARTICLE_SELECTED_NOTICE_USER:
                message = "您的《"+messageContent.getObjectContent()+"》被入选，稿费将于24小时内到账";
                setSpan(message,2,message.length()-15,holder.systemMessageTv);
                break;
            case ARTICLE_SET_ETITLE_NOTICE_USER:
                message = "您的《"+messageContent.getObjectContent()+"》被加精";
                setSpan(message,2,message.length()-3,holder.systemMessageTv);
                break;
            case ARTICLE_SET_TOP_NOTICE_USER:
                message = "您的《"+messageContent.getObjectContent()+"》被置顶";
                setSpan(message,2,message.length()-3,holder.systemMessageTv);
                break;
            case CHANNEL_AREA_LIVE_NOTICE:
//                message = "您预约的节目"+ "<font color='#2f81fa'>"+"《"+messageContent.getCHANNEL_AREA_NAME()+"》"+ "</font>" +"开播了，快去看看吧";
//                holder.systemMessageTv.setText(Html.fromHtml(message));
                message = "您预约的节目《"+messageContent.getCHANNEL_AREA_NAME()+"》开播了，快去看看吧";
                setSpan(message,6,message.length()-9,holder.systemMessageTv);
                break;
        }


        holder.ivGo.setVisibility(View.VISIBLE);
        View parent = (View) holder.systemMessageTv.getParent();
        parent.setBackgroundColor(Color.parseColor("#f2f2f2"));

        holder.systemMessageShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageContent!=null){
                    if(messageContent.getCHANNEL_AREA_ID()!=null){
                        //去直播间
                        LiveActivity.startLiveActivity((BaseActivity)context,Integer.parseInt(messageContent.getCHANNEL_AREA_ID()));
                    }else{
                        SubjectDetailActivity.startSubjectDetailActivity((Activity) context, messageContent.getSUBJECT());
                    }

                }
            }
        });

    }
    private void setSpan(String str,int start,int end,TextView tv){
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#2f81fa")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }


    public static class ViewHolder {

        @BindView(R.id.timeTv)
        TextView timeTv;

        @BindView(R.id.systemMessageTv)
        TextView systemMessageTv;

        @BindView(R.id.ivGo)
        ImageView ivGo;

        public LinearLayout systemMessageShowLayout;

        public ViewHolder(View view) {

            systemMessageShowLayout = (LinearLayout) view.findViewById(R.id.systemMessageShowLayout);

            ButterKnife.bind(this, systemMessageShowLayout);

        }
    }
}
