package com.android.xjq.adapter.main;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.library.Utils.LibAppUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/6/8.
 */

public class RechargeMoneyShowAdapter extends MyBaseAdapter<String> {

    public static final int NORMAL_MONEY_SHOW = 0;

    public static final int CUSTOM_MONEY_SHOW = 1;

    private int currentPosition;

    private double pointCoinExchangeRate;

    private int customPostion = Integer.MAX_VALUE;

    private OnRechargeMoneyCountListener onRechargeMoneyCountListener;

    public RechargeMoneyShowAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public void setPointCoinExchangeRate(double pointCoinExchangeRate) {

        this.pointCoinExchangeRate = pointCoinExchangeRate;
    }


   /* @Override
    public int getCount() {
        return 6;
    }*/

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_recharge_gv, null);
        }

        final ViewHolder holder = ViewHolder.getTag(convertView);

        switch (getItemViewType(i)) {
            case CUSTOM_MONEY_SHOW:
                holder.customMoneyRl.setVisibility(View.VISIBLE);

                holder.pointTv.setVisibility(View.GONE);

                holder.moneyTv.setVisibility(View.GONE);

                holder.customMoneyEt.addTextChangedListener(customMoneyListener);

                customPostion = i;
                /*holder.customMoneyEt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = i;

                        notifyDataSetChanged();
                    }
                });*/
                break;

            case NORMAL_MONEY_SHOW:
                holder.customMoneyRl.setVisibility(View.GONE);

                holder.pointTv.setVisibility(View.VISIBLE);

                holder.moneyTv.setVisibility(View.VISIBLE);

                setItemView(holder, i);
                break;

        }

        changeItemBg(holder, i);

        if (currentPosition == customPostion) {
            holder.customMoneyLl.setVisibility(View.VISIBLE);

            holder.otherMoneyTv.setVisibility(View.GONE);

        } else {
            holder.customMoneyLl.setVisibility(View.GONE);

            holder.otherMoneyTv.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(holder.customMoneyEt.getText()) ||
                    Integer.valueOf(holder.customMoneyEt.getText().toString()) == 0) {

                holder.otherMoneyTv.setText("其他金额");
            } else {

                holder.otherMoneyTv.setText("¥" + holder.customMoneyEt.getText().toString());
            }

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = i;
                if (currentPosition == customPostion) {
                    LibAppUtil.showSoftKeyboard(context, holder.customMoneyEt);

                    if (TextUtils.isEmpty(holder.customMoneyEt.getText().toString())) {
                        onRechargeMoneyCountListener.onRechargeMoneyChange(0);
                    } else {
                        onRechargeMoneyCountListener.onRechargeMoneyChange(Integer.valueOf(holder.customMoneyEt.getEditableText().toString()));
                    }
                } else {
                    onRechargeMoneyCountListener.onRechargeMoneyChange(Double.valueOf(list.get(currentPosition)));
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
       /* if (position == 5) {
            return CUSTOM_MONEY_SHOW;
        } else {
            return NORMAL_MONEY_SHOW;
        }*/

        return NORMAL_MONEY_SHOW;
    }

    private void setItemView(ViewHolder holder, int position) {
        if (list == null || list.size() == 0) {
            return;
        }
        holder.moneyTv.setText("¥" + list.get(position));

        int pointCoin = (int) (Double.valueOf(list.get(position)) * pointCoinExchangeRate);

        holder.pointTv.setText(String.valueOf(pointCoin) + "香蕉币");
    }


    private void changeItemBg(ViewHolder holder, int i) {
        if (i == currentPosition) {

            holder.rechargeLayout.setBackgroundResource(R.drawable.shape_btn_red_border_white_solid);

            holder.moneyTv.setBackgroundResource(R.drawable.shape_recharge_checked);

            holder.moneyTv.setTextColor(Color.WHITE);

            holder.pointTv.setTextColor(Color.parseColor("#EC4432"));
        } else {
            holder.rechargeLayout.setBackgroundResource(R.drawable.shape_gray_solid);

            holder.moneyTv.setBackgroundResource(R.drawable.shape_recharge_unchecked);

            holder.moneyTv.setTextColor(Color.parseColor("#A1A1A1"));

            holder.pointTv.setTextColor(Color.BLACK);
        }
    }

    public interface OnRechargeMoneyCountListener {
        void onRechargeMoneyChange(double moneyCount);
    }

    public void setOnRechargeMoneyCountListener(OnRechargeMoneyCountListener onRechargeMoneyCountListener) {
        this.onRechargeMoneyCountListener = onRechargeMoneyCountListener;
    }

    private TextWatcher customMoneyListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (onRechargeMoneyCountListener == null) return;
            if (!TextUtils.isEmpty(s.toString())) {

                onRechargeMoneyCountListener.onRechargeMoneyChange(Integer.valueOf(s.toString()));
            } else {
                onRechargeMoneyCountListener.onRechargeMoneyChange(0);
            }

        }
    };

    static class ViewHolder {
        @BindView(R.id.pointTv)
        TextView pointTv;
        @BindView(R.id.moneyTv)
        TextView moneyTv;
        @BindView(R.id.otherMoneyTv)
        TextView otherMoneyTv;
        @BindView(R.id.customMoneyEt)
        EditText customMoneyEt;
        @BindView(R.id.customMoneyLl)
        LinearLayout customMoneyLl;
        @BindView(R.id.customMoneyRl)
        RelativeLayout customMoneyRl;
        @BindView(R.id.rechargeLayout)
        LinearLayout rechargeLayout;


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
