package com.android.xjq.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.activity.accountdeatils.AccountDetailsActivity;
import com.android.xjq.adapter.main.MyBaseAdapter;
import com.android.xjq.bean.accountdetails.AccountBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 账务明细适配器
 *
 * @author leslie
 * @version $Id: FinanceAdapter.java, v 0.1 2014年7月14日 上午11:57:04 leslie Exp $
 */
public class AccountDetailsAdapter extends MyBaseAdapter<AccountBean.AccountLogListBean> {

    private int currentSelect = AccountDetailsActivity.GOLD_DETAIL;

    public void setCurrentSelect(int currentSelect) {
        this.currentSelect = currentSelect;
    }

    public AccountDetailsAdapter(Context context, List<AccountBean.AccountLogListBean> list) {
        super(context, list);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_my_account, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, list.get(position));

        return convertView;
    }

    private void setData(ViewHolder holder, AccountBean.AccountLogListBean data) {

        holder.gmtCreateTv.setText(data.getGmtTrans());

        String type = "";

        switch (currentSelect){
            case 0:
                type = "金币";
                break;
            case 1:
                type = "礼金";
                break;
            case 2:
                type = "香蕉币";
                break;
        }
        holder.balanceAfterTv.setText(type+"余额 "+data.getAvailableAfter().toString());

        holder.memoTv.setText(data.getMemo());

        holder.subTransTv.setText(data.getSubTransName());

        if("I".equals(data.getTransDirection().getName())){
            holder.transMoneyTv.setTextColor(context.getResources().getColor(R.color.main_red));
            holder.transMoneyTv.setText("+"+data.getTransAmount().toString());
        }else{
            holder.transMoneyTv.setTextColor(context.getResources().getColor(R.color.main_black_text_color));
            holder.transMoneyTv.setText("-"+data.getTransAmount().toString());
        }

    }

    static class ViewHolder {
        @BindView(R.id.gmtCreateTv)
        TextView gmtCreateTv;
        @BindView(R.id.transMoneyTv)
        TextView transMoneyTv;
        @BindView(R.id.balanceAfterTv)
        TextView balanceAfterTv;
        @BindView(R.id.memoTv)
        TextView memoTv;
        @BindView(R.id.subTransTv)
        TextView subTransTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}