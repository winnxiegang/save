package com.android.xjq.controller.message;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.utils.Money;
import com.android.xjq.R;
import com.android.xjq.bean.message.SystemNotifyBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kokuma on 2017/11/28.
 */

public class CouponViewController {

    private Context context;

    private MyBaseAdapter mBaseAdapter;

    public CouponViewController(Context context, MyBaseAdapter adapter) {

        this.context = context;

        this.mBaseAdapter = adapter;

    }

    public void setCouponView(CouponViewController.ViewHolder holder, final SystemNotifyBean.NoticesBean bean) {

        holder.couponLayout.setVisibility(View.VISIBLE);

        mBaseAdapter.setIconView(holder.portraitIv, bean.getUserLogoUrl());

        holder.userNameTv.setText(bean.getSenderName());

        holder.timeTv.setText(bean.getGmtCreate());

        holder.moneyTv.setText("￥" + new Money(bean.getMessageContent().getAmount()).toString());

        mBaseAdapter.setVip(holder.vipIv, bean.isVip());

        holder.couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  CouponDetailActivity.startCouponDetailActivity((Activity) context, bean.getMessageContent().getCouponNo(),null,null);
            }
        });

        holder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // HomepageActivity.startHomePageActivity((Activity) context,bean.getSenderId());
            }
        });
    }

    public static class ViewHolder {

        @BindView(R.id.portraitIv)
        CircleImageView portraitIv;

        @BindView(R.id.vipIv)
        ImageView vipIv;

        @BindView(R.id.messageTv)
        TextView messageTv;

        @BindView(R.id.timeTv)
        TextView timeTv;

        @BindView(R.id.moneyTv)
        TextView moneyTv;

        @BindView(R.id.userNameTv)
        TextView userNameTv;

        public LinearLayout couponLayout;

        public ViewHolder(View view) {

            couponLayout = (LinearLayout) view.findViewById(R.id.couponLayout);

            ButterKnife.bind(this, couponLayout);
        }
    }

}
