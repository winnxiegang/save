package com.android.xjq.activity.program.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjq.R;
import com.android.xjq.bean.program.ProgramEntityBean;

import java.util.List;

import butterknife.ButterKnife;



public class ProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mIsDetailShow;
    private List<ProgramEntityBean.ChannelProgramBean> mData;
    private Context mCtx;
    private onProgramStatusClickListener mListener;

    public ProgramAdapter(Context ctx, List<ProgramEntityBean.ChannelProgramBean> data) {
        mData = data;
        mCtx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item_program,  parent, false);
        ProgramViewHolder viewHolder = new ProgramViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setType(boolean isDetailShow) {
        mIsDetailShow = isDetailShow;
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {


        public ProgramViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface onProgramStatusClickListener {
        public void onProgramStatusClick(boolean isUserOrderChannelArea, long channelAreaId, int pos);
        public void onGuestPileLayoutClick(long channelAreaId);
    }

    public void setOnProgramStatusClickListener(onProgramStatusClickListener listener) {
        mListener = listener;
    }


}
