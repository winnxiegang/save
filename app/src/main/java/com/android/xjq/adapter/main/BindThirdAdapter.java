package com.android.xjq.adapter.main;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.activity.ThirdWebActivity;
import com.android.xjq.bean.ThirdCertifyBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/7/28.
 */

public class BindThirdAdapter extends MyBaseAdapter<ThirdCertifyBean.ThirdUserMappingConfigsBean> {


    public BindThirdAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_bind_third_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder, position);

        return convertView;
    }

    private void setItemView(ViewHolder holder, int position) {
        final ThirdCertifyBean.ThirdUserMappingConfigsBean bean = list.get(position);

        setImageShow(holder.bindThirdIv, bean.getLogoUrl());

        holder.bindThirdTv.setText(bean.getThirdChannelName());

        if (bean.isHasBind()) {
            holder.hasBindIv.setVisibility(View.VISIBLE);
            holder.bindTv.setVisibility(View.GONE);
            holder.userNameTv.setVisibility(View.VISIBLE);
            holder.userNameTv.setText(bean.getBindUserName());
        } else {
            holder.hasBindIv.setVisibility(View.GONE);
            holder.bindTv.setVisibility(View.VISIBLE);
            holder.userNameTv.setVisibility(View.GONE);
        }

        holder.bindTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThirdWebActivity.startThirdWebActivity((Activity) context, bean.getCertifyUrl());
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.bindThirdIv)
        SimpleDraweeView bindThirdIv;
        @BindView(R.id.bindThirdTv)
        TextView bindThirdTv;
        @BindView(R.id.bindTv)
        TextView bindTv;
        @BindView(R.id.hasBindIv)
        ImageView hasBindIv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;

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
