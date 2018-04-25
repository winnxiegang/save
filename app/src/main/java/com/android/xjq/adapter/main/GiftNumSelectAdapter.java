package com.android.xjq.adapter.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.live.main.gift.GiftCountInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/8.
 */

public class GiftNumSelectAdapter extends MyBaseAdapter<GiftCountInfo> {

    public GiftNumSelectAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size() == 0 ? 0 : (list.size() + 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_gift_select_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);
        setItemView(holder, position);
        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        if (position == list.size()) {
            holder.customTv.setVisibility(View.VISIBLE);
            holder.contentView.setVisibility(View.GONE);
            return;
        }
        GiftCountInfo bean = list.get(position);

        if (bean.isSelected()) {
            holder.isSelectedIv.setVisibility(View.VISIBLE);
        } else {
            holder.isSelectedIv.setVisibility(View.GONE);
        }

        if (bean.isAll()) {
            holder.customTv.setVisibility(View.VISIBLE);
            holder.contentView.setVisibility(View.GONE);
            holder.customTv.setText(bean.getDesc());
        }
        holder.giftNumTv.setText(String.valueOf(bean.getNum()));

        if (TextUtils.isEmpty(bean.getDesc())) {
            holder.giftDescTv.setVisibility(View.GONE);
        } else {
            holder.giftDescTv.setVisibility(View.VISIBLE);
            holder.giftDescTv.setText(bean.getDesc());
        }

    }

    static class ViewHolder {
        @BindView(R.id.giftNumTv)
        TextView giftNumTv;
        @BindView(R.id.giftDescTv)
        TextView giftDescTv;
        @BindView(R.id.isSelectedIv)
        ImageView isSelectedIv;
        @BindView(R.id.customTv)
        TextView customTv;
        @BindView(R.id.contentView)
        RelativeLayout contentView;


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
