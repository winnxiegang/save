package com.android.xjq.adapter.live;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.view.expandtv.CustomMedalDrawable;
import com.android.library.Utils.LibAppUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/9/28.
 */

public class BottomMedalAdapter extends MyBaseAdapter<UserMedalBean> {


    public BottomMedalAdapter(Context context, List<UserMedalBean> list) {
        super(context, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_bottom_medal_gridview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setData(holder, position);
        return convertView;
    }

    private void setData(ViewHolder holder, int position) {
        UserMedalBean bean = list.get(position);
        holder.medalInfoLayout.setVisibility(View.GONE);
        holder.noWearMedalTv.setVisibility(View.GONE);
        holder.medalTagIv.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.medalTagIv.getLayoutParams();
        if (bean.isAdorned()) {
            lp.topMargin = 0;
            lp.rightMargin = 0;
            holder.medalTagIv.setImageResource(R.drawable.icon_medal_wearing);
        } else if (bean.isNewMedal()) {
            lp.topMargin = LibAppUtil.dip2px(context, 5);
            lp.rightMargin = LibAppUtil.dip2px(context, 5);
            holder.medalTagIv.setImageResource(R.drawable.icon_new);
        } else {
            holder.medalTagIv.setVisibility(View.GONE);
        }

        holder.contentLayout.setSelected(bean.isSelected());

        if (TextUtils.isEmpty(bean.getId())) {
            holder.noWearMedalTv.setVisibility(View.VISIBLE);
        } else {
            holder.medalInfoLayout.setVisibility(View.VISIBLE);
            String labelName = bean.getMedalLabelConfigList().size() > 0 ? bean.getMedalLabelConfigList().get(0).getContent() : "";
            String content = bean.getMedalLabelConfigList().size() > 0 ? labelName : "";
            holder.medalIv.setImageDrawable(new CustomMedalDrawable(content, bean.getResId(), context.getResources()));
            SpannableStringBuilder ssb = new SpannableStringBuilder(
                    SpannableStringHelper.changeTextColor(String.valueOf(bean.getCurrentValue()), Color.parseColor("#f7b80f")));
            ssb.append("/" + (bean.getMaxMedalLevelValue() <= 0 ? "-" : bean.getMaxMedalLevelValue()));
            holder.medalValueTv.setText(ssb);
        }

    }

    static class ViewHolder {
        @BindView(R.id.medalIv)
        ImageView medalIv;
        @BindView(R.id.medalValueTv)
        TextView medalValueTv;
        @BindView(R.id.medalInfoLayout)
        LinearLayout medalInfoLayout;
        @BindView(R.id.noWearMedalTv)
        TextView noWearMedalTv;
        @BindView(R.id.medalTagIv)
        ImageView medalTagIv;
        @BindView(R.id.contentLayout)
        RelativeLayout contentLayout;

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
