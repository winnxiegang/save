package com.android.xjq.adapter.schduledatail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.JclqRankingBean;
import com.android.xjq.bean.scheduledetail.JclqRankingInfoBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouyi on 2016/7/8 15:43.
 */
public class JclqAnalysisRankAdapter extends MyBaseAdapter<JclqRankingBean> {

    public JclqAnalysisRankAdapter(Context context, List<JclqRankingBean> list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        if (list.size() == 0) {
            return 1;
        } else {
            return list.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_jclq_analysis_ranking, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        setItemView(position, holder);

        return convertView;
    }

    private void setItemView(int position, ViewHolder holder) {

        if (list.size() == 0) {
            setAllEmptyView(holder);
            return;
        } else {
            holder.recordEmptyView.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
        }

        JclqRankingBean bean = list.get(position);

        if (holder.homeTeamDataLayout.getChildCount() > 0) {
            holder.homeTeamDataLayout.removeAllViews();
        }
        if (holder.guestTeamDataLayout.getChildCount() > 0) {
            holder.guestTeamDataLayout.removeAllViews();
        }

        if (bean.getAllRankList() != null) {
            holder.homeTeamTitleTv.setText(bean.getHomeTitle());
            setRankView(bean.getAllRankList(), holder.homeTeamDataLayout);
            holder.guestTeamLayout.setVisibility(View.GONE);
        }

        if (bean.getHomeRankList() != null) {

            holder.homeTeamTitleTv.setText(bean.getHomeTitle());

            setRankView(bean.getHomeRankList(), holder.homeTeamDataLayout);
        } else {
            if (bean.getAllRankList() == null) {

                setEmptyView(holder.homeTeamTitleTv, holder.homeTeamDataLayout);
            }

        }

        if (bean.getGuestRankList() != null) {

            holder.guestTeamTitleTv.setText(bean.getGuestTitle());

            setRankView(bean.getGuestRankList(), holder.guestTeamDataLayout);
        } else {
            if (bean.getAllRankList() == null) {

                setEmptyView(holder.guestTeamTitleTv, holder.guestTeamDataLayout);
            }
        }


        setVisibleOrGoneListener(holder.homeTeamArrowIv, holder.homeTeamDataLayout);

        setVisibleOrGoneListener(holder.guestTeamArrowIv, holder.guestTeamDataLayout);
    }

    private void setEmptyView(TextView titleTv, LinearLayout layout) {
        titleTv.setText("赛季排名");
        layout.addView(addRankCountTitleView());
        layout.addView(getEmptyView());
    }

    private void setAllEmptyView(ViewHolder holder) {
        holder.homeTeamTitleTv.setText("赛季排名");
        holder.homeTeamDataLayout.addView(addRankCountTitleView());
        holder.homeTeamDataLayout.addView(getEmptyView());
        holder.guestTeamTitleTv.setText("赛季排名");
        holder.guestTeamDataLayout.addView(addRankCountTitleView());
        holder.guestTeamDataLayout.addView(getEmptyView());
        setVisibleOrGoneListener(holder.homeTeamArrowIv, holder.homeTeamDataLayout);
        setVisibleOrGoneListener(holder.guestTeamArrowIv, holder.guestTeamDataLayout);
    }


    private void setRankView(List<JclqRankingInfoBean> list, LinearLayout layout) {
        if (list.size() > 0) {
            layout.addView(addRankCountTitleView());
        } else {
            layout.addView(getCutLine());
            layout.addView(addRankCountTitleView());
            layout.addView(getEmptyView());
        }
        for (JclqRankingInfoBean rankInfoBean : list) {
            layout.addView(addRankDataView(rankInfoBean));
            layout.addView(getCutLine());
        }
    }

    private View getEmptyView() {
        TextView textView = new TextView(context);
        textView.setText("暂无数据");
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dp50));
        textView.setLayoutParams(llp);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public void setVisibleOrGoneListener(final ImageView titleLayout, final View contentLayout) {
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentLayout.getVisibility() == View.GONE) {
                    titleLayout.setImageResource(R.drawable.points_up);
                    contentLayout.setVisibility(View.VISIBLE);
                } else {
                    titleLayout.setImageResource(R.drawable.point_down);
                    contentLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private View addRankDataView(JclqRankingInfoBean bean) {

        String[] methodName = new String[]{"TeamRank",
                "TeamName",
                "MatchCount",
                "MatchWin",
                "MatchLost",
                "InScoreAve",
                "LostScoreAve",
                "GetScoreAve",
                "HoldCountStr"};

        LinearLayout contentLayout = new LinearLayout(context);

        int height = (int) context.getResources().getDimension(R.dimen.dp40);

        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        contentLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < methodName.length; i++) {

            TextView tv = new TextView(context);

            LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

            if (i == 0) {
                llps.weight = 1f;
            } else if (i == methodName.length - 1) {
                llps.weight = 1.2f;
                if (bean.getHoldCount() > 0) {
                    tv.setTextColor(context.getResources().getColor(R.color.main_red));
                } else {
                    tv.setTextColor(Color.parseColor("#54b545"));
                }
            } else if (i == 1) {
                llps.weight = 1.5f;
            } else {
                llps.weight = 1f;
            }

            tv.setLayoutParams(llps);

            tv.setGravity(Gravity.CENTER);

            tv.setMaxLines(1);

            try {
                if (bean == null) {
                    tv.setText("-");
                } else {
                    tv.setText(String.valueOf(JclqRankingInfoBean.class.getMethod("get" + methodName[i]).invoke(bean)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            contentLayout.addView(tv);
        }

        if (bean.isShowGuest()) {
            contentLayout.setBackgroundColor(context.getResources().getColor(R.color.jclq_show_guest_color));
        }

        if (bean.isShowHome()) {
            contentLayout.setBackgroundColor(context.getResources().getColor(R.color.jclq_show_home_color));
        }

        return contentLayout;

    }

    private View getCutLine() {

        View view = new View(context);

        int height = (int) context.getResources().getDimension(R.dimen.dpPoint5);

        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        view.setBackgroundColor(context.getResources().getColor(R.color.line_color));

        return view;

    }

    private View addRankCountTitleView() {

        String[] titles = new String[]{"排名", "球队", "赛", "胜", "负", "得", "失", "净", "连"};

        LinearLayout titleLayout = new LinearLayout(context);

        int height = (int) context.getResources().getDimension(R.dimen.dp40);

        titleLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        titleLayout.setOrientation(LinearLayout.HORIZONTAL);

        titleLayout.setBackgroundColor(Color.parseColor("#e2e2e2"));

        for (int i = 0; i < titles.length; i++) {
            TextView tv = new TextView(context);
            LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                llps.weight = 1f;
            } else if (i == titles.length - 1) {
                llps.weight = 1.2f;
            } else if (i == 1) {
                llps.weight = 1.5f;
            } else {
                llps.weight = 1f;
            }
            tv.setLayoutParams(llps);
            tv.setText(titles[i]);
            tv.setGravity(Gravity.CENTER);
            titleLayout.addView(tv);
        }

        return titleLayout;

    }



    static  class ViewHolder {
        @BindView(R.id.homeTeamTitleTv)
        TextView homeTeamTitleTv;
        @BindView(R.id.homeTeamArrowIv)
        ImageView homeTeamArrowIv;
        @BindView(R.id.homeTeamLayout)
        RelativeLayout homeTeamLayout;
        @BindView(R.id.homeTeamDataLayout)
        LinearLayout homeTeamDataLayout;
        @BindView(R.id.guestTeamTitleTv)
        TextView guestTeamTitleTv;
        @BindView(R.id.guestTeamArrowIv)
        ImageView guestTeamArrowIv;
        @BindView(R.id.guestTeamLayout)
        RelativeLayout guestTeamLayout;
        @BindView(R.id.guestTeamDataLayout)
        LinearLayout guestTeamDataLayout;
        @BindView(R.id.contentLayout)
        LinearLayout contentLayout;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.emptyTipTv)
        TextView emptyTipTv;
        @BindView(R.id.recordEmptyView)
        LinearLayout recordEmptyView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
