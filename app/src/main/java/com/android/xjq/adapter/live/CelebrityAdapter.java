package com.android.xjq.adapter.live;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.adapter.main.MyBaseAdapter;
import com.android.xjq.bean.myzhuwei.OrderCheerListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2018/2/7.
 */

public class CelebrityAdapter extends MyBaseAdapter<OrderCheerListEntity.CheerInfoBean> {
    private List<OrderCheerListEntity.CheerInfoBean> homeWinlist;
    private List<OrderCheerListEntity.CheerInfoBean> guestWinlist;

    public CelebrityAdapter(Context context, List<OrderCheerListEntity.CheerInfoBean> homeWinlist,
                            List<OrderCheerListEntity.CheerInfoBean> guestWinlist) {
        super(context);
        this.homeWinlist = homeWinlist;
        this.guestWinlist = guestWinlist;
    }

    @Override
    public int getCount() {
        int homeCount = homeWinlist == null ? 0 : homeWinlist.size();
        int guestCount = guestWinlist == null ? 0 : guestWinlist.size();
        return homeCount > guestCount ? homeCount : guestCount;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_celebrity_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, i);
        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        OrderCheerListEntity.CheerInfoBean homeInfoBean = homeWinlist.size() > position ? homeWinlist.get(position) : null;
        OrderCheerListEntity.CheerInfoBean guestInfoBean = guestWinlist.size() > position ? guestWinlist.get(position) : null;
        holder.leftInfoLayout.setVisibility(homeInfoBean == null ? View.GONE : View.VISIBLE);
        holder.rightInfoLayout.setVisibility(guestInfoBean == null ? View.GONE : View.VISIBLE);

        if (homeInfoBean != null) {
            holder.leftOrderTv.setText(String.valueOf(position + 1));
            holder.leftNameTv.setText(homeInfoBean.getUserAlias());
            setImageShow(holder.leftPortraitIv, homeInfoBean.getUserLogoUrl());
            holder.leftGiftNumTv.setText("送出" + homeInfoBean.getMultiple());
            changePortraitBackground(holder, position, true);
        }

        if (guestInfoBean != null) {
            holder.rightOrderTv.setText(String.valueOf(position + 1));
            holder.rightNameTv.setText(guestInfoBean.getUserAlias());
            setImageShow(holder.rightPortraitIv, guestInfoBean.getUserLogoUrl());
            holder.rightGiftNumTv.setText("送出" + guestInfoBean.getMultiple());
            changePortraitBackground(holder, position, false);
        }
    }

    private void changePortraitBackground(ViewHolder holder, int position, boolean isLeft) {
        int resid = 0;
        if (position == 0) {
            resid = isLeft ? R.drawable.icon_zhuwei_first_left_bg : R.drawable.icon_zhuwei_first_right_bg;
        } else if (position == 1) {
            resid = isLeft ? R.drawable.icon_zhuwei_second_left_bg : R.drawable.icon_zhuwei_second_right_bg;
        } else if (position == 2) {
            resid = isLeft ? R.drawable.icon_zhuwei_third_left_bg : R.drawable.icon_zhuwei_third_right_bg;
        }
        if (isLeft) {
            holder.leftPortraitBgIv.setBackgroundResource(resid);
        } else {
            holder.rightPortraitBgIv.setBackgroundResource(resid);
        }

        if (isLeft) {
            holder.leftNameTv.setTextColor(position <= 2 ? ContextCompat.getColor(context, R.color.light_red5) :
                    ContextCompat.getColor(context, R.color.colorTextG3));
            holder.leftGiftNumTv.setTextColor(position <= 2 ? ContextCompat.getColor(context, R.color.light_red6) :
                    ContextCompat.getColor(context, R.color.colorTextG2));
        } else {
            holder.rightNameTv.setTextColor(position <= 2 ? ContextCompat.getColor(context, R.color.light_blue1) :
                    ContextCompat.getColor(context, R.color.colorTextG3));
            holder.rightGiftNumTv.setTextColor(position <= 2 ? ContextCompat.getColor(context, R.color.light_blue2) :
                    ContextCompat.getColor(context, R.color.colorTextG2));
        }


    }

    static class ViewHolder {
        @BindView(R.id.leftOrderTv)
        TextView leftOrderTv;
        @BindView(R.id.leftPortraitBgIv)
        ImageView leftPortraitBgIv;
        @BindView(R.id.leftPortraitIv)
        CircleImageView leftPortraitIv;
        @BindView(R.id.leftNameTv)
        TextView leftNameTv;
        @BindView(R.id.leftGiftNumTv)
        TextView leftGiftNumTv;
        @BindView(R.id.rightNameTv)
        TextView rightNameTv;
        @BindView(R.id.rightGiftNumTv)
        TextView rightGiftNumTv;
        @BindView(R.id.rightPortraitBgIv)
        ImageView rightPortraitBgIv;
        @BindView(R.id.rightPortraitIv)
        CircleImageView rightPortraitIv;
        @BindView(R.id.rightOrderTv)
        TextView rightOrderTv;
        @BindView(R.id.rightInfoLayout)
        LinearLayout rightInfoLayout;
        @BindView(R.id.leftInfoLayout)
        LinearLayout leftInfoLayout;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

        public static ViewHolder getTag(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
            }
            return holder;
        }
    }
}
