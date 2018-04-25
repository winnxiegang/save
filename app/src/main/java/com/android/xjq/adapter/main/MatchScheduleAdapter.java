package com.android.xjq.adapter.main;

/**
 * Created by ajiao on 2018\2\22 0022.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import com.android.xjq.bean.MatchScheduleBean;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MatchScheduleAdapter extends BaseAdapter {
    private Context mCtx;
    private List<MatchScheduleBean> mData = new ArrayList<>();
    public static final String RACESTATUS_FINISH = "FINISH";
    public static final String RACESTATUS_WAIT = "WAIT";
    private int mType;

    public MatchScheduleAdapter(Context ctx) {
        mCtx = ctx;
    }

    public void setData(List<MatchScheduleBean> data) {
        this.mData = data;
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        long t1 = System.currentTimeMillis();
        ViewHolder viewHolder = null;
        if (contentview == null) {
            contentview = LayoutInflater.from(mCtx).inflate(R.layout.content_match_schedule_listitem, null, false);
            viewHolder = new ViewHolder(contentview);
            contentview.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentview.getTag();
        }
        setViews(viewHolder, i);
        long t2 = System.currentTimeMillis();
        Log.e("getView: 耗时:" + i, (t2 - t1) + "");
        return contentview;
    }

    private int safeParseColor(String color) {
        int colorInt = Color.BLACK;
        try {
            colorInt = Color.parseColor(color);
        } catch (Exception e) {
        }
        return colorInt;
    }

    private void setViews(ViewHolder viewHolder, int pos) {
        MatchScheduleBean bean = mData.get(pos);
        String time = bean.getGmtFormatedTime(bean.getGmtStart());
        viewHolder.typeTxt.setText(bean.getMatchName());
        viewHolder.typeTxt.setTextColor(safeParseColor(bean.getColor()));
        viewHolder.leftNameTxt.setText(bean.getHomeTeamName());
        viewHolder.rightNameTxt.setText(bean.getGuestTeamName());
        viewHolder.leftTimeTxt.setText(time);
        int totalScore = bean.getFullHomeScore() + bean.getFullGuestScore();
        int differ = bean.getFullHomeScore() - bean.getFullGuestScore();
        //设置不在比赛的状态 POSTPONE("推迟", null), DELAY("延期", null),CUT("腰斩", null),BREAK_OFF("中断", null),
        //DEC("待定", null),CANCEL("取消", null), UNKNOWN("未知", null);
        if ("POSTPONE".equals(bean.getRaceStatus().getName()) || "DELAY".equals(bean.getRaceStatus().getName())
                || "CUT".equals(bean.getRaceStatus().getName()) || "BREAK_OFF".equals(bean.getRaceStatus().getName())
                || "DEC".equals(bean.getRaceStatus().getName()) || "CANCEL".equals(bean.getRaceStatus().getName())
                || "UNKNOWN".equals(bean.getRaceStatus().getName()) ) {
            viewHolder.vsLay.setVisibility(View.VISIBLE);
            viewHolder.vsTxt.setVisibility(View.VISIBLE);
            viewHolder.vsStatus.setVisibility(View.VISIBLE);
            viewHolder.vsStatus.setText(bean.getRaceStatus().getMessage());
            viewHolder.scoreTxt.setVisibility(View.INVISIBLE);
            viewHolder.differTxt.setVisibility(View.GONE);
            viewHolder.percentTxt.setVisibility(View.GONE);
            viewHolder.leaveTime.setVisibility(View.INVISIBLE);
            viewHolder.timeAnimImg.setVisibility(View.GONE);
        } else if (RACESTATUS_FINISH.equals(bean.getRaceStatus().getName())) {
            //if (totalScore < 0) {
            //} else {
            viewHolder.vsLay.setVisibility(View.GONE);
            viewHolder.percentTxt.setVisibility(View.VISIBLE);
            viewHolder.percentTxt.setTextColor(mCtx.getResources().getColor(R.color.black));
            viewHolder.percentTxt.setText(bean.getFullHomeScore() + "-" + bean.getFullGuestScore());
            viewHolder.scoreTxt.setText("总分" + totalScore);
            viewHolder.differTxt.setVisibility(View.VISIBLE);
            viewHolder.leaveTime.setVisibility(View.INVISIBLE);
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
            viewHolder.vsTxt.setVisibility(View.VISIBLE);
            if ("未赛".equals(bean.getRaceStatus().getMessage())) {
                viewHolder.vsStatus.setVisibility(View.GONE);
            } else {
                viewHolder.vsStatus.setVisibility(View.VISIBLE);
                viewHolder.vsStatus.setText(bean.getRaceStatus().getMessage());
            }
            viewHolder.scoreTxt.setVisibility(View.INVISIBLE);
            viewHolder.differTxt.setVisibility(View.GONE);
            viewHolder.percentTxt.setVisibility(View.GONE);
            viewHolder.leaveTime.setVisibility(View.INVISIBLE);
            viewHolder.timeAnimImg.setVisibility(View.GONE);
        } else {
            int score = bean.getFullHomeScore() + bean.getFullGuestScore();
            viewHolder.timeAnimImg.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable)viewHolder.timeAnimImg.getBackground();
            animationDrawable.start();
            viewHolder.vsLay.setVisibility(View.GONE);
            viewHolder.scoreTxt.setVisibility(View.INVISIBLE);
            viewHolder.percentTxt.setVisibility(View.VISIBLE);
            viewHolder.percentTxt.setText(bean.getFullHomeScore() + "-" + bean.getFullGuestScore());
            viewHolder.percentTxt.setTextColor(mCtx.getResources().getColor(R.color.pie_green));
            if ("PAUSE".equals(bean.getRaceStatus().getName()) || "OVERTIME".equals(bean.getRaceStatus().getName())) {
                viewHolder.timeAnimImg.setVisibility(View.GONE);
            }
            viewHolder.leaveTime.setVisibility(View.VISIBLE);

            //设置正在比赛的
            if (mType == 1) {
                String ftStr = "";
                //TODO 从服务端获取时间
                if ("PLAY_F".equals(bean.getRaceStatus().getName())) {           //上半场
                    int ftTime = Math.abs(TimeUtils.differMinutes(bean.getFst(), bean.getServerCurrentTime()));
                    ftStr = ftTime > 45 ? "45+" : String.valueOf(ftTime);
                } else if ("PLAY_S".equals(bean.getRaceStatus().getName())) {     //下半场
                    int ftTime = Math.abs(TimeUtils.differMinutes(bean.getSst(),  bean.getServerCurrentTime()));
                    ftStr = ftTime > 45 ? "90+" : String.valueOf(ftTime + 45);
                }
                viewHolder.differTxt.setVisibility(View.VISIBLE);
                viewHolder.differTxt.setText(bean.getRaceStatus().getMessage());
                viewHolder.leaveTime.setText(ftStr);
            } else {
                viewHolder.differTxt.setVisibility(View.INVISIBLE);
                viewHolder.leaveTime.setText(bean.getRaceStatus().getMessage() + " " + bean.getLeaveTime());
            }

        }
        //设置足球的红黄牌
        if (mType == 1) {
            int bhostRed = bean.getHr() == 0 ? View.INVISIBLE : View.VISIBLE;
            int bhostYellow = bean.getHy() == 0 ? View.INVISIBLE : View.VISIBLE;
            int bGuestRed = bean.getGr() == 0 ? View.INVISIBLE : View.VISIBLE;
            int bGuestYellow = bean.getGy() == 0 ? View.INVISIBLE : View.VISIBLE;
            viewHolder.hyellow.setVisibility(bhostYellow);
            viewHolder.gyellow.setVisibility(bGuestYellow);
            viewHolder.hred.setVisibility(bhostRed);
            viewHolder.gred.setVisibility(bGuestRed);
            if (bhostRed == View.VISIBLE) {
                viewHolder.hred.setText(bean.getHr() + "");
            }
            if (bhostYellow == View.VISIBLE) {
                viewHolder.hyellow.setText(bean.getHy() + "");
            }
            if (bGuestRed == View.VISIBLE) {
                viewHolder.gred.setText(bean.getGr() + "");
            }
            if (bGuestYellow == View.VISIBLE) {
                viewHolder.gyellow.setText(bean.getGy() + "");
            }

        } else {
            viewHolder.hyellow.setVisibility(View.GONE);
            viewHolder.gyellow.setVisibility(View.GONE);
            viewHolder.hred.setVisibility(View.GONE);
            viewHolder.gred.setVisibility(View.GONE);
        }

    }

    static class ViewHolder {
        @BindView(R.id.type)
        TextView typeTxt;
        @BindView(R.id.left_name)
        TextView leftNameTxt;
        @BindView(R.id.left_time)
        TextView leftTimeTxt;
        @BindView(R.id.right_time)
        TextView leaveTime;
        @BindView(R.id.right_name)
        TextView rightNameTxt;
        @BindView(R.id.ctr_score)
        TextView scoreTxt;
        @BindView(R.id.ctr_percent)
        TextView percentTxt;
        @BindView(R.id.ctr_differ)
        TextView differTxt;
        @BindView(R.id.pk)
        TextView vsTxt;
        @BindView(R.id.center_lay)
        LinearLayout ctrLay;
        @BindView(R.id.right_lay)
        LinearLayout rightLay;
        @BindView(R.id.hyellow)
        TextView hyellow;
        @BindView(R.id.hred)
        TextView hred;
        @BindView(R.id.gred)
        TextView gred;
        @BindView(R.id.gyellow)
        TextView gyellow;
        @BindView(R.id.vs_linearLay)
        LinearLayout vsLay;
        @BindView(R.id.vs_status)
        TextView vsStatus;
        @BindView(R.id.time_anim_img)
        ImageView timeAnimImg;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

    }
}
