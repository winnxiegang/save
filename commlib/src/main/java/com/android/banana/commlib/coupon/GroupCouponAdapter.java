package com.android.banana.commlib.coupon;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.couponenum.CouponEnum;
import com.android.banana.commlib.coupon.couponenum.GroupCouponAllocateStatusEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CountdownTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2017/7/25.
 */

public class GroupCouponAdapter extends MyBaseAdapter<GroupCouponInfoBean> {

    private int mPosition;

    public interface CouponItemClickListener {
        void couponValidate(GroupCouponInfoBean groupCouponInfo, int position);
    }

    private CouponItemClickListener couponItemClickListener;

    public void setCouponItemClickListener(CouponItemClickListener couponItemClickListener) {
        this.couponItemClickListener = couponItemClickListener;
    }

    public GroupCouponAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_group_coupon, null);
        }

        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GroupCouponAllocateStatusEnum.ALL_ALLOCATED.name().equals(list.get(position).getStatus().getName()) ||
                        GroupCouponAllocateStatusEnum.UNALLOCATABLE.name().equals(list.get(position).getStatus().getName()) ||
                        list.get(position).isOwnAllocated()) {

                    new CouponDetailDialog(context, list.get(position).getCouponNo()).show();
                } else {
                    if (couponItemClickListener != null) {
                        couponItemClickListener.couponValidate(list.get(position), position);
                        mPosition = position;
                    }

                }
            }
        });

        return convertView;
    }

    private void setItemView(final ViewHolder holder, int position) {
        final GroupCouponInfoBean bean = list.get(position);
        setImageShow(holder.portraitIv, bean.getLogoUrl());
        holder.userNameTv.setText(bean.getLoginName());
        holder.couponTitleTv.setText(bean.getTitle());
        if (CouponEnum.NORMAL_GROUP_COUPON.getMessage().equals(bean.getAmountAllocateType().getName())) {
            holder.couponTypeIv.setImageResource(R.drawable.icon_normal_coupon);
        } else {
            holder.couponTypeIv.setImageResource(R.drawable.icon_lucky_coupon);
        }
        holder.couponStatusIv.setVisibility(View.VISIBLE);
        holder.contentLayout.setAlpha(1f);
        switch (GroupCouponAllocateStatusEnum.safeValueOf(bean.getStatus().getName())) {
            case INIT:
            case WAIT_SPLIT:
            case WAIT_ALLOCATE:
                holder.couponStatusIv.setVisibility(View.GONE);
                break;
            case ALL_ALLOCATED:
                holder.contentLayout.setAlpha(0.4f);
                holder.couponStatusIv.setImageResource(R.drawable.icon_coupon_remain_empty);
                break;
            case UNALLOCATABLE:
                holder.contentLayout.setAlpha(0.4f);
                holder.couponStatusIv.setImageResource(R.drawable.icon_coupon_expired);
                break;
        }
        if (bean.isOwnAllocated()) {
            holder.couponStatusIv.setVisibility(View.VISIBLE);
            holder.couponStatusIv.setImageResource(R.drawable.icon_coupon_has_allocated);
        }
        if (bean.isPublished()) {
            holder.couponTimeTv.setTextColor(Color.parseColor("#999999"));
            holder.couponTimeTv.setText(bean.getGmtPay());
            holder.couponTimeTv.cancel();
        } else {
            holder.couponTimeTv.setTextColor(Color.parseColor("#fa483c"));
            holder.couponTimeTv.setText(bean.getGmtPay());
            String gmtTimed = bean.getGmtTimed();
            long timeDiff = TimeUtils.parseTime(gmtTimed, TimeUtils.LONG_DATEFORMAT) - System.currentTimeMillis();
            holder.couponTimeTv.start(timeDiff);
        }

        holder.couponTimeTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                holder.couponTimeTv.setText("距开抢还有" + TimeUtils.timeFormat(countdownTime));
            }

            @Override
            public void countdownEnd() {
                holder.couponTimeTv.setText("刚刚");
            }
        });

    }

    static class ViewHolder {
        CircleImageView portraitIv;
        TextView userNameTv;
        TextView couponTitleTv;
        ImageView couponTypeIv;
        CountdownTextView couponTimeTv;
        ImageView couponStatusIv;
        RelativeLayout contentLayout;

        public ViewHolder(View convertView) {
            portraitIv = (CircleImageView) convertView.findViewById(R.id.portraitIv);
            userNameTv = (TextView) convertView.findViewById(R.id.userNameTv);
            couponTitleTv = (TextView) convertView.findViewById(R.id.couponTitleTv);
            couponTypeIv = (ImageView) convertView.findViewById(R.id.couponTypeIv);
            couponTimeTv = (CountdownTextView) convertView.findViewById(R.id.couponTimeTv);
            couponStatusIv = (ImageView) convertView.findViewById(R.id.couponStatusIv);
            contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
        }

        public static ViewHolder getTag(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
            }
            return holder;
        }
    }

    public void updateItemData(GroupCouponInfoBean groupCouponInfo) {
        for (int i = 0; i < list.size(); i++) {

            list.set(mPosition, groupCouponInfo);
        }

        notifyDataSetChanged();
    }

}
