package com.android.xjq.adapter.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.MyListView;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.adapter.matchLive.MatchScheduleSubAdapter;
import com.android.xjq.bean.MatchScheduleBean;
import com.android.xjq.bean.matchschedule.ChannelAreaBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajiao on 2017/11/2 0002.
 */

public class MatchScheduleAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context mCtx;
    private List<MatchScheduleBean> mData = new ArrayList<>();
    public static final String RACESTATUS_FINISH = "FINISH";
    public static final String RACESTATUS_WAIT = "WAIT";
    private int mType;
    private onClickCallBack mListener;

    public MatchScheduleAdapter2(Context ctx) {
        mCtx = ctx;
    }

    public void setData(List<MatchScheduleBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public List<MatchScheduleBean> getData() {
        return mData;
    }

    public void setType(int type) {
        mType = type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item_match_schedule,  parent, false);
        MatchViewHolder holder = new MatchViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setViews(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    public int getListDataSize() {
        return mData.size();
    }


    private int safeParseColor(String color) {
        int colorInt = Color.BLACK;
        try {
            colorInt = Color.parseColor(color);
        } catch (Exception e) {
        }
        return colorInt;
    }

    private void setViews(RecyclerView.ViewHolder holder,final int pos) {
        MatchViewHolder viewHolder = (MatchViewHolder) holder;
        MatchScheduleBean bean = mData.get(pos);
        String time = bean.getGmtFormatedTime(bean.getGmtStart());
        String homeIconUrl = bean.getHomeIconUrl();
        String guestIconUrl = bean.getGuestIconUrl();
        LogUtils.d("yjj", "homeIconUrl = " + homeIconUrl + ", guestIconUrl = " + guestIconUrl);
        Picasso.with(mCtx).load(homeIconUrl).error(R.drawable.user_default_logo).into(viewHolder.HomeIconImage);
        Picasso.with(mCtx).load(guestIconUrl).error(R.drawable.user_default_logo).into(viewHolder.GuestIconImage);
        viewHolder.teamNameTxt.setText(bean.getMatchName());
        //viewHolder.teamNameTxt.setTextColor(safeParseColor(bean.getColor()));
        viewHolder.teamNameTxt.setTextColor(mCtx.getResources().getColor(R.color.black));
        viewHolder.homeNameTxt.setText(bean.getHomeTeamName());
        viewHolder.guestNameTxt.setText(bean.getGuestTeamName());
        viewHolder.matchTimeTxt.setText(time);
        int totalScore = bean.getFullHomeScore() + bean.getFullGuestScore();
        int differ = bean.getFullHomeScore() - bean.getFullGuestScore();
        //设置不在比赛的状态 POSTPONE("推迟", null), DELAY("延期", null),CUT("腰斩", null),BREAK_OFF("中断", null),
        //DEC("待定", null),CANCEL("取消", null), UNKNOWN("未知", null);
        if ("POSTPONE".equals(bean.getRaceStatus().getName()) || "DELAY".equals(bean.getRaceStatus().getName())
                || "CUT".equals(bean.getRaceStatus().getName()) || "BREAK_OFF".equals(bean.getRaceStatus().getName())
                || "DEC".equals(bean.getRaceStatus().getName()) || "CANCEL".equals(bean.getRaceStatus().getName())
                || "UNKNOWN".equals(bean.getRaceStatus().getName()) ) {
            viewHolder.vsLay.setVisibility(View.VISIBLE);
            viewHolder.vsStatus.setVisibility(View.VISIBLE);
            viewHolder.vsStatus.setText(bean.getRaceStatus().getMessage());
            viewHolder.ctrScoreLay.setVisibility(View.INVISIBLE);
        } else if (RACESTATUS_FINISH.equals(bean.getRaceStatus().getName())) {
            //if (totalScore < 0) {
            //} else {
            viewHolder.ctrScoreLay.setVisibility(View.VISIBLE);
            viewHolder.vsLay.setVisibility(View.GONE);
            viewHolder.percentTxt.setVisibility(View.VISIBLE);
            viewHolder.percentTxt.setTextColor(mCtx.getResources().getColor(R.color.black));
            viewHolder.percentTxt.setText(bean.getFullHomeScore() + ":" + bean.getFullGuestScore());
            viewHolder.scoreTxt.setText("总分" + totalScore);
            viewHolder.differTxt.setVisibility(View.VISIBLE);
            viewHolder.runTimeTxt.setVisibility(View.GONE);
            viewHolder.timeAnimImg.setVisibility(View.GONE);

            if (mType == 1) {
                viewHolder.scoreTxt.setVisibility(View.INVISIBLE);
                viewHolder.differTxt.setText("(" + bean.getHalfHomeScore() + ":" + bean.getHalfGuestScore() + ")");
            } else {
                viewHolder.differTxt.setText("分差" + differ);
                viewHolder.scoreTxt.setVisibility(View.VISIBLE);
            }
            //}
        } else if (RACESTATUS_WAIT.equals(bean.getRaceStatus().getName())) {
            viewHolder.vsLay.setVisibility(View.VISIBLE);
            if ("未赛".equals(bean.getRaceStatus().getMessage())) {
                viewHolder.vsStatus.setVisibility(View.GONE);
            } else {
                viewHolder.vsStatus.setVisibility(View.VISIBLE);
                viewHolder.vsStatus.setText(bean.getRaceStatus().getMessage());
            }
            viewHolder.ctrScoreLay.setVisibility(View.INVISIBLE);
        } else {
            int score = bean.getFullHomeScore() + bean.getFullGuestScore();
            AnimationDrawable animationDrawable = (AnimationDrawable)viewHolder.timeAnimImg.getBackground();
            animationDrawable.start();
            viewHolder.vsLay.setVisibility(View.GONE);
            viewHolder.ctrScoreLay.setVisibility(View.VISIBLE);
            viewHolder.scoreTxt.setVisibility(View.INVISIBLE);
            viewHolder.percentTxt.setVisibility(View.VISIBLE);
            viewHolder.percentTxt.setText(bean.getFullHomeScore() + ":" + bean.getFullGuestScore());
            viewHolder.percentTxt.setTextColor(mCtx.getResources().getColor(R.color.red_water_melon));
            viewHolder.runTimeTxt.setVisibility(View.VISIBLE);
            viewHolder.timeAnimImg.setVisibility(View.VISIBLE);
            if ("PAUSE".equals(bean.getRaceStatus().getName()) || "OVERTIME".equals(bean.getRaceStatus().getName())) {
                viewHolder.timeAnimImg.setVisibility(View.GONE);
            }

            //设置正在比赛的
            if (mType == 1) {
                String ftStr = "";
                //TODO 从服务端获取时间
                if ("PLAY_F".equals(bean.getRaceStatus().getName())) {           //上半场
                    int ftTime = Math.abs(TimeUtils.differMinutes(bean.getFst(), bean.getServerCurrentTime()));
                    ftStr = ftTime > 45 ? "45+" : String.valueOf(ftTime);
                    viewHolder.differTxt.setVisibility(View.GONE);
                } else if ("PLAY_S".equals(bean.getRaceStatus().getName())) {     //下半场
                    int ftTime = Math.abs(TimeUtils.differMinutes(bean.getSst(),  bean.getServerCurrentTime()));
                    ftStr = ftTime > 45 ? "90+" : String.valueOf(ftTime + 45);
                    viewHolder.differTxt.setVisibility(View.GONE);
                } else {
                    viewHolder.differTxt.setVisibility(View.VISIBLE);
                }

                viewHolder.differTxt.setText(bean.getRaceStatus().getMessage());
                viewHolder.runTimeTxt.setText(ftStr);
                viewHolder.timeAnimImg.setVisibility("".equals(ftStr) ? View.GONE : View.VISIBLE);
            } else {
                viewHolder.differTxt.setVisibility(View.INVISIBLE);
                viewHolder.runTimeTxt.setText(bean.getRaceStatus().getMessage() + " " + bean.getLeaveTime());
                viewHolder.timeAnimImg.setVisibility("".equals(bean.getLeaveTime()) ? View.GONE : View.VISIBLE);
            }

        }
        //设置足球的红黄牌
        if (!RACESTATUS_FINISH.equals(bean.getRaceStatus().getName()) && mType == 1) {
            int bhostRed = bean.getHr() == 0 ? View.INVISIBLE : View.VISIBLE;
            int bhostYellow = bean.getHy() == 0 ? View.INVISIBLE : View.VISIBLE;
            int bGuestRed = bean.getGr() == 0 ? View.INVISIBLE : View.VISIBLE;
            int bGuestYellow = bean.getGy() == 0 ? View.INVISIBLE : View.VISIBLE;
            viewHolder.homeYellowCardTxt.setVisibility(bhostYellow);
            viewHolder.guestYellowCardTxt.setVisibility(bGuestYellow);
            viewHolder.homeRedCardTxt.setVisibility(bhostRed);
            viewHolder.guestRedCardTxt.setVisibility(bGuestRed);
            if (bhostRed == View.VISIBLE) {
                viewHolder.homeRedCardTxt.setText(bean.getHr() + "");
            }
            if (bhostYellow == View.VISIBLE) {
                viewHolder.homeYellowCardTxt.setText(bean.getHy() + "");
            }
            if (bGuestRed == View.VISIBLE) {
                viewHolder.guestRedCardTxt.setText(bean.getGr() + "");
            }
            if (bGuestYellow == View.VISIBLE) {
                viewHolder.guestYellowCardTxt.setText(bean.getGy() + "");
            }

        } else {
            viewHolder.homeYellowCardTxt.setVisibility(View.GONE);
            viewHolder.guestYellowCardTxt.setVisibility(View.GONE);
            viewHolder.homeRedCardTxt.setVisibility(View.GONE);
            viewHolder.guestRedCardTxt.setVisibility(View.GONE);
        }
        if (bean.getChannelAreaBeanList() != null && bean.getChannelAreaBeanList().size() > 0) {
            List<ChannelAreaBean> subData = bean.getChannelAreaBeanList().size() >= 3 ? bean.getChannelAreaBeanList().subList(0,3) : bean.getChannelAreaBeanList();
            viewHolder.chnlDetailLay.setVisibility(View.VISIBLE);
            MatchScheduleSubAdapter subAdapter = new MatchScheduleSubAdapter(mCtx, subData);
            subAdapter.setOnChnlStatusClickListener(new MatchScheduleSubAdapter.onChnlStatusClickListener() {
                @Override
                public void onClickChnlStatus(ChannelAreaBean bean, int npos) {
                    if (mListener != null) {
                        if ("INIT".equals(bean.getStatus())) {
                            mListener.onClickChnlStatus(bean.isUserOrderChannelArea(), bean.getId(), npos, pos);
                        } else {
                            mListener.onGoToLive(bean.getId());
                        }
                    }
                }

                @Override
                public void onGoToLiveChnl(int channelId) {
                    if (mListener != null) {
                        mListener.onGoToLive(channelId);
                    }
                }
            });
            viewHolder.myListView.setAdapter(subAdapter);
            //subAdapter.notifyDataSetChanged();
        } else {
            viewHolder.chnlDetailLay.setVisibility(View.GONE);
        }
        viewHolder.showMoreTxt.setVisibility(bean.isMoreChannelArea() ? View.VISIBLE : View.GONE);
        viewHolder.showMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickMore(pos);
                }
            }
        });
        viewHolder.zhuWeiImage.setVisibility(!bean.isExistedOtherGameBoard() ? View.GONE : View.VISIBLE);
        int zhuWeiImgResId = !bean.isSaleCount() ? R.drawable.zhu_wei_gray : R.drawable.zhu_wei_colorful;
        viewHolder.zhuWeiImage.setImageDrawable(mCtx.getResources().getDrawable(zhuWeiImgResId));

        viewHolder.contentTopLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemContentClick(pos);
                }
            }
        });
        viewHolder.contentBottomLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemContentClick(pos);
                }
            }
        });
        viewHolder.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e( "onClick: ", "onItemClick >>>>");
            }
        });


    }

    public void clearList() {
        mData.clear();
        this.notifyDataSetChanged();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.schedule_content)
        LinearLayout scheduleContentLay;
        @BindView(R.id.team_name_txt)
        TextView teamNameTxt;
        @BindView(R.id.match_time_txt)
        TextView matchTimeTxt;
        @BindView(R.id.zhu_wei_image)
        ImageView zhuWeiImage;
        @BindView(R.id.home_yellow_card)
        TextView homeYellowCardTxt;
        @BindView(R.id.home_red_card)
        TextView homeRedCardTxt;
        @BindView(R.id.guest_yellow_card)
        TextView guestYellowCardTxt;
        @BindView(R.id.guest_red_card)
        TextView guestRedCardTxt;
        @BindView(R.id.home_icon)
        ImageView HomeIconImage;
        @BindView(R.id.guest_icon)
        ImageView GuestIconImage;
        @BindView(R.id.home_name_txt)
        TextView homeNameTxt;
        @BindView(R.id.guest_team_name)
        TextView guestNameTxt;
        @BindView(R.id.center_lay)
        RelativeLayout ctrScoreLay;
        @BindView(R.id.pk)
        TextView vsTxt;
        @BindView(R.id.vs_status)
        TextView vsStatus;
        @BindView(R.id.vs_linearLay)
        LinearLayout vsLay;
        @BindView(R.id.ctr_score)
        TextView scoreTxt;
        @BindView(R.id.ctr_percent)
        TextView percentTxt;
        @BindView(R.id.ctr_differ)
        TextView differTxt;
        @BindView(R.id.running_time_txt)
        TextView runTimeTxt;
        @BindView(R.id.time_anim_img)
        ImageView timeAnimImg;
        @BindView(R.id.match_schedule_item_lv)
        MyListView myListView;
        @BindView(R.id.channel_detail_lay)
        LinearLayout chnlDetailLay;
        @BindView(R.id.show_more_txt)
        TextView showMoreTxt;
        @BindView(R.id.content_top_lay)
        RelativeLayout contentTopLay;
        @BindView(R.id.content_bottom_lay)
        RelativeLayout contentBottomLay;

        MatchViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface onClickCallBack {
        public void onClickMore(int pos);
        public void onClickChnlStatus(boolean isUserOrderChannelArea, int id, int subPos, int pos);
        public void onItemContentClick(int pos);
        public void onGoToLive(int channelId);
    }

    public void setOnClickCallBack(onClickCallBack listener) {
        mListener = listener;
    }

}
