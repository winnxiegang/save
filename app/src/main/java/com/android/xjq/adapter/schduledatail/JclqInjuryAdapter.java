package com.android.xjq.adapter.schduledatail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.JclqInjuryBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zaozao on 2018/2/6.
 * 用处：
 */

public class JclqInjuryAdapter extends MyBaseAdapter<JclqInjuryBean.PlayInjuryBean> {

    public JclqInjuryAdapter(Context context, List<JclqInjuryBean.PlayInjuryBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_jclq_injury, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemView(position, viewHolder);

        return convertView;
    }


    private void setItemView(int position, final ViewHolder viewHolder) {
        final JclqInjuryBean.PlayInjuryBean bean = list.get(position);
        viewHolder.llInjuryTitle.setVisibility(View.GONE);
        viewHolder.teamTitleLayout.setVisibility(View.GONE);
        viewHolder.emptyTv.setVisibility(View.GONE);
        viewHolder.llContent.setVisibility(View.VISIBLE);


        if(bean.showTeamTitle){
            viewHolder.teamTitleLayout.setVisibility(View.VISIBLE);
            viewHolder.llInjuryTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTeamName.setText(bean.getTn());
            Picasso.with(context).load(bean.logoUrl).into(viewHolder.ivTeamIcon);
        }

        if(bean.showEmpty){
            viewHolder.emptyTv.setVisibility(View.VISIBLE);
            viewHolder.llContent.setVisibility(View.GONE);
        }

        viewHolder.tvPlayerName.setText(bean.getPn());
        viewHolder.tvStatus.setText(bean.getStatus());
        viewHolder.tvDescription.setText(bean.getDetail());
        if(bean.isHaveExpand){
            viewHolder.llDescription.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llDescription.setVisibility(View.GONE);
        }

        viewHolder.ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpand(viewHolder,bean);
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.v_decoration)
        View vDecoration;
        @BindView(R.id.iv_team_icon)
        ImageView ivTeamIcon;
        @BindView(R.id.tv_team_name)
        TextView tvTeamName;
        @BindView(R.id.teamTitleLayout)
        LinearLayout teamTitleLayout;
        @BindView(R.id.ll_injury_title)
        LinearLayout llInjuryTitle;
        @BindView(R.id.tv_player_name)
        TextView tvPlayerName;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.iv_expand)
        ImageView ivExpand;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.ll_description)
        LinearLayout llDescription;
        @BindView(R.id.emptyTv)
        TextView emptyTv;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    void setExpand(ViewHolder viewHolder,JclqInjuryBean.PlayInjuryBean bean) {
        if(bean.isHaveExpand){
            bean.isHaveExpand = false;
            viewHolder.ivExpand.setRotationX(0);
            viewHolder.llDescription.setVisibility(View.GONE);
        }else{
            bean.isHaveExpand = true;
            viewHolder.ivExpand.setRotationX(180);
            viewHolder.llDescription.setVisibility(View.VISIBLE);
        }
    }


}
