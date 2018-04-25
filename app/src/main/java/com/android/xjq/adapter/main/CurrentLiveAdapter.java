package com.android.xjq.adapter.main;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/7.
 */

public class CurrentLiveAdapter extends MyBaseAdapter<ChannelListEntity> {

    public CurrentLiveAdapter(Context context, List<ChannelListEntity> channelList) {
        super(context,channelList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_current_live_listview, null);
        }

        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, position);

        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        ChannelListEntity bean = list.get(position);

        setImageShow(holder.portraitIv,bean.getLogoUrl());

        holder.idTv.setText("ID:  "+bean.getId());

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        ssb.append(bean.getChannelName());

        if (bean.isAnchorPushStream()) {

            addIcon(ssb,R.drawable.icon_is_living);
        }
        holder.descTv.setText(ssb);
    }

    static class ViewHolder {
        @BindView(R.id.portraitIv)
        SimpleDraweeView portraitIv;
        @BindView(R.id.descTv)
        TextView descTv;
        @BindView(R.id.idTv)
        TextView idTv;

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
