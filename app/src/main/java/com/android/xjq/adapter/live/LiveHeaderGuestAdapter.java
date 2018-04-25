package com.android.xjq.adapter.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.xjq.R;
import com.android.xjq.bean.live.ChannelUserBean;
import com.android.xjq.dialog.live.PersonalInfoDialog;

import java.util.List;

/**
 * Created by lingjiu on 2018/1/30.
 */

public class LiveHeaderGuestAdapter extends RecyclerView.Adapter<LiveHeaderGuestAdapter.ViewHolder> {
    private List<ChannelUserBean> mList;
    private Context mContext;

    public LiveHeaderGuestAdapter(Context context, List<ChannelUserBean> list) {
        mList = list;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_live_header_guest_recyclerview, null);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        //Picasso.with(mContext).load(mList.get(position)).into(holder.portraitIv);
        final ChannelUserBean bean = mList.get(position);
        PicUtils.load(mContext.getApplicationContext(), holder.portraitIv, bean.userLogoUrl, R.drawable.user_default_logo);

        holder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PersonalInfoDialog(mContext, bean.userId).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView portraitIv;

        public ViewHolder(View itemView) {
            super(itemView);
            portraitIv = (ImageView) itemView.findViewById(R.id.portraitIv);
        }
    }

}
