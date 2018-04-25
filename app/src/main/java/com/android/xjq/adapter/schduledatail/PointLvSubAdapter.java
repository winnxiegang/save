package com.android.xjq.adapter.schduledatail;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.JczqRankingInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/1/20 14:08.
 */
public class PointLvSubAdapter extends MyBaseAdapter<JczqRankingInfoBean> {


    public PointLvSubAdapter(Context context, List<JczqRankingInfoBean> list) {

        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.analysis_points_list_view_item_header, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(position, holder);

        return convertView;

    }

    private void setItemView(int position, ViewHolder holder) {
        JczqRankingInfoBean bean = list.get(position);
        holder.teamNameTv.setText(bean.getTn() + "");
        holder.matchCountTv.setText(bean.getMc() + "");
        holder.matchWinTv.setText(bean.getMw() + "");
        holder.matchDrawTv.setText(bean.getMd() + "");
        holder.matchPointTv.setText(bean.getMp() + "");
        holder.matchLostTv.setText(bean.getMl() + "");
        holder.inGoalTv.setText(bean.getIg() + "");
        holder.lostGoalTv.setText(bean.getLg() + "");
        holder.realGoaTv.setText(bean.getRg() + "");
        holder.teamRankTv.setText(bean.getTr() + "");
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.teamRankTv.getLayoutParams();
        params.leftMargin = 0;
        holder.teamRankTv.setLayoutParams(params);
        if (!TextUtils.isEmpty(bean.getCl()))
            holder.teamRankTv.setBackgroundColor(Color.parseColor(bean.getCl()));
        else
            holder.teamRankTv.setBackgroundColor(Color.WHITE);

        switch (bean.getTeamType()) {
            case HOME:
                holder.mainLayout.setBackgroundColor(Color.parseColor("#F2AAAA"));
                break;
            case GUEST:
                holder.mainLayout.setBackgroundColor(Color.parseColor("#B7D8FA"));
                break;
            case NORMAL:
                holder.mainLayout.setBackgroundColor(Color.WHITE);
                break;
        }
    }



    static  class ViewHolder {
        @BindView(R.id.teamRankTv)
        TextView teamRankTv;
        @BindView(R.id.teamNameTv)
        TextView teamNameTv;
        @BindView(R.id.matchCountTv)
        TextView matchCountTv;
        @BindView(R.id.matchWinTv)
        TextView matchWinTv;
        @BindView(R.id.matchDrawTv)
        TextView matchDrawTv;
        @BindView(R.id.matchLostTv)
        TextView matchLostTv;
        @BindView(R.id.inGoalTv)
        TextView inGoalTv;
        @BindView(R.id.lostGoalTv)
        TextView lostGoalTv;
        @BindView(R.id.realGoaTv)
        TextView realGoaTv;
        @BindView(R.id.matchPointTv)
        TextView matchPointTv;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        public ViewHolder(View rootView) {
            ButterKnife.bind(this, rootView);
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
