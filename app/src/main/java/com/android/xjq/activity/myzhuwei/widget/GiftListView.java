package com.android.xjq.activity.myzhuwei.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.groupchat.view.baselist.BaseListView;
import com.android.banana.groupchat.view.baselist.base.BaseViewHolder;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.xjq.R;
import com.android.xjq.bean.myzhuwei.ZhuweiDetailBean.OrderFundBillDetailBean;

/**
 * Created by kokuma on 2017/11/5.
 */

public class GiftListView extends BaseListView<OrderFundBillDetailBean> {
    private String prizeStatus;
    public String orderStatus;

    public GiftListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(R.layout.item_zhuwei_gift);
    }

    public GiftListView(Context context) {
        super(context);
        initUI(R.layout.item_zhuwei_gift);
    }

    public GiftListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(R.layout.item_zhuwei_gift);
    }

    public void setPrizeStatus(String prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    @Override
    protected void setConvert(BaseViewHolder holder, OrderFundBillDetailBean bean) {
        ImageView ivGift = holder.getView(R.id.ivGift);
        TextView tvCount = holder.getView(R.id.tvCount);
        TextView tvValue1 = holder.getView(R.id.tvValue1);
        TextView tvValue2 = holder.getView(R.id.tvValue2);
        TextView tvValue3 = holder.getView(R.id.tvValue3);
        //
        PicUtils.load(getContext(), ivGift, bean.getImageUrl());
        tvCount.setText("x" + bean.getPayCount());
        tvValue1.setText(MyZhuweiListView.getIntNum("" + (int) bean.getTotalPaidFee()));
        //只有派奖结束显示派奖金币以及礼金
        if ("PRIZE_FINISH".equals(prizeStatus)) {
            tvValue2.setText("" + bean.getPrizeGoldFee());
            tvValue3.setText("" +  bean.getPrizeGiftFee());
        }else {
            if(!TextUtils.isEmpty(orderStatus)&&orderStatus.equals("CANCEL_FINISH")){
                tvValue2.setText("-");
                tvValue3.setText("-");
            }else {
                tvValue2.setText("");
                tvValue3.setText("");
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //
    }
}
