package com.android.xjq.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.recharge.FundChannelsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/4/20.
 */

public class RechargeChannelAdapter extends MyBaseAdapter<FundChannelsBean> {

    private int currentPosition;

    private ChannelSelectedListener channelSelectedListener;

    public RechargeChannelAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_recharge_channel, null);
        }

        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == position)return;
                currentPosition = position;
                if (channelSelectedListener!=null) {
                    channelSelectedListener.onSelectedChange(position);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        FundChannelsBean bean = list.get(position);

        setIconShow(holder.channelIv, bean.getLogoUrl());

        holder.channelNameTv.setText(bean.getName());

        holder.channelDescTv.setText(bean.getUserInstruction());

        if (currentPosition == position) {
            holder.selectRoleIv.setImageResource(R.drawable.icon_check_hook);
        } else {
            holder.selectRoleIv.setImageResource(R.drawable.icon_uncheck_hook);
        }
    }

    public interface ChannelSelectedListener{
        void onSelectedChange(int position);
    }

    public void setChannelSelectedListener(ChannelSelectedListener channelSelectedListener) {
        this.channelSelectedListener = channelSelectedListener;
    }

    static class ViewHolder {
        @BindView(R.id.channelIv)
        ImageView channelIv;
        @BindView(R.id.channelNameTv)
        TextView channelNameTv;
        @BindView(R.id.channelDescTv)
        TextView channelDescTv;
        @BindView(R.id.selectRoleIv)
        ImageView selectRoleIv;

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
