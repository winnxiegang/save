package com.android.xjq.adapter.dynamic;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.bean.dynamic.ImpressionDataBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zaozao on 2018/1/29.
 * 用处：
 */

public class ImpressionShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImpressionDataBean.ImpressionTagSimple> list;

    private OnItemClickListener onItemClickListener;
    private View loadMoreView = null;
    private boolean isLoadMoreLayoutEnable = true;
    private static int TYPE_LOAD_MORE = 3000000;
    private static int NORMAL = 0000000;

    public void setLoadMoreLayoutEnable(boolean loadMoreLayoutEnable) {
        isLoadMoreLayoutEnable = loadMoreLayoutEnable;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImpressionShowAdapter(List<ImpressionDataBean.ImpressionTagSimple> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (isLoadMoreLayoutEnable && viewType == TYPE_LOAD_MORE) {
            if (loadMoreView == null) {
                loadMoreView = LayoutInflater.from(XjqApplication.getContext()).inflate(R.layout.impression_load_more_view, parent, false);
            }
            FooterViewHolder holder = new FooterViewHolder(loadMoreView);
            return holder;
        }else{
            View itemView = LayoutInflater.from(XjqApplication.getContext()).inflate(R.layout.item_impression_show, parent, false);
            ContentViewHolder holder = new ContentViewHolder(itemView);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position<list.size()) {
            final ImpressionDataBean.ImpressionTagSimple impressionTagSimple = list.get(position);
            ((ContentViewHolder)holder).impressTv.setText(impressionTagSimple.tagName + (impressionTagSimple.likeCount>0?"    "+impressionTagSimple.likeCount:""));
            if (impressionTagSimple.liked) {//点击过了
                ((ContentViewHolder)holder).impressTv.setTextColor(XjqApplication.getContext().getResources().getColor(R.color.redImpression));
                ((ContentViewHolder)holder).impressTv.setBackgroundDrawable(XjqApplication.getContext()
                        .getResources().getDrawable(R.drawable.shape_oval_all_red_bg));
            }
            else if(TextUtils.equals(impressionTagSimple.createType,"USER")||impressionTagSimple.likeCount>0){
                ((ContentViewHolder)holder).impressTv.setTextColor(XjqApplication.getContext().getResources().getColor(R.color.redImpression));
                ((ContentViewHolder)holder).impressTv.setBackgroundDrawable(XjqApplication.getContext()
                        .getResources().getDrawable(R.drawable.shape_oval_red_stoke_white_solod));
            }
            else {
                ((ContentViewHolder)holder).impressTv.setTextColor(XjqApplication.getContext().getResources().getColor(R.color.gray));
                ((ContentViewHolder)holder).impressTv.setBackgroundDrawable(XjqApplication.getContext().getResources()
                        .getDrawable(R.drawable.shape_oval_gray_stoke_white_solod));
            }

            ((ContentViewHolder)holder).impressTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (impressionTagSimple.liked) {
                        return;
                    }
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if(list.size()>0&& isLoadMoreLayoutEnable){
            return  list.size()+1;
        }

        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ((isLoadMoreLayoutEnable)&& list.size() > 0 && position == list.size() ) {
            return TYPE_LOAD_MORE;
        }
        return NORMAL;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.impressTv)
        TextView impressTv;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.loadMorePb)
        ProgressBar loadMorePb;
        @BindView(R.id.footView)
        LinearLayout footView;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
