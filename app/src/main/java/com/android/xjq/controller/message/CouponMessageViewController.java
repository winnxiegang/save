package com.android.xjq.controller.message;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.coupon.couponenum.CouponTypeEnum;
import com.android.xjq.R;
import com.android.xjq.bean.message.SystemNotifyBean;
import com.android.xjq.model.message.SystemMessageSubTypeEnum;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kokuma on 2017/11/28.
 */

public class CouponMessageViewController {
    private Context context;

    public CouponMessageViewController(Context context) {

        this.context = context;

    }

    public void setDefaultShow(ViewHolder holder, final SystemNotifyBean.NoticesBean bean){

        holder.timeTv.setText(bean.getGmtCreate());

        holder.couponNotifyLayout.setVisibility(View.VISIBLE);

        String message = "";

        switch (SystemMessageSubTypeEnum.saveValueOf(bean.getMsgSubType().getName())) {

            case FOLLOW_USER_CAN_FETCH:
                message = bean.getSenderName()+" 发了红包,快去抢吧！点此查看";
                break;
            case COPY_PROJECT_CAN_FETCH:
                message = "您复制的方案有红包可以抢哦~点此查看";
                break;
            case USER_GUESS_PRIZE_NOTICE:
                message = "您参与的有奖竞猜已派奖,点此查看";
                break;

        }

        SpannableString ss = new SpannableString(message);

        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue_look)), ss.length()-4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.warnMessageTv.setText(ss);

        holder.couponNotifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (CouponTypeEnum.safeValueOf(bean.getMessageContent().getObjectType())) {
//                    case LOTTERY_PROJECT:
//                        OrderDetailsActivity.startOrderDetailsActivity((Activity) context,bean.getMessageContent().getObjectId());
//                        break;
//                    case SUBJECT:
//                        SubjectDetailActivity.startSubjectDetailActivity((Activity) context,bean.getMessageContent().getObjectId());
//                        break;
//                    case USER_GUESS_PRIZE_NOTICE:
//                        CouponDetailActivity.startCouponDetailActivity((Activity) context,bean.getMessageContent().getCouponNo(),null,null);
//                        break;
                }
            }
        });

    }

    public static class ViewHolder {

        @BindView(R.id.timeTv)
        TextView timeTv;

        @BindView(R.id.messageTv)
        TextView warnMessageTv;

        public LinearLayout couponNotifyLayout;

        public ViewHolder(View view) {

            couponNotifyLayout = (LinearLayout) view.findViewById(R.id.couponNotifyLayout);

            ButterKnife.bind(this, couponNotifyLayout);

        }
    }
}
