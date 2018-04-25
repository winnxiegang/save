package com.android.banana.commlib.coupon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.bean.CouponDetailItemBean;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2017/7/26.
 */

public class CouponDetailAdapter extends MyBaseAdapter<CouponDetailItemBean> {

    public CouponDetailAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_coupon_detail, null);
        }

        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, position);

        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        CouponDetailItemBean bean = list.get(position);
        setImageShow(holder.portraitIv, bean.getUserLogoUrl());
        holder.userNameTv.setText(bean.getLoginName());
        holder.amountTv.setText(String.valueOf((int)bean.getAmount()));
        if (bean.isLuckyKing()) {
            holder.luckyKingIv.setVisibility(View.VISIBLE);
        } else {
            holder.luckyKingIv.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        CircleImageView portraitIv;
        TextView userNameTv;
        ImageView luckyKingIv;
        TextView amountTv;

        public ViewHolder(View convertView) {
            portraitIv = (CircleImageView) convertView.findViewById(R.id.portraitIv);
            userNameTv = (TextView) convertView.findViewById(R.id.userNameTv);
            luckyKingIv = (ImageView) convertView.findViewById(R.id.luckyKingIv);
            amountTv = (TextView) convertView.findViewById(R.id.amountTv);
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
