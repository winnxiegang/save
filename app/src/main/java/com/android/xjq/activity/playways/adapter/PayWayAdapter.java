package com.android.xjq.activity.playways.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjq.R;
import com.android.xjq.bean.payway.CheerBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PayWayAdapter extends RecyclerView.Adapter<PayWayAdapter.ViewHolder>{
    private List<CheerBean> mListData = new ArrayList<>();
    private Context mCtx;

    public PayWayAdapter(Context ctx, List<CheerBean> data) {
        mListData = data;
        mCtx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item_pay_way_cheer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(view);
        }

    }


}
