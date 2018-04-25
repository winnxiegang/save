package com.android.xjq.adapter.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.utils.Money;
import com.android.xjq.R;
import com.android.xjq.bean.live.main.gift.GiftInfoBean;
import com.android.xjq.model.gift.PayType;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/8.
 */

public class GiftAdapter extends MyBaseAdapter<GiftInfoBean> {

    private int checkPosition;

    private int prePosition = -1;

    private PayType currentPayType = PayType.GOLD_COIN;

    public void setCheckPosition(int checkPosition, int prePosition) {
        this.checkPosition = checkPosition;
        this.prePosition = prePosition;
    }

    public void setCurrentPayType(PayType currentPayType) {
        this.currentPayType = currentPayType;
    }

    public GiftAdapter(Context context, List<GiftInfoBean> list) {
        super(context, list);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_gift_gridview, null);
        }
        final ViewHolder holder = ViewHolder.getTag(convertView);
        setItemView(holder, position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });
        return convertView;
    }


    public void itemUpdate(GridView gridView, int id) {
        if (gridView != null) {
            int start = gridView.getFirstVisiblePosition();
            for (int i = start, j = gridView.getLastVisiblePosition(); i <= j; i++)
                if (id == ((GiftInfoBean) gridView.getItemAtPosition(i)).getId()) {
                    View view = gridView.getChildAt(i - start);
                    checkPosition = i;
                    if (0 <= prePosition && prePosition <= getCount() - 1) {
                        getView(prePosition, gridView.getChildAt(prePosition - start), gridView);
                    }
                    prePosition = i;
                    getView(i, view, gridView);
                    break;
                }
        }
    }


    private void setItemView(ViewHolder holder, int position) {
        if (list == null || list.size() == 0) return;
        GiftInfoBean bean = list.get(position);
        if (checkPosition == position) {
            setImageShow(holder.dynamicGiftIv, bean.getDynamicUrl(), true);
            holder.contentLayout.setBackgroundResource(R.drawable.shape_gift_light_red_bg);
        } else {
            holder.contentLayout.setBackgroundColor(Color.WHITE);
            setImageShow(holder.dynamicGiftIv, bean.getGiftImageUrl());
        }

        holder.prizeFlagIv.setVisibility(View.VISIBLE);
        if (bean.getGiftFlag() == null) {
            holder.prizeFlagIv.setVisibility(View.GONE);
        } else if ("WEEK".equals(bean.getGiftFlag())) {
            holder.prizeFlagIv.setImageResource(R.drawable.icon_week_star_gift);
        } else if ("PRIZE_POOL".equals(bean.getGiftFlag())) {
            holder.prizeFlagIv.setImageResource(R.drawable.icon_lucky_gift);
        }
        holder.giftDescTv.setText(bean.getName());
        Drawable rightDrawable = null;
        holder.giftValueTv.setText(new Money(bean.getPrice()).toSimpleString());
        if (PayType.GOLD_COIN.equals(currentPayType)) {
            rightDrawable = ContextCompat.getDrawable(context, R.drawable.icon_silver_gift_coin);
        } else if (PayType.GIFT_COIN.equals(currentPayType)) {
            rightDrawable = ContextCompat.getDrawable(context, R.drawable.icon_silver_money_coin);
        } else if (PayType.POINT_COIN.equals(currentPayType)) {
            rightDrawable = ContextCompat.getDrawable(context, R.drawable.icon_silver_banana_coin);
        }
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        holder.giftValueTv.setCompoundDrawables(null, null, rightDrawable, null);
    }


    static class ViewHolder {
        @BindView(R.id.dynamicGiftIv)
        SimpleDraweeView dynamicGiftIv;
        @BindView(R.id.prizeFlagIv)
        ImageView prizeFlagIv;
        @BindView(R.id.giftDescTv)
        TextView giftDescTv;
        @BindView(R.id.giftValueTv)
        TextView giftValueTv;
        @BindView(R.id.contentLayout)
        FrameLayout contentLayout;

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
