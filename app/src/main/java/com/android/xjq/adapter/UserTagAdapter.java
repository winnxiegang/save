package com.android.xjq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.xjq.R;
import com.android.xjq.activity.UserTagActivity;
import com.android.xjq.bean.interest.UserTagBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danao on 2018/4/20.
 */
public class UserTagAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder> {

    private UserTagActivity mContext;
    private List<UserTagBean.Tag> mData;

    private List<String> tagIds = new ArrayList<>();

    public UserTagAdapter(UserTagActivity mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == UserTagBean.Tag.NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_tag, parent, false);
            return new NormalViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_tag_divide, parent, false);
            return new DivideViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final UserTagBean.Tag tag = mData.get(position);
        if (getItemViewType(position)== UserTagBean.Tag.NORMAL){
            final NormalViewHolder holder = (NormalViewHolder) viewHolder;
            PicUtils.loadCircle(mContext, holder.mIvSign, tag.getImageUrl());
            if (tagIds.contains(tag.getId())) {
                holder.mIvSelected.setVisibility(View.VISIBLE);
                holder.mViewShadow.setVisibility(View.VISIBLE);
            } else {
                holder.mIvSelected.setVisibility(View.INVISIBLE);
                holder.mViewShadow.setVisibility(View.INVISIBLE);
            }
            holder.mTvName.setText(tag.getTagName() != null ? tag.getTagName() : "");
            holder.mIvSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagIds.contains(tag.getId())) {
                        tagIds.remove(tag.getId());
                        holder.mIvSelected.setVisibility(View.INVISIBLE);
                        holder.mViewShadow.setVisibility(View.INVISIBLE);
                        mContext.removeTag(tag);
                    } else {
                        tagIds.add(tag.getId());
                        holder.mIvSelected.setVisibility(View.VISIBLE);
                        holder.mViewShadow.setVisibility(View.VISIBLE);
                        mContext.addTag(tag);
                    }
                }
            });
        } else {

        }
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    public void setData(List<UserTagBean.Tag> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public UserTagBean.Tag getItem(int position) {
        return mData.get(position);
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_tag_sign)
        ImageView mIvSign;
        @BindView(R.id.view_tag_shadow)
        View mViewShadow;
        @BindView(R.id.iv_tag_selected)
        ImageView mIvSelected;
        @BindView(R.id.tv_tag_name)
        TextView mTvName;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DivideViewHolder extends RecyclerView.ViewHolder {

        public DivideViewHolder(View itemView) {
            super(itemView);
        }
    }
}
