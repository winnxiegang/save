package com.android.banana.commlib.bet;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.BetGiftEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lingjiu on 2017/11/6.
 */

public class BetAdapter extends RecyclerView.Adapter<BetAdapter.ViewHolder> {
    private List<BetGiftEntity.BetGiftBean> mList;
    private Context mContext;

    public BetAdapter(Context context, List<BetGiftEntity.BetGiftBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_bet_gift_recyclerview, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BetGiftEntity.BetGiftBean bean = mList.get(position);
        setIconView(holder.giftIv, bean.getGiftImageUrl());
        holder.goldTv.setText("" + (int)bean.getPrice());

        if (bean.isChecked()) {
            holder.contentLayout.setBackgroundResource(R.drawable.shape_white_stroke_rect);
        } else {
            holder.contentLayout.setBackgroundResource(0);
        }

        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BetGiftEntity.BetGiftBean betGiftBean : mList) {
                    betGiftBean.setChecked(false);
                }
                mList.get(position).setChecked(true);
                notifyDataSetChanged();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListener(v.findViewById(R.id.giftIv),position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView giftIv;
        private TextView goldTv;
        private LinearLayout contentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            giftIv = ((ImageView) itemView.findViewById(R.id.giftIv));
            goldTv = (TextView) itemView.findViewById(R.id.goldTv);
            contentLayout = (LinearLayout) itemView.findViewById(R.id.contentLayout);
        }
    }


    public void setIconView(ImageView iv, String url) {
        if (url == null) {
            return;
        }
        Picasso.with(mContext.getApplicationContext())
                .load(Uri.parse(url))
                .into(iv);
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    private MyItemClickListener mItemClickListener;

    public interface MyItemClickListener {
        void onItemClickListener(View view,int position);
    }
}
