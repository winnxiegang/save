package com.android.xjq.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.view.CustomProgressView;
import com.android.xjq.view.expandtv.CustomMedalDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajiao on 2017/9/25 0025.
 */

public class MedalAdapter extends MyBaseAdapter<UserMedalBean> {
    private IonOperateMedalClick mOperateMedalListener;

    public MedalAdapter(Context context, List<UserMedalBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_medal, null);
        }

        ViewHolder holder = ViewHolder.getTag(convertView);
        setData(holder, i);
        return convertView;
    }

    private void setData(ViewHolder holder, final int position) {
        final UserMedalBean medalBean = list.get(position);
        String medalName = medalBean.getMedalLabelConfigList().size() > 0 ? medalBean.getMedalLabelConfigList().get(0).getContent() : "";
        CustomMedalDrawable customMedalDrawable = new CustomMedalDrawable(medalName, medalBean.getResId(), context.getResources());
        holder.medalImg.setImageDrawable(customMedalDrawable);
        holder.medalName.setText(medalBean.getHostName());
        holder.progressBar.setProgressValue(medalBean.getCurrentValue(), medalBean.getMaxMedalLevelValue(), true);
        if (medalBean.getStatusResId() > 0) {
            holder.actionTypeImg.setImageResource(medalBean.getStatusResId());
            holder.actionTypeImg.setVisibility(View.VISIBLE);
        } else {
            holder.actionTypeImg.setVisibility(View.INVISIBLE);
        }

        if (medalBean.isAdorned()) {
            holder.medalStatusTxt.setVisibility(View.VISIBLE);
            holder.operateMedalTxt.setTextColor(context.getResources().getColor(R.color.stroke_gray));
            holder.operateMedalTxt.setBackgroundResource(R.drawable.shape_operate_medal_n);
            holder.operateMedalTxt.setText("取消");
        } else {
            holder.medalStatusTxt.setVisibility(View.GONE);
            holder.operateMedalTxt.setTextColor(context.getResources().getColor(R.color.red));
            holder.operateMedalTxt.setBackgroundResource(R.drawable.shape_operate_medal_h);
            holder.operateMedalTxt.setText("佩戴");
        }

        holder.operateMedalTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOperateMedalListener != null) {
                    mOperateMedalListener.onOperateMedal(medalBean.isAdorned(), medalBean.getId());
                }
            }
        });

    }


    public CustomMedalDrawable getCustomMedalDrawable(int pos) {
        String content = list.get(pos).getMedalLabelConfigList().size() > 0 ? list.get(pos).getMedalLabelConfigList().get(0).getContent() : "";
        CustomMedalDrawable customMedalDrawable = new CustomMedalDrawable(content,
                list.get(pos).getResId(), context.getResources());
        return customMedalDrawable;
    }

    static class ViewHolder {
        @BindView(R.id.medal_img)
        ImageView medalImg;
        @BindView(R.id.medal_status_txt)
        TextView medalStatusTxt;
        @BindView(R.id.medal_name)
        TextView medalName;
        @BindView(R.id.progress_bar)
        CustomProgressView progressBar;
        @BindView(R.id.operate_medal_txt)
        TextView operateMedalTxt;
        @BindView(R.id.action_type_img)
        ImageView actionTypeImg;

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

    public interface IonOperateMedalClick {
        public void onOperateMedal(boolean b, String userMedalDetailId);
    }

    public void setOnOperateMedalClick(IonOperateMedalClick listener) {
        this.mOperateMedalListener = listener;
    }


}
