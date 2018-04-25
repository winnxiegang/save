package com.android.xjq.adapter.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.view.MedalLayout;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.live.main.SeatInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/9.
 */

public class LiveGuestAdapter extends MyBaseAdapter<SeatInfoBean.ChannelVipUserSimpleInfo> {

    public LiveGuestAdapter(Context context, List<SeatInfoBean.ChannelVipUserSimpleInfo> seatSimpleList) {
        super(context, seatSimpleList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_live_guest_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);
        setItemView(holder, position);
        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        if (position == 0) {
            holder.orderTv.setText("龙椅");
            holder.levelIv.setImageResource(R.drawable.icon_dragon_chair);
            setTopAndBottomMargin(holder, context, 4, 0);
        } else {
            holder.orderTv.setText(String.valueOf(position));
            holder.levelIv.setImageResource(R.drawable.icon_guest_chair);
            setTopAndBottomMargin(holder, context, 0, 1);
        }

        SeatInfoBean.ChannelVipUserSimpleInfo bean = list.get(position);
        if (bean != null && bean.getUserId() != null) {
            if (!TextUtils.isEmpty(bean.getLoginName())) {
                holder.nameTv.setText(bean.getLoginName());
            }
            setImageShow(holder.portraitIv, bean.getUserLogoUrl());
        } else {
            holder.portraitIv.setImageResource(R.drawable.icon_guest_default);
            holder.nameTv.setText("");
        }

        holder.medalLayout.removeAllViews();
        if (bean.userMedalLevelList != null) {
            for (UserMedalLevelBean userMedalLevelBean : bean.userMedalLevelList) {
                holder.medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(context,
                        userMedalLevelBean.medalConfigCode, userMedalLevelBean.currentMedalLevelConfigCode));
            }

        }

    }

    private void setTopAndBottomMargin(ViewHolder holder, Context context, int top, int bottom) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.portraitIv.getLayoutParams();

        params.topMargin = LibAppUtil.dip2px(context, top);

        params.bottomMargin = LibAppUtil.dip2px(context, bottom);

        holder.portraitIv.setLayoutParams(params);
    }

    static class ViewHolder {
        @BindView(R.id.orderTv)
        TextView orderTv;
        @BindView(R.id.levelIv)
        ImageView levelIv;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.portraitIv)
        ImageView portraitIv;
        @BindView(R.id.medalLayout)
        MedalLayout medalLayout;

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
