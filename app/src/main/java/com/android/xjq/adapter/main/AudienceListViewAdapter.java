package com.android.xjq.adapter.main;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.live.ChannelUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.xjq.dialog.CurrentAudienceDialog.AUDIENCE_TAB;

/**
 * Created by Admin on 2017/3/7.
 */

public class AudienceListViewAdapter extends MyBaseAdapter<ChannelUserEntity.ChannelUserBean> {

    private int currentTab;

    public AudienceListViewAdapter(Context context, List<ChannelUserEntity.ChannelUserBean> list, int currentTab) {
        super(context, list);
        this.currentTab = currentTab;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_audience_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, i);
        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        ChannelUserEntity.ChannelUserBean bean = list.get(position);
        if (bean == null) return;
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (currentTab == AUDIENCE_TAB) {
            holder.orderNumTv.setVisibility(View.GONE);
        } else {
            holder.orderNumTv.setVisibility(View.VISIBLE);
            holder.orderNumTv.setText(String.valueOf(position + 1));
        }
        ssb.append(getYellowClothes(bean.getUserHorseList()));//只有观众有马甲
        ssb.append(bean.getLoginName());
        holder.descTv.setText(ssb);
        if (bean.getUserLogoUrl() != null) {
            setImageShow(holder.portraitIv, bean.getUserLogoUrl());
        } else {
            holder.portraitIv.setImageResource(R.drawable.user_default_logo);
        }

       /* if (messageParamBean.getLevelImageUrl() != null) {

            holder.medalIv.setVisibility(View.VISIBLE);

            setImageShow(holder.medalIv,messageParamBean.getLevelImageUrl());
        } else {
            holder.medalIv.setVisibility(View.GONE);
        }*/

        setUserMedalShow(bean.getUserMedalList(), holder.userInfoLayout, 1);
    }

    static class ViewHolder {
        @BindView(R.id.orderNumTv)
        TextView orderNumTv;
        @BindView(R.id.portraitIv)
        SimpleDraweeView portraitIv;
        @BindView(R.id.descTv)
        TextView descTv;
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
