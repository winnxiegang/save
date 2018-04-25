package com.android.xjq.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.live.ChannelUserConfigBean;
import com.android.xjq.model.BubbleColorEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/9/13.
 */

public class BubblePanelAdapter extends MyBaseAdapter<ChannelUserConfigBean.ChannelBubbleConfigBean> {

    public void setBubbleSelectedListener(BubbleSelectedListener bubbleSelectedListener) {
        this.bubbleSelectedListener = bubbleSelectedListener;
    }

    private BubbleSelectedListener bubbleSelectedListener;

    public BubblePanelAdapter(Context context, List<ChannelUserConfigBean.ChannelBubbleConfigBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        convertView = View.inflate(context, R.layout.item_bubble_panel_gridview, null);

        ViewHolder holder = ViewHolder.getTag(convertView);

        setData(holder, position);
        return convertView;
    }

    private void setData(final ViewHolder holder, final int position) {

        ChannelUserConfigBean.ChannelBubbleConfigBean bean = list.get(position);

        holder.contentIv.setEnabled(bean.isEnable());

        holder.contentIv.setSelected(bean.isSelected());

        if (bean.isSelected()) {
            holder.selectedTagIv.setVisibility(View.VISIBLE);
        } else {
            holder.selectedTagIv.setVisibility(View.GONE);
        }
        if (bean.isEnable()) {
            holder.coverView.setVisibility(View.GONE);
        } else {
            holder.coverView.setVisibility(View.VISIBLE);
        }
        int resId = BubbleColorEnum.safeValueOf(bean.getFontColor()).getTemplateResId();

        holder.contentIv.setImageResource(resId);

        holder.contentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bubbleSelectedListener != null) {
                    bubbleSelectedListener.selectedBubble(position);
                }
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.contentIv)
        ImageView contentIv;
        @BindView(R.id.selectedTagIv)
        ImageView selectedTagIv;
        @BindView(R.id.cover_view)
        View coverView;
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

    public interface BubbleSelectedListener {
        void selectedBubble(int position);
    }
}
