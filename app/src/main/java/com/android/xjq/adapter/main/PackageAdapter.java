package com.android.xjq.adapter.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.view.BadgeView;
import com.android.xjq.R;
import com.android.xjq.bean.TreasureList;
import com.android.xjq.dialog.GiftDialog;
import com.android.xjq.model.gift.PayType;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2018/3/12.
 */

public class PackageAdapter extends MyBaseAdapter<TreasureList.Treasure> {
    private int checkPosition = -1;

    private int prePosition = -1;

    private PayType currentPayType = PayType.GOLD_COIN;

    public void setCheckPosition(int checkPosition, int prePosition) {
        this.checkPosition = checkPosition;
        this.prePosition = prePosition;
    }

    private String packageType = GiftDialog.UserPackageItemType.GIFT;

    public void setCurrentPackageType(String packageType) {
        this.packageType = packageType;
    }

    public PackageAdapter(Context context, List<TreasureList.Treasure> list) {
        super(context, list);
    }

    private PackageAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PackageAdapter.OnItemClickListener onItemClickListener) {

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
        final PackageAdapter.ViewHolder holder = PackageAdapter.ViewHolder.getTag(convertView);
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


    public void itemUpdate(GridView gridView, int checkPosition) {
        if (gridView != null) {
            int start = gridView.getFirstVisiblePosition();
            for (int i = start, j = gridView.getLastVisiblePosition(); i <= j; i++)
                if (checkPosition == i) {
                    View view = gridView.getChildAt(i - start);
                    this.checkPosition = i;
                    if (0 <= prePosition && prePosition <= getCount() - 1) {
                        getView(prePosition, gridView.getChildAt(prePosition - start), gridView);
                    }
                    prePosition = i;
                    getView(i, view, gridView);
                    break;
                }
        }
    }


    private void setItemView(PackageAdapter.ViewHolder holder, int position) {
        if (list == null || list.size() == 0) return;
        TreasureList.Treasure bean = list.get(position);
        String content;
        String imageUrl;
        if (packageType == GiftDialog.UserPackageItemType.GIFT) {
            content = bean.giftName;
            imageUrl = bean.giftImageUrl;
        } else {
            content = bean.title;
            imageUrl = bean.imageUrl;
        }
        if (checkPosition == position) {
            setImageShow(holder.dynamicGiftIv, imageUrl, true);
            holder.contentLayout.setBackgroundResource(R.drawable.shape_gift_light_red_bg);
        } else {
            holder.contentLayout.setBackgroundColor(Color.WHITE);
            setImageShow(holder.dynamicGiftIv, imageUrl);
        }
        holder.badgeView.setBadgeNum((int) bean.currentTotalCount);
        holder.giftValueTv.setVisibility(View.GONE);
        holder.giftDescTv.setText(content);
        /*Drawable rightDrawable = null;
        //holder.giftValueTv.setText(new Money(bean.getPrice()).toSimpleString());
        if (PayType.GOLD_COIN.equals(currentPayType)) {
            rightDrawable = ContextCompat.getDrawable(context, R.drawable.icon_silver_money_coin);
        } else if (PayType.GIFT_COIN.equals(currentPayType)) {
            rightDrawable = ContextCompat.getDrawable(context, R.drawable.icon_silver_gift_coin);
        } else if (PayType.POINT_COIN.equals(currentPayType)) {
            rightDrawable = ContextCompat.getDrawable(context, R.drawable.icon_silver_banana_coin);
        }
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        holder.giftValueTv.setCompoundDrawables(null, null, rightDrawable, null);*/
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
        @BindView(R.id.badgeView)
        BadgeView badgeView;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

        public static PackageAdapter.ViewHolder getTag(View convertView) {
            PackageAdapter.ViewHolder holder = (PackageAdapter.ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new PackageAdapter.ViewHolder(convertView);
            }
            return holder;
        }
    }
}
