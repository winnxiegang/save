package com.android.xjq.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.live.main.RankedMedalInfoBean;
import com.android.xjq.view.expandtv.CustomMedalDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajiao on 2017/10/10 0010.
 */

public class LiveMedalAdapter extends MyBaseAdapter<RankedMedalInfoBean.RankMedalTotalSimpleListBean> {

    public LiveMedalAdapter(Context context, List<RankedMedalInfoBean.RankMedalTotalSimpleListBean> list) {
        super(context, list);
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item_live_medal, null);
        }
        setItemView(ViewHolder.getTag(convertView), i);
        return convertView;
    }

    private void setItemView(ViewHolder holder, int i) {
        RankedMedalInfoBean.RankMedalTotalSimpleListBean rankedMedalBean = list.get(i);
        holder.levelTxt.setText(String.valueOf(i+1));
        switch (i) {
            case 0:
                holder.levelTxt.setBackgroundResource(R.drawable.shape_red_radiu);
                break;
            case 1:
                holder.levelTxt.setBackgroundResource(R.drawable.shape_deep_gold_radius);
                break;
            case 2:
                holder.levelTxt.setBackgroundResource(R.drawable.shape_gold_radius);
                break;
            default:
                holder.levelTxt.setBackgroundResource(R.drawable.shape_gray_radius);
                break;
        }
        String medalName = rankedMedalBean.getMedalLabelName();
        CustomMedalDrawable customMedalDrawable = new CustomMedalDrawable(medalName, rankedMedalBean.getLevelResId(), context.getResources());
        holder.levelImg.setImageDrawable(customMedalDrawable);
        holder.contentTxt.setText(rankedMedalBean.getName());
        holder.numTxt.setText(rankedMedalBean.getCurrentValue() + "");
        if ("DOWN".equals(rankedMedalBean.getSequenceTrend())) {
            holder.levelStatusImg.setImageResource(R.drawable.medal_level_decrease);
        } else if ("UP".equals(rankedMedalBean.getSequenceTrend())) {
            holder.levelStatusImg.setImageResource(R.drawable.medal_level_increase);
        } else {
            holder.levelStatusImg.setImageResource(R.drawable.medal_level_keep);
        }

    }


    public static class ViewHolder {
        @BindView(R.id.level_text)
        TextView levelTxt;
        @BindView(R.id.content_txt)
        TextView contentTxt;
        @BindView(R.id.medal_value_txt)
        TextView numTxt;
        @BindView(R.id.level_status_img)
        ImageView levelStatusImg;
        @BindView(R.id.level_img)
        ImageView levelImg;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
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
