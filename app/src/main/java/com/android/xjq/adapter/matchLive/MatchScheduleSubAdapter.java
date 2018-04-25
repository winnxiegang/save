package com.android.xjq.adapter.matchLive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.R;
import com.android.xjq.activity.program.ProgramListFragment;
import com.android.xjq.bean.matchschedule.ChannelAreaBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajiao on 2018\2\26 0026.
 */

public class MatchScheduleSubAdapter extends BaseAdapter {

    private List<ChannelAreaBean> mData;
    private Context mCtx;
    private String mLastTime = "";
    private onChnlStatusClickListener mListener;

    public MatchScheduleSubAdapter(Context ctx, List<ChannelAreaBean> data) {
        mCtx = ctx;
        mData = data;
    }


    @Override
    public int getCount() {
        return mData == null? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null? 0 : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.item_my_list_view_match_schedual, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        setViews(viewHolder);
        return convertView;
    }

    //最多只显示3条节目单
    private void setViews(final ViewHolder viewHolder) {
        if (viewHolder.position == 0) {mLastTime = "";}
        final ChannelAreaBean channelAreaBean = mData.get(viewHolder.position);
        String day = TimeUtils.getYueRuiStr2(channelAreaBean.getGmtStart());
        String hourMin = TimeUtils.getHourMinute(channelAreaBean.getGmtStart());
        if (!mLastTime.equals(day)) {
            if (day.equals("今天")) {
                viewHolder.dotImg.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.icon_blue_dot));
                viewHolder.dayTimeTxt.setTextColor(mCtx.getResources().getColor(R.color.light_blue4));
            } else {
                viewHolder.dotImg.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.icon_gray_dot));
                viewHolder.dayTimeTxt.setTextColor(mCtx.getResources().getColor(R.color.gray_text_color));
            }
            viewHolder.dotImg.setVisibility(View.VISIBLE);
            viewHolder.dayTimeTxt.setVisibility(View.VISIBLE);
            viewHolder.dayTimeTxt.setText(day);
        } else {
            viewHolder.dayTimeTxt.setVisibility(View.INVISIBLE);
            viewHolder.dotImg.setVisibility(View.INVISIBLE);
        }
        viewHolder.hourMinTxt.setText(hourMin);
        String title = channelAreaBean.getAreaTitle().length() > 10 ? channelAreaBean.getAreaTitle().substring(0, 10) + "..." : channelAreaBean.getAreaTitle();
        viewHolder.contentTxt.setText(title);
        ProgramListFragment.setProgramStatus(viewHolder.statusTxt, channelAreaBean.getStatus(), channelAreaBean.isUserOrderChannelArea(), mCtx, false);
        viewHolder.statusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Log.e( "onClick: ", "statusTxt >>>>" + viewHolder.position + "");
                    //mListener.onClickChnlStatus(mData.get(viewHolder.position).isUserOrderChannelArea(), mData.get(viewHolder.position).getId(), viewHolder.position);
                    mListener.onClickChnlStatus(mData.get(viewHolder.position), viewHolder.position);
                }
            }
        });

        viewHolder.chnlItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Log.e( "onClick: ", "chnlItemLay >>>>" + viewHolder.position + "");
                    mListener.onGoToLiveChnl(mData.get(viewHolder.position).getId());
                }
            }
        });

        mLastTime = day;
    }
    public interface onChnlStatusClickListener {
        public void onClickChnlStatus(ChannelAreaBean bean, int pos);
        public void onGoToLiveChnl(int channelId);
    }

    public void setOnChnlStatusClickListener (onChnlStatusClickListener listener) {
        mListener = listener;
    }

    static class ViewHolder {

        @BindView(R.id.channel_item_lay)
        LinearLayout chnlItemLay;
        @BindView(R.id.ivDot)
        ImageView dotImg;
        @BindView(R.id.day_time_txt)
        TextView dayTimeTxt;
        @BindView(R.id.hour_min_time)
        TextView hourMinTxt;
        @BindView(R.id.context_txt)
        TextView contentTxt;
        @BindView(R.id.status_txt)
        TextView statusTxt;
        public int position;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
