package com.android.xjq.adapter.main;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.live.main.SearchResultBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2017/3/6.
 */

public class SearchAdapter extends MyBaseAdapter<SearchResultBean.ChannelInfoSimpleBean> {


    public SearchAdapter(Context context, List<SearchResultBean.ChannelInfoSimpleBean> channelInfoList) {
        super(context, channelInfoList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_search_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemData(holder, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getUserId() == null) {

                    LiveActivity.startLiveActivity((Activity) context, list.get(position).getId());
                }
            }
        });

        return convertView;
    }

    private void setItemData(ViewHolder holder, int position) {
        SearchResultBean.ChannelInfoSimpleBean bean = list.get(position);

        if (bean.isShowTitle()) {
            if (bean.getUserId() != null) {
                holder.typeTv.setText("主播");
            } else {
                holder.typeTv.setText("频道");
            }
            holder.typeTv.setVisibility(View.VISIBLE);
        } else {
            holder.typeTv.setVisibility(View.GONE);
        }

        setUserMedalShow(bean.getUserMedalList(), holder.userInfoLayout, 1);

        if (bean.getUserId() != null) {
            holder.nameTv.setText(bean.getUserName());

            holder.idTv.setText("ID:" + bean.getAnchorId() + "");

            holder.cyclePortraitIv.setVisibility(View.VISIBLE);

            holder.rectPortraitIv.setVisibility(View.GONE);

            setImageShow(holder.cyclePortraitIv, bean.getUserLogoUrl());

            holder.peopleNumLayout.setVisibility(View.GONE);

        } else {
            SpannableStringBuilder ssb = new SpannableStringBuilder();

            ssb.append(bean.getChannelName());

            if (bean.isAnchorPushStream()) {

                addIcon(ssb, R.drawable.icon_is_living);
            }

            holder.nameTv.setText(ssb);

            holder.idTv.setText("ID:" + bean.getId());

            holder.cyclePortraitIv.setVisibility(View.GONE);

            holder.rectPortraitIv.setVisibility(View.VISIBLE);

            setImageShow(holder.rectPortraitIv, bean.getLogoUrl(), false);

            holder.peopleNumLayout.setVisibility(View.VISIBLE);

            holder.peopleNumTv.setText(String.valueOf(bean.getInChannelUsers()));
        }
    }

    static class ViewHolder {
        @BindView(R.id.typeTv)
        TextView typeTv;
        @BindView(R.id.rectPortraitIv)
        SimpleDraweeView rectPortraitIv;
        @BindView(R.id.cyclePortraitIv)
        SimpleDraweeView cyclePortraitIv;
        @BindView(R.id.portraitLayout)
        FrameLayout portraitLayout;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.idTv)
        TextView idTv;
        @BindView(R.id.peopleNumLayout)
        LinearLayout peopleNumLayout;
        @BindView(R.id.peopleNumTv)
        TextView peopleNumTv;
        @BindView(R.id.userInfoLayout)
        LinearLayout userInfoLayout;

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
